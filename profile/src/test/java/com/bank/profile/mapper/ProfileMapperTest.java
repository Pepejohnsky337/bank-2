package com.bank.profile.mapper;

import com.bank.profile.dto.ActualRegistrationDto;
import com.bank.profile.dto.PassportDto;
import com.bank.profile.dto.ProfileDto;
import com.bank.profile.entity.ActualRegistrationEntity;
import com.bank.profile.entity.PassportEntity;
import com.bank.profile.entity.ProfileEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProfileMapperTest {

    private final ProfileMapper profileMapper = Mappers.getMapper(ProfileMapper.class);

    @Test
    @DisplayName("маппинг в дто со всеми полями")
    void toDto_MapAllFields() {
        ProfileEntity profileEntity = createTestProfileEntity(1L, 1234567890L, "email",
                "nameOnCard", 11L, 111L, 1111L, 11111L);

        ProfileDto profileDto = profileMapper.toDto(profileEntity);

        assertEquals(profileDto.getId(), profileEntity.getId());
        assertEquals(profileDto.getPhoneNumber(), profileEntity.getPhoneNumber());
        assertEquals(profileDto.getEmail(), profileEntity.getEmail());
        assertEquals(profileDto.getNameOnCard(), profileEntity.getNameOnCard());
        assertEquals(profileDto.getInn(), profileEntity.getInn());
        assertEquals(profileDto.getSnils(), profileEntity.getSnils());
        assertEquals(profileDto.getPassport().getId(), profileEntity.getPassport().getId());
        assertEquals(profileDto.getActualRegistration().getId(), profileEntity.getActualRegistration().getId());
    }

    @Test
    @DisplayName("маппинг в дто из null")
    void toDto_MapNull() {
        ProfileDto profileDto = profileMapper.toDto(null);
        assertNull(profileDto);
    }

    @Test
    @DisplayName("маппинг в дто с несколькими полями")
    void toDto_MapSomeFields() {
        ProfileEntity profileEntity = createTestProfileEntityPart();

        ProfileDto profileDto = profileMapper.toDto(profileEntity);

        assertNotNull(profileDto);
        assertEquals(profileDto.getId(), profileEntity.getId());
        assertEquals(profileDto.getPhoneNumber(), profileEntity.getPhoneNumber());
        assertEquals(profileDto.getEmail(), profileEntity.getEmail());
        assertNull(profileDto.getNameOnCard());
        assertNull(profileDto.getInn());
        assertNull(profileDto.getSnils());
        assertNull(profileDto.getPassport());
        assertNull(profileDto.getActualRegistration());
    }

    @Test
    @DisplayName("маппинг в сущность со всеми полями и игнором id")
    void toEntity_MapAllFields() {
        ProfileDto profileDto = createTestProfileDto();

        ProfileEntity profileEntity = profileMapper.toEntity(profileDto);

        assertNull(profileEntity.getId());
        assertEquals(profileEntity.getPhoneNumber(), profileDto.getPhoneNumber());
        assertEquals(profileEntity.getEmail(), profileDto.getEmail());
        assertEquals(profileEntity.getNameOnCard(), profileDto.getNameOnCard());
        assertEquals(profileEntity.getInn(), profileDto.getInn());
        assertEquals(profileEntity.getSnils(), profileDto.getSnils());
        assertEquals(profileEntity.getPassport().getId(), profileDto.getPassport().getId());
        assertEquals(profileEntity.getActualRegistration().getId(), profileDto.getActualRegistration().getId());
    }

    @Test
    @DisplayName("маппинг в сущность из null")
    void toEntity_MapNull() {
        ProfileEntity profileEntity = profileMapper.toEntity(null);
        assertNull(profileEntity);
    }

    @Test
    @DisplayName("маппинг в сущность с несколькими полями и игнором id")
    void toEntity_MapSomeFields() {
        ProfileDto profileDto = createTestProfileDtoPart();

        ProfileEntity profileEntity = profileMapper.toEntity(profileDto);

        assertNull(profileEntity.getId());
        assertEquals(profileEntity.getPhoneNumber(), profileDto.getPhoneNumber());
        assertEquals(profileEntity.getEmail(), profileDto.getEmail());
        assertEquals(profileEntity.getNameOnCard(), profileDto.getNameOnCard());
        assertNull(profileEntity.getInn());
        assertNull(profileEntity.getSnils());
        assertNull(profileEntity.getPassport());
        assertNull(profileEntity.getActualRegistration());

    }

    @Test
    @DisplayName("обновление в сущность со всеми полями и игнором id")
    void mergeToEntity_MapAllFields() {
        ProfileEntity profileEntity = new ProfileEntity();

        ProfileDto profileDto = createTestProfileDto();

        profileMapper.mergeToEntity(profileDto, profileEntity);

        assertNull(profileEntity.getId());
        assertEquals(profileEntity.getPhoneNumber(), profileDto.getPhoneNumber());
        assertEquals(profileEntity.getEmail(), profileDto.getEmail());
        assertEquals(profileEntity.getNameOnCard(), profileDto.getNameOnCard());
        assertEquals(profileEntity.getInn(), profileDto.getInn());
        assertEquals(profileEntity.getSnils(), profileDto.getSnils());
        assertEquals(profileEntity.getPassport().getId(), profileDto.getPassport().getId());
        assertEquals(profileEntity.getActualRegistration().getId(), profileDto.getActualRegistration().getId());
    }

    @Test
    @DisplayName("обновление в сущность из null")
    void mergeToEntity_MapNull() {
        ProfileEntity profileEntity = createTestProfileEntity(1L, 1234567890L, "email",
                "nameOnCard", 11L, 111L, 1111L, 11111L);

        profileMapper.mergeToEntity(null, profileEntity);

        assertEquals(1L, profileEntity.getId());
        assertEquals(1234567890, profileEntity.getPhoneNumber());
        assertEquals("email", profileEntity.getEmail());
        assertEquals("nameOnCard", profileEntity.getNameOnCard());
        assertEquals(11L, profileEntity.getInn());
        assertEquals(111L, profileEntity.getSnils());
        assertEquals(1111L, profileEntity.getPassport().getId());
        assertEquals(11111L, profileEntity.getActualRegistration().getId());
    }

    @Test
    @DisplayName("обновление в сущность значениями по умолчанию и игнором id")
    void mergeToEntity_MapSomeFields() {
        ProfileEntity profileEntity = createTestProfileEntity(1L, 1234567890L, "email",
                "nameOnCard", 11L, 111L, 1111L, 11111L);

        profileMapper.mergeToEntity(new ProfileDto(), profileEntity);

        assertEquals(1L, profileEntity.getId());
        assertNull(profileEntity.getPhoneNumber());
        assertNull(profileEntity.getEmail());
        assertNull(profileEntity.getNameOnCard());
        assertNull(profileEntity.getInn());
        assertNull(profileEntity.getSnils());
        assertNull(profileEntity.getPassport());
        assertNull(profileEntity.getActualRegistration());
    }

    @Test
    @DisplayName("маппинг в список дто из списка сущностей")
    void toDtoList_MultipleEntities() {
        ProfileEntity profileEntity1 = createTestProfileEntity(1L, 1111111111L, "email",
                "nameOnCard", 11L, 111L, 1111L, 11111L);

        ProfileEntity profileEntity2 = createTestProfileEntity(2L, 2222222222L, "email",
                "nameOnCard", 22L, 222L, 2222L, 22222L);

        ProfileEntity profileEntity3 = createTestProfileEntity(3L, 3333333333L, "email",
                "nameOnCard", 33L, 333L, 3333L, 33333L);

        List<ProfileEntity> profileEntityList = List.of(
                profileEntity1,
                profileEntity2,
                profileEntity3
        );

        List<ProfileDto> dtoList = profileMapper.toDtoList(profileEntityList);

        assertEquals(dtoList.size(), profileEntityList.size());

        assertEquals(dtoList.get(0).getId(), profileEntity1.getId());
        assertEquals(dtoList.get(1).getId(), profileEntity2.getId());
        assertEquals(dtoList.get(2).getId(), profileEntity3.getId());

        assertEquals(dtoList.get(0).getPhoneNumber(), profileEntity1.getPhoneNumber());
        assertEquals(dtoList.get(1).getPhoneNumber(), profileEntity2.getPhoneNumber());
        assertEquals(dtoList.get(2).getPhoneNumber(), profileEntity3.getPhoneNumber());

        assertEquals(dtoList.get(0).getEmail(), profileEntity1.getEmail());
        assertEquals(dtoList.get(1).getEmail(), profileEntity2.getEmail());
        assertEquals(dtoList.get(2).getEmail(), profileEntity3.getEmail());

        assertEquals(dtoList.get(0).getNameOnCard(), profileEntity1.getNameOnCard());
        assertEquals(dtoList.get(1).getNameOnCard(), profileEntity2.getNameOnCard());
        assertEquals(dtoList.get(2).getNameOnCard(), profileEntity3.getNameOnCard());

        assertEquals(dtoList.get(0).getInn(), profileEntity1.getInn());
        assertEquals(dtoList.get(1).getInn(), profileEntity2.getInn());
        assertEquals(dtoList.get(2).getInn(), profileEntity3.getInn());

        assertEquals(dtoList.get(0).getSnils(), profileEntity1.getSnils());
        assertEquals(dtoList.get(1).getSnils(), profileEntity2.getSnils());
        assertEquals(dtoList.get(2).getSnils(), profileEntity3.getSnils());

        assertEquals(dtoList.get(0).getPassport().getId(), profileEntity1.getPassport().getId());
        assertEquals(dtoList.get(1).getPassport().getId(), profileEntity2.getPassport().getId());
        assertEquals(dtoList.get(2).getPassport().getId(), profileEntity3.getPassport().getId());

        assertEquals(dtoList.get(0).getActualRegistration().getId(), profileEntity1.getActualRegistration().getId());
        assertEquals(dtoList.get(1).getActualRegistration().getId(), profileEntity2.getActualRegistration().getId());
        assertEquals(dtoList.get(2).getActualRegistration().getId(), profileEntity3.getActualRegistration().getId());

    }

    @Test
    @DisplayName("маппинг в список дто из пустого списка")
    void toDtoList_EmptyList() {
        List<ProfileDto> dtoList = profileMapper.toDtoList(Collections.emptyList());

        assertNotNull(dtoList);

        assertEquals(0, dtoList.size());
    }

    @Test
    @DisplayName("маппинг в список дто из null")
    void toDtoList_Null() {
        List<ProfileDto> dtoList = profileMapper.toDtoList(null);
        assertNull(dtoList);
    }

    private ProfileEntity createTestProfileEntity(Long id, Long phoneNumber, String email, String nameOnCard,
                                                  Long inn, Long snils, Long passportEntityId, Long actualRegistrationId) {
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setId(id);
        profileEntity.setPhoneNumber(phoneNumber);
        profileEntity.setEmail(email);
        profileEntity.setNameOnCard(nameOnCard);
        profileEntity.setInn(inn);
        profileEntity.setSnils(snils);
        PassportEntity passport = new PassportEntity();
        passport.setId(passportEntityId);
        profileEntity.setPassport(passport);
        ActualRegistrationEntity actualRegistration = new ActualRegistrationEntity();
        actualRegistration.setId(actualRegistrationId);
        profileEntity.setActualRegistration(actualRegistration);
        return profileEntity;
    }

    private ProfileEntity createTestProfileEntityPart() {
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setId(1L);
        profileEntity.setPhoneNumber(1234567890L);
        profileEntity.setEmail("email");
        return profileEntity;
    }

    private ProfileDto createTestProfileDto() {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setId(1L);
        profileDto.setPhoneNumber(1234567890L);
        profileDto.setEmail("email");
        profileDto.setNameOnCard("nameOnCard");
        profileDto.setInn(11L);
        profileDto.setSnils(111L);
        PassportDto passport = new PassportDto();
        passport.setId(1111L);
        profileDto.setPassport(passport);
        ActualRegistrationDto actualRegistration = new ActualRegistrationDto();
        actualRegistration.setId(11111L);
        profileDto.setActualRegistration(actualRegistration);
        return profileDto;
    }

    private ProfileDto createTestProfileDtoPart() {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setId(1L);
        profileDto.setPhoneNumber(1234567890L);
        profileDto.setEmail("email");
        profileDto.setNameOnCard("nameOnCard");
        return profileDto;
    }
}
