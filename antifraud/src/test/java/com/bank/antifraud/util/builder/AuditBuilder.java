package com.bank.antifraud.util.builder;

import com.bank.antifraud.dto.AuditDto;
import com.bank.antifraud.entity.AuditEntity;

import java.sql.Timestamp;

public class AuditBuilder {
    private static final Long DEFAULT_ID = null;
    private static final String DEFAULT_ENTITY_TYPE = "UNKNOWN";
    private static final String DEFAULT_OPERATION_TYPE = "NONE";
    private static final String DEFAULT_CREATED_BY = "SYSTEM";
    private static final String DEFAULT_MODIFIED_BY = "SYSTEM";
    private static final Timestamp DEFAULT_CREATED_AT = new Timestamp(0L);
    private static final Timestamp DEFAULT_MODIFIED_AT = new Timestamp(0L);
    private static final String DEFAULT_NEW_ENTITY_JSON = "{}";
    private static final String DEFAULT_ENTITY_JSON = "{}";
    
    private Long id;
    private String entityType;
    private String operationType;
    private String createdBy;
    private String modifiedBy;
    private Timestamp createdAt;
    private Timestamp modifiedAt;
    private String newEntityJson;
    private String entityJson;
    
    public AuditBuilder() {
        reset();
    }
    
    public AuditBuilder reset() {
        this.id = DEFAULT_ID;
        this.entityType = DEFAULT_ENTITY_TYPE;
        this.operationType = DEFAULT_OPERATION_TYPE;
        this.createdBy = DEFAULT_CREATED_BY;
        this.modifiedBy = DEFAULT_MODIFIED_BY;
        this.createdAt = DEFAULT_CREATED_AT;
        this.modifiedAt = DEFAULT_MODIFIED_AT;
        this.newEntityJson = DEFAULT_NEW_ENTITY_JSON;
        this.entityJson = DEFAULT_ENTITY_JSON;
        return this;
    }
    
    public AuditBuilder id(Long id) {
        this.id = id;
        return this;
    }
    
    public AuditBuilder entityType(String entityType) {
        this.entityType = entityType;
        return this;
    }
    
    public AuditBuilder createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }
    
    public AuditBuilder modifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }
    
    public AuditBuilder createdAt(Timestamp createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    
    public AuditBuilder newEntityJson(String newEntityJson) {
        this.newEntityJson = newEntityJson;
        return this;
    }
    
    public AuditBuilder entityJson(String entityJson) {
        this.entityJson = entityJson;
        return this;
    }
    
    
    public AuditEntity buildEntity() {
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
    
    public AuditDto buildDto() {
        AuditDto dto = new AuditDto();
        dto.setId(id);
        dto.setEntityType(entityType);
        dto.setOperationType(operationType);
        dto.setCreatedBy(createdBy);
        dto.setModifiedBy(modifiedBy);
        dto.setCreatedAt(createdAt);
        dto.setModifiedAt(modifiedAt);
        dto.setNewEntityJson(newEntityJson);
        dto.setEntityJson(entityJson);
        return dto;
    }
}
