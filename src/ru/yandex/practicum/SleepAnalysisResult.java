package ru.yandex.practicum;

public record SleepAnalysisResult<T>(String description, T value) {
    @Override
    public String toString() {
        return description + ": " + value;
    }
}