package chnu.edu.anetrebin.anb.dto.responses;

import chnu.edu.anetrebin.anb.model.Card;
import lombok.Builder;

import java.time.YearMonth;

@Builder
public record CardResponse(
        Long id,
        String cardNumber,
        YearMonth expiryDate,
        short cvv,
        String cardHolderName)
{
    public static CardResponse toResponse(Card card) {
        return CardResponse.builder()
                .id(card.getId())
                .cardNumber(card.getCardNumber())
                .expiryDate(card.getExpiryDate())
                .cvv(card.getCvv())
                .cardHolderName(card.getCardHolderName())
                .build();
    }
}
