package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChronotypeFunction implements AnalysisFunction {
    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Хронотип пользователя", Chronotype.PIGEON);
        }

        List<SleepingSession> nightSessions = sessions.stream()
                .filter(SleepingSession::isNightSession)
                .collect(Collectors.toList());

        if (nightSessions.isEmpty()) {
            return new SleepAnalysisResult("Хронотип пользователя", Chronotype.PIGEON);
        }

        Map<Chronotype, Long> chronotypeCount = nightSessions.stream()
                .map(this::determineChronotype)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        Chronotype result = chronotypeCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(Chronotype.PIGEON);

        long maxCount = chronotypeCount.values().stream()
                .max(Long::compareTo)
                .orElse(0L);

        long typesWithMaxCount = chronotypeCount.values().stream()
                .filter(count -> count.equals(maxCount))
                .count();

        if (typesWithMaxCount > 1) {
            result = Chronotype.PIGEON;
        }

        return new SleepAnalysisResult("Хронотип пользователя", result);
    }

    private Chronotype determineChronotype(SleepingSession session) {
        LocalDateTime start = session.getSleepStart();
        LocalDateTime end = session.getWakeUp();

        int startHour = start.getHour();
        int endHour = end.getHour();

        if (startHour >= 23 && endHour >= 9) {
            return Chronotype.OWL;
        }

        if (startHour < 22 && endHour < 7) {
            return Chronotype.LARK;
        }

        return Chronotype.PIGEON;
    }
}