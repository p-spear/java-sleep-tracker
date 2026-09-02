package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты приложения Sleep Tracker Analyzer")
public class SleepTrackerAppTest {
    private List<SleepingSession> testSessions;

    @BeforeEach
    void setUp() {
        testSessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 22, 15),
                        LocalDateTime.of(2025, 10, 2, 8, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 8, 0),
                        SleepQuality.NORMAL
                ),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 3, 14, 30),
                        LocalDateTime.of(2025, 10, 3, 15, 20),
                        SleepQuality.NORMAL
                ),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 3, 23, 30),
                        LocalDateTime.of(2025, 10, 4, 6, 20),
                        SleepQuality.BAD
                )
        );
    }

    // ==================== Тесты для TotalSessionsFunction ====================

    @Test
    @DisplayName("Подсчет общего количества сессий сна - успешный сценарий")
    void testTotalSessionsFunction() {
        TotalSessionsFunction func = new TotalSessionsFunction();
        SleepAnalysisResult result = func.analyze(testSessions);
        assertEquals(4, result.getValue());
        assertEquals("Общее количество сессий сна", result.getDescription());
    }

    @Test
    @DisplayName("Подсчет общего количества сессий сна - пустой список")
    void testTotalSessionsFunctionEmpty() {
        TotalSessionsFunction func = new TotalSessionsFunction();
        SleepAnalysisResult result = func.analyze(List.of());
        assertEquals(0, result.getValue());
    }

    // ==================== Тесты для MinDurationFunction ====================

    @Test
    @DisplayName("Поиск минимальной продолжительности сессии - успешный сценарий")
    void testMinDurationFunction() {
        MinDurationFunction func = new MinDurationFunction();
        SleepAnalysisResult result = func.analyze(testSessions);
        assertEquals(50, result.getValue()); // 15:30 - 14:30 = 50 минут
    }

    @Test
    @DisplayName("Поиск минимальной продолжительности сессии - пустой список")
    void testMinDurationFunctionEmpty() {
        MinDurationFunction func = new MinDurationFunction();
        SleepAnalysisResult result = func.analyze(List.of());
        assertEquals(0, result.getValue());
    }

    // ==================== Тесты для MaxDurationFunction ====================

    @Test
    @DisplayName("Поиск максимальной продолжительности сессии - успешный сценарий")
    void testMaxDurationFunction() {
        MaxDurationFunction func = new MaxDurationFunction();
        SleepAnalysisResult result = func.analyze(testSessions);
        assertEquals(585, result.getValue()); // 10/1 22:15 до 10/2 08:00 = 585 минут
    }

    @Test
    @DisplayName("Поиск максимальной продолжительности сессии - пустой список")
    void testMaxDurationFunctionEmpty() {
        MaxDurationFunction func = new MaxDurationFunction();
        SleepAnalysisResult result = func.analyze(List.of());
        assertEquals(0, result.getValue());
    }

    // ==================== Тесты для AvgDurationFunction ====================

    @Test
    @DisplayName("Расчет средней продолжительности сессии - успешный сценарий")
    void testAvgDurationFunction() {
        AvgDurationFunction func = new AvgDurationFunction();
        SleepAnalysisResult result = func.analyze(testSessions);
        // (585 + 540 + 50 + 410) / 4 = 396.25
        assertEquals(396.3, (Double) result.getValue(), 0.1);
    }

    @Test
    @DisplayName("Расчет средней продолжительности сессии - пустой список")
    void testAvgDurationFunctionEmpty() {
        AvgDurationFunction func = new AvgDurationFunction();
        SleepAnalysisResult result = func.analyze(List.of());
        assertEquals(0.0, (Double) result.getValue(), 0.1);
    }

    // ==================== Тесты для BadQualitySessionsFunction ====================

    @Test
    @DisplayName("Подсчет сессий с плохим качеством сна - успешный сценарий")
    void testBadQualitySessionsFunction() {
        BadQualitySessionsFunction func = new BadQualitySessionsFunction();
        SleepAnalysisResult result = func.analyze(testSessions);
        assertEquals(1, result.getValue());
    }

    @Test
    @DisplayName("Подсчет сессий с плохим качеством сна - пустой список")
    void testBadQualitySessionsFunctionEmpty() {
        BadQualitySessionsFunction func = new BadQualitySessionsFunction();
        SleepAnalysisResult result = func.analyze(List.of());
        assertEquals(0, result.getValue());
    }

    // ==================== Тесты для SleeplessNightsFunction ====================

    @Test
    @DisplayName("Подсчет бессонных ночей - все ночи с сессиями сна")
    void testSleeplessNightsFunction() {
        SleeplessNightsFunction func = new SleeplessNightsFunction();
        SleepAnalysisResult result = func.analyze(testSessions);
        assertEquals(0, result.getValue());
    }

    @Test
    @DisplayName("Подсчет бессонных ночей - только дневная сессия (начало до 12)")
    void testSleeplessNightsWithDaySessionBeforeNoon() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 11, 0),
                        LocalDateTime.of(2025, 10, 1, 15, 0),
                        SleepQuality.NORMAL
                )
        );
        SleeplessNightsFunction func = new SleeplessNightsFunction();
        SleepAnalysisResult result = func.analyze(sessions);
        assertEquals(1, result.getValue());
    }

    @Test
    @DisplayName("Подсчет бессонных ночей - только дневная сессия (начало после 12)")
    void testSleeplessNightsWithDaySessionAfterNoon() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 13, 0),
                        LocalDateTime.of(2025, 10, 1, 15, 0),
                        SleepQuality.NORMAL
                )
        );
        SleeplessNightsFunction func = new SleeplessNightsFunction();
        SleepAnalysisResult result = func.analyze(sessions);
        assertEquals(0, result.getValue());
    }

    @Test
    @DisplayName("Подсчет бессонных ночей - ночная сессия присутствует")
    void testSleeplessNightsWithNightSession() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 23, 0),
                        LocalDateTime.of(2025, 10, 2, 7, 0),
                        SleepQuality.GOOD
                )
        );
        SleeplessNightsFunction func = new SleeplessNightsFunction();
        SleepAnalysisResult result = func.analyze(sessions);
        assertEquals(0, result.getValue());
    }

    @Test
    @DisplayName("Подсчет бессонных ночей - сессия заканчивается в 5:30 (до 6:00)")
    void testSleeplessNightsWithSessionEndingAt530() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 23, 30),
                        LocalDateTime.of(2025, 10, 2, 5, 30),
                        SleepQuality.GOOD
                )
        );
        SleeplessNightsFunction func = new SleeplessNightsFunction();
        SleepAnalysisResult result = func.analyze(sessions);
        assertEquals(0, result.getValue());
    }

    @Test
    @DisplayName("Подсчет бессонных ночей - сессия начинается в 6:30 (после 6:00)")
    void testSleeplessNightsWithSessionStartingAt630() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 6, 30),
                        LocalDateTime.of(2025, 10, 1, 8, 0),
                        SleepQuality.GOOD
                )
        );
        SleeplessNightsFunction func = new SleeplessNightsFunction();
        SleepAnalysisResult result = func.analyze(sessions);
        assertEquals(1, result.getValue());
    }

    @Test
    @DisplayName("Подсчет бессонных ночей - сессия начинается до 12 (считаем предыдущую ночь)")
    void testSleeplessNightsWithSessionBeforeNoon() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 10, 0),
                        LocalDateTime.of(2025, 10, 1, 11, 0),
                        SleepQuality.GOOD
                )
        );
        SleeplessNightsFunction func = new SleeplessNightsFunction();
        SleepAnalysisResult result = func.analyze(sessions);
        assertEquals(1, result.getValue());
    }

    // ==================== Тесты для ChronotypeFunction ====================

    @Test
    @DisplayName("Определение хронотипа - Сова (засыпание после 23:00 И пробуждение после 9:00)")
    void testChronotypeOwl() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 23, 30),
                        LocalDateTime.of(2025, 10, 2, 9, 30),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 2, 21, 30),
                        LocalDateTime.of(2025, 10, 3, 6, 20),
                        SleepQuality.NORMAL
                ),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 3, 23, 00),
                        LocalDateTime.of(2025, 10, 4, 9, 00),
                        SleepQuality.BAD
                )
        );
        ChronotypeFunction func = new ChronotypeFunction();
        SleepAnalysisResult result = func.analyze(sessions);
        assertEquals(Chronotype.OWL, result.getValue());
    }

    @Test
    @DisplayName("Определение хронотипа - Жаворонок (засыпание до 22:00 И пробуждение до 7:00)")
    void testChronotypeLark() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 21, 30),
                        LocalDateTime.of(2025, 10, 2, 6, 30),
                        SleepQuality.GOOD
                )
        );
        ChronotypeFunction func = new ChronotypeFunction();
        SleepAnalysisResult result = func.analyze(sessions);
        assertEquals(Chronotype.LARK, result.getValue());
    }

    @Test
    @DisplayName("Определение хронотипа - Голубь (промежуточный случай)")
    void testChronotypePigeon() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 22, 30),
                        LocalDateTime.of(2025, 10, 2, 7, 30),
                        SleepQuality.GOOD
                )
        );
        ChronotypeFunction func = new ChronotypeFunction();
        SleepAnalysisResult result = func.analyze(sessions);
        assertEquals(Chronotype.PIGEON, result.getValue());
    }

    @Test
    @DisplayName("Определение хронотипа - ничья между типами, возвращается Голубь")
    void testChronotypeTie() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 23, 30),
                        LocalDateTime.of(2025, 10, 2, 9, 30),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 2, 21, 30),
                        LocalDateTime.of(2025, 10, 3, 6, 30),
                        SleepQuality.GOOD
                )
        );
        ChronotypeFunction func = new ChronotypeFunction();
        SleepAnalysisResult result = func.analyze(sessions);
        assertEquals(Chronotype.PIGEON, result.getValue());
    }

    @Test
    @DisplayName("Определение хронотипа - игнорирование дневных сессий сна")
    void testChronotypeIgnoresDaySessions() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 14, 0),
                        LocalDateTime.of(2025, 10, 1, 15, 0),
                        SleepQuality.NORMAL
                ),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 23, 30),
                        LocalDateTime.of(2025, 10, 2, 9, 30),
                        SleepQuality.GOOD
                )
        );
        ChronotypeFunction func = new ChronotypeFunction();
        SleepAnalysisResult result = func.analyze(sessions);
        assertEquals(Chronotype.OWL, result.getValue());
    }
}