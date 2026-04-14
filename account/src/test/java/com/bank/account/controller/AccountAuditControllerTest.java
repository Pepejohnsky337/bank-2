package com.bank.account.controller;

import com.bank.account.dto.AuditDto;
import com.bank.account.service.AccountAuditService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AccountAuditController.class)
@DisplayName("AccountAuditController Test ")
class AccountAuditControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private AccountAuditService accountAuditService;

    @Test
    @DisplayName("Get/audit/{id} - позитивный: успешное получение аудита по ID")
    void read_WithValidId_ReturnsAuditDto() throws Exception{

        Long auditId = 1L;

        AuditDto expectedDto = new AuditDto();
        expectedDto.setId(auditId);
        expectedDto.setEntityType("AccountDetails");
        expectedDto.setOperationType("CREATE");
        expectedDto.setCreatedBy("admin@bank.com");
        expectedDto.setModifiedBy("admin@bank.com");
        expectedDto.setNewEntityJson("{\"accountNumber\":\"12345\"}");
        expectedDto.setEntityJson("{\"id\":1}");

        when(accountAuditService.findById(auditId)).thenReturn(expectedDto);

        mockMvc.perform(get("/audit/{id}", auditId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(auditId))
                .andExpect(jsonPath("$.entityType").value("AccountDetails"))
                .andExpect(jsonPath("$.operationType").value("CREATE"))
                .andExpect(jsonPath("$.createdBy").value("admin@bank.com"))
                .andExpect(jsonPath("$.modifiedBy").value("admin@bank.com"))
                .andExpect(jsonPath("$.newEntityJson").value("{\"accountNumber\":\"12345\"}"))
                .andExpect(jsonPath("$.entityJson").value("{\"id\":1}"));


        verify(accountAuditService, times(1)).findById(auditId);
    }

    @Test
    @DisplayName("GET /audit/{id} - негативный: некорректный формат ID (не число)")
    void read_WithInvalidIdFormat_ReturnsBadRequest() throws Exception {

        mockMvc.perform(get("/audit/{id}", "abc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(accountAuditService, never()).findById(anyLong());
    }

    @Test
    @DisplayName("GET /audit/{id} - негативный: аудит не найден")
    void read_WithNonExistentId_ThrowsException() throws Exception {
        Long nonExistentId = 999L;

        when(accountAuditService.findById(nonExistentId))
                .thenThrow(new RuntimeException("Audit not found"));

        mockMvc.perform(get("/audit/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());

        verify(accountAuditService, times(1)).findById(nonExistentId);
    }
}