package prince.aktiveskochbuch.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import prince.aktiveskochbuch.adapter.db.ConfirmationRepository;
import prince.aktiveskochbuch.adapter.db.UserRepository;
import prince.aktiveskochbuch.application.exceptions.EMailSend;
import prince.aktiveskochbuch.application.exceptions.UserAlreadyExists;
import prince.aktiveskochbuch.domain.models.Confirmation;
import prince.aktiveskochbuch.domain.models.User;
import prince.aktiveskochbuch.domain.usecases.SendEmailUseCase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConfirmationRepository confirmationRepository;

    @Mock
    private SendEmailUseCase emailService;

    @InjectMocks
    private UserService userService;

    @Test
    void saveUser_NewUser_Success() throws EMailSend {
        User newUser = new User();
        newUser.setEmail("newuser@example.com");


        // Konfiguriere die Rückgabesequenz für confirmationRepository.save
        Confirmation savedConfirmation = new Confirmation(newUser);
        savedConfirmation.setToken("token");

        // Konfiguration für userRepository.existsByEmail
        when(userRepository.existsByEmail(newUser.getEmail())).thenReturn(false);

        // Konfiguration für confirmationRepository.findByUser
        Confirmation confirmation = new Confirmation(newUser);
        when(confirmationRepository.findByUser(newUser)).thenReturn(confirmation);

        // Konfiguration für confirmationRepository.save
        when(confirmationRepository.save(any(Confirmation.class))).thenReturn(confirmation);

        // Konfiguration für getToken
        when(confirmationRepository.findByUser(newUser)).thenReturn(new Confirmation(newUser));
        assertDoesNotThrow(() -> userService.saveUser(newUser));

        verify(userRepository, times(1)).save(newUser);
        verify(confirmationRepository, times(1)).save(any(Confirmation.class));
        verify(emailService, times(1)).sendSimpleMessage(eq(newUser.getName()), eq(newUser.getEmail()), anyString());
    }

    @Test
    void saveUser_ExistingUser_Exception() {
        User existingUser = new User();
        existingUser.setEmail("existinguser@example.com");

        when(userRepository.existsByEmail(existingUser.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyExists.class, () -> userService.saveUser(existingUser));

        verify(userRepository, never()).save(existingUser);
        verify(confirmationRepository, never()).save(any(Confirmation.class));
        verify(emailService, never()).sendSimpleMessage(anyString(), anyString(), anyString());
    }

    @Test
    void verifyToken_ValidToken_Success() {
        String validToken = "validToken";
        Confirmation confirmation = new Confirmation();
        User user = new User();
        confirmation.setUser(user);

        when(confirmationRepository.findByToken(validToken)).thenReturn(confirmation);
        when(userRepository.findByEmailIgnoreCase(user.getEmail())).thenReturn(user);

        assertTrue(userService.verifyToken(validToken), "Token should be verified");

        verify(userRepository, times(1)).save(user);
        verify(confirmationRepository, times(1)).delete(confirmation);
    }

    @Test
    void verifyToken_InvalidToken_Failure() {
        String invalidToken = "invalidToken";

        when(confirmationRepository.findByToken(invalidToken)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> userService.verifyToken(invalidToken));

        verify(userRepository, never()).save(any(User.class));
        verify(confirmationRepository, never()).delete(any(Confirmation.class));
    }

}