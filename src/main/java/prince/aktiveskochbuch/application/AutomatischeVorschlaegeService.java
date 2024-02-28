package prince.aktiveskochbuch.application;

import org.springframework.stereotype.Service;
import prince.aktiveskochbuch.domain.usecases.AutomatischeVorschlaegeUseCase;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class AutomatischeVorschlaegeService implements AutomatischeVorschlaegeUseCase {

    private boolean automaticSuggestionsActivated = false;
    private Set<DayOfWeek> scheduledDays = new HashSet<>();
    private LocalTime scheduledTime = LocalTime.now();

    @Override
    public void activateAutomaticSuggestions(boolean activate) {
        automaticSuggestionsActivated = activate;
    }

    @Override
    public void setSchedule(Set<DayOfWeek> daysOfWeek, LocalTime time) {
        scheduledDays = daysOfWeek;
        scheduledTime = time;
    }

    @Override
    public boolean isAutomaticSuggestionsActivated() {
        return automaticSuggestionsActivated;
    }

    @Override
    public Set<DayOfWeek> getScheduledDays() {
        return scheduledDays;
    }

    @Override
    public LocalTime getScheduledTime() {
        return scheduledTime;
    }
}
