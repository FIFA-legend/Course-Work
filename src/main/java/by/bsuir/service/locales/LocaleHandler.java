package by.bsuir.service.locales;

import by.bsuir.App;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LocaleHandler {

    //класс, который осуществляет выбор языка в приложении
    private static final Map<String, Locale> LOCALES = new HashMap<>();

    //в этом статисческом блоке мы в map LOCALES кладем 2 локалт
    //русскую и английскую
    static {
        LOCALES.put("rus", new Locale("ru", "RU"));
        LOCALES.put("eng", new Locale("en", "EN"));
    }

    //метод, который возвращает локаль
    public static Locale getLocale(String str) {
        if (LOCALES.containsKey(str)) {
            return LOCALES.get(str);
        } else {
            return Locale.getDefault();
        }
    }

    //метод, который меняет локаль
    //и соответственно язык в приложении
    public static Locale changeLocale() {
        return App.getLocale().equals(LOCALES.get("rus")) ? LOCALES.get("eng") : LOCALES.get("rus");
    }
}