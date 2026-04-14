package com.bank.antifraud.controller.feign;

import com.bank.antifraud.dto.external.transfer.ExternalAccountTransferDto;
import com.bank.antifraud.dto.external.transfer.ExternalCardTransferDto;
import com.bank.antifraud.dto.external.transfer.ExternalPhoneTransferDto;
import com.bank.antifraud.feign.transfer.FeignTransferClient;
import com.bank.antifraud.service.feign.FeignTransferService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

/**
 * Контроллер для {@link FeignTransferClient}
 */
@RestController
@RequiredArgsConstructor
public class FeignTransferController {
    
    private final FeignTransferService feignTransferService;
    
    /**
     *
     * @param accountTransferId технический идентификатор
     * @return {@link ExternalAccountTransferDto}
     */
    @GetMapping("/account/read/{id}")
    public ExternalAccountTransferDto readAccountTransfer(@PathVariable("id") Long accountTransferId) {
        return feignTransferService.readAccountTransfer(accountTransferId);
    }
    
    /**
     *
     * @param cardTransferId технический идентификатор
     * @return {@link ExternalCardTransferDto}
     */
    @GetMapping("/card/read/{id}")
    public ExternalCardTransferDto readCardTransfer(@PathVariable("id") Long cardTransferId) {
        return feignTransferService.readCardTransfer(cardTransferId);
    }
    
    /**
     *
     * @param phoneTransferId технический идентификатор
     * @return {@link ExternalPhoneTransferDto}
     */
    @GetMapping("/phone/read/{id}")
    public ExternalPhoneTransferDto readPhoneTransfer(@PathVariable("id") Long phoneTransferId) {
        return feignTransferService.readPhoneTransfer(phoneTransferId);
    }
    
}
