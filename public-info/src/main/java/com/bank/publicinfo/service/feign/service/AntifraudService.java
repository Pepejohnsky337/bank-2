package com.bank.publicinfo.service.feign.service;

import com.bank.publicinfo.dto.feign.SuspiciousAccountTransferDto;
import com.bank.publicinfo.dto.feign.SuspiciousCardTransferDto;
import com.bank.publicinfo.dto.feign.SuspiciousPhoneTransferDto;

public interface AntifraudService {

    SuspiciousAccountTransferDto readAccount(Long id);

    SuspiciousCardTransferDto readCard(Long id);

    SuspiciousPhoneTransferDto readPhone(Long id);
}
