package com.example.archtst;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class UiTest2 {

    @BeforeAll
    static void setup() {
        // Укажите адрес, где запущен ваш фронтенд
        Configuration.baseUrl = "http://localhost:8080";
        //Configuration.browserSize = "1920x1080";
    }

    @Test
    void shouldCreateUserAndAssignRole() {
        //open("file:///D://Work//SashaProjectTest//Frontend_test//index.html");
        File file = new File("src/test/resources/index.html");
        String url = "file:///" + file.getAbsolutePath();
        open(url);

        // 1. Открываем модалку и создаем юзера
        $(".btn-success").click(); // Кнопка "Добавить пользователя"
        $("#createUserModal").shouldBe(visible);

        String uniqueEmail = "test_" + System.currentTimeMillis() + "@example.com";

        $("#createName").setValue("Тестовый Юзер");
        $("#createEmail").setValue(uniqueEmail); // Используем уникальный адрес
        $("#createAge").setValue("30");
        $("#createUserForm .btn-success").click();

        // 2. Проверяем, что юзер появился в таблице
        $("#usersTableBody").shouldHave(text("Тестовый Юзер"));
        $("#usersTableBody").shouldHave(text(uniqueEmail));

        // 3. Открываем модалку ролей (берем первую строку таблицы)
        $("#usersTableBody tr").$(".btn-outline-warning").click();
        $("#rolesModal").shouldBe(visible);

        // 4. Выдаем роль MANAGER
        $("#newRoleName").selectOptionByValue("ROLE_MANAGER");
        $("#rolesModal .btn-primary").click();

        // 5. Проверяем, что роль появилась в списке внутри модалки
        $("#rolesList").shouldHave(text("ROLE_MANAGER"));

        // 6. Закрываем модалку и проверяем Badge в основной таблице
        //$(".btn-close").click();
        $(".modal.show .btn-close").click();
        $("#usersTableBody tr").$(".badge").shouldHave(text("ROLE_MANAGER"));
    }

    @Test
    void shouldSearchUserByName() {
        File file = new File("src/test/resources/index.html");
        String url = "file:///" + file.getAbsolutePath();
        open(url);

        // Вводим имя в поиск
        $("#searchName").setValue("Тестовый");
        $("button[type='submit']").click();

        // Проверяем, что в таблице остались только нужные записи
        $$("#usersTableBody tr").shouldHave(CollectionCondition.size(1));
        $("#usersTableBody").shouldHave(text("Тестовый Юзер"));

        // Сбрасываем поиск
        $(".btn-secondary").click();
        $$("#usersTableBody tr").shouldHave(CollectionCondition.sizeGreaterThan(0));
    }
}
