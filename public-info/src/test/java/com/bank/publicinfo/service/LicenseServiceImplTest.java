package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.LicenseDto;
import com.bank.publicinfo.entity.LicenseEntity;
import com.bank.publicinfo.mapper.LicenseMapper;
import com.bank.publicinfo.repository.LicenseRepository;
import com.bank.publicinfo.service.impl.LicenseServiceImpl;
import com.bank.publicinfo.util.EntityNotFoundSupplier;

import static com.bank.publicinfo.utilTest.TestDataFactory.createLicenseDto;
import static com.bank.publicinfo.utilTest.TestDataFactory.createLicenseEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
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
public class LicenseServiceImplTest {

    @InjectMocks
    private LicenseServiceImpl service;

    @Mock
    private LicenseRepository repository;

    @Mock
    private LicenseMapper mapper;

    @Mock
    private EntityNotFoundSupplier supplierNotFound;

    private static final List<Long> IDS = List.of(1L, 2L);
    private static final Long ID = 1L;
    private static final Long NON_EXISTING_ID = 999L;
    private final static String MESSAGE = "Лицензии не найдено с id ";

    @Test
    @DisplayName("Успешное получение списка лицензий по списку ID")
    void shouldFindAllLicensesByIds() {

        List<LicenseEntity> licenseEntities = List.of(createLicenseEntity(1L), createLicenseEntity(2L));
        List<LicenseDto> licenseDtos = List.of(createLicenseDto(1L), createLicenseDto(2L));

        when(repository.findAllById(IDS)).thenReturn(licenseEntities);
        when(mapper.toDtoList(licenseEntities)).thenReturn(licenseDtos);

        doNothing().when(supplierNotFound).checkForSizeAndLogging(anyString(), eq(IDS), eq(licenseEntities));
        List<LicenseDto> allById = service.findAllById(IDS);

        assertThat(allById).isEqualTo(licenseDtos);

        verify(repository, times(1)).findAllById(IDS);
        verify(mapper, times(1)).toDtoList(licenseEntities);
        verify(supplierNotFound, times(1)).checkForSizeAndLogging(anyString(), eq(IDS), eq(licenseEntities));
    }

    @Test
    @DisplayName("Ошибка при получении списка лицензий по несуществующим ID")
    void shouldThrowEntityNotFoundException_whenAllIdsNotFound() {
        List<Long> ids = List.of(1L, 2L);
        List<LicenseEntity> licenseEntities = List.of();

        when(repository.findAllById(ids)).thenReturn(licenseEntities);
        doThrow(new EntityNotFoundException(MESSAGE + ids))
                .when(supplierNotFound)
                .checkForSizeAndLogging(anyString(), eq(ids), eq(licenseEntities));

        assertThatThrownBy(() -> service.findAllById(ids)).isInstanceOf(EntityNotFoundException.class);

        verify(repository, times(1)).findAllById(ids);
        verify(supplierNotFound, times(1)).checkForSizeAndLogging(anyString(), eq(ids), eq(licenseEntities));
        verify(mapper, never()).toDtoList(any());
    }

    @Test
    @DisplayName("Успешное создание лицензии")
    void shouldCreateLicense() {
        LicenseEntity licenseEntity = createLicenseEntity(1L);
        LicenseDto licenseDto = createLicenseDto(1L);

        when(mapper.toEntity(licenseDto)).thenReturn(licenseEntity);
        when(repository.save(licenseEntity)).thenReturn(licenseEntity);
        when(mapper.toDto(licenseEntity)).thenReturn(licenseDto);

        LicenseDto saveLicenseDto = service.create(licenseDto);
        assertThat(saveLicenseDto).isEqualTo(licenseDto);

        verify(mapper, times(1)).toEntity(licenseDto);
        verify(repository, times(1)).save(licenseEntity);
        verify(mapper, times(1)).toDto(licenseEntity);
    }

    @Test
    @DisplayName("Ошибка при создании лицензии")
    void shouldThrowExceptionWhenSaveFails() {
        LicenseEntity licenseEntity = createLicenseEntity(1L);
        LicenseDto licenseDto = createLicenseDto(1L);

        when(mapper.toEntity(any(LicenseDto.class))).thenReturn(licenseEntity);
        when(repository.save(any(LicenseEntity.class))).thenThrow(new NullPointerException());

        assertThatThrownBy(() -> service.create(licenseDto)).isInstanceOf(NullPointerException.class);

        verify(mapper, times(1)).toEntity(licenseDto);
        verify(repository, times(1)).save(licenseEntity);

    }

    @Test
    @DisplayName("Успешное обновление лицензии")
    void shouldUpdateLicenseSuccessfully() {
        LicenseEntity existingEntity = createLicenseEntity(1L);
        LicenseDto updateDto = createLicenseDto(1L);
        LicenseEntity updatedEntity = createLicenseEntity(1L);
        LicenseDto updatedDto = createLicenseDto(1L);

        when(repository.findById(ID)).thenReturn(Optional.of(existingEntity));
        when(mapper.mergeToEntity(updateDto, existingEntity)).thenReturn(updatedEntity);
        when(mapper.toDto(updatedEntity)).thenReturn(updatedDto);

        LicenseDto result = service.update(ID, updateDto);

        assertThat(result).isEqualTo(updatedDto);
        assertThat(result.getId()).isEqualTo(ID);

        verify(repository, times(1)).findById(ID);
        verify(mapper, times(1)).mergeToEntity(updateDto, existingEntity);
        verify(mapper, times(1)).toDto(updatedEntity);
    }

    @Test
    @DisplayName("Ошибка при обновлении несуществующей лицензии")
    void shouldThrowExceptionWhenLicenseNotFound() {
        LicenseDto licenseDto = createLicenseDto(1L);

        when(repository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());
        when(supplierNotFound.getException(MESSAGE, NON_EXISTING_ID))
                .thenReturn(new EntityNotFoundException(MESSAGE + NON_EXISTING_ID));

        assertThatThrownBy(() -> service.update(NON_EXISTING_ID, licenseDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(MESSAGE);

        verify(repository, times(1)).findById(NON_EXISTING_ID);
        verify(supplierNotFound, times(1)).getException(MESSAGE, NON_EXISTING_ID);

    }

    @Test
    @DisplayName("Успешное получение лицензии по ID")
    void shouldFindLicenseById() {

        LicenseEntity licenseEntity = createLicenseEntity(ID);
        LicenseDto licenseDto = createLicenseDto(ID);

        when(repository.findById(ID)).thenReturn(Optional.of(licenseEntity));
        when(mapper.toDto(licenseEntity)).thenReturn(licenseDto);

        LicenseDto byId = service.findById(ID);

        assertThat(byId).isEqualTo(licenseDto);

        verify(repository, times(1)).findById(ID);
        verify(mapper, times(1)).toDto(licenseEntity);
    }

    @Test
    @DisplayName("Ошибка при получении несуществующей лицензии")
    void shouldThrowEntityNotFoundExceptionWhenLicenseNotFound() {
        when(repository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());
        when(supplierNotFound.getException(MESSAGE, NON_EXISTING_ID)).thenReturn(new EntityNotFoundException(MESSAGE + NON_EXISTING_ID));

        assertThatThrownBy(() -> service.findById(NON_EXISTING_ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(MESSAGE);

        verify(repository, times(1)).findById(NON_EXISTING_ID);
        verify(supplierNotFound, times(1)).getException(MESSAGE, NON_EXISTING_ID);
    }
}
