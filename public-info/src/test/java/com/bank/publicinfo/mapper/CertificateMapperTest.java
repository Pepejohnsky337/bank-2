package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.CertificateDto;
import com.bank.publicinfo.entity.CertificateEntity;

import static com.bank.publicinfo.utilTest.TestDataFactory.createCertificateDto;
import static com.bank.publicinfo.utilTest.TestDataFactory.createCertificateEntity;
import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

class CertificateMapperTest {
    CertificateMapper certificateMapper = Mappers.getMapper(CertificateMapper.class);

    @Test
    @DisplayName("Должен корректно маппить  CertificateDto в CertificateEntity")
    void toEntity_shouldMapCertificateDtoToEntity() {
        CertificateDto certificateDto = createCertificateDto(1L);
        CertificateEntity certificateEntity = certificateMapper.toEntity(certificateDto);
        assertThat(certificateEntity).isNotNull();
        assertThat(certificateEntity.getId()).isNull();
        assertThat(certificateEntity.getBankDetails()).isNull();
        assertThat(certificateEntity.getPhotoCertificate()).isEqualTo(certificateDto.getPhotoCertificate());
    }

    @Test
    @DisplayName("При передаче null в toEntity должен возвращаться null")
    void toEntity_WhenInputIsNull_ShouldReturnNull() {
        CertificateEntity certificateEntity = certificateMapper.toEntity(null);
        assertThat(certificateEntity).isNull();
    }

    @Test
    @DisplayName("Должен корректно маппить CertificateEntity в CertificateDto")
    void toDto_shouldMapCertificateEntityToDto() {
        CertificateEntity certificateEntity = createCertificateEntity(1L);
        CertificateDto certificateDto = certificateMapper.toDto(certificateEntity);
        assertThat(certificateDto).isNotNull();
        assertThat(certificateDto.getId()).isEqualTo(certificateEntity.getId());
        assertThat(certificateDto.getBankDetails()).isNull();
        assertThat(certificateDto.getPhotoCertificate()).isEqualTo(certificateEntity.getPhotoCertificate());
    }

    @Test
    @DisplayName("При передаче null в toDto должен возвращаться null")
    void toDto_WhenInputIsNull_ShouldReturnNull() {
        CertificateDto certificateDto = certificateMapper.toDto(null);
        assertThat(certificateDto).isNull();
    }

    @Test
    @DisplayName("Должен обновить существующую CertificateEntity данными из CertificateDto")
    void mergeToEntity_ShouldUpdateExistingEntity() {
        CertificateEntity branchEntity = createCertificateEntity(1L);
        CertificateDto certificateDto = createCertificateDto(2L);
        CertificateEntity mergeEntity = certificateMapper.mergeToEntity(certificateDto, branchEntity);
        assertThat(mergeEntity).isNotNull();
        assertThat(mergeEntity.getId()).isNotNull();
        assertThat(mergeEntity.getBankDetails()).isNull();
        assertThat(mergeEntity.getPhotoCertificate()).isEqualTo(certificateDto.getPhotoCertificate());
    }

    @Test
    @DisplayName("При передаче null в CertificateDto должен на затрагивать сущность")
    void mergeToEntity_WhenInputIsNull() {
        CertificateEntity branchEntity = createCertificateEntity(1L);
        CertificateDto certificateDto = null;
        CertificateEntity mergeEntity = certificateMapper.mergeToEntity(certificateDto, branchEntity);
        assertThat(mergeEntity).isNotNull();
        assertThat(mergeEntity.getId()).isEqualTo(branchEntity.getId());
        assertThat(mergeEntity.getBankDetails()).isNull();
        assertThat(mergeEntity.getPhotoCertificate()).isEqualTo(branchEntity.getPhotoCertificate());
    }

    @Test
    @DisplayName("Должен корректно маппить список CertificateEntity в список CertificateDto")
    void toDtoList_ShouldMapAllEntities() {
        List<CertificateEntity> certificateEntities = List.of(createCertificateEntity(1L),
                createCertificateEntity(2L), createCertificateEntity(3L));
        List<CertificateDto> dtoList = certificateMapper.toDtoList(certificateEntities);

        assertThat(dtoList).isNotNull();
        assertThat(dtoList.size()).isEqualTo(certificateEntities.size());
        for (int i = 0; i < dtoList.size(); i++) {
            assertThat(dtoList.get(i).getId()).isEqualTo(certificateEntities.get(i).getId());
            assertThat(dtoList.get(i).getBankDetails()).isNull();
            assertThat(dtoList.get(i).getPhotoCertificate()).isEqualTo(certificateEntities.get(i).getPhotoCertificate());
        }
    }

    @Test
    @DisplayName("При передаче пустого списка в toDtoList должен возвращаться пустой список")
    void toDtoList_WhenInputIsEmpty_ShouldReturnEmptyList() {
        List<CertificateEntity> input = List.of();
        List<CertificateDto> result = certificateMapper.toDtoList(input);
        AssertionsForInterfaceTypes.assertThat(result).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("При передаче null в toDtoList должен возвращаться null")
    void toDtoList_WhenInputIsNull_ShouldReturnNull() {
        List<CertificateDto> result = certificateMapper.toDtoList(null);
        AssertionsForInterfaceTypes.assertThat(result).isNull();
    }
}

