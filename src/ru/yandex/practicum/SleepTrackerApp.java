package ru.yandex.practicum;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class SleepTrackerApp {

    private static final List<Function<List<SleepingSession>, ? extends SleepAnalysisResult<?>>> ANALYTICAL_FUNCTIONS =
            Stream.<Function<List<SleepingSession>, ? extends SleepAnalysisResult<?>>>of(
                    new TotalSessionsFunction(),
                    new MinDurationFunction(),
                    new MaxDurationFunction(),
                    new AvgDurationFunction(),
                    new BadQualityCountFunction(),
                    new SleeplessNightsFunction(),
                    new UserChronotypeFunction()
            ).toList();

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Ошибка: Передайте путь к файлу лога сна в качестве аргумента командной строки.");
            return;
        }

        try {
            List<SleepingSession> sessions = parseSleepLog(args[0]);

            System.out.println("=== РЕЗУЛЬТАТЫ АНАЛИЗА ЛОГА СНА ===");


            ANALYTICAL_FUNCTIONS.stream()
                    .map(function -> function.apply(sessions))
                    .forEach(System.out::println);

        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла лога: " + e.getMessage());
        }
    }

    public static List<SleepingSession> parseSleepLog(String filePath) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            return lines
                    .filter(line -> !line.isBlank())
                    .map(SleepTrackerApp::parseLine)
                    .toList();
        }
    }

    private static SleepingSession parseLine(String line) {
        String[] parts = line.split(";");
        LocalDateTime start = LocalDateTime.parse(parts[0].trim(), FORMATTER);
        LocalDateTime end = LocalDateTime.parse(parts[1].trim(), FORMATTER);
        Quality quality = Quality.valueOf(parts[2].trim());
        return new SleepingSession(start, end, quality);
    }
}