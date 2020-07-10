package by.bsuir;

import by.bsuir.service.loading.Load;
import by.bsuir.service.locales.LocaleHandler;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.shape.QuadCurve;
import javafx.util.Duration;

import java.util.ResourceBundle;

public class WelcomeMenuController {

    @FXML
    private Button queueButton;

    @FXML
    private Button reviewButton;

    @FXML
    private Button statsButton;

    @FXML
    private Button registerButton;

    @FXML
    private Button exitButton;

    @FXML
    private ImageView shipImage;

    @FXML
    private ImageView stormerImage;

    @FXML
    private Button changeLanguageButton;

    private final String QUEUE_WINDOW = "queueMenu.fxml";
    private final String CREATE_CLIENT_WINDOW = "createClientMenu.fxml";
    private final String REVIEW_WINDOW = "reviewMenu.fxml";
    private final String STATS_WINDOW = "statsMenu.fxml";
    private final String THIS_WINDOW = "welcomeMenu.fxml";

    @FXML
    void initialize() {
        //при нажатии на кнопку переходит на окно "Очередь"
        queueButton.setOnAction(actionEvent -> Load.getInstance().hideWindow(QUEUE_WINDOW));

        //при нажатии на кнопку переходит на окно "Оценить Услугу"
        reviewButton.setOnAction(actionEvent -> Load.getInstance().hideWindow(REVIEW_WINDOW));

        //при нажатии на кнопку переходит на окно "Статистика"
        statsButton.setOnAction(actionEvent -> Load.getInstance().hideWindow(STATS_WINDOW));

        //при нажатии на кнопку переходит на окно "Зарегистрироваться"
        registerButton.setOnAction(actionEvent -> Load.getInstance().hideWindow(CREATE_CLIENT_WINDOW));

        //при нажатии на кнопку спрашивает, точно ли мы хотим выйти
        //если да, то выходит из системы
        //если нет, то остаемся на главном меню
        exitButton.setOnAction(actionEvent -> {
            ResourceBundle bundle = ResourceBundle.getBundle("translations", App.getLocale());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(bundle.getString("exit"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("wantExit"));
            alert.showAndWait();
            if (alert.getResult().getText().equals("OK")) {
                App.getThread2().getThread().stop();
                exitButton.getScene().getWindow().hide();
            }
        });

        //при нажатии на кнопку меняет язык системы
        changeLanguageButton.setOnAction(actionEvent -> {
            App.setLocale(LocaleHandler.changeLocale());
            Load.getInstance().hideWindow(THIS_WINDOW);
        });

        //запускает анимацию движения фотографии
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(60), shipImage);
        translateTransition.setToX(-150);
        translateTransition.setToY(20);
        translateTransition.play();

        //запускает анимацию увеличения фотографии
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(60), shipImage);
        double x = shipImage.getX();
        double y = shipImage.getY();
        scaleTransition.setFromX(x);
        scaleTransition.setFromY(y);
        scaleTransition.setByX(1);
        scaleTransition.setByY(1);
        scaleTransition.play();

        //задаем путь движения фотографии
        PathTransition pathTransition = new PathTransition();
        pathTransition.setNode(stormerImage);
        pathTransition.setDuration(Duration.seconds(3));
        pathTransition.setPath(new QuadCurve(stormerImage.getX() + 600, stormerImage.getY(), -720, 600, -2000, 400));

        //запускает анимацию изменения угла наклона фотографии
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(3), stormerImage);
        rotateTransition.setFromAngle(-15);
        rotateTransition.setToAngle(20);

        //запускает анимации PathTransition и RotateTransition одновременно
        ParallelTransition parallelTransition = new ParallelTransition(pathTransition, rotateTransition);
        parallelTransition.setDelay(Duration.seconds(15));
        parallelTransition.play();
    }
}