package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.component.Book;
import ru.otus.spring.exception.NotFoundException;
import ru.otus.spring.repository.h2.BookH2Repository;
import ru.otus.spring.repository.mongo.BookMongoRepository;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.function.Consumer;

import static java.lang.System.out;
import static ru.otus.spring.config.JobConfig.IMPORT_BOOK_JOB_NAME;

@RequiredArgsConstructor
@ShellComponent
public class BatchCommands {

    private final Job importBookJob;
    private final JobLauncher jobLauncher;
    private final JobOperator jobOperator;
    private final JobExplorer jobExplorer;
    private final JobRepository jobRepository;
    private final BookH2Repository bookH2Repository;
    private final BookMongoRepository bookMongoRepository;

    @ShellMethod(value = "Get book by id from H2 DB", key = "h2-bbi")
    private void getBookH2ById(@ShellOption Long id) throws NotFoundException {
        out.println(Book.convert(bookH2Repository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Книга в базе H2 c id=%d не найдена", id)))));
    }

    @ShellMethod(value = "Get all books from H2 DB", key = {"h2-books", "h2b"})
    private void getAllH2Books() {
        bookH2Repository.findAll().stream().map(Book::convert).forEach(out::println);
    }

    @ShellMethod(value = "Get count books from H2", key = {"h2-books-count", "h2bc"})
    private void getCountH2Books() {
        out.println(bookH2Repository.findAll().size());
    }

    @ShellMethod(value = "Delete all books in H2", key = {"h2-delete-all-books", "d"})
    private void deleteAllMongoBooks() {
        bookH2Repository.deleteAll();
        out.println("Все книги удалены из H2");
    }

    @ShellMethod(value = "Get book by id from MongoDB", key = "mongo-bbi")
    private void getBookMongoById(@ShellOption String id) throws NotFoundException {
        out.println(bookMongoRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Книга в базе Mongo c id=%s не найдена", id))));
    }

    @ShellMethod(value = "Get all books from MongoDB", key = {"mongo-books", "mb"})
    private void getAllMongoBooks() {
        bookMongoRepository.findAll().forEach(out::println);
    }

    @ShellMethod(value = "Get count books from MongoDB", key = {"mongo-books-count", "mbc"})
    private void getCountMongoBooks() {
        out.println(bookMongoRepository.findAll().size());
    }

    @ShellMethod(value = "startMigrationJobWithJobLauncher", key = "jl-run")
    public void startMigrationJobWithAuthorsJobLauncher() throws Exception {
        JobExecution execution = jobLauncher.run(importBookJob, new JobParametersBuilder()
                .addDate("date", new Date())
                .toJobParameters());
        out.println(execution);
    }


    @ShellMethod(value = "restartMigrationJobWithJobOperator", key = "jo-restart")
    public void restartMigrationJobWithJobOperator(@ShellOption long id) throws Exception {
        checkExecutionAndDoAction(id, (execution) -> {
            try {
                Long executionId = jobOperator.restart(id);
                out.println(jobOperator.getSummary(executionId));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @ShellMethod(value = "stopMigrationJobWithJobOperator", key = "jo-stop")
    public void stopMigrationJobWithJobOperator(@ShellOption long id) {
        checkExecutionAndDoAction(id, (execution) -> {
            try {
                out.println("JobOperator c id = " + id + " isStopped = " + jobOperator.stop(id));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @ShellMethod(value = "showInfo", key = {"i", "info"})
    public void showInfo() {
        out.println("JobNames: " + jobExplorer.getJobNames());
        out.println("LastJobInstance: " + jobExplorer.getLastJobInstance(IMPORT_BOOK_JOB_NAME));
    }

    @ShellMethod(value = "showInfoById", key = {"ibi", "info-by-id"})
    public void showInfoById(@ShellOption long id) {
        checkExecutionAndDoAction(id, (execution) -> out.println("JobExecution: " + execution));
    }

    @ShellMethod(value = "Change batch step execution status by id", key = "change-status")
    public void changeBatchStepExecutionStatus(@ShellOption long id, @ShellOption String status) {
        checkExecutionAndDoAction(id, (execution) -> {
            execution.setStatus(BatchStatus.valueOf(status));
            jobRepository.update(execution);
        });
    }

    private void checkExecutionAndDoAction(long id, @NotNull Consumer<JobExecution> consumer) {
        JobExecution execution = jobExplorer.getJobExecution(id);
        if (execution != null) {
            consumer.accept(execution);
        } else {
            out.println("JobExecution с id = " + id + " не найден");
        }
    }
}
