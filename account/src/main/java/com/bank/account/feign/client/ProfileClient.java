package com.bank.account.feign.client;

import com.bank.account.dto.feign.ProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PROFILE-APP")
public interface ProfileClient {

    @GetMapping("/api/profile/profile/read/{id}")
    ProfileDto getProfileById(@PathVariable("id") Long id);

}
