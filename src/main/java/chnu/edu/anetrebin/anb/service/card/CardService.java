package chnu.edu.anetrebin.anb.service.card;

import chnu.edu.anetrebin.anb.dto.requests.CardRequest;
import chnu.edu.anetrebin.anb.dto.responses.CardResponse;

import java.util.List;

public interface CardService {
    void deleteAccount(Long id);

    CardResponse getCardById(Long id);

    CardResponse updateAccount(Long id, CardRequest cardRequest);

    List<CardResponse> getAllCards();
}
