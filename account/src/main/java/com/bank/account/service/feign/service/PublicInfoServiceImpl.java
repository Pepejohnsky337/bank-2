package com.bank.account.service.feign.service;

import com.bank.account.dto.feign.BankDetailsDto;
import com.bank.account.feign.client.PublicInfoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublicInfoServiceImpl implements PublicInfoService {

    private final PublicInfoClient publicInfoService;

    @Override
    public BankDetailsDto getBankDetailsById(Long id) {
        return publicInfoService.getBankDetailsById(id);
    }
}
