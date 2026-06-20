package main.java.ru.yandex.practicum.sleeptracker.functions;

import main.java.ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import main.java.ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;
import java.util.function.Function;

public class MinDurationFunction implements Function<List<SleepingSession>, SleepAnalysisResult<Long>> {
    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        long min = sessions.stream()
                .mapToLong(SleepingSession::getDurationInMinutes)
                .min()
                .orElse(0);
        return new SleepAnalysisResult<>("Минимальная продолжительность сессии (минуты)", min);
    }
}