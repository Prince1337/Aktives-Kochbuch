package prince.aktiveskochbuch.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import prince.aktiveskochbuch.adapter.db.RezeptRepository;
import prince.aktiveskochbuch.domain.dtos.HttpResponse;
import prince.aktiveskochbuch.domain.models.Rezept;
import prince.aktiveskochbuch.domain.usecases.DeleteRezeptUseCase;
import prince.aktiveskochbuch.domain.usecases.GetRezeptUseCase;
import prince.aktiveskochbuch.domain.usecases.PostRezeptUseCase;
import prince.aktiveskochbuch.domain.usecases.PutRezeptUseCase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RezeptService implements GetRezeptUseCase, PostRezeptUseCase, DeleteRezeptUseCase, PutRezeptUseCase {

    private final RezeptRepository rezeptRepository;

    @Override
    public ResponseEntity<HttpResponse> createRezept(Rezept rezept) {
        log.info("RezeptService: createRezept: rezept: {}", rezept);
        rezeptRepository.save(rezept);
        log.info("rezeptRepository.save(rezept) wurde ausgeführt");

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .data(Map.of("rezept", rezept))
                        .message("Rezept erfolgreich erstellt")
                        .developerMessage("Rezept erfolgreich zur Datenbank hinzugefügt")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .timeStamp(LocalDateTime.now())
                        .build());
    }


    @Override
    public ResponseEntity<HttpResponse> deleteRezept(Long id) {
        log.info("RezeptService: deleteRezept: id: {}", id);
        if(rezeptRepository.findById(id).isEmpty()) {
            log.info("Rezept nicht gefunden");
            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .message("Rezept nicht gefunden")
                            .developerMessage("Rezept nicht in der Datenbank gefunden")
                            .status(HttpStatus.NOT_FOUND)
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .timeStamp(LocalDateTime.now())
                            .build());
        }
        rezeptRepository.deleteById(id);
        log.info("rezeptRepository.deleteById(id) wurde ausgeführt");

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .message("Rezept erfolgreich gelöscht")
                        .developerMessage("Rezept erfolgreich aus der Datenbank gelöscht")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .timeStamp(LocalDateTime.now())
                        .build());

    }

    @Override
    public ResponseEntity<HttpResponse> getAllRezepte() {
        List<Rezept> rezepte = rezeptRepository.findAll();
        log.info("RezeptService: getAllRezepte: rezepte: {}", rezepte);
        if(rezepte.isEmpty()) {
            log.info("Keine Rezepte gefunden");
            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .message("Keine Rezepte gefunden")
                            .developerMessage("Keine Rezepte in der Datenbank gefunden")
                            .status(HttpStatus.NOT_FOUND)
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .timeStamp(LocalDateTime.now())
                            .build());
        }
        return ResponseEntity.ok(
                        HttpResponse.builder()
                                .data(Map.of("rezepte", rezepte))
                                .message("Rezepte erfolgreich abgerufen")
                                .developerMessage("Rezepte erfolgreich aus der Datenbank abgerufen")
                                .status(HttpStatus.OK)
                                .statusCode(HttpStatus.OK.value())
                                .timeStamp(LocalDateTime.now())
                                .build()
        );
    }

    @Override
    public ResponseEntity<HttpResponse> getRezept(String title) {
        log.info("RezeptService: getRezept: title: {}", title);
        Optional<Rezept> rezept = rezeptRepository.findByTitel(title);
        log.info("rezeptRepository.findByTitel(title) wurde ausgeführt");
        log.info("RezeptService: getRezept: rezept: {}", rezept);
        if (rezept.isEmpty()) {
            log.info("Rezept nicht gefunden");
            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .message("Rezept nicht gefunden")
                            .developerMessage("Rezept nicht in der Datenbank gefunden")
                            .status(HttpStatus.NOT_FOUND)
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .timeStamp(LocalDateTime.now())
                            .build());
        }

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .data(Map.of("rezept", rezept))
                        .message("Rezept erfolgreich abgerufen")
                        .developerMessage("Rezept erfolgreich aus der Datenbank abgerufen")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .timeStamp(LocalDateTime.now())
                        .build());
    }

    @Override
    public ResponseEntity<HttpResponse> updateRezept(Long id, Rezept rezept) {
        log.info("RezeptService: updateRezept: id: {}, rezept: {}", id, rezept);
        Optional<Rezept> newRezept = rezeptRepository.findById(id);
        log.info("rezeptRepository.findById(id) wurde ausgeführt");
        if (newRezept.isEmpty()) {
            log.info("Rezept nicht gefunden");
            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .message("Rezept nicht gefunden")
                            .developerMessage("Rezept nicht in der Datenbank gefunden")
                            .status(HttpStatus.NOT_FOUND)
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .timeStamp(LocalDateTime.now())
                            .build());
        }

        newRezept.get().setTitel(rezept.getTitel());
        newRezept.get().setRezeptur(rezept.getRezeptur());
        newRezept.get().setTags(rezept.getTags());

        log.info("RezeptService: updateRezept: newRezept: {}", newRezept);
        rezeptRepository.save(newRezept.get());
        log.info("rezeptRepository.save(newRezept.get()) wurde ausgeführt");

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .data(Map.of("rezept", newRezept))
                        .message("Rezept erfolgreich aktualisiert")
                        .developerMessage("Rezept erfolgreich in der Datenbank aktualisiert")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .timeStamp(LocalDateTime.now())
                        .build());
    }
}
