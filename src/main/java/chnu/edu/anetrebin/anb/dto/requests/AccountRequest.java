package chnu.edu.anetrebin.anb.dto.requests;

import chnu.edu.anetrebin.anb.enums.Currency;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AccountRequest(
        @NotNull(message = "Account name cannot be null")
        @Size(min = 1, max = 20, message = "Account name should contain from 1 to 20 symbols")
        String accountName,

        @NotNull(message = "Account currency cannot be null")
        Currency currency
) {}
