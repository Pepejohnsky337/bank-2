package com.bank.profile.mapper;

import com.bank.profile.dto.AccountDetailsIdDto;
import com.bank.profile.dto.ProfileDto;
import com.bank.profile.entity.AccountDetailsIdEntity;
import com.bank.profile.entity.ProfileEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AccountDetailsIdMapperTest {

    private final AccountDetailsIdMapper accountDetailsIdMapper = Mappers.getMapper(AccountDetailsIdMapper.class);

    @Test
    @DisplayName("маппинг в дто со всеми полями")
    void toDto_MapAllFields() {
        AccountDetailsIdEntity accountDetailsIdEntity = createTestAccountDetailsIdEntity(1L, 2L, 3L);

        AccountDetailsIdDto accountDetailsIdDto = accountDetailsIdMapper.toDto(accountDetailsIdEntity);

        assertEquals(accountDetailsIdDto.getId(), accountDetailsIdEntity.getId());
        assertEquals(accountDetailsIdDto.getAccountId(), accountDetailsIdEntity.getAccountId());
        assertEquals(accountDetailsIdDto.getProfile().getId(), accountDetailsIdEntity.getProfile().getId());
    }

    @Test
    @DisplayName("маппинг в дто из null")
    void toDto_MapNull() {
        AccountDetailsIdDto accountDetailsIdDto = accountDetailsIdMapper.toDto(null);
        assertNull(accountDetailsIdDto);
    }

    @Test
    @DisplayName("маппинг в дто с несколькими полями")
    void toDto_MapSomeFields() {
        AccountDetailsIdEntity accountDetailsIdEntity = createTestAccountDetailsIdEntityPart();

        AccountDetailsIdDto accountDetailsIdDto = accountDetailsIdMapper.toDto(accountDetailsIdEntity);

        assertNotNull(accountDetailsIdDto);
        assertEquals(accountDetailsIdDto.getId(), accountDetailsIdEntity.getId());
        assertNull(accountDetailsIdDto.getAccountId());
        assertNull(accountDetailsIdDto.getProfile());
    }

    @Test
    @DisplayName("маппинг в сущность со всеми полями и игнором id")
    void toEntity_MapAllFields() {
        AccountDetailsIdDto accountDetailsIdDto = createTestAccountDetailsIdDto();

        AccountDetailsIdEntity accountDetailsIdEntity = accountDetailsIdMapper.toEntity(accountDetailsIdDto);

        assertNull(accountDetailsIdEntity.getId());
        assertEquals(accountDetailsIdEntity.getAccountId(), accountDetailsIdDto.getAccountId());
        assertEquals(accountDetailsIdEntity.getProfile().getId(), accountDetailsIdDto.getProfile().getId());
    }

    @Test
    @DisplayName("маппинг в сущность из null")
    void toEntity_MapNull() {
        AccountDetailsIdEntity accountDetailsIdEntity = accountDetailsIdMapper.toEntity(null);
        assertNull(accountDetailsIdEntity);
    }

    @Test
    @DisplayName("маппинг в сущность с несколькими полями и игнором id")
    void toEntity_MapSomeFields() {
        AccountDetailsIdDto accountDetailsIdDto = createTestAccountDetailsIdDtoPart();
        AccountDetailsIdEntity accountDetailsIdEntity = accountDetailsIdMapper.toEntity(accountDetailsIdDto);

        assertNull(accountDetailsIdEntity.getId());
        assertEquals(accountDetailsIdEntity.getAccountId(), accountDetailsIdDto.getAccountId());
        assertNull(accountDetailsIdEntity.getProfile());
    }

    @Test
    @DisplayName("обновление в сущность со всеми полями и игнором id")
    void mergeToEntity_MapAllFields() {
        AccountDetailsIdEntity accountDetailsIdEntity = new AccountDetailsIdEntity();

        AccountDetailsIdDto accountDetailsIdDto = createTestAccountDetailsIdDto();

        accountDetailsIdMapper.mergeToEntity(accountDetailsIdDto, accountDetailsIdEntity);

        assertNull(accountDetailsIdEntity.getId());
        assertEquals(accountDetailsIdEntity.getAccountId(), accountDetailsIdDto.getAccountId());
        assertEquals(accountDetailsIdEntity.getProfile().getId(), accountDetailsIdDto.getProfile().getId());
    }

    @Test
    @DisplayName("обновление в сущность из null")
    void mergeToEntity_MapNull() {
        AccountDetailsIdEntity accountDetailsIdEntity = createTestAccountDetailsIdEntity(1L, 2L, 3L);

        accountDetailsIdMapper.mergeToEntity(null, accountDetailsIdEntity);

        assertEquals(1L, accountDetailsIdEntity.getId());
        assertEquals(2L, accountDetailsIdEntity.getAccountId());
        assertEquals(3L, accountDetailsIdEntity.getProfile().getId());
    }

    @Test
    @DisplayName("обновление в сущность значениями по умолчанию и игнором id")
    void mergeToEntity_MapSomeFields() {
        AccountDetailsIdEntity accountDetailsIdEntity = createTestAccountDetailsIdEntity(1L, 2L, 3L);

        accountDetailsIdMapper.mergeToEntity(new AccountDetailsIdDto(), accountDetailsIdEntity);

        assertEquals(1L, accountDetailsIdEntity.getId());
        assertNull(accountDetailsIdEntity.getAccountId());
        assertNull(accountDetailsIdEntity.getProfile());
    }

    @Test
    @DisplayName("маппинг в список дто из списка сущностей")
    void toDtoList_MultipleEntities() {
        AccountDetailsIdEntity accountDetailsIdEntity1 = createTestAccountDetailsIdEntity(1L, 2L, 3L);

        AccountDetailsIdEntity accountDetailsIdEntity2 = createTestAccountDetailsIdEntity(11L, 22L, 33L);

        AccountDetailsIdEntity accountDetailsIdEntity3 = createTestAccountDetailsIdEntity(111L, 222L, 333L);

        List<AccountDetailsIdEntity> accountDetailsIdEntityList = List.of(
                accountDetailsIdEntity1,
                accountDetailsIdEntity2,
                accountDetailsIdEntity3
        );

        List<AccountDetailsIdDto> dtoList = accountDetailsIdMapper.toDtoList(accountDetailsIdEntityList);

        assertEquals(dtoList.size(), accountDetailsIdEntityList.size());
        assertEquals(dtoList.get(0).getId(), accountDetailsIdEntity1.getId());
        assertEquals(dtoList.get(1).getId(), accountDetailsIdEntity2.getId());
        assertEquals(dtoList.get(2).getId(), accountDetailsIdEntity3.getId());

        assertEquals(dtoList.get(0).getAccountId(), accountDetailsIdEntity1.getAccountId());
        assertEquals(dtoList.get(1).getAccountId(), accountDetailsIdEntity2.getAccountId());
        assertEquals(dtoList.get(2).getAccountId(), accountDetailsIdEntity3.getAccountId());

        assertEquals(dtoList.get(0).getProfile().getId(), accountDetailsIdEntity1.getProfile().getId());
        assertEquals(dtoList.get(1).getProfile().getId(), accountDetailsIdEntity2.getProfile().getId());
        assertEquals(dtoList.get(2).getProfile().getId(), accountDetailsIdEntity3.getProfile().getId());
    }

    @Test
    @DisplayName("маппинг в список дто из пустого списка")
    void toDtoList_EmptyList() {
        List<AccountDetailsIdDto> dtoList = accountDetailsIdMapper.toDtoList(Collections.emptyList());
        assertNotNull(dtoList);

        assertEquals(0, dtoList.size());
    }

    @Test
    @DisplayName("маппинг в список дто из null")
    void toDtoList_Null() {
        List<AccountDetailsIdDto> dtoList = accountDetailsIdMapper.toDtoList(null);
        assertNull(dtoList);
    }

    private AccountDetailsIdEntity createTestAccountDetailsIdEntity(Long id, Long accountId, Long profileEntityId) {
        AccountDetailsIdEntity accountDetailsIdEntity = new AccountDetailsIdEntity();
        accountDetailsIdEntity.setId(id);
        accountDetailsIdEntity.setAccountId(accountId);
        ProfileEntity profile = new ProfileEntity();
        profile.setId(profileEntityId);
        accountDetailsIdEntity.setProfile(profile);
        return accountDetailsIdEntity;
    }

    private AccountDetailsIdEntity createTestAccountDetailsIdEntityPart() {
        AccountDetailsIdEntity accountDetailsIdEntity = new AccountDetailsIdEntity();
        accountDetailsIdEntity.setId(1L);
        return accountDetailsIdEntity;
    }

    private AccountDetailsIdDto createTestAccountDetailsIdDto() {
        AccountDetailsIdDto accountDetailsIdDto = new AccountDetailsIdDto();
        accountDetailsIdDto.setId(1L);
        accountDetailsIdDto.setAccountId(2L);
        ProfileDto profileDto = new ProfileDto();
        profileDto.setId(3L);
        accountDetailsIdDto.setProfile(profileDto);
        return accountDetailsIdDto;
    }

    private AccountDetailsIdDto createTestAccountDetailsIdDtoPart() {
        AccountDetailsIdDto accountDetailsIdDto = new AccountDetailsIdDto();
        accountDetailsIdDto.setId(1L);
        accountDetailsIdDto.setAccountId(2L);
        return accountDetailsIdDto;
    }
}