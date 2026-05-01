package com.bank.account.feign.client;

import com.bank.account.dto.feign.BankDetailsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PUBLIC-INFO-APP")
public interface PublicInfoClient {

    @GetMapping("/api/public-info/bank/details/{id}")
    BankDetailsDto getBankDetailsById(@PathVariable("id") Long id);

}
