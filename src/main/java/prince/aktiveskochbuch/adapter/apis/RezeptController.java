package prince.aktiveskochbuch.adapter.apis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import prince.aktiveskochbuch.application.RezeptConstants;
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

    private final GetRezeptUseCase getRezeptUseCase;
    private final PostRezeptUseCase postRezeptUseCase;
    private final PutRezeptUseCase putRezeptUseCase;
    private final DeleteRezeptUseCase deleteRezeptUseCase;


    @PostMapping("/rezepte")
    public ResponseEntity<HttpResponse> createRezept(@RequestBody RezeptDto rezeptRequest) {
        log.info("RezeptController: createRezept: rezept: {}", rezeptRequest);
        return getHttpResponseEntity(rezeptRequest);
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

    private ResponseEntity<HttpResponse> getHttpResponseEntity(RezeptDto rezeptRequest) {
        if(isVegetarisch(rezeptRequest)){
            log.info(RezeptConstants.REZEPT_CONTROLLER_CREATE_REZEPT, rezeptRequest);
            return postRezeptUseCase.createVegetarischesRezept(rezeptRequest);
        }
        else if (isGlutenfrei(rezeptRequest)){
            log.info(RezeptConstants.REZEPT_CONTROLLER_CREATE_REZEPT, rezeptRequest);
            return postRezeptUseCase.createGlutenfreiesRezept(rezeptRequest);
        } else {
            log.info(RezeptConstants.REZEPT_CONTROLLER_CREATE_REZEPT, rezeptRequest);
            return postRezeptUseCase.createStandardRezept(rezeptRequest);
        }
    }

    private boolean isGlutenfrei(RezeptDto request) {
        return "GLUTENFREI".equals(request.getType());
    }

    private boolean isVegetarisch(RezeptDto request) {
        return "VEGETARISCH".equals(request.getType());
    }
}
