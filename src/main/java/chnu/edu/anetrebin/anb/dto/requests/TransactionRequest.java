package chnu.edu.anetrebin.anb.dto.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransactionRequest(
        @NotNull(message = "Sender account id name cannot be null")
        @Positive(message = "Sender account id must be a positive number")
        Long senderAccountId,

        @NotNull(message = "Receiver account id name cannot be null")
        @Positive(message = "Receiver account id must be a positive number")
        Long receiverAccountId,

        @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
        @NotNull(message = "Amount is required")
        BigDecimal amount,

        @NotNull(message = "Description cannot be null")
        @Size(min = 1, max = 50, message = "Description should contain from 1 to 50 symbols")
        String description
) {}
