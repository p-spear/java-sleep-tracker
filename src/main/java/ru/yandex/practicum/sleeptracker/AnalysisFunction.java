package ru.yandex.practicum.sleeptracker;

import java.util.List;

@FunctionalInterface
public interface AnalysisFunction {
    SleepAnalysisResult analyze(List<SleepingSession> sessions);
}