package prince.aktiveskochbuch.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import prince.aktiveskochbuch.adapter.db.UserRepository;
import prince.aktiveskochbuch.application.exceptions.EMailSend;
import prince.aktiveskochbuch.domain.models.Rezept;
import prince.aktiveskochbuch.domain.models.Role;
import prince.aktiveskochbuch.domain.models.StandardRezept;
import prince.aktiveskochbuch.domain.models.User;
import prince.aktiveskochbuch.domain.usecases.AutomatischeVorschlaegeUseCase;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailSenderServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EmailSenderService emailSenderService;

    @BeforeEach
    void setUp() {
        emailSenderService = new EmailSenderService(emailSender, userRepository);
    }

    @Test
    void sendAutomaticSuggestionsEmail_NotActivated() throws EMailSend {
        AutomatischeVorschlaegeUseCase automatischeVorschlaegeUseCase = mock(AutomatischeVorschlaegeUseCase.class);
        when(automatischeVorschlaegeUseCase.isAutomaticSuggestionsActivated()).thenReturn(false);

        List<Rezept> vorschlaege = Collections.singletonList(new StandardRezept("name", "description", Collections.emptyList()));

        emailSenderService.sendAutomaticSuggestionsEmail(vorschlaege, automatischeVorschlaegeUseCase);

        verify(emailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendAutomaticSuggestionsEmail_Activated_DueDateNotReached() throws EMailSend {
        AutomatischeVorschlaegeUseCase automatischeVorschlaegeUseCase = mock(AutomatischeVorschlaegeUseCase.class);
        when(automatischeVorschlaegeUseCase.isAutomaticSuggestionsActivated()).thenReturn(true);
        when(automatischeVorschlaegeUseCase.getScheduledDays()).thenReturn(Collections.singleton(DayOfWeek.MONDAY));
        when(automatischeVorschlaegeUseCase.getScheduledTime()).thenReturn(LocalTime.NOON);

        List<Rezept> vorschlaege = Collections.singletonList(new StandardRezept("name", "description", Collections.emptyList()));

        emailSenderService.sendAutomaticSuggestionsEmail(vorschlaege, automatischeVorschlaegeUseCase);

        verify(emailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendAutomaticSuggestionsEmail_Activated_DueDateReached() throws EMailSend {
        AutomatischeVorschlaegeUseCase automatischeVorschlaegeUseCase = mock(AutomatischeVorschlaegeUseCase.class);
        when(automatischeVorschlaegeUseCase.isAutomaticSuggestionsActivated()).thenReturn(true);
        when(automatischeVorschlaegeUseCase.getScheduledDays()).thenReturn(Collections.singleton(LocalDateTime.now().getDayOfWeek()));
        when(automatischeVorschlaegeUseCase.getScheduledTime()).thenReturn(LocalTime.now().minusMinutes(1));

        User user = new User(1L, "name", "email", "password", true, Role.USER, Collections.emptyList());
        when(userRepository.findByEmailIgnoreCase(null)).thenReturn(user);

        List<Rezept> vorschlaege = Collections.singletonList(new StandardRezept("Recipe", "Description", Collections.emptyList()));

        emailSenderService.sendAutomaticSuggestionsEmail(vorschlaege, automatischeVorschlaegeUseCase);

        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
    }

}