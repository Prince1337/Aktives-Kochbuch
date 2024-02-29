package prince.aktiveskochbuch.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import prince.aktiveskochbuch.adapter.db.UserRepository;
import prince.aktiveskochbuch.application.exceptions.EMailSend;
import prince.aktiveskochbuch.domain.models.Rezept;
import prince.aktiveskochbuch.domain.models.User;
import prince.aktiveskochbuch.domain.usecases.AutomatischeVorschlaegeUseCase;
import prince.aktiveskochbuch.domain.usecases.SendEmailUseCase;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static prince.aktiveskochbuch.application.utils.EmailUtils.buildEmailText;
import static prince.aktiveskochbuch.application.utils.EmailUtils.getConfirmationMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService implements SendEmailUseCase {

    public static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification";
    public static final String YOUR_AUTOMATIC_RECIPE_SUGGESTIONS = "Your Automatic Recipe Suggestions";

    private final JavaMailSender emailSender;

    private final UserRepository userRepository;

    @Value("${spring.mail.verify.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String fromEmail;
    private Set<DayOfWeek> scheduledDays;
    private LocalTime scheduledTime;
    private DayOfWeek currentDay;
    private LocalTime currentTime;

    @Override
    public void sendAutomaticSuggestionsEmail(List<Rezept> vorschlaege, AutomatischeVorschlaegeUseCase automatischeVorschlaegeUseCase) throws EMailSend {
        if (!isAutomaticSuggestionsActivated(automatischeVorschlaegeUseCase)) {
            return;
        }

        log.info("Automatic suggestions is activated");
        setSchedule(automatischeVorschlaegeUseCase);

        if (!dueDateReached()) {
            return;
        }

        User user = userRepository.findByEmailIgnoreCase(fromEmail);

        log.info("VorschlaegeService: generateVorschlaege: vorschlaege: {}", vorschlaege);
        sendVorschlaegeMail(user.getName(), user.getEmail(), vorschlaege);
    }

    private void sendVorschlaegeMail(String name, String to, List<Rezept> vorschlaege) throws EMailSend {
        try {
            StringBuilder emailText = buildEmailText(name, vorschlaege);
            sendEmail(fromEmail, to, emailText.toString());
        } catch (Exception exception) {
            throw new EMailSend(exception);
        }
    }

    private void sendEmail(String from, String to, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(EmailSenderService.YOUR_AUTOMATIC_RECIPE_SUGGESTIONS);
        message.setFrom(from);
        message.setTo(to);
        message.setText(text);
        emailSender.send(message);
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
    public void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel)  {
        //Implement this method

    }

    @Override
    public void sendMessageUsingFreemarkerTemplate(String to, String subject, Map<String, Object> templateModel)  {
        //Implement this method
    }

    private void setSchedule(AutomatischeVorschlaegeUseCase automatischeVorschlaegeUseCase) {
        scheduledDays = automatischeVorschlaegeUseCase.getScheduledDays();
        scheduledTime = automatischeVorschlaegeUseCase.getScheduledTime();

        LocalDateTime now = LocalDateTime.now();
        currentDay = now.getDayOfWeek();
        currentTime = now.toLocalTime();

        log.info("Current day: {}", currentDay);
        log.info("Current time: {}", currentTime);
    }

    private static boolean isAutomaticSuggestionsActivated(AutomatischeVorschlaegeUseCase automatischeVorschlaegeUseCase) {
        return automatischeVorschlaegeUseCase.isAutomaticSuggestionsActivated();
    }

    private boolean dueDateReached() {
        return scheduledDays.contains(currentDay) && currentTime.isAfter(scheduledTime);
    }
}
