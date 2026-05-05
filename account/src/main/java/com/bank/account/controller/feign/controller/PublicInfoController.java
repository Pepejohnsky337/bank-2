package com.bank.account.controller.feign.controller;

import com.bank.account.dto.feign.BankDetailsDto;
import com.bank.account.service.feign.service.PublicInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public-info")
public class PublicInfoController {

    private final PublicInfoService publicInfoService;

    @GetMapping("/bank-details/read/{id}")
    public BankDetailsDto read(@PathVariable("id") Long id) {
        return publicInfoService.getBankDetailsById(id);
    }

    @PostMapping("/evict")
    public void evict() {
        publicInfoService.evict();
    }
}
