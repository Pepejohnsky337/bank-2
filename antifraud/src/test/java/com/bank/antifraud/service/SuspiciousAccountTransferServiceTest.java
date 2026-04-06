package com.bank.antifraud.service;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import com.bank.antifraud.dto.SuspiciousAccountTransferDto;
import com.bank.antifraud.entity.SuspiciousAccountTransferEntity;
import com.bank.antifraud.mappers.SuspiciousAccountTransferMapper;
import com.bank.antifraud.repository.SuspiciousAccountTransferRepository;
import com.bank.antifraud.service.common.ExceptionReturner;
import com.bank.antifraud.service.impl.SuspiciousAccountTransferServiceImpl;
import com.bank.antifraud.util.builder.SuspiciousAccountTransferBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyString;

@ExtendWith(MockitoExtension.class)
class SuspiciousAccountTransferServiceTest {
    
    @Mock
    private SuspiciousAccountTransferRepository repository;
    @Mock
    private SuspiciousAccountTransferMapper mapper;
    @Mock
    private ExceptionReturner returner;
    @InjectMocks
    private SuspiciousAccountTransferServiceImpl service;
    
    private final Long TRANSFER_ID_FIRST = 1L;
    private final Long TRANSFER_ID_SECOND = 2L;
    
    @Test
    @DisplayName("Если request dto валидное, то возвращается response dto с сгенерированным id")
    void save_WhenDtoValid_ThenReturnDtoWithId() {
        SuspiciousAccountTransferBuilder builder = new SuspiciousAccountTransferBuilder();
        SuspiciousAccountTransferDto saveRequestDto = builder.buildDto();
        SuspiciousAccountTransferEntity entityBeforeSave = builder.buildEntity();
        SuspiciousAccountTransferDto saveResponseDto = builder.id(TRANSFER_ID_FIRST).buildDto();
        
        when(mapper.toEntity(saveRequestDto)).thenReturn(entityBeforeSave);
        when(repository.save(any())).thenAnswer(inv -> {
            SuspiciousAccountTransferEntity entity = inv.getArgument(0);
            entity.setId(TRANSFER_ID_FIRST);
            return entity;
        });
        when(mapper.toDto(any())).thenReturn(saveResponseDto);
        
        SuspiciousAccountTransferDto actualDto = service.save(saveRequestDto);
        
        assertThat(actualDto)
            .isNotNull()
            .hasFieldOrPropertyWithValue("id", TRANSFER_ID_FIRST);
        
        verify(mapper).toEntity(any(SuspiciousAccountTransferDto.class));
        verify(repository).save(any(SuspiciousAccountTransferEntity.class));
        verify(mapper).toDto(any(SuspiciousAccountTransferEntity.class));
    }
    
    @Test
    @DisplayName("Если dto null, то выбрасывается RuntimeException от репозитория (IllegalStateException или NPE)")
    void save_WhenDtoNull_ThenThrowRuntimeException() {
        when(mapper.toEntity(null)).thenReturn(null);
        when(repository.save(null)).thenThrow(new RuntimeException());
        
        assertThrows(RuntimeException.class, () -> service.save(null));
        
        verify(mapper).toEntity(null);
        verify(repository).save(null);
    }
    
    @Test
    @DisplayName("Если entity существует, то возвращается dto с id")
    void findById_WhenEntityExists_ThenReturnDtoWithId() {
        SuspiciousAccountTransferBuilder builder = new SuspiciousAccountTransferBuilder();
        SuspiciousAccountTransferEntity entity = builder.id(TRANSFER_ID_FIRST).buildEntity();
        SuspiciousAccountTransferDto responseDto = builder.buildDto();
        
        when(repository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(mapper.toDto(any(SuspiciousAccountTransferEntity.class))).thenReturn(responseDto);
        
        SuspiciousAccountTransferDto actualDto = service.findById(TRANSFER_ID_FIRST);
        
        assertThat(actualDto)
            .isNotNull()
            .hasFieldOrPropertyWithValue("id", TRANSFER_ID_FIRST);
        
        verify(repository).findById(TRANSFER_ID_FIRST);
        verify(mapper).toDto(any(SuspiciousAccountTransferEntity.class));
    }
    
    @Test
    @DisplayName("Если entity не существует, то выбрасывается EntityNotFoundException")
    void findById_WhenEntityNotExists_ThenThrowEntityNotFoundException() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        when(returner.getEntityNotFoundException(anyString())).thenReturn(new EntityNotFoundException());
        
        assertThrows(EntityNotFoundException.class, () -> service.findById(TRANSFER_ID_FIRST));
    }
    
    @Test
    @DisplayName("Если id и request dto валидны, то возвращается валидное response dto")
    void update_WhenIdAndDtoAreValid_ThenReturnValidResponseDto() {
        final String newSuspiciousReason = "Частые переводы дробными суммами";
        SuspiciousAccountTransferBuilder builder = new SuspiciousAccountTransferBuilder();
        
        SuspiciousAccountTransferDto updateRequestDto = builder
                                                               .suspicious(true, newSuspiciousReason)
                                                               .buildDto();
        SuspiciousAccountTransferDto updateResponseDto = builder.buildDto();
        SuspiciousAccountTransferEntity validEntity = builder.reset().id(TRANSFER_ID_FIRST).buildEntity();
        
        when(repository.findById(anyLong())).thenReturn(Optional.of(validEntity));
        when(mapper.mergeToEntity(any(), any())).thenAnswer(inv -> {
            SuspiciousAccountTransferEntity entity = inv.getArgument(1);
            entity.setSuspiciousReason(newSuspiciousReason);
            return entity;
        });
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toDto(any())).thenReturn(updateResponseDto);
        
        SuspiciousAccountTransferDto actualDto = service.update(TRANSFER_ID_FIRST, updateRequestDto);
        
        assertThat(actualDto)
            .isNotNull()
            .hasFieldOrPropertyWithValue("suspiciousReason", newSuspiciousReason);
        
        verify(repository).findById(TRANSFER_ID_FIRST);
        verify(mapper)
            .mergeToEntity(any(SuspiciousAccountTransferDto.class), any(SuspiciousAccountTransferEntity.class));
        verify(repository).save(any(SuspiciousAccountTransferEntity.class));
        verify(mapper).toDto(any(SuspiciousAccountTransferEntity.class));
    }
    
    @Test
    @DisplayName("Если id null, то выбрасывается EntityNotFoundException")
    void update_WhenIdNull_ThenThrowEntityNotFoundException() {
        SuspiciousAccountTransferDto validDto = new SuspiciousAccountTransferBuilder()
                                                                                      .id(TRANSFER_ID_FIRST)
                                                                                      .buildDto();

        when(repository.findById(null)).thenReturn(Optional.empty());
        when(returner.getEntityNotFoundException(anyString())).thenReturn(new EntityNotFoundException());
        
        assertThrows(EntityNotFoundException.class, () -> service.update(null, validDto));
    }
    
    @Test
    @DisplayName("Если ids не пустой список, то возвращается не пустой список")
    void findAllById_WhenIdsNotEmpty_ShouldReturnNotEmptyDtos() {
        List<Long> ids = List.of(TRANSFER_ID_FIRST, TRANSFER_ID_SECOND);
        SuspiciousAccountTransferBuilder builder = new SuspiciousAccountTransferBuilder();
        SuspiciousAccountTransferEntity entity1 = builder.id(TRANSFER_ID_FIRST).buildEntity();
        SuspiciousAccountTransferDto dto1 = builder.buildDto();
        SuspiciousAccountTransferEntity entity2 = builder.id(TRANSFER_ID_SECOND).buildEntity();
        SuspiciousAccountTransferDto dto2 = builder.buildDto();
        List<SuspiciousAccountTransferDto> dtos = List.of(dto1, dto2);
        
        when(repository.findById(TRANSFER_ID_FIRST)).thenReturn(Optional.of(entity1));
        when(repository.findById(TRANSFER_ID_SECOND)).thenReturn(Optional.of(entity2));
        when(mapper.toListDto(anyList())).thenReturn(dtos);
        
        List<SuspiciousAccountTransferDto> actualDtos = service.findAllById(ids);
        
        assertThat(actualDtos)
            .isNotNull()
            .isNotEmpty()
            .hasSize(2)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrder(dto1, dto2);
        
        verify(repository).findById(TRANSFER_ID_FIRST);
        verify(repository).findById(TRANSFER_ID_SECOND);
        verify(mapper).toListDto(anyList());
    }
    
    @Test
    @DisplayName("Если ids null, то выбрасывается nullPointerException")
    void findAllById_WhenIdsNull_ThenThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.findAllById(null));
    }
}