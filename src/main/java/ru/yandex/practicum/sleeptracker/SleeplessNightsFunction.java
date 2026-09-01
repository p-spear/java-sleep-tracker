package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.stream.IntStream;

public class SleeplessNightsFunction implements AnalysisFunction {
    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Количество бессонных ночей", 0);
        }

        LocalDateTime firstStart = sessions.get(0).getSleepStart();
        LocalDateTime lastEnd = sessions.get(sessions.size() - 1).getWakeUp();

        LocalDate startDate = firstStart.toLocalDate();
        LocalDate endDate = lastEnd.toLocalDate();

        if (firstStart.getHour() >= 12) {
            startDate = startDate.plusDays(1);
        }

        int totalNights = Period.between(startDate, endDate).getDays();
        if (totalNights <= 0) {
            return new SleepAnalysisResult("Количество бессонных ночей", 0);
        }

        LocalDate finalStartDate = startDate;

        int sleeplessNights = (int) IntStream.range(0, totalNights)
                .mapToObj(i -> finalStartDate.plusDays(i))
                .filter(date -> isSleeplessNight(date, sessions))
                .count();

        return new SleepAnalysisResult("Количество бессонных ночей", sleeplessNights);
    }

    private boolean isSleeplessNight(LocalDate date, List<SleepingSession> sessions) {
        LocalDateTime nightStart = date.atTime(0, 0);
        LocalDateTime nightEnd = date.atTime(6, 0);

        return sessions.stream().noneMatch(session -> {
            LocalDateTime start = session.getSleepStart();
            LocalDateTime end = session.getWakeUp();

            return (start.isBefore(nightEnd) && end.isAfter(nightStart)) ||
                    (start.isBefore(nightEnd) && end.isAfter(nightEnd)) ||
                    (start.isBefore(nightStart) && end.isAfter(nightStart));
        });
    }
}