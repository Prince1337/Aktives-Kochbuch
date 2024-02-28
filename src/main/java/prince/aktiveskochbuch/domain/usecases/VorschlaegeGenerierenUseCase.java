package prince.aktiveskochbuch.domain.usecases;

import prince.aktiveskochbuch.domain.dtos.VorschlaegeDto;
import prince.aktiveskochbuch.domain.models.Rezept;

import java.util.List;

public interface VorschlaegeGenerierenUseCase {

    List<Rezept> generateVorschlaege(VorschlaegeDto vorschlaegeDto);

}

