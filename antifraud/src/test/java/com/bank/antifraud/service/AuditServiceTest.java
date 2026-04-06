package com.bank.antifraud.service;

import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import com.bank.antifraud.dto.AuditDto;
import com.bank.antifraud.entity.AuditEntity;
import com.bank.antifraud.mappers.AuditMapper;
import com.bank.antifraud.repository.AuditRepository;
import com.bank.antifraud.service.impl.AuditServiceImpl;
import com.bank.antifraud.util.builder.AuditBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.anyLong;

@ExtendWith(MockitoExtension.class)
class AuditServiceTest {
    
    @Mock
    private AuditRepository auditRepository;
    @Mock
    private AuditMapper auditMapper;
    @InjectMocks
    private AuditServiceImpl auditService;
    
    @Test
    @DisplayName("Если AuditEntity существует, то возвращается AuditDto с тем же ID")
    void findAuditById_WhenExists_ThenReturnsAudit() {
        final Long expectedId = 111L;
        AuditBuilder builder = new AuditBuilder();
        AuditEntity auditEntity = builder.id(expectedId).buildEntity();
        AuditDto expectedDto = builder.buildDto();

        when(auditRepository.findById(expectedId)).thenReturn(Optional.of(auditEntity));
        when(auditMapper.toDto(auditEntity)).thenReturn(expectedDto);

        AuditDto actualDto = auditService.findById(expectedId);

        assertThat(actualDto)
            .isNotNull()
            .hasFieldOrPropertyWithValue("id", expectedId);

        verify(auditRepository).findById(expectedId);
        verify(auditMapper).toDto(auditEntity);
        verifyNoMoreInteractions(auditRepository, auditMapper);
    }
    
    @Test
    @DisplayName("Если AuditEntity с ID не существует, то выбрасывается EntityNotFoundException")
    void findAuditById_WhenNotExists_ThenReturnEntityNotFoundException() {
        when(auditRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(
            EntityNotFoundException.class,
            () -> auditService.findById(anyLong())
        );
    }
    
}