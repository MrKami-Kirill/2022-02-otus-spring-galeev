package ru.otus.spring.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.model.h2.AuthorH2;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorMongo {
    private String name;

    public static AuthorMongo convertAuthor(AuthorH2 author) {
        return new AuthorMongo(author.getName());
    }
}
