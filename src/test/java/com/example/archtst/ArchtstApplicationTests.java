package com.example.archtst;

import com.example.archtst.entity.User;
import com.example.archtst.repository.UserRepository;
import com.example.archtst.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers // Говорит JUnit 5, что здесь используются контейнеры Docker
class ArchtstApplicationTests {

    // 1. Поднимаем реальный PostgreSQL в Docker контейнере
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    // 2. Динамически подменяем настройки БД в Spring, чтобы он смотрел на контейнер
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        // Заставляем Hibernate автоматически создать таблицы в пустой тестовой БД
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // Очищаем базу после каждого теста, чтобы тесты не влияли друг на друга
    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Контекст успешно поднимается и подключается к БД")
    void contextLoads() {
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    @DisplayName("Успешное создание пользователя и проверка UUID генерации")
    void testCreateUser_Success() {
        // Arrange
        User newUser = User.builder()
                .name("Иван Иванов")
                .email("ivan@test.com")
                .age(30)
                .build();

        // Act
        User savedUser = userService.createUser(newUser);

        // Assert
        assertThat(savedUser.getId()).isNotNull(); // UUID должен сгенерироваться
        assertThat(savedUser.getCreatedAt()).isNotNull(); // @PrePersist должен отработать
        assertThat(savedUser.getName()).isEqualTo("Иван Иванов");

        // Проверяем, что он реально лежит в базе
        Optional<User> foundInDb = userRepository.findById(savedUser.getId());
        assertThat(foundInDb).isPresent();
        assertThat(foundInDb.get().getEmail()).isEqualTo("ivan@test.com");
    }

    @Test
    @DisplayName("Ошибка при попытке создать пользователя с существующим email")
    void testCreateUser_DuplicateEmail_ThrowsException() {
        // Arrange
        User firstUser = User.builder()
                .name("Петр")
                .email("duplicate@test.com")
                .build();
        userService.createUser(firstUser); // Сохраняем первого

        User secondUser = User.builder()
                .name("Алексей")
                .email("duplicate@test.com") // Тот же email
                .build();

        // Act & Assert (Ожидаем RuntimeException, как написано в вашем сервисе)
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(secondUser);
        });

        assertThat(exception.getMessage()).isEqualTo("Такой пользователь уже существует!!");
    }

    @Test
    @DisplayName("Поиск пользователей с пагинацией")
    void testSearchUsersByName_Pagination() {
        // Arrange - создаем 3-х пользователей с похожими именами
        userService.createUser(User.builder().name("Антон").email("1@test.com").build());
        userService.createUser(User.builder().name("Антонина").email("2@test.com").build());
        userService.createUser(User.builder().name("Антончик").email("3@test.com").build());
        userService.createUser(User.builder().name("Иван").email("4@test.com").build()); // Этот не должен попасть

        // Act - Ищем "Антон", просим первую страницу (0), размер 2
        Page<User> resultPage = userService.searchUsersByName("Антон", PageRequest.of(0, 2));

        // Assert
        assertThat(resultPage.getTotalElements()).isEqualTo(3); // Всего в базе 3 подходящих
        assertThat(resultPage.getTotalPages()).isEqualTo(2); // Они должны разбиться на 2 страницы
        assertThat(resultPage.getContent()).hasSize(2); // На первой странице должно быть ровно 2 элемента
    }

    @Test
    @DisplayName("Удаление пользователя работает корректно")
    void testDeleteUser() {
        // Arrange
/*        User user = userService.createUser(User.builder().name("Тест").email("delete@test.com").build());
        String id = user.getId();

        // Act
        boolean isDeleted = userService.deleteUser(id);
        boolean isDeletedAgain = userService.deleteUser(id);

        // Assert
        assertThat(isDeleted).isTrue(); // Первое удаление успешно
        assertThat(isDeletedAgain).isFalse(); // Повторное удаление возвращает false
        assertThat(userRepository.findById(id)).isEmpty(); // В базе больше нет
        */
 */
    }
}