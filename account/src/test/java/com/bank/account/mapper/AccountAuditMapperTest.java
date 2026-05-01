package com.bank.account.mapper;

import com.bank.account.dto.AuditDto;
import com.bank.account.entity.AuditEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AccountAuditMapper Tests")
class AccountAuditMapperTest {

    private AccountAuditMapper accountAuditMapper;

    @BeforeEach
    void setUp(){
        accountAuditMapper = new AccountAuditMapperImpl();
    }

    @Test
    @DisplayName("toDto - успешнок преобразование Entity в DTO")
    void toDto_Success() {
        AuditEntity entity = new AuditEntity();
        entity.setId(1L);
        entity.setEntityType("AccountDetails");
        entity.setOperationType("CREATE");
        entity.setCreatedBy("admin@bank.com");
        entity.setModifiedBy("admin@bank.com");
        entity.setNewEntityJson("{\"accountNumber\":\"12345\"}");
        entity.setEntityJson("{\"id\"1}");

        AuditDto dto = accountAuditMapper.toDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getEntityType()).isEqualTo("AccountDetails");
        assertThat(dto.getOperationType()).isEqualTo("CREATE");
        assertThat(dto.getCreatedBy()).isEqualTo("admin@bank.com");
        assertThat(dto.getModifiedBy()).isEqualTo("admin@bank.com");
        assertThat(dto.getNewEntityJson()).isEqualTo("{\"accountNumber\":\"12345\"}");
        assertThat(dto.getEntityJson()).isEqualTo("{\"id\"1}");
    }

    @Test
    @DisplayName("toDto -негативный: Entity с null полями")
    void toDto_EntityWithNullFields_ReturnsWitNulls(){
        AuditEntity entity = new AuditEntity();
        entity.setId(1L);

        AuditDto dto = accountAuditMapper.toDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getEntityType()).isNull();
        assertThat(dto.getOperationType()).isNull();
        assertThat(dto.getCreatedBy()).isNull();
        assertThat(dto.getModifiedBy()).isNull();
        assertThat(dto.getCreatedAt()).isNull();
        assertThat(dto.getModifiedAt()).isNull();
        assertThat(dto.getNewEntityJson()).isNull();
        assertThat(dto.getEntityJson()).isNull();
    }
}
