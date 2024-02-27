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
class RezeptServiceTests {

    @Mock
    private RezeptRepository rezeptRepository;

    @InjectMocks
    private RezeptService rezeptService;

    @Test
    public void testCreateRezept() {
        Rezept rezept = new Rezept(1L, "someTitle", "someRezeptur", List.of("someTags"));
        when(rezeptRepository.save(rezept)).thenReturn(rezept);

        ResponseEntity<HttpResponse> responseEntity = rezeptService.createRezept(rezept);

                assertEquals(HttpStatus.OK, Objects.requireNonNull(responseEntity.getBody()).getStatus());
                assertEquals("Rezept erfolgreich erstellt", Objects.requireNonNull(responseEntity.getBody()).getMessage());
                assertEquals(rezept, Objects.requireNonNull(responseEntity.getBody()).getData().get("rezept"));
                assertEquals(1, Objects.requireNonNull(responseEntity.getBody()).getData().size());
                assertEquals(200, Objects.requireNonNull(responseEntity.getBody()).getStatusCode());
                assertNotNull(Objects.requireNonNull(responseEntity.getBody()).getTimeStamp());
                assertEquals("Rezept erfolgreich zur Datenbank hinzugefügt", Objects.requireNonNull(responseEntity.getBody()).getDeveloperMessage());

        // Add more assertions based on the expected behavior
    }

    // Add tests for other methods in RezeptServiceImplementation

    @Test
    public void testGetRezept_WhenRezeptExists_ShouldReturnRezept() {
        String title = "someTitle";
        Rezept rezept = new Rezept(1L, title, "someRezeptur", List.of("someTags"));
        when(rezeptRepository.findByTitel(title)).thenReturn(Optional.of(rezept));

        ResponseEntity<HttpResponse> responseEntity = rezeptService.getRezept(title);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).getData().get("rezept").toString().contains("1"));
        assertFalse(Objects.requireNonNull(responseEntity.getBody()).getData().get("rezept").toString().contains("2"));
        // Add more assertions based on the expected behavior
    }

    @Test
    public void testGetRezept_WhenRezeptNotExists_ShouldReturnNotFound() {
        String title = "nonExistentTitle";
        when(rezeptRepository.findByTitel(title)).thenReturn(Optional.empty());

        ResponseEntity<HttpResponse> responseEntity = rezeptService.getRezept(title);

        assertEquals(HttpStatus.NOT_FOUND, Objects.requireNonNull(responseEntity.getBody()).getStatus());
        // Add more assertions based on the expected behavior
    }

    @Test
    public void testUpdateRezept_WhenRezeptExists_ShouldReturnUpdatedRezept() {
        Long id = 1L;
        Rezept rezept = new Rezept(id, "someTitle", "someRezeptur", List.of("someTags"));
        when(rezeptRepository.findById(id)).thenReturn(Optional.of(rezept));
        when(rezeptRepository.save(rezept)).thenReturn(rezept);

        ResponseEntity<HttpResponse> responseEntity = rezeptService.updateRezept(id, rezept);

        assertEquals(HttpStatus.OK, Objects.requireNonNull(responseEntity.getBody()).getStatus());
        // Add more assertions based on the expected behavior
    }

    @Test
    public void testUpdateRezept_WhenRezeptNotExists_ShouldReturnNotFound() {
        Long id = 1L;
        Rezept rezept = new Rezept(id, "someTitle", "someRezeptur", List.of("someTags"));
        when(rezeptRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<HttpResponse> responseEntity = rezeptService.updateRezept(id, rezept);

        assertEquals(HttpStatus.NOT_FOUND, Objects.requireNonNull(responseEntity.getBody()).getStatus());
        // Add more assertions based on the expected behavior
    }

    @Test
    public void testDeleteRezept_WhenRezeptExists_ShouldReturnDeletedRezept() {
        Long id = 1L;
        Rezept rezept = new Rezept(id, "someTitle", "someRezeptur", List.of("someTags"));
        when(rezeptRepository.findById(id)).thenReturn(Optional.of(rezept));

        ResponseEntity<HttpResponse> responseEntity = rezeptService.deleteRezept(id);

        assertEquals(HttpStatus.OK, Objects.requireNonNull(responseEntity.getBody()).getStatus());
        // Add more assertions based on the expected behavior
    }

    @Test
    public void testDeleteRezept_WhenRezeptNotExists_ShouldReturnNotFound() {
        Long id = 1L;
        when(rezeptRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<HttpResponse> responseEntity = rezeptService.deleteRezept(id);

        assertEquals(HttpStatus.NOT_FOUND, Objects.requireNonNull(responseEntity.getBody()).getStatus());
        // Add more assertions based on the expected behavior
    }

    @Test
    public void testGetAllRezepte_WhenRezepteExists_ShouldReturnRezepte() {
        List<Rezept> rezepte = List.of(new Rezept(1L, "someTitle", "someRezeptur", List.of("someTags")));
        when(rezeptRepository.findAll()).thenReturn(rezepte);

        ResponseEntity<HttpResponse> responseEntity = rezeptService.getAllRezepte();

        assertEquals(HttpStatus.OK, Objects.requireNonNull(responseEntity.getBody()).getStatus());
        // Add more assertions based on the expected behavior
    }

    @Test
    public void testGetAllRezepte_WhenRezepteNotExists_ShouldReturnNotFound() {
        when(rezeptRepository.findAll()).thenReturn(List.of());

        ResponseEntity<HttpResponse> responseEntity = rezeptService.getAllRezepte();

        assertEquals(HttpStatus.NOT_FOUND, Objects.requireNonNull(responseEntity.getBody()).getStatus());
        // Add more assertions based on the expected behavior
    }


    // Add more tests as needed
}
