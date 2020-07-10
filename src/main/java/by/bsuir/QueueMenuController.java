package by.bsuir;

import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import by.bsuir.service.clock.Clock;
import by.bsuir.service.loading.Load;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class QueueMenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button returnButton;

    @FXML
    private Text timeText;

    @FXML
    private Text dataText;

    @FXML
    private Text depositText;

    @FXML
    private Text putMoneyText;

    @FXML
    private Text bankGuaranteesText;

    @FXML
    private Text exchangeMoneyText;

    @FXML
    private Text bankText;

    private final String WELCOME_MENU = "welcomeMenu.fxml";

    @FXML
    synchronized void initialize() {
        //блок, передающий поля класса Text
        //в поток обработки очереди
        App.getThread2().setText1(depositText);
        App.getThread2().setText2(putMoneyText);
        App.getThread2().setText3(bankGuaranteesText);
        App.getThread2().setText4(exchangeMoneyText);

        showData(); //метод, отвечающий за вывод даты на экран

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

        //блок, отвечающий за вывод точного времени на экран
        Clock newClock = new Clock();
        newClock.setTextToShowTime(timeText);
        newClock.showTime();

        //при нажатии на кнопку возврат на главное меню
        returnButton.setOnAction(actionEvent -> Load.getInstance().hideWindow(WELCOME_MENU));
    }

    //метод, отвечающий за вывод даты на экран
    private void showData() {
        Calendar calendar = new GregorianCalendar();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month / 10 == 0) dataText.setText(day + ".0" + month);
        else dataText.setText(day + "." + month);
    }
}