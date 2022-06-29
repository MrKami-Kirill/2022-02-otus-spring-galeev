package ru.otus.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Caterpillar;
import ru.otus.spring.exception.NotFoundException;
import ru.otus.spring.repository.CaterpillarRepository;
import ru.otus.spring.service.CaterpillarService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CaterpillarServiceImpl implements CaterpillarService {

    private final CaterpillarRepository repository;

    @Override
    public Caterpillar findById(String id) throws Exception {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Гусеница с id=%s не найдена!", id)));
    }

    @Override
    public List<Caterpillar> findAll() {
        return repository.findAll();
    }

    @Override
    public Caterpillar addValidNew() {
        return repository.save(Caterpillar.newRandomValidCaterpillar());
    }

    @Override
    public Caterpillar addNew() {
        return repository.save(Caterpillar.newRandomCaterpillar());
    }

    @Override
    public Caterpillar addNewByName(String name) {
        return repository.save(Caterpillar.newCaterpillar(name));
    }

    @Override
    public void updateCaterPillar(Caterpillar caterpillar) {
        repository.save(caterpillar);
    }
}
