package ru.otus.spring.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.model.h2.GenreH2;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreMongo {

    private String name;

    public static GenreMongo convertGenre(GenreH2 genre) {
        return new GenreMongo(genre.getName());
    }
}
