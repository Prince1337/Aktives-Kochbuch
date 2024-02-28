package prince.aktiveskochbuch.adapter.apis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import prince.aktiveskochbuch.domain.dtos.HttpResponse;
import prince.aktiveskochbuch.domain.dtos.VorschlaegeDto;
import prince.aktiveskochbuch.domain.models.Rezept;
import prince.aktiveskochbuch.domain.usecases.VorschlaegeGenerierenUseCase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
public class VorschlaegeController {

    private final VorschlaegeGenerierenUseCase vorschlaegeGenerierenUseCase;

    @PostMapping("/vorschlaege")
    public ResponseEntity<HttpResponse> generateVorschlaege(@RequestBody VorschlaegeDto vorschlaegeDto) {
        log.info("VorschlaegeController: generateVorschlaege: vorschlaegeDto: {}", vorschlaegeDto);
        List<Rezept> vorschlaege = vorschlaegeGenerierenUseCase.generateVorschlaege(vorschlaegeDto);

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
}
