package com.bank.transfer.controller;

import com.bank.transfer.dto.PhoneTransferDto;
import com.bank.transfer.service.PhoneTransferService;

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

@WebMvcTest(PhoneTransferController.class)
class PhoneTransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PhoneTransferService service;

    private final Long PHONE_ID_FIRST = 1L;
    private final Long PHONE_ID_SECOND = 2L;

    @Test
    @DisplayName("GET /phone/read/all - возвращает список DTO")
    void readAll_ShouldReturnListOfDtos() throws Exception {
        List<PhoneTransferDto> dtos = List.of(new PhoneTransferDto(), new PhoneTransferDto());
        when(service.findAllById(List.of(PHONE_ID_FIRST, PHONE_ID_SECOND)))
                .thenReturn(dtos);

        mockMvc.perform(get("/phone/read/all")
                        .param("ids", String.valueOf(PHONE_ID_FIRST), String.valueOf(PHONE_ID_SECOND)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("GET /phone/read/{id} - возвращает DTO, если найден")
    void read_WhenExists_ShouldReturnDto() throws Exception {
        PhoneTransferDto dto = new PhoneTransferDto();
        dto.setId(PHONE_ID_FIRST);
        when(service.findById(PHONE_ID_FIRST)).thenReturn(dto);

        mockMvc.perform(get("/phone/read/{id}", PHONE_ID_FIRST))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(PHONE_ID_FIRST));
    }

    @Test
    @DisplayName("GET /phone/read/{id} - возвращает 404, если не найден")
    void read_WhenNotExists_ShouldReturnNotFound() throws Exception {
        when(service.findById(PHONE_ID_FIRST)).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/phone/read/{id}", PHONE_ID_FIRST))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /phone/create - создаёт и возвращает DTO")
    void create_ShouldReturnSavedDto() throws Exception {
        PhoneTransferDto savedDto = new PhoneTransferDto();
        savedDto.setId(PHONE_ID_FIRST);
        when(service.save(any(PhoneTransferDto.class))).thenReturn(savedDto);

        mockMvc.perform(post("/phone/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(PHONE_ID_FIRST));
    }

    @Test
    @DisplayName("PUT /phone/update/{id} - обновляет и возвращает DTO")
    void update_ShouldReturnUpdatedDto() throws Exception {
        PhoneTransferDto updatedDto = new PhoneTransferDto();
        updatedDto.setId(PHONE_ID_FIRST);
        when(service.update(eq(PHONE_ID_FIRST), any(PhoneTransferDto.class)))
                .thenReturn(updatedDto);

        mockMvc.perform(put("/phone/update/{id}", PHONE_ID_FIRST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(PHONE_ID_FIRST));
    }

    @Test
    @DisplayName("PUT /phone/update/{id} - возвращает 404, если не найден")
    void update_WhenNotFound_ShouldReturnNotFound() throws Exception {
        when(service.update(eq(PHONE_ID_FIRST), any(PhoneTransferDto.class)))
                .thenThrow(new EntityNotFoundException());

        mockMvc.perform(put("/phone/update/{id}", PHONE_ID_FIRST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound());
    }
}
