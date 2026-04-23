package com.bank.transfer.mapper;

import com.bank.transfer.dto.AccountTransferDto;
import com.bank.transfer.entity.AccountTransferEntity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTransferMapperTest {

    private final AccountTransferMapper mapper = Mappers.getMapper(AccountTransferMapper.class);

    private AccountTransferDto createAccountTransferDto(Long id, Long accountNumber, BigDecimal amount,
                                                        String purpose, Long accountDetailsId) {
        return new AccountTransferDto(id, accountNumber, amount, purpose, accountDetailsId);
    }

    private AccountTransferEntity createAccountTransferEntity(Long id, Long accountNumber, BigDecimal amount,
                                                              String purpose, Long accountDetailsId) {
        return new AccountTransferEntity(id, accountNumber, amount, purpose, accountDetailsId);
    }

    @Test
    @DisplayName("Должен маппить DTO в Entity, игнорируя id")
    void toEntity_ShouldMapDtoToEntity() {
        AccountTransferDto dto = createAccountTransferDto(null, 123456789L,
                new BigDecimal("100.50"), "Test transfer", 888L);
        AccountTransferEntity entity = mapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getAccountNumber()).isEqualTo(123456789L);
        assertThat(entity.getAmount()).isEqualByComparingTo(new BigDecimal("100.50"));
        assertThat(entity.getPurpose()).isEqualTo("Test transfer");
        assertThat(entity.getAccountDetailsId()).isEqualTo(888L);
    }

    @Test
    @DisplayName("Должен вернуть null при null DTO")
    void toEntity_WhenDtoNull_ShouldReturnNull() {
        assertThat(mapper.toEntity(null)).isNull();
    }

    @Test
    @DisplayName("Должен маппить Entity в DTO")
    void toDto_ShouldMapEntityToDto() {
        AccountTransferEntity entity = createAccountTransferEntity(1L, 123456789L,
                new BigDecimal("100.50"), "Test transfer", 888L);
        AccountTransferDto dto = mapper.toDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getAccountNumber()).isEqualTo(123456789L);
        assertThat(dto.getAmount()).isEqualByComparingTo(new BigDecimal("100.50"));
        assertThat(dto.getPurpose()).isEqualTo("Test transfer");
        assertThat(dto.getAccountDetailsId()).isEqualTo(888L);
    }

    @Test
    @DisplayName("Должен вернуть null при null Entity")
    void toDto_WhenEntityNull_ShouldReturnNull() {
        assertThat(mapper.toDto(null)).isNull();
    }

    @Test
    @DisplayName("Должен обновить существующую Entity, не меняя id")
    void mergeToEntity_ShouldUpdateExistingEntity() {
        AccountTransferDto dto = createAccountTransferDto(
                null, 999L, BigDecimal.valueOf(200), "Updated", 777L);
        AccountTransferEntity existing = createAccountTransferEntity(1L, 123456789L,
                new BigDecimal("100.50"), "Test transfer", 888L);
        AccountTransferEntity merged = mapper.mergeToEntity(dto, existing);

        assertThat(merged.getId()).isEqualTo(1L);
        assertThat(merged.getAccountNumber()).isEqualTo(999L);
        assertThat(merged.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(200));
        assertThat(merged.getPurpose()).isEqualTo("Updated");
        assertThat(merged.getAccountDetailsId()).isEqualTo(777L);
    }

    @Test
    @DisplayName("При null DTO возвращает исходную Entity без изменений")
    void mergeToEntity_WhenDtoNull_ShouldReturnOriginalEntity() {
        AccountTransferEntity existing = createAccountTransferEntity(1L, 123456789L,
                new BigDecimal("100.50"), "Test transfer", 888L);
        AccountTransferEntity result = mapper.mergeToEntity(null, existing);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getAccountNumber()).isEqualTo(123456789L);
        assertThat(result.getAmount()).isEqualByComparingTo(new BigDecimal("100.50"));
        assertThat(result.getPurpose()).isEqualTo("Test transfer");
        assertThat(result.getAccountDetailsId()).isEqualTo(888L);
    }

    @Test
    @DisplayName("Должен маппить список Entity в список DTO")
    void toDtoList_ShouldMapList() {
        List<AccountTransferEntity> entities = List.of(
                createAccountTransferEntity(1L, 111L, BigDecimal.ONE, "a", 1L),
                createAccountTransferEntity(2L, 222L, BigDecimal.TEN, "b", 2L)
        );
        List<AccountTransferDto> dtos = mapper.toDtoList(entities);
        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).getId()).isEqualTo(1L);
        assertThat(dtos.get(1).getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("При пустом списке возвращает пустой список")
    void toDtoList_WhenEmptyList_ShouldReturnEmptyList() {
        assertThat(mapper.toDtoList(List.of())).isEmpty();
    }
}
