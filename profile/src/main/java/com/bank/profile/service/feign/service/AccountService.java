package com.bank.profile.service.feign.service;

import com.bank.profile.dto.feign.AccountDetailsDto;

public interface AccountService {

    AccountDetailsDto readAccountDetails(Long id);

}
