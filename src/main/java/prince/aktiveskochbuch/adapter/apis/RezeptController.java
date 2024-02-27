package prince.aktiveskochbuch.adapter.apis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import prince.aktiveskochbuch.domain.dtos.HttpResponse;
import prince.aktiveskochbuch.domain.models.Rezept;
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
    public ResponseEntity<HttpResponse> createRezept(@RequestBody Rezept rezept) {
        log.info("RezeptController: createRezept: rezept: {}", rezept);
        return postRezeptUseCase.createRezept(rezept);
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
    public ResponseEntity<HttpResponse> updateRezept(@PathVariable Long id, @RequestBody Rezept rezept) {
        log.info("RezeptController: updateRezept: id: {}, rezept: {}", id, rezept);
        return putRezeptUseCase.updateRezept(id, rezept);
    }

    @DeleteMapping("/rezepte/{id}")
    public ResponseEntity<HttpResponse> deleteRezept(@PathVariable Long id) {
        log.info("RezeptController: deleteRezept: id: {}", id);
        return deleteRezeptUseCase.deleteRezept(id);
    }
}
