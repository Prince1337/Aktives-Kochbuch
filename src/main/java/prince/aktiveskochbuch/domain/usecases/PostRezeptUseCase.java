package prince.aktiveskochbuch.domain.usecases;

import org.springframework.http.ResponseEntity;
import prince.aktiveskochbuch.domain.dtos.HttpResponse;
import prince.aktiveskochbuch.domain.models.Rezept;

public interface PostRezeptUseCase {

    ResponseEntity<HttpResponse> createRezept(Rezept rezept);
}
