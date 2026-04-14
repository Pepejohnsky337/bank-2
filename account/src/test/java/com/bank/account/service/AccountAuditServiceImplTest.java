package com.bank.account.service;

import com.bank.account.dto.AuditDto;
import com.bank.account.entity.AuditEntity;
import com.bank.account.mapper.AccountAuditMapper;
import com.bank.account.repository.AccountAuditRepository;
import com.bank.account.service.common.ExceptionReturner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.anyString;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccountAuditService")
class AccountAuditServiceImplTest {

    @Mock
    private AccountAuditRepository repository;

    @Mock
    private AccountAuditMapper mapper;

    @Mock
    private ExceptionReturner exceptionReturner;

    @InjectMocks
    private AccountAuditServiceImpl service;

    @Test
    @DisplayName("findById - позитивный: успешное получение аудита по ID")
    void findById_WithValidId_ReturnsAuditDto() {

        Long auditId = 1L;

        AuditEntity entity = new AuditEntity();
        entity.setId(auditId);
        entity.setEntityType("AccountDetails");
        entity.setOperationType("CREATE");
        entity.setCreatedBy("admin@bank.com");
        entity.setModifiedBy("admin@bank.com");
        entity.setNewEntityJson("{\"accountNumber\":\"12345\"}");
        entity.setEntityJson("{\"id\":1}");

        AuditDto expectedDto = new AuditDto();
        expectedDto.setId(auditId);
        expectedDto.setEntityType("AccountDetails");
        expectedDto.setOperationType("CREATE");
        expectedDto.setCreatedBy("admin@bank.com");
        expectedDto.setModifiedBy("admin@bank.com");
        expectedDto.setNewEntityJson("{\"accountNumber\":\"12345\"}");
        expectedDto.setEntityJson("{\"id\":1}");

        when(repository.findById(auditId)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(expectedDto);

        AuditDto result = service.findById(auditId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(auditId);
        assertThat(result.getEntityType()).isEqualTo("AccountDetails");
        assertThat(result.getOperationType()).isEqualTo("CREATE");
        assertThat(result.getCreatedBy()).isEqualTo("admin@bank.com");
        assertThat(result.getModifiedBy()).isEqualTo("admin@bank.com");
        assertThat(result.getNewEntityJson()).isEqualTo("{\"accountNumber\":\"12345\"}");
        assertThat(result.getEntityJson()).isEqualTo("{\"id\":1}");

        verify(repository, times(1)).findById(auditId);
        verify(mapper, times(1)).toDto(entity);
        verify(exceptionReturner, never()).getEntityNotFoundException(anyString());
    }

    @Test
    @DisplayName("findById - негативный: ID не найден, выбрасывает исключение через ExceptionReturner")
    void findById_WithNonExistentId_ThrowsExceptionViaExceptionReturner() {

        Long nonExistentId = 999L;

        EntityNotFoundException expectedException = new EntityNotFoundException("Не существующий id = 999");

        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());
        when(exceptionReturner.getEntityNotFoundException("Не существующий id = " + nonExistentId))
                .thenReturn(expectedException);

        assertThatThrownBy(() -> service.findById(nonExistentId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Не существующий id = 999");

        verify(repository, times(1)).findById(nonExistentId);
        verify(exceptionReturner, times(1)).getEntityNotFoundException("Не существующий id = " + nonExistentId);
        verify(mapper, never()).toDto(any());
    }
}