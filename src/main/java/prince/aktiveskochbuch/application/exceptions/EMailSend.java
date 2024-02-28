package prince.aktiveskochbuch.application.exceptions;

public class EMailSend extends Throwable {
    public EMailSend(Exception exception) {
        super(exception);
    }
}
