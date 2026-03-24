package com.example.archtst.controller;

import com.example.archtst.AbstractIntegrationTest;
import com.example.archtst.dto.UserRequestDTO;
import com.example.archtst.dto.UserSearchRequestDTO;
import com.example.archtst.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc // Позволяет делать запросы к API без запуска реального Tomcat
class UserControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // Наш инструмент для "стрельбы" запросами

    @Autowired
    private UserRepository userRepository; // Чтобы чистить базу перед тестами

    @Autowired
    private ObjectMapper objectMapper; // Превращает объекты в JSON и обратно

    @BeforeEach
    void setUp() {
        // Перед каждым тестом чистим базу, чтобы данные не смешивались
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateAndFindUser() throws Exception {
        // --- 1. Подготовка данных (Given) ---
        UserRequestDTO newUser = new UserRequestDTO();
        newUser.setName("Василий Тестовый");
        newUser.setEmail("vasya_test@example.com");
        newUser.setAge(25);
        newUser.setShoeSize(42);

        // --- 2. Действие: Создаем юзера (When) ---
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk()) // Ожидаем 200 OK
                .andExpect(jsonPath("$.id").isNotEmpty()) // Проверяем, что ID вернулся
                .andExpect(jsonPath("$.name").value("Василий Тестовый"));

        // --- 3. Действие: Ищем юзера через фильтр (And check search) ---

        // Формируем запрос поиска (ищем по части имени и диапазону возраста)
        UserSearchRequestDTO searchRequest = new UserSearchRequestDTO();
        searchRequest.setNames(Set.of("Василий"));
        searchRequest.setMinAge(20);
        searchRequest.setMaxAge(30);

        mockMvc.perform(post("/api/users/search") // Или какой у тебя URL для поиска
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1)) // Должен найтись 1 юзер
                .andExpect(jsonPath("$.content[0].email").value("vasya_test@example.com"));
    }

    @Test
    void shouldFailValidation() throws Exception {
        // Проверяем, что валидация работает
        UserRequestDTO badUser = new UserRequestDTO();
        badUser.setName(""); // Пустое имя!

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badUser)))
                .andExpect(status().isBadRequest()) // Ждем 400
                .andExpect(jsonPath("$.errors.name").exists()); // Ждем ошибку в поле name
    }
}