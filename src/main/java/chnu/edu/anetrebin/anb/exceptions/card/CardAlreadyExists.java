package chnu.edu.anetrebin.anb.exceptions.card;

public class CardAlreadyExists extends RuntimeException {
    public CardAlreadyExists(String message) {
        super(message);
    }
}
