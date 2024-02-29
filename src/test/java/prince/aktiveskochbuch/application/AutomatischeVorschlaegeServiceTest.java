package prince.aktiveskochbuch.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
class AutomatischeVorschlaegeServiceTest {

    private AutomatischeVorschlaegeService vorschlaegeService;

    @BeforeEach
    void setUp() {
        vorschlaegeService = new AutomatischeVorschlaegeService();
    }

    @Test
    void activateAutomaticSuggestions() {
        vorschlaegeService.activateAutomaticSuggestions(true);
        assertTrue(vorschlaegeService.isAutomaticSuggestionsActivated(), "Automatic suggestions should be activated");

        vorschlaegeService.activateAutomaticSuggestions(false);
        assertFalse(vorschlaegeService.isAutomaticSuggestionsActivated(), "Automatic suggestions should be deactivated");
    }

    @Test
    void setSchedule() {
        Set<DayOfWeek> daysOfWeek = new HashSet<>();
        daysOfWeek.add(DayOfWeek.MONDAY);
        daysOfWeek.add(DayOfWeek.WEDNESDAY);

        LocalTime time = LocalTime.of(12, 30);

        vorschlaegeService.setSchedule(daysOfWeek, time);

        assertEquals(daysOfWeek, vorschlaegeService.getScheduledDays(), "Scheduled days should be the same as set");
        assertEquals(time, vorschlaegeService.getScheduledTime(), "Scheduled time should be the same as set");
    }

    @Test
    void getScheduledDays() {
        Set<DayOfWeek> daysOfWeek = new HashSet<>();
        daysOfWeek.add(DayOfWeek.MONDAY);
        daysOfWeek.add(DayOfWeek.WEDNESDAY);

        vorschlaegeService.setSchedule(daysOfWeek, LocalTime.now());

        assertEquals(daysOfWeek, vorschlaegeService.getScheduledDays(), "Scheduled days should be the same as set");
    }

    @Test
    void getScheduledTime() {
        LocalTime time = LocalTime.of(12, 30);
        vorschlaegeService.setSchedule(new HashSet<>(), time);

        assertEquals(time, vorschlaegeService.getScheduledTime(), "Scheduled time should be the same as set");
    }

}