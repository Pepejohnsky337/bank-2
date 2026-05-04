package com.bank.transfer.service.profile.impl;

import com.bank.transfer.dto.external.profile.AccountDetailsIdDto;
import com.bank.transfer.feign.profile.ProfileClient;
import com.bank.transfer.service.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileClient profileClient;

    @Override
    public AccountDetailsIdDto getAccountDetailsById(Long id) {
        log.info("Запрос данных из profile по accountDetailsId = {}", id);
        return profileClient.getAccountDetailsById(id);
    }
}
