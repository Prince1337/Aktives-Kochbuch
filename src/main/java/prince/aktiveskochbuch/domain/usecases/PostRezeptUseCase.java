package prince.aktiveskochbuch.domain.usecases;

import org.springframework.http.ResponseEntity;
import prince.aktiveskochbuch.domain.dtos.HttpResponse;
import prince.aktiveskochbuch.domain.dtos.RezeptDto;

public interface PostRezeptUseCase {

    ResponseEntity<HttpResponse> createStandardRezept(RezeptDto rezept);
    ResponseEntity<HttpResponse> createVegetarischesRezept(RezeptDto rezept);
    ResponseEntity<HttpResponse> createGlutenfreiesRezept(RezeptDto rezept);

}
