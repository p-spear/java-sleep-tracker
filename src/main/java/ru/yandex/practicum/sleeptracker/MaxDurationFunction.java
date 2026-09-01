package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class MaxDurationFunction implements AnalysisFunction {
    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Максимальная продолжительность сессии (мин)", 0);
        }

        int maxDuration = sessions.stream()
                .mapToInt(SleepingSession::getDurationMinutes)
                .max()
                .orElse(0);

        return new SleepAnalysisResult("Максимальная продолжительность сессии (мин)", maxDuration);
    }
}