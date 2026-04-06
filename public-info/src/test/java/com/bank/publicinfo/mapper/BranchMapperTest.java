package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.BranchDto;
import com.bank.publicinfo.entity.BranchEntity;

import static com.bank.publicinfo.utilTest.TestDataFactory.createBranchDto;
import static com.bank.publicinfo.utilTest.TestDataFactory.createBranchDtoNotAllFields;
import static com.bank.publicinfo.utilTest.TestDataFactory.createBranchEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

class BranchMapperTest {

    BranchMapper branchMapper = Mappers.getMapper(BranchMapper.class);

    @Test
    @DisplayName("Должен корректно маппить  BranchDto в BranchEntity")
    void toEntity_shouldMapBranchDtoToEntity() {
        BranchDto branchDto = createBranchDto(1L, "Москва");
        BranchEntity branchEntity = branchMapper.toEntity(branchDto);
        assertThat(branchEntity).isNotNull();
        assertThat(branchEntity.getId()).isNull();
        assertThat(branchEntity.getAddress()).isEqualTo(branchDto.getAddress());
        assertThat(branchEntity.getPhoneNumber()).isEqualTo(branchDto.getPhoneNumber());
        assertThat(branchEntity.getCity()).isEqualTo(branchDto.getCity());
        assertThat(branchEntity.getStartOfWork()).isEqualTo(branchDto.getStartOfWork());
        assertThat(branchEntity.getEndOfWork()).isEqualTo(branchDto.getEndOfWork());
    }

    @Test
    @DisplayName("Должен корректно маппить BranchDto в BranchEntity с частично заполненными полями")
    void toEntity_shouldMapOnlyProvidedFields_whenDtoHasPartialData() {
        BranchDto branchDto = createBranchDtoNotAllFields(1L, "Москва");
        BranchEntity resultEntity = branchMapper.toEntity(branchDto);
        assertThat(resultEntity).isNotNull();
        assertThat(resultEntity.getId()).isNull();
        assertThat(resultEntity.getAddress()).isEqualTo(branchDto.getAddress());
        assertThat(resultEntity.getPhoneNumber()).isEqualTo(branchDto.getPhoneNumber());
        assertThat(resultEntity.getCity()).isEqualTo(branchDto.getCity());
        assertThat(resultEntity.getStartOfWork()).isNull();
        assertThat(resultEntity.getEndOfWork()).isNull();
    }

    @Test
    @DisplayName("При передаче null в toEntity должен возвращаться null")
    void toEntity_WhenInputIsNull_ShouldReturnNull() {
        BranchEntity branchEntity = branchMapper.toEntity(null);
        assertThat(branchEntity).isNull();
    }

    @Test
    @DisplayName("Должен корректно маппить BranchEntity в BranchDto")
    void toDto_shouldMapBranchEntityToDto() {
        BranchEntity branchEntity = createBranchEntity(1L, "Москва");
        BranchDto branchDto = branchMapper.toDto(branchEntity);
        assertThat(branchDto).isNotNull();
        assertThat(branchDto.getId()).isEqualTo(branchEntity.getId());
        assertThat(branchDto.getAddress()).isEqualTo(branchEntity.getAddress());
        assertThat(branchDto.getPhoneNumber()).isEqualTo(branchEntity.getPhoneNumber());
        assertThat(branchDto.getCity()).isEqualTo(branchEntity.getCity());
        assertThat(branchDto.getStartOfWork()).isEqualTo(branchEntity.getStartOfWork());
        assertThat(branchDto.getEndOfWork()).isEqualTo(branchEntity.getEndOfWork());
    }

    @Test
    @DisplayName("При передаче null в toDto должен возвращаться null")
    void toDto_WhenInputIsNull_ShouldReturnNull() {
        BranchDto branchDto = branchMapper.toDto(null);
        assertThat(branchDto).isNull();
    }

    @Test
    @DisplayName("Должен обновить существующую BranchEntity данными из BranchDto")
    void mergeToEntity_ShouldUpdateExistingEntity() {
        BranchEntity branchEntity = createBranchEntity(1L, "Москва");
        BranchDto branchDto = createBranchDto(2L, "Санкт-Петербург");
        BranchEntity mergeEntity = branchMapper.mergeToEntity(branchDto, branchEntity);
        assertThat(mergeEntity).isNotNull();
        assertThat(mergeEntity.getId()).isEqualTo(branchEntity.getId());
        assertThat(mergeEntity.getAddress()).isEqualTo(branchDto.getAddress());
        assertThat(mergeEntity.getPhoneNumber()).isEqualTo(branchDto.getPhoneNumber());
        assertThat(mergeEntity.getCity()).isEqualTo(branchDto.getCity());
        assertThat(mergeEntity.getStartOfWork()).isEqualTo(branchDto.getStartOfWork());
        assertThat(mergeEntity.getEndOfWork()).isEqualTo(branchDto.getEndOfWork());
    }

    @Test
    @DisplayName("Должен обновить существующую BranchEntity данными из BranchDto если некоторые поля null")
    void mergeToEntity_shouldUpdateFromDtoAndNullifyMissingFields() {
        BranchEntity branchEntity = createBranchEntity(1L, "Москва");
        BranchDto branchDto = createBranchDtoNotAllFields(2L, "Санкт-Петербург");
        BranchEntity mergeEntity = branchMapper.mergeToEntity(branchDto, branchEntity);
        assertThat(mergeEntity).isNotNull();
        assertThat(mergeEntity.getId()).isEqualTo(branchEntity.getId());
        assertThat(mergeEntity.getAddress()).isEqualTo(branchDto.getAddress());
        assertThat(mergeEntity.getPhoneNumber()).isEqualTo(branchDto.getPhoneNumber());
        assertThat(mergeEntity.getCity()).isEqualTo(branchDto.getCity());
        assertThat(mergeEntity.getStartOfWork()).isNull();
        assertThat(mergeEntity.getEndOfWork()).isNull();
    }

    @Test
    @DisplayName("Должен не затрагивать существующую BranchEntity если BranchDto равен null")
    void mergeToEntity_WhenDtoIsNull_ShouldNotChangeEntity() {
        BranchEntity branchEntity = createBranchEntity(1L, "Москва");
        BranchDto branchDto = null;
        BranchEntity mergeEntity = branchMapper.mergeToEntity(branchDto, branchEntity);
        assertThat(mergeEntity).isNotNull();
        assertThat(mergeEntity.getId()).isEqualTo(branchEntity.getId());
        assertThat(mergeEntity.getAddress()).isEqualTo(branchEntity.getAddress());
        assertThat(mergeEntity.getPhoneNumber()).isEqualTo(branchEntity.getPhoneNumber());
        assertThat(mergeEntity.getCity()).isEqualTo(branchEntity.getCity());
        assertThat(mergeEntity.getStartOfWork()).isEqualTo(branchEntity.getStartOfWork());
        assertThat(mergeEntity.getEndOfWork()).isEqualTo(branchEntity.getEndOfWork());
    }

    @Test
    @DisplayName("При передаче null в целевую сущность должно выбросить NullPointerException")
    void mergeToEntity_WhenInputIsNull() {
        BranchDto branchDto = createBranchDto(1L, "Санкт-Петербург");
        assertThatThrownBy(() -> branchMapper.mergeToEntity(branchDto, null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Должен корректно маппить список BranchEntity в список BranchDto")
    void toDtoList_ShouldMapAllEntities() {
        List<BranchEntity> branchEntities = List.of(createBranchEntity(1L, "Москва"),
                createBranchEntity(2L, "Москва"), createBranchEntity(3L, "Москва"));
        List<BranchDto> dtoList = branchMapper.toDtoList(branchEntities);

        assertThat(dtoList).isNotNull();
        assertThat(dtoList.size()).isEqualTo(branchEntities.size());
        for (int i = 0; i < dtoList.size(); i++) {
            assertThat(dtoList.get(i).getId()).isEqualTo(branchEntities.get(i).getId());
            assertThat(dtoList.get(i).getAddress()).isEqualTo(branchEntities.get(i).getAddress());
            assertThat(dtoList.get(i).getPhoneNumber()).isEqualTo(branchEntities.get(i).getPhoneNumber());
            assertThat(dtoList.get(i).getCity()).isEqualTo(branchEntities.get(i).getCity());
            assertThat(dtoList.get(i).getStartOfWork()).isEqualTo(branchEntities.get(i).getStartOfWork());
            assertThat(dtoList.get(i).getEndOfWork()).isEqualTo(branchEntities.get(i).getEndOfWork());
        }
    }

    @Test
    @DisplayName("При передаче пустого списка в toDtoList должен возвращаться пустой список")
    void toDtoList_WhenInputIsEmpty_ShouldReturnEmptyList() {
        List<BranchEntity> input = List.of();
        List<BranchDto> result = branchMapper.toDtoList(input);
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("При передаче null в toDtoList должен возвращаться null")
    void toDtoList_WhenInputIsNull_ShouldReturnNull() {
        List<BranchDto> result = branchMapper.toDtoList(null);
        assertThat(result).isNull();
    }
}