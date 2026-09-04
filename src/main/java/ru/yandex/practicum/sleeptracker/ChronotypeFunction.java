package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChronotypeFunction implements AnalysisFunction {
    // Константы для определения хронотипа "Сова"
    private static final int OWL_SLEEP_START_HOUR = 23;
    private static final int OWL_WAKE_UP_HOUR = 9;

    // Константы для определения хронотипа "Жаворонок"
    private static final int LARK_SLEEP_START_HOUR = 22;
    private static final int LARK_WAKE_UP_HOUR = 7;
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

        // Сова: засыпание после 23:00 И пробуждение после 9:00
        if (startHour >= OWL_SLEEP_START_HOUR && endHour >= OWL_WAKE_UP_HOUR) {
            return Chronotype.OWL;
        }

        // Жаворонок: засыпание до 22:00 И пробуждение до 7:00
        if (startHour < LARK_SLEEP_START_HOUR && endHour < LARK_WAKE_UP_HOUR) {
            return Chronotype.LARK;
        }

        return Chronotype.PIGEON;
    }
}