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

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RezeptDtoTest {

    @Mock
    private RezeptRepository rezeptRepository;
    @InjectMocks
    private RezeptService rezeptService;

    @Test
    public void testCreateRezept() {
        RezeptDto rezeptDto = RezeptDto.builder().titel("someTitle").rezeptur("someRezeptur").tags(List.of("someTags")).build();
        Rezept rezept = Rezept.builder().titel(rezeptDto.getTitel()).rezeptur(rezeptDto.getRezeptur()).tags(rezeptDto.getTags()).build();
        when(rezeptRepository.save(rezept)).thenReturn(rezept);

        ResponseEntity<HttpResponse> responseEntity = rezeptService.createRezept(rezept);

        assertEquals(HttpStatus.OK, Objects.requireNonNull(responseEntity.getBody()).getStatus());
        // Add more assertions based on the expected behavior
    }

    @Test
    void getTitel() {
        RezeptDto rezeptDto = RezeptDto.builder().titel("someTitle").rezeptur("someRezeptur").tags(List.of("someTags")).build();
        assertEquals("someTitle", rezeptDto.getTitel());
    }

    @Test
    void getRezeptur() {
        RezeptDto rezeptDto = RezeptDto.builder().titel("someTitle").rezeptur("someRezeptur").tags(List.of("someTags")).build();
        assertEquals("someRezeptur", rezeptDto.getRezeptur());
    }

    @Test
    void getTags() {
        RezeptDto rezeptDto = RezeptDto.builder().titel("someTitle").rezeptur("someRezeptur").tags(List.of("someTags")).build();
        assertEquals(List.of("someTags"), rezeptDto.getTags());
    }

    @Test
    void setTitel() {
        RezeptDto rezeptDto = RezeptDto.builder().titel("someTitle").rezeptur("someRezeptur").tags(List.of("someTags")).build();
        rezeptDto.setTitel("newTitle");
        assertEquals("newTitle", rezeptDto.getTitel());
    }

    @Test
    void setRezeptur() {
        RezeptDto rezeptDto = RezeptDto.builder().titel("someTitle").rezeptur("someRezeptur").tags(List.of("someTags")).build();
        rezeptDto.setRezeptur("newRezeptur");
        assertEquals("newRezeptur", rezeptDto.getRezeptur());
    }

    @Test
    void setTags() {
        RezeptDto rezeptDto = RezeptDto.builder().titel("someTitle").rezeptur("someRezeptur").tags(List.of("someTags")).build();
        rezeptDto.setTags(List.of("newTags"));
        assertEquals(List.of("newTags"), rezeptDto.getTags());
    }

    @Test
    void testConstructor() {
        RezeptDto rezeptDto = new RezeptDto("someTitle", "someRezeptur", List.of("someTags"));

        assertEquals("someTitle", rezeptDto.getTitel());
        assertEquals("someRezeptur", rezeptDto.getRezeptur());
        assertEquals(List.of("someTags"), rezeptDto.getTags());
    }
}