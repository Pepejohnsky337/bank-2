package com.bank.profile.mapper;

import com.bank.profile.dto.RegistrationDto;
import com.bank.profile.entity.RegistrationEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RegistrationMapperTest {
    private final RegistrationMapper registrationMapper = Mappers.getMapper(RegistrationMapper.class);

    @Test
    @DisplayName("маппинг в дто со всеми полями")
    void toDto_MapAllFields() {
        RegistrationEntity registrationEntity = createTestRegistrationEntity(1L, "country", "region",
                "city", "district", "locality", "street", "houseNumber",
                "houseBlock", "flatNumber", 111L);

        RegistrationDto registrationDto = registrationMapper.toDto(registrationEntity);

        assertEquals(registrationDto.getId(), registrationEntity.getId());
        assertEquals(registrationDto.getCountry(), registrationEntity.getCountry());
        assertEquals(registrationDto.getRegion(), registrationEntity.getRegion());
        assertEquals(registrationDto.getCity(), registrationEntity.getCity());
        assertEquals(registrationDto.getDistrict(), registrationEntity.getDistrict());
        assertEquals(registrationDto.getLocality(), registrationEntity.getLocality());
        assertEquals(registrationDto.getStreet(), registrationEntity.getStreet());
        assertEquals(registrationDto.getHouseNumber(), registrationEntity.getHouseNumber());
        assertEquals(registrationDto.getHouseBlock(), registrationEntity.getHouseBlock());
        assertEquals(registrationDto.getFlatNumber(), registrationEntity.getFlatNumber());
        assertEquals(registrationDto.getIndex(), registrationEntity.getIndex());
    }

    @Test
    @DisplayName("маппинг в дто из null")
    void toDto_MapNull() {
        RegistrationDto registrationDto = registrationMapper.toDto(null);
        assertNull(registrationDto);
    }

    @Test
    @DisplayName("маппинг в дто с несколькими полями")
    void toDto_MapSomeFields() {
        RegistrationEntity registrationEntity = createTestRegistrationEntityPart();

        RegistrationDto registrationDto = registrationMapper.toDto(registrationEntity);

        assertNotNull(registrationDto);
        assertEquals(registrationDto.getId(), registrationEntity.getId());
        assertEquals(registrationDto.getCountry(), registrationEntity.getCountry());
        assertEquals(registrationDto.getRegion(), registrationEntity.getRegion());
        assertEquals(registrationDto.getCity(), registrationEntity.getCity());
        assertNull(registrationDto.getDistrict());
        assertNull(registrationDto.getLocality());
        assertNull(registrationDto.getStreet());
        assertNull(registrationDto.getHouseNumber());
        assertNull(registrationDto.getHouseBlock());
        assertNull(registrationDto.getFlatNumber());
        assertNull(registrationDto.getIndex());
    }

    @Test
    @DisplayName("маппинг в сущность со всеми полями и игнором id")
    void toEntity_MapAllFields() {
        RegistrationDto registrationDto = createTestRegistrationDto();

        RegistrationEntity registrationEntity = registrationMapper.toEntity(registrationDto);

        assertNull(registrationEntity.getId());
        assertEquals(registrationEntity.getCountry(), registrationDto.getCountry());
        assertEquals(registrationEntity.getRegion(), registrationDto.getRegion());
        assertEquals(registrationEntity.getCity(), registrationDto.getCity());
        assertEquals(registrationEntity.getDistrict(), registrationDto.getDistrict());
        assertEquals(registrationEntity.getLocality(), registrationDto.getLocality());
        assertEquals(registrationEntity.getStreet(), registrationDto.getStreet());
        assertEquals(registrationEntity.getHouseNumber(), registrationDto.getHouseNumber());
        assertEquals(registrationEntity.getHouseBlock(), registrationDto.getHouseBlock());
        assertEquals(registrationEntity.getFlatNumber(), registrationDto.getFlatNumber());
        assertEquals(registrationEntity.getIndex(), registrationDto.getIndex());
    }

    @Test
    @DisplayName("маппинг в сущность из null")
    void toEntity_MapNull() {
        RegistrationEntity registrationEntity = registrationMapper.toEntity(null);
        assertNull(registrationEntity);
    }

    @Test
    @DisplayName("маппинг в сущность с несколькими полями и игнором id")
    void toEntity_MapSomeFields() {
        RegistrationDto registrationDto = createTestRegistrationDtoPart();

        RegistrationEntity registrationEntity = registrationMapper.toEntity(registrationDto);

        assertNull(registrationEntity.getId());
        assertEquals(registrationEntity.getCountry(), registrationDto.getCountry());
        assertEquals(registrationEntity.getRegion(), registrationDto.getRegion());
        assertEquals(registrationEntity.getCity(), registrationDto.getCity());
        assertNull(registrationEntity.getDistrict());
        assertNull(registrationEntity.getLocality());
        assertNull(registrationEntity.getStreet());
        assertNull(registrationEntity.getHouseNumber());
        assertNull(registrationEntity.getHouseBlock());
        assertNull(registrationEntity.getFlatNumber());
        assertNull(registrationEntity.getIndex());
    }

    @Test
    @DisplayName("обновление в сущность со всеми полями и игнором id")
    void mergeToEntity_MapAllFields() {
        RegistrationEntity registrationEntity = new RegistrationEntity();

        RegistrationDto registrationDto = createTestRegistrationDto();

        registrationMapper.mergeToEntity(registrationDto, registrationEntity);

        assertNull(registrationEntity.getId());
        assertEquals(registrationEntity.getCountry(), registrationDto.getCountry());
        assertEquals(registrationEntity.getRegion(), registrationDto.getRegion());
        assertEquals(registrationEntity.getCity(), registrationDto.getCity());
        assertEquals(registrationEntity.getDistrict(), registrationDto.getDistrict());
        assertEquals(registrationEntity.getLocality(), registrationDto.getLocality());
        assertEquals(registrationEntity.getStreet(), registrationDto.getStreet());
        assertEquals(registrationEntity.getHouseNumber(), registrationDto.getHouseNumber());
        assertEquals(registrationEntity.getHouseBlock(), registrationDto.getHouseBlock());
        assertEquals(registrationEntity.getFlatNumber(), registrationDto.getFlatNumber());
        assertEquals(registrationEntity.getIndex(), registrationDto.getIndex());
    }

    @Test
    @DisplayName("обновление в сущность из null")
    void mergeToEntity_MapNull() {
        RegistrationEntity registrationEntity = createTestRegistrationEntity(1L, "country", "region",
                "city", "district", "locality", "street", "houseNumber",
                "houseBlock", "flatNumber", 111L);

        registrationMapper.mergeToEntity(null, registrationEntity);

        assertEquals(1L, registrationEntity.getId());
        assertEquals("country", registrationEntity.getCountry());
        assertEquals("region", registrationEntity.getRegion());
        assertEquals("city", registrationEntity.getCity());
        assertEquals("district", registrationEntity.getDistrict());
        assertEquals("locality", registrationEntity.getLocality());
        assertEquals("street", registrationEntity.getStreet());
        assertEquals("houseNumber", registrationEntity.getHouseNumber());
        assertEquals("houseBlock", registrationEntity.getHouseBlock());
        assertEquals("flatNumber", registrationEntity.getFlatNumber());
        assertEquals(111L, registrationEntity.getIndex());
    }

    @Test
    @DisplayName("обновление в сущность значениями по умолчанию и игнором id")
    void mergeToEntity_MapSomeFields() {
        RegistrationEntity registrationEntity = createTestRegistrationEntity(1L, "country", "region",
                "city", "district", "locality", "street", "houseNumber",
                "houseBlock", "flatNumber", 111L);

        registrationMapper.mergeToEntity(new RegistrationDto(), registrationEntity);

        assertEquals(1L, registrationEntity.getId());
        assertNull(registrationEntity.getCountry());
        assertNull(registrationEntity.getRegion());
        assertNull(registrationEntity.getCity());
        assertNull(registrationEntity.getDistrict());
        assertNull(registrationEntity.getLocality());
        assertNull(registrationEntity.getStreet());
        assertNull(registrationEntity.getHouseNumber());
        assertNull(registrationEntity.getHouseBlock());
        assertNull(registrationEntity.getFlatNumber());
        assertNull(registrationEntity.getIndex());
    }

    @Test
    @DisplayName("маппинг в список дто из списка сущностей")
    void toDtoList_MultipleEntities() {
        RegistrationEntity registrationEntity1 = createTestRegistrationEntity(1L, "country1", "region1",
                "city1", "district1", "locality1", "street1", "houseNumber1",
                "houseBlock1", "flatNumber1", 111L);

        RegistrationEntity registrationEntity2 = createTestRegistrationEntity(2L, "country2", "region2",
                "city2", "district2", "locality2", "street2", "houseNumber2",
                "houseBlock2", "flatNumber2", 222L);

        RegistrationEntity registrationEntity3 = createTestRegistrationEntity(3L, "country3", "region3",
                "city3", "district3", "locality3", "street3", "houseNumber3",
                "houseBlock3", "flatNumber3", 333L);

        List<RegistrationEntity> registrationEntityList = List.of(
                registrationEntity1,
                registrationEntity2,
                registrationEntity3
        );

        List<RegistrationDto> dtoList = registrationMapper.toDtoList(registrationEntityList);

        assertEquals(dtoList.size(), registrationEntityList.size());

        assertEquals(dtoList.get(0).getId(), registrationEntity1.getId());
        assertEquals(dtoList.get(1).getId(), registrationEntity2.getId());
        assertEquals(dtoList.get(2).getId(), registrationEntity3.getId());

        assertEquals(dtoList.get(0).getCountry(), registrationEntity1.getCountry());
        assertEquals(dtoList.get(1).getCountry(), registrationEntity2.getCountry());
        assertEquals(dtoList.get(2).getCountry(), registrationEntity3.getCountry());

        assertEquals(dtoList.get(0).getRegion(), registrationEntity1.getRegion());
        assertEquals(dtoList.get(1).getRegion(), registrationEntity2.getRegion());
        assertEquals(dtoList.get(2).getRegion(), registrationEntity3.getRegion());

        assertEquals(dtoList.get(0).getCity(), registrationEntity1.getCity());
        assertEquals(dtoList.get(1).getCity(), registrationEntity2.getCity());
        assertEquals(dtoList.get(2).getCity(), registrationEntity3.getCity());

        assertEquals(dtoList.get(0).getDistrict(), registrationEntity1.getDistrict());
        assertEquals(dtoList.get(1).getDistrict(), registrationEntity2.getDistrict());
        assertEquals(dtoList.get(2).getDistrict(), registrationEntity3.getDistrict());

        assertEquals(dtoList.get(0).getLocality(), registrationEntity1.getLocality());
        assertEquals(dtoList.get(1).getLocality(), registrationEntity2.getLocality());
        assertEquals(dtoList.get(2).getLocality(), registrationEntity3.getLocality());

        assertEquals(dtoList.get(0).getStreet(), registrationEntity1.getStreet());
        assertEquals(dtoList.get(1).getStreet(), registrationEntity2.getStreet());
        assertEquals(dtoList.get(2).getStreet(), registrationEntity3.getStreet());

        assertEquals(dtoList.get(0).getHouseNumber(), registrationEntity1.getHouseNumber());
        assertEquals(dtoList.get(1).getHouseNumber(), registrationEntity2.getHouseNumber());
        assertEquals(dtoList.get(2).getHouseNumber(), registrationEntity3.getHouseNumber());

        assertEquals(dtoList.get(0).getHouseBlock(), registrationEntity1.getHouseBlock());
        assertEquals(dtoList.get(1).getHouseBlock(), registrationEntity2.getHouseBlock());
        assertEquals(dtoList.get(2).getHouseBlock(), registrationEntity3.getHouseBlock());

        assertEquals(dtoList.get(0).getFlatNumber(), registrationEntity1.getFlatNumber());
        assertEquals(dtoList.get(1).getFlatNumber(), registrationEntity2.getFlatNumber());
        assertEquals(dtoList.get(2).getFlatNumber(), registrationEntity3.getFlatNumber());

        assertEquals(dtoList.get(0).getIndex(), registrationEntity1.getIndex());
        assertEquals(dtoList.get(1).getIndex(), registrationEntity2.getIndex());
        assertEquals(dtoList.get(2).getIndex(), registrationEntity3.getIndex());

    }

    @Test
    @DisplayName("маппинг в список дто из пустого списка")
    void toDtoList_EmptyList() {
        List<RegistrationDto> dtoList = registrationMapper.toDtoList(Collections.emptyList());

        assertNotNull(dtoList);

        assertEquals(0, dtoList.size());
    }

    @Test
    @DisplayName("маппинг в список дто из null")
    void toDtoList_Null() {
        List<RegistrationDto> dtoList = registrationMapper.toDtoList(null);
        assertNull(dtoList);
    }

    private RegistrationEntity createTestRegistrationEntity(Long id, String country, String region, String city,
                                                            String district, String locality, String street,
                                                            String houseNumber, String houseBlock, String flatNumber,
                                                            Long index) {
        RegistrationEntity registrationEntity = new RegistrationEntity();
        registrationEntity.setId(id);
        registrationEntity.setCountry(country);
        registrationEntity.setRegion(region);
        registrationEntity.setCity(city);
        registrationEntity.setDistrict(district);
        registrationEntity.setLocality(locality);
        registrationEntity.setStreet(street);
        registrationEntity.setHouseNumber(houseNumber);
        registrationEntity.setHouseBlock(houseBlock);
        registrationEntity.setFlatNumber(flatNumber);
        registrationEntity.setIndex(index);
        return registrationEntity;
    }

    private RegistrationEntity createTestRegistrationEntityPart() {
        RegistrationEntity registrationEntity = new RegistrationEntity();
        registrationEntity.setId(1L);
        registrationEntity.setCountry("country");
        registrationEntity.setRegion("region");
        registrationEntity.setCity("city");
        return registrationEntity;
    }

    private RegistrationDto createTestRegistrationDto() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setId(1L);
        registrationDto.setCountry("country");
        registrationDto.setRegion("region");
        registrationDto.setCity("city");
        registrationDto.setDistrict("district");
        registrationDto.setLocality("locality");
        registrationDto.setStreet("street");
        registrationDto.setHouseNumber("houseNumber");
        registrationDto.setHouseBlock("houseBlock");
        registrationDto.setFlatNumber("flatNumber");
        registrationDto.setIndex(111L);
        return registrationDto;
    }

    private RegistrationDto createTestRegistrationDtoPart() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setId(1L);
        registrationDto.setCountry("country");
        registrationDto.setRegion("region");
        registrationDto.setCity("city");
        return registrationDto;
    }
}
