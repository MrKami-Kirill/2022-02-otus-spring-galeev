package ru.otus.spring.domain;

import lombok.Data;

@Data
public class Caterpillar {

    private String name;
    private Size size;
    private Color color;
    private boolean isAlive;
    private boolean inCocoon;

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
}
