package com.bank.profile.feign.client;

import com.bank.profile.dto.feign.AccountDetailsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "account-app")
public interface FeignAccountClient {

    @GetMapping("/api/account/details/{id}")
    AccountDetailsDto read(@PathVariable("id") Long id);

}
