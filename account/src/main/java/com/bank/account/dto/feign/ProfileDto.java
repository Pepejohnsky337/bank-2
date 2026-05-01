package com.bank.account.dto.feign;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileDto implements Serializable {

    Long id;
    Long phoneNumber;
    String email;
    String nameOnCard;
    Long inn;
    Long snils;
    PassportDto passport;
    ActualRegistrationDto actualRegistration;
}
