package com.bank.transfer.controller.profile;

import com.bank.transfer.dto.external.profile.AccountDetailsIdDto;
import com.bank.transfer.service.profile.ProfileService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/account-details/{id}")
    public ResponseEntity<AccountDetailsIdDto> getAccountDetailsById(@PathVariable Long id) {
        final AccountDetailsIdDto result = profileService.getAccountDetailsById(id);
        return ResponseEntity.ok(result);
    }
}
