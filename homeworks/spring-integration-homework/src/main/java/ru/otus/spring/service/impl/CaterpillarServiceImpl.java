package ru.otus.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Butterfly;
import ru.otus.spring.domain.Caterpillar;
import ru.otus.spring.integration.Transformation;
import ru.otus.spring.service.CaterpillarService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CaterpillarServiceImpl implements CaterpillarService {

    private static final int NEW_TRANSFORMATION_PROCESS_DELAY_MILLS = 7000;

    private final Transformation transformation;


    @Override
    public void startTransformation() throws Exception {
        ForkJoinPool pool = ForkJoinPool.commonPool();

        while (true) {
            Thread.sleep(NEW_TRANSFORMATION_PROCESS_DELAY_MILLS);

            pool.execute(() -> {
                Collection<Caterpillar> caterpillars = generateCaterpillars();
                System.out.println("Новые гусеницы в кол-ве " + caterpillars.size() + ": " +
                        caterpillars);
                Collection<Butterfly> butterflies = transformation.transformationToButterfly(caterpillars);
                System.out.println("Новые бабочки в кол-ве " + butterflies.size() + ": " + butterflies);
            });
        }
    }

    private Caterpillar generateCaterpillar() {
        int length = 16;
        Random r = new Random();
        String name = r.ints(48, 122)
                .filter(i -> (i < 57 || i > 65) && (i < 90 || i > 97))
                .mapToObj(i -> (char) i)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        return Caterpillar.newCaterpillar(name);
    }

    private Collection<Caterpillar> generateCaterpillars() {
        List<Caterpillar> items = new ArrayList<>();
        for (int i = 0; i < RandomUtils.nextInt(1, 5); ++i) {
            items.add(generateCaterpillar());
        }

        return items;
    }
}
