package ru.otus.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Butterfly;
import ru.otus.spring.domain.Caterpillar;
import ru.otus.spring.service.ButterflyService;

import static java.lang.System.out;

@Service
@RequiredArgsConstructor
public class ButterflyServiceImpl implements ButterflyService {

    @Override
    public Butterfly transformation(Caterpillar caterpillar) throws Exception {
        out.println("Трансформация гусеницы '" + caterpillar.getName() + "' началась");
        Thread.sleep(3000);
        Butterfly butterfly = Butterfly.transformFromCaterpillar(caterpillar);
        out.println("Появилась новая бабочка '" + butterfly.getName() + "'");
        return butterfly;
    }

    @Override
    public void checkAliveAndInCocoon(Caterpillar caterpillar) {
        if (!caterpillar.isAlive()) {
            out.println(caterpillar.getName() + " не может превратиться в бабочку, т.к. она мертва");
        } else if (!caterpillar.isInCocoon()) {
            out.println(caterpillar.getName() + " не может превратиться в бабочку, т.к. она еще не в коконе");
        }
    }

}
