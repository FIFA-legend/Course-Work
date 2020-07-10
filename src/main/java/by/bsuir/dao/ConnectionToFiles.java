package by.bsuir.dao;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ConnectionToFiles {
    //класс для работы с файлами

    private static final Calendar calendar = new GregorianCalendar();

    private static final File clientsFile = new File("Clients.csv");
    private static final File reviewFile = getFile();
    private static final File reviewDirectory = new File("reviews");

    private static final String PATH_TO_FILE = clientsFile.getAbsolutePath();
    private static final String PATH_TO_REVIEWS = reviewFile.getAbsolutePath();

    //метод, который возвращает список файлов с оценками за указанный промежуток времени
    public static ArrayList<File> getReviewFiles(int timePeriod) {
        ArrayList<File> files = new ArrayList<>(timePeriod);
        String[] fileNames = reviewDirectory.list();
        for (String string: fileNames) {
            int month = Integer.parseInt(string.substring(0, 2));
            int day = Integer.parseInt(string.substring(3, 5));
            Calendar date = new GregorianCalendar();
            switch (month) {
                case 1: date.set(Calendar.MONTH, Calendar.JANUARY); break;
                case 2: date.set(Calendar.MONTH, Calendar.FEBRUARY); break;
                case 3: date.set(Calendar.MONTH, Calendar.MARCH); break;
                case 4: date.set(Calendar.MONTH, Calendar.APRIL); break;
                case 5: date.set(Calendar.MONTH, Calendar.MAY); break;
                case 6: date.set(Calendar.MONTH, Calendar.JUNE); break;
                case 7: date.set(Calendar.MONTH, Calendar.JULY); break;
                case 8: date.set(Calendar.MONTH, Calendar.AUGUST); break;
                case 9: date.set(Calendar.MONTH, Calendar.SEPTEMBER); break;
                case 10: date.set(Calendar.MONTH, Calendar.OCTOBER); break;
                case 11: date.set(Calendar.MONTH, Calendar.NOVEMBER); break;
                case 12: date.set(Calendar.MONTH, Calendar.DECEMBER); break;
            }
            date.set(Calendar.DAY_OF_MONTH, day);
            if (calendar.get(Calendar.DAY_OF_YEAR) - timePeriod <= date.get(Calendar.DAY_OF_YEAR)) {
                files.add(new File(reviewDirectory.getAbsolutePath() + File.separator + string));
            }
        }
        return files;
    }

    public static BufferedWriter writeToUserDB() throws IOException {
        return new BufferedWriter(new FileWriter(PATH_TO_FILE, true));
    }

    public static BufferedReader readFromUsersDB() throws IOException {
        return new BufferedReader(new FileReader(PATH_TO_FILE));
    }

    public static BufferedWriter writeWithoutAppend() throws IOException {
        return new BufferedWriter(new FileWriter(PATH_TO_FILE));
    }

    public static BufferedWriter writeToReviews() throws IOException {
        return new BufferedWriter(new FileWriter(PATH_TO_REVIEWS, true));
    }

    public static BufferedReader readFromReviewsDB() throws FileNotFoundException {
        return new BufferedReader(new FileReader(PATH_TO_REVIEWS));
    }

    public static BufferedWriter writeToReviewsWithoutAppend() throws IOException {
        return new BufferedWriter(new FileWriter(PATH_TO_REVIEWS));
    }

    //метод, который возвращает файл с названием в формате ММ.ДД.csv
    public static File getFile() {
        String month;
        String day;
        if (calendar.get(Calendar.MONTH) / 10 == 0) {
            month = "0" + (calendar.get(Calendar.MONTH) + 1);
        } else {
            month = "" + (calendar.get(Calendar.MONTH) + 1);
        }
        if (calendar.get(Calendar.DAY_OF_MONTH) / 10 == 0) {
            day = "0" + calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            day = "" + calendar.get(Calendar.DAY_OF_MONTH);
        }
        return new File("reviews" + File.separator + month + "." + day + ".csv");
    }
}