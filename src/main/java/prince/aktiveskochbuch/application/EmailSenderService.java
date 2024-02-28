package prince.aktiveskochbuch.application;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import prince.aktiveskochbuch.application.exceptions.EMailSend;
import prince.aktiveskochbuch.domain.models.Rezept;
import prince.aktiveskochbuch.domain.usecases.AutomatischeVorschlaegeUseCase;
import prince.aktiveskochbuch.domain.usecases.SendEmailUseCase;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static prince.aktiveskochbuch.application.utils.EmailUtils.getConfirmationMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService implements SendEmailUseCase {

    public static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification";

    private final JavaMailSender emailSender;

    @Value("${spring.mail.verify.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendAutomaticSuggestionsEmail(List<Rezept> vorschlaege, AutomatischeVorschlaegeUseCase automatischeVorschlaegeUseCase) throws EMailSend {
        if (automatischeVorschlaegeUseCase.isAutomaticSuggestionsActivated()) {


            log.info("Automatic suggestions is activated");
            Set<DayOfWeek> scheduledDays = automatischeVorschlaegeUseCase.getScheduledDays();
            LocalTime scheduledTime = automatischeVorschlaegeUseCase.getScheduledTime();

            LocalDateTime now = LocalDateTime.now();
            DayOfWeek currentDay = now.getDayOfWeek();
            LocalTime currentTime = now.toLocalTime();

            log.info("Current day: {}", currentDay);
            log.info("Current time: {}", currentTime);


            if (scheduledDays.contains(currentDay) && currentTime.isAfter(scheduledTime)) {
                // Send automatic suggestions email
                log.info("VorschlaegeService: generateVorschlaege: vorschlaege: {}", vorschlaege);
                sendVorschlaegeMail("user.getName()", "prince.pieritz@gmail.com", vorschlaege);
            }
        }
    }

    public void sendVorschlaegeMail(String name, String to, List<Rezept> vorschlaege) throws EMailSend {
        try {
            StringBuilder emailText = new StringBuilder();
            emailText.append("Hallo ").append(name).append(",\n\n");
            emailText.append("Hier sind Ihre automatischen Vorschläge für heute:\n");
            // Fügen Sie jeden Vorschlag aus der Liste ein
            for (Rezept vorschlag : vorschlaege) {
                emailText.append(vorschlag.toString()).append("\n");
            }
            // Fügen Sie zusätzliche Informationen oder Grußformel hinzu, falls erforderlich
            emailText.append("\nViele Grüße,\nIhr Kochbuch-Team");

            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("Your Automatic Recipe Suggestions");
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(emailText.toString());
            emailSender.send(message);
        } catch (Exception exception) {
            throw new EMailSend(exception);
        }
    }

    @Override
    @Async
    public void sendSimpleMessage(String name, String to, String token) throws EMailSend {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(getConfirmationMessage(name, host, token));
            emailSender.send(message);
        } catch (Exception exception) {
            throw new EMailSend(exception);
        }
    }

    @Override
    public void sendSimpleMessageUsingTemplate(String to, String subject, String... templateModel) {
        //Implement this method
    }

    @Override
    public void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) {
        //Implement this method
    }

    @Override
    public void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel) throws IOException, MessagingException {
        //Implement this method

    }

    @Override
    public void sendMessageUsingFreemarkerTemplate(String to, String subject, Map<String, Object> templateModel) throws IOException, MessagingException {
        //Implement this method
    }
}
