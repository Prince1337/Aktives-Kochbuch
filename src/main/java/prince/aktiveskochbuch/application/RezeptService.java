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

@Service
@RequiredArgsConstructor
@Slf4j
public class RezeptService implements GetRezeptUseCase, PostRezeptUseCase, DeleteRezeptUseCase, PutRezeptUseCase {

    private final RezeptRepository rezeptRepository;

    @Override
    public StandardRezept createStandardRezept(RezeptDto rezeptDto) {
        log.info("RezeptService: createRezept: rezeptDto: {}", rezeptDto);
        StandardRezept rezept = dtoToEntity(rezeptDto);
        rezept = rezeptRepository.save(rezept);
        log.info(RezeptConstants.REZEPT_REPOSITORY_SAVE_REZEPT_WURDE_AUSGEFUEHRT);

        return rezept;
    }

    @Override
    public VegetarischesRezept createVegetarischesRezept(RezeptDto rezeptDto) {
        log.info("RezeptService: createRezept: rezept: {}", rezeptDto);
        VegetarischesRezept rezept = new VegetarischesRezept(rezeptDto.getTitel(), rezeptDto.getRezeptur(), rezeptDto.getTags());
        rezeptRepository.save(rezept);
        log.info(RezeptConstants.REZEPT_REPOSITORY_SAVE_REZEPT_WURDE_AUSGEFUEHRT);

        return rezept;
    }

    @Override
    public GlutenfreiesRezept createGlutenfreiesRezept(RezeptDto rezeptDto) {
        log.info("RezeptService: createRezept: rezept: {}", rezeptDto);
        GlutenfreiesRezept rezept = new GlutenfreiesRezept(rezeptDto.getTitel(), rezeptDto.getRezeptur(), rezeptDto.getTags());
        rezeptRepository.save(rezept);
        log.info(RezeptConstants.REZEPT_REPOSITORY_SAVE_REZEPT_WURDE_AUSGEFUEHRT);

        return rezept;
    }

    @Override
    public void deleteRezept(Long id) {
        log.info("RezeptService: deleteRezept: id: {}", id);
        if (rezeptRepository.findById(id).isEmpty()) {
            log.info(RezeptConstants.REZEPT_NICHT_GEFUNDEN);
            return;
        }
        rezeptRepository.deleteById(id);
        log.info("rezeptRepository.deleteById(id) wurde ausgeführt");
    }

    @Override
    public List<Rezept> getAllRezepte() {
        List<Rezept> rezepte = rezeptRepository.findAll();
        log.info("RezeptService: getAllRezepte: rezepte: {}", rezepte);
        if(rezepte.isEmpty()) {
            return Collections.emptyList();
        }
        return rezepte;
    }

    @Override
    public Rezept getRezept(String title) {
        log.info("RezeptService: getRezept: title: {}", title);
        Optional<Rezept> rezept = rezeptRepository.findByTitel(title);
        log.info("RezeptService: getRezept: rezept: {}", rezept);

        if (rezept.isEmpty()) {
            log.info(RezeptConstants.REZEPT_NICHT_GEFUNDEN);
            return null;
        }

        return rezept.get();
    }



    @Override
    public Rezept updateRezept(Long id, RezeptDto rezeptDto) {
        log.info("RezeptService: updateRezept: id: {}, rezept: {}", id, rezeptDto);
        Optional<Rezept> newRezept = rezeptRepository.findById(id);
        log.info("rezeptRepository.findById(id) wurde ausgeführt");

        if (notFound(newRezept)) {
            log.info(RezeptConstants.REZEPT_NICHT_GEFUNDEN);
            return null;
        }

        dtoToNewRezept(rezeptDto, newRezept);

        log.info("RezeptService: updateRezept: newRezept: {}", newRezept);
        //noinspection OptionalGetWithoutIsPresent
        rezeptRepository.save(newRezept.get());
        log.info("rezeptRepository.save(newRezept.get()) wurde ausgeführt");

        return newRezept.get();
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

    private static boolean notFound(Optional<Rezept> rezept) {
        return rezept.isEmpty();
    }
}
