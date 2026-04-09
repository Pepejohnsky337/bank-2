package com.bank.profile.service.impl;

import com.bank.profile.dto.AuditDto;
import com.bank.profile.entity.AuditEntity;
import com.bank.profile.mapper.AuditMapper;
import com.bank.profile.repository.AuditRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {

    private static final String MESSAGE_PREFIX = "Не найден аудит с ID  ";

    @Mock
    private AuditRepository repository;

    @Mock
    private AuditMapper mapper;

    @InjectMocks
    private AuditServiceImpl service;

    @Test
    @DisplayName("поиск по существующему id, успешно")
    void findById_IdIsExist_ReturnsValidDto() {
        Long id = 1L;
        AuditEntity entity = createTestAuditEntity(id);

        AuditDto dto = createTestAuditDto(id);

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        AuditDto result = service.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(repository, times(1)).findById(id);
        verify(mapper, times(1)).toDto(entity);
    }

    @Test
    @DisplayName("поиск по несуществующему id, ошибка")
    void findById_IdIsNotExist_ThrowsEntityNotFoundException() {
        Long id = 999L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> service.findById(id));
        assertEquals(MESSAGE_PREFIX + id, exception.getMessage());
        verify(repository, times(1)).findById(id);
    }

    @Test
    @DisplayName("попытка найти по null id, ошибка")
    void findById_NullId_ThrowsException() {
        when(repository.findById(null)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> service.findById(null));
        verify(repository, times(1)).findById(null);
    }

    private AuditEntity createTestAuditEntity(Long id) {
        AuditEntity auditEntity = new AuditEntity();
        auditEntity.setId(id);
        return auditEntity;
    }

    private AuditDto createTestAuditDto(Long id) {
        AuditDto auditDto = new AuditDto();
        auditDto.setId(id);
        return auditDto;
    }
}