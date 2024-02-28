package prince.aktiveskochbuch.adapter.apis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import prince.aktiveskochbuch.application.exceptions.EMailSend;
import prince.aktiveskochbuch.domain.dtos.HttpResponse;
import prince.aktiveskochbuch.domain.dtos.VorschlaegeDto;
import prince.aktiveskochbuch.domain.models.Rezept;
import prince.aktiveskochbuch.domain.usecases.AutomatischeVorschlaegeUseCase;
import prince.aktiveskochbuch.domain.usecases.SendEmailUseCase;
import prince.aktiveskochbuch.domain.usecases.VorschlaegeGenerierenUseCase;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
public class VorschlaegeController {

    private final VorschlaegeGenerierenUseCase vorschlaegeGenerierenUseCase;
    private final SendEmailUseCase sendEmailUseCase;
    private final AutomatischeVorschlaegeUseCase automatischeVorschlaegeUseCase;


    @PostMapping("/vorschlaege")
    public ResponseEntity<HttpResponse> generateAutomaticVorschlaege(@RequestBody VorschlaegeDto vorschlaegeDto) throws EMailSend {
        log.info("VorschlaegeController: generateVorschlaege: vorschlaegeDto: {}", vorschlaegeDto);
        List<Rezept> vorschlaege = vorschlaegeGenerierenUseCase.generateVorschlaege(vorschlaegeDto);
        sendEmailUseCase.sendAutomaticSuggestionsEmail(vorschlaege, automatischeVorschlaegeUseCase);

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .status(HttpStatus.OK)
                        .statusCode(200)
                        .message("Vorschlaege generiert")
                        .timeStamp(LocalDateTime.now())
                        .developerMessage("Vorschlaege generiert")
                        .data(Map.of("vorschlaege", vorschlaege))
                        .build()
        );
    }

    @PostMapping("/automatic-suggestions")
    public ResponseEntity<HttpResponse> toggleAutomaticSuggestions(@RequestParam boolean activate) {
        automatischeVorschlaegeUseCase.activateAutomaticSuggestions(activate);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .status(HttpStatus.OK)
                        .timeStamp(LocalDateTime.now())
                        .statusCode(200)
                        .developerMessage("Einstellungen für automatische Vorschläge aktualisiert")
                        .message("Einstellungen erfolgreich gespeichert")
                        .build());
    }

    @PostMapping("/schedule")
    public ResponseEntity<HttpResponse> setSchedule(@RequestParam Set<DayOfWeek> daysOfWeek, @RequestParam LocalTime time) {
        automatischeVorschlaegeUseCase.setSchedule(daysOfWeek, time);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .status(HttpStatus.OK)
                        .timeStamp(LocalDateTime.now())
                        .statusCode(200)
                        .developerMessage("Einstellungen für den Zeitplan aktualisiert")
                        .message("Einstellungen erfolgreich gespeichert")
                        .build());
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getAutomaticSuggestionsStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("activated", automatischeVorschlaegeUseCase.isAutomaticSuggestionsActivated());
        status.put("scheduledDays", automatischeVorschlaegeUseCase.getScheduledDays());
        status.put("scheduledTime", automatischeVorschlaegeUseCase.getScheduledTime());
        return ResponseEntity.ok(status);
    }
}
