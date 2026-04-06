package com.bank.antifraud.mappers;

import java.util.List;
import com.bank.antifraud.dto.SuspiciousPhoneTransferDto;
import com.bank.antifraud.entity.SuspiciousPhoneTransferEntity;
import com.bank.antifraud.util.builder.SuspiciousPhoneTransferBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class SuspiciousPhoneTransferMapperTest {
    
    private final SuspiciousPhoneTransferMapper mapper = new SuspiciousPhoneTransferMapperImpl();
    
    private final Long TRANSFER_ID_FIRST = 1L;
    private final Long TRANSFER_ID_SECOND = 2L;
    private final Long PHONE_TRANSFER_ID_FIRST = 1111L;
    private final Long PHONE_TRANSFER_ID_SECOND = 2222L;
    
    @Test
    @DisplayName("Если entity не null, тогда ожидается не null дто со всеми полями")
    void toDto_WhenEntityNotNull_ThenMapToDto() {
        SuspiciousPhoneTransferBuilder builder = new SuspiciousPhoneTransferBuilder();
        SuspiciousPhoneTransferEntity entity = builder.id(TRANSFER_ID_FIRST).buildEntity();
        SuspiciousPhoneTransferDto expectedDto = builder.buildDto();
        
        SuspiciousPhoneTransferDto actualDto = mapper.toDto(entity);
        
        assertThat(actualDto)
            .usingRecursiveComparison()
            .isEqualTo(expectedDto);
    }
    
    @Test
    @DisplayName("Если entity null, тогда ожидается null результат")
    void toDto_WhenEntityNull_ThenReturnNull() {
        SuspiciousPhoneTransferDto dto = mapper.toDto(null);
        
        assertThat(dto).isNull();
    }
    
    @Test
    @DisplayName("Если dto не null, тогда ожидается не null entity со всеми полями (кроме id) из dto")
    void toEntity_WhenDtoNotNull_ThenReturnExpectedEntity() {
        SuspiciousPhoneTransferBuilder builder = new SuspiciousPhoneTransferBuilder();
        SuspiciousPhoneTransferDto dto = builder.buildDto();
        SuspiciousPhoneTransferEntity expectedEntity = builder.buildEntity();
        
        SuspiciousPhoneTransferEntity actualEntity = mapper.toEntity(dto);
        
        assertThat(actualEntity)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(expectedEntity);
    }
    
    @Test
    @DisplayName("Если dto null, тогда ожидается null результат")
    void toEntity_WhenDtoNull_ThenReturnNull() {
        SuspiciousPhoneTransferEntity entity = mapper.toEntity(null);
        
        assertThat(entity).isNull();
    }
    
    @Test
    @DisplayName("Если список entities не пустой и они существуют, то возвращается непустой список dto с теми же id")
    void toListDto_WhenEntitiesNotEmpty_ThenReturnNotEmptyDtos() {
        SuspiciousPhoneTransferBuilder builder = new SuspiciousPhoneTransferBuilder();
        SuspiciousPhoneTransferEntity entity1 = builder
                                                       .id(TRANSFER_ID_FIRST)
                                                       .phoneTransferId(PHONE_TRANSFER_ID_FIRST)
                                                       .buildEntity();
        SuspiciousPhoneTransferDto dto1 = builder.buildDto();
        SuspiciousPhoneTransferEntity entity2 = builder
                                                       .id(TRANSFER_ID_SECOND)
                                                       .phoneTransferId(PHONE_TRANSFER_ID_SECOND)
                                                       .buildEntity();
        SuspiciousPhoneTransferDto dto2 = builder.buildDto();
        
        List<SuspiciousPhoneTransferEntity> entities = List.of(entity1, entity2);
        
        List<SuspiciousPhoneTransferDto> actualDtos = mapper.toListDto(entities);
        
        assertThat(actualDtos)
            .isNotNull()
            .hasSize(2)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrder(dto1, dto2);
    }
    
    @Test
    @DisplayName("Если список entities null, то ожидается null результат")
    void toListDto_WhenEntitiesListNull_ThenReturnNull() {
        List<SuspiciousPhoneTransferDto> dtos = mapper.toListDto(null);
        
        assertThat(dtos).isNull();
    }
    
    @Test
    @DisplayName("Если список entities пустой, то ожидается пустой список dto")
    void toListDto_WhenEntitiesListEmpty_ThenReturnEmptyDtoList() {
        List<SuspiciousPhoneTransferEntity> entities = List.of();
        
        List<SuspiciousPhoneTransferDto> dtos = mapper.toListDto(entities);
        
        assertThat(dtos)
            .isNotNull()
            .isEmpty();
    }
    
    @Test
    @DisplayName("Если entity и dto валидны, ожидается возврат expectedEntity")
    void mergeToEntity_WhenEntityValidAndDtoValid_ThenReturnEntity() {
        final String newSuspiciousReason = "Подозрительные переводы дробными суммами";
        SuspiciousPhoneTransferBuilder builder = new SuspiciousPhoneTransferBuilder();
        SuspiciousPhoneTransferEntity entity = builder
                                                      .id(TRANSFER_ID_FIRST)
                                                      .buildEntity();
        
        SuspiciousPhoneTransferDto dto = builder.suspicious(true, newSuspiciousReason).buildDto();
        
        mapper.mergeToEntity(dto, entity);
        
        assertThat(entity)
            .hasFieldOrPropertyWithValue("id", TRANSFER_ID_FIRST)
            .hasFieldOrPropertyWithValue("suspiciousReason", newSuspiciousReason);
    }
    
    @Test
    @DisplayName("Если dto null, а entity валидна, тогда ожидается возврат той же ссылки на entity")
    void mergeToEntity_WhenDtoNullAndEntityValid_ThenReturnEntity() {
        SuspiciousPhoneTransferEntity entity = new SuspiciousPhoneTransferBuilder()
                                                                                   .id(TRANSFER_ID_FIRST)
                                                                                   .buildEntity();
        
        SuspiciousPhoneTransferEntity mergedEntity = mapper.mergeToEntity(null, entity);
        
        assertThat(mergedEntity)
            .isNotNull()
            .isSameAs(entity);
    }
}