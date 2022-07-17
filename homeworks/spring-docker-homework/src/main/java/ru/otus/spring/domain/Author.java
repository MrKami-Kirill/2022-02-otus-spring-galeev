package ru.otus.spring.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class Author {
    private String name;

    @JsonCreator
    public Author(@JsonProperty("name") String name) {
        this.name = name;
    }
}
