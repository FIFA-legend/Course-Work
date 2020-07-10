package by.bsuir.service.loading;

import by.bsuir.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.ResourceBundle;

public class Load {

    private static Load INSTANCE;

    private Load() {}

    //паттерн проектирования Singleton,
    //который запрещает создание больше одного экземпляра данного класса
    public static Load getInstance() {
        if (INSTANCE == null) {
            synchronized (Load.class) {
                if (INSTANCE == null) INSTANCE = new Load();
            }
        }
        return INSTANCE;
    }

    //метод, который делает переход между окнами
    public void hideWindow(String str) {
        FXMLLoader loader = getLoader(str);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        App.getMainStage().setScene(new Scene(root));
        App.getMainStage().show();
    }

    //метод, который возвращает FXMLLoader для перехода на новое окно
    private FXMLLoader getLoader(String fxml) {
        ResourceBundle bundle = ResourceBundle.getBundle("translations", App.getLocale());
        return new FXMLLoader(App.class.getResource(fxml), bundle);
    }
}
