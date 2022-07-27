package ru.otus.spring.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.domain.Book;
import ru.otus.spring.rest.dto.request.ChangeBookNameDto;
import ru.otus.spring.service.LibraryService;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final LibraryService libraryService;

    @GetMapping("/")
    public List<Book> getAllBooks() {
        return libraryService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable("id") String id) {
        return libraryService.getBookById(id);
    }

    @PostMapping("/")
    public void addNewBook(@RequestBody Book book) {
        libraryService.addNewBook(book);
    }

    @PutMapping("/{id}")
    public void changeBookName(@PathVariable("id") String id, @RequestBody ChangeBookNameDto dto) {
        libraryService.changeBookName(id, dto.getName());
    }

    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable("id") String id) {
        libraryService.deleteBookById(id);
    }
}
