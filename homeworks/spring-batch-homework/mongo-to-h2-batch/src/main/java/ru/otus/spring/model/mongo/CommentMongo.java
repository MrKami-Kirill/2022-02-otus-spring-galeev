package ru.otus.spring.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.model.h2.CommentH2;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentMongo {

    private String text;

    public static CommentMongo convertComment(CommentH2 comment) {
        return new CommentMongo(comment.getText());
    }
}
