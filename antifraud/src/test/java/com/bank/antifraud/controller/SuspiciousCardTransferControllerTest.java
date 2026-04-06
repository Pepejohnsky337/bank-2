package com.bank.antifraud.controller;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import com.bank.antifraud.dto.SuspiciousCardTransferDto;
import com.bank.antifraud.service.SuspiciousCardTransferService;
import com.bank.antifraud.util.builder.SuspiciousCardTransferBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(SuspiciousCardTransferController.class)
@DisplayName("Тесты для контроллера подозрительных переводов по карте")
class SuspiciousCardTransferControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private SuspiciousCardTransferService service;
    
    private final String BASE_ENDPOINT = "/suspicious/card/transfer";
    private final String URL_READ = BASE_ENDPOINT + "/{id}";
    private final String URL_READ_ALL = BASE_ENDPOINT;
    private final String URL_CREATE = BASE_ENDPOINT + "/create";
    private final String URL_UPDATE = BASE_ENDPOINT + "/{id}";
    
    private final Long TRANSFER_ID_FIRST = 1L;
    private final Long TRANSFER_ID_SECOND = 2L;
    private final Long CARD_TRANSFER_ID_FIRST = 11111L;
    private final String INVALID_JSON_DTO = "{ invalid }";
    
    @Test
    @DisplayName("Должен вернуть подозрительный перевод по счету по id")
    void read_WhenExists_ThenReturnOk() throws Exception {
        SuspiciousCardTransferBuilder builder = new SuspiciousCardTransferBuilder();
        SuspiciousCardTransferDto expectedDto = builder
            .id(TRANSFER_ID_FIRST)
            .cardTransferId(CARD_TRANSFER_ID_FIRST)
            .buildDto();
        
        when(service.findById(TRANSFER_ID_FIRST)).thenReturn(expectedDto);
        
        mockMvc.perform(
                get(URL_READ, TRANSFER_ID_FIRST)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(TRANSFER_ID_FIRST))
            .andExpect(jsonPath("$.cardTransferId").value(CARD_TRANSFER_ID_FIRST));
        
        verify(service).findById(TRANSFER_ID_FIRST);
    }
    
    @Test
    @DisplayName("Должен вернуть 404, если подозрительный перевод не найден")
    void read_WhenNotExists_ThenReturnNotFound() throws Exception {
        when(service.findById(TRANSFER_ID_FIRST)).thenThrow(new EntityNotFoundException());
        
        mockMvc.perform(
                get(URL_READ, TRANSFER_ID_FIRST)
            )
            .andExpect(status().isNotFound());
        
        verify(service).findById(TRANSFER_ID_FIRST);
    }
    
    @Test
    @DisplayName("Должен вернуть список подозрительных переводов по списку id")
    void readAll_WhenIdsExist_ThenReturnOk() throws Exception {
        List<Long> ids = List.of(TRANSFER_ID_FIRST, TRANSFER_ID_SECOND);
        String[] idsArray = ids.stream().map(String::valueOf).toArray(String[]::new);
        SuspiciousCardTransferBuilder builder = new SuspiciousCardTransferBuilder();
        SuspiciousCardTransferDto dto1 = builder.id(TRANSFER_ID_FIRST).buildDto();
        SuspiciousCardTransferDto dto2 = builder.id(TRANSFER_ID_SECOND).buildDto();
        List<SuspiciousCardTransferDto> expectedDtos = List.of(dto1, dto2);
        
        when(service.findAllById(ids)).thenReturn(expectedDtos);
        
        mockMvc.perform(get(URL_READ_ALL).param("ids", idsArray))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].id").value(TRANSFER_ID_FIRST))
            .andExpect(jsonPath("$[1].id").value(TRANSFER_ID_SECOND))
            .andReturn();
        
        verify(service).findAllById(ids);
    }
    
    @Test
    @DisplayName("Должен вернуть 404, если подозрительные переводы не найдены")
    void readAll_WhenTransfersNotExists_ThenReturnNotFound() throws Exception {
        List<Long> ids = List.of(TRANSFER_ID_FIRST, TRANSFER_ID_SECOND);
        String[] idsArray = ids.stream().map(String::valueOf).toArray(String[]::new);
        
        when(service.findAllById(ids)).thenThrow(new EntityNotFoundException());
        
        mockMvc.perform(
                get(URL_READ_ALL)
                    .param("ids", idsArray)
            )
            .andExpect(status().isNotFound());
        
        verify(service).findAllById(ids);
    }
    
    @Test
    @DisplayName("Должен создать подозрительный перевод и вернуть 200")
    void create_WhenValidData_ThenReturnOk() throws Exception {
        SuspiciousCardTransferBuilder builder = new SuspiciousCardTransferBuilder();
        SuspiciousCardTransferDto transferCreateDto = builder.cardTransferId(CARD_TRANSFER_ID_FIRST).buildDto();
        SuspiciousCardTransferDto savedDto = builder.id(TRANSFER_ID_FIRST).buildDto();
        
        when(service.save(transferCreateDto)).thenReturn(savedDto);
        
        mockMvc.perform(
                post(URL_CREATE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(transferCreateDto))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(TRANSFER_ID_FIRST))
            .andExpect(jsonPath("$.cardTransferId").value(CARD_TRANSFER_ID_FIRST));
        
        verify(service).save(transferCreateDto);
    }
    
    @Test
    @DisplayName("Должен вернуть 400, если тип данных неверный")
    void create_WhenInvalidType_ThenReturnBadRequest() throws Exception {
        mockMvc.perform(
                post(URL_CREATE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(INVALID_JSON_DTO)
            )
            .andExpect(status().isBadRequest());
        
        verify(service, never()).save(any());
    }
    
    @Test
    @DisplayName("Должен обновить подозрительный перевод и вернуть 200")
    void update_WhenValidData_ThenReturnOk() throws Exception {
        final String newSuspiciousReason = "Подозрительные переводы круглыми суммами";
        SuspiciousCardTransferBuilder builder = new SuspiciousCardTransferBuilder();
        SuspiciousCardTransferDto transferUpdateDto = builder
            .suspicious(true, newSuspiciousReason)
            .buildDto();
        SuspiciousCardTransferDto updatedDto = builder.id(TRANSFER_ID_FIRST).buildDto();
        
        when(service.update(TRANSFER_ID_FIRST, transferUpdateDto)).thenReturn(updatedDto);
        
        mockMvc.perform(
                put(URL_UPDATE, TRANSFER_ID_FIRST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(transferUpdateDto))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(TRANSFER_ID_FIRST));
        
        verify(service).update(TRANSFER_ID_FIRST, transferUpdateDto);
    }
    
    @Test
    @DisplayName("Должен вернуть 400, если тип данных неверный")
    void update_WhenInvalidType_ThenReturnBadRequest() throws Exception {
        mockMvc.perform(
                put(URL_UPDATE, TRANSFER_ID_FIRST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(INVALID_JSON_DTO)
            )
            .andExpect(status().isBadRequest());
        
        verify(service, never()).update(anyLong(), any(SuspiciousCardTransferDto.class));
    }
}