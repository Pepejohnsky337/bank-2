package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.BranchDto;
import com.bank.publicinfo.entity.BranchEntity;
import com.bank.publicinfo.mapper.BranchMapper;
import com.bank.publicinfo.repository.BranchRepository;
import com.bank.publicinfo.service.impl.BranchServiceImpl;
import com.bank.publicinfo.util.EntityNotFoundSupplier;

import static com.bank.publicinfo.utilTest.TestDataFactory.createBranchDto;
import static com.bank.publicinfo.utilTest.TestDataFactory.createBranchEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BranchServiceImplTest {

    @InjectMocks
    private BranchServiceImpl branchService;

    @Mock
    private BranchRepository repository;

    @Mock
    private BranchMapper mapper;

    @Mock
    private EntityNotFoundSupplier supplierNotFound;

    private final static String MESSAGE = "Информации об отделении не найдено с id ";

    @Test
    @DisplayName("Успешное получение списка отделений по списку ID")
    void shouldFindAllBranchesByIds() {
        List<Long> ids = List.of(1L, 2L);
        List<BranchEntity> branchEntity = List.of(createBranchEntity(1L, "Москва"), createBranchEntity(2L, "Москва"));
        List<BranchDto> branchDto = List.of(createBranchDto(1L, "Москва"), createBranchDto(2L, "Москва"));

        when(repository.findAllById(ids)).thenReturn(branchEntity);
        when(mapper.toDtoList(branchEntity)).thenReturn(branchDto);

        doNothing().when(supplierNotFound).checkForSizeAndLogging(anyString(), eq(ids), eq(branchEntity));
        List<BranchDto> allById = branchService.findAllById(ids);

        assertThat(allById).isEqualTo(branchDto);

        verify(repository, times(1)).findAllById(ids);
        verify(mapper, times(1)).toDtoList(branchEntity);
        verify(supplierNotFound, times(1)).checkForSizeAndLogging(anyString(), eq(ids), eq(branchEntity));
    }

    @Test
    @DisplayName("Ошибка при получении списка отделений по несуществующим ID")
    void shouldThrowEntityNotFoundException_whenAllIdsNotFound() {
        List<Long> ids = List.of(1L, 2L);
        List<BranchEntity> branchEntity = List.of();

        when(repository.findAllById(ids)).thenReturn(branchEntity);
        doThrow(new EntityNotFoundException(MESSAGE + ids))
                .when(supplierNotFound)
                .checkForSizeAndLogging(ArgumentMatchers.anyString(), eq(ids), eq(branchEntity));

        assertThatThrownBy(() -> branchService.findAllById(ids)).isInstanceOf(EntityNotFoundException.class);

        verify(repository, times(1)).findAllById(ids);
        verify(supplierNotFound, times(1)).checkForSizeAndLogging(anyString(), eq(ids), eq(branchEntity));
    }

    @Test
    @DisplayName("Успешное создание отделения")
    void shouldCreateEntity() {
        BranchEntity branchEntity = createBranchEntity(1L, "Москва");
        BranchDto branchDto = createBranchDto(1L, "Москва");

        when(repository.save(branchEntity)).thenReturn(branchEntity);
        when(mapper.toEntity(branchDto)).thenReturn(branchEntity);
        when(mapper.toDto(branchEntity)).thenReturn(branchDto);
        BranchDto returnDto = branchService.create(branchDto);

        assertThat(branchDto).isEqualTo(returnDto);

        verify(mapper, times(1)).toEntity(branchDto);
        verify(repository, times(1)).save(branchEntity);
        verify(mapper, times(1)).toDto(branchEntity);
    }

    @Test
    @DisplayName("Ошибка при создании отделения")
    void shouldThrowExceptionWhenSaveFails() {
        BranchEntity branchEntity = createBranchEntity(1L, "Москва");
        BranchDto branchDto = createBranchDto(1L, "Москва");

        when(mapper.toEntity(branchDto)).thenReturn(branchEntity);
        when(repository.save(branchEntity)).thenThrow(new NullPointerException());

        assertThatThrownBy(() -> branchService.create(branchDto)).isInstanceOf(NullPointerException.class);

        verify(mapper, times(1)).toEntity(branchDto);
        verify(repository, times(1)).save(branchEntity);
    }


    @Test
    @DisplayName("Успешное обновление отделения")
    void shouldUpdateBranchSuccessfully() {
        Long id = 1L;
        BranchEntity existingEntity = createBranchEntity(1L, "Москва");
        BranchDto updateDto = createBranchDto(1L, "Санкт-Петербург");
        BranchEntity updatedEntity = createBranchEntity(1L, "Санкт-Петербург");
        BranchDto updatedDto = createBranchDto(1L, "Санкт-Петербург");

        when(repository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(mapper.mergeToEntity(updateDto, existingEntity)).thenReturn(updatedEntity);
        when(mapper.toDto(updatedEntity)).thenReturn(updatedDto);
        BranchDto result = branchService.update(id, updateDto);

        assertThat(result).isEqualTo(updatedDto);
        assertThat(result.getCity()).isEqualTo("Санкт-Петербург");

        verify(repository, times(1)).findById(id);
        verify(mapper, times(1)).mergeToEntity(updateDto, existingEntity);
        verify(mapper, times(1)).toDto(updatedEntity);
    }

    @Test
    @DisplayName("Ошибка при обновлении несуществующего отделения")
    void shouldThrowExceptionWhenBranchNotFound() {
        Long id = 999L;
        BranchDto branchDto = createBranchDto(1L, "Москва");

        when(repository.findById(id)).thenReturn(Optional.empty());
        when(supplierNotFound.getException(MESSAGE, id))
                .thenReturn(new EntityNotFoundException(MESSAGE + id));
        assertThatThrownBy(() -> branchService.update(id, branchDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(MESSAGE);

        verify(repository, times(1)).findById(id);
        verify(supplierNotFound, times(1)).getException(MESSAGE, id);
        verify(mapper, never()).mergeToEntity(any(), any());
    }

    @Test
    @DisplayName("Успешное получение отделения по ID")
    void shouldFindBranchById() {
        Long id = 1L;
        BranchEntity entity = createBranchEntity(1L, "Москва");
        BranchDto dto = createBranchDto(id, "Москва");

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);
        BranchDto byId = branchService.findById(id);

        assertThat(byId).isEqualTo(dto);

        verify(repository, times(1)).findById(id);
        verify(mapper, times(1)).toDto(entity);
    }

    @Test
    @DisplayName("Ошибка при получении несуществующего отделения")
    void shouldThrowEntityNotFoundExceptionWhenBranchNotFound() {
        Long id = 999L;

        when(repository.findById(id)).thenReturn(Optional.empty());
        when(supplierNotFound.getException(MESSAGE, id)).thenReturn(new EntityNotFoundException(MESSAGE + id));

        assertThatThrownBy(() -> branchService.findById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(MESSAGE);

        verify(repository, times(1)).findById(id);
        verify(supplierNotFound, times(1)).getException(MESSAGE, id);
    }
}
