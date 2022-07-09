package ru.otus.spring.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class Genre {
    private String name;

    @JsonCreator
    public Genre(@JsonProperty("name") String name) {
        this.name = name;
    }
}
