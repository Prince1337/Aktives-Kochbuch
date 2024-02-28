package prince.aktiveskochbuch.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import prince.aktiveskochbuch.adapter.db.ConfirmationRepository;
import prince.aktiveskochbuch.adapter.db.UserRepository;
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
    public User saveUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        user.setEnabled(false);
        userRepository.save(user);

        Confirmation confirmation = new Confirmation(user);
        confirmationRepository.save(confirmation);

        /* TODO Send email to user with token */
        emailService.sendSimpleMessage(user.getName(), user.getEmail(), confirmation.getToken());
        //emailService.sendMimeMessageWithAttachments(user.getName(), user.getEmail(), confirmation.getToken());
        //emailService.sendMimeMessageWithEmbeddedFiles(user.getName(), user.getEmail(), confirmation.getToken());
        //emailService.sendHtmlEmail(user.getName(), user.getEmail(), confirmation.getToken());
//        emailService.sendHtmlEmailWithEmbeddedFiles(user.getName(), user.getEmail(), confirmation.getToken());

        return user;
    }

    @Override
    public Boolean verifyToken(String token) {
        Confirmation confirmation = confirmationRepository.findByToken(token);
        User user = userRepository.findByEmailIgnoreCase(confirmation.getUser().getEmail());
        user.setEnabled(true);
        userRepository.save(user);
        //confirmationRepository.delete(confirmation);
        return Boolean.TRUE;
    }
}
