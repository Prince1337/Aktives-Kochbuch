package prince.aktiveskochbuch.adapter.apis;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import prince.aktiveskochbuch.domain.dtos.HttpResponse;
import prince.aktiveskochbuch.domain.usecases.AutomatischeVorschlaegeUseCase;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class SettingsController {

    private final AutomatischeVorschlaegeUseCase automatischeVorschlaegeUseCase;

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
