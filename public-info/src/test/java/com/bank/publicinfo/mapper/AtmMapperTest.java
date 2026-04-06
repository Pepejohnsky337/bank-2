package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.AtmDto;
import com.bank.publicinfo.entity.AtmEntity;

import static com.bank.publicinfo.utilTest.TestDataFactory.createAtmDto;
import static com.bank.publicinfo.utilTest.TestDataFactory.createAtmEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

class AtmMapperTest {

    private final AtmMapper atmMapper = Mappers.getMapper(AtmMapper.class);

    @Test
    @DisplayName("Должен корректно маппить AtmDto в AtmEntity")
    void toEntity_ShouldMapAllFields() {
        AtmDto atmDto = createAtmDto(1L, "address 1");
        AtmEntity result = atmMapper.toEntity(atmDto);
        assertNotNull(result);
        assertThat(result.getId()).isNull();
        assertThat(result.getAddress()).isEqualTo(atmDto.getAddress());
        assertThat(result.getStartOfWork()).isEqualTo(atmDto.getStartOfWork());
        assertThat(result.getEndOfWork()).isEqualTo(atmDto.getEndOfWork());
        assertThat(result.getAllHours()).isEqualTo(atmDto.getAllHours());
    }

    @Test
    @DisplayName("При передаче null в toEntity должен возвращаться null")
    void toEntity_WhenInputIsNull_ShouldReturnNull() {
        AtmEntity result = atmMapper.toEntity(null);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Должен корректно маппить AtmEntity в AtmDto")
    void toDto_ShouldMapAllFields() {
        AtmEntity atmEntity = createAtmEntity(1L, "address 1");
        AtmDto result = atmMapper.toDto(atmEntity);
        assertThat(result.getId()).isEqualTo(atmEntity.getId());
        assertThat(result.getAddress()).isEqualTo(atmEntity.getAddress());
        assertThat(result.getStartOfWork()).isEqualTo(atmEntity.getStartOfWork());
        assertThat(result.getEndOfWork()).isEqualTo(atmEntity.getEndOfWork());
        assertThat(result.getAllHours()).isEqualTo(atmEntity.getAllHours());
    }

    @Test
    @DisplayName("При передаче null в toDto должен возвращаться null")
    void toDto_WhenInputIsNull_ShouldReturnNull() {
        AtmDto result = atmMapper.toDto(null);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Должен обновить существующую AtmEntity данными из AtmDto")
    void mergeToEntity_ShouldUpdateExistingEntity() {
        AtmDto atmDto = createAtmDto(2L, "address 2");
        AtmEntity existingEntity = createAtmEntity(1L, "address 1");
        AtmEntity result = atmMapper.mergeToEntity(atmDto, existingEntity);
        assertThat(result.getId()).isEqualTo(existingEntity.getId());
        assertThat(result.getAddress()).isEqualTo(atmDto.getAddress());
        assertThat(result.getStartOfWork()).isEqualTo(atmDto.getStartOfWork());
        assertThat(result.getEndOfWork()).isEqualTo(atmDto.getEndOfWork());
        assertThat(result.getAllHours()).isEqualTo(atmDto.getAllHours());
    }

    @Test
    @DisplayName("При передаче null в целевую сущность должно выбросить NullPointerException")
    void mergeToEntity_WhenInputIsNull() {
        AtmDto atmDto = createAtmDto(1L, "address 1");
        assertThatThrownBy(() -> atmMapper.mergeToEntity(atmDto, null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Должен корректно маппить список AtmEntity в список AtmDto")
    void toDtoList_ShouldMapAllEntities() {
        List<AtmEntity> entities = List.of(
                createAtmEntity(1L, "address 1"),
                createAtmEntity(2L, "address 2"),
                createAtmEntity(3L, "address 3")
        );

        List<AtmDto> dtos = atmMapper.toDtoList(entities);

        assertThat(dtos).hasSameSizeAs(entities);

        for (int i = 0; i < dtos.size(); i++) {
            assertThat(dtos.get(i)).isNotNull();
            assertThat(dtos.get(i).getId()).isEqualTo(entities.get(i).getId());
            assertThat(dtos.get(i).getAddress()).isEqualTo(entities.get(i).getAddress());
            assertThat(dtos.get(i).getStartOfWork()).isEqualTo(entities.get(i).getStartOfWork());
            assertThat(dtos.get(i).getEndOfWork()).isEqualTo(entities.get(i).getEndOfWork());
            assertThat(dtos.get(i).getAllHours()).isEqualTo(entities.get(i).getAllHours());
        }
    }

    @Test
    @DisplayName("При передаче пустого списка в toDtoList должен возвращаться пустой список")
    void toDtoList_WhenInputIsEmpty_ShouldReturnEmptyList() {
        List<AtmEntity> input = List.of();
        List<AtmDto> result = atmMapper.toDtoList(input);
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("При передаче null в toDtoList должен возвращаться null")
    void toDtoList_WhenInputIsNull_ShouldReturnNull() {
        List<AtmDto> result = atmMapper.toDtoList(null);
        assertThat(result).isNull();
    }
}