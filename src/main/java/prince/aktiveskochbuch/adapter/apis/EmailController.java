package prince.aktiveskochbuch.adapter.apis;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import prince.aktiveskochbuch.application.EmailSenderService;
import prince.aktiveskochbuch.domain.dtos.HttpResponse;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailSenderService emailSenderService;

    @PostMapping("/send-automatic-emails")
    public ResponseEntity<HttpResponse> sendAutomaticEmails() {
        emailSenderService.sendAutomaticSuggestionsEmail();
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .status(HttpStatus.OK)
                        .message("Automatic emails sent")
                        .build()
        );
    }
}
