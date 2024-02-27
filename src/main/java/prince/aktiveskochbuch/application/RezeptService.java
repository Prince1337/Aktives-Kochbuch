package prince.aktiveskochbuch.application;

import lombok.RequiredArgsConstructor;
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
public class RezeptService implements GetRezeptUseCase, PostRezeptUseCase, DeleteRezeptUseCase, PutRezeptUseCase {

    private final RezeptRepository rezeptRepository;

    @Override
    public ResponseEntity<HttpResponse> createRezept(Rezept rezept) {
        rezept = rezeptRepository.save(rezept);

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
        if(rezeptRepository.findById(id).isEmpty()) {
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
        if(rezepte.isEmpty()) {
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
        Optional<Rezept> rezept = rezeptRepository.findByTitel(title);
        if (rezept.isEmpty()) {
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
        Optional<Rezept> newRezept = rezeptRepository.findById(id);
        if (newRezept.isEmpty()) {
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

        rezeptRepository.save(newRezept.get());

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
