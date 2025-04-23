package chnu.edu.anetrebin.anb.service.card.impl;

import chnu.edu.anetrebin.anb.dto.requests.CardRequest;
import chnu.edu.anetrebin.anb.dto.responses.CardResponse;
import chnu.edu.anetrebin.anb.exceptions.card.CardAlreadyExists;
import chnu.edu.anetrebin.anb.exceptions.card.CardNotFoundException;
import chnu.edu.anetrebin.anb.model.Card;
import chnu.edu.anetrebin.anb.repository.CardRepository;
import chnu.edu.anetrebin.anb.service.card.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;

    @Transactional
    @Override
    public void deleteAccount(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException("Card not found with id: " + id));
        cardRepository.delete(card);
    }

    @Transactional(readOnly = true)
    @Override
    public CardResponse getCardById(Long id) {
        return CardResponse.toResponse(cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException("Card not found with id: " + id)));
    }

    @Transactional
    @Override
    public CardResponse updateAccount(Long id, CardRequest cardRequest) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException("Card not found with id: " + id));

        if (cardRequest.cardNumber() != null && cardRepository.existsByCardNumber(cardRequest.cardNumber()) && !Objects.equals(card.getCardNumber(), cardRequest.cardNumber())) {
            throw new CardAlreadyExists("Card number already exists.");
        }

        card.setCardNumber(cardRequest.cardNumber());
        card.setExpiryDate(cardRequest.expiryDate());
        card.setCvv(cardRequest.cvv());

        return CardResponse.toResponse(cardRepository.save(card));
    }

    @Transactional(readOnly = true)
    @Override
    public List<CardResponse> getAllCards() {
        return cardRepository.findAllByOrderByIdAsc().stream().map(CardResponse::toResponse).collect(Collectors.toList());
    }
}
