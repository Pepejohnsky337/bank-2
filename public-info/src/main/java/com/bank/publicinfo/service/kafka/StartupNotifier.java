package com.bank.publicinfo.service.kafka;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class StartupNotifier {

    private final KafkaProducerService kafkaProducerService;

    public StartupNotifier(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostConstruct
    public void onStartup() {
        final String message = "Public-info сервис успешно запущен! Время: " + new java.util.Date();
        kafkaProducerService.sendEvent(message);
    }
}

