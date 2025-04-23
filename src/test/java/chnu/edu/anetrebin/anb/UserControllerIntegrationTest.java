package chnu.edu.anetrebin.anb;

import chnu.edu.anetrebin.anb.controller.UserController;
import chnu.edu.anetrebin.anb.dto.requests.AccountRequest;
import chnu.edu.anetrebin.anb.dto.requests.CardRequest;
import chnu.edu.anetrebin.anb.dto.requests.UserRequest;
import chnu.edu.anetrebin.anb.dto.responses.UserResponse;
import chnu.edu.anetrebin.anb.enums.Currency;
import chnu.edu.anetrebin.anb.exceptions.card.CardAlreadyExists;
import chnu.edu.anetrebin.anb.exceptions.user.UserAlreadyExists;
import chnu.edu.anetrebin.anb.exceptions.user.UserNotFoundException;
import chnu.edu.anetrebin.anb.service.user.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserServiceImpl userService;

    private UserResponse testUser;

    @BeforeEach
    void setUp() {
        testUser = UserResponse.builder()
                .id(1L)
                .login("testuser")
                .name("John")
                .surname("Doe")
                .email("john.doe@example.com")
                .phone("+380123456789")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();
    }

    @Test
    void getAllUsers_ShouldReturnUsersList() throws Exception {
        Mockito.when(userService.getAllUsers())
                .thenReturn(List.of(testUser));

        mockMvc.perform(get("/api/v1/users/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].login", is("testuser")));
    }

    @Test
    void getUserById_WhenExists_ShouldReturnUser() throws Exception {
        Mockito.when(userService.getUserById(1L))
                .thenReturn(testUser);

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));
    }

    @Test
    void getUserById_WhenNotExists_ShouldReturnNotFound() throws Exception {
        Mockito.when(userService.getUserById(999L))
                .thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/api/v1/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void registerUser_WithValidData_ShouldReturnCreated() throws Exception {
        UserRequest validRequest = new UserRequest(
                "newuser",
                "password123",
                "Alice",
                "Smith",
                "alice@example.com",
                "+380987654321",
                LocalDate.of(1995, 5, 15)
        );

        mockMvc.perform(post("/api/v1/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void registerUser_WithShortLogin_ShouldReturnBadRequest() throws Exception {
        UserRequest invalidRequest = new UserRequest(
                "ab",  // Too short login
                "password123",
                "Alice",
                "Smith",
                "alice@example.com",
                "+380987654321",
                LocalDate.of(1995, 5, 15)
        );

        mockMvc.perform(post("/api/v1/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.login", is("Login should contain from 3 to 50 letters")));
    }

    @Test
    void registerUser_WithExistingLogin_ShouldReturnConflict() throws Exception {
        UserRequest existingRequest = new UserRequest(
                "existing",
                "password123",
                "Alice",
                "Smith",
                "alice@example.com",
                "+380987654321",
                LocalDate.of(1995, 5, 15)
        );

        Mockito.doThrow(new UserAlreadyExists("User with this login already exists."))
                .when(userService).registerUser(any(UserRequest.class));

        mockMvc.perform(post("/api/v1/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    void updateUser_WithValidData_ShouldReturnUpdatedUser() throws Exception {
        UserRequest updateRequest = new UserRequest(
                "updated",
                "newpassword",
                "John",
                "Doe",
                "john.new@example.com",
                "+380987654321",
                LocalDate.of(1990, 1, 1)
        );

        UserResponse updatedUser = UserResponse.builder()
                .id(1L)
                .login("updated")
                .name("John")
                .surname("Doe")
                .email("john.new@example.com")
                .phone("+380987654321")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();

        Mockito.when(userService.updateUser(anyLong(), any(UserRequest.class)))
                .thenReturn(updatedUser);

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login", is("updated")));
    }

    @Test
    void deleteUser_WhenExists_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void createAccount_WithValidData_ShouldReturnCreated() throws Exception {
        AccountRequest accountRequest = new AccountRequest(
                "My Account",
                Currency.USD
        );

        mockMvc.perform(post("/api/v1/users/createAccount/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void addCard_WithValidData_ShouldReturnCreated() throws Exception {
        CardRequest cardRequest = new CardRequest(
                "1234567812345678",
                java.time.YearMonth.of(2025, 12),
                (short) 123
        );

        mockMvc.perform(post("/api/v1/users/addCard/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void addCard_WithExistingCardNumber_ShouldReturnConflict() throws Exception {
        CardRequest cardRequest = new CardRequest(
                "1234567812345678",
                java.time.YearMonth.of(2025, 12),
                (short) 123
        );

        Mockito.doThrow(new CardAlreadyExists("Card number already exists."))
                .when(userService).addCard(anyLong(), any(CardRequest.class));

        mockMvc.perform(post("/api/v1/users/addCard/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardRequest)))
                .andExpect(status().isConflict());
    }
}