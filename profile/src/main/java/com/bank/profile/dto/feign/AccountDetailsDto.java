package com.bank.profile.dto.feign;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountDetailsDto implements Serializable {

    Long id;
    Long passportId;
    Long accountNumber;
    Long bankDetailsId;
    BigDecimal money;
    Boolean negativeBalance;
    Long profileId;
}
