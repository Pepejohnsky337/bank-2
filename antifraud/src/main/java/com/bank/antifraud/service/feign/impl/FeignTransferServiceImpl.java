package com.bank.antifraud.service.feign.impl;

import com.bank.antifraud.dto.external.transfer.ExternalAccountTransferDto;
import com.bank.antifraud.dto.external.transfer.ExternalCardTransferDto;
import com.bank.antifraud.dto.external.transfer.ExternalPhoneTransferDto;
import com.bank.antifraud.feign.transfer.FeignTransferClient;
import com.bank.antifraud.service.feign.FeignTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeignTransferServiceImpl implements FeignTransferService {
    
    private final FeignTransferClient client;
    
    @Override
    public ExternalAccountTransferDto readAccountTransfer(Long id) {
        return client.readAccountTransfer(id);
    }
    
    @Override
    public ExternalCardTransferDto readCardTransfer(Long id) {
        return client.readCardTransfer(id);
    }
    
    @Override
    public ExternalPhoneTransferDto readPhoneTransfer(Long id) {
        return client.readPhoneTransfer(id);
    }
    
}
