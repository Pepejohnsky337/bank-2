package com.bank.antifraud.feign.transfer;

import com.bank.antifraud.dto.external.transfer.ExternalAccountTransferDto;
import com.bank.antifraud.dto.external.transfer.ExternalCardTransferDto;
import com.bank.antifraud.dto.external.transfer.ExternalPhoneTransferDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "transfer-app",
        path = "/api/transfer"
)
public interface FeignTransferClient {
    
    /**
     * Возвращает информацию о переводе по номеру счета
     *
     * @param id технический идентификатор
     * @return {@link ExternalAccountTransferDto}
     */
    @GetMapping("/account/read/{id}")
    ExternalAccountTransferDto readAccountTransfer(@PathVariable("id") Long id);
    
    /**
     * Возвращает информацию о переводе по номеру карты
     *
     * @param id технический идентификатор
     * @return {@link ExternalCardTransferDto}
     */
    @GetMapping("/card/read/{id}")
    ExternalCardTransferDto readCardTransfer(@PathVariable("id") Long id);
    
    /**
     * Возвращает информацию о переводе по номеру телефона
     *
     * @param id технический идентификатор
     * @return {@link ExternalPhoneTransferDto}
     */
    @GetMapping("/phone/read/{id}")
    ExternalPhoneTransferDto readPhoneTransfer(@PathVariable("id") Long id);
    
}
