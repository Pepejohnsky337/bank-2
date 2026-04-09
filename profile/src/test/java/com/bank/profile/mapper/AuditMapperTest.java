package com.bank.profile.mapper;

import com.bank.profile.dto.AuditDto;
import com.bank.profile.entity.AuditEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AuditMapperTest {
    private static final Timestamp CREATED_AT = Timestamp.valueOf("2020-02-20 00:00:00");
    private static final Timestamp MODIFIED_AT = Timestamp.valueOf("2021-01-21 00:00:00");

    private final AuditMapper auditMapper = Mappers.getMapper(AuditMapper.class);

    @Test
    @DisplayName("маппинг в дто со всеми полями")
    void toDto_MapAllFields() {
        AuditEntity auditEntity = createTestAuditEntity();

        AuditDto auditDto = auditMapper.toDto(auditEntity);

        assertEquals(auditDto.getId(), auditEntity.getId());
        assertEquals(auditDto.getEntityType(), auditEntity.getEntityType());
        assertEquals(auditDto.getOperationType(), auditEntity.getOperationType());
        assertEquals(auditDto.getCreatedBy(), auditEntity.getCreatedBy());
        assertEquals(auditDto.getModifiedBy(), auditEntity.getModifiedBy());
        assertEquals(auditDto.getCreatedAt().getTime(), auditEntity.getCreatedAt().getTime());
        assertEquals(auditDto.getModifiedAt().getTime(), auditEntity.getModifiedAt().getTime());
        assertEquals(auditDto.getNewEntityJson(), auditEntity.getNewEntityJson());
        assertEquals(auditDto.getEntityJson(), auditEntity.getEntityJson());
    }

    @Test
    @DisplayName("маппинг в дто из null")
    void toDto_MapNull() {
        AuditDto dto = auditMapper.toDto(null);
        assertNull(dto);
    }

    @Test
    @DisplayName("маппинг в дто с несколькими полями")
    void toDto_MapSomeFields() {
        AuditEntity auditEntity = createTestAuditEntityPart();

        AuditDto auditDto = auditMapper.toDto(auditEntity);

        assertNotNull(auditDto);
        assertEquals(auditDto.getEntityType(), auditEntity.getEntityType());
        assertEquals(auditDto.getOperationType(), auditEntity.getOperationType());
        assertEquals(auditDto.getCreatedBy(), auditEntity.getCreatedBy());
        assertEquals(auditDto.getModifiedBy(), auditEntity.getModifiedBy());
        assertNull(auditDto.getCreatedAt());
        assertNull(auditDto.getModifiedAt());
        assertNull(auditDto.getNewEntityJson());
        assertNull(auditDto.getEntityJson());
    }

    private AuditEntity createTestAuditEntity() {
        AuditEntity auditEntity = new AuditEntity();
        auditEntity.setId(1L);
        auditEntity.setEntityType("entityType");
        auditEntity.setOperationType("operationType");
        auditEntity.setCreatedBy("createdBy");
        auditEntity.setModifiedBy("modifiedBy");
        auditEntity.setCreatedAt(CREATED_AT);
        auditEntity.setModifiedAt(MODIFIED_AT);
        auditEntity.setNewEntityJson("newEntityJson");
        auditEntity.setEntityJson("entityJson");
        return auditEntity;
    }

    private AuditEntity createTestAuditEntityPart() {
        AuditEntity auditEntity = new AuditEntity();
        auditEntity.setId(1L);
        auditEntity.setEntityType("entityType");
        auditEntity.setOperationType("operationType");
        auditEntity.setCreatedBy("createdBy");
        auditEntity.setModifiedBy("modifiedBy");
        return auditEntity;
    }
}