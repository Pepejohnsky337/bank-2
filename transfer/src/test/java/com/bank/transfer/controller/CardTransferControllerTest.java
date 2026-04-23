package com.bank.transfer.controller;

import com.bank.transfer.dto.CardTransferDto;
import com.bank.transfer.service.CardTransferService;

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

@WebMvcTest(CardTransferController.class)
class CardTransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardTransferService service;

    private final Long CARD_ID_FIRST = 1L;
    private final Long CARD_ID_SECOND = 2L;

    @Test
    @DisplayName("GET /card/read/all - возвращает список DTO")
    void readAll_ShouldReturnListOfDtos() throws Exception {
        List<CardTransferDto> dtos = List.of(new CardTransferDto(), new CardTransferDto());
        when(service.findAllById(List.of(CARD_ID_FIRST, CARD_ID_SECOND)))
                .thenReturn(dtos);

        mockMvc.perform(get("/card/read/all")
                        .param("ids", String.valueOf(CARD_ID_FIRST), String.valueOf(CARD_ID_SECOND)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("GET /card/read/{id} - возвращает DTO, если найден")
    void read_WhenExists_ShouldReturnDto() throws Exception {
        CardTransferDto dto = new CardTransferDto();
        dto.setId(CARD_ID_FIRST);
        when(service.findById(CARD_ID_FIRST)).thenReturn(dto);

        mockMvc.perform(get("/card/read/{id}", CARD_ID_FIRST))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(CARD_ID_FIRST));
    }

    @Test
    @DisplayName("GET /card/read/{id} - возвращает 404, если не найден")
    void read_WhenNotExists_ShouldReturnNotFound() throws Exception {
        when(service.findById(CARD_ID_FIRST)).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/card/read/{id}", CARD_ID_FIRST))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /card/create - создаёт и возвращает DTO")
    void create_ShouldReturnSavedDto() throws Exception {
        CardTransferDto savedDto = new CardTransferDto();
        savedDto.setId(CARD_ID_FIRST);
        when(service.save(any(CardTransferDto.class))).thenReturn(savedDto);

        mockMvc.perform(post("/card/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CARD_ID_FIRST));
    }

    @Test
    @DisplayName("PUT /card/update/{id} - обновляет и возвращает DTO")
    void update_ShouldReturnUpdatedDto() throws Exception {
        CardTransferDto updatedDto = new CardTransferDto();
        updatedDto.setId(CARD_ID_FIRST);
        when(service.update(eq(CARD_ID_FIRST), any(CardTransferDto.class)))
                .thenReturn(updatedDto);

        mockMvc.perform(put("/card/update/{id}", CARD_ID_FIRST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CARD_ID_FIRST));
    }

    @Test
    @DisplayName("PUT /card/update/{id} - возвращает 404, если не найден")
    void update_WhenNotFound_ShouldReturnNotFound() throws Exception {
        when(service.update(eq(CARD_ID_FIRST), any(CardTransferDto.class)))
                .thenThrow(new EntityNotFoundException());

        mockMvc.perform(put("/card/update/{id}", CARD_ID_FIRST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound());
    }
}
