package com.bank.account.service.feign.service;

import com.bank.account.dto.feign.BankDetailsDto;
import com.bank.account.feign.client.PublicInfoClient;
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
@CacheConfig(cacheNames = "bankDetails")
public class PublicInfoServiceImpl implements PublicInfoService {

    private final PublicInfoClient publicInfoClient;

    @SneakyThrows
    @Override
    @Cacheable(key = "#id")
    public BankDetailsDto getBankDetailsById(Long id) {
        log.info("Обращаемся к PUBLIC-INFO-APP за банковскими реквизитами с id: {}", id);
        Thread.sleep(3000);
        final BankDetailsDto bankDetailsDto = publicInfoClient.getBankDetailsById(id);
        log.info("Банковские реквизиты с id {} успешно получены и будут закешированы", id);
        return bankDetailsDto;
    }


    @Override
    @CacheEvict(allEntries = true)
    public void evict() {
        log.info("Cache 'bankDetails' cleared");
    }
}
