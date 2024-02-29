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
        validateUserDoesNotExist(user);

        user.setEnabled(false);
        saveUserAndCreateConfirmation(user);
        sendConfirmationEmail(user);

        return user;
    }

    @Override
    public Boolean verifyToken(String token) {
        Confirmation confirmation = findConfirmationByToken(token);
        User user = enableUserAndSave(confirmation);
        userRepository.save(user);
        deleteConfirmation(confirmation);

        return Boolean.TRUE;
    }

    private void validateUserDoesNotExist(User user) {
        if (existsByMail(user).equals(true)) {
            throw new UserAlreadyExists("Email already exists");
        }
    }

    private void saveUserAndCreateConfirmation(User user) {
        userRepository.save(user);

        Confirmation confirmation = new Confirmation(user);
        confirmationRepository.save(confirmation);
    }

    private void sendConfirmationEmail(User user) throws EMailSend {
        emailService.sendSimpleMessage(user.getName(), user.getEmail(), generateConfirmationToken(user));
    }

    private String generateConfirmationToken(User user) {
        return confirmationRepository.findByUser(user).getToken();
    }

    private Boolean existsByMail(User user) {
        return userRepository.existsByEmail(user.getEmail());
    }

    private Confirmation findConfirmationByToken(String token) {
        if (confirmationRepository.findByToken(token) == null) {
            throw new IllegalArgumentException("Token not found");
        }
        return confirmationRepository.findByToken(token);
    }

    private User enableUserAndSave(Confirmation confirmation) {
        User user = findUserByEmail(confirmation.getUser().getEmail());
        user.setEnabled(true);
        return userRepository.save(user);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    private void deleteConfirmation(Confirmation confirmation) {
        confirmationRepository.delete(confirmation);
    }

}
