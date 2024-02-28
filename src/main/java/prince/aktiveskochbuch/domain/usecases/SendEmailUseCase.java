package prince.aktiveskochbuch.domain.usecases;

import jakarta.mail.MessagingException;
import prince.aktiveskochbuch.domain.models.Rezept;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SendEmailUseCase {

    void sendAutomaticSuggestionsEmail(List<Rezept> vorschlaege, AutomatischeVorschlaegeUseCase automatischeVorschlaegeUseCase);

    void sendSimpleMessage(String to,
                           String subject,
                           String text);
    void sendSimpleMessageUsingTemplate(String to,
                                        String subject,
                                        String ...templateModel);
    void sendMessageWithAttachment(String to,
                                   String subject,
                                   String text,
                                   String pathToAttachment);

    void sendMessageUsingThymeleafTemplate(String to,
                                           String subject,
                                           Map<String, Object> templateModel)
            throws IOException, MessagingException;

    void sendMessageUsingFreemarkerTemplate(String to,
                                            String subject,
                                            Map<String, Object> templateModel)
            throws IOException, MessagingException;
}
