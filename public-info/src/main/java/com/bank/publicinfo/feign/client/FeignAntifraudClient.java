package com.bank.publicinfo.feign.client;

import com.bank.publicinfo.dto.feign.SuspiciousAccountTransferDto;
import com.bank.publicinfo.dto.feign.SuspiciousCardTransferDto;
import com.bank.publicinfo.dto.feign.SuspiciousPhoneTransferDto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "anti-fraud-app")
public interface FeignAntifraudClient {

    @GetMapping("/api/anti-fraud/suspicious/account/transfer/{id}")
    SuspiciousAccountTransferDto readAccount(@PathVariable("id") Long id);

    @GetMapping("/api/anti-fraud/suspicious/card/transfer/{id}")
    SuspiciousCardTransferDto readCard(@PathVariable("id") Long id);

    @GetMapping("/api/anti-fraud/suspicious/phone/transfer/{id}")
    SuspiciousPhoneTransferDto readPhone(@PathVariable("id") Long id);
}

