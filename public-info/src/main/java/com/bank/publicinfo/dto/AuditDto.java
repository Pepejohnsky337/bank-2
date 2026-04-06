package com.bank.publicinfo.dto;

import com.bank.publicinfo.entity.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO для {@link AuditEntity}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuditDto implements Serializable {
    Long id;
    String entityType;
    String operationType;
    String createdBy;
    String modifiedBy;
    Timestamp createdAt;
    Timestamp modifiedAt;
    String newEntityJson;
    String entityJson;
}
