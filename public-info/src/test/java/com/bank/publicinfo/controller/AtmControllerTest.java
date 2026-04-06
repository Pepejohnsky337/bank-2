package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.AtmDto;
import com.bank.publicinfo.service.AtmService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;

import java.time.LocalTime;
import java.util.List;

@WebMvcTest(controllers = AtmController.class)
class AtmControllerTest {

    @MockBean
    private AtmService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Успешное получение ATM по ID")
    void readById_Success() throws Exception {
        AtmDto atmDto = createDto(1L, "address");
        when(service.findById(1L)).thenReturn(atmDto);
        mockMvc.perform(get("/atm/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.address").value("address"))
                .andExpect(jsonPath("$.startOfWork").value("09:00:00"))
                .andExpect(jsonPath("$.endOfWork").value("23:00:00"))
                .andExpect(jsonPath("$.allHours").value(true));
    }

    @Test
    @DisplayName("Ошибка 404 при получении несуществующего ATM")
    void shouldExceptionThrownWhenNonExistentIdPassed() throws Exception {
        when(service.findById(1L)).thenThrow(new EntityNotFoundException("Is not found"));
        mockMvc.perform(get("/atm/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Успешное получение списка ATM по ID")
    void readAllById_Success() throws Exception {
        List<Long> ids = List.of(1L, 2L);
        List<AtmDto> atmDto = List.of(createDto(1L, "address 1"), createDto(2L, "address 2"));
        when(service.findAllById(ids)).thenReturn(atmDto);
        mockMvc.perform(get("/atm/read/all?ids=1&ids=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].address").value("address 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].address").value("address 2"));
        verify(service, times(1)).findAllById(ids);
    }

    @Test
    @DisplayName("Ошибка 404 при получении несуществующих ATM")
    void readAllById_NotFound() throws Exception {
        List<Long> ids = List.of(1L, 2L);
        when(service.findAllById(ids)).thenThrow(new EntityNotFoundException("Is not found"));
        mockMvc.perform(get("/atm/read/all?ids=1&ids=2"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Успешное создание ATM")
    void create_Success() throws Exception {
        AtmDto atmDto = createDto(1L, "address 1");
        String AtmDtoJson = objectMapper.writeValueAsString(atmDto);
        when(service.create(atmDto)).thenReturn(atmDto);
        mockMvc.perform(post("/atm/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AtmDtoJson))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.address").value("address 1"))
                .andExpect(status().isOk());
        verify(service, times(1)).create(atmDto);
    }

    @Test
    @DisplayName("Ошибка 400 при пустом теле запроса")
    void create_NoSuccess() throws Exception {
        mockMvc.perform(post("/atm/create")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Успешное обновление ATM")
    void update_Success() throws Exception {
        AtmDto atmDto = createDto(1L, "address 1");
        String atmDtoJson = objectMapper.writeValueAsString(atmDto);
        when(service.update(1L, atmDto)).thenReturn(atmDto);
        mockMvc.perform(put("/atm/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(atmDtoJson))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.address").value("address 1"))
                .andExpect(status().isOk());
        verify(service, times(1)).update(1L, atmDto);
    }

    @Test
    @DisplayName("Ошибка 400 при пустом теле запроса")
    void update_NoSuccess_EmptyBody() throws Exception {
        mockMvc.perform(put("/atm/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private AtmDto createDto(Long id, String address) {
        return AtmDto.builder()
                .id(id)
                .address(address)
                .startOfWork(LocalTime.of(9, 0))
                .endOfWork(LocalTime.of(23, 0))
                .allHours(true)
                .build();
    }
}
