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
        Random random = new Random();
        return new Caterpillar(name, Size.randomSize(), Color.randomColor(), random.nextBoolean(), random.nextBoolean());
    }

    public static Caterpillar newRandomValidCaterpillar() {
        return new Caterpillar(getRandomName(), Size.randomSize(), Color.randomColor(), true, true);
    }

    public static Caterpillar newRandomCaterpillar() {
        Random random = new Random();
        return new Caterpillar(getRandomName(), Size.randomSize(), Color.randomColor(), random.nextBoolean(), random.nextBoolean());
    }

    private static String getRandomName() {
        int length = 16;
        return new Random().ints(48, 122)
                .filter(i -> (i < 57 || i > 65) && (i < 90 || i > 97))
                .mapToObj(i -> (char) i)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}
