package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;
import java.time.Duration;

public class SleepingSession {
    private final LocalDateTime sleepStart;
    private final LocalDateTime wakeUp;
    private final SleepQuality quality;

    public SleepingSession(LocalDateTime sleepStart, LocalDateTime wakeUp, SleepQuality quality) {
        this.sleepStart = sleepStart;
        this.wakeUp = wakeUp;
        this.quality = quality;
    }

    public LocalDateTime getSleepStart() {
        return sleepStart;
    }

    public LocalDateTime getWakeUp() {
        return wakeUp;
    }

    public SleepQuality getQuality() {
        return quality;
    }

    public int getDurationMinutes() {
        return (int) Duration.between(sleepStart, wakeUp).toMinutes();
    }

    public boolean isNightSession() {
        LocalDateTime start = this.sleepStart;
        LocalDateTime end = this.wakeUp;

        // Проверяем пересечение с интервалом 0:00-6:00
        LocalDateTime nightStart = start.toLocalDate().atTime(0, 0);
        LocalDateTime nightEnd = end.toLocalDate().atTime(6, 0);

        // Сессия пересекает интервал, если:
        // - Началась до окончания интервала И закончилась после начала интервала
        return start.isBefore(nightEnd) && end.isAfter(nightStart);
    }

    @Override
    public String toString() {
        return String.format("%s;%s;%s",
                sleepStart.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")),
                wakeUp.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")),
                quality);
    }
}