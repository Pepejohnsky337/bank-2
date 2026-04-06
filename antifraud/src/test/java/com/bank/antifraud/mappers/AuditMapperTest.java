package com.bank.antifraud.mappers;

import com.bank.antifraud.dto.AuditDto;
import com.bank.antifraud.entity.AuditEntity;
import com.bank.antifraud.util.builder.AuditBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AuditMapperTest {
    
    private final AuditMapper mapper = new AuditMapperImpl();
    
    @Test
    @DisplayName("Если entity не null, тогда возвращается валидный dto")
    void toDto_WhenEntityNotNull_ThenReturnValidDto() {
        AuditBuilder builder = new AuditBuilder();
        AuditEntity validEntity = builder.id(1L).buildEntity();
        AuditDto expectedDto = builder.buildDto();
        
        AuditDto actualDto = mapper.toDto(validEntity);
        
        assertThat(actualDto)
            .isNotNull()
            .usingRecursiveComparison()
            .isEqualTo(expectedDto);
    }
    
    @Test
    @DisplayName("Если entity null, тогда возвращается null результат")
    void toDto_WhenEntityNull_ThenReturnNull() {
        AuditDto dto = mapper.toDto(null);
        
        assertThat(dto).isNull();
    }
    
}