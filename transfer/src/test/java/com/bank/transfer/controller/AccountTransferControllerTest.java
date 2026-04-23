package com.bank.transfer.controller;

import com.bank.transfer.dto.AccountTransferDto;
import com.bank.transfer.service.AccountTransferService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountTransferController.class)
class AccountTransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountTransferService service;

    private final Long TRANSFER_ID_FIRST = 1L;
    private final Long TRANSFER_ID_SECOND = 2L;

    @Test
    @DisplayName("GET /account/read/all - возвращает список DTO")
    void readAll_ShouldReturnListOfDtos() throws Exception {
        List<AccountTransferDto> dtos = List.of(new AccountTransferDto(), new AccountTransferDto());
        when(service.findAllById(List.of(TRANSFER_ID_FIRST, TRANSFER_ID_SECOND)))
                .thenReturn(dtos);

        mockMvc.perform(get("/account/read/all")
                        .param("ids", String.valueOf(TRANSFER_ID_FIRST), String.valueOf(TRANSFER_ID_SECOND)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("GET /account/read/{id} - возвращает DTO, если найден")
    void read_WhenExists_ShouldReturnDto() throws Exception {
        AccountTransferDto dto = new AccountTransferDto();
        dto.setId(TRANSFER_ID_FIRST);
        when(service.findById(TRANSFER_ID_FIRST)).thenReturn(dto);

        mockMvc.perform(get("/account/read/{id}", TRANSFER_ID_FIRST))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(TRANSFER_ID_FIRST));
    }

    @Test
    @DisplayName("GET /account/read/{id} - возвращает 404, если не найден")
    void read_WhenNotExists_ShouldReturnNotFound() throws Exception {
        when(service.findById(TRANSFER_ID_FIRST)).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/account/read/{id}", TRANSFER_ID_FIRST))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /account/create - создаёт и возвращает DTO")
    void create_ShouldReturnSavedDto() throws Exception {
        AccountTransferDto savedDto = new AccountTransferDto();
        savedDto.setId(TRANSFER_ID_FIRST);
        when(service.save(any(AccountTransferDto.class))).thenReturn(savedDto);

        mockMvc.perform(post("/account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TRANSFER_ID_FIRST));
    }

    @Test
    @DisplayName("PUT /account/update/{id} - обновляет и возвращает DTO")
    void update_ShouldReturnUpdatedDto() throws Exception {
        AccountTransferDto updatedDto = new AccountTransferDto();
        updatedDto.setId(TRANSFER_ID_FIRST);
        when(service.update(eq(TRANSFER_ID_FIRST), any(AccountTransferDto.class)))
                .thenReturn(updatedDto);

        mockMvc.perform(put("/account/update/{id}", TRANSFER_ID_FIRST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TRANSFER_ID_FIRST));
    }

    @Test
    @DisplayName("PUT /account/update/{id} - возвращает 404, если не найден")
    void update_WhenNotFound_ShouldReturnNotFound() throws Exception {
        when(service.update(eq(TRANSFER_ID_FIRST), any(AccountTransferDto.class)))
                .thenThrow(new EntityNotFoundException());

        mockMvc.perform(put("/account/update/{id}", TRANSFER_ID_FIRST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound());
    }
}
