package chnu.edu.anetrebin.anb.service.transaction;

import chnu.edu.anetrebin.anb.dto.requests.TransactionRequest;
import chnu.edu.anetrebin.anb.dto.responses.TransactionResponse;

import java.util.List;

public interface TransactionService {
    void createTransaction(TransactionRequest transactionRequest);

    TransactionResponse getTransactionById(Long transactionId);

    List<TransactionResponse> getAllTransactions();
}
