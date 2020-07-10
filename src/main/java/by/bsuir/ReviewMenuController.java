package by.bsuir;

import by.bsuir.dao.Dao;
import by.bsuir.entity.Client;
import by.bsuir.service.clock.Clock;
import by.bsuir.service.loading.Load;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class ReviewMenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text timeText;

    @FXML
    private Button homeButton;

    @FXML
    private Button perfectSmileButton;

    @FXML
    private Button goodSmileButton;

    @FXML
    private Button normalSmileButton;

    @FXML
    private Button badSmileButton;

    @FXML
    private Button terribleSmileButton;

    @FXML
    private TextField numberField;

    @FXML
    private Text bankText;

    private final String WELCOME_WINDOW = "welcomeMenu.fxml";

    @FXML
    void initialize() {
        //блок, отвечающий за вывод точного времени на экран
        Clock newClock = new Clock();
        newClock.setTextToShowTime(timeText);
        newClock.showTime();

        //при нажатии возвращает на главное меню
        homeButton.setOnAction(actionEvent -> Load.getInstance().hideWindow(WELCOME_WINDOW));

        //метод, который записывает 1, если указанный номер есть в базе
        //иначе выводит ошибку
        terribleSmileButton.setOnAction(actionEvent -> {
            if (writeMark(1)) {
                Load.getInstance().hideWindow(WELCOME_WINDOW);
            } else {
                getErrorAlert();
            }
        });

        //метод, который записывает 2, если указанный номер есть в базе
        //иначе выводит ошибку
        badSmileButton.setOnAction(actionEvent -> {
            if (writeMark(2)) {
                Load.getInstance().hideWindow(WELCOME_WINDOW);
            } else {
                getErrorAlert();
            }
        });

        //метод, который записывает 3, если указанный номер есть в базе
        //иначе выводит ошибку
        normalSmileButton.setOnAction(actionEvent -> {
            if (writeMark(3)) {
                Load.getInstance().hideWindow(WELCOME_WINDOW);
            } else {
                getErrorAlert();
            }
        });

        //метод, который записывает 4, если указанный номер есть в базе
        //иначе выводит ошибку
        goodSmileButton.setOnAction(actionEvent -> {
            if (writeMark(4)) {
                Load.getInstance().hideWindow(WELCOME_WINDOW);
            } else {
                getErrorAlert();
            }
        });

        //метод, который записывает 5, если указанный номер есть в базе
        //иначе выводит ошибку
        perfectSmileButton.setOnAction(actionEvent -> {
            if (writeMark(5)) {
                Load.getInstance().hideWindow(WELCOME_WINDOW);
            } else {
                getErrorAlert();
            }
        });

        //создание анимации, которая определенным образом
        //выводит надпись "Банк/Bank" на экран
        String str = " " + bankText.getText() + " ";
        Animation animation = new Transition() {
            {
                setCycleDuration(Duration.seconds(4));
                setCycleCount(1);
            }
            @Override
            protected void interpolate(double frac) {
                int length = str.length();
                int n = (int) (length * frac);
                bankText.setText(str.substring(0, n));
            }
        };
        animation.play();
        animation.setOnFinished(actionEvent -> {
            animation.setDelay(Duration.seconds(10));
            animation.play();
        });
    }

    //метод, выводящий на экран сообщение об ошибке
    private void getErrorAlert() {
        ResourceBundle bundle = ResourceBundle.getBundle("translations", App.getLocale());
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(bundle.getString("error"));
        alert.setHeaderText(null);
        alert.setContentText(bundle.getString("incorrectId"));
        alert.showAndWait();
    }

    //метод, считывающий номер клиента, который он ввел на форме
    private int readTextField() {
        try {
            return Integer.parseInt(numberField.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //метод, который записывает оценку, если указанный номер есть в базе
    //иначе выводит ошибку
    private boolean writeMark(int mark) {
        int id = readTextField();
        Client client = null;
        for (int i = 0; i < App.getReviewClients().getLength(); i++) {
            if (App.getReviewClients().get(i).getId() == id) {
                client = App.getReviewClients().get(i);
                id = i;
                break;
            }
        }
        if (client != null) {
            Dao.getInstance().writeReview(mark, client);
            App.getReviewClients().delete(id);
            return true;
        } else {
            return false;
        }
    }
}