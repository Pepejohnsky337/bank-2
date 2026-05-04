package com.bank.profile.service.feign.service.impl;


import com.bank.profile.dto.feign.AccountDetailsDto;
import com.bank.profile.feign.client.FeignAccountClient;
import com.bank.profile.service.feign.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final FeignAccountClient feignAccountClient;

    @Override
    public AccountDetailsDto readAccountDetails(Long id) {
        return feignAccountClient.read(id);
    }
}
