package com.example.archtst.constant;

public final class Urls {
    private Urls() {}

    // Базовый путь (если захотите перенести его из интерфейса или использовать в логах/тестах)
    public static final String BASE_URL = "/api/users";

    // Пути методов
    public static final String BY_ID = "/{id}";
    public static final String SEARCH = "/search";
    public static final String STATS = "/stats";
    public static final String HEALTH = "/health";
}
