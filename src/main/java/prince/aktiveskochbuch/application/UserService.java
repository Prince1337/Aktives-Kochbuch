package prince.aktiveskochbuch.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import prince.aktiveskochbuch.adapter.db.ConfirmationRepository;
import prince.aktiveskochbuch.adapter.db.UserRepository;
import prince.aktiveskochbuch.application.exceptions.EMailSend;
import prince.aktiveskochbuch.application.exceptions.UserAlreadyExists;
import prince.aktiveskochbuch.domain.models.Confirmation;
import prince.aktiveskochbuch.domain.models.User;
import prince.aktiveskochbuch.domain.usecases.CreateUserUseCase;
import prince.aktiveskochbuch.domain.usecases.SendEmailUseCase;

@Service
@RequiredArgsConstructor
public class UserService implements CreateUserUseCase {
    private final UserRepository userRepository;
    private final ConfirmationRepository confirmationRepository;
    private final SendEmailUseCase emailService;

    @Override
    public User saveUser(User user) throws EMailSend {
        if (Boolean.TRUE.equals(existsByMail(user))) {
            throw new UserAlreadyExists("Email already exists");
        }

        user.setEnabled(false);
        userRepository.save(user);

        Confirmation confirmation = new Confirmation(user);
        confirmationRepository.save(confirmation);

        emailService.sendSimpleMessage(user.getName(), user.getEmail(), confirmation.getToken());

        return user;
    }

    private Boolean existsByMail(User user) {
        return userRepository.existsByEmail(user.getEmail());
    }

    @Override
    public Boolean verifyToken(String token) {
        Confirmation confirmation = confirmationRepository.findByToken(token);
        User user = userRepository.findByEmailIgnoreCase(confirmation.getUser().getEmail());
        user.setEnabled(true);
        userRepository.save(user);
        confirmationRepository.delete(confirmation);
        return Boolean.TRUE;
    }
}
