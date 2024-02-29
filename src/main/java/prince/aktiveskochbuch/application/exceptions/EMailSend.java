package prince.aktiveskochbuch.application.exceptions;

@SuppressWarnings("PMD.MissingSerialVersionUID")
public class EMailSend extends RuntimeException {
    public EMailSend(Exception exception) {
        super(exception);
    }
}
