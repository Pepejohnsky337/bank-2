package com.bank.publicinfo.service.feign.service.impl;

import com.bank.publicinfo.dto.feign.AccountTransferDto;
import com.bank.publicinfo.dto.feign.CardTransferDto;
import com.bank.publicinfo.dto.feign.PhoneTransferDto;
import com.bank.publicinfo.feign.client.FeignTransferClient;
import com.bank.publicinfo.service.feign.service.TransferService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final FeignTransferClient feignClient;

    @Override
    public AccountTransferDto readAccount(Long id) {
        return feignClient.readAccount(id);
    }

    @Override
    public CardTransferDto readCard(Long id) {
        return feignClient.readCard(id);
    }

    @Override
    public PhoneTransferDto readPhone(Long id) {
        return feignClient.readPhone(id);
    }
}
