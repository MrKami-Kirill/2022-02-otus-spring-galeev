package ru.otus.spring.domain;

import lombok.Data;

import java.util.List;

@Data
public class Butterfly {

    private String name;
    private Size size;
    private Color color;
    private List<Color> wingsColorList;
    private boolean isAlive;

    public Butterfly(String name, Size size, Color color, List<Color> wingsColorList, boolean isAlive) {
        this.name = name;
        this.size = size;
        this.color = color;
        this.wingsColorList = wingsColorList;
        this.isAlive = isAlive;
    }

    public static Butterfly transformFromCaterpillar(Caterpillar caterpillar) {
        return new Butterfly(caterpillar.getName(), caterpillar.getSize(), caterpillar.getColor(), Color.randomColorList(3), caterpillar.isAlive());
    }
}
