package com.bank.profile.mapper;

import com.bank.profile.dto.ActualRegistrationDto;
import com.bank.profile.entity.ActualRegistrationEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ActualRegistrationMapperTest {

    private final ActualRegistrationMapper actualRegistrationMapper = Mappers.getMapper(ActualRegistrationMapper.class);

    @Test
    @DisplayName("маппинг в дто со всеми полями")
    void toDto_MapAllFields() {
        ActualRegistrationEntity actualRegistrationEntity = createTestActualRegistrationEntity(1L, "country",
                "region", "city", "district", "locality", "street",
                "houseNumber", "houseBlock", "flatNumber", 123L);

        ActualRegistrationDto actualRegistrationDto = actualRegistrationMapper.toDto(actualRegistrationEntity);

        assertEquals(actualRegistrationDto.getId(), actualRegistrationEntity.getId());
        assertEquals(actualRegistrationDto.getCountry(), actualRegistrationEntity.getCountry());
        assertEquals(actualRegistrationDto.getRegion(), actualRegistrationEntity.getRegion());
        assertEquals(actualRegistrationDto.getCity(), actualRegistrationEntity.getCity());
        assertEquals(actualRegistrationDto.getDistrict(), actualRegistrationEntity.getDistrict());
        assertEquals(actualRegistrationDto.getLocality(), actualRegistrationEntity.getLocality());
        assertEquals(actualRegistrationDto.getStreet(), actualRegistrationEntity.getStreet());
        assertEquals(actualRegistrationDto.getHouseNumber(), actualRegistrationEntity.getHouseNumber());
        assertEquals(actualRegistrationDto.getHouseBlock(), actualRegistrationEntity.getHouseBlock());
        assertEquals(actualRegistrationDto.getFlatNumber(), actualRegistrationEntity.getFlatNumber());
        assertEquals(actualRegistrationDto.getIndex(), actualRegistrationEntity.getIndex());

    }

    @Test
    @DisplayName("маппинг в дто из null")
    void toDto_MapNull() {
        ActualRegistrationDto actualRegistrationDto = actualRegistrationMapper.toDto(null);
        assertNull(actualRegistrationDto);
    }

    @Test
    @DisplayName("маппинг в дто с несколькими полями")
    void toDto_MapSomeFields() {
        ActualRegistrationEntity actualRegistrationEntity = createTestActualRegistrationEntityPart();

        ActualRegistrationDto actualRegistrationDto = actualRegistrationMapper.toDto(actualRegistrationEntity);

        assertNotNull(actualRegistrationDto);
        assertEquals(actualRegistrationDto.getId(), actualRegistrationEntity.getId());
        assertEquals(actualRegistrationDto.getCountry(), actualRegistrationEntity.getCountry());
        assertEquals(actualRegistrationDto.getRegion(), actualRegistrationEntity.getRegion());
        assertEquals(actualRegistrationDto.getCity(), actualRegistrationEntity.getCity());
        assertNull(actualRegistrationDto.getDistrict());
        assertNull(actualRegistrationDto.getLocality());
        assertNull(actualRegistrationDto.getStreet());
        assertNull(actualRegistrationDto.getHouseNumber());
        assertNull(actualRegistrationDto.getHouseBlock());
        assertNull(actualRegistrationDto.getFlatNumber());
        assertNull(actualRegistrationDto.getIndex());
    }

    @Test
    @DisplayName("маппинг в сущность со всеми полями и игнором id")
    void toEntity_MapAllFields() {
        ActualRegistrationDto actualRegistrationDto = createTestActualRegistrationDto();

        ActualRegistrationEntity actualRegistrationEntity = actualRegistrationMapper.toEntity(actualRegistrationDto);

        assertNull(actualRegistrationEntity.getId());
        assertEquals(actualRegistrationEntity.getCountry(), actualRegistrationDto.getCountry());
        assertEquals(actualRegistrationEntity.getRegion(), actualRegistrationDto.getRegion());
        assertEquals(actualRegistrationEntity.getCity(), actualRegistrationDto.getCity());
        assertEquals(actualRegistrationEntity.getDistrict(), actualRegistrationDto.getDistrict());
        assertEquals(actualRegistrationEntity.getLocality(), actualRegistrationDto.getLocality());
        assertEquals(actualRegistrationEntity.getStreet(), actualRegistrationDto.getStreet());
        assertEquals(actualRegistrationEntity.getHouseNumber(), actualRegistrationDto.getHouseNumber());
        assertEquals(actualRegistrationEntity.getHouseBlock(), actualRegistrationDto.getHouseBlock());
        assertEquals(actualRegistrationEntity.getFlatNumber(), actualRegistrationDto.getFlatNumber());
        assertEquals(actualRegistrationEntity.getIndex(), actualRegistrationDto.getIndex());
    }

    @Test
    @DisplayName("маппинг в сущность из null")
    void toEntity_MapNull() {
        ActualRegistrationEntity actualRegistrationEntity = actualRegistrationMapper.toEntity(null);
        assertNull(actualRegistrationEntity);
    }

    @Test
    @DisplayName("маппинг в сущность с несколькими полями и игнором id")
    void toEntity_MapSomeFields() {
        ActualRegistrationDto actualRegistrationDto = createTestActualRegistrationDtoPart();

        ActualRegistrationEntity actualRegistrationEntity = actualRegistrationMapper.toEntity(actualRegistrationDto);

        assertNull(actualRegistrationEntity.getId());
        assertEquals(actualRegistrationEntity.getCountry(), actualRegistrationDto.getCountry());
        assertEquals(actualRegistrationEntity.getRegion(), actualRegistrationDto.getRegion());
        assertEquals(actualRegistrationEntity.getCity(), actualRegistrationDto.getCity());
        assertNull(actualRegistrationEntity.getDistrict());
        assertNull(actualRegistrationEntity.getLocality());
        assertNull(actualRegistrationEntity.getStreet());
        assertNull(actualRegistrationEntity.getHouseNumber());
        assertNull(actualRegistrationEntity.getHouseBlock());
        assertNull(actualRegistrationEntity.getFlatNumber());
        assertNull(actualRegistrationEntity.getIndex());
    }

    @Test
    @DisplayName("обновление в сущность со всеми полями и игнором id")
    void mergeToEntity_MapAllFields() {
        ActualRegistrationEntity actualRegistrationEntity = new ActualRegistrationEntity();

        ActualRegistrationDto actualRegistrationDto = createTestActualRegistrationDto();

        actualRegistrationMapper.mergeToEntity(actualRegistrationDto, actualRegistrationEntity);

        assertNull(actualRegistrationEntity.getId());
        assertEquals(actualRegistrationEntity.getCountry(), actualRegistrationDto.getCountry());
        assertEquals(actualRegistrationEntity.getRegion(), actualRegistrationDto.getRegion());
        assertEquals(actualRegistrationEntity.getCity(), actualRegistrationDto.getCity());
        assertEquals(actualRegistrationEntity.getDistrict(), actualRegistrationDto.getDistrict());
        assertEquals(actualRegistrationEntity.getLocality(), actualRegistrationDto.getLocality());
        assertEquals(actualRegistrationEntity.getStreet(), actualRegistrationDto.getStreet());
        assertEquals(actualRegistrationEntity.getHouseNumber(), actualRegistrationDto.getHouseNumber());
        assertEquals(actualRegistrationEntity.getHouseBlock(), actualRegistrationDto.getHouseBlock());
        assertEquals(actualRegistrationEntity.getFlatNumber(), actualRegistrationDto.getFlatNumber());
        assertEquals(actualRegistrationEntity.getIndex(), actualRegistrationDto.getIndex());
    }

    @Test
    @DisplayName("обновление в сущность из null")
    void mergeToEntity_MapNull() {
        ActualRegistrationEntity actualRegistrationEntity = createTestActualRegistrationEntity(1L, "country",
                "region", "city", "district", "locality", "street",
                "houseNumber", "houseBlock", "flatNumber", 123L);

        actualRegistrationMapper.mergeToEntity(null, actualRegistrationEntity);

        assertEquals(1L, actualRegistrationEntity.getId());
        assertEquals("country", actualRegistrationEntity.getCountry());
        assertEquals("region", actualRegistrationEntity.getRegion());
        assertEquals("city", actualRegistrationEntity.getCity());
        assertEquals("district", actualRegistrationEntity.getDistrict());
        assertEquals("locality", actualRegistrationEntity.getLocality());
        assertEquals("street", actualRegistrationEntity.getStreet());
        assertEquals("houseNumber", actualRegistrationEntity.getHouseNumber());
        assertEquals("houseBlock", actualRegistrationEntity.getHouseBlock());
        assertEquals("flatNumber", actualRegistrationEntity.getFlatNumber());
        assertEquals(123L, actualRegistrationEntity.getIndex());
    }

    @Test
    @DisplayName("обновление в сущность значениями по умолчанию и игнором id")
    void mergeToEntity_MapSomeFields() {
        ActualRegistrationEntity actualRegistrationEntity = createTestActualRegistrationEntity(1L, "country",
                "region", "city", "district", "locality", "street",
                "houseNumber", "houseBlock", "flatNumber", 123L);

        actualRegistrationMapper.mergeToEntity(new ActualRegistrationDto(), actualRegistrationEntity);

        assertEquals(1L, actualRegistrationEntity.getId());
        assertNull(actualRegistrationEntity.getCountry());
        assertNull(actualRegistrationEntity.getRegion());
        assertNull(actualRegistrationEntity.getCity());
        assertNull(actualRegistrationEntity.getDistrict());
        assertNull(actualRegistrationEntity.getLocality());
        assertNull(actualRegistrationEntity.getStreet());
        assertNull(actualRegistrationEntity.getHouseNumber());
        assertNull(actualRegistrationEntity.getHouseBlock());
        assertNull(actualRegistrationEntity.getFlatNumber());
        assertNull(actualRegistrationEntity.getIndex());
    }

    @Test
    @DisplayName("маппинг в список дто из списка сущностей")
    void toDtoList_MultipleEntities() {
        ActualRegistrationEntity actualRegistrationEntity1 = createTestActualRegistrationEntity(1L, "country1",
                "region1", "city1", "district1", "locality1", "street1",
                "houseNumber1", "houseBlock1", "flatNumber1", 111L);

        ActualRegistrationEntity actualRegistrationEntity2 = createTestActualRegistrationEntity(2L, "country2",
                "region2", "city2", "district2", "locality2", "street2",
                "houseNumber2", "houseBlock2", "flatNumber2", 222L);

        ActualRegistrationEntity actualRegistrationEntity3 = createTestActualRegistrationEntity(3L, "country3",
                "region3", "city3", "district3", "locality3", "street3",
                "houseNumber3", "houseBlock3", "flatNumber3", 333L);

        List<ActualRegistrationEntity> actualRegistrationEntityList = List.of(
                actualRegistrationEntity1,
                actualRegistrationEntity2,
                actualRegistrationEntity3
        );

        List<ActualRegistrationDto> dtoList = actualRegistrationMapper.toDtoList(actualRegistrationEntityList);

        assertEquals(dtoList.size(), actualRegistrationEntityList.size());

        assertEquals(dtoList.get(0).getId(), actualRegistrationEntity1.getId());
        assertEquals(dtoList.get(1).getId(), actualRegistrationEntity2.getId());
        assertEquals(dtoList.get(2).getId(), actualRegistrationEntity3.getId());

        assertEquals(dtoList.get(0).getCountry(), actualRegistrationEntity1.getCountry());
        assertEquals(dtoList.get(1).getCountry(), actualRegistrationEntity2.getCountry());
        assertEquals(dtoList.get(2).getCountry(), actualRegistrationEntity3.getCountry());

        assertEquals(dtoList.get(0).getRegion(), actualRegistrationEntity1.getRegion());
        assertEquals(dtoList.get(1).getRegion(), actualRegistrationEntity2.getRegion());
        assertEquals(dtoList.get(2).getRegion(), actualRegistrationEntity3.getRegion());

        assertEquals(dtoList.get(0).getCity(), actualRegistrationEntity1.getCity());
        assertEquals(dtoList.get(1).getCity(), actualRegistrationEntity2.getCity());
        assertEquals(dtoList.get(2).getCity(), actualRegistrationEntity3.getCity());

        assertEquals(dtoList.get(0).getDistrict(), actualRegistrationEntity1.getDistrict());
        assertEquals(dtoList.get(1).getDistrict(), actualRegistrationEntity2.getDistrict());
        assertEquals(dtoList.get(2).getDistrict(), actualRegistrationEntity3.getDistrict());

        assertEquals(dtoList.get(0).getLocality(), actualRegistrationEntity1.getLocality());
        assertEquals(dtoList.get(1).getLocality(), actualRegistrationEntity2.getLocality());
        assertEquals(dtoList.get(2).getLocality(), actualRegistrationEntity3.getLocality());

        assertEquals(dtoList.get(0).getStreet(), actualRegistrationEntity1.getStreet());
        assertEquals(dtoList.get(1).getStreet(), actualRegistrationEntity2.getStreet());
        assertEquals(dtoList.get(2).getStreet(), actualRegistrationEntity3.getStreet());

        assertEquals(dtoList.get(0).getHouseNumber(), actualRegistrationEntity1.getHouseNumber());
        assertEquals(dtoList.get(1).getHouseNumber(), actualRegistrationEntity2.getHouseNumber());
        assertEquals(dtoList.get(2).getHouseNumber(), actualRegistrationEntity3.getHouseNumber());

        assertEquals(dtoList.get(0).getHouseBlock(), actualRegistrationEntity1.getHouseBlock());
        assertEquals(dtoList.get(1).getHouseBlock(), actualRegistrationEntity2.getHouseBlock());
        assertEquals(dtoList.get(2).getHouseBlock(), actualRegistrationEntity3.getHouseBlock());

        assertEquals(dtoList.get(0).getFlatNumber(), actualRegistrationEntity1.getFlatNumber());
        assertEquals(dtoList.get(1).getFlatNumber(), actualRegistrationEntity2.getFlatNumber());
        assertEquals(dtoList.get(2).getFlatNumber(), actualRegistrationEntity3.getFlatNumber());

        assertEquals(dtoList.get(0).getIndex(), actualRegistrationEntity1.getIndex());
        assertEquals(dtoList.get(1).getIndex(), actualRegistrationEntity2.getIndex());
        assertEquals(dtoList.get(2).getIndex(), actualRegistrationEntity3.getIndex());
    }

    @Test
    @DisplayName("маппинг в список дто из пустого списка")
    void toDtoList_EmptyList() {
        List<ActualRegistrationDto> dtoList = actualRegistrationMapper.toDtoList(Collections.emptyList());
        assertNotNull(dtoList);

        assertEquals(0, dtoList.size());
    }

    @Test
    @DisplayName("маппинг в список дто из null")
    void toDtoList_Null() {
        List<ActualRegistrationDto> dtoList = actualRegistrationMapper.toDtoList(null);
        assertNull(dtoList);
    }

    private ActualRegistrationEntity createTestActualRegistrationEntity(Long id, String country, String region, String city,
                                                                        String district, String locality, String street,
                                                                        String houseNumber, String houseBlock,
                                                                        String flatNumber, Long index) {
        ActualRegistrationEntity actualRegistrationEntity = new ActualRegistrationEntity();
        actualRegistrationEntity.setId(id);
        actualRegistrationEntity.setCountry(country);
        actualRegistrationEntity.setRegion(region);
        actualRegistrationEntity.setCity(city);
        actualRegistrationEntity.setDistrict(district);
        actualRegistrationEntity.setLocality(locality);
        actualRegistrationEntity.setStreet(street);
        actualRegistrationEntity.setHouseNumber(houseNumber);
        actualRegistrationEntity.setHouseBlock(houseBlock);
        actualRegistrationEntity.setFlatNumber(flatNumber);
        actualRegistrationEntity.setIndex(index);
        return actualRegistrationEntity;
    }

    private ActualRegistrationEntity createTestActualRegistrationEntityPart() {
        ActualRegistrationEntity actualRegistrationEntity = new ActualRegistrationEntity();
        actualRegistrationEntity.setId(1L);
        actualRegistrationEntity.setCountry("country");
        actualRegistrationEntity.setRegion("region");
        actualRegistrationEntity.setCity("city");
        return actualRegistrationEntity;
    }

    private ActualRegistrationDto createTestActualRegistrationDto() {
        ActualRegistrationDto actualRegistrationDto = new ActualRegistrationDto();
        actualRegistrationDto.setId(1L);
        actualRegistrationDto.setCountry("country");
        actualRegistrationDto.setRegion("region");
        actualRegistrationDto.setCity("city");
        actualRegistrationDto.setDistrict("district");
        actualRegistrationDto.setLocality("locality");
        actualRegistrationDto.setStreet("street");
        actualRegistrationDto.setHouseNumber("houseNumber");
        actualRegistrationDto.setHouseBlock("houseBlock");
        actualRegistrationDto.setFlatNumber("flatNumber");
        actualRegistrationDto.setIndex(123L);
        return actualRegistrationDto;
    }

    private ActualRegistrationDto createTestActualRegistrationDtoPart() {
        ActualRegistrationDto actualRegistrationDto = new ActualRegistrationDto();
        actualRegistrationDto.setId(1L);
        actualRegistrationDto.setCountry("country");
        actualRegistrationDto.setRegion("region");
        actualRegistrationDto.setCity("city");
        return actualRegistrationDto;
    }
}