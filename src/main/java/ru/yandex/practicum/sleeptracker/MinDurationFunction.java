package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class MinDurationFunction implements AnalysisFunction {
    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Минимальная продолжительность сессии (мин)", 0);
        }

        int minDuration = sessions.stream()
                .mapToInt(SleepingSession::getDurationMinutes)
                .min()
                .orElse(0);

        return new SleepAnalysisResult("Минимальная продолжительность сессии (мин)", minDuration);
    }
}