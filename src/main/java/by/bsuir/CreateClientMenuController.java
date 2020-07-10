package by.bsuir;

import java.net.URL;
import java.util.ResourceBundle;

import by.bsuir.dao.Dao;
import by.bsuir.entity.BankServices;
import by.bsuir.entity.Client;
import by.bsuir.service.clock.Clock;
import by.bsuir.service.loading.Load;
import by.bsuir.service.locales.LocaleHandler;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class CreateClientMenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text timeText;

    @FXML
    private Button returnButton;

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameFiled;

    @FXML
    private CheckBox checkBox1;

    @FXML
    private CheckBox checkBox2;

    @FXML
    private Button agreeButton;

    @FXML
    private RadioButton openDepositRadio;

    @FXML
    private RadioButton putMoneyRadio;

    @FXML
    private RadioButton bankGuaranteesRadio;

    @FXML
    private RadioButton exchangeMoneyRadio;

    @FXML
    private ImageView ufo1;

    @FXML
    private ImageView ufo2;

    private final String WELCOME_MENU = "welcomeMenu.fxml";

    private int priority;
    private static int id;

    public static int getId() {
        return id;
    }

    @FXML
    void initialize() {
        //блок, отвечающий за вывод точного времени на экран
        Clock newClock = new Clock();
        newClock.setTextToShowTime(timeText);
        newClock.showTime();

        //объединяем RadioButton в одну группу
        //по умолчанию выбираем услугу "Открытие Депозита"
        ToggleGroup group = new ToggleGroup();
        putMoneyRadio.setToggleGroup(group);
        openDepositRadio.setToggleGroup(group);
        bankGuaranteesRadio.setToggleGroup(group);
        exchangeMoneyRadio.setToggleGroup(group);
        group.selectToggle(openDepositRadio);

        //при нажатии на кнопку возвращает на главное меню
        returnButton.setOnAction(actionEvent -> Load.getInstance().hideWindow(WELCOME_MENU));

        //блок, который отвечает за отображение анимаций
        showAnimation(ufo1, Duration.seconds(0));
        showAnimation(ufo2, Duration.seconds(1));

        //при нажатии на кнопку считывает данные с формы
        //если данные корректные, то сохранение данных
        //и запись в определенную очередь
        //если некорректные, то вывод соответствующего сообщения об ошибке
        //что именно некорректно
        agreeButton.setOnAction(actionEvent -> {
            String name = nameField.getText().trim();
            String surname = surnameFiled.getText().trim();
            if (!name.isEmpty() && !surname.isEmpty()) {
                if (isCorrect(name) && isCorrect(surname)) {
                    BankServices service;
                    if (checkBox1.isSelected()) priority++;
                    if (checkBox2.isSelected()) priority++;
                    if (group.getSelectedToggle() == putMoneyRadio) {
                        service = BankServices.PUT_MONEY;
                        Client client = new Client(priority, name, surname, service);
                        id = client.getId();
                        if (App.getPriorityQueues().get(1).insert(client)) {
                            Dao.getInstance().saveToDB(client);
                            getAlert(true, Alert.AlertType.INFORMATION);
                        } else {
                            getOverFlowAlert();
                        }
                    }
                    if (group.getSelectedToggle() == openDepositRadio) {
                        service = BankServices.OPEN_DEPOSIT;
                        Client client = new Client(priority, name, surname, service);
                        id = client.getId();
                        if (App.getPriorityQueues().get(0).insert(client)) {
                            Dao.getInstance().saveToDB(client);
                            getAlert(true, Alert.AlertType.INFORMATION);
                        } else {
                            getOverFlowAlert();
                        }
                    }
                    if (group.getSelectedToggle() == bankGuaranteesRadio) {
                        service = BankServices.BANK_GUARANTEES;
                        Client client = new Client(priority, name, surname, service);
                        id = client.getId();
                        if (App.getPriorityQueues().get(2).insert(client)) {
                            Dao.getInstance().saveToDB(client);
                            getAlert(true, Alert.AlertType.INFORMATION);
                        } else {
                            getOverFlowAlert();
                        }
                    }
                    if (group.getSelectedToggle() == exchangeMoneyRadio) {
                        service = BankServices.FOREIGN_MONEY_EXCHANGE;
                        Client client = new Client(priority, name, surname, service);
                        id = client.getId();
                        if (App.getPriorityQueues().get(3).insert(client)) {
                            Dao.getInstance().saveToDB(client);
                            getAlert(true, Alert.AlertType.INFORMATION);
                        } else {
                            getOverFlowAlert();
                        }
                    }
                    nameField.clear();
                    surnameFiled.clear();
                    priority = 0;
                    checkBox1.setSelected(false);
                    checkBox2.setSelected(false);
                    group.selectToggle(openDepositRadio);
                } else {
                    getAlert(false, Alert.AlertType.ERROR);
                }
            } else {
                getAlert(true, Alert.AlertType.ERROR);
            }
        });
    }

    //метод, который отвечает за отображение анимаций
    private void showAnimation(ImageView image, Duration duration) {
        FadeTransition fd = new FadeTransition(Duration.seconds(2), image);
        fd.setFromValue(1);
        fd.setDelay(duration);
        fd.setToValue(0);
        fd.setCycleCount(Animation.INDEFINITE);
        fd.setAutoReverse(true);
        fd.play();
    }

    //метод, отвечающий за проверку корректности введенных данных
    private boolean isCorrect(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isLetter(str.charAt(i))) return false;
        }
        return true;
    }

    //метод, отвечающий за вывод сообщения об ошибке
    private void getAlert(boolean empty, Alert.AlertType type) {
        ResourceBundle bundle = ResourceBundle.getBundle("translations", App.getLocale());
        Alert alert;
        if (type == Alert.AlertType.INFORMATION) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString("information"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("lineNumber") + " " + id);
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("error"));
            alert.setHeaderText(null);
            alert.setContentText(empty ? bundle.getString("notFillRequiredFields") : bundle.getString("invalidSymbol"));
        }
        alert.showAndWait();
    }

    //метод, отвечающий за вывод сообщения о переполнении очереди
    private void getOverFlowAlert() {
        ResourceBundle bundle = ResourceBundle.getBundle("translations", App.getLocale());
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(bundle.getString("error"));
        alert.setHeaderText(null);
        alert.setContentText(bundle.getString("queueOverflow") + "\r\n" + bundle.getString("returnTomorrow"));
        alert.showAndWait();
    }
}