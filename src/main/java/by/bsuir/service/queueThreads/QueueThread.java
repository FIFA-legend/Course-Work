package by.bsuir.service.queueThreads;

import by.bsuir.App;
import by.bsuir.dao.Dao;
import by.bsuir.entity.Client;
import by.bsuir.service.queue.PriorityQueue;
import javafx.scene.text.Text;

public class QueueThread implements Runnable {
    //класс, обрабатывающий элементы очереди
    //работает в отдельном потоке

    //время обработки одного клиента в секундах
    private final int TIME = 60;

    //поля для отображения текста
    private Text text1;
    private Text text2;
    private Text text3;
    private Text text4;

    private final Thread thread;

    //сеттеры
    public Thread getThread() {
        return thread;
    }

    public void setText1(Text text1) {
        this.text1 = text1;
    }

    public void setText2(Text text2) {
        this.text2 = text2;
    }

    public void setText3(Text text3) {
        this.text3 = text3;
    }

    public void setText4(Text text4) {
        this.text4 = text4;
    }

    //конструктор
    public QueueThread() {
        this.thread = new Thread(this);
        this.thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    @Override
    public void run() {
        try {
            queueRun();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //метод, который каждые 60 секунд отображает номер нового клиента
    private synchronized void queueRun() throws InterruptedException {
        wait(500);
        int timer1 = 0;
        int timer2 = 0;
        int timer3 = 0;
        int timer4 = 0;
        int id1 = getNumber(App.getPriorityQueues().get(0));
        int id2 = getNumber(App.getPriorityQueues().get(1));
        int id3 = getNumber(App.getPriorityQueues().get(2));
        int id4 = getNumber(App.getPriorityQueues().get(3));
        while (true) {
            if (id1 != 0 && timer1 < TIME * 2) {
                if (text1 != null) {
                    if (timer1 < 10 && timer1 % 2 == 1) {
                        text1.setText("");
                    } else {
                        text1.setText(id1 + "");
                    }
                }
                timer1++;
            } else {
                if (text1 != null) text1.setText("");
                if (id1 != 0) Dao.getInstance().deleteFromDB(id1);
                id1 = getNumber(App.getPriorityQueues().get(0));
                timer1 = 0;
            }
            if (id2 != 0 && timer2 < TIME * 2) {
                if (text2 != null) {
                    if (timer2 < 10 && timer2 % 2 == 1) {
                        text2.setText("");
                    } else {
                        text2.setText(id2 + "");
                    }
                }
                timer2++;
            } else {
                if (text2 != null) text2.setText("");
                if (id2 != 0) Dao.getInstance().deleteFromDB(id2);
                id2 = getNumber(App.getPriorityQueues().get(1));
                timer2 = 0;
            }
            if (id3 != 0 && timer3 < TIME * 2) {
                if (text3 != null) {
                    if (timer3 < 10 && timer3 % 2 == 1) {
                        text3.setText("");
                    } else {
                        text3.setText(id3 + "");
                    }
                }
                timer3++;
            } else {
                if (text3 != null) text3.setText("");
                if (id3 != 0) Dao.getInstance().deleteFromDB(id3);
                id3 = getNumber(App.getPriorityQueues().get(2));
                timer3 = 0;
            }
            if (id4 != 0 && timer4 < TIME * 2) {
                if (text4 != null) {
                    if (timer4 < 10 && timer4 % 2 == 1) {
                        text4.setText("");
                    } else {
                        text4.setText(id4 + "");
                    }
                }
                timer4++;
            } else {
                if (text4 != null) text4.setText("");
                if (id4 != 0) Dao.getInstance().deleteFromDB(id4);
                id4 = getNumber(App.getPriorityQueues().get(3));
                timer4 = 0;
            }
            wait(500);
        }
    }

    //метод, котрый проверяет, есть ли кто-то в очереди
    //если есть, то возвращает его номер для отображения на экране
    //если нет, то возвращает 0
    private int getNumber(PriorityQueue pq) {
        if (pq.peek() != null) {
            Client client = pq.remove();
            App.getReviewClients().insertLast(client);
            return client.getId();
        } else {
            return 0;
        }
    }
}
