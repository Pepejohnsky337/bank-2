package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.LicenseDto;
import com.bank.publicinfo.service.LicenseService;

import static com.bank.publicinfo.utilTest.TestDataFactory.createLicenseDto;
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

@WebMvcTest(controllers = LicenseController.class)
class LicenseControllerTest {

    @MockBean
    private LicenseService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final List<Long> IDS = List.of(1L, 2L);
    private static final LicenseDto DTO = createLicenseDto(1L);

    @Test
    @DisplayName("Успешное получение License по ID")
    void readById_Success() throws Exception {
        LicenseDto dto = createLicenseDto(1L);
        when(service.findById(1L)).thenReturn(dto);
        mockMvc.perform(get("/license/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
        verify(service, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Ошибка 404 при получении несуществующей License")
    void shouldExceptionThrownWhenNonExistentIdPassed() throws Exception {
        when(service.findById(1L)).thenThrow(new EntityNotFoundException("Is not found"));
        mockMvc.perform(get("/license/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Успешное получение списка License по ID")
    void readAllById_Success() throws Exception {
        List<LicenseDto> dto = List.of(createLicenseDto(1L), createLicenseDto(2L));
        when(service.findAllById(IDS)).thenReturn(dto);
        mockMvc.perform(get("/license/read/all?ids=1&ids=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].photoLicense").exists())
                .andExpect(jsonPath("$[0].photoLicense.length()").value(5))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].photoLicense.length()").value(5));
        verify(service, times(1)).findAllById(IDS);
    }

    @Test
    @DisplayName("Ошибка 404 при получении несуществующих License")
    void readAllById_NotFound() throws Exception {
        when(service.findAllById(IDS)).thenThrow(new EntityNotFoundException("Is not found"));
        mockMvc.perform(get("/license/read/all?ids=1&ids=2"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Успешное создание License")
    void create_Success() throws Exception {
        LicenseDto dto = createLicenseDto(1L);
        String licenseDtoJson = objectMapper.writeValueAsString(dto);
        when(service.create(dto)).thenReturn(dto);
        mockMvc.perform(post("/license/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(licenseDtoJson))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.photoLicense").exists())
                .andExpect(jsonPath("$.photoLicense.length()").value(5))
                .andExpect(status().isOk());
        verify(service, times(1)).create(dto);
    }

    @Test
    @DisplayName("Ошибка 400 при пустом теле запроса")
    void create_NoSuccess() throws Exception {
        mockMvc.perform(post("/license/create")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Успешное обновление License")
    void update_Success() throws Exception {
        LicenseDto dto = createLicenseDto(1L);
        String licenseDtoJson = objectMapper.writeValueAsString(dto);
        when(service.update(1L, dto)).thenReturn(dto);
        mockMvc.perform(put("/license/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(licenseDtoJson))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.photoLicense").exists())
                .andExpect(jsonPath("$.photoLicense.length()").value(5))
                .andExpect(status().isOk());
        verify(service, times(1)).update(1L, dto);
    }

    @Test
    @DisplayName("Ошибка 400 при пустом теле запроса")
    void update_NoSuccess_EmptyBody() throws Exception {
        mockMvc.perform(put("/license/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}