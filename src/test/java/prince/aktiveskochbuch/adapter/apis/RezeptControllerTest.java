package prince.aktiveskochbuch.adapter.apis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import prince.aktiveskochbuch.application.RezeptService;
import prince.aktiveskochbuch.domain.dtos.HttpResponse;
import prince.aktiveskochbuch.domain.models.Rezept;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RezeptControllerTest {

    private final String someTitle = "someTitle";
    private final String someRezeptur = "someRezeptur";
    private final String someTags = "someTags";
    private final String rezept = "rezept";
    private final String messageStatus200 = "Status code should be 200";
    @Mock
    private RezeptService rezeptService;

    @InjectMocks
    private RezeptController rezeptController;


    @Test
    void testCreateRezept() {
        Rezept rezept = new Rezept(1L, someTitle, someRezeptur, List.of(someTags));
        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.OK)
                .message("Rezept erfolgreich erstellt")
                .data(Map.of(this.rezept, rezept))
                .statusCode(200)
                .timeStamp(LocalDateTime.now())
                .developerMessage("Rezept erfolgreich zur Datenbank hinzugefügt")
                .build();

        when(rezeptService.createRezept(any(Rezept.class))).thenReturn(ResponseEntity.ok(response));

        ResponseEntity<HttpResponse> responseEntity = rezeptController.createRezept(rezept);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), messageStatus200);
        // Add more assertions based on the expected behavior
    }

    @Test
    void testGetRezepte() {
        Rezept rezept = new Rezept(1L, someTitle, someRezeptur, List.of(someTags));
        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.OK)
                .message("Rezepte erfolgreich gefunden")
                .data(Map.of( "rezepte", List.of(rezept)))
                .statusCode(200)
                .timeStamp(LocalDateTime.now())
                .developerMessage("Rezepte erfolgreich aus der Datenbank geholt")
                .build();

        when(rezeptService.getAllRezepte()).thenReturn(ResponseEntity.ok(response));
        assertEquals(HttpStatus.OK, rezeptController.getRezepte().getStatusCode(), messageStatus200);

    }

    @Test
    void testGetRezept() {
        Rezept rezept = new Rezept(1L, someTitle, someRezeptur, List.of(someTags));
        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.OK)
                .message("Rezept erfolgreich gefunden")
                .data(Map.of(this.rezept, rezept))
                .statusCode(200)
                .timeStamp(LocalDateTime.now())
                .developerMessage("Rezept erfolgreich aus der Datenbank geholt")
                .build();

        when(rezeptService.getRezept(someTitle)).thenReturn(ResponseEntity.ok(response));
        assertEquals(HttpStatus.OK, rezeptController.getRezept(someTitle).getStatusCode(), messageStatus200);
    }

    @Test
    void testUpdateRezept() {

        Rezept newRezept = new Rezept(1L, "newTitle", "newRezeptur", List.of("newTags"));

        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.OK)
                .message("Rezept erfolgreich aktualisiert")
                .data(Map.of(this.rezept, newRezept))
                .statusCode(200)
                .timeStamp(LocalDateTime.now())
                .developerMessage("Rezept erfolgreich in der Datenbank aktualisiert")
                .build();

        when(rezeptService.updateRezept(1L, newRezept)).thenReturn(ResponseEntity.ok(response));
        assertEquals(HttpStatus.OK, rezeptController.updateRezept(1L, newRezept).getStatusCode(), messageStatus200);
    }

    @Test
    void testDeleteRezept() {
        Rezept rezept = new Rezept(1L, someTitle, someRezeptur, List.of(someTags));
        HttpResponse response = HttpResponse.builder()
                .status(HttpStatus.OK)
                .message("Rezept erfolgreich gelöscht")
                .data(Map.of(this.rezept, rezept))
                .statusCode(200)
                .timeStamp(LocalDateTime.now())
                .developerMessage("Rezept erfolgreich aus der Datenbank gelöscht")
                .build();

        when(rezeptService.deleteRezept(1L)).thenReturn(ResponseEntity.ok(response));
        assertEquals(HttpStatus.OK, rezeptController.deleteRezept(1L).getStatusCode(), messageStatus200);
    }

}