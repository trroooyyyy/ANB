package chnu.edu.anetrebin.anb.dto.responses;

import chnu.edu.anetrebin.anb.enums.TransactionStatus;
import chnu.edu.anetrebin.anb.model.Account;
import chnu.edu.anetrebin.anb.model.Transaction;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record TransactionResponse(
        Long id,
        Long senderAccountId,
        String senderAccountNumber,
        Long receiverAccountId,
        String receiverAccountNumber,
        BigDecimal amount,
        TransactionStatus status,
        String description,
        LocalDateTime createdAt
) {
    public static TransactionResponse toResponse(Transaction transaction, Account senderAccount, Account receiverAccount) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .senderAccountId(senderAccount.getId())
                .senderAccountNumber(senderAccount.getAccountNumber())
                .receiverAccountId(receiverAccount.getId())
                .receiverAccountNumber(receiverAccount.getAccountNumber())
                .amount(transaction.getAmount())
                .status(transaction.getStatus())
                .description(transaction.getDescription())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
