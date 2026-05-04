package com.bank.transfer.service.profile;

import com.bank.transfer.dto.external.profile.AccountDetailsIdDto;

public interface ProfileService {
    AccountDetailsIdDto getAccountDetailsById(Long id);
}
