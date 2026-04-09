package com.bank.profile.service.impl;

import com.bank.profile.dto.PassportDto;
import com.bank.profile.entity.PassportEntity;
import com.bank.profile.mapper.PassportMapper;
import com.bank.profile.repository.PassportRepository;
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
class PassportServiceImpTest {

    private static final String MESSAGE_FIND_BY_ID = "passport с данным id не найден!";
    private static final String MESSAGE_UPDATE = "Обновление невозможно, passport не найден!";

    @Mock
    private PassportRepository repository;

    @Mock
    private PassportMapper mapper;

    @InjectMocks
    private PassportServiceImp service;

    @Test
    @DisplayName("поиск по существующему id, успешно")
    void findById_IdIsExist_ReturnsValidDto() {
        Long id = 1L;
        PassportEntity entity = createTestPassportEntity(id);

        PassportDto dto = createTestPassportDto(id);

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        PassportDto result = service.findById(id);

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
        PassportDto dto = createTestPassportDto(id);

        PassportEntity entity = createTestPassportEntity(id);

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        PassportDto result = service.save(dto);

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
        PassportDto dto = createTestPassportDto(id);

        PassportEntity existingEntity = createTestPassportEntity(id);

        PassportEntity updatedEntity = createTestPassportEntity(id);

        when(repository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(mapper.mergeToEntity(dto, existingEntity)).thenReturn(updatedEntity);
        when(repository.save(updatedEntity)).thenReturn(updatedEntity);
        when(mapper.toDto(updatedEntity)).thenReturn(dto);

        PassportDto result = service.update(id, dto);

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
        PassportDto dto = createTestPassportDto(id);

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

        PassportEntity entity1 = createTestPassportEntity(1L);

        PassportEntity entity2 = createTestPassportEntity(2L);

        PassportEntity entity3 = createTestPassportEntity(3L);

        List<PassportEntity> entities = List.of(entity1, entity2, entity3);

        PassportDto dto1 = createTestPassportDto(1L);

        PassportDto dto2 = createTestPassportDto(2L);

        PassportDto dto3 = createTestPassportDto(3L);

        List<PassportDto> dtos = List.of(dto1, dto2, dto3);

        when(repository.findAllById(ids)).thenReturn(entities);
        when(mapper.toDtoList(entities)).thenReturn(dtos);

        List<PassportDto> result = service.findAllById(ids);

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

        List<PassportDto> result = service.findAllById(ids);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAllById(ids);
        verify(mapper, times(1)).toDtoList(Collections.emptyList());
    }

    private PassportEntity createTestPassportEntity(Long id) {
        PassportEntity passportEntity = new PassportEntity();
        passportEntity.setId(id);
        return passportEntity;
    }

    private PassportDto createTestPassportDto(Long id) {
        PassportDto passportDto = new PassportDto();
        passportDto.setId(id);
        return passportDto;
    }
}