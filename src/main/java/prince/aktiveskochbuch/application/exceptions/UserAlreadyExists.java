package prince.aktiveskochbuch.application.exceptions;

public class UserAlreadyExists extends RuntimeException {
    public UserAlreadyExists(String emailAlreadyExists) {
        super(emailAlreadyExists);
    }
}
