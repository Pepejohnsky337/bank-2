package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.entity.BankDetailsEntity;
import com.bank.publicinfo.mapper.BankDetailsMapper;
import com.bank.publicinfo.repository.BankDetailsRepository;
import com.bank.publicinfo.service.impl.BankDetailsServiceImpl;
import com.bank.publicinfo.util.EntityNotFoundSupplier;

import static com.bank.publicinfo.utilTest.TestDataFactory.createBankDetailsDto;
import static com.bank.publicinfo.utilTest.TestDataFactory.createBankDetailsEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
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

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BankDetailsServiceImplTest {

    @InjectMocks
    private BankDetailsServiceImpl service;

    @Mock
    private BankDetailsRepository repository;

    @Mock
    private BankDetailsMapper mapper;

    @Mock
    private EntityNotFoundSupplier supplierNotFound;

    private static final Long ID = 1L;
    private static final Long NON_EXISTING_ID = 999L;

    private static final String MESSAGE = "Информации о банке не найдено с id ";

    @Test
    @DisplayName("Успешное получение списка BankDetails по списку ID")
    void shouldReturnBankDetailsDtoList_whenAllIdsFound() {
        List<Long> ids = List.of(1L, 2L);
        List<BankDetailsEntity> entityList = List.of(new BankDetailsEntity(), new BankDetailsEntity());
        List<BankDetailsDto> dtoList = List.of(new BankDetailsDto(), new BankDetailsDto());

        when(repository.findAllById(ids)).thenReturn(entityList);
        when(mapper.toDtoList(entityList)).thenReturn(dtoList);

        List<BankDetailsDto> result = service.findAllById(ids);

        assertThat(result).isEqualTo(dtoList);
        assertThat(result).hasSize(2);

        verify(repository, times(1)).findAllById(ids);
        verify(mapper, times(1)).toDtoList(entityList);
        verify(supplierNotFound, times(1)).checkForSizeAndLogging(anyString(), eq(ids), eq(entityList));
    }

    @Test
    @DisplayName("Ошибка при получении списка BankDetails по несуществующим ID")
    void shouldThrowException_whenAllIdsNotFound() {
        List<Long> ids = List.of(1L, 2L);
        List<BankDetailsEntity> entityList = List.of();

        when(repository.findAllById(ids)).thenReturn(entityList);
        doThrow(new EntityNotFoundException(MESSAGE + ids))
                .when(supplierNotFound).checkForSizeAndLogging(anyString(), eq(ids), eq(entityList));

        assertThatThrownBy(() -> service.findAllById(ids))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(MESSAGE + ids);

        verify(repository, times(1)).findAllById(ids);
        verify(supplierNotFound, times(1)).checkForSizeAndLogging(anyString(), eq(ids), eq(entityList));
    }

    @Test
    @DisplayName("Успешное создание BankDetails")
    void shouldCreateEntity() {
        BankDetailsDto dto = createBankDetailsDto(1L, "Сбербанк");
        BankDetailsEntity entity = createBankDetailsEntity(1L, "Сбербанк");

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        BankDetailsDto result = service.create(dto);

        assertThat(result).isEqualTo(dto);

        verify(mapper, times(1)).toEntity(dto);
        verify(repository, times(1)).save(entity);
        verify(mapper, times(1)).toDto(entity);
    }

    @Test
    @DisplayName("Ошибка при создании BankDetails (NullPointerException)")
    void shouldThrowExceptionWhenFieldNull() {
        BankDetailsDto dto = createBankDetailsDto(1L, "Сбербанк");
        BankDetailsEntity entity = createBankDetailsEntity(1L, "Сбербанк");

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenThrow(new NullPointerException());

        assertThatThrownBy(() -> service.create(dto))
                .isInstanceOf(NullPointerException.class);

        verify(mapper, times(1)).toEntity(dto);
        verify(repository, times(1)).save(entity);
    }

    @Test
    @DisplayName("Успешное обновление BankDetails")
    void shouldReturnUpdateEntityDto() {
        BankDetailsDto dto = createBankDetailsDto(1L, "Сбербанк");
        BankDetailsEntity entity = createBankDetailsEntity(1L, "Сбербанк");

        when(repository.findById(ID)).thenReturn(Optional.of(entity));
        when(mapper.mergeToEntity(dto, entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        BankDetailsDto result = service.update(ID, dto);
        assertThat(result).isEqualTo(dto);

        verify(repository, times(1)).findById(ID);
        verify(mapper, times(1)).mergeToEntity(dto, entity);
        verify(mapper, times(1)).toDto(entity);
    }

    @Test
    @DisplayName("Ошибка при обновлении несуществующего BankDetails")
    void shouldThrowExceptionWhenUpdateNotFound() {

        BankDetailsDto dto = createBankDetailsDto(1L, "Сбербанк");
        when(repository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());
        when(supplierNotFound.getException(MESSAGE, NON_EXISTING_ID))
                .thenReturn(new EntityNotFoundException(MESSAGE + NON_EXISTING_ID));

        assertThatThrownBy(() -> service.update(NON_EXISTING_ID, dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(MESSAGE);

        verify(repository, times(1)).findById(NON_EXISTING_ID);
        verify(supplierNotFound, times(1)).getException(MESSAGE, NON_EXISTING_ID);
    }

    @Test
    @DisplayName("Успешное получение BankDetails по ID")
    void shouldFindEntityById() {
        BankDetailsDto dto = createBankDetailsDto(1L, "Сбербанк");
        BankDetailsEntity entity = createBankDetailsEntity(1L, "Сбербанк");

        when(repository.findById(ID)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        BankDetailsDto result = service.findById(ID);

        assertThat(result).isEqualTo(dto);

        verify(repository, times(1)).findById(ID);
        verify(mapper, times(1)).toDto(entity);
    }

    @Test
    @DisplayName("Ошибка при получении несуществующего BankDetails")
    void shouldThrowExceptionWhenFindByIdNotFound() {

        when(repository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());
        when(supplierNotFound.getException(MESSAGE, NON_EXISTING_ID))
                .thenReturn(new EntityNotFoundException(MESSAGE + NON_EXISTING_ID));

        assertThatThrownBy(() -> service.findById(NON_EXISTING_ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(MESSAGE);

        verify(repository, times(1)).findById(NON_EXISTING_ID);
        verify(supplierNotFound, times(1)).getException(MESSAGE, NON_EXISTING_ID);
    }
}
