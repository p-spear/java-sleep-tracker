package ru.yandex.practicum.sleeptracker;

public enum Chronotype {
    OWL,        // Сова
    LARK,       // Жаворонок
    PIGEON;     // Голубь

    @Override
    public String toString() {
        switch (this) {
            case OWL: return "Сова";
            case LARK: return "Жаворонок";
            case PIGEON: return "Голубь";
            default: return super.toString();
        }
    }
}
