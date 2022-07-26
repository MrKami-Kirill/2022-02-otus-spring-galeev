package ru.otus.spring.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.service.LibraryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {

    private final BookRepository bookRepository;

    @Override
    @HystrixCommand(commandKey="getAllBooks", commandProperties= {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="5000")
    }, fallbackMethod = "buildFallbackBooks")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    @HystrixCommand(commandKey="getBookById", commandProperties= {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="5000")
    }, fallbackMethod = "buildFallbackBook")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public Book getBookById(String id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Книга с id=%d не найдена%n", id)));
    }

    @Override
    @HystrixCommand(commandKey="addNewBook", commandProperties= {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="5000")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addNewBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    @HystrixCommand(commandKey="changeBookName", commandProperties= {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="5000")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void changeBookName(String id, String name) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Книга с id=%d не найдена%n", id)));
        book.setName(name);
        bookRepository.save(book);
    }

    @Override
    @HystrixCommand(commandKey="deleteBookById", commandProperties= {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="5000")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteBookById(String id) {
        bookRepository.deleteById(id);
    }

    public List<Book> buildFallbackBooks() {
        return List.of(buildFallbackBook("0"));
    }

    public Book buildFallbackBook(String id) {
        Book book = new Book();
        book.setId(id);
        book.setName("N/A");
        book.setComments(List.of(new Comment("N/A")));
        book.setAuthors(List.of(new Author("N/A")));
        book.setGenres(List.of(new Genre("N/A")));
        return book;
    }
}
