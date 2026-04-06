package com.bank.antifraud.controller;

import javax.persistence.EntityNotFoundException;
import com.bank.antifraud.dto.AuditDto;
import com.bank.antifraud.service.AuditService;
import com.bank.antifraud.util.builder.AuditBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AuditController.class)
@DisplayName("Тесты для контроллера аудитов")
class AuditControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuditService service;
    
    private final String BASE_ENDPOINT = "/audit";
    private final String URL_READ = BASE_ENDPOINT + "/{id}";
    
    @Test
    @DisplayName("Должен вернуть аудит по id")
    void read_WhenExists_ThenReturnOk() throws Exception {
        final Long auditId = 1L;
        AuditBuilder builder = new AuditBuilder();
        AuditDto expectedDto = builder.id(auditId).buildDto();
        
        when(service.findById(auditId)).thenReturn(expectedDto);
        
        mockMvc.perform(
                get(URL_READ, auditId)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(auditId));
        
        verify(service).findById(auditId);
    }
    
    @Test
    @DisplayName("Должен вернуть 404, если аудит не найден")
    void read_WhenNotExists_ThenReturnNotFound() throws Exception {
        final Long auditId = 111L;
        
        when(service.findById(auditId)).thenThrow(new EntityNotFoundException());
        
        mockMvc.perform(
                get(URL_READ, auditId)
            )
            .andExpect(status().isNotFound());
        
        verify(service).findById(auditId);
    }
}