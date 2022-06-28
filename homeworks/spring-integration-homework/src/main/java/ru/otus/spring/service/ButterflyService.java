package ru.otus.spring.service;

import ru.otus.spring.domain.Butterfly;
import ru.otus.spring.domain.Caterpillar;

public interface ButterflyService {

    Butterfly transformation(Caterpillar caterpillar) throws Exception;

    void checkAliveAndInCocoon(Caterpillar caterpillar);
}
