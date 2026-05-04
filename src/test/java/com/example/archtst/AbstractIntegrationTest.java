package com.example.archtst;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers // Включаем поддержку контейнеров
@ActiveProfiles("test") // Можно использовать отдельный профиль, если нужно
public abstract class AbstractIntegrationTest {

    // 1. Объявляем контейнер с Postgres
    // Мы используем ту же версию, что и в проде (желательно)
    @Container
    @ServiceConnection // Эта магия сама подставит url, username и password в Spring
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

}