package com.bank.transfer.feign.profile;

import com.bank.transfer.dto.external.profile.AccountDetailsIdDto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-app", path = "/api/profile")
public interface ProfileClient {

    /**
     * Получение деталей счёта по идентификатору.
     * Вызывает endpoint /account/details/read/{id} в микросервисе profile.
     *
     * @param id идентификатор записи account_details_id
     * @return DTO с подробной информацией, включая профиль
     */
    @GetMapping("/account/details/read/{id}")
    AccountDetailsIdDto getAccountDetailsById(@PathVariable("id") Long id);
}
