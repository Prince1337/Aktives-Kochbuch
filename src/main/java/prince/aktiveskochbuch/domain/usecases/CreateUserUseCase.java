package prince.aktiveskochbuch.domain.usecases;

import prince.aktiveskochbuch.application.exceptions.EMailSend;
import prince.aktiveskochbuch.domain.models.User;

public interface CreateUserUseCase {
    User saveUser(User user) throws EMailSend;
    Boolean verifyToken(String token);
}
