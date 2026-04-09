package com.bank.profile.service.impl;

import com.bank.profile.dto.AccountDetailsIdDto;
import com.bank.profile.entity.AccountDetailsIdEntity;
import com.bank.profile.mapper.AccountDetailsIdMapper;
import com.bank.profile.repository.AccountDetailsIdRepository;
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
class AccountDetailsIdServiceImpTest {

    private static final String MESSAGE_FIND_BY_ID = "accountDetailsId с данным id не найден!";
    private static final String MESSAGE_UPDATE = "Обновление невозможно, accountDetailsId не найден!";

    @Mock
    private AccountDetailsIdRepository repository;

    @Mock
    private AccountDetailsIdMapper mapper;

    @InjectMocks
    private AccountDetailsIdServiceImp service;

    @Test
    @DisplayName("поиск по существующему id, успешно")
    void findById_IdIsExist_ReturnsValidDto() {
        Long id = 1L;
        AccountDetailsIdEntity entity = createTestAccountDetailsIdEntity(id, null);
        AccountDetailsIdDto dto = createTestAccountDetailsIdDto(id, null);

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        AccountDetailsIdDto result = service.findById(id);

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
        AccountDetailsIdDto dto = createTestAccountDetailsIdDto(1L, 100L);

        AccountDetailsIdEntity entity = createTestAccountDetailsIdEntity(1L, 100L);

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        AccountDetailsIdDto result = service.save(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getAccountId(), result.getAccountId());
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
        AccountDetailsIdDto dto = createTestAccountDetailsIdDto(id, 200L);

        AccountDetailsIdEntity existingEntity = createTestAccountDetailsIdEntity(id, 100L);

        AccountDetailsIdEntity updatedEntity = createTestAccountDetailsIdEntity(id, 200L);

        when(repository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(mapper.mergeToEntity(dto, existingEntity)).thenReturn(updatedEntity);
        when(repository.save(updatedEntity)).thenReturn(updatedEntity);
        when(mapper.toDto(updatedEntity)).thenReturn(dto);

        AccountDetailsIdDto result = service.update(id, dto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(200L, result.getAccountId());
        verify(repository, times(1)).findById(id);
        verify(mapper, times(1)).mergeToEntity(dto, existingEntity);
        verify(repository, times(1)).save(updatedEntity);
        verify(mapper, times(1)).toDto(updatedEntity);
    }

    @Test
    @DisplayName("обновление дто по несуществующему id, ошибка")
    void update_IdIsNotExist_ThrowsEntityNotFoundException() {
        Long id = 999L;
        AccountDetailsIdDto dto = createTestAccountDetailsIdDto(id, null);

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

        AccountDetailsIdEntity entity1 = createTestAccountDetailsIdEntity(1L, 100L);
        AccountDetailsIdEntity entity2 = createTestAccountDetailsIdEntity(2L, 200L);
        AccountDetailsIdEntity entity3 = createTestAccountDetailsIdEntity(3L, 300L);

        List<AccountDetailsIdEntity> entities = List.of(entity1, entity2, entity3);

        AccountDetailsIdDto dto1 = createTestAccountDetailsIdDto(1L, 100L);
        AccountDetailsIdDto dto2 = createTestAccountDetailsIdDto(2L, 200L);
        AccountDetailsIdDto dto3 = createTestAccountDetailsIdDto(3L, 300L);

        List<AccountDetailsIdDto> dtos = List.of(dto1, dto2, dto3);

        when(repository.findAllById(ids)).thenReturn(entities);
        when(mapper.toDtoList(entities)).thenReturn(dtos);

        List<AccountDetailsIdDto> result = service.findAllById(ids);

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

        List<AccountDetailsIdDto> result = service.findAllById(ids);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAllById(ids);
        verify(mapper, times(1)).toDtoList(Collections.emptyList());
    }

    private AccountDetailsIdEntity createTestAccountDetailsIdEntity(Long id, Long accountId) {
        AccountDetailsIdEntity entity = new AccountDetailsIdEntity();
        entity.setId(id);
        entity.setAccountId(accountId);
        return entity;
    }

    private AccountDetailsIdDto createTestAccountDetailsIdDto(Long id, Long accountId) {
        AccountDetailsIdDto dto = new AccountDetailsIdDto();
        dto.setId(id);
        dto.setAccountId(accountId);
        return dto;
    }
}