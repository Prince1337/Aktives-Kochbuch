package prince.aktiveskochbuch.domain.usecases;

import org.springframework.http.ResponseEntity;
import prince.aktiveskochbuch.domain.dtos.HttpResponse;

public interface DeleteRezeptUseCase {

    ResponseEntity<HttpResponse> deleteRezept(Long id);
}
