package prince.aktiveskochbuch.adapter.apis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import prince.aktiveskochbuch.application.RezeptConstants;
import prince.aktiveskochbuch.domain.dtos.HttpResponse;
import prince.aktiveskochbuch.domain.dtos.RezeptDto;
import prince.aktiveskochbuch.domain.models.Rezept;
import prince.aktiveskochbuch.domain.models.StandardRezept;
import prince.aktiveskochbuch.domain.usecases.DeleteRezeptUseCase;
import prince.aktiveskochbuch.domain.usecases.GetRezeptUseCase;
import prince.aktiveskochbuch.domain.usecases.PostRezeptUseCase;
import prince.aktiveskochbuch.domain.usecases.PutRezeptUseCase;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static prince.aktiveskochbuch.application.RezeptConstants.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
class RezeptControllerTest {

    @Mock
    private GetRezeptUseCase getRezeptUseCase;

    @Mock
    private PostRezeptUseCase postRezeptUseCase;

    @Mock
    private PutRezeptUseCase putRezeptUseCase;

    @Mock
    private DeleteRezeptUseCase deleteRezeptUseCase;

    @InjectMocks
    private RezeptController rezeptController;

    @Test
    void testCreateRezept() {
        RezeptDto rezeptDto = new RezeptDto("Titel", RezeptConstants.REZEPTUR, List.of(TAG_1, TAG_2), STANDARD);
        when(postRezeptUseCase.createStandardRezept(rezeptDto)).thenReturn(new StandardRezept(rezeptDto.getTitel(), rezeptDto.getRezeptur(), rezeptDto.getTags()));

        ResponseEntity<HttpResponse> responseEntity = rezeptController.createRezept(rezeptDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), STATUS_CODE_SHOULD_BE_200);
        assertEquals(RezeptConstants.REZEPT_ERFOLGREICH_ERSTELLT, Objects.requireNonNull(responseEntity.getBody()).getMessage(), "Message should be 'Rezept erfolgreich erstellt'");
    }

    @Test
    void testGetRezepte() {
        RezeptDto rezeptDto = new RezeptDto("Titel", RezeptConstants.REZEPTUR, List.of(TAG_1, TAG_2), STANDARD);
        List<Rezept> rezepte = List.of(new StandardRezept(rezeptDto.getTitel(), rezeptDto.getRezeptur(), rezeptDto.getTags()));
        when(getRezeptUseCase.getAllRezepte()).thenReturn(rezepte);

        ResponseEntity<HttpResponse> responseEntity = rezeptController.getRezepte();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), STATUS_CODE_SHOULD_BE_200);
        assertEquals("Rezepte gefunden", Objects.requireNonNull(responseEntity.getBody()).getMessage(), "Message should be 'Rezepte gefunden'");
        assertEquals(rezepte, responseEntity.getBody().getData().get("rezepte"), "Rezepte should be returned");
        verify(getRezeptUseCase, times(1)).getAllRezepte();
    }

    @Test
    void testGetRezept() {
        String titel = "TestRezept";
        RezeptDto rezeptDto = new RezeptDto("TestRezept", RezeptConstants.REZEPTUR, List.of(TAG_1, TAG_2), STANDARD);
        Rezept rezept = new StandardRezept(rezeptDto.getTitel(), rezeptDto.getRezeptur(), rezeptDto.getTags());
        when(getRezeptUseCase.getRezept(titel)).thenReturn(rezept);

        ResponseEntity<HttpResponse> responseEntity = rezeptController.getRezept(titel);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), STATUS_CODE_SHOULD_BE_200);
        assertEquals("Rezept gefunden", Objects.requireNonNull(responseEntity.getBody()).getMessage(), "Message should be 'Rezept gefunden'");
        assertEquals(rezept, responseEntity.getBody().getData().get(RezeptConstants.REZEPT), "Rezept should be returned");
        verify(getRezeptUseCase, times(1)).getRezept(titel);
    }

    @Test
    void testUpdateRezept() {
        Long id = 1L;
        RezeptDto rezeptDto = new RezeptDto("TestRezept", RezeptConstants.REZEPTUR, List.of(TAG_1, TAG_2), STANDARD);

        Rezept updatedRezept = new StandardRezept(rezeptDto.getTitel(), rezeptDto.getRezeptur(), rezeptDto.getTags());
        when(putRezeptUseCase.updateRezept(id, rezeptDto)).thenReturn(updatedRezept);

        ResponseEntity<HttpResponse> responseEntity = rezeptController.updateRezept(id, rezeptDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), STATUS_CODE_SHOULD_BE_200);
        assertEquals("Rezept aktualisiert", Objects.requireNonNull(responseEntity.getBody()).getMessage(), "Message should be 'Rezept aktualisiert'");
        assertEquals(updatedRezept, responseEntity.getBody().getData().get(RezeptConstants.REZEPT), "Rezept should be returned");
        verify(putRezeptUseCase, times(1)).updateRezept(id, rezeptDto);
    }

    @Test
    void testDeleteRezept() {
        Long id = 1L;

        ResponseEntity<HttpResponse> responseEntity = rezeptController.deleteRezept(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), STATUS_CODE_SHOULD_BE_200);
        assertEquals("Rezept gelöscht", Objects.requireNonNull(responseEntity.getBody()).getMessage(), "Message should be 'Rezept gelöscht'");
        verify(deleteRezeptUseCase, times(1)).deleteRezept(id);
    }




}