package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class AvgDurationFunction implements AnalysisFunction {
    private static final int DECIMAL_PLACES = 1;
    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Средняя продолжительность сессии (мин)", 0.0);
        }

        double avgDuration = sessions.stream()
                .mapToInt(SleepingSession::getDurationMinutes)
                .average()
                .orElse(0.0);

        double roundedAvg = roundToDecimalPlaces(avgDuration, DECIMAL_PLACES);

        return new SleepAnalysisResult("Средняя продолжительность сессии (мин)",
                Math.round(avgDuration * 10) / 10.0);
    }

    private double roundToDecimalPlaces(double value, int places) {
        double multiplier = Math.pow(10, places);
        return Math.round(value * multiplier) / multiplier;
    }
}