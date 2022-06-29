package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "butterflies")
public class Butterfly {

    @Id
    private String id;
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
