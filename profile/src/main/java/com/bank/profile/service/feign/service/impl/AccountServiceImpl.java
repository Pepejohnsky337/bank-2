package com.bank.profile.service.feign.service.impl;


import com.bank.profile.dto.feign.AccountDetailsDto;
import com.bank.profile.feign.client.FeignAccountClient;
import com.bank.profile.service.feign.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final FeignAccountClient feignAccountClient;

    @Override
    @Cacheable(value = "accountDetailsCache", key = "#id")
    public AccountDetailsDto readAccountDetails(Long id) {
        log.info("Cache MISS: запрос в account по id = {}", id);
        return feignAccountClient.read(id);
    }
}
