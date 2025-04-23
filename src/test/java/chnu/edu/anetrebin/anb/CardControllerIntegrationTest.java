package chnu.edu.anetrebin.anb;

import chnu.edu.anetrebin.anb.controller.CardController;
import chnu.edu.anetrebin.anb.dto.requests.CardRequest;
import chnu.edu.anetrebin.anb.dto.responses.CardResponse;
import chnu.edu.anetrebin.anb.service.card.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CardController.class)
@AutoConfigureMockMvc
public class CardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CardService cardService;

    @Test
    public void getAllCards_ShouldReturnAllCards() throws Exception {
        CardResponse card1 = CardResponse.builder()
                .id(1L)
                .cardNumber("1234567812345678")
                .expiryDate(YearMonth.of(2025, 12))
                .cvv((short) 123)
                .cardHolderName("John Doe")
                .build();

        CardResponse card2 = CardResponse.builder()
                .id(2L)
                .cardNumber("8765432187654321")
                .expiryDate(YearMonth.of(2026, 6))
                .cvv((short) 456)
                .cardHolderName("Jane Smith")
                .build();

        List<CardResponse> cards = Arrays.asList(card1, card2);

        Mockito.when(cardService.getAllCards()).thenReturn(cards);

        mockMvc.perform(get("/api/v1/cards/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].cardNumber", is("1234567812345678")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].cardNumber", is("8765432187654321")));
    }

    @Test
    public void getCardById_WhenCardExists_ShouldReturnCard() throws Exception {
        CardResponse card = CardResponse.builder()
                .id(1L)
                .cardNumber("1234567812345678")
                .expiryDate(YearMonth.of(2025, 12))
                .cvv((short) 123)
                .cardHolderName("John Doe")
                .build();

        Mockito.when(cardService.getCardById(1L)).thenReturn(card);

        mockMvc.perform(get("/api/v1/cards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.cardNumber", is("1234567812345678")))
                .andExpect(jsonPath("$.expiryDate", is("2025-12")))
                .andExpect(jsonPath("$.cvv", is(123)))
                .andExpect(jsonPath("$.cardHolderName", is("John Doe")));
    }

    @Test
    public void updateCard_WithValidData_ShouldReturnUpdatedCard() throws Exception {
        CardRequest request = new CardRequest(
                "1234567812345678",
                YearMonth.of(2025, 12),
                (short) 123
        );

        CardResponse response = CardResponse.builder()
                .id(1L)
                .cardNumber("1234567812345678")
                .expiryDate(YearMonth.of(2025, 12))
                .cvv((short) 123)
                .cardHolderName("John Doe")
                .build();

        Mockito.when(cardService.updateAccount(anyLong(), any(CardRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/cards/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.cardNumber", is("1234567812345678")));
    }

    @Test
    public void updateCard_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        CardRequest invalidRequest = new CardRequest(
                "1234",
                YearMonth.of(2020, 1),
                (short) 99
        );

        mockMvc.perform(put("/api/v1/cards/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.cardNumber", is("Card number must be 16 digits")))
                .andExpect(jsonPath("$.expiryDate", is("Card must not be expired")))
                .andExpect(jsonPath("$.cvv", is("CVV cannot be less than 100")));
    }

    @Test
    public void deleteCard_WhenCardExists_ShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(cardService).deleteAccount(1L);

        mockMvc.perform(delete("/api/v1/cards/1"))
                .andExpect(status().isNoContent());
    }
}