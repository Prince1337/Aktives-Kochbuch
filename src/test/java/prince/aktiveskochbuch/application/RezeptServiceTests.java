package prince.aktiveskochbuch.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import prince.aktiveskochbuch.adapter.db.RezeptRepository;
import prince.aktiveskochbuch.domain.dtos.HttpResponse;
import prince.aktiveskochbuch.domain.models.Rezept;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
class RezeptServiceTests {

    public static final String SOME_TITLE = "someTitle";
    public static final String SOME_REZEPTUR = "someRezeptur";
    public static final String SOME_TAGS = "someTags";
    public static final String STATUS_CODE_SHOULD_BE_404 = "Status code should be 404";
    public static final String STATUS_CODE_SHOULD_BE_200 = "Status code should be 200";
    @Mock
    private RezeptRepository rezeptRepository;

    @InjectMocks
    private RezeptService rezeptService;

    @Test
    void testCreateRezept() {
        Rezept rezept = new Rezept(1L, SOME_TITLE, SOME_REZEPTUR, List.of(SOME_TAGS));
        when(rezeptRepository.save(rezept)).thenReturn(rezept);

        ResponseEntity<HttpResponse> responseEntity = rezeptService.createRezept(rezept);

                assertEquals(HttpStatus.OK, Objects.requireNonNull(responseEntity.getBody()).getStatus(), STATUS_CODE_SHOULD_BE_200);
                assertEquals("Rezept erfolgreich erstellt", Objects.requireNonNull(responseEntity.getBody()).getMessage(), "Message should be 'Rezept erfolgreich erstellt'");
                assertEquals(rezept, Objects.requireNonNull(responseEntity.getBody()).getData().get("rezept"), "Rezept should be returned");
                assertEquals(1, Objects.requireNonNull(responseEntity.getBody()).getData().size(), "Response should contain only one element");
                assertEquals(200, Objects.requireNonNull(responseEntity.getBody()).getStatusCode(), STATUS_CODE_SHOULD_BE_200);
                assertNotNull(Objects.requireNonNull(responseEntity.getBody()).getTimeStamp(), "Timestamp should not be null");
                assertEquals("Rezept erfolgreich zur Datenbank hinzugefügt", Objects.requireNonNull(responseEntity.getBody()).getDeveloperMessage(), "Developer message should be 'Rezept erfolgreich zur Datenbank hinzugefügt'");

    }


    @Test
    void testGetRezept_WhenRezeptExists_ShouldReturnRezept() {
        String title = SOME_TITLE;
        Rezept rezept = new Rezept(1L, title, SOME_REZEPTUR, List.of(SOME_TAGS));
        when(rezeptRepository.findByTitel(title)).thenReturn(Optional.of(rezept));

        ResponseEntity<HttpResponse> responseEntity = rezeptService.getRezept(title);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), STATUS_CODE_SHOULD_BE_200);
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).getData().get("rezept").toString().contains("1"), "Rezept should be returned");
        assertFalse(Objects.requireNonNull(responseEntity.getBody()).getData().get("rezept").toString().contains("2"), "Rezept should be returned");
    }

    @Test
    void testGetRezept_WhenRezeptNotExists_ShouldReturnNotFound() {
        String title = "nonExistentTitle";
        when(rezeptRepository.findByTitel(title)).thenReturn(Optional.empty());

        ResponseEntity<HttpResponse> responseEntity = rezeptService.getRezept(title);

        assertEquals(HttpStatus.NOT_FOUND, Objects.requireNonNull(responseEntity.getBody()).getStatus(), STATUS_CODE_SHOULD_BE_404);
    }

    @Test
    void testUpdateRezept_WhenRezeptExists_ShouldReturnUpdatedRezept() {
        Long id = 1L;
        Rezept rezept = new Rezept(id, SOME_TITLE, SOME_REZEPTUR, List.of(SOME_TAGS));
        when(rezeptRepository.findById(id)).thenReturn(Optional.of(rezept));
        when(rezeptRepository.save(rezept)).thenReturn(rezept);

        ResponseEntity<HttpResponse> responseEntity = rezeptService.updateRezept(id, rezept);

        assertEquals(HttpStatus.OK, Objects.requireNonNull(responseEntity.getBody()).getStatus(), STATUS_CODE_SHOULD_BE_200);
    }

    @Test
    void testUpdateRezept_WhenRezeptNotExists_ShouldReturnNotFound() {
        Long id = 1L;
        Rezept rezept = new Rezept(id, SOME_TITLE, SOME_REZEPTUR, List.of(SOME_TAGS));
        when(rezeptRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<HttpResponse> responseEntity = rezeptService.updateRezept(id, rezept);

        assertEquals(HttpStatus.NOT_FOUND, Objects.requireNonNull(responseEntity.getBody()).getStatus(), STATUS_CODE_SHOULD_BE_404);
    }

    @Test
    void testDeleteRezept_WhenRezeptExists_ShouldReturnDeletedRezept() {
        Long id = 1L;
        Rezept rezept = new Rezept(id, SOME_TITLE, SOME_REZEPTUR, List.of(SOME_TAGS));
        when(rezeptRepository.findById(id)).thenReturn(Optional.of(rezept));

        ResponseEntity<HttpResponse> responseEntity = rezeptService.deleteRezept(id);

        assertEquals(HttpStatus.OK, Objects.requireNonNull(responseEntity.getBody()).getStatus(), STATUS_CODE_SHOULD_BE_200);
    }

    @Test
    void testDeleteRezept_WhenRezeptNotExists_ShouldReturnNotFound() {
        Long id = 1L;
        when(rezeptRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<HttpResponse> responseEntity = rezeptService.deleteRezept(id);

        assertEquals(HttpStatus.NOT_FOUND, Objects.requireNonNull(responseEntity.getBody()).getStatus(), STATUS_CODE_SHOULD_BE_404);
    }

    @Test
    void testGetAllRezepte_WhenRezepteExists_ShouldReturnRezepte() {
        List<Rezept> rezepte = List.of(new Rezept(1L, SOME_TITLE, SOME_REZEPTUR, List.of(SOME_TAGS)));
        when(rezeptRepository.findAll()).thenReturn(rezepte);

        ResponseEntity<HttpResponse> responseEntity = rezeptService.getAllRezepte();

        assertEquals(HttpStatus.OK, Objects.requireNonNull(responseEntity.getBody()).getStatus(), STATUS_CODE_SHOULD_BE_200);
    }

    @Test
    void testGetAllRezepte_WhenRezepteNotExists_ShouldReturnNotFound() {
        when(rezeptRepository.findAll()).thenReturn(List.of());

        ResponseEntity<HttpResponse> responseEntity = rezeptService.getAllRezepte();

        assertEquals(HttpStatus.NOT_FOUND, Objects.requireNonNull(responseEntity.getBody()).getStatus(), STATUS_CODE_SHOULD_BE_404);
    }


}
