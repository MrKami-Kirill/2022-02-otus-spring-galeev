package ru.otus.spring.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.spring.domain.Butterfly;
import ru.otus.spring.domain.Caterpillar;

import java.util.Collection;

@MessagingGateway
public interface Transformation {

    @Gateway(requestChannel = "transformationFlow.input")
    Collection<Butterfly> transformationToButterfly(Collection<Caterpillar> caterpillars);
}
