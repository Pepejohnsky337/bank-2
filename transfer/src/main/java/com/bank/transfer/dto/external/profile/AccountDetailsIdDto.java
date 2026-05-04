package com.bank.transfer.dto.external.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetailsIdDto {
    private Long id;
    private Long accountId;
    private ProfileDto profile;
}
