package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Random;

@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "caterpillars")
public class Caterpillar {

    @Id
    private String id;
    private String name;
    private Size size;
    private Color color;
    private boolean isAlive;
    private boolean inCocoon;
    private boolean isTransform = false;

    public Caterpillar(String name, Size size, Color color, boolean isAlive, boolean inCocoon) {
        this.name = name;
        this.size = size;
        this.color = color;
        this.isAlive = isAlive;
        this.inCocoon = inCocoon;
    }

    public static Caterpillar newCaterpillar(String name) {
        return new Caterpillar(name, Size.randomSize(), Color.randomColor(), Math.random() < 0.5, Math.random() < 0.5);
    }

    public static Caterpillar newRandomValidCaterpillar() {
        return new Caterpillar(getRandomName(), Size.randomSize(), Color.randomColor(), true, true);
    }

    public static Caterpillar newRandomCaterpillar() {
        return new Caterpillar(getRandomName(), Size.randomSize(), Color.randomColor(), Math.random() < 0.5, Math.random() < 0.5);
    }

    private static String getRandomName() {
        int length = 16;
        Random r = new Random();
        return r.ints(48, 122)
                .filter(i -> (i < 57 || i > 65) && (i < 90 || i > 97))
                .mapToObj(i -> (char) i)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}
