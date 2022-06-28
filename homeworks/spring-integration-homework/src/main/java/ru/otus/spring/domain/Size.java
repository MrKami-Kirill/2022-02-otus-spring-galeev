package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Getter
public enum Size {

    SMALL("Маленький"),
    MEDIUM("Средний"),
    BIG("Большой");

    private final String value;

    private static final List<Size> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Size randomSize()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
