package com.bank.transfer.service;

import com.bank.transfer.dto.AuditDto;
import com.bank.transfer.entity.AuditEntity;
import com.bank.transfer.mapper.AuditMapper;
import com.bank.transfer.repository.AuditRepository;
import com.bank.transfer.service.impl.AuditServiceImpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {

    @Mock
    private AuditRepository repository;
    @Mock
    private AuditMapper mapper;
    @InjectMocks
    private AuditServiceImpl service;

    private final Long AUDIT_ID = 1L;
    private final Long AUDIT_ID_NON_EXISTENT = 999L;

    private AuditEntity createAuditEntity(Long id, String entityType, String operationType,
                                          String createdBy, Timestamp createdAt) {
        AuditEntity entity = new AuditEntity();
        entity.setId(id);
        entity.setEntityType(entityType);
        entity.setOperationType(operationType);
        entity.setCreatedBy(createdBy);
        entity.setCreatedAt(createdAt);
        return entity;
    }

    @Test
    @DisplayName("Если аудит существует, возвращается DTO")
    void findById_WhenAuditExists_ShouldReturnDto() {
        String entityType = "AccountTransfer";
        String operationType = "CREATE";
        String createdBy = "admin";
        Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());

        AuditEntity entity = createAuditEntity(
                AUDIT_ID, entityType, operationType, createdBy, createdAt);

        AuditDto expectedDto = new AuditDto();
        expectedDto.setId(AUDIT_ID);
        expectedDto.setEntityType(entityType);
        expectedDto.setOperationType(operationType);
        expectedDto.setCreatedBy(createdBy);
        expectedDto.setCreatedAt(createdAt);

        when(repository.findById(AUDIT_ID)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(expectedDto);

        AuditDto actualDto = service.findById(AUDIT_ID);

        assertThat(actualDto).isNotNull();
        assertThat(actualDto.getId()).isEqualTo(AUDIT_ID);
        assertThat(actualDto.getEntityType()).isEqualTo(entityType);
        assertThat(actualDto.getOperationType()).isEqualTo(operationType);
        assertThat(actualDto.getCreatedBy()).isEqualTo(createdBy);
        verify(repository).findById(AUDIT_ID);
        verify(mapper).toDto(entity);
    }

    @Test
    @DisplayName("Если аудит не существует, выбрасывается EntityNotFoundException")
    void findById_WhenAuditNotExists_ShouldThrowEntityNotFoundException() {
        when(repository.findById(AUDIT_ID_NON_EXISTENT)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.findById(AUDIT_ID_NON_EXISTENT));
        verify(repository).findById(AUDIT_ID_NON_EXISTENT);
    }
}
