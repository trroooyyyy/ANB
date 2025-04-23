package chnu.edu.anetrebin.anb;

import chnu.edu.anetrebin.anb.controller.TransactionController;
import chnu.edu.anetrebin.anb.dto.requests.TransactionRequest;
import chnu.edu.anetrebin.anb.dto.responses.TransactionResponse;
import chnu.edu.anetrebin.anb.enums.TransactionStatus;
import chnu.edu.anetrebin.anb.exceptions.transaction.TransactionNotFoundException;
import chnu.edu.anetrebin.anb.service.transaction.TransactionService;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
public class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TransactionService transactionService;

    private TransactionResponse testTransaction;

    @BeforeEach
    void setUp() {
        testTransaction = TransactionResponse.builder()
                .id(1L)
                .senderAccountId(101L)
                .senderAccountNumber("UA1234567890")
                .receiverAccountId(102L)
                .receiverAccountNumber("UA0987654321")
                .amount(new BigDecimal("100.50"))
                .status(TransactionStatus.COMPLETED)
                .description("Test transfer")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void getAllTransactions_ShouldReturnTransactionsList() throws Exception {
        Mockito.when(transactionService.getAllTransactions())
                .thenReturn(List.of(testTransaction));

        mockMvc.perform(get("/api/v1/transactions/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].amount", is(100.50)));
    }

    @Test
    void getTransactionById_WhenExists_ShouldReturnTransaction() throws Exception {
        Mockito.when(transactionService.getTransactionById(1L))
                .thenReturn(testTransaction);

        mockMvc.perform(get("/api/v1/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.senderAccountNumber", is("UA1234567890")))
                .andExpect(jsonPath("$.status", is("COMPLETED")));
    }

    @Test
    void createTransaction_WithValidData_ShouldReturnCreated() throws Exception {
        TransactionRequest validRequest = new TransactionRequest(
                101L,
                102L,
                new BigDecimal("50.00"),
                "Valid transaction"
        );

        mockMvc.perform(post("/api/v1/transactions/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void createTransaction_WithInvalidSenderId_ShouldReturnBadRequest() throws Exception {
        TransactionRequest invalidRequest = new TransactionRequest(
                -1L,
                102L,
                new BigDecimal("50.00"),
                "Invalid sender"
        );

        mockMvc.perform(post("/api/v1/transactions/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.senderAccountId", is("Sender account id must be a positive number")));
    }

    @Test
    void createTransaction_WithInvalidAmount_ShouldReturnBadRequest() throws Exception {
        TransactionRequest invalidRequest = new TransactionRequest(
                101L,
                102L,
                new BigDecimal("0.00"),
                "Invalid amount"
        );

        mockMvc.perform(post("/api/v1/transactions/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.amount", is("Amount must be greater than 0")));
    }

    @Test
    void createTransaction_WithLongDescription_ShouldReturnBadRequest() throws Exception {
        String longDescription = "This description is way too long and exceeds the maximum allowed length of 50 characters";

        TransactionRequest invalidRequest = new TransactionRequest(
                101L,
                102L,
                new BigDecimal("10.00"),
                longDescription
        );

        mockMvc.perform(post("/api/v1/transactions/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description", is("Description should contain from 1 to 50 symbols")));
    }

    @Test
    void getTransactionById_WhenNotExists_ShouldReturnNotFound() throws Exception {
        Mockito.when(transactionService.getTransactionById(999L))
                .thenThrow(new TransactionNotFoundException("Transaction not found"));

        mockMvc.perform(get("/api/v1/transactions/999"))
                .andExpect(status().isNotFound());
    }
}