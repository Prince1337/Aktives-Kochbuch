package prince.aktiveskochbuch.adapter.apis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import prince.aktiveskochbuch.domain.dtos.HttpResponse;
import prince.aktiveskochbuch.domain.dtos.RezeptDto;
import prince.aktiveskochbuch.domain.usecases.DeleteRezeptUseCase;
import prince.aktiveskochbuch.domain.usecases.GetRezeptUseCase;
import prince.aktiveskochbuch.domain.usecases.PostRezeptUseCase;
import prince.aktiveskochbuch.domain.usecases.PutRezeptUseCase;


@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
public class RezeptController {

    public static final String REZEPT_CONTROLLER_CREATE_REZEPT_REZEPT = "RezeptController: createRezept: rezept: {}";
    private final GetRezeptUseCase getRezeptUseCase;
    private final PostRezeptUseCase postRezeptUseCase;
    private final PutRezeptUseCase putRezeptUseCase;
    private final DeleteRezeptUseCase deleteRezeptUseCase;


    @PostMapping("/rezepte")
    public ResponseEntity<HttpResponse> createRezept(@RequestBody RezeptDto request) {
        if("VEGETARISCH".equals(request.getType())){
            log.info(REZEPT_CONTROLLER_CREATE_REZEPT_REZEPT, request);
            return postRezeptUseCase.createVegetarischesRezept(request);
        }
        else if ("GLUTENFREI".equals(request.getType())){
            log.info(REZEPT_CONTROLLER_CREATE_REZEPT_REZEPT, request);
            return postRezeptUseCase.createGlutenfreiesRezept(request);
        }
        else if ("STANDARD".equals(request.getType())){
            log.info(REZEPT_CONTROLLER_CREATE_REZEPT_REZEPT, request);
            return postRezeptUseCase.createStandardRezept(request);
        }

        log.info(REZEPT_CONTROLLER_CREATE_REZEPT_REZEPT, request);
        return postRezeptUseCase.createStandardRezept(request);
    }

    @GetMapping("/rezepte")
    public ResponseEntity<HttpResponse> getRezepte() {
        log.info("RezeptController: getRezepte");
        return getRezeptUseCase.getAllRezepte();
    }

    @GetMapping("/rezepte/{titel}")
    public ResponseEntity<HttpResponse> getRezept(@PathVariable String titel) {
        log.info("RezeptController: getRezept: titel: {}", titel);
        return getRezeptUseCase.getRezept(titel);
    }

    @PutMapping("/rezepte/{id}")
    public ResponseEntity<HttpResponse> updateRezept(@PathVariable Long id, @RequestBody RezeptDto request) {
        log.info("RezeptController: updateRezept: id: {}, rezept: {}", id, request);
        return putRezeptUseCase.updateRezept(id, request);
    }

    @DeleteMapping("/rezepte/{id}")
    public ResponseEntity<HttpResponse> deleteRezept(@PathVariable Long id) {
        log.info("RezeptController: deleteRezept: id: {}", id);
        return deleteRezeptUseCase.deleteRezept(id);
    }
}
