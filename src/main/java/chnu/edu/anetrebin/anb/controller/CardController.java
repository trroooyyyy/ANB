package chnu.edu.anetrebin.anb.controller;

import chnu.edu.anetrebin.anb.dto.requests.CardRequest;
import chnu.edu.anetrebin.anb.dto.responses.CardResponse;
import chnu.edu.anetrebin.anb.service.card.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
@Validated
public class CardController {
    private final CardService cardService;

    @GetMapping("/")
    public ResponseEntity<List<CardResponse>> getAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardResponse> getCardById(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.getCardById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardResponse> updateCard(@PathVariable Long id, @Valid @RequestBody CardRequest cardRequest) {
        return ResponseEntity.ok(cardService.updateAccount(id, cardRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
