package ru.yandex.practicum.sleeptracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SleepTrackerApp {
    private final List<AnalysisFunction> functions;
    private static final String LOG_FILE_PATH = "sleep_log.txt";

    public SleepTrackerApp() {
        functions = new ArrayList<>();
        functions.add(new TotalSessionsFunction());
        functions.add(new MinDurationFunction());
        functions.add(new MaxDurationFunction());
        functions.add(new AvgDurationFunction());
        functions.add(new BadQualitySessionsFunction());
        functions.add(new SleeplessNightsFunction());
        functions.add(new ChronotypeFunction());
    }

    public List<SleepingSession> loadDataFromResources() throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(LOG_FILE_PATH);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            if (inputStream == null) {
                throw new IOException("Файл " + LOG_FILE_PATH + " не найден в папке resources");
            }

            return reader.lines()
                    .filter(line -> !line.trim().isEmpty())
                    .map(line -> {
                        String[] parts = line.split(";");
                        if (parts.length != 3) {
                            throw new IllegalArgumentException("Некорректный формат строки: " + line);
                        }
                        try {
                            LocalDateTime sleepStart = LocalDateTime.parse(parts[0].trim(), formatter);
                            LocalDateTime wakeUp = LocalDateTime.parse(parts[1].trim(), formatter);
                            SleepQuality quality = SleepQuality.valueOf(parts[2].trim());
                            return new SleepingSession(sleepStart, wakeUp, quality);
                        } catch (Exception e) {
                            throw new IllegalArgumentException("Ошибка парсинга строки: " + line, e);
                        }
                    })
                    .collect(Collectors.toList());
        }
    }

    public void runAnalysis() {
        try {
            List<SleepingSession> sessions = loadDataFromResources();
            System.out.println("Анализ сна пользователя");
            System.out.println("=".repeat(40));

            functions.stream()
                    .map(function -> function.analyze(sessions))
                    .forEach(System.out::println);

        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка в формате данных: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SleepTrackerApp app = new SleepTrackerApp();
        app.runAnalysis();
    }
}