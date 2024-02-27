package prince.aktiveskochbuch.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import prince.aktiveskochbuch.adapter.db.RezeptRepository;
import prince.aktiveskochbuch.domain.dtos.HttpResponse;
import prince.aktiveskochbuch.domain.dtos.RezeptDto;
import prince.aktiveskochbuch.domain.models.*;
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
    public ResponseEntity<HttpResponse> createStandardRezept(RezeptDto rezeptDto) {
        log.info("RezeptService: createRezept: rezeptDto: {}", rezeptDto);
        StandardRezept rezept = dtoToEntity(rezeptDto);
        rezeptRepository.save(rezept);
        log.info(RezeptConstants.REZEPT_REPOSITORY_SAVE_REZEPT_WURDE_AUSGEFUEHRT);

        return getResponseForCreateStandard(rezept);
    }

    @Override
    public ResponseEntity<HttpResponse> createVegetarischesRezept(RezeptDto rezeptDto) {
        log.info("RezeptService: createRezept: rezept: {}", rezeptDto);
        VegetarischesRezept rezept = new VegetarischesRezept(rezeptDto.getTitel(), rezeptDto.getRezeptur(), rezeptDto.getTags());
        rezeptRepository.save(rezept);
        log.info(RezeptConstants.REZEPT_REPOSITORY_SAVE_REZEPT_WURDE_AUSGEFUEHRT);

        return getResponseForCreateVegetarisch(rezept);
    }

    @Override
    public ResponseEntity<HttpResponse> createGlutenfreiesRezept(RezeptDto rezeptDto) {
        log.info("RezeptService: createRezept: rezept: {}", rezeptDto);
        GlutenfreiesRezept rezept = new GlutenfreiesRezept(rezeptDto.getTitel(), rezeptDto.getRezeptur(), rezeptDto.getTags());
        rezeptRepository.save(rezept);
        log.info(RezeptConstants.REZEPT_REPOSITORY_SAVE_REZEPT_WURDE_AUSGEFUEHRT);

        return getResponseForCreateGlutenfrei(rezept);
    }

    @Override
    public ResponseEntity<HttpResponse> deleteRezept(Long id) {
        log.info("RezeptService: deleteRezept: id: {}", id);
        if(notFound(id)) {
            log.info(RezeptConstants.REZEPT_NICHT_GEFUNDEN);
            return getNotFoundResponse();
        }
        rezeptRepository.deleteById(id);
        log.info("rezeptRepository.deleteById(id) wurde ausgeführt");

        return getDeleteResponse();

    }

    @Override
    public ResponseEntity<HttpResponse> getAllRezepte() {
        List<Rezept> rezepte = rezeptRepository.findAll();
        log.info("RezeptService: getAllRezepte: rezepte: {}", rezepte);
        if(rezepte.isEmpty()) {
            return getEmptyRezepteListResponse();
        }
        return getAllResponse(rezepte);
    }

    @Override
    public ResponseEntity<HttpResponse> getRezept(String title) {
        log.info("RezeptService: getRezept: title: {}", title);
        Optional<Rezept> rezept = rezeptRepository.findByTitel(title);
        log.info("RezeptService: getRezept: rezept: {}", rezept);

        if (notFound(rezept)) {
            log.info(RezeptConstants.REZEPT_NICHT_GEFUNDEN);
            return getNotFoundResponse();
        }

        return getRezeptResponse(rezept);
    }



    @Override
    public ResponseEntity<HttpResponse> updateRezept(Long id, RezeptDto rezeptDto) {
        log.info("RezeptService: updateRezept: id: {}, rezept: {}", id, rezeptDto);
        Optional<Rezept> newRezept = rezeptRepository.findById(id);
        log.info("rezeptRepository.findById(id) wurde ausgeführt");

        if (notFound(newRezept)) {
            log.info(RezeptConstants.REZEPT_NICHT_GEFUNDEN);
            return getNotFoundResponse();
        }

        dtoToNewRezept(rezeptDto, newRezept);

        log.info("RezeptService: updateRezept: newRezept: {}", newRezept);
        //noinspection OptionalGetWithoutIsPresent
        rezeptRepository.save(newRezept.get());
        log.info("rezeptRepository.save(newRezept.get()) wurde ausgeführt");

        return getUpdateResponse(newRezept);
    }

    private static ResponseEntity<HttpResponse> getUpdateResponse(Optional<Rezept> newRezept) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .data(Map.of(RezeptConstants.REZEPT_LITERAL, newRezept))
                        .message("Rezept erfolgreich aktualisiert")
                        .developerMessage("Rezept erfolgreich in der Datenbank aktualisiert")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .timeStamp(LocalDateTime.now())
                        .build());
    }

    private static void dtoToNewRezept(RezeptDto rezeptDto, Optional<Rezept> newRezept) {
        if(notFound(newRezept))
            return;
        if(newRezept.isPresent()) {
            newRezept.get().setTitel(rezeptDto.getTitel());
            newRezept.get().setRezeptur(rezeptDto.getRezeptur());
            newRezept.get().setTags(rezeptDto.getTags());
            newRezept.get().setType(Type.fromString(rezeptDto.getType()));
        }
    }

    private static StandardRezept dtoToEntity(RezeptDto rezeptDto) {
        return new StandardRezept(rezeptDto.getTitel(), rezeptDto.getRezeptur(), rezeptDto.getTags());
    }

    private static ResponseEntity<HttpResponse> getResponseForCreateStandard(StandardRezept rezept) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .data(Map.of(RezeptConstants.REZEPT_LITERAL, rezept))
                        .message(RezeptConstants.REZEPT_ERFOLGREICH_ERSTELLT)
                        .developerMessage(RezeptConstants.REZEPT_ERFOLGREICH_ZUR_DATENBANK_HINZUGEFUEGT)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .timeStamp(LocalDateTime.now())
                        .build());
    }

    private static ResponseEntity<HttpResponse> getResponseForCreateVegetarisch(VegetarischesRezept rezept) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .data(Map.of(RezeptConstants.REZEPT_LITERAL, rezept))
                        .message(RezeptConstants.REZEPT_ERFOLGREICH_ERSTELLT)
                        .developerMessage(RezeptConstants.REZEPT_ERFOLGREICH_ZUR_DATENBANK_HINZUGEFUEGT)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .timeStamp(LocalDateTime.now())
                        .build());
    }

    private static ResponseEntity<HttpResponse> getResponseForCreateGlutenfrei(GlutenfreiesRezept rezept) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .data(Map.of(RezeptConstants.REZEPT_LITERAL, rezept))
                        .message(RezeptConstants.REZEPT_ERFOLGREICH_ERSTELLT)
                        .developerMessage(RezeptConstants.REZEPT_ERFOLGREICH_ZUR_DATENBANK_HINZUGEFUEGT)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .timeStamp(LocalDateTime.now())
                        .build());
    }

    private static ResponseEntity<HttpResponse> getDeleteResponse() {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .message("Rezept erfolgreich gelöscht")
                        .developerMessage("Rezept erfolgreich aus der Datenbank gelöscht")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .timeStamp(LocalDateTime.now())
                        .build());
    }

    private static ResponseEntity<HttpResponse> getNotFoundResponse() {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .message(RezeptConstants.REZEPT_NICHT_GEFUNDEN)
                        .developerMessage(RezeptConstants.REZEPT_NICHT_IN_DER_DATENBANK_GEFUNDEN)
                        .status(HttpStatus.NOT_FOUND)
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .timeStamp(LocalDateTime.now())
                        .build());
    }

    private boolean notFound(Long id) {
        return notFound(rezeptRepository.findById(id));
    }

    private static ResponseEntity<HttpResponse> getAllResponse(List<Rezept> rezepte) {
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

    private static ResponseEntity<HttpResponse> getEmptyRezepteListResponse() {
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

    private static ResponseEntity<HttpResponse> getRezeptResponse(Optional<Rezept> rezept) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .data(Map.of(RezeptConstants.REZEPT_LITERAL, rezept))
                        .message("Rezept erfolgreich abgerufen")
                        .developerMessage("Rezept erfolgreich aus der Datenbank abgerufen")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .timeStamp(LocalDateTime.now())
                        .build());
    }

    private static boolean notFound(Optional<Rezept> rezept) {
        return rezept.isEmpty();
    }
}
