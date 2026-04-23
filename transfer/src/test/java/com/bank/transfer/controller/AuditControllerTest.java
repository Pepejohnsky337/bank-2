package com.bank.transfer.controller;

import com.bank.transfer.dto.AuditDto;
import com.bank.transfer.service.AuditService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuditController.class)
class AuditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuditService service;

    private final Long AUDIT_ID = 1L;

    @Test
    @DisplayName("GET /audit/{id} - возвращает DTO, если найден")
    void read_WhenExists_ShouldReturnDto() throws Exception {
        AuditDto dto = new AuditDto();
        dto.setId(AUDIT_ID);
        when(service.findById(AUDIT_ID)).thenReturn(dto);

        mockMvc.perform(get("/audit/{id}", AUDIT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(AUDIT_ID));
    }

    @Test
    @DisplayName("GET /audit/{id} - возвращает 404, если не найден")
    void read_WhenNotExists_ShouldReturnNotFound() throws Exception {
        when(service.findById(AUDIT_ID)).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/audit/{id}", AUDIT_ID))
                .andExpect(status().isNotFound());
    }
}
