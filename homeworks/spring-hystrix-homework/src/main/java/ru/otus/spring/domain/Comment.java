package ru.otus.spring.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Comment {

    private String text;

    @JsonCreator
    public Comment(@JsonProperty("text") String text) {
        this.text = text;
    }
}
