package com.bank.antifraud.service.feign.impl;

import com.bank.antifraud.dto.external.transfer.ExternalAccountTransferDto;
import com.bank.antifraud.dto.external.transfer.ExternalCardTransferDto;
import com.bank.antifraud.dto.external.transfer.ExternalPhoneTransferDto;
import com.bank.antifraud.feign.transfer.FeignTransferClient;
import com.bank.antifraud.service.feign.FeignTransferService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeignTransferServiceImpl implements FeignTransferService {
    
    private final Logger logger;
    private final FeignTransferClient client;
    
    @Cacheable(
            cacheNames = "externalAccountTransferCache",
            key = "#id"
    )
    @Override
    public ExternalAccountTransferDto readAccountTransfer(Long id) {
        logger.info("Промах кэша: запрос Account Transfer по id = {}", id);
        return client.readAccountTransfer(id);
    }
    
    @Cacheable(
            cacheNames = "externalCardTransferCache",
            key = "#id"
    )
    @Override
    public ExternalCardTransferDto readCardTransfer(Long id) {
        logger.info("Промах кэша: запрос Card Transfer по id = {}", id);
        return client.readCardTransfer(id);
    }
    
    @Cacheable(
            cacheNames = "externalPhoneTransferCache",
            key = "#id"
    )
    @Override
    public ExternalPhoneTransferDto readPhoneTransfer(Long id) {
        logger.info("Промах кэша: запрос Phone Transfer по id = {}", id);
        return client.readPhoneTransfer(id);
    }
    
}
