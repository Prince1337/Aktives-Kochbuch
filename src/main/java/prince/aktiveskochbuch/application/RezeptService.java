package prince.aktiveskochbuch.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import prince.aktiveskochbuch.adapter.db.RezeptRepository;
import prince.aktiveskochbuch.application.utils.RezeptConstants;
import prince.aktiveskochbuch.domain.dtos.RezeptDto;
import prince.aktiveskochbuch.domain.models.*;
import prince.aktiveskochbuch.domain.usecases.DeleteRezeptUseCase;
import prince.aktiveskochbuch.domain.usecases.GetRezeptUseCase;
import prince.aktiveskochbuch.domain.usecases.PostRezeptUseCase;
import prince.aktiveskochbuch.domain.usecases.PutRezeptUseCase;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@Service
@RequiredArgsConstructor
@Slf4j
public class RezeptService implements GetRezeptUseCase, PostRezeptUseCase, DeleteRezeptUseCase, PutRezeptUseCase {

    private final RezeptRepository rezeptRepository;

    @Override
    public StandardRezept createStandardRezept(RezeptDto rezeptDto) {
        logRezeptCreation(rezeptDto);
        return createAndSaveStandardRezept(rezeptDto);
    }

    @Override
    public VegetarischesRezept createVegetarischesRezept(RezeptDto rezeptDto) {
        logRezeptCreation(rezeptDto);
        return createAndSaveVegetarischesRezept(rezeptDto);
    }

    @Override
    public GlutenfreiesRezept createGlutenfreiesRezept(RezeptDto rezeptDto) {
        logRezeptCreation(rezeptDto);
        return createAndSaveGlutenfreiesRezept(rezeptDto);
    }

    @Override
    public void deleteRezept(Long id) {
        logRezeptDeletion(id);
        if (isRezeptNotFound(id)) {
            return;
        }
        deleteRezeptById(id);
        logRezeptDeletionExecution();
    }

    @Override
    public List<Rezept> getAllRezepte() {
        List<Rezept> rezepte = findAllRezepte();
        logRezepte(rezepte);
        return rezepte;
    }

    @Override
    public Rezept getRezept(String title) {
        logRezeptRetrieval(title);
        Optional<Rezept> rezept = findRezeptByTitel(title);
        logRezeptRetrievalResult(rezept);

        return rezept.orElse(null);
    }

    @Override
    public Rezept updateRezept(Long id, RezeptDto rezeptDto) {
        logRezeptUpdate(id, rezeptDto);
        Optional<Rezept> existingRezept = findRezeptById(id);
        logRezeptUpdateCheck(existingRezept);

        if (isRezeptNotFound(existingRezept)) {
            return null;
        }

        updateRezeptDetails(rezeptDto, existingRezept);

        logRezeptUpdateExecution(existingRezept);
        return existingRezept.get();
    }

    private void logRezeptCreation(RezeptDto rezeptDto) {
        log.info("RezeptService: createRezept: rezeptDto: {}", rezeptDto);
    }

    private StandardRezept createAndSaveStandardRezept(RezeptDto rezeptDto) {
        StandardRezept rezept = dtoToEntity(rezeptDto);
        saveRezept(rezept);
        return rezept;
    }

    private VegetarischesRezept createAndSaveVegetarischesRezept(RezeptDto rezeptDto) {
        VegetarischesRezept rezept = new VegetarischesRezept(rezeptDto.getTitel(), rezeptDto.getRezeptur(), rezeptDto.getTags());
        saveRezept(rezept);
        return rezept;
    }

    private GlutenfreiesRezept createAndSaveGlutenfreiesRezept(RezeptDto rezeptDto) {
        GlutenfreiesRezept rezept = new GlutenfreiesRezept(rezeptDto.getTitel(), rezeptDto.getRezeptur(), rezeptDto.getTags());
        saveRezept(rezept);
        return rezept;
    }

    private void saveRezept(Rezept rezept) {
        rezeptRepository.save(rezept);
        log.info(RezeptConstants.REZEPT_REPOSITORY_SAVE_REZEPT_WURDE_AUSGEFUEHRT);
    }

    private void logRezeptDeletion(Long id) {
        log.info("RezeptService: deleteRezept: id: {}", id);
    }

    private boolean isRezeptNotFound(Long id) {
        boolean rezeptNotFound = isRezeptNotFound(findRezeptById(id));
        if (rezeptNotFound) {
            logRezeptNotFound();
        }
        return rezeptNotFound;
    }

    private void deleteRezeptById(Long id) {
        rezeptRepository.deleteById(id);
    }

    private void logRezeptDeletionExecution() {
        log.info("rezeptRepository.deleteById(id) wurde ausgeführt");
    }

    private List<Rezept> findAllRezepte() {
        List<Rezept> rezepte = rezeptRepository.findAll();
        return rezepte.isEmpty() ? Collections.emptyList() : rezepte;
    }

    private void logRezepte(List<Rezept> rezepte) {
        log.info("RezeptService: getAllRezepte: rezepte: {}", rezepte);
    }

    private Optional<Rezept> findRezeptByTitel(String title) {
        return rezeptRepository.findByTitel(title);
    }

    private void logRezeptRetrieval(String title) {
        log.info("RezeptService: getRezept: title: {}", title);
    }

    private void logRezeptRetrievalResult(Optional<Rezept> rezept) {
        log.info("RezeptService: getRezept: rezept: {}", rezept);
    }

    private Optional<Rezept> findRezeptById(Long id) {
        return rezeptRepository.findById(id);
    }

    private void logRezeptUpdate(Long id, RezeptDto rezeptDto) {
        log.info("RezeptService: updateRezept: id: {}, rezept: {}", id, rezeptDto);
    }

    private void logRezeptUpdateCheck(Optional<Rezept> existingRezept) {
        if (isRezeptNotFound(existingRezept)) {
            log.info(RezeptConstants.REZEPT_NICHT_GEFUNDEN);
        }
    }

    private void updateRezeptDetails(RezeptDto rezeptDto, Optional<Rezept> existingRezept) {
        if (existingRezept.isPresent()) {
            Rezept updatedRezept = existingRezept.get();
            updatedRezept.setTitel(rezeptDto.getTitel());
            updatedRezept.setRezeptur(rezeptDto.getRezeptur());
            updatedRezept.setTags(rezeptDto.getTags());
            updatedRezept.setType(Type.fromString(rezeptDto.getType()));
        }
    }

    private void logRezeptUpdateExecution(Optional<Rezept> existingRezept) {
        //noinspection OptionalGetWithoutIsPresent
        rezeptRepository.save(existingRezept.get());
        log.info("rezeptRepository.save(existingRezept.get()) wurde ausgeführt");
    }

    private boolean isRezeptNotFound(Optional<Rezept> rezept) {
        boolean rezeptNotFound = rezept.isEmpty();
        if (rezeptNotFound) {
            logRezeptNotFound();
        }
        return rezeptNotFound;
    }

    private void logRezeptNotFound() {
        log.info(RezeptConstants.REZEPT_NICHT_GEFUNDEN);
    }


    private static StandardRezept dtoToEntity(RezeptDto rezeptDto) {
        return new StandardRezept(rezeptDto.getTitel(), rezeptDto.getRezeptur(), rezeptDto.getTags());
    }

}
