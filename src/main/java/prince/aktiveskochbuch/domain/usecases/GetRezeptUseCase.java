package prince.aktiveskochbuch.domain.usecases;

import prince.aktiveskochbuch.domain.models.Rezept;

import java.util.List;

public interface GetRezeptUseCase {

    List<Rezept> getAllRezepte();
    Rezept getRezept(String title);
}
