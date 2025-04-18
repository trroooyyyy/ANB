package chnu.edu.anetrebin.anb.dto.requests;

import jakarta.validation.constraints.*;

import java.time.YearMonth;

public record CardRequest(
        // TODO: Handle beginning with zero
        @NotBlank(message = "Card number cannot be blank")
        @NotNull(message = "Card number cannot be blank")
        @Pattern(regexp = "^[0-9]{16}$", message = "Card number must be 16 digits")
        String cardNumber,

        @NotNull(message = "Expiry date cannot be null")
        @Future(message = "Card must not be expired")
        YearMonth expiryDate,

        @Min(value = 100, message = "CVV cannot be less than 100")
        @Max(value = 999, message = "CVV cannot be more than 999")
        short cvv
) {}
