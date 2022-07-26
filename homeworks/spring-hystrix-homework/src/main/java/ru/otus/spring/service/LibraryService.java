package ru.otus.spring.service;

import ru.otus.spring.domain.Book;

import java.util.List;

public interface LibraryService {
    
    
    List<Book> getAllBooks();

    Book getBookById(String id);

    void addNewBook(Book book);

    void changeBookName(String id, String name);

    void deleteBookById(String id);
}
