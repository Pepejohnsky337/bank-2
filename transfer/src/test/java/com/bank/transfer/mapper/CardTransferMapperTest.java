package com.bank.transfer.mapper;

import com.bank.transfer.dto.CardTransferDto;
import com.bank.transfer.entity.CardTransferEntity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CardTransferMapperTest {

    private final CardTransferMapper mapper = Mappers.getMapper(CardTransferMapper.class);

    private CardTransferDto createCardTransferDto(Long id, Long cardNumber, BigDecimal amount,
                                                  String purpose, Long accountDetailsId) {
        return new CardTransferDto(id, cardNumber, amount, purpose, accountDetailsId);
    }

    private CardTransferEntity createCardTransferEntity(Long id, Long cardNumber, BigDecimal amount,
                                                        String purpose, Long accountDetailsId) {
        return new CardTransferEntity(id, cardNumber, amount, purpose, accountDetailsId);
    }

    @Test
    @DisplayName("Должен маппить DTO в Entity, игнорируя id")
    void toEntity_ShouldMapDtoToEntity() {
        CardTransferDto dto = createCardTransferDto(null, 1234567890123456L,
                new BigDecimal("500.00"), "Card transfer", 777L);
        CardTransferEntity entity = mapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getCardNumber()).isEqualTo(1234567890123456L);
        assertThat(entity.getAmount()).isEqualByComparingTo(new BigDecimal("500.00"));
        assertThat(entity.getPurpose()).isEqualTo("Card transfer");
        assertThat(entity.getAccountDetailsId()).isEqualTo(777L);
    }

    @Test
    @DisplayName("Должен вернуть null при null DTO")
    void toEntity_WhenDtoNull_ShouldReturnNull() {
        assertThat(mapper.toEntity(null)).isNull();
    }

    @Test
    @DisplayName("Должен маппить Entity в DTO")
    void toDto_ShouldMapEntityToDto() {
        CardTransferEntity entity = createCardTransferEntity(1L, 1234567890123456L,
                new BigDecimal("500.00"), "Card transfer", 777L);
        CardTransferDto dto = mapper.toDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getCardNumber()).isEqualTo(1234567890123456L);
        assertThat(dto.getAmount()).isEqualByComparingTo(new BigDecimal("500.00"));
        assertThat(dto.getPurpose()).isEqualTo("Card transfer");
        assertThat(dto.getAccountDetailsId()).isEqualTo(777L);
    }

    @Test
    @DisplayName("Должен вернуть null при null Entity")
    void toDto_WhenEntityNull_ShouldReturnNull() {
        assertThat(mapper.toDto(null)).isNull();
    }

    @Test
    @DisplayName("Должен обновить существующую Entity, не меняя id")
    void mergeToEntity_ShouldUpdateExistingEntity() {
        CardTransferDto dto = createCardTransferDto(
                null, 999L, BigDecimal.valueOf(300), "Updated card", 888L);
        CardTransferEntity existing = createCardTransferEntity(1L, 1234567890123456L,
                new BigDecimal("500.00"), "Card transfer", 777L);
        CardTransferEntity merged = mapper.mergeToEntity(dto, existing);

        assertThat(merged.getId()).isEqualTo(1L);
        assertThat(merged.getCardNumber()).isEqualTo(999L);
        assertThat(merged.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(300));
        assertThat(merged.getPurpose()).isEqualTo("Updated card");
        assertThat(merged.getAccountDetailsId()).isEqualTo(888L);
    }

    @Test
    @DisplayName("При null DTO возвращает исходную Entity без изменений")
    void mergeToEntity_WhenDtoNull_ShouldReturnOriginalEntity() {
        CardTransferEntity existing = createCardTransferEntity(1L, 1234567890123456L,
                new BigDecimal("500.00"), "Card transfer", 777L);
        CardTransferEntity result = mapper.mergeToEntity(null, existing);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getCardNumber()).isEqualTo(1234567890123456L);
        assertThat(result.getAmount()).isEqualByComparingTo(new BigDecimal("500.00"));
        assertThat(result.getPurpose()).isEqualTo("Card transfer");
        assertThat(result.getAccountDetailsId()).isEqualTo(777L);
    }

    @Test
    @DisplayName("Должен маппить список Entity в список DTO")
    void toDtoList_ShouldMapList() {
        List<CardTransferEntity> entities = List.of(
                createCardTransferEntity(1L, 111L, BigDecimal.ONE, "a", 1L),
                createCardTransferEntity(2L, 222L, BigDecimal.TEN, "b", 2L)
        );
        List<CardTransferDto> dtos = mapper.toDtoList(entities);
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
