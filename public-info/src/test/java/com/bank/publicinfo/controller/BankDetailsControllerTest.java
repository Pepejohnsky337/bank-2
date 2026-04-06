package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.service.BankDetailsService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.EntityNotFoundException;

import java.util.List;

@WebMvcTest(controllers = BankDetailsController.class)
class BankDetailsControllerTest {

    @MockBean
    private BankDetailsService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Успешное получение BankDetails по ID")
    void readById_Success() throws Exception {
        BankDetailsDto dto = createDto(1L, "Сбербанк");
        when(service.findById(1L)).thenReturn(dto);
        mockMvc.perform(get("/bank/details/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Сбербанк"))
                .andExpect(jsonPath("$.city").value("Москва"));
        verify(service, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Ошибка 404 при получении несуществующего BankDetails")
    void shouldExceptionThrownWhenNonExistentIdPassed() throws Exception {
        when(service.findById(1L)).thenThrow(new EntityNotFoundException("Is not found"));
        mockMvc.perform(get("/bank/details{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Успешное получение списка BankDetails по ID")
    void readAllById_Success() throws Exception {
        List<Long> ids = List.of(1L, 2L);
        List<BankDetailsDto> dto = List.of(createDto(1L, "Сбербанк"), createDto(2L, "Россельхозбанк"));
        when(service.findAllById(ids)).thenReturn(dto);
        mockMvc.perform(get("/bank/details/read/all?ids=1&ids=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Сбербанк"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Россельхозбанк"));
        verify(service, times(1)).findAllById(ids);
    }

    @Test
    @DisplayName("Ошибка 404 при получении несуществующих BankDetails")
    void readAllById_NotFound() throws Exception {
        List<Long> ids = List.of(1L, 2L);
        when(service.findAllById(ids)).thenThrow(new EntityNotFoundException("Is not found"));
        mockMvc.perform(get("/bank/details/read/all?ids=1&ids=2"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Успешное создание BankDetails")
    void create_Success() throws Exception {
        BankDetailsDto dto = createDto(1L, "Сбербанк");
        String bankDetailsDtoJson = objectMapper.writeValueAsString(dto);
        when(service.create(dto)).thenReturn(dto);
        mockMvc.perform(post("/bank/details/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bankDetailsDtoJson))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Сбербанк"))
                .andExpect(status().isOk());
        verify(service, times(1)).create(dto);
    }

    @Test
    @DisplayName("Ошибка 400 при пустом теле запроса")
    void create_NoSuccess() throws Exception {
        mockMvc.perform(post("/bank/details/create")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Успешное обновление BankDetails")
    void update_Success() throws Exception {
        BankDetailsDto dto = createDto(1L, "Сбербанк");
        String bankDetailsDtoJson = objectMapper.writeValueAsString(dto);
        when(service.update(1L, dto)).thenReturn(dto);
        mockMvc.perform(put("/bank/details/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bankDetailsDtoJson))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Сбербанк"))
                .andExpect(status().isOk());
        verify(service, times(1)).update(1L, dto);
    }

    @Test
    @DisplayName("Ошибка 400 при пустом теле запроса")
    void update_NoSuccess_EmptyBody() throws Exception {
        mockMvc.perform(put("/bank/details/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private BankDetailsDto createDto(Long id, String name) {
        return BankDetailsDto.builder()
                .id(id)
                .name(name)
                .city("Москва")
                .build();
    }
}


