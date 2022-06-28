package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Getter
public enum Color {

    RED("Красный"),
    GRAY("Серый"),
    LIGHT_BLUE("Голубой"),
    DARK_BLUE("Синий"),
    GREEN("Зеленый"),
    YELLOW("Желтый"),
    PINK("Розовый"),
    ORANGE("Оранжевый"),
    BROWN("Коричневый"),
    WHITE("Белый"),
    BLACK("Черный"),
    VIOLET("Фиолетовый");

    private final String value;

    private static final List<Color> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Color randomColor()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    public static List<Color> randomColorList(int count)  {
        List<Color> result = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            result.add(VALUES.get(RANDOM.nextInt(SIZE)));
        }

        return result;
    }
}
