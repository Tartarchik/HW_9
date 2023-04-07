package org.example;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class WebTest {

    @BeforeEach
    void setUp() {
        open("https://market.yandex.ru/");
    }

    @BeforeAll
    static void beforeAll() {
        Configuration.browserSize = "2560x1440";
    }

    @ValueSource(strings = {
            "Intel", "Iphone"
    })
    @ParameterizedTest(name = "В результате поиска в ЯндексМаркет должно отображаться минимум 5 товаров по запросу {0}")
    void searchResultForYandexMarket(String data) {
        $("#header-search").setValue(data).pressEnter();
        sleep(5000);
        $$("[data-index]").shouldHave(sizeGreaterThanOrEqual(5));
    }

    @CsvSource({
            "Intel , Intel",
            "Iphone, Iphone"
    })
    @ParameterizedTest(name = "По результату поиска запроса {0} должен отображаться текст {1}")
    void searchResultByExpectedText(String data, String expText) {
        $("#header-search").setValue(data).pressEnter();
        $("._1c3hh ").shouldHave(text(expText));
    }

    static Stream<Arguments> searchResultByExpectedListManufacturers() {
        return Stream.of(
                Arguments.of("phone", List.of("Apple", "Xiaomi", "Samsung", "Google", "Baseus")),
                Arguments.of("hi.fi", List.of("Denon", "Bowers & Wilkins", "Audio Pro", "DIGMA", "Ruizu"))
        );
    }

    @MethodSource
    @ParameterizedTest(name = "По результату поиска запроса {0} должен отображаться список производителей {1}")
    void searchResultByExpectedListManufacturers(String data, List<String> a) {
        sleep(10000);
        $("#header-search").setValue(data).pressEnter();
        $$(".XkAMv span ._1ZDAA").shouldHave(texts(a));
        sleep(5000);
    }
}