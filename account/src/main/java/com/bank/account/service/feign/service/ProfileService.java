package com.bank.account.service.feign.service;

import com.bank.account.dto.feign.ProfileDto;

public interface ProfileService {

    ProfileDto getProfileById(Long id);

    void evict();
}
