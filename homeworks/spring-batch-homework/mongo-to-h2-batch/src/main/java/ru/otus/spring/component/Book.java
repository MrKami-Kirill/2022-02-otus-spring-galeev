package ru.otus.spring.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.model.h2.AuthorH2;
import ru.otus.spring.model.h2.BookH2;
import ru.otus.spring.model.h2.CommentH2;
import ru.otus.spring.model.h2.GenreH2;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    private long id;
    private String name;
    private Set<Comment> comments = new HashSet<>();
    private Set<Author> authors = new HashSet<>();
    private Set<Genre> genres = new HashSet<>();

    @Data
    @AllArgsConstructor
    static class Comment {
        private long id;
        private String text;

        public static Comment convert(CommentH2 comment) {
            return new Comment(
                    comment.getId(),
                    comment.getText()
            );
        }
    }

    @Data
    @AllArgsConstructor
    static class Author {
        private long id;
        private String name;

        public static Author convert(AuthorH2 author) {
            return new Author(
                    author.getId(),
                    author.getName()
            );
        }
    }

    @Data
    @AllArgsConstructor
    static class Genre {
        private long id;
        private String name;

        public static Genre convert(GenreH2 genre) {
            return new Genre(
                    genre.getId(),
                    genre.getName()
            );
        }
    }

    public static Book convert(BookH2 book) {
        Book result = new Book();
        result.setId(book.getId());
        result.setName(book.getName());
        book.getComments().forEach(c -> result.getComments().add(Comment.convert(c)));
        book.getAuthors().forEach(a -> result.getAuthors().add(Author.convert(a)));
        book.getGenres().forEach(g -> result.getGenres().add(Genre.convert(g)));
        return result;
    }
}
