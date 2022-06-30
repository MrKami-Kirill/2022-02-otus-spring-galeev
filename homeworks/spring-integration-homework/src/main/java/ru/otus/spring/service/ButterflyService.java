package ru.otus.spring.service;

import ru.otus.spring.domain.Butterfly;
import ru.otus.spring.domain.Caterpillar;

import java.util.List;

public interface ButterflyService {

    Butterfly transformation(Caterpillar caterpillar) throws Exception;

    void checkAliveAndInCocoon(Caterpillar caterpillar);

    Butterfly findById(String id) throws Exception;

    List<Butterfly> findAll();
}
