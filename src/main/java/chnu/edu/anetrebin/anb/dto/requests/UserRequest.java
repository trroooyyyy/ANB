package chnu.edu.anetrebin.anb.dto.requests;

import java.time.LocalDate;
import jakarta.validation.constraints.*;

public record UserRequest(
        @NotNull(message = "Login cannot be null")
        @Size(min = 3, max = 50, message = "Login should contain from 3 to 50 letters")
        String login,

        @NotNull(message = "Password cannot be null")
        @Size(min = 6, max = 50, message = "Password should contain from 6 to 50 symbols")
        String password,

        @Size(max = 50, message = "Name should contain less than 50 letters")
        String name,

        @Size(max = 50, message = "Surname should contain less than 50 letters")
        String surname,

        @Size(max = 100, message = "Email should contain less than 100 characters")
        @Email(message = "Email should be valid")
        String email,

        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number should be valid and contain from 10 to 15 numbers")
        String phone,

        @Past(message = "Date of birth should be in the past")
        LocalDate dateOfBirth
) {}

