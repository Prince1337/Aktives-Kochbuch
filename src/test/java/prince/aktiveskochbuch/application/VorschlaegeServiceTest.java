package prince.aktiveskochbuch.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import prince.aktiveskochbuch.adapter.db.RezeptRepository;
import prince.aktiveskochbuch.domain.dtos.VorschlaegeDto;
import prince.aktiveskochbuch.domain.models.GlutenfreiesRezept;
import prince.aktiveskochbuch.domain.models.Rezept;
import prince.aktiveskochbuch.domain.models.StandardRezept;
import prince.aktiveskochbuch.domain.models.VegetarischesRezept;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
@Tag("UnitTest")
class VorschlaegeServiceTest {

    @Mock
    private RezeptRepository rezeptRepository;

    @InjectMocks
    private VorschlaegeService vorschlaegeService;

    private List<Rezept> rezeptList;

    @BeforeEach
    void setRezeptList(){
        rezeptList = Arrays.asList(
                new StandardRezept("Spaghetti Bolognese", "Spaghetti, Ei, Speck, Parmesan, Pfeffer, Salz", List.of("Italienisch", "Fleisch")),
                new StandardRezept("Pasta Carbonara", "Pasta, Ei, Speck, Parmesan, Pfeffer, Salz", List.of("Südeuropäisch", "Fleisch")),
                new VegetarischesRezept("Vegetarische Lasagne", "Lasagneplatten, Tomatensauce, Gemüse, Bechamelsauce, Käse", List.of("Gesund", "Käse")),
                new VegetarischesRezept("Vegetarische Pizza", "Pizzateig, Tomatensauce, Gemüse, Käse", List.of("Gesund", "Gemüse")),
                new GlutenfreiesRezept("Glutenfreier Kuchen", "Glutenfreies Mehl, Ei, Zucker, Butter", List.of("Kuchen", "Glutenfrei"))
        );
    }

    @Test
    void testGenerateVorschlaege_ShouldReturnCorrectNumberOfSuggestions() {
        // Arrange
        VorschlaegeDto vorschlaegeDto = new VorschlaegeDto();
        vorschlaegeDto.setSelectedTags(List.of("Gesund", "Gemüse"));
        vorschlaegeDto.setNumberOfSuggestions(2);

        when(rezeptRepository.findAll()).thenReturn(rezeptList);

        // Act
        List<Rezept> result = vorschlaegeService.generateVorschlaege(vorschlaegeDto);

        // Assert
        assertEquals(2, result.size(), "Should return 2 suggestions");
        verify(rezeptRepository, times(1)).findAll();
    }

    @Test
    void testGenerateVorschlaege_ShouldReturnEmptyList_WhenNoMatchingTags() {
        // Arrange
        VorschlaegeDto vorschlaegeDto = new VorschlaegeDto();
        vorschlaegeDto.setSelectedTags(Arrays.asList("Tag1", "Tag2", "Tag3"));
        vorschlaegeDto.setNumberOfSuggestions(2);

        when(rezeptRepository.findAll()).thenReturn(rezeptList);

        // Act
        List<Rezept> result = vorschlaegeService.generateVorschlaege(vorschlaegeDto);

        // Assert
        assertEquals(0, result.size(), "Should return empty list");
        verify(rezeptRepository, times(1)).findAll();
    }


}