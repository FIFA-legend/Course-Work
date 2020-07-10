package by.bsuir;

import by.bsuir.dao.Dao;
import by.bsuir.entity.BankServices;
import by.bsuir.service.loading.Load;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.*;

public class StatsMenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button homeButton;

    @FXML
    private PieChart pieChart;

    @FXML
    private Slider slider;

    @FXML
    private RadioButton openDepositRadio;

    @FXML
    private RadioButton exchangeRadio;

    @FXML
    private RadioButton bankGuaranteesRadio;

    @FXML
    private RadioButton putMoneyRadio;

    @FXML
    private TextField textField;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private LineChart<String, Number> lineChart;

    private final String WELCOME_WINDOW = "welcomeMenu.fxml";

    private final Label label = initLabel();

    private int timePeriod = 3;

    @FXML
    void initialize() {
        //устанавливаем изначальный период времени
        textField.setText(String.valueOf(timePeriod));

        //объединяем RadioButton в группу
        ToggleGroup group = new ToggleGroup();
        openDepositRadio.setToggleGroup(group);
        exchangeRadio.setToggleGroup(group);
        putMoneyRadio.setToggleGroup(group);
        bankGuaranteesRadio.setToggleGroup(group);
        group.selectToggle(openDepositRadio);

        //при нажатии на эту кнопку выводит графики за эту услугу
        openDepositRadio.setOnAction(actionEvent -> initializeCharts(timePeriod, BankServices.OPEN_DEPOSIT));

        //при нажатии на эту кнопку выводит графики за эту услугу
        exchangeRadio.setOnAction(actionEvent -> initializeCharts(timePeriod, BankServices.FOREIGN_MONEY_EXCHANGE));

        //при нажатии на эту кнопку выводит графики за эту услугу
        putMoneyRadio.setOnAction(actionEvent -> initializeCharts(timePeriod, BankServices.PUT_MONEY));

        //при нажатии на эту кнопку выводит графики за эту услугу
        bankGuaranteesRadio.setOnAction(actionEvent -> initializeCharts(timePeriod, BankServices.BANK_GUARANTEES));

        //инициализирует графики начальными значениями
        initializeCharts(timePeriod, BankServices.OPEN_DEPOSIT);

        //при движении рычага Slider выводит статистику на грфиках за выбранный промежуток времени
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int newTimePeriod = newValue.intValue();
            if (newTimePeriod != timePeriod) {
                textField.setText(newTimePeriod + "");
                BankServices bankServices = null;
                if (group.getSelectedToggle() == putMoneyRadio) bankServices = BankServices.PUT_MONEY;
                if (group.getSelectedToggle() == openDepositRadio) bankServices = BankServices.OPEN_DEPOSIT;
                if (group.getSelectedToggle() == bankGuaranteesRadio) bankServices = BankServices.BANK_GUARANTEES;
                if (group.getSelectedToggle() == exchangeRadio) bankServices = BankServices.FOREIGN_MONEY_EXCHANGE;
                initializeCharts(newTimePeriod, bankServices);
                timePeriod = newTimePeriod;
            }
        });

        //при нажатии на кнопку возвращает на главный экран
        homeButton.setOnAction(actionEvent -> Load.getInstance().hideWindow(WELCOME_WINDOW));

        anchorPane.getChildren().add(label);
    }

    //придает Label (надпись) определенные свойства
    private Label initLabel() {
        final Label label = new Label();
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font: 18 arial;");
        return label;
    }

    //метод, изменяющий графики в соответствии с указанным промежутком времени
    private void initializeCharts(int timePeriod, BankServices bs) {
        Map<String, List<Integer>> map = Dao.getInstance().getMarksForTimePeriod(timePeriod, bs);
        initializeLineChart(map);
        initializeBarChart(map);
        initializePieChart(map);
    }

    //инициализирует линейный график
    private void initializeLineChart(Map<String, List<Integer>> map) {
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        map.keySet().forEach(key -> {
            series.getData().add(new XYChart.Data<>(key, map.get(key).size()));
        });
        lineChart.getXAxis().setTickLabelFill(Color.WHITE);
        lineChart.getYAxis().setTickLabelFill(Color.WHITE);
        lineChart.getData().removeAll(lineChart.getData());
        lineChart.getData().add(series);
    }

    //инициализирует гистограмму
    private void initializeBarChart(Map<String, List<Integer>> map) {
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        map.keySet().forEach(key -> {
            series.getData().add(new XYChart.Data<>(key, countAverage(map.get(key))));
        });
        barChart.getXAxis().setTickLabelFill(Color.WHITE);
        barChart.getYAxis().setTickLabelFill(Color.WHITE);
        barChart.getData().removeAll(barChart.getData());
        barChart.getData().add(series);
    }

    //инициализирует круговую диаграмму
    private void initializePieChart(Map<String, List<Integer>> map) {
        label.setText("");

        ObservableList<PieChart.Data> values = FXCollections.observableArrayList();
        for (int i = 1; i <= 5; i++) {
            values.add(new PieChart.Data(i + "", sortOutMarks(i, map)));
        }
        pieChart.setData(values);

        for (PieChart.Data data : pieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
                label.setTranslateX(mouseEvent.getSceneX());
                label.setTranslateY(mouseEvent.getSceneY());
                label.setText(String.valueOf(data.getPieValue()));
            });
        }
    }

    //считает среднюю оценку по списку
    private double countAverage(List<Integer> list) {
        if (list.size() == 0) return 0;
        return list.stream()
                .mapToDouble(s -> s)
                .sum() / list.size();
    }

    //считает количество указанных оценок во всей map
    private int sortOutMarks(int mark, Map<String, List<Integer>> map) {
        return map.keySet().stream()
                .mapToInt(key -> findMarks(mark, map.get(key)))
                .sum();
    }

    //считает количество указанных оценок в списке
    private int findMarks(int mark, List<Integer> list) {
        return (int) list.stream()
                .filter(m -> m == mark)
                .count();
    }
}