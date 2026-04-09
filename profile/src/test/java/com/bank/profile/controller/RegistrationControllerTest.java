package com.bank.profile.controller;

import com.bank.common.handler.GlobalRestExceptionHandler;
import com.bank.profile.dto.RegistrationDto;
import com.bank.profile.service.RegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {

    @Mock
    private RegistrationService service;

    @InjectMocks
    private RegistrationController registrationController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController)
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Поиск по id, успешно")
    void read_IdIsExist_ReturnsValidResponseEntity() throws Exception {
        RegistrationDto registrationDto = createTestRegistrationDto(1L);

        when(service.findById(1L)).thenReturn(registrationDto);

        mockMvc.perform(get("/registration/read/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
        verify(service, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Поиск по несуществующему id, должен вернуться статус not found")
    void read_IdIsNotExist_ReturnsNotFoundStatus() throws Exception {
        when(service.findById(999L))
                .thenThrow(new EntityNotFoundException("RegistrationEntity with id 999 not found"));

        mockMvc.perform(get("/registration/read/{id}", 999L))
                .andExpect(status().isNotFound());
        verify(service, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Поиск по существующим id, успешно")
    void readAllById_IdsIsExist_ReturnsValidResponseEntity() throws Exception {
        RegistrationDto dto1 = createTestRegistrationDto(1L);
        RegistrationDto dto2 = createTestRegistrationDto(2L);

        List<RegistrationDto> registrationDtos = new ArrayList<>();
        registrationDtos.add(dto1);
        registrationDtos.add(dto2);

        when(service.findAllById(List.of(1L, 2L))).thenReturn(registrationDtos);

        mockMvc.perform(get("/registration/read/all?ids=1&ids=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
        verify(service, times(1)).findAllById(List.of(1L, 2L));
    }

    @Test
    @DisplayName("Поиск по несуществующим id, должен вернуться статус not found")
    void readAllById_IdsIsNotExist_ReturnsNotFoundStatus() throws Exception {
        when(service.findAllById(List.of(999L, 888L)))
                .thenThrow(new EntityNotFoundException("RegistrationEntity not found"));

        mockMvc.perform(get("/registration/read/all?ids=999&ids=888"))
                .andExpect(status().isNotFound());
        verify(service, times(1)).findAllById(List.of(999L, 888L));
    }

    @Test
    @DisplayName("Создание записи, успешно")
    void create_ReturnsValidResponseEntity() throws Exception {
        RegistrationDto registrationDto = createTestRegistrationDto(null);
        registrationDto.setId(10L);

        String registrationDtoJson = objectMapper.writeValueAsString(registrationDto);

        when(service.save(any(RegistrationDto.class))).thenReturn(registrationDto);

        mockMvc.perform(post("/registration/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
        verify(service, times(1)).save(any(RegistrationDto.class));
    }

    @Test
    @DisplayName("Создание записи с пустым телом запроса, должен вернуться статус bad request")
    void create_WithNullBody_ReturnsBadRequestStatus() throws Exception {
        mockMvc.perform(post("/registration/create")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Обновление записи, успешно")
    void update_ReturnsValidResponseEntity() throws Exception {
        RegistrationDto registrationDto = createTestRegistrationDto(5L);
        String registrationDtoJson = objectMapper.writeValueAsString(registrationDto);

        when(service.update(5L, registrationDto)).thenReturn(registrationDto);

        mockMvc.perform(put("/registration/update/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));
        verify(service, times(1)).update(5L, registrationDto);
    }

    @Test
    @DisplayName("Обновление записи по несуществующему id, должен вернуться статус not found")
    void update_IdIsNotExist_ReturnsNotFoundStatus() throws Exception {
        RegistrationDto registrationDto = createTestRegistrationDto(2L);
        String registrationDtoJson = objectMapper.writeValueAsString(registrationDto);

        when(service.update(2L, registrationDto))
                .thenThrow(new EntityNotFoundException("RegistrationEntity with id 2 not found"));

        mockMvc.perform(put("/registration/update/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationDtoJson))
                .andExpect(status().isNotFound());
        verify(service, times(1)).update(2L, registrationDto);
    }

    private RegistrationDto createTestRegistrationDto(Long id) {
        RegistrationDto dto = new RegistrationDto();
        dto.setId(id);
        return dto;
    }
}