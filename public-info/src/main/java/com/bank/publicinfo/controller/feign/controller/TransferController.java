package com.bank.publicinfo.controller.feign.controller;

import com.bank.publicinfo.dto.feign.AccountTransferDto;
import com.bank.publicinfo.dto.feign.CardTransferDto;
import com.bank.publicinfo.dto.feign.PhoneTransferDto;
import com.bank.publicinfo.service.feign.service.impl.TransferServiceImpl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transfer")
public class TransferController {

    private final TransferServiceImpl transferService;

    @GetMapping("/account/read/{id}")
    AccountTransferDto readAccount(@PathVariable("id") Long id) {
        return transferService.readAccount(id);
    }

    @GetMapping("/card/read/{id}")
    CardTransferDto readCard(@PathVariable("id") Long id) {
        return transferService.readCard(id);
    }

    @GetMapping("/phone/read/{id}")
    PhoneTransferDto readPhone(@PathVariable("id") Long id) {
        return transferService.readPhone(id);
    }

    @PostMapping("/evict")
    public void evict() {
        transferService.evict();
    }
}
