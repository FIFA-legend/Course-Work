package by.bsuir.dao;

import by.bsuir.entity.BankServices;
import by.bsuir.entity.Client;
import by.bsuir.service.queue.PriorityQueue;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dao {

    //регулярное выражение для выбора записей о клиентах
    private final String REGEX = "^(\\d+);(\\d+);(\\w+|\\W+);(\\w+|\\W+);(\\w+)$";

    //регулярное выражение для выбора записей об оценках
    private final String REVIEW_REGEX = "^(\\d+);(\\w+)$";

    private static Dao INSTANCE;

    private Dao() {}

    //паттерн проектирования Singleton,
    //который запрещает создание больше одного экземпляра данного класса
    public static Dao getInstance() {
        if (INSTANCE == null) {
            synchronized (Dao.class) {
                if (INSTANCE == null) INSTANCE = new Dao();
            }
        }
        return INSTANCE;
    }

    //метод для сохранения клиента в файл
    public void saveToDB(Client client) {
        try (BufferedWriter bw = ConnectionToFiles.writeToUserDB()){
            String userToWrite = client.toString() + "\r\n";
            bw.write(userToWrite);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //метод, который возвращает очередь с клиентами по определенной услуге
    public PriorityQueue readAllClients(BankServices bankServices) {
        PriorityQueue pq = new PriorityQueue();
        try (BufferedReader br = ConnectionToFiles.readFromUsersDB()){
            br.lines()
                    .forEach(str -> {
                Pattern pattern = Pattern.compile(REGEX);
                Matcher matcher = pattern.matcher(str);
                if (matcher.matches() && matcher.group(5).equals(bankServices.toString())) {
                    int id = Integer.parseInt(matcher.group(1));
                    int priority = Integer.parseInt(matcher.group(2));
                    Client client = new Client(id, priority, matcher.group(3), matcher.group(4), bankServices);
                    pq.insert(client);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pq;
    }

    //метод, который возвращает последний занятый номер
    public int getLastId() {
        int i = 0;
        try (BufferedReader br = ConnectionToFiles.readFromUsersDB()){
            i = br.lines()
                    .mapToInt(s -> {
                        Pattern pattern = Pattern.compile(REGEX);
                        Matcher matcher = pattern.matcher(s);
                        if (matcher.matches()) {
                            return Integer.parseInt(matcher.group(1));
                        } else return 0;
                    })
                    .max().orElse(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return i;
    }

    public Client readFromDB(int id) {
        try (BufferedReader bufferedReader = ConnectionToFiles.readFromUsersDB()){
            Pattern pattern = Pattern.compile(REGEX);
            Client client = null;
            String line = bufferedReader.readLine();
            while (line != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches() && matcher.group(1).equals("" + id)) {
                    BankServices service = BankServices.valueOf(matcher.group(5));
                    int priority = Integer.parseInt(matcher.group(2));
                    client = new Client(id, priority, matcher.group(3), matcher.group(4), service);
                    break;
                } else line = bufferedReader.readLine();
            }
            return client;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //метод, который удаляет клиента с указанным номером из файла
    public void deleteFromDB(int id) {
        List<String> clients = new ArrayList<>();
        try (BufferedReader bufferedReader = ConnectionToFiles.readFromUsersDB()){
            String line = bufferedReader.readLine();
            Pattern pattern = Pattern.compile(REGEX);
            while (line != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches() && matcher.group(1).equals("" + id)) {
                    line = bufferedReader.readLine();
                    continue;
                }
                clients.add(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter bw = ConnectionToFiles.writeWithoutAppend()){
            for (String str: clients) {
                String string = str + "\r\n";
                bw.write(string);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //метод, который записывает указанную оценку в файл
    public void writeReview(int mark, Client client) {
        try (BufferedWriter bw = ConnectionToFiles.writeToReviews()){
            String str = mark + ";" + client.getVisitPurpose() + "\r\n";
            bw.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //метод, который при выключении программы
    //выводит в файл среднюю оценку за услугу
    public void writeAverageMarks() {
        List<BankServices> services = new ArrayList<>(4);
        services.add(BankServices.OPEN_DEPOSIT);
        services.add(BankServices.PUT_MONEY);
        services.add(BankServices.BANK_GUARANTEES);
        services.add(BankServices.FOREIGN_MONEY_EXCHANGE);
        services.forEach(service -> {
            String str;
            double average = countAverage(service);
            if (countAverage(service) != 0) {
                str = String.format("%s;%.2f\r\n", service.toString(), average);
            } else {
                str = service.toString() + ";no visitors today\r\n";
            }
            try (BufferedWriter bw = ConnectionToFiles.writeToReviews()){
                bw.write(str);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    //метод, который при запуске программы удаляет средние оценки из файла
    public void clearMarks() {
        List<String> lines = new LinkedList<>();
        Pattern pattern = Pattern.compile(REVIEW_REGEX);
        try (BufferedReader br = ConnectionToFiles.readFromReviewsDB()){
            String str = br.readLine();
            while (str != null) {
                Matcher matcher = pattern.matcher(str);
                if (matcher.matches()) lines.add(str);
                str = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter bw = ConnectionToFiles.writeToReviewsWithoutAppend()){
            bw.write("Marks;Service\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        lines.parallelStream().forEach(line -> {
            try (BufferedWriter bwriter = ConnectionToFiles.writeToReviews()){
                bwriter.write(line + "\r\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    //метод возвращает оценки за указанный период времени
    public Map<String, List<Integer>> getMarksForTimePeriod(int timePeriod, BankServices bs) {
        ArrayList<File> files = ConnectionToFiles.getReviewFiles(timePeriod);
        Map<String, List<Integer>> map = new TreeMap<>();
        Pattern pattern = Pattern.compile(REVIEW_REGEX);
        files.forEach(file -> {
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        List<Integer> list = new ArrayList<>();
                        br.lines().forEach(line -> {
                            Matcher matcher = pattern.matcher(line);
                            if (matcher.matches() && matcher.group(2).equals(bs.toString())) {
                                list.add(Integer.parseInt(matcher.group(1)));
                            }
                        });
                        map.put(file.getName().substring(0, 5), list);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                });
        return map;
    }

    //метод, возвращающий список оценок за сегодня
    private List<Integer> getMarksForToday(BankServices bs) {
        Map<String, List<Integer>> map = getMarksForTimePeriod(1, bs);
        return map.get(ConnectionToFiles.getFile().getName().substring(0, 5));
    }

    //метод, считающий среднюю оценку за сегодня
    private Double countAverage(BankServices bs) {
        return getMarksForToday(bs).stream()
                .mapToInt(mark -> mark)
                .average().orElse(0);
    }
}