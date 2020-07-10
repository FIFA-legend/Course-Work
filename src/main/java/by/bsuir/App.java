package by.bsuir;

import by.bsuir.dao.Dao;
import by.bsuir.entity.BankServices;
import by.bsuir.entity.Client;
import by.bsuir.service.locales.LocaleHandler;
import by.bsuir.service.myLinkedList.MyLinkedList;
import by.bsuir.service.queue.PriorityQueue;
import by.bsuir.service.queueThreads.QueueThread;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

/**
 * JavaFX App
 */
public class App extends Application {

    //поле, содержащее сцену
    private static Stage mainStage;

    //поле, содержащее ссылку на поток обработки очереди
    private static QueueThread thread2;

    //поле, содержащее локаль приложения
    private static Locale locale = LocaleHandler.getLocale("rus");

    //лист, содержащий в себе все очереди
    private static final List<PriorityQueue> priorityQueues = new ArrayList<>(4);

    //список, содержащий в себе клиентов, которые могут оценить работу услуги
    private static final MyLinkedList<Client> reviewClients = new MyLinkedList<>();

    //геттеры и сеттеры
    public static Stage getMainStage() {
        return mainStage;
    }

    public static QueueThread getThread2() {
        return thread2;
    }

    public static Locale getLocale() {
        return locale;
    }

    public static void setLocale(Locale locale) {
        App.locale = locale;
    }

    public static List<PriorityQueue> getPriorityQueues() {
        return priorityQueues;
    }

    public static MyLinkedList<Client> getReviewClients() {
        return reviewClients;
    }

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        mainStage.setTitle("Электронная очередь");
        mainStage.setResizable(false);
        Dao.getInstance().clearMarks();
        PriorityQueue depositQueue = Dao.getInstance().readAllClients(BankServices.OPEN_DEPOSIT);
        PriorityQueue putMoneyQueue = Dao.getInstance().readAllClients(BankServices.PUT_MONEY);
        PriorityQueue bankGuaranteesQueue = Dao.getInstance().readAllClients(BankServices.BANK_GUARANTEES);
        PriorityQueue exchangeQueue = Dao.getInstance().readAllClients(BankServices.FOREIGN_MONEY_EXCHANGE);
        priorityQueues.add(depositQueue);
        priorityQueues.add(putMoneyQueue);
        priorityQueues.add(bankGuaranteesQueue);
        priorityQueues.add(exchangeQueue);
        thread2 = new QueueThread();
        Scene scene = new Scene(loadFXML());
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResource("/by/bsuir/images/Queue.png").toString()));
        stage.show();
    }

    //переопределенный метод, который отвечает за действия при закрытии программы
    //поток с обработкой клиентов останавливается
    //записываются средние оценки по услугам
    @Override
    public void stop() {
        Dao.getInstance().writeAverageMarks();
        thread2.getThread().stop();
    }

    //метод, который загружает главное меню программы
    private static Parent loadFXML() throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("translations", locale);
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("welcomeMenu.fxml"), bundle);
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}