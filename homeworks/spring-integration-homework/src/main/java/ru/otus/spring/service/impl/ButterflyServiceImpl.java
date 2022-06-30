package ru.otus.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Butterfly;
import ru.otus.spring.domain.Caterpillar;
import ru.otus.spring.exception.NotFoundException;
import ru.otus.spring.repository.ButterflyRepository;
import ru.otus.spring.service.ButterflyService;
import ru.otus.spring.service.CaterpillarService;

import java.util.List;

import static java.lang.System.out;

@Service
@RequiredArgsConstructor
public class ButterflyServiceImpl implements ButterflyService {

    private final ButterflyRepository repository;
    private final CaterpillarService caterpillarService;

    @Override
    public Butterfly transformation(Caterpillar caterpillar) throws Exception {
        out.println("Трансформация гусеницы '" + caterpillar.getName() + "' началась");
        Thread.sleep(3000);
        caterpillar.setTransform(true);
        caterpillarService.updateCaterPillar(caterpillar);
        Butterfly butterfly = repository.save(Butterfly.transformFromCaterpillar(caterpillar));
        out.println("Появилась новая бабочка '" + butterfly.getName() + "'");
        return butterfly;
    }

    @Override
    public void checkAliveAndInCocoon(Caterpillar caterpillar) {
        if (!caterpillar.isAlive()) {
            out.println(caterpillar.getName() + " не может превратиться в бабочку, т.к. она мертва");
        } else if (!caterpillar.isInCocoon()) {
            out.println(caterpillar.getName() + " не может превратиться в бабочку, т.к. она еще не в коконе");
        } else if (caterpillar.isTransform()) {
            out.println(caterpillar.getName() + " не может превратиться в бабочку, т.к. она уже стала бабочкой");
        }
    }

    @Override
    public Butterfly findById(String id) throws Exception {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Бабочка с id=%s не найдена!", id)));
    }

    @Override
    public List<Butterfly> findAll() {
        return repository.findAll();
    }

}
