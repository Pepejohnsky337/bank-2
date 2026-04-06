package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.entity.AuditEntity;

import static com.bank.publicinfo.utilTest.TestDataFactory.createAuditEntity;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class AuditMapperTest {

    private final AuditMapper auditMapper = Mappers.getMapper(AuditMapper.class);

    @Test
    @DisplayName("Должен корректно маппить  AuditEntity в AuditDto")
    void toDto_shouldMapAuditEntityToDto() {
        AuditEntity auditEntity = createAuditEntity(1L, "Audit");
        AuditDto dto = auditMapper.toDto(auditEntity);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(auditEntity.getId());
        assertThat(dto.getEntityType()).isEqualTo(auditEntity.getEntityType());
        assertThat(dto.getOperationType()).isEqualTo(auditEntity.getOperationType());
        assertThat(dto.getCreatedBy()).isEqualTo(auditEntity.getCreatedBy());
        assertThat(dto.getModifiedBy()).isEqualTo(auditEntity.getModifiedBy());
        assertThat(dto.getCreatedAt()).isEqualTo(auditEntity.getCreatedAt());
        assertThat(dto.getModifiedAt()).isEqualTo(auditEntity.getModifiedAt());
        assertThat(dto.getNewEntityJson()).isEqualTo(auditEntity.getNewEntityJson());
        assertThat(dto.getEntityJson()).isEqualTo(auditEntity.getEntityJson());
    }

    @Test
    @DisplayName("Маппинг AuditEntity в AuditDto если указаны не все поля")
    void toDto_() {
        AuditEntity auditEntity = createAuditEntity(1L, "Audit");
        AuditDto dto = auditMapper.toDto(auditEntity);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(auditEntity.getId());
        assertThat(dto.getEntityType()).isEqualTo(auditEntity.getEntityType());
        assertThat(dto.getOperationType()).isNull();
        assertThat(dto.getCreatedBy()).isNull();
        assertThat(dto.getModifiedBy()).isNull();
        assertThat(dto.getCreatedAt()).isNull();
        assertThat(dto.getModifiedAt()).isNull();
        assertThat(dto.getNewEntityJson()).isNull();
        assertThat(dto.getEntityJson()).isNull();
    }

    @Test
    @DisplayName("При передаче null в toDto должен возвращаться null")
    void toDto_WhenInputIsNull_ShouldReturnNull() {
        AuditDto dto = auditMapper.toDto(null);
        assertThat(dto).isNull();
    }
}