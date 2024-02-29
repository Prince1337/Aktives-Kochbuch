package prince.aktiveskochbuch.application.exceptions;

@SuppressWarnings("PMD.MissingSerialVersionUID")
public class UserAlreadyExists extends RuntimeException {
    public UserAlreadyExists(String emailAlreadyExists) {
        super(emailAlreadyExists);
    }
}
