package com.bank.account.service;

import com.bank.account.dto.AccountDetailsDto;
import com.bank.account.entity.AccountDetailsEntity;
import com.bank.account.mapper.AccountDetailsMapper;
import com.bank.account.repository.AccountDetailsRepository;
import com.bank.account.service.common.ExceptionReturner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccountDetailsServiceImpl Test")
class AccountDetailsServiceImplTest {

    @Mock
    private AccountDetailsRepository repository;

    @Mock
    private AccountDetailsMapper mapper;

    @Mock
    private ExceptionReturner exceptionReturner;

    @InjectMocks
    private AccountDetailsServiceImpl service;

    private AccountDetailsEntity entity;
    private AccountDetailsDto dto;

    @BeforeEach
    void setUp() {

        entity = new AccountDetailsEntity();
        entity.setId(1L);
        entity.setPassportId(12345L);
        entity.setAccountNumber(4081781009999L);
        entity.setBankDetailsId(100L);
        entity.setMoney(BigDecimal.valueOf(1000.00));
        entity.setNegativeBalance(false);
        entity.setProfileId(1L);

        dto = new AccountDetailsDto();
        dto.setId(1L);
        dto.setPassportId(12345L);
        dto.setAccountNumber(4081781009999L);
        dto.setBankDetailsId(100L);
        dto.setMoney(BigDecimal.valueOf(1000.00));
        dto.setNegativeBalance(false);
        dto.setProfileId(1L);
    }

    @Test
    @DisplayName("findById - позитивный: успешное получение по ID")
    void findById_WithValidId_ReturnsDto() {

        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        AccountDetailsDto result = service.findById(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getPassportId()).isEqualTo(12345L);
        assertThat(result.getMoney()).isEqualTo(BigDecimal.valueOf(1000.00));

        verify(repository,times(1)).findById(id);
        verify(mapper,times(1)).toDto(entity);
        verify(exceptionReturner, never()).getEntityNotFoundException(anyString());
    }

    @Test
    @DisplayName("findById - негативный: ID не найден, выбрасывает исключение")
    void findById_WithNonExistentId_ThrowsException() {
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
        verify(mapper,never()).toDto(any());
    }

    @Test
    @DisplayName("findAllById - позитивный: успешное получение списка по ID")
    void findAllById_WithValidIds_ReturnsListOfDto() {

        List<Long> ids = List.of(1L, 2L);

        AccountDetailsEntity entity2 = new AccountDetailsEntity();
        entity2.setId(2L);
        entity2.setPassportId(22222L);
        entity2.setMoney(BigDecimal.valueOf(2000.00));

        AccountDetailsDto dto2 = new AccountDetailsDto();
        dto2.setId(2L);
        dto2.setPassportId(22222L);
        dto2.setMoney(BigDecimal.valueOf(2000.00));

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.findById(2L)).thenReturn(Optional.of(entity2));

        List<AccountDetailsEntity> entities = List.of(entity, entity2);
        List<AccountDetailsDto> expectedDtos = List.of(dto, dto2);
        when(mapper.toDtoList(entities)).thenReturn(expectedDtos);

        List<AccountDetailsDto> result = service.findAllById(ids);

        assertThat(result).isNotNull();
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getPassportId()).isEqualTo(12345L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getPassportId()).isEqualTo(22222L);

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).findById(2L);
        verify(mapper, times(1)).toDtoList(entities);
        verify(mapper, never()).toDto(any());
        verify(exceptionReturner, never()).getEntityNotFoundException(anyString());
    }

    @Test
    @DisplayName("findAllById - негативный: один из ID не найден, выбрасывает исключение")
    void findAllById_WithOneNonExistentId_ThrowsException() {

        List<Long> ids = List.of(1L, 999L);
        EntityNotFoundException  expectedException = new EntityNotFoundException("Не существующий id = 999");

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.findById(999L)).thenReturn(Optional.empty());
        when(exceptionReturner.getEntityNotFoundException("Не существующий id = 999"))
                .thenReturn(expectedException);

        assertThatThrownBy(() -> service.findAllById(ids))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Не существующий id = 999");

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).findById(999L);
        verify(exceptionReturner, times(1)).getEntityNotFoundException("Не существующий id = 999");
        verify(mapper, never()).toDtoList(anyList());
    }

    @Test
    @DisplayName("save - позитивный: успешное сохранение")
    void save_WithValidDto_ReturnsSavedDto() {

        AccountDetailsDto inputDto = new AccountDetailsDto();
        inputDto.setPassportId(12345L);
        inputDto.setAccountNumber(4081781009999L);
        inputDto.setBankDetailsId(100L);
        inputDto.setMoney(BigDecimal.valueOf(1000.00));
        inputDto.setNegativeBalance(false);
        inputDto.setProfileId(1L);

        AccountDetailsEntity entityWithoutId = new  AccountDetailsEntity();

        AccountDetailsEntity savedEntity = new AccountDetailsEntity();
        savedEntity.setId(10L);
        savedEntity.setPassportId(12345L);
        savedEntity.setAccountNumber(4081781009999L);

        AccountDetailsDto expectedDto = new AccountDetailsDto();
        expectedDto.setId(10L);
        expectedDto.setPassportId(12345L);
        expectedDto.setAccountNumber(4081781009999L);

        when(mapper.toEntity(inputDto)).thenReturn(entityWithoutId);
        when(repository.save(entityWithoutId)).thenReturn(savedEntity);
        when(mapper.toDto(savedEntity)).thenReturn(expectedDto);

        AccountDetailsDto result = service.save(inputDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getPassportId()).isEqualTo(12345L);

        verify(mapper, times(1)).toEntity(inputDto);
        verify(repository, times(1)).save(entityWithoutId);
        verify(mapper, times(1)).toDto(savedEntity);
    }

    @Test
    @DisplayName("save - негативный: репозиторий выбросил исключение")
    void save_WhenRepositoryThrowsException_ThrowsException() {

        AccountDetailsDto inputDto = new AccountDetailsDto();
        inputDto.setPassportId(12345L);

        when(mapper.toEntity(inputDto)).thenReturn(new AccountDetailsEntity());
        when(repository.save(any(AccountDetailsEntity.class)))
                .thenThrow(new RuntimeException("Database error"));

        assertThatThrownBy(() -> service.save(inputDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database error");

        verify(mapper, times(1)).toEntity(inputDto);
        verify(repository, times(1)).save(any());
        verify(mapper, never()).toDto(any());
    }

    @Test
    @DisplayName("update - позитивный: успешное обновление")
    void update_WithValidData_ReturnsUpdatedDto() {

        Long id = 1L;
        AccountDetailsDto updateDto = new AccountDetailsDto();
        updateDto.setPassportId(99999L);
        updateDto.setAccountNumber(999999999L);
        updateDto.setMoney(BigDecimal.valueOf(5000.00));
        updateDto.setNegativeBalance(true);

        AccountDetailsEntity updatedEntity = new AccountDetailsEntity();
        updatedEntity.setId(id);
        updatedEntity.setPassportId(99999L);
        updatedEntity.setAccountNumber(999999999L);
        updatedEntity.setMoney(BigDecimal.valueOf(5000.00));
        updatedEntity.setNegativeBalance(true);

        AccountDetailsDto expectedDto = new AccountDetailsDto();
        expectedDto.setId(id);
        expectedDto.setPassportId(99999L);
        expectedDto.setAccountNumber(999999999L);
        expectedDto.setMoney(BigDecimal.valueOf(5000.00));
        expectedDto.setNegativeBalance(true);

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.mergeToEntity(entity, updateDto)).thenReturn(updatedEntity);
        when(repository.save(updatedEntity)).thenReturn(updatedEntity);
        when(mapper.toDto(updatedEntity)).thenReturn(expectedDto);


        AccountDetailsDto result = service.update(id, updateDto);


        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getPassportId()).isEqualTo(99999L);
        assertThat(result.getMoney()).isEqualTo(BigDecimal.valueOf(5000.00));
        assertThat(result.getNegativeBalance()).isTrue();

        verify(repository, times(1)).findById(id);
        verify(mapper, times(1)).mergeToEntity(entity, updateDto);
        verify(repository, times(1)).save(updatedEntity);
        verify(mapper, times(1)).toDto(updatedEntity);
    }

    @Test
    @DisplayName("update - негативный: ID не найден, выбрасывает исключение")
    void update_WithNonExistentId_ThrowsException() {

        Long nonExistentId = 999L;
        AccountDetailsDto updateDto = new AccountDetailsDto();
        updateDto.setPassportId(99999L);

        EntityNotFoundException expectedException = new EntityNotFoundException("Не существующий id = 999");

        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());
        when(exceptionReturner.getEntityNotFoundException("Не существующий id = " + nonExistentId))
                .thenReturn(expectedException);


        assertThatThrownBy(() -> service.update(nonExistentId, updateDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Не существующий id = 999");

        verify(repository, times(1)).findById(nonExistentId);
        verify(exceptionReturner, times(1)).getEntityNotFoundException("Не существующий id = " + nonExistentId);
        verify(mapper, never()).mergeToEntity(any(), any());
        verify(repository, never()).save(any());
    }
}

