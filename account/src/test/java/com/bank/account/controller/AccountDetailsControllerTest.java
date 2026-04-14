package com.bank.account.controller;

import com.bank.account.dto.AccountDetailsDto;
import com.bank.account.service.AccountDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(AccountDetailsController.class)
@DisplayName("AccountDetailsController Test")
class AccountDetailsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountDetailsService accountDetailsService;

    private static final Long DEFAULT_ID = 1L;
    private static final Long DEFAULT_PASSPORT_ID = 12345L;
    private static final Long DEFAULT_ACCOUNT_NUMBER = 4081781009999L;
    private static final Long DEFAULT_BANK_DETAILS_ID = 100L;
    private static final BigDecimal DEFAULT_MONEY = BigDecimal.valueOf(15000.50);
    private static final boolean DEFAULT_NEGATIVE_BALANCE = false;
    private static final Long DEFAULT_PROFILE_ID = 1L;

    @Test
    @DisplayName("GET/details/{id} - позитивный : успешное получение по id")
    void read_WithValidId_ReturnsAccountDetailsDto() throws Exception {
        Long id = DEFAULT_ID;
        AccountDetailsDto expectedDto = createAccountDetailsDtoWithId(id);

        when(accountDetailsService.findById(id)).thenReturn(expectedDto);

        mockMvc.perform(get("/details/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.passportId").value(DEFAULT_PASSPORT_ID))
                .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER))
                .andExpect(jsonPath("$.bankDetailsId").value(DEFAULT_BANK_DETAILS_ID))
                .andExpect(jsonPath("$.money").value(DEFAULT_MONEY))
                .andExpect(jsonPath("$.negativeBalance").value(DEFAULT_NEGATIVE_BALANCE))
                .andExpect(jsonPath("$.profileId").value(DEFAULT_PROFILE_ID));

        verify(accountDetailsService, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /details/{id} - негативный: некорректный формат ID")
    void read_WithInvalidIdFormat_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/details/{id}", "abc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(accountDetailsService, never()).findById(anyLong());
    }

    @Test
    @DisplayName("POST /details/create - позитивный: успешное создание")
    void create_WithValidData_ReturnsCreatedAccount() throws Exception {
        AccountDetailsDto requestDto = createDefaultAccountDetailsDto();

        AccountDetailsDto responseDto = createAccountDetailsDtoWithId(10L);

        when(accountDetailsService.save(any(AccountDetailsDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/details/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.passportId").value(DEFAULT_PASSPORT_ID))
                .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER))
                .andExpect(jsonPath("$.bankDetailsId").value(DEFAULT_BANK_DETAILS_ID))
                .andExpect(jsonPath("$.money").value(DEFAULT_MONEY))
                .andExpect(jsonPath("$.negativeBalance").value(DEFAULT_NEGATIVE_BALANCE))
                .andExpect(jsonPath("$.profileId").value(DEFAULT_PROFILE_ID));

        verify(accountDetailsService, times(1)).save(any(AccountDetailsDto.class));
    }

    @Test
    @DisplayName("POST /details/create - негативный: пустое тело запроса")
    void create_WithEmptyBody_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/details/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());

        verify(accountDetailsService, never()).save(any());
    }

    @Test
    @DisplayName("PUT /details/update/{id} - позитивный: успешное обновление")
    void update_WithValidData_ReturnsUpdateAccount() throws Exception {
        Long id = DEFAULT_ID;
        AccountDetailsDto requestDto = createDefaultAccountDetailsDto();

        AccountDetailsDto responseDto = createAccountDetailsDtoWithId(10L);
        responseDto.setNegativeBalance(true);

        when(accountDetailsService.update(eq(id), any(AccountDetailsDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/details/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.passportId").value(DEFAULT_PASSPORT_ID))
                .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER))
                .andExpect(jsonPath("$.money").value(DEFAULT_MONEY))
                .andExpect(jsonPath("$.negativeBalance").value(true));

        verify(accountDetailsService, times(1)).update(eq(id), any(AccountDetailsDto.class));
    }

    @Test
    @DisplayName("PUT /details/{id} - негативный: обновление несуществующей записи")
    void update_WithNonExistentId_TrowsException() throws Exception {
        Long nonExistentId = 999L;
        AccountDetailsDto requestDto = createDefaultAccountDetailsDto();
        requestDto.setMoney(BigDecimal.valueOf(1000.00));

        when(accountDetailsService.update(eq(nonExistentId), any(AccountDetailsDto.class)))
                .thenThrow(new RuntimeException("Account not found with id:" + nonExistentId));

        mockMvc.perform(put("/details/update/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().is5xxServerError());

        verify(accountDetailsService, times(1)).update(eq(nonExistentId), any(AccountDetailsDto.class));
    }

    @Test
    @DisplayName("GET /details/read/all - позитивный: успешное получение списка по ID")
    void readAll_WithValidIds_ReturnsListOfDtos() throws Exception {
        List<Long> ids = List.of(1L, 2L, 3L);

        AccountDetailsDto dto1 = createCustomAccountDetailsDto(1L, 111L, BigDecimal.valueOf(1000.00));
        AccountDetailsDto dto2 = createCustomAccountDetailsDto(2L, 222L, BigDecimal.valueOf(2000.00));
        AccountDetailsDto dto3 = createCustomAccountDetailsDto(3L, 333L, BigDecimal.valueOf(3000.00));

        List<AccountDetailsDto> expectedDtos = Arrays.asList(dto1, dto2, dto3);

        when(accountDetailsService.findAllById(ids)).thenReturn(expectedDtos);

        mockMvc.perform(get("/details/read/all")
                        .param("ids", "1", "2", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[2].id").value(3L));

        verify(accountDetailsService, times(1)).findAllById(ids);
    }

    @Test
    @DisplayName("GET /details/read/all - негативный: сервис выбросил исключение")
    void readAll_WhenServiceThrowsException_ReturnsServerError() throws Exception {
        List<Long> ids = List.of(1L, 2L);
        when(accountDetailsService.findAllById(ids))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/details/read/all")
                        .param("ids", "1", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());

        verify(accountDetailsService, times(1)).findAllById(ids);
    }

    private static AccountDetailsDto createDefaultAccountDetailsDto() {
        AccountDetailsDto dto = new AccountDetailsDto();
        dto.setPassportId(DEFAULT_PASSPORT_ID);
        dto.setAccountNumber(DEFAULT_ACCOUNT_NUMBER);
        dto.setBankDetailsId(DEFAULT_BANK_DETAILS_ID);
        dto.setMoney(DEFAULT_MONEY);
        dto.setNegativeBalance(DEFAULT_NEGATIVE_BALANCE);
        dto.setProfileId(DEFAULT_PROFILE_ID);
        return dto;
    }

    private static AccountDetailsDto createAccountDetailsDtoWithId(Long id) {
        AccountDetailsDto dto = createDefaultAccountDetailsDto();
        dto.setId(id);
        return dto;
    }

    private static AccountDetailsDto createCustomAccountDetailsDto(Long id, Long passportId, BigDecimal money) {
        AccountDetailsDto dto = createDefaultAccountDetailsDto();
        dto.setId(id);
        dto.setPassportId(passportId);
        dto.setMoney(money);
        return dto;
    }
}