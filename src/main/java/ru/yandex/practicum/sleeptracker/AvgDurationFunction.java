package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class AvgDurationFunction implements AnalysisFunction {
    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Средняя продолжительность сессии (мин)", 0.0);
        }

        double avgDuration = sessions.stream()
                .mapToInt(SleepingSession::getDurationMinutes)
                .average()
                .orElse(0.0);

        return new SleepAnalysisResult("Средняя продолжительность сессии (мин)",
                Math.round(avgDuration * 10) / 10.0);
    }
}