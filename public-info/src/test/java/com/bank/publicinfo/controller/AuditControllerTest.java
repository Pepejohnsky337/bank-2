package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.service.AuditService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;

@WebMvcTest(controllers = AuditController.class)
class AuditControllerTest {

    @MockBean
    private AuditService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Успешное получение Audit по ID")
    void readById_Success() throws Exception {
        AuditDto auditDto = createDto(1L, "audit");
        when(service.findById(1L)).thenReturn(auditDto);

        mockMvc.perform(get("/audit/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.entityType").value("audit"))
                .andExpect(jsonPath("$.operationType").value("CREATE"))
                .andExpect(jsonPath("$.createdBy").value("admin"))
                .andExpect(jsonPath("$.modifiedBy").value("admin"))
                .andExpect(jsonPath("$.createdAt").doesNotExist())
                .andExpect(jsonPath("$.modifiedAt").doesNotExist())
                .andExpect(jsonPath("$.newEntityJson").value("new entity json"))
                .andExpect(jsonPath("$.entityJson").value("old entity json"));

        verify(service, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Ошибка 404 при получении несуществующего Audit")
    void shouldExceptionThrownWhenNonExistentIdPassed() throws Exception {
        when(service.findById(1L)).thenThrow(new EntityNotFoundException("Is not found"));
        mockMvc.perform(get("/audit/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    private AuditDto createDto(Long id, String entityType) {
        return AuditDto.builder()
                .id(id)
                .entityType(entityType)
                .operationType("CREATE")
                .createdBy("admin")
                .modifiedBy("admin")
                .newEntityJson("new entity json")
                .entityJson("old entity json")
                .build();
    }
}