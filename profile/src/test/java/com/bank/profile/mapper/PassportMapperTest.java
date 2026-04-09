package com.bank.profile.mapper;

import com.bank.profile.dto.PassportDto;
import com.bank.profile.dto.RegistrationDto;
import com.bank.profile.entity.PassportEntity;
import com.bank.profile.entity.RegistrationEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PassportMapperTest {
    private final PassportMapper passportMapper = Mappers.getMapper(PassportMapper.class);

    @Test
    @DisplayName("маппинг в дто со всеми полями")
    void toDto_MapAllFields() {
        PassportEntity passportEntity = createTestPassportEntity(1L, 111, 111L, "lastName",
                "firstName", "middleName", "gender", LocalDate.of(2000, 1, 1),
                "birthPlace", "issuedBy", LocalDate.of(2015, 1, 1),
                1010, LocalDate.of(2040, 1, 1), 1L);

        PassportDto passportDto = passportMapper.toDto(passportEntity);

        assertEquals(passportDto.getId(), passportEntity.getId());
        assertEquals(passportDto.getSeries(), passportEntity.getSeries());
        assertEquals(passportDto.getNumber(), passportEntity.getNumber());
        assertEquals(passportDto.getLastName(), passportEntity.getLastName());
        assertEquals(passportDto.getFirstName(), passportEntity.getFirstName());
        assertEquals(passportDto.getMiddleName(), passportEntity.getMiddleName());
        assertEquals(passportDto.getGender(), passportEntity.getGender());
        assertEquals(passportDto.getBirthDate(), passportEntity.getBirthDate());
        assertEquals(passportDto.getBirthPlace(), passportEntity.getBirthPlace());
        assertEquals(passportDto.getIssuedBy(), passportEntity.getIssuedBy());
        assertEquals(passportDto.getDateOfIssue(), passportEntity.getDateOfIssue());
        assertEquals(passportDto.getDivisionCode(), passportEntity.getDivisionCode());
        assertEquals(passportDto.getExpirationDate(), passportEntity.getExpirationDate());
        assertEquals(passportDto.getRegistration().getId(), passportEntity.getRegistration().getId());
    }

    @Test
    @DisplayName("маппинг в дто из null")
    void toDto_MapNull() {
        PassportDto passportDto = passportMapper.toDto(null);
        assertNull(passportDto);
    }

    @Test
    @DisplayName("маппинг в дто с несколькими полями")
    void toDto_MapSomeFields() {
        PassportEntity passportEntity = createTestPassportEntityPart();

        PassportDto passportDto = passportMapper.toDto(passportEntity);

        assertNotNull(passportDto);
        assertEquals(passportDto.getId(), passportEntity.getId());
        assertEquals(passportDto.getId(), passportEntity.getId());
        assertEquals(passportDto.getSeries(), passportEntity.getSeries());
        assertEquals(passportDto.getNumber(), passportEntity.getNumber());
        assertEquals(passportDto.getLastName(), passportEntity.getLastName());
        assertEquals(passportDto.getFirstName(), passportEntity.getFirstName());
        assertEquals(passportDto.getMiddleName(), passportEntity.getMiddleName());
        assertNull(passportDto.getGender());
        assertNull(passportDto.getBirthDate());
        assertNull(passportDto.getBirthPlace());
        assertNull(passportDto.getIssuedBy());
        assertNull(passportDto.getDateOfIssue());
        assertNull(passportDto.getDivisionCode());
        assertNull(passportDto.getExpirationDate());
        assertNull(passportDto.getRegistration());
    }

    @Test
    @DisplayName("маппинг в сущность со всеми полями и игнором id")
    void toEntity_MapAllFields() {
        PassportDto passportDto = createTestPassportDto();

        PassportEntity passportEntity = passportMapper.toEntity(passportDto);

        assertNull(passportEntity.getId());
        assertEquals(passportEntity.getSeries(), passportDto.getSeries());
        assertEquals(passportEntity.getNumber(), passportDto.getNumber());
        assertEquals(passportEntity.getLastName(), passportDto.getLastName());
        assertEquals(passportEntity.getFirstName(), passportDto.getFirstName());
        assertEquals(passportEntity.getMiddleName(), passportDto.getMiddleName());
        assertEquals(passportEntity.getGender(), passportDto.getGender());
        assertEquals(passportEntity.getBirthDate(), passportDto.getBirthDate());
        assertEquals(passportEntity.getBirthPlace(), passportDto.getBirthPlace());
        assertEquals(passportEntity.getIssuedBy(), passportDto.getIssuedBy());
        assertEquals(passportEntity.getDateOfIssue(), passportDto.getDateOfIssue());
        assertEquals(passportEntity.getDivisionCode(), passportDto.getDivisionCode());
        assertEquals(passportEntity.getExpirationDate(), passportDto.getExpirationDate());
        assertEquals(passportEntity.getRegistration().getId(), passportDto.getRegistration().getId());
    }

    @Test
    @DisplayName("маппинг в сущность из null")
    void toEntity_MapNull() {
        PassportEntity passportEntity = passportMapper.toEntity(null);
        assertNull(passportEntity);
    }

    @Test
    @DisplayName("маппинг в сущность с несколькими полями и игнором id")
    void toEntity_MapSomeFields() {
        PassportDto passportDto = createTestPassportDtoPart();

        PassportEntity passportEntity = passportMapper.toEntity(passportDto);

        assertNull(passportEntity.getId());
        assertEquals(passportEntity.getSeries(), passportDto.getSeries());
        assertEquals(passportEntity.getNumber(), passportDto.getNumber());
        assertEquals(passportEntity.getLastName(), passportDto.getLastName());
        assertEquals(passportEntity.getFirstName(), passportDto.getFirstName());
        assertEquals(passportEntity.getMiddleName(), passportDto.getMiddleName());
        assertNull(passportEntity.getGender());
        assertNull(passportEntity.getBirthDate());
        assertNull(passportEntity.getBirthPlace());
        assertNull(passportEntity.getIssuedBy());
        assertNull(passportEntity.getDivisionCode());
        assertNull(passportEntity.getExpirationDate());
        assertNull(passportEntity.getRegistration());
    }

    @Test
    @DisplayName("обновление в сущность со всеми полями и игнором id")
    void mergeToEntity_MapAllFields() {
        PassportEntity passportEntity = new PassportEntity();

        PassportDto passportDto = createTestPassportDto();

        passportMapper.mergeToEntity(passportDto, passportEntity);

        assertNull(passportEntity.getId());
        assertEquals(passportEntity.getSeries(), passportDto.getSeries());
        assertEquals(passportEntity.getNumber(), passportDto.getNumber());
        assertEquals(passportEntity.getLastName(), passportDto.getLastName());
        assertEquals(passportEntity.getFirstName(), passportDto.getFirstName());
        assertEquals(passportEntity.getMiddleName(), passportDto.getMiddleName());
        assertEquals(passportEntity.getGender(), passportDto.getGender());
        assertEquals(passportEntity.getBirthDate(), passportDto.getBirthDate());
        assertEquals(passportEntity.getBirthPlace(), passportDto.getBirthPlace());
        assertEquals(passportEntity.getIssuedBy(), passportDto.getIssuedBy());
        assertEquals(passportEntity.getDateOfIssue(), passportDto.getDateOfIssue());
        assertEquals(passportEntity.getDivisionCode(), passportDto.getDivisionCode());
        assertEquals(passportEntity.getExpirationDate(), passportDto.getExpirationDate());
        assertEquals(passportEntity.getRegistration().getId(), passportDto.getRegistration().getId());
    }

    @Test
    @DisplayName("обновление в сущность из null")
    void mergeToEntity_MapNull() {
        PassportEntity passportEntity = createTestPassportEntity(1L, 111, 111L, "lastName",
                "firstName", "middleName", "gender", LocalDate.of(2000, 1, 1),
                "birthPlace", "issuedBy", LocalDate.of(2015, 1, 1),
                1010, LocalDate.of(2040, 1, 1), 1L);

        passportMapper.mergeToEntity(null, passportEntity);

        assertEquals(1L, passportEntity.getId());
        assertEquals(111, passportEntity.getSeries());
        assertEquals(111L, passportEntity.getNumber());
        assertEquals("lastName", passportEntity.getLastName());
        assertEquals("firstName", passportEntity.getFirstName());
        assertEquals("middleName", passportEntity.getMiddleName());
        assertEquals("gender", passportEntity.getGender());
        assertEquals(LocalDate.of(2000, 1, 1), passportEntity.getBirthDate());
        assertEquals("birthPlace", passportEntity.getBirthPlace());
        assertEquals("issuedBy", passportEntity.getIssuedBy());
        assertEquals(LocalDate.of(2015, 1, 1), passportEntity.getDateOfIssue());
        assertEquals(1010, passportEntity.getDivisionCode());
        assertEquals(LocalDate.of(2040, 1, 1), passportEntity.getExpirationDate());
        assertEquals(1L, passportEntity.getRegistration().getId());
    }

    @Test
    @DisplayName("обновление в сущность значениями по умолчанию и игнором id")
    void mergeToEntity_MapSomeFields() {
        PassportEntity passportEntity = createTestPassportEntity(1L, 111, 111L, "lastName",
                "firstName", "middleName", "gender", LocalDate.of(2000, 1, 1),
                "birthPlace", "issuedBy", LocalDate.of(2015, 1, 1),
                1010, LocalDate.of(2040, 1, 1), 1L);

        passportMapper.mergeToEntity(new PassportDto(), passportEntity);

        assertEquals(1L, passportEntity.getId());
        assertNull(passportEntity.getSeries());
        assertNull(passportEntity.getNumber());
        assertNull(passportEntity.getLastName());
        assertNull(passportEntity.getFirstName());
        assertNull(passportEntity.getMiddleName());
        assertNull(passportEntity.getGender());
        assertNull(passportEntity.getBirthDate());
        assertNull(passportEntity.getBirthPlace());
        assertNull(passportEntity.getIssuedBy());
        assertNull(passportEntity.getDateOfIssue());
        assertNull(passportEntity.getDivisionCode());
        assertNull(passportEntity.getExpirationDate());
        assertNull(passportEntity.getRegistration());
    }

    @Test
    @DisplayName("маппинг в список дто из списка сущностей")
    void toDtoList_MultipleEntities() {
        PassportEntity passportEntity1 = createTestPassportEntity(1L, 111, 111L, "lastName1",
                "firstName1", "middleName1", "gender1", LocalDate.of(2000, 1, 1),
                "birthPlace1", "issuedBy1", LocalDate.of(2015, 1, 1),
                1010, LocalDate.of(2040, 1, 1), 1L);
        PassportEntity passportEntity2 = createTestPassportEntity(2L, 222, 222L, "lastName2",
                "firstName2", "middleName2", "gender2", LocalDate.of(2005, 5, 5),
                "birthPlace2", "issuedBy2", LocalDate.of(2020, 5, 5),
                2020, LocalDate.of(2045, 5, 5), 2L);
        PassportEntity passportEntity3 = createTestPassportEntity(3L, 333, 333L, "lastName3",
                "firstName3", "middleName3", "gender3", LocalDate.of(2010, 10, 10),
                "birthPlace3", "issuedBy3", LocalDate.of(2025, 10, 10),
                3030, LocalDate.of(2050, 10, 10), 3L);

        List<PassportEntity> passportEntityList = List.of(
                passportEntity1,
                passportEntity2,
                passportEntity3
        );

        List<PassportDto> dtoList = passportMapper.toDtoList(passportEntityList);

        assertEquals(dtoList.size(), passportEntityList.size());

        assertEquals(dtoList.get(0).getId(), passportEntity1.getId());
        assertEquals(dtoList.get(1).getId(), passportEntity2.getId());
        assertEquals(dtoList.get(2).getId(), passportEntity3.getId());

        assertEquals(dtoList.get(0).getSeries(), passportEntity1.getSeries());
        assertEquals(dtoList.get(1).getSeries(), passportEntity2.getSeries());
        assertEquals(dtoList.get(2).getSeries(), passportEntity3.getSeries());

        assertEquals(dtoList.get(0).getNumber(), passportEntity1.getNumber());
        assertEquals(dtoList.get(1).getNumber(), passportEntity2.getNumber());
        assertEquals(dtoList.get(2).getNumber(), passportEntity3.getNumber());

        assertEquals(dtoList.get(0).getLastName(), passportEntity1.getLastName());
        assertEquals(dtoList.get(1).getLastName(), passportEntity2.getLastName());
        assertEquals(dtoList.get(2).getLastName(), passportEntity3.getLastName());

        assertEquals(dtoList.get(0).getFirstName(), passportEntity1.getFirstName());
        assertEquals(dtoList.get(1).getFirstName(), passportEntity2.getFirstName());
        assertEquals(dtoList.get(2).getFirstName(), passportEntity3.getFirstName());

        assertEquals(dtoList.get(0).getMiddleName(), passportEntity1.getMiddleName());
        assertEquals(dtoList.get(1).getMiddleName(), passportEntity2.getMiddleName());
        assertEquals(dtoList.get(2).getMiddleName(), passportEntity3.getMiddleName());

        assertEquals(dtoList.get(0).getGender(), passportEntity1.getGender());
        assertEquals(dtoList.get(1).getGender(), passportEntity2.getGender());
        assertEquals(dtoList.get(2).getGender(), passportEntity3.getGender());

        assertEquals(dtoList.get(0).getBirthDate(), passportEntity1.getBirthDate());
        assertEquals(dtoList.get(1).getBirthDate(), passportEntity2.getBirthDate());
        assertEquals(dtoList.get(2).getBirthDate(), passportEntity3.getBirthDate());

        assertEquals(dtoList.get(0).getBirthPlace(), passportEntity1.getBirthPlace());
        assertEquals(dtoList.get(1).getBirthPlace(), passportEntity2.getBirthPlace());
        assertEquals(dtoList.get(2).getBirthPlace(), passportEntity3.getBirthPlace());

        assertEquals(dtoList.get(0).getIssuedBy(), passportEntity1.getIssuedBy());
        assertEquals(dtoList.get(1).getIssuedBy(), passportEntity2.getIssuedBy());
        assertEquals(dtoList.get(2).getIssuedBy(), passportEntity3.getIssuedBy());

        assertEquals(dtoList.get(0).getDateOfIssue(), passportEntity1.getDateOfIssue());
        assertEquals(dtoList.get(1).getDateOfIssue(), passportEntity2.getDateOfIssue());
        assertEquals(dtoList.get(2).getDateOfIssue(), passportEntity3.getDateOfIssue());

        assertEquals(dtoList.get(0).getDivisionCode(), passportEntity1.getDivisionCode());
        assertEquals(dtoList.get(1).getDivisionCode(), passportEntity2.getDivisionCode());
        assertEquals(dtoList.get(2).getDivisionCode(), passportEntity3.getDivisionCode());

        assertEquals(dtoList.get(0).getExpirationDate(), passportEntity1.getExpirationDate());
        assertEquals(dtoList.get(1).getExpirationDate(), passportEntity2.getExpirationDate());
        assertEquals(dtoList.get(2).getExpirationDate(), passportEntity3.getExpirationDate());

        assertEquals(dtoList.get(0).getRegistration().getId(), passportEntity1.getRegistration().getId());
        assertEquals(dtoList.get(1).getRegistration().getId(), passportEntity2.getRegistration().getId());
        assertEquals(dtoList.get(2).getRegistration().getId(), passportEntity3.getRegistration().getId());
    }

    @Test
    @DisplayName("маппинг в список дто из пустого списка")
    void toDtoList_EmptyList() {
        List<PassportDto> dtoList = passportMapper.toDtoList(Collections.emptyList());

        assertNotNull(dtoList);

        assertEquals(0, dtoList.size());
    }

    @Test
    @DisplayName("маппинг в список дто из null")
    void toDtoList_Null() {
        List<PassportDto> dtoList = passportMapper.toDtoList(null);
        assertNull(dtoList);
    }

    private PassportEntity createTestPassportEntity(Long id, Integer series, Long number, String lastName,
                                                    String firstName, String middleName, String gender,
                                                    LocalDate birthDate, String birthPlace, String issuedBy,
                                                    LocalDate dateOfIssue, Integer divisionCode,
                                                    LocalDate expirationDate, Long registrationEntityId) {
        PassportEntity passportEntity = new PassportEntity();
        passportEntity.setId(id);
        passportEntity.setSeries(series);
        passportEntity.setNumber(number);
        passportEntity.setLastName(lastName);
        passportEntity.setFirstName(firstName);
        passportEntity.setMiddleName(middleName);
        passportEntity.setGender(gender);
        passportEntity.setBirthDate(birthDate);
        passportEntity.setBirthPlace(birthPlace);
        passportEntity.setIssuedBy(issuedBy);
        passportEntity.setDateOfIssue(dateOfIssue);
        passportEntity.setDivisionCode(divisionCode);
        passportEntity.setExpirationDate(expirationDate);
        RegistrationEntity registration = new RegistrationEntity();
        registration.setId(registrationEntityId);
        passportEntity.setRegistration(registration);
        return passportEntity;
    }

    private PassportEntity createTestPassportEntityPart() {
        PassportEntity passportEntity = new PassportEntity();
        passportEntity.setId(1L);
        passportEntity.setSeries(111);
        passportEntity.setNumber(111L);
        passportEntity.setLastName("lastName");
        passportEntity.setFirstName("firstName");
        passportEntity.setMiddleName("middleName");
        return passportEntity;
    }

    private PassportDto createTestPassportDto() {
        PassportDto passportDto = new PassportDto();
        passportDto.setId(1L);
        passportDto.setSeries(111);
        passportDto.setNumber(111L);
        passportDto.setLastName("lastName");
        passportDto.setFirstName("firstName");
        passportDto.setMiddleName("middleName");
        passportDto.setGender("gender");
        passportDto.setBirthDate(LocalDate.of(2000, 1, 1));
        passportDto.setBirthPlace("birthPlace");
        passportDto.setIssuedBy("issuedBy");
        passportDto.setDateOfIssue(LocalDate.of(2015, 1, 1));
        passportDto.setDivisionCode(1010);
        passportDto.setExpirationDate(LocalDate.of(2040, 1, 1));
        RegistrationDto registration = new RegistrationDto();
        registration.setId(1L);
        passportDto.setRegistration(registration);
        return passportDto;
    }

    private PassportDto createTestPassportDtoPart() {
        PassportDto passportDto = new PassportDto();
        passportDto.setId(1L);
        passportDto.setSeries(111);
        passportDto.setNumber(111L);
        passportDto.setLastName("lastName");
        passportDto.setFirstName("firstName");
        passportDto.setMiddleName("middleName");
        return passportDto;
    }
}
