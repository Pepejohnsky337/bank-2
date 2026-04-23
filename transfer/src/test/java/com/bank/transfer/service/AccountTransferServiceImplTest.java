package com.bank.transfer.service;

import com.bank.transfer.dto.AccountTransferDto;
import com.bank.transfer.entity.AccountTransferEntity;
import com.bank.transfer.mapper.AccountTransferMapper;
import com.bank.transfer.repository.AccountTransferRepository;
import com.bank.transfer.service.common.EntityNotFoundReturner;
import com.bank.transfer.service.impl.AccountTransferServiceImpl;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountTransferServiceImplTest {

    @Mock
    private AccountTransferRepository repository;
    @Mock
    private AccountTransferMapper mapper;
    @Mock
    private EntityNotFoundReturner notFoundReturner;
    @InjectMocks
    private AccountTransferServiceImpl service;

    private final Long TRANSFER_ID = 1L;
    private final Long TRANSFER_ID_NON_EXISTENT = 999L;
    private final Long ACCOUNT_NUMBER = 123456789L;
    private final BigDecimal AMOUNT = new BigDecimal("100.50");
    private final String PURPOSE = "Test transfer";
    private final Long ACCOUNT_DETAILS_ID = 888L;

    private AccountTransferDto createDto(Long id, Long accountNumber, BigDecimal amount,
                                         String purpose, Long accountDetailsId) {
        return new AccountTransferDto(id, accountNumber, amount, purpose, accountDetailsId);
    }

    private AccountTransferEntity createEntity(Long id, Long accountNumber, BigDecimal amount,
                                               String purpose, Long accountDetailsId) {
        return new AccountTransferEntity(id, accountNumber, amount, purpose, accountDetailsId);
    }

    @Test
    @DisplayName("Если entity существует, возвращается DTO")
    void findById_WhenEntityExists_ShouldReturnDto() {
        AccountTransferEntity entity = createEntity(
                TRANSFER_ID, ACCOUNT_NUMBER, AMOUNT, PURPOSE, ACCOUNT_DETAILS_ID);
        AccountTransferDto expectedDto = createDto(
                TRANSFER_ID, ACCOUNT_NUMBER, AMOUNT, PURPOSE, ACCOUNT_DETAILS_ID);
        when(repository.findById(TRANSFER_ID)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(expectedDto);

        AccountTransferDto actualDto = service.findById(TRANSFER_ID);

        assertThat(actualDto).isNotNull();
        assertThat(actualDto.getId()).isEqualTo(TRANSFER_ID);
        verify(repository).findById(TRANSFER_ID);
        verify(mapper).toDto(entity);
    }

    @Test
    @DisplayName("Если entity не существует, выбрасывается EntityNotFoundException")
    void findById_WhenEntityNotExists_ShouldThrowEntityNotFoundException() {
        when(repository.findById(TRANSFER_ID_NON_EXISTENT)).thenReturn(Optional.empty());
        when(notFoundReturner.getEntityNotFoundException(
                TRANSFER_ID_NON_EXISTENT, "Не найден перевод по номеру счета с ID "))
                .thenReturn(new EntityNotFoundException("Not found"));

        assertThrows(EntityNotFoundException.class,
                () -> service.findById(TRANSFER_ID_NON_EXISTENT));
        verify(repository).findById(TRANSFER_ID_NON_EXISTENT);
        verify(notFoundReturner).getEntityNotFoundException(
                TRANSFER_ID_NON_EXISTENT, "Не найден перевод по номеру счета с ID ");
        verify(mapper, never()).toDto(any());
    }

    @Test
    @DisplayName("Если все ids существуют, возвращается список DTO")
    void findAllById_WhenAllIdsExist_ShouldReturnListOfDtos() {
        List<Long> ids = List.of(1L, 2L);
        AccountTransferEntity entity1 = createEntity(1L, 111L,
                BigDecimal.ONE, "a", 1L);
        AccountTransferEntity entity2 = createEntity(2L, 222L,
                BigDecimal.TEN, "b", 2L);
        AccountTransferDto dto1 = createDto(1L, 111L, BigDecimal.ONE, "a", 1L);
        AccountTransferDto dto2 = createDto(2L, 222L, BigDecimal.TEN, "b", 2L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity1));
        when(repository.findById(2L)).thenReturn(Optional.of(entity2));
        when(mapper.toDto(entity1)).thenReturn(dto1);
        when(mapper.toDto(entity2)).thenReturn(dto2);

        List<AccountTransferDto> result = service.findAllById(ids);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
        verify(repository).findById(1L);
        verify(repository).findById(2L);
        verify(mapper).toDto(entity1);
        verify(mapper).toDto(entity2);
    }

    @Test
    @DisplayName("Если один из ids не существует, выбрасывается EntityNotFoundException")
    void findAllById_WhenOneIdNotExists_ShouldThrowEntityNotFoundException() {
        List<Long> ids = List.of(1L, 999L);
        AccountTransferEntity entity1 = createEntity(1L, 111L,
                BigDecimal.ONE, "a", 1L);
        when(repository.findById(1L)).thenReturn(Optional.of(entity1));
        when(repository.findById(999L)).thenReturn(Optional.empty());
        when(notFoundReturner.getEntityNotFoundException(
                999L, "Не найден перевод по номеру счета с ID "))
                .thenReturn(new EntityNotFoundException("Not found"));

        assertThrows(EntityNotFoundException.class, () -> service.findAllById(ids));

        verify(repository).findById(1L);
        verify(repository).findById(999L);
    }

    @Test
    @DisplayName("При сохранении валидного DTO возвращается DTO с сгенерированным ID")
    void save_WhenDtoValid_ShouldReturnDtoWithGeneratedId() {
        AccountTransferDto inputDto = createDto(
                null, ACCOUNT_NUMBER, AMOUNT, PURPOSE, ACCOUNT_DETAILS_ID);
        AccountTransferEntity entityToSave = createEntity(
                null, ACCOUNT_NUMBER, AMOUNT, PURPOSE, ACCOUNT_DETAILS_ID);
        AccountTransferEntity savedEntity = createEntity(
                TRANSFER_ID, ACCOUNT_NUMBER, AMOUNT, PURPOSE, ACCOUNT_DETAILS_ID);
        AccountTransferDto expectedDto = createDto(
                TRANSFER_ID, ACCOUNT_NUMBER, AMOUNT, PURPOSE, ACCOUNT_DETAILS_ID);

        when(mapper.toEntity(inputDto)).thenReturn(entityToSave);
        when(repository.save(entityToSave)).thenReturn(savedEntity);
        when(mapper.toDto(savedEntity)).thenReturn(expectedDto);

        AccountTransferDto result = service.save(inputDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(TRANSFER_ID);
        verify(mapper).toEntity(inputDto);
        verify(repository).save(entityToSave);
        verify(mapper).toDto(savedEntity);
    }

    @Test
    @DisplayName("Если маппер не может преобразовать DTO в Entity, выбрасывается исключение")
    void save_WhenMapperFails_ShouldThrowException() {
        AccountTransferDto inputDto = new AccountTransferDto();
        when(mapper.toEntity(inputDto)).thenThrow(new RuntimeException("Mapping error"));

        assertThrows(RuntimeException.class, () -> service.save(inputDto));
        verify(mapper).toEntity(inputDto);
        verify(repository, never()).save(any());
        verify(mapper, never()).toDto(any());
    }

    @Test
    @DisplayName("При обновлении существующей записи возвращается обновлённый DTO")
    void update_WhenEntityExists_ShouldReturnUpdatedDto() {
        AccountTransferDto updateDto = createDto(null, 999L,
                new BigDecimal("200.00"), "Updated purpose", 777L);
        AccountTransferEntity existingEntity = createEntity(
                TRANSFER_ID, ACCOUNT_NUMBER, AMOUNT, PURPOSE, ACCOUNT_DETAILS_ID);
        AccountTransferEntity mergedEntity = createEntity(TRANSFER_ID, 999L,
                new BigDecimal("200.00"), "Updated purpose", 777L);
        AccountTransferDto expectedDto = createDto(TRANSFER_ID, 999L,
                new BigDecimal("200.00"), "Updated purpose", 777L);

        when(repository.findById(TRANSFER_ID)).thenReturn(Optional.of(existingEntity));
        when(mapper.mergeToEntity(updateDto, existingEntity)).thenReturn(mergedEntity);
        when(repository.save(mergedEntity)).thenReturn(mergedEntity);
        when(mapper.toDto(mergedEntity)).thenReturn(expectedDto);

        AccountTransferDto result = service.update(TRANSFER_ID, updateDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(TRANSFER_ID);
        assertThat(result.getAccountNumber()).isEqualTo(999L);
        verify(repository).findById(TRANSFER_ID);
        verify(mapper).mergeToEntity(updateDto, existingEntity);
        verify(repository).save(mergedEntity);
        verify(mapper).toDto(mergedEntity);
    }

    @Test
    @DisplayName("Если запись не найдена, выбрасывается EntityNotFoundException")
    void update_WhenEntityNotFound_ShouldThrowEntityNotFoundException() {
        AccountTransferDto updateDto = new AccountTransferDto();
        when(repository.findById(TRANSFER_ID_NON_EXISTENT)).thenReturn(Optional.empty());
        when(notFoundReturner.getEntityNotFoundException(
                TRANSFER_ID_NON_EXISTENT, "Не найден перевод по номеру счета с ID "))
                .thenReturn(new EntityNotFoundException());

        assertThrows(EntityNotFoundException.class,
                () -> service.update(TRANSFER_ID_NON_EXISTENT, updateDto));
        verify(repository).findById(TRANSFER_ID_NON_EXISTENT);
        verify(mapper, never()).mergeToEntity(any(), any());
        verify(repository, never()).save(any());
    }
}
