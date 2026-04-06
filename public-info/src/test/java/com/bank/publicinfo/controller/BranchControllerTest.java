package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.BranchDto;
import com.bank.publicinfo.service.BranchService;

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

@WebMvcTest(controllers = BranchController.class)
class BranchControllerTest {

    @MockBean
    private BranchService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final List<Long> IDS = List.of(1L, 2L);

    @Test
    @DisplayName("Успешное получение Branch по ID")
    void readById_Success() throws Exception {
        BranchDto dto = createDto(1L, "Москва");
        when(service.findById(1L)).thenReturn(dto);
        mockMvc.perform(get("/branch/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.city").value("Москва"));
        verify(service, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Ошибка 404 при получении несуществующего Branch")
    void shouldExceptionThrownWhenNonExistentIdPassed() throws Exception {
        when(service.findById(1L)).thenThrow(new EntityNotFoundException("Is not found"));
        mockMvc.perform(get("/branch/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Успешное получение списка Branch по ID")
    void readAllById_Success() throws Exception {
        List<BranchDto> dto = List.of(createDto(1L, "Москва"), createDto(2L, "Санкт-Петербург"));
        when(service.findAllById(IDS)).thenReturn(dto);
        mockMvc.perform(get("/branch/read/all?ids=1&ids=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].city").value("Москва"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].city").value("Санкт-Петербург"));
        verify(service, times(1)).findAllById(IDS);
    }

    @Test
    @DisplayName("Ошибка 404 при получении несуществующих Branch")
    void readAllById_NotFound() throws Exception {
        when(service.findAllById(IDS)).thenThrow(new EntityNotFoundException("Is not found"));
        mockMvc.perform(get("/branch/read/all?ids=1&ids=2"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Успешное создание Branch")
    void create_Success() throws Exception {
        BranchDto dto = createDto(1L, "Москва");
        String branchDtoJson = objectMapper.writeValueAsString(dto);
        when(service.create(dto)).thenReturn(dto);
        mockMvc.perform(post("/branch/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(branchDtoJson))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.city").value("Москва"))
                .andExpect(status().isOk());
        verify(service, times(1)).create(dto);
    }

    @Test
    @DisplayName("Ошибка 400 при пустом теле запроса")
    void create_NoSuccess() throws Exception {
        mockMvc.perform(post("/branch/create")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Успешное обновление Branch")
    void update_Success() throws Exception {
        BranchDto dto = createDto(1L, "Москва");
        String branchDtoJson = objectMapper.writeValueAsString(dto);
        when(service.update(1L, dto)).thenReturn(dto);
        mockMvc.perform(put("/branch/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(branchDtoJson))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.city").value("Москва"))
                .andExpect(status().isOk());
        verify(service, times(1)).update(1L, dto);
    }

    @Test
    @DisplayName("Ошибка 400 при пустом теле запроса")
    void update_NoSuccess_EmptyBody() throws Exception {
        mockMvc.perform(put("/branch/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private BranchDto createDto(Long id, String city) {
        return BranchDto.builder()
                .id(id)
                .city(city)
                .build();
    }
}