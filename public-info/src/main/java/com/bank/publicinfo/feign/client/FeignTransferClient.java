package com.bank.publicinfo.feign.client;

import com.bank.publicinfo.dto.feign.AccountTransferDto;
import com.bank.publicinfo.dto.feign.CardTransferDto;
import com.bank.publicinfo.dto.feign.PhoneTransferDto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "transfer-app")
public interface FeignTransferClient {

    @GetMapping("/api/transfer/account/read/{id}")
    AccountTransferDto readAccount(@PathVariable("id") Long id);

    @GetMapping("/api/transfer/card/read/{id}")
    CardTransferDto readCard(@PathVariable("id") Long id);

    @GetMapping("/api/transfer/phone/read/{id}")
    PhoneTransferDto readPhone(@PathVariable("id") Long id);
}
