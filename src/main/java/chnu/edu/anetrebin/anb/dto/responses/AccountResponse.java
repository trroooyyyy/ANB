package chnu.edu.anetrebin.anb.dto.responses;

import chnu.edu.anetrebin.anb.enums.AccountStatus;
import chnu.edu.anetrebin.anb.enums.Currency;
import chnu.edu.anetrebin.anb.model.Account;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AccountResponse(
        Long id,
        String accountName,
        String accountNumber,
        BigDecimal balance,
        Currency currency,
        AccountStatus status
) {
    public static AccountResponse toResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountName(account.getAccountName())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .status(account.getAccountStatus())
                .build();
    }
}
