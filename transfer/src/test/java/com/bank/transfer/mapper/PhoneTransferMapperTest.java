package com.bank.transfer.mapper;

import com.bank.transfer.dto.PhoneTransferDto;
import com.bank.transfer.entity.PhoneTransferEntity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PhoneTransferMapperTest {

    private final PhoneTransferMapper mapper = Mappers.getMapper(PhoneTransferMapper.class);

    private PhoneTransferDto createPhoneTransferDto(Long id, Long phoneNumber, BigDecimal amount,
                                                    String purpose, Long accountDetailsId) {
        return new PhoneTransferDto(id, phoneNumber, amount, purpose, accountDetailsId);
    }

    private PhoneTransferEntity createPhoneTransferEntity(Long id, Long phoneNumber, BigDecimal amount,
                                                          String purpose, Long accountDetailsId) {
        return new PhoneTransferEntity(id, phoneNumber, amount, purpose, accountDetailsId);
    }

    @Test
    @DisplayName("Должен маппить DTO в Entity, игнорируя id")
    void toEntity_ShouldMapDtoToEntity() {
        PhoneTransferDto dto = createPhoneTransferDto(null, 79161234567L,
                new BigDecimal("150.00"), "Phone transfer", 555L);
        PhoneTransferEntity entity = mapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getPhoneNumber()).isEqualTo(79161234567L);
        assertThat(entity.getAmount()).isEqualByComparingTo(new BigDecimal("150.00"));
        assertThat(entity.getPurpose()).isEqualTo("Phone transfer");
        assertThat(entity.getAccountDetailsId()).isEqualTo(555L);
    }

    @Test
    @DisplayName("Должен вернуть null при null DTO")
    void toEntity_WhenDtoNull_ShouldReturnNull() {
        assertThat(mapper.toEntity(null)).isNull();
    }

    @Test
    @DisplayName("Должен маппить Entity в DTO")
    void toDto_ShouldMapEntityToDto() {
        PhoneTransferEntity entity = createPhoneTransferEntity(1L, 79161234567L,
                new BigDecimal("150.00"), "Phone transfer", 555L);
        PhoneTransferDto dto = mapper.toDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getPhoneNumber()).isEqualTo(79161234567L);
        assertThat(dto.getAmount()).isEqualByComparingTo(new BigDecimal("150.00"));
        assertThat(dto.getPurpose()).isEqualTo("Phone transfer");
        assertThat(dto.getAccountDetailsId()).isEqualTo(555L);
    }

    @Test
    @DisplayName("Должен вернуть null при null Entity")
    void toDto_WhenEntityNull_ShouldReturnNull() {
        assertThat(mapper.toDto(null)).isNull();
    }

    @Test
    @DisplayName("Должен обновить существующую Entity, не меняя id")
    void mergeToEntity_ShouldUpdateExistingEntity() {
        PhoneTransferDto dto = createPhoneTransferDto(
                null, 999L, BigDecimal.valueOf(250), "New phone purpose", 999L);
        PhoneTransferEntity existing = createPhoneTransferEntity(1L, 79161234567L,
                new BigDecimal("150.00"), "Phone transfer", 555L);
        PhoneTransferEntity merged = mapper.mergeToEntity(dto, existing);

        assertThat(merged.getId()).isEqualTo(1L);
        assertThat(merged.getPhoneNumber()).isEqualTo(999L);
        assertThat(merged.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(250));
        assertThat(merged.getPurpose()).isEqualTo("New phone purpose");
        assertThat(merged.getAccountDetailsId()).isEqualTo(999L);
    }

    @Test
    @DisplayName("При null DTO возвращает исходную Entity без изменений")
    void mergeToEntity_WhenDtoNull_ShouldReturnOriginalEntity() {
        PhoneTransferEntity existing = createPhoneTransferEntity(1L, 79161234567L,
                new BigDecimal("150.00"), "Phone transfer", 555L);
        PhoneTransferEntity result = mapper.mergeToEntity(null, existing);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getPhoneNumber()).isEqualTo(79161234567L);
        assertThat(result.getAmount()).isEqualByComparingTo(new BigDecimal("150.00"));
        assertThat(result.getPurpose()).isEqualTo("Phone transfer");
        assertThat(result.getAccountDetailsId()).isEqualTo(555L);
    }

    @Test
    @DisplayName("Должен маппить список Entity в список DTO")
    void toDtoList_ShouldMapList() {
        List<PhoneTransferEntity> entities = List.of(
                createPhoneTransferEntity(1L, 111L, BigDecimal.ONE, "a", 1L),
                createPhoneTransferEntity(2L, 222L, BigDecimal.TEN, "b", 2L)
        );
        List<PhoneTransferDto> dtos = mapper.toDtoList(entities);
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
