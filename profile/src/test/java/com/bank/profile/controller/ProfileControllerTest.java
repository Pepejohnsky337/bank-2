package com.bank.profile.controller;

import com.bank.common.handler.GlobalRestExceptionHandler;
import com.bank.profile.dto.ProfileDto;
import com.bank.profile.service.ProfileService;
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
class ProfileControllerTest {

    @Mock
    private ProfileService service;

    @InjectMocks
    private ProfileController profileController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(profileController)
                .setControllerAdvice(new GlobalRestExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Поиск по id, успешно")
    void read_IdIsExist_ReturnsValidResponseEntity() throws Exception {
        ProfileDto profileDto = createTestProfileDto(1L);

        when(service.findById(1L)).thenReturn(profileDto);

        mockMvc.perform(get("/profile/read/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
        verify(service, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Поиск по несуществующему id, должен вернуться статус not found")
    void read_IdIsNotExist_ReturnsNotFoundStatus() throws Exception {
        when(service.findById(999L))
                .thenThrow(new EntityNotFoundException("ProfileEntity with id 999 not found"));

        mockMvc.perform(get("/profile/read/{id}", 999L))
                .andExpect(status().isNotFound());
        verify(service, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Поиск по существующим id, успешно")
    void readAllById_IdsIsExist_ReturnsValidResponseEntity() throws Exception {
        ProfileDto dto1 = createTestProfileDto(1L);
        ProfileDto dto2 = createTestProfileDto(2L);

        List<ProfileDto> profileDtos = new ArrayList<>();
        profileDtos.add(dto1);
        profileDtos.add(dto2);

        when(service.findAllById(List.of(1L, 2L))).thenReturn(profileDtos);

        mockMvc.perform(get("/profile/read/all?ids=1&ids=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
        verify(service, times(1)).findAllById(List.of(1L, 2L));
    }

    @Test
    @DisplayName("Поиск по несуществующим id, должен вернуться статус not found")
    void readAllById_IdsIsNotExist_ReturnsNotFoundStatus() throws Exception {
        when(service.findAllById(List.of(999L, 888L)))
                .thenThrow(new EntityNotFoundException("ProfileEntity not found"));

        mockMvc.perform(get("/profile/read/all?ids=999&ids=888"))
                .andExpect(status().isNotFound());
        verify(service, times(1)).findAllById(List.of(999L, 888L));
    }

    @Test
    @DisplayName("Создание записи, успешно")
    void create_ReturnsValidResponseEntity() throws Exception {
        ProfileDto profileDto = createTestProfileDto(null);
        profileDto.setId(10L);

        String profileDtoJson = objectMapper.writeValueAsString(profileDto);

        when(service.save(any(ProfileDto.class))).thenReturn(profileDto);

        mockMvc.perform(post("/profile/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(profileDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
        verify(service, times(1)).save(any(ProfileDto.class));
    }

    @Test
    @DisplayName("Создание записи с пустым телом запроса, должен вернуться статус bad request")
    void create_WithNullBody_ReturnsBadRequestStatus() throws Exception {
        mockMvc.perform(post("/profile/create")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Обновление записи, успешно")
    void update_ReturnsValidResponseEntity() throws Exception {
        ProfileDto profileDto = createTestProfileDto(5L);
        String profileDtoJson = objectMapper.writeValueAsString(profileDto);

        when(service.update(5L, profileDto)).thenReturn(profileDto);

        mockMvc.perform(put("/profile/update/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(profileDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));
        verify(service, times(1)).update(5L, profileDto);
    }

    @Test
    @DisplayName("Обновление записи по несуществующему id, должен вернуться статус not found")
    void update_IdIsNotExist_ReturnsNotFoundStatus() throws Exception {
        ProfileDto profileDto = createTestProfileDto(2L);
        String profileDtoJson = objectMapper.writeValueAsString(profileDto);

        when(service.update(2L, profileDto))
                .thenThrow(new EntityNotFoundException("ProfileEntity with id 2 not found"));

        mockMvc.perform(put("/profile/update/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(profileDtoJson))
                .andExpect(status().isNotFound());
        verify(service, times(1)).update(2L, profileDto);
    }

    private ProfileDto createTestProfileDto(Long id) {
        ProfileDto dto = new ProfileDto();
        dto.setId(id);
        return dto;
    }
}