package ru.yandex.practicum;

import java.time.Duration;
import java.time.LocalDateTime;

public record SleepingSession(LocalDateTime start, LocalDateTime end, Quality quality) {

    public long getDurationInMinutes() {
        return Duration.between(start, end).toMinutes();
    }

    public boolean intersectsNightInterval(LocalDateTime nightStart) {
        LocalDateTime targetStart = nightStart.toLocalDate().atStartOfDay(); // 00:00
        LocalDateTime targetEnd = targetStart.plusHours(6);                  // 06:00

        return start.isBefore(targetEnd) && end.isAfter(targetStart);
    }
}