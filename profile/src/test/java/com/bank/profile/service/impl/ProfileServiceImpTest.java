package com.bank.profile.service.impl;

import com.bank.profile.dto.ProfileDto;
import com.bank.profile.entity.ProfileEntity;
import com.bank.profile.mapper.ProfileMapper;
import com.bank.profile.repository.ProfileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImpTest {

    private static final String MESSAGE_FIND_BY_ID = "profile с данным id не найден!";
    private static final String MESSAGE_UPDATE = "Обновление невозможно, profile не найден!";

    @Mock
    private ProfileRepository repository;

    @Mock
    private ProfileMapper mapper;

    @InjectMocks
    private ProfileServiceImp service;

    @Test
    @DisplayName("поиск по существующему id, успешно")
    void findById_IdIsExist_ReturnsValidDto() {
        Long id = 1L;
        ProfileEntity entity = createTestProfileEntity(id);

        ProfileDto dto = createTestProfileDto(id);

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        ProfileDto result = service.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(repository, times(1)).findById(id);
        verify(mapper, times(1)).toDto(entity);
    }

    @Test
    @DisplayName("поиск по несуществующему id, ошибка")
    void findById_IdIsNotExist_ThrowsEntityNotFoundException() {
        Long id = 999L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> service.findById(id));
        assertEquals(MESSAGE_FIND_BY_ID, exception.getMessage());
        verify(repository, times(1)).findById(id);
    }

    @Test
    @DisplayName("сохранение дто, успешно")
    void save_ReturnsValidDto() {
        Long id = 1L;
        ProfileDto dto = createTestProfileDto(id);

        ProfileEntity entity = createTestProfileEntity(id);

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        ProfileDto result = service.save(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        verify(mapper, times(1)).toEntity(dto);
        verify(repository, times(1)).save(entity);
        verify(mapper, times(1)).toDto(entity);
    }

    @Test
    @DisplayName("попытка сохранить null, ошибка")
    void save_NullDto_ThrowsException() {
        when(mapper.toEntity(null)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> service.save(null));
    }

    @Test
    @DisplayName("обновление дто по существующему id, успешно")
    void update_IdIsExist_ReturnsValidDto() {
        Long id = 5L;
        ProfileDto dto = createTestProfileDto(id);

        ProfileEntity existingEntity = createTestProfileEntity(id);

        ProfileEntity updatedEntity = createTestProfileEntity(id);

        when(repository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(mapper.mergeToEntity(dto, existingEntity)).thenReturn(updatedEntity);
        when(repository.save(updatedEntity)).thenReturn(updatedEntity);
        when(mapper.toDto(updatedEntity)).thenReturn(dto);

        ProfileDto result = service.update(id, dto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(repository, times(1)).findById(id);
        verify(mapper, times(1)).mergeToEntity(dto, existingEntity);
        verify(repository, times(1)).save(updatedEntity);
        verify(mapper, times(1)).toDto(updatedEntity);
    }

    @Test
    @DisplayName("обновление дто по несуществующему id, ошибка")
    void update_IdIsNotExist_ThrowsEntityNotFoundException() {
        Long id = 999L;
        ProfileDto dto = createTestProfileDto(id);

        when(repository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> service.update(id, dto));
        assertEquals(MESSAGE_UPDATE, exception.getMessage());
        verify(repository, times(1)).findById(id);
    }

    @Test
    @DisplayName("поиск по существующим id, успешно")
    void findAllById_IdsIsExist_ReturnsValidDtoList() {
        List<Long> ids = List.of(1L, 2L, 3L);

        ProfileEntity entity1 = createTestProfileEntity(1L);

        ProfileEntity entity2 = createTestProfileEntity(2L);

        ProfileEntity entity3 = createTestProfileEntity(3L);

        List<ProfileEntity> entities = List.of(entity1, entity2, entity3);

        ProfileDto dto1 = createTestProfileDto(1L);

        ProfileDto dto2 = createTestProfileDto(2L);

        ProfileDto dto3 = createTestProfileDto(3L);

        List<ProfileDto> dtos = List.of(dto1, dto2, dto3);

        when(repository.findAllById(ids)).thenReturn(entities);
        when(mapper.toDtoList(entities)).thenReturn(dtos);

        List<ProfileDto> result = service.findAllById(ids);

        assertNotNull(result);
        assertEquals(dtos.size(), result.size());
        assertEquals(dtos, result);
        verify(repository, times(1)).findAllById(ids);
        verify(mapper, times(1)).toDtoList(entities);
    }

    @Test
    @DisplayName("поиск по несуществующим id, возвращает пустой список")
    void findAllById_IdsIsNotExist_ReturnsEmptyList() {
        List<Long> ids = List.of(999L, 888L);
        when(repository.findAllById(ids)).thenReturn(Collections.emptyList());
        when(mapper.toDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<ProfileDto> result = service.findAllById(ids);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAllById(ids);
        verify(mapper, times(1)).toDtoList(Collections.emptyList());
    }

    private ProfileEntity createTestProfileEntity(Long id) {
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setId(id);
        return profileEntity;
    }

    private ProfileDto createTestProfileDto(Long id) {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setId(id);
        return profileDto;
    }
}