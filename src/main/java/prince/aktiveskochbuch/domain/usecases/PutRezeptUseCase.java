package prince.aktiveskochbuch.domain.usecases;

import prince.aktiveskochbuch.domain.dtos.RezeptDto;
import prince.aktiveskochbuch.domain.models.Rezept;

public interface PutRezeptUseCase {

    Rezept updateRezept(Long id, RezeptDto rezept);
}
