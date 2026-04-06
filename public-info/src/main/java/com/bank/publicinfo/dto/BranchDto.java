package com.bank.publicinfo.dto;

import com.bank.publicinfo.entity.BranchEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * DTO для {@link BranchEntity}
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BranchDto implements Serializable {
    Long id;
    String address;
    Long phoneNumber;
    String city;
    LocalTime startOfWork;
    LocalTime endOfWork;
}
