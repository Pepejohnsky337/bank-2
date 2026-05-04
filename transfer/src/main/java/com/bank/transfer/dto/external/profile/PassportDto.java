package com.bank.transfer.dto.external.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassportDto {
    private Long id;
    private Integer series;
    private Long number;
    private String lastName;
    private String firstName;
    private String middleName;
    private String gender;
    private LocalDate birthDate;
    private String birthPlace;
    private String issuedBy;
    private LocalDate dateOfIssue;
    private Integer divisionCode;
    private LocalDate expirationDate;
    private RegistrationDto registration;
}
