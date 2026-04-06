package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.CertificateDto;
import com.bank.publicinfo.entity.CertificateEntity;
import com.bank.publicinfo.mapper.CertificateMapper;
import com.bank.publicinfo.repository.CertificateRepository;
import com.bank.publicinfo.service.impl.CertificateServiceImpl;
import com.bank.publicinfo.util.EntityNotFoundSupplier;

import static com.bank.publicinfo.utilTest.TestDataFactory.createCertificateDto;
import static com.bank.publicinfo.utilTest.TestDataFactory.createCertificateEntity;
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
public class CertificateServiceImplTest {

    @InjectMocks
    private CertificateServiceImpl service;

    @Mock
    private CertificateRepository repository;

    @Mock
    private CertificateMapper mapper;

    @Mock
    private EntityNotFoundSupplier supplierNotFound;

    private static final Long ID = 1L;
    private static final Long NON_EXISTING_ID = 999L;
    private final static String MESSAGE = "Сертификата не найдено с id ";

    @Test
    @DisplayName("Успешное получение списка сертификатов по списку ID")
    void shouldFindAllCertificatesByIds() {
        List<Long> ids = List.of(1L, 2L);
        List<CertificateEntity> certificateEntities = List.of(createCertificateEntity(1L), createCertificateEntity(2L));
        List<CertificateDto> certificateDto = List.of(createCertificateDto(1L), createCertificateDto(2L));

        when(repository.findAllById(ids)).thenReturn(certificateEntities);
        when(mapper.toDtoList(certificateEntities)).thenReturn(certificateDto);
        doNothing().when(supplierNotFound).checkForSizeAndLogging(anyString(), eq(ids), eq(certificateEntities));

        List<CertificateDto> allById = service.findAllById(ids);

        assertThat(allById).isEqualTo(certificateDto);

        verify(repository, times(1)).findAllById(ids);
        verify(mapper, times(1)).toDtoList(certificateEntities);
        verify(supplierNotFound, times(1)).checkForSizeAndLogging(anyString(), eq(ids), eq(certificateEntities));
    }

    @Test
    @DisplayName("Ошибка при получении списка сертификатов по несуществующим ID")
    void shouldThrowEntityNotFoundException_whenAllIdsNotFound() {
        List<Long> ids = List.of(1L, 2L);
        List<CertificateEntity> certificateEntities = List.of();

        when(repository.findAllById(ids)).thenReturn(certificateEntities);

        doThrow(new EntityNotFoundException(MESSAGE + ids))
                .when(supplierNotFound)
                .checkForSizeAndLogging(anyString(), eq(ids), eq(certificateEntities));

        assertThatThrownBy(() -> service.findAllById(ids)).isInstanceOf(EntityNotFoundException.class);

        verify(repository, times(1)).findAllById(ids);
        verify(supplierNotFound, times(1)).checkForSizeAndLogging(anyString(), eq(ids), eq(certificateEntities));
        verify(mapper, never()).toDtoList(any());
    }

    @Test
    @DisplayName("Успешное создание сертификата")
    void shouldCreateCertificate() {
        CertificateEntity certificateEntity = createCertificateEntity(1L);
        CertificateDto certificateDto = createCertificateDto(1L);

        when(mapper.toEntity(certificateDto)).thenReturn(certificateEntity);
        when(repository.save(certificateEntity)).thenReturn(certificateEntity);
        when(mapper.toDto(certificateEntity)).thenReturn(certificateDto);

        CertificateDto saveCertificateDto = service.create(certificateDto);

        assertThat(saveCertificateDto).isEqualTo(certificateDto);

        verify(mapper, times(1)).toEntity(certificateDto);
        verify(repository, times(1)).save(certificateEntity);
        verify(mapper, times(1)).toDto(certificateEntity);
    }

    @Test
    @DisplayName("Ошибка при создании сертификата")
    void shouldThrowExceptionWhenSaveFails() {
        CertificateEntity certificateEntity = createCertificateEntity(1L);
        CertificateDto certificateDto = createCertificateDto(1L);

        when(mapper.toEntity(any(CertificateDto.class))).thenReturn(certificateEntity);
        when(repository.save(any(CertificateEntity.class))).thenThrow(new NullPointerException());

        assertThatThrownBy(() -> service.create(certificateDto)).isInstanceOf(NullPointerException.class);

        verify(mapper, times(1)).toEntity(certificateDto);
        verify(repository, times(1)).save(certificateEntity);
    }

    @Test
    @DisplayName("Успешное обновление сертификата")
    void shouldUpdateCertificateSuccessfully() {
        CertificateEntity existingEntity = createCertificateEntity(1L);
        CertificateDto updateDto = createCertificateDto(1L);
        CertificateEntity updatedEntity = createCertificateEntity(1L);
        CertificateDto updatedDto = createCertificateDto(1L);

        when(repository.findById(ID)).thenReturn(Optional.of(existingEntity));
        when(mapper.mergeToEntity(updateDto, existingEntity)).thenReturn(updatedEntity);
        when(mapper.toDto(updatedEntity)).thenReturn(updatedDto);

        CertificateDto result = service.update(ID, updateDto);

        assertThat(result).isEqualTo(updatedDto);
        assertThat(result.getId()).isEqualTo(ID);

        verify(repository, times(1)).findById(ID);
        verify(mapper, times(1)).mergeToEntity(updateDto, existingEntity);
        verify(mapper, times(1)).toDto(updatedEntity);

    }

    @Test
    @DisplayName("Ошибка при обновлении несуществующего сертификата")
    void shouldThrowExceptionWhenCertificateNotFound() {
        CertificateDto certificateDto = createCertificateDto(1L);

        when(repository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());
        when(supplierNotFound.getException(MESSAGE, NON_EXISTING_ID))
                .thenReturn(new EntityNotFoundException(MESSAGE + NON_EXISTING_ID));

        assertThatThrownBy(() -> service.update(NON_EXISTING_ID, certificateDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(MESSAGE);

        verify(repository, times(1)).findById(NON_EXISTING_ID);
        verify(supplierNotFound, times(1)).getException(MESSAGE, NON_EXISTING_ID);
        verify(mapper, never()).mergeToEntity(any(), any());
    }

    @Test
    @DisplayName("Успешное получение сертификата по ID")
    void shouldFindCertificateById() {
        CertificateEntity certificateEntity = createCertificateEntity(1L);
        CertificateDto certificateDto = createCertificateDto(1L);

        when(repository.findById(ID)).thenReturn(Optional.of(certificateEntity));
        when(mapper.toDto(certificateEntity)).thenReturn(certificateDto);

        CertificateDto byId = service.findById(ID);

        assertThat(byId).isEqualTo(certificateDto);

        verify(repository, times(1)).findById(ID);
        verify(mapper, times(1)).toDto(certificateEntity);
    }

    @Test
    @DisplayName("Ошибка при получении несуществующего сертификата")
    void shouldThrowEntityNotFoundExceptionWhenCertificateNotFound() {
        when(repository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());
        when(supplierNotFound.getException(MESSAGE, NON_EXISTING_ID)).thenReturn(new EntityNotFoundException(MESSAGE + NON_EXISTING_ID));

        assertThatThrownBy(() -> service.findById(NON_EXISTING_ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(MESSAGE);

        verify(repository, times(1)).findById(NON_EXISTING_ID);
        verify(supplierNotFound, times(1)).getException(MESSAGE, NON_EXISTING_ID);
    }
}
