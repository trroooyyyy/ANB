package chnu.edu.anetrebin.anb.exception;

public class UserAlreadyExists extends RuntimeException{
    public UserAlreadyExists(String message) {
        super(message);
    }
}
