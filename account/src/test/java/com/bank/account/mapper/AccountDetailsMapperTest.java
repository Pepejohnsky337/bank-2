package com.bank.account.mapper;

import com.bank.account.dto.AccountDetailsDto;
import com.bank.account.entity.AccountDetailsEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AccountDetailsMapper Test")
class AccountDetailsMapperTest {

    private AccountDetailsMapper accountDetailsMapper;

    @BeforeEach
    void setUp() {
        accountDetailsMapper = new AccountDetailsMapperImpl();
    }

    @Test
    @DisplayName("toEntity - позитивный: успешное преобразование DTO в Entity,id игнор")
    void toEntity_WithValidDto_ReturnEntityWithIgnoredId() {

        AccountDetailsDto dto = new AccountDetailsDto();
        dto.setId(313L);
        dto.setPassportId(12345L);
        dto.setAccountNumber(49493821293L);
        dto.setBankDetailsId(100L);
        dto.setMoney(BigDecimal.valueOf(1500.50));
        dto.setNegativeBalance(false);
        dto.setProfileId(1L);

        AccountDetailsEntity entity = accountDetailsMapper.toEntity(dto);
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getPassportId()).isEqualTo(12345L);
        assertThat(entity.getAccountNumber()).isEqualTo(49493821293L);
        assertThat(entity.getBankDetailsId()).isEqualTo(100L);
        assertThat(entity.getMoney()).isEqualTo(BigDecimal.valueOf(1500.50));
        assertThat(entity.getNegativeBalance()).isFalse();
        assertThat(entity.getProfileId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("toEntity - негативный: DTO с null полями")
    void toEntity_WithNullFields_ReturnsEntityWithNulls(){

        AccountDetailsDto dto = new AccountDetailsDto();
        dto.setId(999L);

        AccountDetailsEntity entity = accountDetailsMapper.toEntity(dto);
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getPassportId()).isNull();
        assertThat(entity.getAccountNumber()).isNull();
        assertThat(entity.getBankDetailsId()).isNull();
        assertThat(entity.getMoney()).isNull();
        assertThat(entity.getNegativeBalance()).isNull();
        assertThat(entity.getProfileId()).isNull();
    }

    @Test
    @DisplayName("toDto - позитивный: успешное преобразование Entity в DTO")
    void toDto_WithValidEntity_ReturnsCorrectDto() {

        AccountDetailsEntity entity = new AccountDetailsEntity();
        entity.setId(1L);
        entity.setPassportId(12345L);
        entity.setAccountNumber(4081781009999L);
        entity.setBankDetailsId(100L);
        entity.setMoney(BigDecimal.valueOf(2500.50));
        entity.setNegativeBalance(false);
        entity.setProfileId(1L);

        AccountDetailsDto dto = accountDetailsMapper.toDto(entity);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getPassportId()).isEqualTo(12345L);
        assertThat(dto.getAccountNumber()).isEqualTo(4081781009999L);
        assertThat(dto.getBankDetailsId()).isEqualTo(100L);
        assertThat(dto.getMoney()).isEqualTo(BigDecimal.valueOf(2500.50));
        assertThat(dto.getNegativeBalance()).isFalse();
        assertThat(dto.getProfileId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("toDto - негативный: Entity с null полями")
    void toDto_WithNullFields_ReturnWithNulls() {
        AccountDetailsEntity entity = new AccountDetailsEntity();
        entity.setId(1L);

        AccountDetailsDto dto = accountDetailsMapper.toDto(entity);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getPassportId()).isNull();
        assertThat(dto.getAccountNumber()).isNull();
        assertThat(dto.getBankDetailsId()).isNull();
        assertThat(dto.getMoney()).isNull();
        assertThat(dto.getNegativeBalance()).isNull();
        assertThat(dto.getProfileId()).isNull();
    }

    @Test
    @DisplayName("toDtoList - позитивный: успешное преобразование списка Entity в список DTO")
    void toDtoList_WithValid_ReturnsCorrectDtoList(){

        AccountDetailsEntity entity1 = new AccountDetailsEntity();
        entity1.setId(1L);
        entity1.setPassportId(1111L);
        entity1.setAccountNumber(11111111L);
        entity1.setBankDetailsId(10L);
        entity1.setMoney(BigDecimal.valueOf(1000.00));
        entity1.setNegativeBalance(false);
        entity1.setProfileId(1L);

        AccountDetailsEntity entity2 = new AccountDetailsEntity();
        entity2.setId(2L);
        entity2.setPassportId(222L);
        entity2.setAccountNumber(2222222L);
        entity2.setBankDetailsId(20L);
        entity2.setMoney(BigDecimal.valueOf(2000.00));
        entity2.setNegativeBalance(true);
        entity2.setProfileId(2L);

        List<AccountDetailsEntity> entities = List.of(entity1, entity2);
        List<AccountDetailsDto> dtos = accountDetailsMapper.toDtoList(entities);
        assertThat(dtos).isNotNull();
        assertThat(dtos).hasSize(2);

        assertThat(dtos.get(0).getId()).isEqualTo(1L);
        assertThat(dtos.get(0).getPassportId()).isEqualTo(1111L);
        assertThat(dtos.get(0).getAccountNumber()).isEqualTo(11111111L);
        assertThat(dtos.get(0).getBankDetailsId()).isEqualTo(10L);
        assertThat(dtos.get(0).getMoney()).isEqualTo(BigDecimal.valueOf(1000.00));

        assertThat(dtos.get(1).getId()).isEqualTo(2L);
        assertThat(dtos.get(1).getPassportId()).isEqualTo(222L);
        assertThat(dtos.get(1).getAccountNumber()).isEqualTo(2222222L);
        assertThat(dtos.get(1).getMoney()).isEqualTo(BigDecimal.valueOf(2000.00));
        assertThat(dtos.get(1).getNegativeBalance()).isTrue();
    }

    @Test
    @DisplayName("toDtoList - позитивный: пустой список возвращает пустой список")
    void toDtoList_WithEmptyList_ReturnsEmptyList(){

        List<AccountDetailsEntity> entityList = Collections.emptyList();
        List<AccountDetailsDto> result = accountDetailsMapper.toDtoList(entityList);
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("toDtoList - негативный: null входные данные возвращают null")
    void toDtoList_WithNullInput_ReturnsNull() {

        List<AccountDetailsDto> result = accountDetailsMapper.toDtoList(null);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("mergeToEntity - позитивный: успешное слияние DTO в существующий Entity")
    void mergeEntity_WithValidData_UpdatesExistingEntity() {

        AccountDetailsEntity existEntity = new AccountDetailsEntity();
        existEntity.setId(1L);
        existEntity.setPassportId(111L);
        existEntity.setAccountNumber(11111L);
        existEntity.setBankDetailsId(10L);
        existEntity.setMoney(BigDecimal.valueOf(1000.00));
        existEntity.setNegativeBalance(false);
        existEntity.setProfileId(1L);

        AccountDetailsDto updateDto = new AccountDetailsDto();
        updateDto.setId(2L);
        updateDto.setPassportId(222L);
        updateDto.setAccountNumber(22222L);
        updateDto.setBankDetailsId(22L);
        updateDto.setMoney(BigDecimal.valueOf(9999.99));
        updateDto.setNegativeBalance(true);
        updateDto.setProfileId(22L);

        AccountDetailsEntity result =accountDetailsMapper.mergeToEntity(existEntity,updateDto);

        assertThat(result).isSameAs(existEntity);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getPassportId()).isEqualTo(222L);
        assertThat(result.getAccountNumber()).isEqualTo(22222L);
        assertThat(result.getBankDetailsId()).isEqualTo(22L);
        assertThat(result.getMoney()).isEqualTo(BigDecimal.valueOf(9999.99));
        assertThat(result.getNegativeBalance()).isTrue();
        assertThat(result.getProfileId()).isEqualTo(22L);
    }

    @Test
    @DisplayName("mergeToEntity - негативный: source = null не меняет target")
    void mergeToEntity_WithNullSource_ReturnsTargetUnchanged() {

        AccountDetailsEntity existingEntity = new AccountDetailsEntity();
        existingEntity.setId(1L);
        existingEntity.setPassportId(11111L);
        existingEntity.setAccountNumber(111111111L);
        existingEntity.setMoney(BigDecimal.valueOf(1000.00));

        AccountDetailsEntity result = accountDetailsMapper.mergeToEntity(existingEntity, null);

        assertThat(result).isSameAs(existingEntity);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getPassportId()).isEqualTo(11111L);
        assertThat(result.getAccountNumber()).isEqualTo(111111111L);
        assertThat(result.getMoney()).isEqualTo(BigDecimal.valueOf(1000.00));
    }

}
