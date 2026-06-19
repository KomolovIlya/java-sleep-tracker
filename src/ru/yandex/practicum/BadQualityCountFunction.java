package ru.yandex.practicum;

import java.util.List;
import java.util.function.Function;

public class BadQualityCountFunction implements Function<List<SleepingSession>, SleepAnalysisResult<Long>> {
    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        long count = sessions.stream()
                .filter(s -> s.quality() == Quality.BAD)
                .count();
        return new SleepAnalysisResult<>("Количество сессий с плохим качеством сна", count);
    }
}