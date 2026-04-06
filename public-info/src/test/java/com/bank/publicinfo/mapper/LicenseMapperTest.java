package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.LicenseDto;
import com.bank.publicinfo.entity.LicenseEntity;

import static com.bank.publicinfo.utilTest.TestDataFactory.createLicenseDto;
import static com.bank.publicinfo.utilTest.TestDataFactory.createLicenseEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

class LicenseMapperTest {

    LicenseMapper licenseMapper = Mappers.getMapper(LicenseMapper.class);

    @Test
    @DisplayName("Должен корректно маппить  LicenseDto в LicenseEntity")
    void toEntity_shouldMapLicenseDtoToEntity() {
        LicenseDto licenseDto = createLicenseDto(1L);
        LicenseEntity licenseEntity = licenseMapper.toEntity(licenseDto);
        assertThat(licenseEntity).isNotNull();
        assertThat(licenseEntity.getId()).isNull();
        assertThat(licenseEntity.getPhotoLicense()).isEqualTo(licenseDto.getPhotoLicense());
    }

    @Test
    @DisplayName("При передаче null в toEntity должен возвращаться null")
    void toEntity_WhenInputIsNull_ShouldReturnNull() {
        LicenseEntity licenseEntity = licenseMapper.toEntity(null);
        assertThat(licenseEntity).isNull();
    }

    @Test
    @DisplayName("Должен корректно маппить LicenseEntity в LicenseDto")
    void toDto_shouldMapLicenseEntityToDto() {
        LicenseEntity licenseEntity = createLicenseEntity(1L);
        LicenseDto licenseDto = licenseMapper.toDto(licenseEntity);
        assertThat(licenseDto).isNotNull();
        assertThat(licenseDto.getId()).isEqualTo(licenseEntity.getId());
        assertThat(licenseDto.getPhotoLicense()).isEqualTo(licenseEntity.getPhotoLicense());
    }

    @Test
    @DisplayName("При передаче null в toDto должен возвращаться null")
    void toDto_WhenInputIsNull_ShouldReturnNull() {
        LicenseDto licenseDto = licenseMapper.toDto(null);
        assertThat(licenseDto).isNull();
    }

    @Test
    @DisplayName("Должен обновить существующую LicenseEntity данными из LicenseDto")
    void mergeToEntity_ShouldUpdateExistingEntity() {
        LicenseEntity licenseEntity = createLicenseEntity(1L);
        LicenseDto licenseDto = createLicenseDto(2L);
        LicenseEntity mergeEntity = licenseMapper.mergeToEntity(licenseDto, licenseEntity);
        assertThat(mergeEntity).isNotNull();
        assertThat(mergeEntity.getId()).isNotNull();
        assertThat(mergeEntity.getId()).isEqualTo(licenseEntity.getId());
        assertThat(mergeEntity.getPhotoLicense()).isEqualTo(licenseDto.getPhotoLicense());
    }

    @Test
    @DisplayName("При передаче null в целевую сущность должно выбросить NullPointerException")
    void mergeToEntity_WhenInputIsNull() {
        LicenseDto licenseDto = createLicenseDto(1L);
        assertThatThrownBy(() -> licenseMapper.mergeToEntity(licenseDto, null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Должен корректно маппить список LicenseEntity в список LicenseDto")
    void toDtoList_ShouldMapAllEntities() {
        List<LicenseEntity> licenseEntities = List.of(createLicenseEntity(1L),
                createLicenseEntity(2L), createLicenseEntity(3L));
        List<LicenseDto> dtoList = licenseMapper.toDtoList(licenseEntities);

        assertThat(dtoList).isNotNull();
        assertThat(dtoList.size()).isEqualTo(licenseEntities.size());
        for (int i = 0; i < dtoList.size(); i++) {
            assertThat(dtoList.get(i).getId()).isEqualTo(licenseEntities.get(i).getId());
            assertThat(dtoList.get(i).getPhotoLicense()).isEqualTo(licenseEntities.get(i).getPhotoLicense());
        }
    }

    @Test
    @DisplayName("При передаче пустого списка в toDtoList должен возвращаться пустой список")
    void toDtoList_WhenInputIsEmpty_ShouldReturnEmptyList() {
        List<LicenseEntity> input = List.of();
        List<LicenseDto> result = licenseMapper.toDtoList(input);
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("При передаче null в toDtoList должен возвращаться null")
    void toDtoList_WhenInputIsNull_ShouldReturnNull() {
        List<LicenseDto> result = licenseMapper.toDtoList(null);
        assertThat(result).isNull();
    }
}