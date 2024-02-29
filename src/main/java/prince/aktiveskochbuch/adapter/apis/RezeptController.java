package prince.aktiveskochbuch.adapter.apis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import prince.aktiveskochbuch.application.utils.RezeptConstants;
import prince.aktiveskochbuch.domain.dtos.HttpResponse;
import prince.aktiveskochbuch.domain.dtos.RezeptDto;
import prince.aktiveskochbuch.domain.models.Rezept;
import prince.aktiveskochbuch.domain.usecases.DeleteRezeptUseCase;
import prince.aktiveskochbuch.domain.usecases.GetRezeptUseCase;
import prince.aktiveskochbuch.domain.usecases.PostRezeptUseCase;
import prince.aktiveskochbuch.domain.usecases.PutRezeptUseCase;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
public class RezeptController {

    private final GetRezeptUseCase getRezeptUseCase;
    private final PostRezeptUseCase postRezeptUseCase;
    private final PutRezeptUseCase putRezeptUseCase;
    private final DeleteRezeptUseCase deleteRezeptUseCase;


    @PostMapping("/rezepte")
    public ResponseEntity<HttpResponse> createRezept(@RequestBody RezeptDto rezeptRequest) {
        log.info("RezeptController: createRezept: rezept: {}", rezeptRequest);
        Rezept rezept = getRezeptWithType(rezeptRequest);

        return getHttpResponseCreateEntity(rezept);
    }


    @GetMapping("/rezepte")
    public ResponseEntity<HttpResponse> getRezepte() {
        log.info("RezeptController: getRezepte");
        List<Rezept> rezepte = getRezeptUseCase.getAllRezepte();
        return getHttpResponseGetAllEntity(rezepte);
    }

    @GetMapping("/rezepte/{titel}")
    public ResponseEntity<HttpResponse> getRezept(@PathVariable String titel) {
        log.info("RezeptController: getRezept: titel: {}", titel);
        Rezept rezept = getRezeptUseCase.getRezept(titel);
        return getHttpResponseGetEntity(rezept);
    }


    @PutMapping("/rezepte/{id}")
    public ResponseEntity<HttpResponse> updateRezept(@PathVariable Long id, @RequestBody RezeptDto request) {
        log.info("RezeptController: updateRezept: id: {}, rezept: {}", id, request);
        Rezept rezept = putRezeptUseCase.updateRezept(id, request);
        return getHttpResponseUpdateEntity(rezept);
    }


    @DeleteMapping("/rezepte/{id}")
    public ResponseEntity<HttpResponse> deleteRezept(@PathVariable Long id) {
        log.info("RezeptController: deleteRezept: id: {}", id);
        deleteRezeptUseCase.deleteRezept(id);

        return getHttpResponseDeleteEntity();
    }

    private static ResponseEntity<HttpResponse> getHttpResponseDeleteEntity() {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .status(HttpStatus.OK)
                        .statusCode(200)
                        .message("Rezept gelöscht")
                        .timeStamp(LocalDateTime.now())
                        .developerMessage("deleteRezept erfolgreich ausgeführt")
                        .build()
        );
    }

    private Rezept getRezeptWithType(RezeptDto rezeptRequest) {
        if (isVegetarisch(rezeptRequest)) {
            log.info(RezeptConstants.REZEPT_CONTROLLER_CREATE_REZEPT, rezeptRequest);
            return postRezeptUseCase.createVegetarischesRezept(rezeptRequest);
        } else if (isGlutenfrei(rezeptRequest)) {
            log.info(RezeptConstants.REZEPT_CONTROLLER_CREATE_REZEPT, rezeptRequest);
            return postRezeptUseCase.createGlutenfreiesRezept(rezeptRequest);
        } else {
            log.info(RezeptConstants.REZEPT_CONTROLLER_CREATE_REZEPT, rezeptRequest);
            return postRezeptUseCase.createStandardRezept(rezeptRequest);
        }
    }

    private static ResponseEntity<HttpResponse> getHttpResponseUpdateEntity(Rezept rezept) {
        if (rezept == null) {
            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .status(HttpStatus.OK)
                            .statusCode(200)
                            .message("Rezept nicht gefunden")
                            .data(Collections.emptyMap())
                            .timeStamp(LocalDateTime.now())
                            .developerMessage("updateRezept erfolgreich ausgeführt")
                            .build()
            );
        }
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .status(HttpStatus.OK)
                        .statusCode(200)
                        .message("Rezept aktualisiert")
                        .timeStamp(LocalDateTime.now())
                        .developerMessage("updateRezept erfolgreich ausgeführt")
                        .data(Map.of(RezeptConstants.REZEPT, rezept))
                        .build()
        );
    }

    private static ResponseEntity<HttpResponse> getHttpResponseGetEntity(Rezept rezept) {
        if (rezept == null) {
            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .status(HttpStatus.OK)
                            .statusCode(200)
                            .message("Rezept nicht gefunden")
                            .data(Collections.emptyMap())
                            .timeStamp(LocalDateTime.now())
                            .developerMessage("getRezept erfolgreich ausgeführt")
                            .build()
            );
        }
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .status(HttpStatus.OK)
                        .statusCode(200)
                        .message("Rezept gefunden")
                        .timeStamp(LocalDateTime.now())
                        .developerMessage("getRezept erfolgreich ausgeführt")
                        .data(Map.of(RezeptConstants.REZEPT, rezept))
                        .build()
        );
    }

    private static ResponseEntity<HttpResponse> getHttpResponseCreateEntity(Rezept rezept) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .status(HttpStatus.OK)
                        .statusCode(200)
                        .timeStamp(LocalDateTime.now())
                        .message(RezeptConstants.REZEPT_ERFOLGREICH_ERSTELLT)
                        .developerMessage(RezeptConstants.REZEPT_ERFOLGREICH_ERSTELLT)
                        .data(Map.of(RezeptConstants.REZEPT, rezept))
                        .build()
        );
    }

    private static ResponseEntity<HttpResponse> getHttpResponseGetAllEntity(List<Rezept> rezepte) {
        if (rezepte.isEmpty()) {
            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .status(HttpStatus.OK)
                            .statusCode(200)
                            .message("Keine Rezepte gefunden")
                            .data(Collections.emptyMap())
                            .timeStamp(LocalDateTime.now())
                            .developerMessage("getRezepte erfolgreich ausgeführt")
                            .build()
            );
        }
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .status(HttpStatus.OK)
                        .statusCode(200)
                        .message("Rezepte gefunden")
                        .timeStamp(LocalDateTime.now())
                        .developerMessage("getRezepte erfolgreich ausgeführt")
                        .data(Map.of("rezepte", rezepte))
                        .build()
        );
    }

    private boolean isGlutenfrei(RezeptDto request) {
        return "GLUTENFREI".equals(request.getType());
    }

    private boolean isVegetarisch(RezeptDto request) {
        return "VEGETARISCH".equals(request.getType());
    }
}
