package com.bank.profile.controller;

import com.bank.common.handler.GlobalRestExceptionHandler;
import com.bank.profile.dto.AccountDetailsIdDto;
import com.bank.profile.dto.ProfileDto;
import com.bank.profile.service.AccountDetailsIdService;
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
class AccountDetailsIdControllerTest {

    @Mock
    private AccountDetailsIdService service;

    @InjectMocks
    private AccountDetailsIdController accountDetailsIdController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountDetailsIdController)
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Поиск по id, успешно")
    void read_IdIsExist_ReturnsValidResponseEntity() throws Exception {
        ProfileDto profile = createTestProfile(10L);
        AccountDetailsIdDto accountDetailsIdDto = createTestAccountDetailsDto(1L, 100L, profile);

        when(service.findById(1L)).thenReturn(accountDetailsIdDto);

        mockMvc.perform(get("/account/details/read/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountId").value(100))
                .andExpect(jsonPath("$.profile.id").value(10));
        verify(service, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Поиск по несуществующему id, должен вернуться статус not found")
    void read_IdIsNotExist_ReturnsNotFoundStatus() throws Exception {
        when(service.findById(999L))
                .thenThrow(new EntityNotFoundException("AccountDetailsIdEntity with id 999 not found"));

        mockMvc.perform(get("/account/details/read/{id}", 999L))
                .andExpect(status().isNotFound());
        verify(service, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Поиск по существующим id, успешно")
    void readAllById_IdsIsExist_ReturnsValidResponseEntity() throws Exception {
        ProfileDto profile1 = createTestProfile(1L);
        ProfileDto profile2 = createTestProfile(2L);

        AccountDetailsIdDto dto1 = createTestAccountDetailsDto(1L, 100L, profile1);
        AccountDetailsIdDto dto2 = createTestAccountDetailsDto(2L, 200L, profile2);

        List<AccountDetailsIdDto> accountDetailsIdDtos = new ArrayList<>();
        accountDetailsIdDtos.add(dto1);
        accountDetailsIdDtos.add(dto2);

        when(service.findAllById(List.of(1L, 2L))).thenReturn(accountDetailsIdDtos);

        mockMvc.perform(get("/account/details/read/all?ids=1&ids=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].accountId").value(100))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].accountId").value(200));
        verify(service, times(1)).findAllById(List.of(1L, 2L));
    }

    @Test
    @DisplayName("Поиск по несуществующим id, должен вернуться статус not found")
    void readAllById_IdsIsNotExist_ReturnsNotFoundStatus() throws Exception {
        when(service.findAllById(List.of(999L, 888L)))
                .thenThrow(new EntityNotFoundException("AccountDetailsIdEntity not found"));

        mockMvc.perform(get("/account/details/read/all?ids=999&ids=888"))
                .andExpect(status().isNotFound());
        verify(service, times(1)).findAllById(List.of(999L, 888L));
    }

    @Test
    @DisplayName("Создание записи, успешно")
    void create_ReturnsValidResponseEntity() throws Exception {
        ProfileDto profile = createTestProfile(null);
        AccountDetailsIdDto accountDetailsIdDto = createTestAccountDetailsDto(null, 100L, profile);
        accountDetailsIdDto.setId(10L); // ID после сохранения

        String accountDetailsIdDtoJson = objectMapper.writeValueAsString(accountDetailsIdDto);

        when(service.save(any(AccountDetailsIdDto.class))).thenReturn(accountDetailsIdDto);

        mockMvc.perform(post("/account/details/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountDetailsIdDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.accountId").value(100));
        verify(service, times(1)).save(any(AccountDetailsIdDto.class));
    }

    @Test
    @DisplayName("Создание записи с пустым телом запроса, должен вернуться статус bad request")
    void create_WithNullBody_ReturnsBadRequestStatus() throws Exception {
        mockMvc.perform(post("/account/details/create")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Обновление записи, успешно")
    void update_ReturnsValidResponseEntity() throws Exception {
        ProfileDto profile = createTestProfile(15L);
        AccountDetailsIdDto accountDetailsIdDto = createTestAccountDetailsDto(5L, 300L, profile);
        String accountDetailsIdDtoJson = objectMapper.writeValueAsString(accountDetailsIdDto);

        when(service.update(5L, accountDetailsIdDto)).thenReturn(accountDetailsIdDto);

        mockMvc.perform(put("/account/details/update/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountDetailsIdDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.accountId").value(300))
                .andExpect(jsonPath("$.profile.id").value(15));
        verify(service, times(1)).update(5L, accountDetailsIdDto);
    }

    @Test
    @DisplayName("Обновление записи по несуществующему id, должен вернуться статус not found")
    void update_IdIsNotExist_ReturnsNotFoundStatus() throws Exception {
        ProfileDto profile = createTestProfile(20L);
        AccountDetailsIdDto accountDetailsIdDto = createTestAccountDetailsDto(2L, 400L, profile);
        String accountDetailsIdDtoJson = objectMapper.writeValueAsString(accountDetailsIdDto);

        when(service.update(2L, accountDetailsIdDto))
                .thenThrow(new EntityNotFoundException("AccountDetailsIdEntity with id 2 not found"));

        mockMvc.perform(put("/account/details/update/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountDetailsIdDtoJson))
                .andExpect(status().isNotFound());
        verify(service, times(1)).update(2L, accountDetailsIdDto);
    }

    private ProfileDto createTestProfile(Long id) {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setId(id);
        return profileDto;
    }

    private AccountDetailsIdDto createTestAccountDetailsDto(Long id, Long accountId, ProfileDto profile) {
        AccountDetailsIdDto dto = new AccountDetailsIdDto();
        dto.setId(id);
        dto.setAccountId(accountId);
        dto.setProfile(profile);
        return dto;
    }
}