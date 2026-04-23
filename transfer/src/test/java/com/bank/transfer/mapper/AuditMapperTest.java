package com.bank.transfer.mapper;

import com.bank.transfer.dto.AuditDto;
import com.bank.transfer.entity.AuditEntity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AuditMapperTest {

    private final AuditMapper mapper = Mappers.getMapper(AuditMapper.class);

    private AuditEntity createAuditEntity(Long id, String entityType, String operationType, String createdBy,
                                          String modifiedBy, Timestamp createdAt, Timestamp modifiedAt,
                                          String newEntityJson, String entityJson) {
        AuditEntity entity = new AuditEntity();
        entity.setId(id);
        entity.setEntityType(entityType);
        entity.setOperationType(operationType);
        entity.setCreatedBy(createdBy);
        entity.setModifiedBy(modifiedBy);
        entity.setCreatedAt(createdAt);
        entity.setModifiedAt(modifiedAt);
        entity.setNewEntityJson(newEntityJson);
        entity.setEntityJson(entityJson);
        return entity;
    }

    @Test
    @DisplayName("Должен маппить Entity в DTO")
    void toDto_ShouldMapEntityToDto() {
        Long id = 1L;
        String entityType = "AccountTransfer";
        String operationType = "CREATE";
        String createdBy = "admin";
        String modifiedBy = "user";
        Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());
        Timestamp modifiedAt = Timestamp.valueOf(LocalDateTime.now().plusMinutes(5));
        String newEntityJson = "{\"amount\":100}";
        String entityJson = "{\"amount\":50}";

        AuditEntity entity = createAuditEntity(id, entityType, operationType,
                createdBy, modifiedBy, createdAt, modifiedAt, newEntityJson, entityJson);
        AuditDto dto = mapper.toDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getEntityType()).isEqualTo(entityType);
        assertThat(dto.getOperationType()).isEqualTo(operationType);
        assertThat(dto.getCreatedBy()).isEqualTo(createdBy);
        assertThat(dto.getModifiedBy()).isEqualTo(modifiedBy);
        assertThat(dto.getCreatedAt()).isEqualTo(createdAt);
        assertThat(dto.getModifiedAt()).isEqualTo(modifiedAt);
        assertThat(dto.getNewEntityJson()).isEqualTo(newEntityJson);
        assertThat(dto.getEntityJson()).isEqualTo(entityJson);
    }

    @Test
    @DisplayName("Должен вернуть null при null Entity")
    void toDto_WhenEntityNull_ShouldReturnNull() {
        assertThat(mapper.toDto(null)).isNull();
    }
}
