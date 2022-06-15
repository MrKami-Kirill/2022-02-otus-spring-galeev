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
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.NonNull;
import ru.otus.spring.config.component.ListDelegateWriter;
import ru.otus.spring.exception.NotFoundException;
import ru.otus.spring.model.h2.AuthorH2;
import ru.otus.spring.model.h2.BookH2;
import ru.otus.spring.model.h2.CommentH2;
import ru.otus.spring.model.h2.GenreH2;
import ru.otus.spring.model.mongo.AuthorMongo;
import ru.otus.spring.model.mongo.BookMongo;
import ru.otus.spring.model.mongo.GenreMongo;
import ru.otus.spring.repository.h2.AuthorH2Repository;
import ru.otus.spring.repository.h2.BookH2Repository;
import ru.otus.spring.repository.h2.GenreH2Repository;

import javax.persistence.EntityManagerFactory;
import java.util.*;

@Configuration
public class JobConfig {
    public static final String IMPORT_BOOK_JOB_NAME = "importBookJob";
    private static final int CHUNK_SIZE = 5;
    private final Logger logger = LoggerFactory.getLogger(JobConfig.class);

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final AuthorH2Repository authorH2Repository;
    private final GenreH2Repository genreH2Repository;
    private final BookH2Repository bookH2Repository;

    private List<String> authorNameList = new ArrayList<>();
    private List<String> genreNameList = new ArrayList<>();

    @Autowired
    public JobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, AuthorH2Repository authorH2Repository, GenreH2Repository genreH2Repository, BookH2Repository bookH2Repository) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.authorH2Repository = authorH2Repository;
        this.genreH2Repository = genreH2Repository;
        this.bookH2Repository = bookH2Repository;
    }

    @StepScope
    @Bean
    public MongoItemReader<BookMongo> reader(MongoTemplate mongoTemplate) {
        return new MongoItemReaderBuilder<BookMongo>()
                .name("BookReader")
                .template(mongoTemplate)
                .jsonQuery("{}")
                .targetType(BookMongo.class)
                .sorts(Collections.EMPTY_MAP)
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<BookMongo, List<AuthorH2>> authorsProcessor() {
        return book -> {
            List<AuthorH2> authors = new ArrayList<>();
            book.getAuthors().forEach(author -> {
                String name = author.getName();
                if (!authorNameList.contains(name)) {
                    authors.add(new AuthorH2(name));
                    authorNameList.add(name);
                }
            });

            return authors;
        };
    }

    @StepScope
    @Bean
    public ItemProcessor<BookMongo, List<GenreH2>> genresProcessor() {
        return book -> {
            List<GenreH2> genres = new ArrayList<>();
            book.getGenres().forEach(genre -> {
                String name = genre.getName();
                if (!genreNameList.contains(name)) {
                    genres.add(new GenreH2(name));
                    genreNameList.add(name);
                }
            });

            return genres;
        };
    }

    @StepScope
    @Bean
    public ItemProcessor<BookMongo, BookH2> booksProcessor() {
        return book -> new BookH2(book.getName(),
               authorH2Repository.findAllAuthorsByNameList(book.getAuthors().stream().map(AuthorMongo::getName).toList()),
               genreH2Repository.findAllGenresByNameList(book.getGenres().stream().map(GenreMongo::getName).toList())
        );
    }

    @StepScope
    @Bean
    public ItemProcessor<BookMongo, List<CommentH2>> commentsProcessor() {
        return book -> {
            List<CommentH2> comments = new ArrayList<>();
            BookH2 bookH2 = bookH2Repository.findByName(book.getName()).orElseThrow(() -> new NotFoundException("Книга в базе H2 не найдена"));
            book.getComments().forEach(comment -> comments.add(new CommentH2(comment.getText(), bookH2)));
            return comments;
        };
    }

    @StepScope
    @Bean
    public ListDelegateWriter<AuthorH2> authorsWriter(EntityManagerFactory entityManagerFactory) {
        return new ListDelegateWriter<>(new JpaItemWriterBuilder<AuthorH2>()
                .entityManagerFactory(entityManagerFactory)
                .build());
    }

    @StepScope
    @Bean
    public ListDelegateWriter<GenreH2> genresWriter(EntityManagerFactory entityManagerFactory) {
        return new ListDelegateWriter<>(new JpaItemWriterBuilder<GenreH2>()
                .entityManagerFactory(entityManagerFactory)
                .build());
    }

    @StepScope
    @Bean
    public JpaItemWriter<BookH2> booksWriter(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<BookH2>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @StepScope
    @Bean
    public ListDelegateWriter<CommentH2> commentsWriter(EntityManagerFactory entityManagerFactory) {
        return new ListDelegateWriter<>(new JpaItemWriterBuilder<CommentH2>()
                .entityManagerFactory(entityManagerFactory)
                .build());
    }

    @Bean
    public Job importBookJob(Step transformAuthorsStep, Step transformGenresStep, Step transformBooksStep, Step transformCommentsStep) {
        return jobBuilderFactory.get(IMPORT_BOOK_JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(transformAuthorsStep)
                .next(transformGenresStep)
                .next(transformBooksStep)
                .next(transformCommentsStep)
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
    public Step transformAuthorsStep(MongoItemReader<BookMongo> reader, ListDelegateWriter<AuthorH2> authorsWriter,
                                     ItemProcessor<BookMongo, List<AuthorH2>> authorsProcessor) {
        return stepBuilderFactory.get("transformAuthorsStep")
                .<BookMongo, List<AuthorH2>>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(authorsProcessor)
                .writer(authorsWriter)
                .listener(new ItemReadListener<>() {
                    public void beforeRead() {
                        logger.info("Начало чтения");
                    }

                    public void afterRead(@NonNull BookMongo book) {
                        logger.info("Конец чтения книги \"" + book.getName() + "\"");
                    }

                    public void onReadError(@NonNull Exception e) {
                        logger.info("Ошибка чтения", e);
                    }
                })
                .listener(new ItemProcessListener<>() {
                    public void beforeProcess(@NonNull BookMongo book) {
                        logger.info("Начало обработки книги \"" + book.getName() + "\"");
                    }

                    public void afterProcess(@NonNull BookMongo book, List<AuthorH2> authorH2List) {
                        logger.info("Конец обработки книги \"" + book.getName() + "\"");
                    }

                    public void onProcessError(@NonNull BookMongo book, @NonNull Exception e) {
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

    @Bean
    public Step transformGenresStep(MongoItemReader<BookMongo> reader, ListDelegateWriter<GenreH2> genresWriter,
                                    ItemProcessor<BookMongo, List<GenreH2>> genresProcessor) {
        return stepBuilderFactory.get("transformGenresStep")
                .<BookMongo, List<GenreH2>>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(genresProcessor)
                .writer(genresWriter)
                .listener(new ItemReadListener<>() {
                    public void beforeRead() {
                        logger.info("Начало чтения");
                    }

                    public void afterRead(@NonNull BookMongo book) {
                        logger.info("Конец чтения книги \"" + book.getName() + "\"");
                    }

                    public void onReadError(@NonNull Exception e) {
                        logger.info("Ошибка чтения", e);
                    }
                })
                .listener(new ItemProcessListener<>() {
                    public void beforeProcess(@NonNull BookMongo book) {
                        logger.info("Начало обработки книги \"" + book.getName() + "\"");
                    }

                    public void afterProcess(@NonNull BookMongo book, List<GenreH2> genreH2List) {
                        logger.info("Конец обработки книги \"" + book.getName() + "\"");
                    }

                    public void onProcessError(@NonNull BookMongo book, @NonNull Exception e) {
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

    @Bean
    public Step transformBooksStep(MongoItemReader<BookMongo> reader, JpaItemWriter<BookH2> booksWriter,
                                   ItemProcessor<BookMongo, BookH2> booksProcessor) {
        return stepBuilderFactory.get("transformBooksStep")
                .<BookMongo, BookH2>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(booksProcessor)
                .writer(booksWriter)
                .listener(new ItemReadListener<>() {
                    public void beforeRead() {
                        logger.info("Начало чтения");
                    }

                    public void afterRead(@NonNull BookMongo bookMongo) {
                        logger.info("Конец чтения книги \"" + bookMongo.getName() + "\"");
                    }

                    public void onReadError(@NonNull Exception e) {
                        logger.info("Ошибка чтения", e);
                    }
                })
                .listener(new ItemProcessListener<>() {
                    public void beforeProcess(@NonNull BookMongo bookMongo) {
                        logger.info("Начало обработки книги \"" + bookMongo.getName() + "\"");
                    }

                    public void afterProcess(@NonNull BookMongo bookMongo, BookH2 bookH2) {
                        logger.info("Конец обработки книги \"" + bookMongo.getName() + "\"");
                    }

                    public void onProcessError(@NonNull BookMongo bookMongo, @NonNull Exception e) {
                        logger.info("Ошибка обработки книги \"" + bookMongo.getName() + "\"", e);
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

    @Bean
    public Step transformCommentsStep(MongoItemReader<BookMongo> reader, ListDelegateWriter<CommentH2> commentsWriter,
                                      ItemProcessor<BookMongo, List<CommentH2>> commentsProcessor) {
        return stepBuilderFactory.get("transformCommentsStep")
                .<BookMongo, List<CommentH2>>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(commentsProcessor)
                .writer(commentsWriter)
                .listener(new ItemReadListener<>() {
                    public void beforeRead() {
                        logger.info("Начало чтения");
                    }

                    public void afterRead(@NonNull BookMongo book) {
                        logger.info("Конец чтения книги \"" + book.getName() + "\"");
                    }

                    public void onReadError(@NonNull Exception e) {
                        logger.info("Ошибка чтения", e);
                    }
                })
                .listener(new ItemProcessListener<>() {
                    public void beforeProcess(@NonNull BookMongo book) {
                        logger.info("Начало обработки книги \"" + book.getName() + "\"");
                    }

                    public void afterProcess(@NonNull BookMongo book, List<CommentH2> commentH2List) {
                        logger.info("Конец обработки книги \"" + book.getName() + "\"");
                    }

                    public void onProcessError(@NonNull BookMongo book, @NonNull Exception e) {
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
