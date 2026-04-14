package com.bank.antifraud.service.feign;

import com.bank.antifraud.dto.external.transfer.ExternalAccountTransferDto;
import com.bank.antifraud.dto.external.transfer.ExternalCardTransferDto;
import com.bank.antifraud.dto.external.transfer.ExternalPhoneTransferDto;

public interface FeignTransferService {
    
    ExternalAccountTransferDto readAccountTransfer(Long id);
    
    ExternalCardTransferDto readCardTransfer(Long id);
    
    ExternalPhoneTransferDto readPhoneTransfer(Long id);
    
}
