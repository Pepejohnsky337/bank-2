package com.bank.publicinfo.service.feign.service.impl;

import com.bank.publicinfo.dto.feign.SuspiciousAccountTransferDto;
import com.bank.publicinfo.dto.feign.SuspiciousCardTransferDto;
import com.bank.publicinfo.dto.feign.SuspiciousPhoneTransferDto;
import com.bank.publicinfo.feign.client.FeignAntifraudClient;
import com.bank.publicinfo.service.feign.service.AntifraudService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AntifraudServiceImpl implements AntifraudService {

    private final FeignAntifraudClient feignClient;

    @Override
    public SuspiciousAccountTransferDto readAccount(Long id) {
        return feignClient.readAccount(id);
    }

    @Override
    public SuspiciousCardTransferDto readCard(Long id) {
        return feignClient.readCard(id);
    }

    @Override
    public SuspiciousPhoneTransferDto readPhone(Long id) {
        return feignClient.readPhone(id);
    }
}

