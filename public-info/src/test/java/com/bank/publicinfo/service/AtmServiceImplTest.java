package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.AtmDto;
import com.bank.publicinfo.entity.AtmEntity;
import com.bank.publicinfo.mapper.AtmMapper;
import com.bank.publicinfo.repository.AtmRepository;
import com.bank.publicinfo.service.impl.AtmServiceImpl;
import com.bank.publicinfo.util.EntityNotFoundSupplier;

import static com.bank.publicinfo.utilTest.TestDataFactory.createAtmDto;
import static com.bank.publicinfo.utilTest.TestDataFactory.createAtmEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AtmServiceImplTest {

    @InjectMocks
    private AtmServiceImpl service;

    @Mock
    private AtmRepository repository;

    @Mock
    private EntityNotFoundSupplier supplierNotFound;

    @Mock
    private AtmMapper mapper;

    private static final Long ID = 999L;
    private static final String MESSAGE = "Банкомат не найден с id ";

    @Test
    @DisplayName("Успешное получение списка ATM по списку ID")
    void shouldReturnAtmDtoList_whenAllIdsFound() {

        List<Long> ids = List.of(1L, 2L);

        List<AtmEntity> atms = List.of(
                createAtmEntity(1L, "Address 1"),
                createAtmEntity(2L, "Address 2"));
        List<AtmDto> atmDto = List.of(createAtmDto(1L, "Address 1"), createAtmDto(2L, "Address 2"));

        when(repository.findAllById(ids)).thenReturn(atms);
        doNothing().when(supplierNotFound).checkForSizeAndLogging(anyString(), eq(ids), eq(atms));
        when(mapper.toDtoList(atms)).thenReturn(atmDto);
        List<AtmDto> result = service.findAllById(ids);
        assertThat(result).isEqualTo(atmDto);
        verify(repository, times(1)).findAllById(ids);
        verify(supplierNotFound, times(1)).checkForSizeAndLogging(anyString(), eq(ids), eq(atms));
        verify(mapper, times(1)).toDtoList(atms);
    }

    @Test
    @DisplayName("Ошибка при получении списка ATM по несуществующим ID")
    void shouldReturnEntityNotFoundException_whenAllIdsNotFound() {

        List<Long> ids = List.of(1L, 2L);
        List<AtmEntity> emptyAtms = List.of();
        when(repository.findAllById(ids)).thenReturn(emptyAtms);
        doThrow(new EntityNotFoundException(MESSAGE + ids))
                .when(supplierNotFound)
                .checkForSizeAndLogging(anyString(), eq(ids), eq(emptyAtms));

        assertThatThrownBy(() -> service.findAllById(ids))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(MESSAGE);
        verify(repository, times(1)).findAllById(ids);
        verify(supplierNotFound, times(1)).checkForSizeAndLogging(anyString(), eq(ids), eq(emptyAtms));
    }

    @Test
    @DisplayName("Успешное создание ATM")
    void shouldCreateUser() {
        AtmEntity atmEntity = createAtmEntity(1L, "Address 1");
        AtmDto atmDto = createAtmDto(1L, "Address 1");
        when(mapper.toEntity(atmDto)).thenReturn(atmEntity);
        when(mapper.toDto(atmEntity)).thenReturn(atmDto);
        when(repository.save(atmEntity)).thenReturn(atmEntity);
        AtmDto saveDto = service.create(atmDto);
        assertThat(saveDto).isEqualTo(atmDto);
        verify(mapper, times(1)).toEntity(atmDto);
        verify(repository, times(1)).save(atmEntity);
        verify(mapper, times(1)).toDto(atmEntity);
    }


    @Test
    @DisplayName("Ошибка при создании ATM с null полем")
    void exceptionShouldThrownFieldNull() {
        AtmEntity invalidEntity = createAtmEntity(1L, null);
        AtmDto invalidDto = createAtmDto(1L, "Address 1");
        when(mapper.toEntity(invalidDto)).thenReturn(invalidEntity);
        when(repository.save(invalidEntity)).
                thenThrow(new NullPointerException("NOT NULL constraint failed"));

        assertThatThrownBy(() -> service.create(invalidDto)).isInstanceOf(NullPointerException.class);
        verify(mapper, times(1)).toEntity(invalidDto);
        verify(repository, times(1)).save(invalidEntity);
    }

    @Test
    @DisplayName("Успешное обновление ATM")
    void shouldReturnUpdateEntityDto() {
        AtmEntity existingEntity = createAtmEntity(1L, "Address 1");
        AtmDto updateDto = createAtmDto(1L, "Address 2");
        AtmEntity updatedEntity = createAtmEntity(1L, "Address 2");
        AtmDto updatedDto = createAtmDto(1L, "Address 2");

        when(repository.findById(1L)).thenReturn(Optional.of(existingEntity));
        when(mapper.mergeToEntity(updateDto, existingEntity)).thenReturn(updatedEntity);
        when(mapper.toDto(updatedEntity)).thenReturn(updatedDto);

        AtmDto result = service.update(1L, updateDto);

        assertThat(result).isEqualTo(updatedDto);
        assertThat(result.getAddress()).isEqualTo("Address 2");

        verify(repository, times(1)).findById(1L);
        verify(mapper, times(1)).mergeToEntity(updateDto, existingEntity);
        verify(mapper, times(1)).toDto(updatedEntity);
    }


    @Test
    @DisplayName("Ошибка при обновлении несуществующего ATM")
    void shouldThrowExceptionByReturnUpdateEntityDto() {

        AtmDto updateDto = createAtmDto(ID, "Address 1");

        when(repository.findById(ID)).thenReturn(Optional.empty());
        when(supplierNotFound.getException(MESSAGE, ID))
                .thenReturn(new EntityNotFoundException(MESSAGE + ID));

        assertThatThrownBy(() -> service.update(ID, updateDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(MESSAGE);

        verify(repository, times(1)).findById(ID);
        verify(supplierNotFound, times(1)).getException(MESSAGE, ID);
        verify(mapper, never()).mergeToEntity(any(), any());
    }

    @Test
    @DisplayName("Успешное получение ATM по ID")
    void shouldFindEntityById() {

        AtmEntity entity = createAtmEntity(1L, "Address 1");
        AtmDto atmDto = createAtmDto(ID, "Address 1");
        when(repository.findById(ID)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(atmDto);
        AtmDto dtoById = service.findById(ID);
        assertThat(dtoById).isEqualTo(atmDto);
        verify(repository, times(1)).findById(ID);
        verify(mapper, times(1)).toDto(entity);
    }

    @Test
    @DisplayName("Ошибка при получении несуществующего ATM")
    void shouldThrowExceptionByFindById() {
        Long id = 999L;
        when(repository.findById(id)).thenReturn(Optional.empty());
        when(supplierNotFound.getException(MESSAGE, id)).thenReturn(new EntityNotFoundException(MESSAGE + id));
        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(MESSAGE);
        verify(repository, times(1)).findById(id);
        verify(supplierNotFound, times(1)).getException(MESSAGE, id);
    }
}
