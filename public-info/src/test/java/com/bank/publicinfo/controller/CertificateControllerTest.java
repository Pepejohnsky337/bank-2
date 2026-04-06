package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.CertificateDto;
import com.bank.publicinfo.service.CertificateService;

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

@WebMvcTest(controllers = CertificateController.class)
class CertificateControllerTest {

    @MockBean
    private CertificateService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final List<Long> IDS = List.of(1L, 2L);

    @Test
    @DisplayName("Успешное получение Certificate по ID")
    void readById_Success() throws Exception {
        CertificateDto dto = createDto(1L);
        when(service.findById(1L)).thenReturn(dto);
        mockMvc.perform(get("/certificate/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.photoCertificate").exists())
                .andExpect(jsonPath("$.photoCertificate.length()").value(5));
        verify(service, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Ошибка 404 при получении несуществующего Certificate")
    void shouldExceptionThrownWhenNonExistentIdPassed() throws Exception {
        when(service.findById(1L)).thenThrow(new EntityNotFoundException("Is not found"));
        mockMvc.perform(get("/certificate/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Успешное получение списка Certificate по ID")
    void readAllById_Success() throws Exception {
        List<CertificateDto> dto = List.of(createDto(1L), createDto(2L));
        when(service.findAllById(IDS)).thenReturn(dto);
        mockMvc.perform(get("/certificate/read/all?ids=1&ids=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].photoCertificate").exists())
                .andExpect(jsonPath("$[0].photoCertificate.length()").value(5))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].photoCertificate").exists())
                .andExpect(jsonPath("$[1].photoCertificate.length()").value(5));
        verify(service, times(1)).findAllById(IDS);
    }

    @Test
    @DisplayName("Ошибка 404 при получении несуществующих Certificate")
    void readAllById_NotFound() throws Exception {
        when(service.findAllById(IDS)).thenThrow(new EntityNotFoundException("Is not found"));
        mockMvc.perform(get("/certificate/read/all?ids=1&ids=2"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Успешное создание Certificate")
    void create_Success() throws Exception {
        CertificateDto dto = createDto(1L);
        String certificateDtoJson = objectMapper.writeValueAsString(dto);
        when(service.create(dto)).thenReturn(dto);
        mockMvc.perform(post("/certificate/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(certificateDtoJson))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.photoCertificate").exists())
                .andExpect(jsonPath("$.photoCertificate.length()").value(5))
                .andExpect(status().isOk());
        verify(service, times(1)).create(dto);
    }

    @Test
    @DisplayName("Ошибка 400 при пустом теле запроса")
    void create_NoSuccess() throws Exception {
        mockMvc.perform(post("/certificate/create")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Успешное обновление Certificate")
    void update_Success() throws Exception {
        CertificateDto dto = createDto(1L);
        String certificateDtoJson = objectMapper.writeValueAsString(dto);
        when(service.update(1L, dto)).thenReturn(dto);
        mockMvc.perform(put("/certificate/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(certificateDtoJson))
                .andExpect(jsonPath("$.photoCertificate").exists())
                .andExpect(jsonPath("$.photoCertificate.length()").value(5))
                .andExpect(status().isOk());
        verify(service, times(1)).update(1L, dto);
    }

    @Test
    @DisplayName("Ошибка 400 при пустом теле запроса")
    void update_NoSuccess_EmptyBody() throws Exception {
        mockMvc.perform(put("/certificate/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private CertificateDto createDto(Long id) {
        return CertificateDto.builder()
                .id(id)
                .photoCertificate(new Byte[]{1, 2, 3, 4, 5})
                .build();
    }
}