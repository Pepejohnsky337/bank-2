package com.bank.account.service.feign.service;

import com.bank.account.dto.feign.BankDetailsDto;

public interface PublicInfoService {

    BankDetailsDto getBankDetailsById(Long id);
}
