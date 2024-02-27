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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HttpResponseTest {

    public static final String SOME_TITLE = "someTitle";
    public static final String SOME_REZEPTUR = "someRezeptur";
    public static final String SOME_TAGS = "someTags";
    @Mock
    private RezeptRepository rezeptRepository;

    @InjectMocks
    private RezeptService rezeptService;

    @Test
    void getTimeStamp() {
        Rezept rezept = new Rezept(1L, SOME_TITLE, SOME_REZEPTUR, List.of(SOME_TAGS));
        when(rezeptRepository.save(rezept)).thenReturn(rezept);

        ResponseEntity<HttpResponse> responseEntity = rezeptService.createRezept(rezept);
        assertNotNull(Objects.requireNonNull(responseEntity.getBody()).getTimeStamp(), "Timestamp should not be null");
    }

    @Test
    void getStatusCode() {
        Rezept rezept = new Rezept(1L, SOME_TITLE, SOME_REZEPTUR, List.of(SOME_TAGS));
        when(rezeptRepository.save(rezept)).thenReturn(rezept);

        ResponseEntity<HttpResponse> responseEntity = rezeptService.createRezept(rezept);
        assertEquals(200, Objects.requireNonNull(responseEntity.getBody()).getStatusCode(), "Status code should be 200");
    }

    @Test
    void getStatus() {
        Rezept rezept = new Rezept(1L, SOME_TITLE, SOME_REZEPTUR, List.of(SOME_TAGS));
        when(rezeptRepository.save(rezept)).thenReturn(rezept);

        ResponseEntity<HttpResponse> responseEntity = rezeptService.createRezept(rezept);
        assertEquals(HttpStatus.OK, Objects.requireNonNull(responseEntity.getBody()).getStatus(), "Status should be OK");
    }

    @Test
    void getMessage() {
        Rezept rezept = new Rezept(1L, SOME_TITLE, SOME_REZEPTUR, List.of(SOME_TAGS));
        when(rezeptRepository.save(rezept)).thenReturn(rezept);

        ResponseEntity<HttpResponse> responseEntity = rezeptService.createRezept(rezept);
        assertEquals("Rezept erfolgreich erstellt", Objects.requireNonNull(responseEntity.getBody()).getMessage(), "Message should be 'Rezept erfolgreich erstellt'");
    }

    @Test
    void getDeveloperMessage() {
        Rezept rezept = new Rezept(1L, SOME_TITLE, SOME_REZEPTUR, List.of(SOME_TAGS));
        when(rezeptRepository.save(rezept)).thenReturn(rezept);

        ResponseEntity<HttpResponse> responseEntity = rezeptService.createRezept(rezept);
        assertEquals("Rezept erfolgreich zur Datenbank hinzugefügt", Objects.requireNonNull(responseEntity.getBody()).getDeveloperMessage(), "Developer message should be 'Rezept erfolgreich zur Datenbank hinzugefügt'");
    }

    @Test
    void getData() {
        Rezept rezept = new Rezept(1L, SOME_TITLE, SOME_REZEPTUR, List.of(SOME_TAGS));
        when(rezeptRepository.save(rezept)).thenReturn(rezept);

        ResponseEntity<HttpResponse> responseEntity = rezeptService.createRezept(rezept);
        assertEquals(rezept, Objects.requireNonNull(responseEntity.getBody()).getData().get("rezept"), "Rezept should be returned");
    }
}