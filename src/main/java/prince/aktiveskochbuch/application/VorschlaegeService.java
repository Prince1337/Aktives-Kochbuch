package prince.aktiveskochbuch.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import prince.aktiveskochbuch.adapter.db.RezeptRepository;
import prince.aktiveskochbuch.domain.dtos.VorschlaegeDto;
import prince.aktiveskochbuch.domain.models.Rezept;
import prince.aktiveskochbuch.domain.usecases.VorschlaegeGenerierenUseCase;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VorschlaegeService implements VorschlaegeGenerierenUseCase {

    private final RezeptRepository rezeptRepository;

    @Override
    public List<Rezept> generateVorschlaege(VorschlaegeDto vorschlaegeDto) {
        List<Rezept> vorschlaege;

        log.info("VorschlaegeService: generateVorschlaege: vorschlaegeDto: {}", vorschlaegeDto);
        List<Rezept> passendeRezepte = filterRezepteNachTags(vorschlaegeDto.getSelectedTags());
        log.info("VorschlaegeService: generateVorschlaege: passende Rezepte: {}", passendeRezepte);

        int anzahlVorschlaege = Math.min(vorschlaegeDto.getNumberOfSuggestions(), passendeRezepte.size());
        log.info("VorschlaegeService: generateVorschlaege: anzahlVorschlaege: {}", anzahlVorschlaege);

        List<Rezept> list = new ArrayList<>();
        for (int i = 0; i < anzahlVorschlaege; i++) {
            Rezept rezept = passendeRezepte.get(i);
            list.add(rezept);
        }
        vorschlaege = list;
        log.info("VorschlaegeService: generateVorschlaege: vorschlaege: {}", vorschlaege);

        return vorschlaege;
    }

    private List<Rezept> filterRezepteNachTags(List<String> selectedTags) {
        List<Rezept> alleRezepte = rezeptRepository.findAll();
        log.info("VorschlaegeService: filterRezepteNachTags: alleRezepte: {}", alleRezepte);
        return alleRezepte.stream()
                .filter(rezept -> rezept.getTags().stream().anyMatch(selectedTags::contains))
                .toList();
    }

}
