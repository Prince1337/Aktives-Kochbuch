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
import prince.aktiveskochbuch.domain.models.StandardRezept;

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
        RezeptDto request = getRequest();
        StandardRezept rezept = new StandardRezept(request.getTitel(), request.getRezeptur(), request.getTags());
        when(rezeptRepository.save(rezept)).thenReturn(rezept);

        ResponseEntity<HttpResponse> responseEntity = rezeptService.createStandardRezept(request);
        assertNotNull(Objects.requireNonNull(responseEntity.getBody()).getTimeStamp(), "Timestamp should not be null");
    }



    @Test
    void getStatusCode() {
        RezeptDto request = getRequest();
        StandardRezept rezept = new StandardRezept(request.getTitel(), request.getRezeptur(), request.getTags());
        when(rezeptRepository.save(rezept)).thenReturn(rezept);

        ResponseEntity<HttpResponse> responseEntity = rezeptService.createStandardRezept(request);
        assertEquals(200, Objects.requireNonNull(responseEntity.getBody()).getStatusCode(), "Status code should be 200");
    }

    @Test
    void getStatus() {
        RezeptDto request = getRequest();
        StandardRezept rezept = new StandardRezept(request.getTitel(), request.getRezeptur(), request.getTags());
        when(rezeptRepository.save(rezept)).thenReturn(rezept);

        ResponseEntity<HttpResponse> responseEntity = rezeptService.createStandardRezept(request);
        assertEquals(HttpStatus.OK, Objects.requireNonNull(responseEntity.getBody()).getStatus(), "Status should be OK");
    }

    @Test
    void getMessage() {
        RezeptDto request = getRequest();
        StandardRezept rezept = new StandardRezept(request.getTitel(), request.getRezeptur(), request.getTags());
        when(rezeptRepository.save(rezept)).thenReturn(rezept);

        ResponseEntity<HttpResponse> responseEntity = rezeptService.createStandardRezept(request);
        assertEquals("Rezept erfolgreich erstellt", Objects.requireNonNull(responseEntity.getBody()).getMessage(), "Message should be 'Rezept erfolgreich erstellt'");
    }

    @Test
    void getDeveloperMessage() {
        RezeptDto request = getRequest();
        StandardRezept rezept = new StandardRezept(request.getTitel(), request.getRezeptur(), request.getTags());
        when(rezeptRepository.save(rezept)).thenReturn(rezept);

        ResponseEntity<HttpResponse> responseEntity = rezeptService.createStandardRezept(request);
        assertEquals("Rezept erfolgreich zur Datenbank hinzugefügt", Objects.requireNonNull(responseEntity.getBody()).getDeveloperMessage(), "Developer message should be 'Rezept erfolgreich zur Datenbank hinzugefügt'");
    }

    @Test
    void getData() {
        RezeptDto request = getRequest();
        StandardRezept rezept = new StandardRezept(request.getTitel(), request.getRezeptur(), request.getTags());
        when(rezeptRepository.save(rezept)).thenReturn(rezept);

        ResponseEntity<HttpResponse> responseEntity = rezeptService.createStandardRezept(request);
        assertEquals(rezept, Objects.requireNonNull(responseEntity.getBody()).getData().get("rezept"), "Rezept should be returned");
    }

    private static RezeptDto getRequest() {
        return new RezeptDto(SOME_TITLE, SOME_REZEPTUR, List.of(SOME_TAGS), "StandardRezept");
    }
}