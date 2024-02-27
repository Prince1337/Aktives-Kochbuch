package prince.aktiveskochbuch.domain.usecases;

import org.springframework.http.ResponseEntity;
import prince.aktiveskochbuch.domain.dtos.HttpResponse;

public interface GetRezeptUseCase {

    ResponseEntity<HttpResponse> getAllRezepte();
    ResponseEntity<HttpResponse> getRezept(String title);
}
