package chnu.edu.anetrebin.anb.service.transaction.impl;

import chnu.edu.anetrebin.anb.dto.requests.TransactionRequest;
import chnu.edu.anetrebin.anb.dto.responses.TransactionResponse;
import chnu.edu.anetrebin.anb.enums.TransactionStatus;
import chnu.edu.anetrebin.anb.exceptions.account.AccountNotFoundException;
import chnu.edu.anetrebin.anb.exceptions.transaction.TransactionException;
import chnu.edu.anetrebin.anb.exceptions.transaction.TransactionNotFoundException;
import chnu.edu.anetrebin.anb.model.Account;
import chnu.edu.anetrebin.anb.model.Transaction;
import chnu.edu.anetrebin.anb.repository.AccountRepository;
import chnu.edu.anetrebin.anb.repository.TransactionRepository;
import chnu.edu.anetrebin.anb.service.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Transactional
    @Override
    public void createTransaction(TransactionRequest transactionRequest) {
        Account sender = accountRepository.findById(transactionRequest.senderAccountId()).orElseThrow(() ->
                new AccountNotFoundException("Sender account not found"));

        Account receiver = accountRepository.findById(transactionRequest.receiverAccountId()).orElseThrow(() ->
                new AccountNotFoundException("Receiver account not found"));

        validateTransaction(sender, receiver, transactionRequest.amount());

        Transaction transaction = Transaction.builder()
                .senderAccount(sender)
                .receiverAccount(receiver)
                .amount(transactionRequest.amount())
                .description(transactionRequest.description())
                .build();

        transaction = transactionRepository.save(transaction);
        try {
            processTransaction(transaction, sender, receiver, transactionRequest.amount());

            transaction.setStatus(TransactionStatus.COMPLETED);
            transactionRepository.save(transaction);
        } catch (Exception e) {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);

            throw new TransactionException("Transaction processing failed: " + e.getMessage());
        }
    }

    private void validateTransaction(Account sender, Account receiver, BigDecimal amount) {
        if (!sender.getAccountStatus().isActive() || !receiver.getAccountStatus().isActive()) {
            throw new TransactionException("One of the accounts is not active");
        }

        if (sender.getId().equals(receiver.getId())) {
            throw new TransactionException("Sender and Receiver accounts are the same");
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new TransactionException("Insufficient funds");
        }

        // TODO: Currency EXCHANGE
        if (!sender.getCurrency().equals(receiver.getCurrency())) {
            throw new TransactionException("Currency mismatch");
        }
    }

    @Transactional
    protected void processTransaction(Transaction transaction, Account sender, Account receiver, BigDecimal amount) {
        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        accountRepository.save(sender);
        accountRepository.save(receiver);
    }

    @Transactional(readOnly = true)
    @Override
    public TransactionResponse getTransactionById(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + transactionId));

        return TransactionResponse.toResponse(
                transaction,
                transaction.getSenderAccount(),
                transaction.getReceiverAccount()
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(transaction -> TransactionResponse.toResponse(
                        transaction,
                        transaction.getSenderAccount(),
                        transaction.getReceiverAccount()
                ))
                .collect(Collectors.toList());
    }
}
