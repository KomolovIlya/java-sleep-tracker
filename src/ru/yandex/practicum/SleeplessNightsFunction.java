package ru.yandex.practicum;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.function.Function;
import java.util.stream.LongStream;

public class SleeplessNightsFunction implements Function<List<SleepingSession>, SleepAnalysisResult<Long>> {
    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult<>("Количество бессонных ночей", 0L);
        }

        SleepingSession first = sessions.get(0);
        LocalDate startDate = first.start().getHour() >= 12
                ? first.start().toLocalDate().plusDays(1)
                : first.start().toLocalDate();

        LocalDate endDate = sessions.get(sessions.size() - 1).end().toLocalDate();

        long totalNights = Period.between(startDate, endDate.plusDays(1)).getDays();

        long sleeplessNights = LongStream.range(0, totalNights)
                .mapToObj(startDate::plusDays)
                .filter(nightDate -> sessions.stream()
                        .noneMatch(session -> session.intersectsNightInterval(nightDate.atStartOfDay())))
                .count();

        return new SleepAnalysisResult<>("Количество бессонных ночей", sleeplessNights);
    }
}