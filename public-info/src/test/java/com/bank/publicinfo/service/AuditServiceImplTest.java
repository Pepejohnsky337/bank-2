package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.entity.AuditEntity;
import com.bank.publicinfo.mapper.AuditMapper;
import com.bank.publicinfo.repository.AuditRepository;
import com.bank.publicinfo.service.impl.AuditServiceImpl;

import static com.bank.publicinfo.utilTest.TestDataFactory.createAuditDto;
import static com.bank.publicinfo.utilTest.TestDataFactory.createAuditEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuditServiceImplTest {

    @InjectMocks
    private AuditServiceImpl service;

    @Mock
    private AuditRepository repository;

    @Mock
    private AuditMapper mapper;

    private static final Long ID = 1L;

    @Test
    @DisplayName("Успешное получение Audit по ID")
    void shouldFindAuditById() {
        AuditEntity auditEntity = createAuditEntity(1L, "audit");
        AuditDto auditDto = createAuditDto(1L, "audit");
        Optional<AuditEntity> optionalAuditEntity = Optional.of(auditEntity);
        when(repository.findById(ID)).thenReturn(optionalAuditEntity);
        when(mapper.toDto(auditEntity)).thenReturn(auditDto);
        AuditDto returnDto = service.findById(ID);
        assertThat(returnDto).isEqualTo(auditDto);
        verify(repository, times(1)).findById(ID);
        verify(mapper, times(1)).toDto(auditEntity);
    }

    @Test
    @DisplayName("Ошибка 404 при получении несуществующего Audit")
    void shouldThrowEntityNotFoundExceptionWhenAuditNotFound() {
        final String MESSAGE = "Не найден аудит с ID  " + ID;
        when(repository.findById(ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findById(ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(MESSAGE);
        verify(repository, times(1)).findById(ID);
        verify(mapper, times(0)).toDto(null);
    }
}
