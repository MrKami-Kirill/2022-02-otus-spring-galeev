package ru.otus.spring.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "books")
public class BookMongo {

    @Id
    private String id;
    private String name;
    private Set<CommentMongo> comments = new HashSet<>();
    private Set<AuthorMongo> authors = new HashSet<>();
    private Set<GenreMongo> genres = new HashSet<>();

    public BookMongo(String name, Set<CommentMongo> comments, Set<AuthorMongo> authors, Set<GenreMongo> genres) {
        this.name = name;
        this.comments = comments;
        this.authors = authors;
        this.genres = genres;
    }
}
