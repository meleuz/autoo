package ru.netology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class DeliveryCardTest {

    @BeforeEach
    void setUpAll() {
        open("http://localhost:9999");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
    }

    @Test
    void shouldSendFormWithCorrectData() {
        $("[data-test-id='city'] input").setValue(DataGenerator.getRandomCity());
        $("[data-test-id='date'] input").setValue(DataGenerator.getCorrectDate(3));
        $("[data-test-id='name'] input").setValue(DataGenerator.getRandomName());
        $("[data-test-id='phone'] input").setValue(DataGenerator.getRandomPhone());
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Запланировать")).click();
        $(byText("Успешно!")).waitUntil(visible, 5000);
        $("[data-test-id=success-notification] .notification__content").shouldHave(text("Встреча успешно запланирована на "+DataGenerator.getCorrectDate(3)));
        $("[data-test-id=success-notification] button").click();
    }

    @Test
    void shouldSendFormWithAnotherDateAndTheSameData() {
        final String city = DataGenerator.getRandomCity();
        final String name = DataGenerator.getRandomName();
        final String phone = DataGenerator.getRandomPhone();

        $("[data-test-id='city'] input").setValue(city);
        $("[data-test-id='date'] input").setValue(DataGenerator.getCorrectDate(3));
        $("[data-test-id='name'] input").setValue(name);
        $("[data-test-id='phone'] input").setValue(phone);
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=success-notification] .notification__content").shouldHave(text("Встреча успешно запланирована на "+DataGenerator.getCorrectDate(3)));
        $("[data-test-id=success-notification] button").click();

        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(DataGenerator.getCorrectDate(7));
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id='replan-notification'] .notification__content").shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $$("[data-test-id='replan-notification'] button").find(exactText("Перепланировать")).click();

        $("[data-test-id=success-notification] .notification__content").shouldHave(text("Встреча успешно запланирована на "+DataGenerator.getCorrectDate(7)));
    }

    @Test
    void shouldNotSendFormIfNotCorrectCity() {
        $("[data-test-id='city'] input").setValue(DataGenerator.getNotCorrectCity());
        $("[data-test-id='date'] input").setValue(DataGenerator.getCorrectDate(3));
        $("[data-test-id='name'] input").setValue(DataGenerator.getRandomName());
        $("[data-test-id='phone'] input").setValue(DataGenerator.getRandomPhone());
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldNotSendFormIfNotCorrectDate() {
        $("[data-test-id='city'] input").setValue(DataGenerator.getRandomCity());
        $("[data-test-id='date'] input").setValue(DataGenerator.getNotCorrectDate());
        $("[data-test-id='name'] input").setValue(DataGenerator.getRandomName());
        $("[data-test-id='phone'] input").setValue(DataGenerator.getRandomPhone());
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldNotSendFormIfNameInEnglish() {
        $("[data-test-id='city'] input").setValue(DataGenerator.getRandomCity());
        $("[data-test-id='date'] input").setValue(DataGenerator.getCorrectDate(3));
        $("[data-test-id='name'] input").setValue(DataGenerator.getNotCorrectName());
        $("[data-test-id='phone'] input").setValue(DataGenerator.getRandomPhone());
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldSendFormIfNameWithYo() {
        $("[data-test-id='city'] input").setValue(DataGenerator.getRandomCity());
        $("[data-test-id='date'] input").setValue(DataGenerator.getCorrectDate(3));
        $("[data-test-id='name'] input").setValue(DataGenerator.getNotCorrectNameWithYo());
        $("[data-test-id='phone'] input").setValue(DataGenerator.getRandomPhone());
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Запланировать")).click();
        $(byText("Успешно!")).waitUntil(visible, 5000);
        $("[data-test-id=success-notification] .notification__content").shouldHave(text("Встреча успешно запланирована на "+DataGenerator.getCorrectDate(3)));
        $("[data-test-id=success-notification] button").click();
    }

    @Test
    void shouldNotSendFormIfNotCorrectPhone() {
        $("[data-test-id='city'] input").setValue(DataGenerator.getRandomCity());
        $("[data-test-id='date'] input").setValue(DataGenerator.getCorrectDate(3));
        $("[data-test-id='name'] input").setValue(DataGenerator.getRandomName());
        $("[data-test-id='phone'] input").setValue(DataGenerator.getNotCorrectPhone());
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldNotSendFormIfNotAgreement() {
        $("[data-test-id='city'] input").setValue(DataGenerator.getRandomCity());
        $("[data-test-id='date'] input").setValue(DataGenerator.getCorrectDate(3));
        $("[data-test-id='name'] input").setValue(DataGenerator.getRandomName());
        $("[data-test-id='phone'] input").setValue(DataGenerator.getRandomPhone());
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=agreement].input_invalid .checkbox__text").shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    @Test
    void shouldNotSendEmptyForm() {
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.ESCAPE);
        $("[data-test-id='agreement'] .checkbox__box").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotSendFormIfEmptyCity() {
        $("[data-test-id='date'] input").setValue(DataGenerator.getCorrectDate(3));
        $("[data-test-id='name'] input").setValue(DataGenerator.getRandomName());
        $("[data-test-id='phone'] input").setValue(DataGenerator.getRandomPhone());
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotSendFormIfEmptyDate() {
        $("[data-test-id='city'] input").setValue(DataGenerator.getRandomCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='name'] input").setValue(DataGenerator.getRandomName());
        $("[data-test-id='phone'] input").setValue(DataGenerator.getRandomPhone());
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(exactText("Неверно введена дата"));
    }

    @Test
    void shouldNotSendFormIfEmptyName() {
        $("[data-test-id='city'] input").setValue(DataGenerator.getRandomCity());
        $("[data-test-id='date'] input").setValue(DataGenerator.getCorrectDate(3));
        $("[data-test-id='phone'] input").setValue(DataGenerator.getRandomPhone());
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotSendFormIfEmptyPhone() {
        $("[data-test-id='city'] input").setValue(DataGenerator.getRandomCity());
        $("[data-test-id='date'] input").setValue(DataGenerator.getCorrectDate(3));
        $("[data-test-id='name'] input").setValue(DataGenerator.getRandomName());
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

}
