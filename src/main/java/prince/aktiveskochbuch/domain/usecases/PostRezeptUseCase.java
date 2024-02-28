package prince.aktiveskochbuch.domain.usecases;

import prince.aktiveskochbuch.domain.dtos.RezeptDto;
import prince.aktiveskochbuch.domain.models.GlutenfreiesRezept;
import prince.aktiveskochbuch.domain.models.StandardRezept;
import prince.aktiveskochbuch.domain.models.VegetarischesRezept;

public interface PostRezeptUseCase {

    StandardRezept createStandardRezept(RezeptDto rezept);
    VegetarischesRezept createVegetarischesRezept(RezeptDto rezept);
    GlutenfreiesRezept createGlutenfreiesRezept(RezeptDto rezept);

}
