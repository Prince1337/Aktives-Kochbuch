package prince.aktiveskochbuch.adapter.apis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import prince.aktiveskochbuch.application.exceptions.EMailSend;
import prince.aktiveskochbuch.domain.dtos.HttpResponse;
import prince.aktiveskochbuch.domain.dtos.VorschlaegeDto;
import prince.aktiveskochbuch.domain.models.Rezept;
import prince.aktiveskochbuch.domain.models.VegetarischesRezept;
import prince.aktiveskochbuch.domain.usecases.AutomatischeVorschlaegeUseCase;
import prince.aktiveskochbuch.domain.usecases.SendEmailUseCase;
import prince.aktiveskochbuch.domain.usecases.VorschlaegeGenerierenUseCase;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
class VorschlaegeControllerTest {
    @Mock
    private VorschlaegeGenerierenUseCase vorschlaegeGenerierenUseCase;

    @Mock
    private SendEmailUseCase sendEmailUseCase;

    @Mock
    private AutomatischeVorschlaegeUseCase automatischeVorschlaegeUseCase;

    @InjectMocks
    private VorschlaegeController vorschlaegeController;

    @BeforeEach
    void setUp() {
        vorschlaegeController = new VorschlaegeController(vorschlaegeGenerierenUseCase, sendEmailUseCase, automatischeVorschlaegeUseCase);
    }

    @Test
    void testGenerateVorschlaege() throws EMailSend {
        VorschlaegeDto vorschlaegeDto = new VorschlaegeDto(List.of("Gesund", "Gemüse"), 3);

        List<Rezept> expectedVorschlaege = List.of(new VegetarischesRezept("Vegetarische Lasagne", "Lasagneplatten, Tomatensauce, Gemüse, Bechamelsauce, Käse", List.of("Gesund", "Käse")),
                new VegetarischesRezept("Vegetarische Pizza", "Pizzateig, Tomatensauce, Gemüse, Käse", List.of("Gesund", "Gemüse")));
        when(vorschlaegeGenerierenUseCase.generateVorschlaege(vorschlaegeDto)).thenReturn(expectedVorschlaege);
        ResponseEntity<HttpResponse> result = vorschlaegeController.generateAutomaticVorschlaege(vorschlaegeDto);

        assertEquals(HttpStatus.OK, result.getStatusCode(), "Status code should be 200");
        assertEquals("Vorschlaege generiert", Objects.requireNonNull(result.getBody()).getMessage(), "Message should be 'Vorschlaege generiert'");
        assertEquals(expectedVorschlaege, result.getBody().getData().get("vorschlaege"), "Vorschlaege should be the same as expected");
        verify(vorschlaegeGenerierenUseCase, times(1)).generateVorschlaege(vorschlaegeDto);
    }

}