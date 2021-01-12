package com.devk.angebotAuswaehlen.HandlerConfig;


import com.devk.angebotAuswaehlen.HandlerService.HandlerService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.backoff.ExponentialBackoffStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class HandlerConfiguration {

    private HandlerService handlerService;

    public HandlerConfiguration(HandlerService handlerService) {
        this.handlerService = handlerService;
    }

    @Bean
    public void createTopicSubscriberHandler() {
        ExponentialBackoffStrategy fetchTimer = new ExponentialBackoffStrategy(500L, 2, 500L);
        int maxTasksToFetchWithinOnRequest = 1;

        ExternalTaskClient externalTaskClient = ExternalTaskClient
                .create()
                .baseUrl("http://localhost:8080/engine-rest")
                .maxTasks(3).backoffStrategy(fetchTimer)
                .build();

        externalTaskClient
                .subscribe("training_angebot_waehlen")
                .handler((handlerService))
                .open();
    }
}