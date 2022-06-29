package ru.otus.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Caterpillar;
import ru.otus.spring.integration.Transformation;
import ru.otus.spring.service.CaterpillarService;
import ru.otus.spring.service.TransformationService;

import java.util.List;
import java.util.concurrent.Executors;


@Service
@RequiredArgsConstructor
public class TransformationServiceImpl implements TransformationService {

    private final Transformation transformation;
    private final CaterpillarService caterpillarService;

    private ConcurrentTaskExecutor executor = new ConcurrentTaskExecutor(Executors.newFixedThreadPool(10));

    @Async
    @Override
    public void startTransformation() {
        executor.execute(() -> transformation.transformationToButterfly(caterpillarService.findAll()));
    }
}
