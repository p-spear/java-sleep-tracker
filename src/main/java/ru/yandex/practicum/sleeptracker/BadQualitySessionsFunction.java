package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class BadQualitySessionsFunction implements AnalysisFunction {
    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions) {
        int badQualityCount = (int) sessions.stream()
                .filter(s -> s.getQuality() == SleepQuality.BAD)
                .count();

        return new SleepAnalysisResult("Количество сессий с плохим качеством сна", badQualityCount);
    }
}