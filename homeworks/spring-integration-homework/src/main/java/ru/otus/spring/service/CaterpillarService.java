package ru.otus.spring.service;

import ru.otus.spring.domain.Caterpillar;

import java.util.List;

public interface CaterpillarService {

    Caterpillar findById(String id) throws Exception;

    List<Caterpillar> findAll();

    Caterpillar addValidNew();

    Caterpillar addNew();

    Caterpillar addNewByName(String name);

    void updateCaterPillar(Caterpillar caterpillar);
}
