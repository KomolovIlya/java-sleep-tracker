package ru.yandex.practicum;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserChronotypeFunction implements Function<List<SleepingSession>, SleepAnalysisResult<Chronotype>> {
    @Override
    public SleepAnalysisResult<Chronotype> apply(List<SleepingSession> sessions) {
        List<SleepingSession> validSessions = sessions.stream()
                .filter(this::isNotPureDaySleep)
                .toList();

        if (validSessions.isEmpty()) {
            return new SleepAnalysisResult<>("Хронотип пользователя", Chronotype.DOVE);
        }

        Map<Chronotype, Long> counts = validSessions.stream()
                .map(this::classifySession)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        long larkCount = counts.getOrDefault(Chronotype.LARK, 0L);
        long owlCount = counts.getOrDefault(Chronotype.OWL, 0L);
        long doveCount = counts.getOrDefault(Chronotype.DOVE, 0L);

        Chronotype finalChronotype;
        if (larkCount > owlCount && larkCount > doveCount) {
            finalChronotype = Chronotype.LARK;
        } else if (owlCount > larkCount && owlCount > doveCount) {
            finalChronotype = Chronotype.OWL;
        } else {
            finalChronotype = Chronotype.DOVE;
        }

        return new SleepAnalysisResult<>("Хронотип пользователя", finalChronotype);
    }

    private boolean isNotPureDaySleep(SleepingSession session) {
        LocalTime bedTime = session.start().toLocalTime();

        if (bedTime.isAfter(LocalTime.of(23, 0)) || bedTime.isBefore(LocalTime.of(22, 0))) {
            return true;
        }

        return session.intersectsNightInterval(session.start().toLocalDate().atStartOfDay());
    }

    private Chronotype classifySession(SleepingSession session) {
        LocalTime bedTime = session.start().toLocalTime();
        LocalTime wakeTime = session.end().toLocalTime();

        boolean isLark = bedTime.isBefore(LocalTime.of(22, 0)) && wakeTime.isBefore(LocalTime.of(7, 0));

        boolean isOwl = bedTime.isAfter(LocalTime.of(23, 0)) && wakeTime.isAfter(LocalTime.of(9, 0));

        if (isLark) return Chronotype.LARK;
        if (isOwl) return Chronotype.OWL;
        return Chronotype.DOVE;
    }
}