package com.bank.publicinfo.service.feign.service.impl;

import com.bank.publicinfo.dto.feign.AccountTransferDto;
import com.bank.publicinfo.dto.feign.CardTransferDto;
import com.bank.publicinfo.dto.feign.PhoneTransferDto;
import com.bank.publicinfo.feign.client.FeignTransferClient;
import com.bank.publicinfo.service.feign.service.TransferService;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "transfer")
public class TransferServiceImpl implements TransferService {

    private final FeignTransferClient feignClient;

    @SneakyThrows
    @Override
    @Cacheable(key = "'account'+ #id")
    public AccountTransferDto readAccount(Long id) {
        log.info("обращаемся к readAccount с задержкой в 3 секунды");
        Thread.sleep(3000);
        final AccountTransferDto accountTransferDto = feignClient.readAccount(id);
        log.info("readAccount закеширован");
        return accountTransferDto;
    }

    @SneakyThrows
    @Override
    @Cacheable(key = "'card'+ #id")
    public CardTransferDto readCard(Long id) {
        log.info("обращаемся к readCard с задержкой в 3 секунды");
        Thread.sleep(3000);
        final CardTransferDto cardTransferDto = feignClient.readCard(id);
        log.info("readCard закеширован");
        return cardTransferDto;
    }

    @SneakyThrows
    @Override
    @Cacheable(key = "'phone'+ #id")
    public PhoneTransferDto readPhone(Long id) {
        log.info("обращаемся к readPhone с задержкой в 3 секунды");
        Thread.sleep(3000);
        final PhoneTransferDto phoneTransferDto = feignClient.readPhone(id);
        log.info("readPhone закеширован");
        return phoneTransferDto;
    }

    @Override
    @CacheEvict(allEntries = true)
    public void evict() {
    }
}

