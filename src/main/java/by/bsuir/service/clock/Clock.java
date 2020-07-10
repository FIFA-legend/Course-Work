package by.bsuir.service.clock;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Clock {

    //поле с сылкой на тескт, куда выводить время
    private Text textToShowTime;

    //сеттер
    public void setTextToShowTime(Text textToShowTime) {
        this.textToShowTime = textToShowTime;
    }

    //метод, который создает анимацию
    //которая показывает точное время
    public void showTime() {
        Animation animation = new Transition() {
            {
                setCycleDuration(Duration.seconds(0.5));
                setCycleCount(INDEFINITE);
            }
            @Override
            protected void interpolate(double frac) {
                Calendar calendar = new GregorianCalendar();
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minuets = calendar.get(Calendar.MINUTE);
                String time;
                if (minuets / 10 == 0) time = hours + ":0" + minuets;
                else time = hours + ":" + minuets;
                textToShowTime.setText(time);
            }
        };
        animation.play();
    }
}