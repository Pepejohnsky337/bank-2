package com.bank.antifraud.dto.external.transfer;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExternalPhoneTransferDto implements Serializable {
    Long id;
    
    Long phoneNumber;
    
    BigDecimal amount;
    
    String purpose;
    Long accountDetailsId;
}
