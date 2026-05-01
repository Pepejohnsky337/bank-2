package com.bank.account.service.feign.service;

import com.bank.account.dto.feign.ProfileDto;
import com.bank.account.feign.client.ProfileClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileClient profileClient;

    @Override
    public ProfileDto getProfileById(Long id) {
        return profileClient.getProfileById(id);
    }
}
