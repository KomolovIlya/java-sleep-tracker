package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleepTrackerTest {

    @Test
    void testTotalSessions_WithData() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 0), LocalDateTime.of(2025, 10, 2, 6, 0), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0), LocalDateTime.of(2025, 10, 3, 7, 0), Quality.NORMAL)
        );
        assertEquals(2L, new TotalSessionsFunction().apply(sessions).value());
    }

    @Test
    void testTotalSessions_Empty() {
        List<SleepingSession> sessions = List.of();
        assertEquals(0L, new TotalSessionsFunction().apply(sessions).value());
    }

    @Test
    void testMinDuration_Standard() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0), LocalDateTime.of(2025, 10, 2, 5, 0), Quality.NORMAL),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 22, 0), LocalDateTime.of(2025, 10, 3, 6, 0), Quality.GOOD)
        );
        assertEquals(360L, new MinDurationFunction().apply(sessions).value());
    }

    @Test
    void testMinDuration_Empty() {
        List<SleepingSession> sessions = List.of();
        assertEquals(0L, new MinDurationFunction().apply(sessions).value());
    }

    @Test
    void testMaxDuration_Standard() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0), LocalDateTime.of(2025, 10, 2, 5, 0), Quality.NORMAL),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 22, 0), LocalDateTime.of(2025, 10, 3, 6, 0), Quality.GOOD)
        );
        assertEquals(480L, new MaxDurationFunction().apply(sessions).value());
    }

    @Test
    void testMaxDuration_Empty() {
        List<SleepingSession> sessions = List.of();
        assertEquals(0L, new MaxDurationFunction().apply(sessions).value());
    }

    @Test
    void testAvgDuration_Standard() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0), LocalDateTime.of(2025, 10, 2, 5, 0), Quality.NORMAL),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 22, 0), LocalDateTime.of(2025, 10, 3, 6, 0), Quality.GOOD)
        );
        assertEquals(420.0, new AvgDurationFunction().apply(sessions).value());
    }

    @Test
    void testAvgDuration_Empty() {
        List<SleepingSession> sessions = List.of();
        assertEquals(0.0, new AvgDurationFunction().apply(sessions).value());
    }

    @Test
    void testBadQualityCount_Mixed() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 0), LocalDateTime.of(2025, 10, 2, 6, 0), Quality.BAD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0), LocalDateTime.of(2025, 10, 3, 7, 0), Quality.NORMAL),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 23, 30), LocalDateTime.of(2025, 10, 4, 6, 20), Quality.BAD)
        );
        assertEquals(2L, new BadQualityCountFunction().apply(sessions).value());
    }

    @Test
    void testBadQualityCount_None() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 0), LocalDateTime.of(2025, 10, 2, 6, 0), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0), LocalDateTime.of(2025, 10, 3, 7, 0), Quality.NORMAL)
        );
        assertEquals(0L, new BadQualityCountFunction().apply(sessions).value());
    }

    @Test
    void testSleeplessNights_AllNormal() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0), LocalDateTime.of(2025, 10, 2, 7, 0), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0), LocalDateTime.of(2025, 10, 3, 7, 0), Quality.GOOD)
        );
        assertEquals(0L, new SleeplessNightsFunction().apply(sessions).value());
    }

    @Test
    void testSleeplessNights_WithDaySleepOnly() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 0), LocalDateTime.of(2025, 10, 2, 6, 0), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 7, 0), LocalDateTime.of(2025, 10, 3, 11, 0), Quality.NORMAL)
        );
        assertEquals(1L, new SleeplessNightsFunction().apply(sessions).value());
    }

    @Test
    void testSleeplessNights_RuleAfter12() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0), LocalDateTime.of(2025, 10, 2, 7, 0), Quality.GOOD)
        );
        assertEquals(0L, new SleeplessNightsFunction().apply(sessions).value());
    }

    @Test
    void testSleeplessNights_MonthTransition() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 30, 23, 0), LocalDateTime.of(2025, 10, 31, 6, 0), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 11, 1, 23, 0), LocalDateTime.of(2025, 11, 2, 6, 0), Quality.GOOD)
        );
        assertEquals(1L, new SleeplessNightsFunction().apply(sessions).value());
    }

    @Test
    void testUserChronotype_Owl() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 30), LocalDateTime.of(2025, 10, 2, 9, 30), Quality.GOOD)
        );
        assertEquals(Chronotype.OWL, new UserChronotypeFunction().apply(sessions).value());
    }

    @Test
    void testUserChronotype_EqualityToDove() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 21, 0), LocalDateTime.of(2025, 10, 2, 6, 0), Quality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 30), LocalDateTime.of(2025, 10, 3, 9, 30), Quality.GOOD)
        );
        assertEquals(Chronotype.DOVE, new UserChronotypeFunction().apply(sessions).value());
    }
}