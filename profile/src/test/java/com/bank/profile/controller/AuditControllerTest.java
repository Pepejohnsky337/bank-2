package com.bank.profile.controller;

import com.bank.common.handler.GlobalRestExceptionHandler;
import com.bank.profile.dto.AuditDto;
import com.bank.profile.service.AuditService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.EntityNotFoundException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuditControllerTest {

    @Mock
    private AuditService service;

    @InjectMocks
    private AuditController auditController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(auditController)
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Поиск по id, успешно")
    void read_IdIsExist_ReturnsValidResponseEntity() throws Exception {
        AuditDto auditDto = createTestAuditDto(1L);

        when(service.findById(1L)).thenReturn(auditDto);

        mockMvc.perform(get("/audit/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
        verify(service, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Поиск по несуществующему id, должен вернуться статус not found")
    void read_IdIsNotExist_ReturnsNotFoundStatus() throws Exception {
        when(service.findById(999L))
                .thenThrow(new EntityNotFoundException("AuditEntity with id 999 not found"));

        mockMvc.perform(get("/audit/{id}", 999L))
                .andExpect(status().isNotFound());
        verify(service, times(1)).findById(999L);
    }

    private AuditDto createTestAuditDto(Long id) {
        AuditDto dto = new AuditDto();
        dto.setId(id);
        return dto;
    }
}