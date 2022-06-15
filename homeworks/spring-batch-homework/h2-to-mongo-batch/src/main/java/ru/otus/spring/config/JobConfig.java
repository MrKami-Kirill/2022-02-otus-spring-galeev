package ru.otus.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.NonNull;
import ru.otus.spring.model.mongo.AuthorMongo;
import ru.otus.spring.model.mongo.BookMongo;
import ru.otus.spring.model.mongo.CommentMongo;
import ru.otus.spring.model.mongo.GenreMongo;
import ru.otus.spring.model.h2.BookH2;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class JobConfig {
    public static final String IMPORT_BOOK_JOB_NAME = "importBookJob";
    private static final int CHUNK_SIZE = 5;
    private final Logger logger = LoggerFactory.getLogger(JobConfig.class);

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    public JobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<BookH2> reader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<BookH2>()
                .name("BookReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT b FROM BookH2 b")
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<BookH2, BookMongo> processor() {
        return book -> {
            String name = book.getName();
            Set<CommentMongo> commentMongoSet = book.getComments().stream().map(CommentMongo::convertComment).collect(Collectors.toSet());
            Set<AuthorMongo> authorMongoSet = book.getAuthors().stream().map(AuthorMongo::convertAuthor).collect(Collectors.toSet());
            Set<GenreMongo> genreMongoSet = book.getGenres().stream().map(GenreMongo::convertGenre).collect(Collectors.toSet());
            return new BookMongo(name, commentMongoSet, authorMongoSet, genreMongoSet);
        };
    }

    @StepScope
    @Bean
    public MongoItemWriter<BookMongo> writer(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<BookMongo>()
                .collection("books")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public Job importBookJob(Step transformBooksStep) {
        return jobBuilderFactory.get(IMPORT_BOOK_JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .flow(transformBooksStep)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        logger.info("---------------------->Начало job");
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        logger.info("<----------------------Конец job");
                    }
                })
                .build();
    }

    @Bean
    public Step transformBooksStep(JpaPagingItemReader<BookH2> reader, MongoItemWriter<BookMongo> writer,
                                   ItemProcessor<BookH2, BookMongo> itemProcessor) {
        return stepBuilderFactory.get("step1")
                .<BookH2, BookMongo>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(new ItemReadListener<>() {
                    public void beforeRead() {
                        logger.info("Начало чтения");
                    }

                    public void afterRead(@NonNull BookH2 book) {
                        logger.info("Конец чтения книги \"" + book.getName() + "\"");
                    }

                    public void onReadError(@NonNull Exception e) {
                        logger.info("Ошибка чтения", e);
                    }
                })
                .listener(new ItemProcessListener<>() {
                    public void beforeProcess(@NonNull BookH2 book) {
                        logger.info("Начало обработки книги \"" + book.getName() + "\"");
                    }

                    public void afterProcess(@NonNull BookH2 bookH2, BookMongo bookMongo) {
                        logger.info("Конец обработки книги \"" + bookH2.getName() + "\"");
                    }

                    public void onProcessError(@NonNull BookH2 book, @NonNull Exception e) {
                        logger.info("Ошибка обработки книги \"" + book.getName() + "\"", e);
                    }
                })
                .listener(new ItemWriteListener<>() {
                    public void beforeWrite(@NonNull List list) {
                        logger.info("Начало записи");
                    }
                    public void afterWrite(@NonNull List list) {
                        logger.info("Конец записи");
                    }

                    public void onWriteError(@NonNull Exception e, @NonNull List list) {
                        logger.info("Ошибка записи", e);
                    }
                })
                .listener(new ChunkListener() {
                    public void beforeChunk(@NonNull ChunkContext chunkContext) {
                        logger.info("Начало пачки");
                    }

                    public void afterChunk(@NonNull ChunkContext chunkContext) {
                        logger.info("Конец пачки");
                    }

                    public void afterChunkError(@NonNull ChunkContext chunkContext) {
                        logger.info("Ошибка пачки");
                    }
                })
                .build();
    }


}
