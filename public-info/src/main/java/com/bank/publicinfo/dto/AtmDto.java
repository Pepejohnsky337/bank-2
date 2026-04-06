package com.bank.publicinfo.dto;

import com.bank.publicinfo.entity.AtmEntity;

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
 * DTO для {@link AtmEntity}
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AtmDto implements Serializable {
    Long id;
    String address;
    LocalTime startOfWork;
    LocalTime endOfWork;
    Boolean allHours;
    BranchDto branch;
}
