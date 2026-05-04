package com.bank.profile.controller.feign.controller;

import com.bank.profile.dto.feign.AccountDetailsDto;
import com.bank.profile.service.feign.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/details/{id}")
    public AccountDetailsDto readAccountDetails(@PathVariable Long id) {
        return accountService.readAccountDetails(id);
    }
}
