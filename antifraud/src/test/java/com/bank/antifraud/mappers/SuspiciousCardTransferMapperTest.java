package com.bank.antifraud.mappers;

import java.util.List;
import com.bank.antifraud.dto.SuspiciousCardTransferDto;
import com.bank.antifraud.entity.SuspiciousCardTransferEntity;
import com.bank.antifraud.util.builder.SuspiciousCardTransferBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class SuspiciousCardTransferMapperTest {
    
    private final SuspiciousCardTransferMapper mapper = new SuspiciousCardTransferMapperImpl();
    
    private final Long TRANSFER_ID_FIRST = 1L;
    private final Long TRANSFER_ID_SECOND = 2L;
    private final Long CARD_TRANSFER_ID_FIRST = 1111L;
    private final Long CARD_TRANSFER_ID_SECOND = 2222L;
    
    @Test
    @DisplayName("Если entity не null, тогда ожидается не null дто со всеми полями")
    void toDto_WhenEntityNotNull_ThenMapToDto() {
        SuspiciousCardTransferBuilder builder = new SuspiciousCardTransferBuilder();
        SuspiciousCardTransferEntity entity = builder.id(TRANSFER_ID_FIRST).buildEntity();
        SuspiciousCardTransferDto expectedDto = builder.buildDto();
        
        SuspiciousCardTransferDto actualDto = mapper.toDto(entity);
        
        assertThat(actualDto)
            .usingRecursiveComparison()
            .isEqualTo(expectedDto);
    }
    
    @Test
    @DisplayName("Если entity null, тогда ожидается null результат")
    void toDto_WhenEntityNull_ThenReturnNull() {
        SuspiciousCardTransferDto dto = mapper.toDto(null);
        
        assertThat(dto).isNull();
    }
    
    @Test
    @DisplayName("Если dto не null, тогда ожидается не null entity со всеми полями (кроме id) из dto")
    void toEntity_WhenDtoNotNull_ThenReturnExpectedEntity() {
        SuspiciousCardTransferBuilder builder = new SuspiciousCardTransferBuilder();
        SuspiciousCardTransferDto dto = builder.buildDto();
        SuspiciousCardTransferEntity expectedEntity = builder.buildEntity();
        
        SuspiciousCardTransferEntity actualEntity = mapper.toEntity(dto);
        
        assertThat(actualEntity)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(expectedEntity);
    }
    
    @Test
    @DisplayName("Если dto null, тогда ожидается null результат")
    void toEntity_WhenDtoNull_ThenReturnNull() {
        SuspiciousCardTransferEntity entity = mapper.toEntity(null);
        
        assertThat(entity).isNull();
    }
    
    @Test
    @DisplayName("Если список entities не пустой и они существуют, то возвращается непустой список dto с теми же id")
    void toListDto_WhenEntitiesNotEmpty_ThenReturnNotEmptyDtos() {
        SuspiciousCardTransferBuilder builder = new SuspiciousCardTransferBuilder();
        SuspiciousCardTransferEntity entity1 = builder
                                                      .id(TRANSFER_ID_FIRST)
                                                      .cardTransferId(CARD_TRANSFER_ID_FIRST)
                                                      .buildEntity();
        SuspiciousCardTransferDto dto1 = builder.buildDto();
        SuspiciousCardTransferEntity entity2 = builder
                                                      .id(TRANSFER_ID_SECOND)
                                                      .cardTransferId(CARD_TRANSFER_ID_SECOND)
                                                      .buildEntity();
        SuspiciousCardTransferDto dto2 = builder.buildDto();
        
        List<SuspiciousCardTransferEntity> entities = List.of(entity1, entity2);
        
        List<SuspiciousCardTransferDto> actualDtos = mapper.toListDto(entities);
        
        assertThat(actualDtos)
            .isNotNull()
            .hasSize(2)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrder(dto1, dto2);
    }
    
    @Test
    @DisplayName("Если список entities null, то ожидается null результат")
    void toListDto_WhenEntitiesListNull_ThenReturnNull() {
        List<SuspiciousCardTransferDto> dtos = mapper.toListDto(null);
        
        assertThat(dtos).isNull();
    }
    
    @Test
    @DisplayName("Если список entities пустой, то ожидается пустой список dto")
    void toListDto_WhenEntitiesListEmpty_ThenReturnEmptyDtoList() {
        List<SuspiciousCardTransferEntity> entities = List.of();
        
        List<SuspiciousCardTransferDto> dtos = mapper.toListDto(entities);
        
        assertThat(dtos)
            .isNotNull()
            .isEmpty();
    }
    
    @Test
    @DisplayName("Если entity и dto валидны, ожидается возврат expectedEntity")
    void mergeToEntity_WhenEntityValidAndDtoValid_ThenReturnEntity() {
        final String newSuspiciousReason = "Подозрительные переводы дробными суммами";
        SuspiciousCardTransferBuilder builder = new SuspiciousCardTransferBuilder();
        SuspiciousCardTransferEntity entity = builder.id(TRANSFER_ID_FIRST).buildEntity();
        
        SuspiciousCardTransferDto dto = builder.suspicious(true, newSuspiciousReason).buildDto();
        
        mapper.mergeToEntity(dto, entity);
        
        assertThat(entity)
            .hasFieldOrPropertyWithValue("id", TRANSFER_ID_FIRST)
            .hasFieldOrPropertyWithValue("suspiciousReason", newSuspiciousReason);
    }
    
    @Test
    @DisplayName("Если dto null, а entity валидна, тогда ожидается возврат той же ссылки на entity")
    void mergeToEntity_WhenDtoNullAndEntityValid_ThenReturnEntity() {
        SuspiciousCardTransferEntity entity = new SuspiciousCardTransferBuilder()
                                                                                 .id(TRANSFER_ID_FIRST)
                                                                                 .buildEntity();
        
        SuspiciousCardTransferEntity mergedEntity = mapper.mergeToEntity(null, entity);
        
        assertThat(mergedEntity)
            .isNotNull()
            .isSameAs(entity);
    }
}