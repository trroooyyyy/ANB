package chnu.edu.anetrebin.anb.controller;

import chnu.edu.anetrebin.anb.dto.requests.TransactionRequest;
import chnu.edu.anetrebin.anb.dto.responses.TransactionResponse;
import chnu.edu.anetrebin.anb.service.transaction.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Validated
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/")
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @PostMapping("/")
    public ResponseEntity<Void> createTransaction(@RequestBody @Valid TransactionRequest transactionRequest) {
        transactionService.createTransaction(transactionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
