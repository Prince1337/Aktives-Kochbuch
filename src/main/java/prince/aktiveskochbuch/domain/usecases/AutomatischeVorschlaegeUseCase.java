package prince.aktiveskochbuch.domain.usecases;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

public interface AutomatischeVorschlaegeUseCase {

    void activateAutomaticSuggestions(boolean activate);

    void setSchedule(Set<DayOfWeek> daysOfWeek, LocalTime time);

    boolean isAutomaticSuggestionsActivated();

    Set<DayOfWeek> getScheduledDays();

    LocalTime getScheduledTime();

}
