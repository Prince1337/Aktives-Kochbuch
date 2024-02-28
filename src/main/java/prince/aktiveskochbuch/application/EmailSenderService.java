package prince.aktiveskochbuch.application;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import prince.aktiveskochbuch.domain.usecases.AutomatischeVorschlaegeUseCase;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final AutomatischeVorschlaegeUseCase automatischeVorschlaegeUseCase;
    private final JavaMailSender javaMailSender;

    public void sendAutomaticSuggestionsEmail() {
        if (automatischeVorschlaegeUseCase.isAutomaticSuggestionsActivated()) {
            Set<DayOfWeek> scheduledDays = automatischeVorschlaegeUseCase.getScheduledDays();
            LocalTime scheduledTime = automatischeVorschlaegeUseCase.getScheduledTime();

            LocalDateTime now = LocalDateTime.now();
            DayOfWeek currentDay = now.getDayOfWeek();
            LocalTime currentTime = now.toLocalTime();

            if (scheduledDays.contains(currentDay) && currentTime.isAfter(scheduledTime)) {
                // Send automatic suggestions email
                sendEmail("Your Automatic Recipe Suggestions", "Here are your recipe suggestions for today!");
            }
        }
    }

    private void sendEmail(String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setText(text);
        message.setTo("prince.pieritz@gmail.com");
        javaMailSender.send(message);
    }
}
