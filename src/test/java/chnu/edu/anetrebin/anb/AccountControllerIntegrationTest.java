package chnu.edu.anetrebin.anb;

import chnu.edu.anetrebin.anb.controller.AccountController;
import chnu.edu.anetrebin.anb.dto.requests.AccountRequest;
import chnu.edu.anetrebin.anb.dto.responses.AccountResponse;
import chnu.edu.anetrebin.anb.enums.AccountStatus;
import chnu.edu.anetrebin.anb.enums.Currency;
import chnu.edu.anetrebin.anb.exceptions.account.AccountNotFoundException;
import chnu.edu.anetrebin.anb.service.account.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
public class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AccountService accountService;

    private AccountResponse testAccount;

    @BeforeEach
    void setUp() {
        testAccount = AccountResponse.builder()
                .id(1L)
                .accountName("Test Account")
                .accountNumber("UA1234567890")
                .balance(new BigDecimal("1000.00"))
                .currency(Currency.UAH)
                .status(AccountStatus.ACTIVE)
                .build();
    }

    @Test
    void getAllAccounts_ShouldReturnAccountsList() throws Exception {
        Mockito.when(accountService.getAllAccounts())
                .thenReturn(List.of(testAccount));

        mockMvc.perform(get("/api/v1/accounts/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].accountName", is("Test Account")));
    }

    @Test
    void getAccountById_WhenExists_ShouldReturnAccount() throws Exception {
        Mockito.when(accountService.getAccountById(1L))
                .thenReturn(testAccount);

        mockMvc.perform(get("/api/v1/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.accountNumber", is("UA1234567890")))
                .andExpect(jsonPath("$.status", is("ACTIVE")));
    }

    @Test
    void getAccountById_WhenNotExists_ShouldReturnNotFound() throws Exception {
        Mockito.when(accountService.getAccountById(999L))
                .thenThrow(new AccountNotFoundException("Account not found"));

        mockMvc.perform(get("/api/v1/accounts/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAccount_WithValidData_ShouldReturnUpdatedAccount() throws Exception {
        AccountRequest request = new AccountRequest("Updated Name", Currency.UAH);

        AccountResponse updatedAccount = AccountResponse.builder()
                .id(1L)
                .accountName("Updated Name")
                .accountNumber("UA1234567890")
                .balance(new BigDecimal("1000.00"))
                .currency(Currency.UAH)
                .status(AccountStatus.ACTIVE)
                .build();

        Mockito.when(accountService.updateAccount(anyLong(), any(AccountRequest.class)))
                .thenReturn(updatedAccount);

        mockMvc.perform(put("/api/v1/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountName", is("Updated Name")));
    }

    @Test
    void updateAccount_WithEmptyName_ShouldReturnBadRequest() throws Exception {
        AccountRequest invalidRequest = new AccountRequest("", Currency.UAH);

        mockMvc.perform(put("/api/v1/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.accountName", is("Account name should contain from 1 to 20 symbols")));
    }

    @Test
    void updateAccount_WithNullCurrency_ShouldReturnBadRequest() throws Exception {
        AccountRequest invalidRequest = new AccountRequest("Valid Name", null);

        mockMvc.perform(put("/api/v1/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.currency", is("Account currency cannot be null")));
    }

    @Test
    void deleteAccount_WhenExists_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/accounts/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAccount_WhenNotExists_ShouldThrowException() throws Exception {
        Mockito.doThrow(new AccountNotFoundException("Account not found"))
                .when(accountService).deleteAccount(999L);

        mockMvc.perform(delete("/api/v1/accounts/999"))
                .andExpect(status().isNotFound());
    }
}