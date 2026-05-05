package com.bank.account.controller.feign.controller;

import com.bank.account.dto.feign.ProfileDto;
import com.bank.account.service.feign.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/read/{id}")
    public ProfileDto read(@PathVariable("id") Long id) {
        return profileService.getProfileById(id);
    }

    @PostMapping("/evict")
    public void evict() {
        profileService.evict();
    }
}
