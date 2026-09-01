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
        int startHour = sleepStart.getHour();
        int endHour = wakeUp.getHour();

        return (startHour < 6) ||
                (endHour >= 0 && endHour <= 6) ||
                (startHour >= 0 && startHour <= 6);
    }

    @Override
    public String toString() {
        return String.format("%s;%s;%s",
                sleepStart.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")),
                wakeUp.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")),
                quality);
    }
}