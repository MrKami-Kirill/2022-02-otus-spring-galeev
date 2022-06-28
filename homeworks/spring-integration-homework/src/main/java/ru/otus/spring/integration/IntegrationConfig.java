package ru.otus.spring.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.spring.domain.Caterpillar;
import ru.otus.spring.service.ButterflyService;

@Configuration
public class IntegrationConfig {

    private static final int QUEUE_CAPACITY = 10;
    private static final String TRANSFORMATION_METHOD_NAME = "transformation";
    private static final String CHECK_ALIVE_AND_IN_COCOON_METHOD_NAME = "checkAliveAndInCocoon";

    @Bean
    public QueueChannel caterpillarsChannel() {
        return MessageChannels.queue(QUEUE_CAPACITY).get();
    }

    @Bean
    public PublishSubscribeChannel butterfliesChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2).get();
    }

    @Bean
    public IntegrationFlow transformationFlow(ButterflyService butterflyService) {
        return f -> f
                .split()
                .<Caterpillar, Boolean>route(
                        c -> c.isAlive() && c.isInCocoon(),
                        mapping -> mapping
                                .subFlowMapping(true, sf -> sf
                                        .handle(butterflyService, TRANSFORMATION_METHOD_NAME)
                                        .aggregate()
                                        .channel(butterfliesChannel())
                                )
                                .subFlowMapping(false, sf -> sf
                                        .handle(butterflyService, CHECK_ALIVE_AND_IN_COCOON_METHOD_NAME)
                                )
                );
    }
}
