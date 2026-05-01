package com.bank.account.dto.feign;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationDto implements Serializable {

    Long id;
    String country;
    String region;
    String city;
    String district;
    String locality;
    String street;
    String houseNumber;
    String houseBlock;
    String flatNumber;
    Long index;
}
