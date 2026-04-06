package com.bank.publicinfo.dto;

import com.bank.publicinfo.entity.CertificateEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;


import java.io.Serializable;

/**
 * DTO для {@link CertificateEntity}
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CertificateDto implements Serializable {
    Long id;
    Byte[] photoCertificate;
    BankDetailsDto bankDetails;
}
