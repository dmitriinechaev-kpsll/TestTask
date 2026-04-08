package com.example.archtst;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class UiEndToEndTest {

    @BeforeAll
    static void setup() {
        // Указываем браузер (по умолчанию Chrome)
        Configuration.browser = "chrome";
        // Можно поставить true, чтобы браузер не закрывался сразу и ты увидел результат глазами
        Configuration.holdBrowserOpen = false;
    }

    @Test
    void shouldCreateNewUserAndDisplayInTable() {
        // 1. Открываем наш локальный HTML файл.
        // ВАЖНО: Укажи здесь реальный путь до твоего файла index.html на компьютере!
        open("file:///D://Work//SashaProjectTest//Frontend_test//index.html");

        // 2. Ждем, пока таблица загрузится (бэкенд должен быть запущен!)
        // Selenide сам умно ждет до 4 секунд, пока элемент не появится
        $("#usersTableBody").shouldNotHave(text("Загрузка..."));

        // 3. Кликаем на зеленую кнопку "Добавить пользователя"
        $("button[data-bs-target='#createUserModal']").click();

        // Генерируем уникальное число (например, текущее время в миллисекундах)
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());

        // 4. Ждем, пока модалка появится, и заполняем форму
        $("#createName").shouldBe(visible).setValue("UI Автотест " + uniqueSuffix);
        $("#createEmail").setValue("autotest" + uniqueSuffix + "@example.com"); // Уникальный email!
        $("#createAge").setValue("99");

        // 5. Нажимаем "Сохранить"
        $("#createUserForm button[type='submit']").click();

        // 6. Проверяем, что модалка закрылась
        $("#createUserModal").shouldNotBe(visible);

        // 7. Проверяем, что наш новый юзер появился в главной таблице
        $("#usersTableBody").shouldHave(text("UI Автотест"));
        $("#usersTableBody").shouldHave(text("autotest@example.com"));
        $("#usersTableBody").shouldHave(text("99"));
    }
    @Test
    void shouldSearchUserByName() {
        // 1. Открываем страницу (замени на свой путь)
        open("file:///D://Work//SashaProjectTest//Frontend_test//index.html");
        $("#usersTableBody").shouldNotHave(text("Загрузка..."));

        // 2. Вводим в поле поиска имя (например, "Глафира" - если она есть в базе)
        // Если Глафиры нет, впиши сюда "UI Автотест", которого мы только что создали
        String searchTarget = "UI Автотест";
        $("#searchName").setValue(searchTarget);

        // 3. Кликаем "Найти" (ищем кнопку по иконке лупы внутри)
        $("button[type='submit']").click();

        // 4. Ждем, пока таблица обновится.
        // Проверяем, что нужный юзер есть в результатах
        $("#usersTableBody").shouldHave(text(searchTarget));

        // 5. Проверяем, что в таблице ТОЛЬКО нужные строки (например, их немного)
        // Для надежности можем проверить, что таблица не пустая
        $$("#usersTableBody tr").shouldHave(sizeGreaterThan(0));
    }

}
