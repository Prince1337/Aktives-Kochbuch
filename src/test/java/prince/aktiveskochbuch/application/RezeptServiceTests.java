package prince.aktiveskochbuch.application;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import prince.aktiveskochbuch.adapter.db.RezeptRepository;
import prince.aktiveskochbuch.domain.dtos.RezeptDto;
import prince.aktiveskochbuch.domain.models.GlutenfreiesRezept;
import prince.aktiveskochbuch.domain.models.Rezept;
import prince.aktiveskochbuch.domain.models.StandardRezept;
import prince.aktiveskochbuch.domain.models.VegetarischesRezept;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static prince.aktiveskochbuch.application.utils.RezeptConstants.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
@Tag("UnitTest")
class RezeptServiceTests {

    private final RezeptDto rezeptDto = new RezeptDto("titel", REZEPTUR, List.of(TAG_1, TAG_2), "Vegetarisch");
    @Mock
    private RezeptRepository rezeptRepository;

    @InjectMocks
    private RezeptService rezeptService;

    @Test
    void testCreateStandardRezept() {
        RezeptDto rezeptDto = new RezeptDto(TITEL, "Rezeptur", List.of("Tag1", "Tag2"), "Standard");
        StandardRezept expectedRezept = new StandardRezept(rezeptDto.getTitel(), rezeptDto.getRezeptur(), rezeptDto.getTags());
        when(rezeptRepository.save(any(StandardRezept.class))).thenReturn(expectedRezept);

        StandardRezept result = rezeptService.createStandardRezept(rezeptDto);

        assertEquals(expectedRezept, result, REZEPT_SHOULD_BE_THE_SAME_AS_EXPECTED);
        verify(rezeptRepository, times(1)).save(any(StandardRezept.class));
    }

    @Test
    void testCreateVegetarischesRezept() {
        VegetarischesRezept expectedRezept = new VegetarischesRezept(rezeptDto.getTitel(), rezeptDto.getRezeptur(), rezeptDto.getTags());
        when(rezeptRepository.save(any(VegetarischesRezept.class))).thenReturn(expectedRezept);

        VegetarischesRezept result = rezeptService.createVegetarischesRezept(rezeptDto);

        assertEquals(expectedRezept, result, REZEPT_SHOULD_BE_THE_SAME_AS_EXPECTED);
        verify(rezeptRepository, times(1)).save(any(VegetarischesRezept.class));
    }

    @Test
    void testCreateGlutenfreiesRezept() {
        RezeptDto rezeptDto = new RezeptDto(TITEL, "Rezeptur", List.of("Tag1", "Tag2"), "Glutenfrei");

        GlutenfreiesRezept expectedRezept = new GlutenfreiesRezept(rezeptDto.getTitel(), rezeptDto.getRezeptur(), rezeptDto.getTags());
        when(rezeptRepository.save(any(GlutenfreiesRezept.class))).thenReturn(expectedRezept);

        GlutenfreiesRezept result = rezeptService.createGlutenfreiesRezept(rezeptDto);

        assertEquals(expectedRezept, result, REZEPT_SHOULD_BE_THE_SAME_AS_EXPECTED);
        verify(rezeptRepository, times(1)).save(any(GlutenfreiesRezept.class));
    }

    @Test
    void testDeleteRezept() {
        Long id = 1L;

        when(rezeptRepository.findById(id)).thenReturn(Optional.of(new StandardRezept()));

        rezeptService.deleteRezept(id);

        verify(rezeptRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetAllRezepte() {
        List<Rezept> expectedRezepte = List.of(new StandardRezept(TITEL, "Rezeptur", List.of("Tag1", "Tag2")));
        when(rezeptRepository.findAll()).thenReturn(expectedRezepte);

        List<Rezept> result = rezeptService.getAllRezepte();

        assertEquals(expectedRezepte, result, "Rezepte should be the same as expected");
    }

    @Test
    void testGetRezept() {
        String title = "TestRezept";
        Optional<Rezept> expectedRezept = Optional.of(new StandardRezept());
        when(rezeptRepository.findByTitel(title)).thenReturn(expectedRezept);

        Rezept result = rezeptService.getRezept(title);

        assertEquals(expectedRezept.get(), result, REZEPT_SHOULD_BE_THE_SAME_AS_EXPECTED);
    }

    @Test
    void testUpdateRezept() {
        Long id = 1L;

        Optional<Rezept> existingRezept = Optional.of(new StandardRezept());
        when(rezeptRepository.findById(id)).thenReturn(existingRezept);
        when(rezeptRepository.save(any(StandardRezept.class))).thenReturn(new StandardRezept());

        Rezept result = rezeptService.updateRezept(id, rezeptDto);

        assertEquals(existingRezept.get(), result, REZEPT_SHOULD_BE_THE_SAME_AS_EXPECTED);
        verify(rezeptRepository, times(1)).findById(id);
        verify(rezeptRepository, times(1)).save(any(StandardRezept.class));
    }


}
