package com.bank.publicinfo.controller.feign.controller;

import com.bank.publicinfo.dto.feign.SuspiciousAccountTransferDto;
import com.bank.publicinfo.dto.feign.SuspiciousCardTransferDto;
import com.bank.publicinfo.dto.feign.SuspiciousPhoneTransferDto;
import com.bank.publicinfo.service.feign.service.AntifraudService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/antifraud")
public class AntifraudController {

    private final AntifraudService antifraudService;

    @GetMapping("/account/read/{id}")
    SuspiciousAccountTransferDto readAccount(@PathVariable("id") Long id) {
        return antifraudService.readAccount(id);
    }

    @GetMapping("/card/read/{id}")
    SuspiciousCardTransferDto readCard(@PathVariable("id") Long id) {
        return antifraudService.readCard(id);
    }

    @GetMapping("/phone/read/{id}")
    SuspiciousPhoneTransferDto readPhone(@PathVariable("id") Long id) {
        return antifraudService.readPhone(id);
    }

    @PostMapping("/evict")
    public void evict() {
        antifraudService.evict();
    }
}
