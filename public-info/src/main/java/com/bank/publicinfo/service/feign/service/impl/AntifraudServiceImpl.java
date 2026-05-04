package com.bank.publicinfo.service.feign.service.impl;

import com.bank.publicinfo.dto.feign.SuspiciousAccountTransferDto;
import com.bank.publicinfo.dto.feign.SuspiciousCardTransferDto;
import com.bank.publicinfo.dto.feign.SuspiciousPhoneTransferDto;
import com.bank.publicinfo.feign.client.FeignAntifraudClient;
import com.bank.publicinfo.service.kafka.KafkaProducerService;
import com.bank.publicinfo.service.feign.service.AntifraudService;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "antifraud")
public class AntifraudServiceImpl implements AntifraudService {

    private final FeignAntifraudClient feignClient;
    private final KafkaProducerService kafkaProducerService;

    @SneakyThrows
    @Override
    @Cacheable(key = "'account'+ #id")
    public SuspiciousAccountTransferDto readAccount(Long id) {
        log.info("обращаемся к readAccount с задержкой в 3 секунды");
        Thread.sleep(3000);
        final SuspiciousAccountTransferDto suspiciousAccountTransferDto = feignClient.readAccount(id);
        log.info("readAccount закеширован");
        final String message = "Выполнен Feign-вызов SuspiciousAccountTransferDto.";
        kafkaProducerService.sendEvent(message);
        return suspiciousAccountTransferDto;
    }

    @SneakyThrows
    @Override
    @Cacheable(key = "'card' + #id")
    public SuspiciousCardTransferDto readCard(Long id) {
        log.info("обращаемся к readCard с задержкой в 3 секунды");
        Thread.sleep(3000);
        final SuspiciousCardTransferDto suspiciousCardTransferDto = feignClient.readCard(id);
        log.info("readCard закеширован");
        final String message = "Выполнен Feign-вызов SuspiciousCardTransferDto.";
        kafkaProducerService.sendEvent(message);
        return suspiciousCardTransferDto;
    }

    @SneakyThrows
    @Override
    @Cacheable(key = "'phone' + #id")
    public SuspiciousPhoneTransferDto readPhone(Long id) {
        log.info("обращаемся к readPhone с задержкой в 3 секунды");
        Thread.sleep(3000);
        final SuspiciousPhoneTransferDto suspiciousPhoneTransferDto = feignClient.readPhone(id);
        log.info("readPhone закеширован");
        final String message = "Выполнен Feign-вызов SuspiciousPhoneTransferDto.";
        kafkaProducerService.sendEvent(message);
        return suspiciousPhoneTransferDto;
    }

    @Override
    @CacheEvict(allEntries = true)
    public void evict() {
    }
}
