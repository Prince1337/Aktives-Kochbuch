package prince.aktiveskochbuch.domain.usecases;

import org.springframework.http.ResponseEntity;
import prince.aktiveskochbuch.domain.dtos.HttpResponse;
import prince.aktiveskochbuch.domain.dtos.RezeptDto;

public interface DeleteRezeptUseCase {

    ResponseEntity<HttpResponse> deleteRezept(Long id);
}
