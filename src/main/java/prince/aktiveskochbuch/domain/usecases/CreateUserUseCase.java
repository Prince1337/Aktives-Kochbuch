package prince.aktiveskochbuch.domain.usecases;

import prince.aktiveskochbuch.domain.models.User;

public interface CreateUserUseCase {
    User saveUser(User user);
    Boolean verifyToken(String token);
}
