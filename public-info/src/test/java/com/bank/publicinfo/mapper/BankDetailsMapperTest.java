package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.entity.BankDetailsEntity;

import static com.bank.publicinfo.utilTest.TestDataFactory.createBankDetailsDto;
import static com.bank.publicinfo.utilTest.TestDataFactory.createBankDetailsEntity;
import static com.bank.publicinfo.utilTest.TestDataFactory.createBankDetailsDtoNotAllFields;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

class BankDetailsMapperTest {

    BankDetailsMapper bankDetailsMapper = Mappers.getMapper(BankDetailsMapper.class);

    @Test
    @DisplayName("Должен корректно маппить  BankDetailsDto в BankDetailsEntity")
    void toEntity_shouldMapBankDetailsDtoToEntity() {
        BankDetailsDto bankDetailsDto = createBankDetailsDto(1L, "Сбербанк");
        BankDetailsEntity bankDetailsEntity = bankDetailsMapper.toEntity(bankDetailsDto);
        assertThat(bankDetailsEntity).isNotNull();
        assertThat(bankDetailsEntity.getId()).isNull();
        assertThat(bankDetailsEntity.getBik()).isEqualTo(bankDetailsDto.getBik());
        assertThat(bankDetailsEntity.getInn()).isEqualTo(bankDetailsDto.getInn());
        assertThat(bankDetailsEntity.getName()).isEqualTo(bankDetailsDto.getName());
        assertThat(bankDetailsEntity.getCorAccount()).isEqualTo(bankDetailsDto.getCorAccount());
        assertThat(bankDetailsEntity.getCity()).isEqualTo(bankDetailsDto.getCity());
        assertThat(bankDetailsEntity.getJointStockCompany()).isEqualTo(bankDetailsDto.getJointStockCompany());
    }

    @Test
    @DisplayName("Должен корректно маппить только заполненные поля BankDetailsDto в BankDetailsEntity")
    void ttoEntity_shouldMapOnlyProvidedFields_andIgnoreIdAndBankFields_whenDtoHasPartialData() {
        BankDetailsDto bankDetailsDto = createBankDetailsDtoNotAllFields(1L, "Сбербанк");
        BankDetailsEntity bankDetailsEntity = bankDetailsMapper.toEntity(bankDetailsDto);
        assertThat(bankDetailsEntity).isNotNull();
        assertThat(bankDetailsEntity.getId()).isNull();
        assertThat(bankDetailsEntity.getBik()).isNull();
        assertThat(bankDetailsEntity.getInn()).isNull();
        assertThat(bankDetailsEntity.getKpp()).isNull();
        assertThat(bankDetailsEntity.getCorAccount()).isNull();
        assertThat(bankDetailsEntity.getName()).isEqualTo(bankDetailsDto.getName());
        assertThat(bankDetailsEntity.getCity()).isEqualTo(bankDetailsDto.getCity());
        assertThat(bankDetailsEntity.getJointStockCompany()).isEqualTo(bankDetailsDto.getJointStockCompany());
    }

    @Test
    @DisplayName("При передаче null в toEntity должен возвращаться null")
    void toEntity_WhenInputIsNull_ShouldReturnNull() {
        BankDetailsEntity bankDetailsEntity = bankDetailsMapper.toEntity(null);
        assertThat(bankDetailsEntity).isNull();
    }

    @Test
    @DisplayName("Должен корректно маппить BankDetailsEntity в BankDetailsDto")
    void toDto_shouldMapBankDetailsEntityToDto() {
        BankDetailsEntity bankDetailsEntity = createBankDetailsEntity(1L, "Сбербанк");
        BankDetailsDto dto = bankDetailsMapper.toDto(bankDetailsEntity);
        assertNotNull(dto);
        assertThat(dto.getId()).isEqualTo(bankDetailsEntity.getId());
        assertThat(dto.getBik()).isEqualTo(bankDetailsEntity.getBik());
        assertThat(dto.getInn()).isEqualTo(bankDetailsEntity.getInn());
        assertThat(dto.getName()).isEqualTo(bankDetailsEntity.getName());
        assertThat(dto.getCorAccount()).isEqualTo(bankDetailsEntity.getCorAccount());
        assertThat(dto.getCity()).isEqualTo(bankDetailsEntity.getCity());
        assertThat(dto.getJointStockCompany()).isEqualTo(bankDetailsEntity.getJointStockCompany());
    }

    @Test
    @DisplayName("При передаче null в toDto должен возвращаться null")
    void toDto_WhenInputIsNull_ShouldReturnNull() {
        BankDetailsDto bankDetailsDto = bankDetailsMapper.toDto(null);
        assertThat(bankDetailsDto).isNull();
    }

    @Test
    @DisplayName("Должен обновить существующую BankDetailsEntity данными из BankDetailsDto")
    void mergeToEntity_ShouldUpdateExistingEntity() {
        BankDetailsEntity bankDetailsEntity = createBankDetailsEntity(1L, "Сбербанк");
        BankDetailsDto bankDetailsDto = createBankDetailsDto(2L, "Россельхозбанк");
        BankDetailsEntity mergeEntity = bankDetailsMapper.mergeToEntity(bankDetailsDto, bankDetailsEntity);
        assertThat(mergeEntity).isNotNull();
        assertThat(mergeEntity.getId()).isEqualTo(bankDetailsEntity.getId());
        assertThat(mergeEntity.getBik()).isEqualTo(bankDetailsEntity.getBik());
        assertThat(mergeEntity.getInn()).isEqualTo(bankDetailsEntity.getInn());
        assertThat(mergeEntity.getKpp()).isEqualTo(bankDetailsEntity.getKpp());
        assertThat(mergeEntity.getCorAccount()).isEqualTo(bankDetailsEntity.getCorAccount());
        assertThat(mergeEntity.getCity()).isEqualTo(bankDetailsEntity.getCity());
        assertThat(mergeEntity.getJointStockCompany()).isEqualTo(bankDetailsEntity.getJointStockCompany());
        assertThat(mergeEntity.getName()).isEqualTo(bankDetailsEntity.getName());
    }

    @Test
    @DisplayName("Должен не затрагивать существующую BranchEntity если BranchDto равен null")
    void mergeToEntity_WhenDtoIsNull_ShouldNotChangeEntity() {
        BankDetailsEntity bankDetailsEntity = createBankDetailsEntity(1L, "Сбербанк");
        BankDetailsDto bankDetailsDto = null;
        BankDetailsEntity mergeEntity = bankDetailsMapper.mergeToEntity(bankDetailsDto, bankDetailsEntity);
        assertThat(mergeEntity).isNotNull();
        assertThat(mergeEntity.getId()).isEqualTo(bankDetailsEntity.getId());
        assertThat(mergeEntity.getBik()).isEqualTo(bankDetailsEntity.getBik());
        assertThat(mergeEntity.getInn()).isEqualTo(bankDetailsEntity.getInn());
        assertThat(mergeEntity.getKpp()).isEqualTo(bankDetailsEntity.getKpp());
        assertThat(mergeEntity.getCorAccount()).isEqualTo(bankDetailsEntity.getCorAccount());
        assertThat(mergeEntity.getCity()).isEqualTo(bankDetailsEntity.getCity());
        assertThat(mergeEntity.getJointStockCompany()).isEqualTo(bankDetailsEntity.getJointStockCompany());
        assertThat(mergeEntity.getName()).isEqualTo(bankDetailsEntity.getName());
    }

    @Test
    @DisplayName("При передаче null в целевую сущность должно выбросить NullPointerException")
    void mergeToEntity_WhenInputIsNull() {
        BankDetailsDto bankDetailsDto = createBankDetailsDto(1L, "Россельхозбанк");
        assertThatThrownBy(() -> bankDetailsMapper.mergeToEntity(bankDetailsDto, null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Должен корректно маппить список BankDetailsEntity в список BankDetailsDto")
    void toDtoList_ShouldMapAllEntities() {
        List<BankDetailsEntity> bankDetailsEntities = List.of(createBankDetailsEntity(1L, "Сбербанк"),
                createBankDetailsEntity(2L, "Т-банк"), createBankDetailsEntity(3L, "ВТБ"));
        List<BankDetailsDto> dtoList = bankDetailsMapper.toDtoList(bankDetailsEntities);

        assertThat(dtoList).isNotNull();
        assertThat(dtoList.size()).isEqualTo(bankDetailsEntities.size());
        for (int i = 0; i < dtoList.size(); i++) {
            assertThat(dtoList.get(i).getId()).isEqualTo(bankDetailsEntities.get(i).getId());
            assertThat(dtoList.get(i).getBik()).isEqualTo(bankDetailsEntities.get(i).getBik());
            assertThat(dtoList.get(i).getInn()).isEqualTo(bankDetailsEntities.get(i).getInn());
            assertThat(dtoList.get(i).getKpp()).isEqualTo(bankDetailsEntities.get(i).getKpp());
            assertThat(dtoList.get(i).getCorAccount()).isEqualTo(bankDetailsEntities.get(i).getCorAccount());
            assertThat(dtoList.get(i).getCity()).isEqualTo(bankDetailsEntities.get(i).getCity());
            assertThat(dtoList.get(i).getJointStockCompany()).isEqualTo(bankDetailsEntities.get(i).getJointStockCompany());
            assertThat(dtoList.get(i).getName()).isEqualTo(bankDetailsEntities.get(i).getName());

        }
    }

    @Test
    @DisplayName("При передаче пустого списка в toDtoList должен возвращаться пустой список")
    void toDtoList_WhenInputIsEmpty_ShouldReturnEmptyList() {
        List<BankDetailsEntity> input = List.of();
        List<BankDetailsDto> result = bankDetailsMapper.toDtoList(input);
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("При передаче null в toDtoList должен возвращаться null")
    void toDtoList_WhenInputIsNull_ShouldReturnNull() {
        List<BankDetailsDto> result = bankDetailsMapper.toDtoList(null);
        assertThat(result).isNull();
    }
}