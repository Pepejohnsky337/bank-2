package com.bank.publicinfo.service.feign.service;

import com.bank.publicinfo.dto.feign.AccountTransferDto;
import com.bank.publicinfo.dto.feign.CardTransferDto;
import com.bank.publicinfo.dto.feign.PhoneTransferDto;

public interface TransferService {

    AccountTransferDto readAccount(Long id);

    CardTransferDto readCard(Long id);

    PhoneTransferDto readPhone(Long id);

    void evict();
}
