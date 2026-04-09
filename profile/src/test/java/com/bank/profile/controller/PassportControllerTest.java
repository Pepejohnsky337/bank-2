package com.bank.profile.controller;

import com.bank.common.handler.GlobalRestExceptionHandler;
import com.bank.profile.dto.PassportDto;
import com.bank.profile.service.PassportService;
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
class PassportControllerTest {

    @Mock
    private PassportService service;

    @InjectMocks
    private PassportController passportController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(passportController)
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Поиск по id, успешно")
    void read_IdIsExist_ReturnsValidResponseEntity() throws Exception {
        PassportDto passportDto = createTestPassportDto(1L, 1234, 123456L);

        when(service.findById(1L)).thenReturn(passportDto);

        mockMvc.perform(get("/passport/read/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.series").value(1234))
                .andExpect(jsonPath("$.number").value(123456L));
        verify(service, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Поиск по несуществующему id, должен вернуться статус not found")
    void read_IdIsNotExist_ReturnsNotFoundStatus() throws Exception {
        when(service.findById(999L))
                .thenThrow(new EntityNotFoundException("PassportEntity with id 999 not found"));

        mockMvc.perform(get("/passport/read/{id}", 999L))
                .andExpect(status().isNotFound());
        verify(service, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Поиск по существующим id, успешно")
    void readAllById_IdsIsExist_ReturnsValidResponseEntity() throws Exception {
        PassportDto dto1 = createTestPassportDto(1L, 1234, 123456L);
        PassportDto dto2 = createTestPassportDto(2L, 4321, 654321L);

        List<PassportDto> passportDtos = new ArrayList<>();
        passportDtos.add(dto1);
        passportDtos.add(dto2);

        when(service.findAllById(List.of(1L, 2L))).thenReturn(passportDtos);

        mockMvc.perform(get("/passport/read/all?ids=1&ids=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].series").value(1234))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].number").value(654321L));
        verify(service, times(1)).findAllById(List.of(1L, 2L));
    }

    @Test
    @DisplayName("Поиск по несуществующим id, должен вернуться статус not found")
    void readAllById_IdsIsNotExist_ReturnsNotFoundStatus() throws Exception {
        when(service.findAllById(List.of(999L, 888L)))
                .thenThrow(new EntityNotFoundException("PassportEntity not found"));

        mockMvc.perform(get("/passport/read/all?ids=999&ids=888"))
                .andExpect(status().isNotFound());
        verify(service, times(1)).findAllById(List.of(999L, 888L));
    }

    @Test
    @DisplayName("Создание записи, успешно")
    void create_ReturnsValidResponseEntity() throws Exception {
        PassportDto passportDto = createTestPassportDto(null, 1234, 123456L);
        passportDto.setId(10L);

        String passportDtoJson = objectMapper.writeValueAsString(passportDto);

        when(service.save(any(PassportDto.class))).thenReturn(passportDto);

        mockMvc.perform(post("/passport/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(passportDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.series").value(1234))
                .andExpect(jsonPath("$.number").value(123456L));
        verify(service, times(1)).save(any(PassportDto.class));
    }

    @Test
    @DisplayName("Создание записи с пустым телом запроса, должен вернуться статус bad request")
    void create_WithNullBody_ReturnsBadRequestStatus() throws Exception {
        mockMvc.perform(post("/passport/create")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Обновление записи, успешно")
    void update_ReturnsValidResponseEntity() throws Exception {
        PassportDto passportDto = createTestPassportDto(5L, 1234, 123456L);
        String passportDtoJson = objectMapper.writeValueAsString(passportDto);

        when(service.update(5L, passportDto)).thenReturn(passportDto);

        mockMvc.perform(put("/passport/update/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(passportDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.series").value(1234))
                .andExpect(jsonPath("$.number").value(123456L));
        verify(service, times(1)).update(5L, passportDto);
    }

    @Test
    @DisplayName("Обновление записи по несуществующему id, должен вернуться статус not found")
    void update_IdIsNotExist_ReturnsNotFoundStatus() throws Exception {
        PassportDto passportDto = createTestPassportDto(2L, 1234, 123456L);
        String passportDtoJson = objectMapper.writeValueAsString(passportDto);

        when(service.update(2L, passportDto))
                .thenThrow(new EntityNotFoundException("PassportEntity with id 2 not found"));

        mockMvc.perform(put("/passport/update/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(passportDtoJson))
                .andExpect(status().isNotFound());
        verify(service, times(1)).update(2L, passportDto);
    }

    private PassportDto createTestPassportDto(Long id, Integer series, Long number) {
        PassportDto dto = new PassportDto();
        dto.setId(id);
        dto.setSeries(series);
        dto.setNumber(number);
        return dto;
    }
}