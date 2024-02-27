package prince.aktiveskochbuch.domain.dtos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import prince.aktiveskochbuch.adapter.db.RezeptRepository;
import prince.aktiveskochbuch.application.RezeptService;
import prince.aktiveskochbuch.domain.models.Rezept;
import prince.aktiveskochbuch.domain.models.StandardRezept;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
class RezeptDtoTest {

    public static final String SOME_TITLE = "someTitle";
    public static final String SOME_REZEPTUR = "someRezeptur";
    public static final String SOME_TAGS = "someTags";
    @Mock
    private RezeptRepository rezeptRepository;
    @InjectMocks
    private RezeptService rezeptService;

    @Test
    void testCreateRezept() {
        RezeptDto rezeptDto = RezeptDto.builder().titel(SOME_TITLE).rezeptur(SOME_REZEPTUR).tags(List.of(SOME_TAGS)).type("STANDARD").build();
        Rezept rezept = new StandardRezept(rezeptDto.getTitel(), rezeptDto.getRezeptur(), rezeptDto.getTags());
        when(rezeptRepository.save(rezept)).thenReturn(rezept);

        ResponseEntity<HttpResponse> responseEntity = rezeptService.createStandardRezept(rezeptDto);

        assertEquals(HttpStatus.OK, Objects.requireNonNull(responseEntity.getBody()).getStatus(), "Status should be OK");
    }

    @Test
    void getTitel() {
        RezeptDto rezeptDto = RezeptDto.builder().titel(SOME_TITLE).rezeptur(SOME_REZEPTUR).tags(List.of(SOME_TAGS)).build();
        assertEquals(SOME_TITLE, rezeptDto.getTitel(), "Title should be 'someTitle'");
    }

    @Test
    void getRezeptur() {
        RezeptDto rezeptDto = RezeptDto.builder().titel(SOME_TITLE).rezeptur(SOME_REZEPTUR).tags(List.of(SOME_TAGS)).build();
        assertEquals(SOME_REZEPTUR, rezeptDto.getRezeptur(), "Rezeptur should be 'someRezeptur'");
    }

    @Test
    void getTags() {
        RezeptDto rezeptDto = RezeptDto.builder().titel(SOME_TITLE).rezeptur(SOME_REZEPTUR).tags(List.of(SOME_TAGS)).build();
        assertEquals(List.of(SOME_TAGS), rezeptDto.getTags(), "Tags should be 'someTags'");
    }

    @Test
    void setTitel() {
        RezeptDto rezeptDto = RezeptDto.builder().titel(SOME_TITLE).rezeptur(SOME_REZEPTUR).tags(List.of(SOME_TAGS)).build();
        rezeptDto.setTitel("newTitle");
        assertEquals("newTitle", rezeptDto.getTitel(), "Title should be 'newTitle'");
    }

    @Test
    void setRezeptur() {
        RezeptDto rezeptDto = RezeptDto.builder().titel(SOME_TITLE).rezeptur(SOME_REZEPTUR).tags(List.of(SOME_TAGS)).build();
        rezeptDto.setRezeptur("newRezeptur");
        assertEquals("newRezeptur", rezeptDto.getRezeptur(), "Rezeptur should be 'newRezeptur'");
    }

    @Test
    void setTags() {
        RezeptDto rezeptDto = RezeptDto.builder().titel(SOME_TITLE).rezeptur(SOME_REZEPTUR).tags(List.of(SOME_TAGS)).build();
        rezeptDto.setTags(List.of("newTags"));
        assertEquals(List.of("newTags"), rezeptDto.getTags(), "Tags should be 'newTags'");
    }

    @Test
    void testConstructor() {
        RezeptDto rezeptDto = new RezeptDto(SOME_TITLE, SOME_REZEPTUR, List.of(SOME_TAGS), "STANDARD");

        assertEquals(SOME_TITLE, rezeptDto.getTitel(), "Title should be 'someTitle'");
        assertEquals(SOME_REZEPTUR, rezeptDto.getRezeptur(), "Rezeptur should");
        assertEquals(List.of(SOME_TAGS), rezeptDto.getTags(), "Tags should be 'someTags'");
    }

}