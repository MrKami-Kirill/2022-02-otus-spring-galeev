package ru.otus.spring.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@EqualsAndHashCode(of = "id")
@Document(collection = "books")
public class Book {

    @Id
    private String id;

    private String name;

    private List<Comment> comments;

    private List<Author> authors;

    private List<Genre> genres;

    @JsonCreator
    public Book(@JsonProperty("name") String name, @JsonProperty("comments") List<Comment> comments, @JsonProperty("authors") List<Author> authors, @JsonProperty("genres") List<Genre> genres) {
        this.name = name;
        this.comments = comments;
        this.authors = authors;
        this.genres = genres;
    }
}
