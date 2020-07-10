package by.bsuir.entity;

import by.bsuir.dao.Dao;

import java.util.Objects;

public class Client {

    //поле-счетчик, способствующее созданию уникаальных номеров
    private static int count = Dao.getInstance().getLastId();

    //поле с номером клиента
    private int id;

    //поле с приоритетом клиента
    private int priority;

    //поле с именем клиента
    private String name;

    //поле с фамилией клиента
    private String surname;

    //поле с названием банковского сервиса
    BankServices visitPurpose;

    //конструкторы
    public Client(int priority, String name, String surname, BankServices visitPurpose) {
        this.id = ++count;
        this.priority = priority;
        this.name = name;
        this.surname = surname;
        this.visitPurpose = visitPurpose;
    }

    public Client(int id, int priority, String name, String surname, BankServices visitPurpose) {
        this.id = id;
        this.priority = priority;
        this.name = name;
        this.surname = surname;
        this.visitPurpose = visitPurpose;
    }

    //геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public BankServices getVisitPurpose() {
        return visitPurpose;
    }

    public void setVisitPurpose(BankServices visitPurpose) {
        this.visitPurpose = visitPurpose;
    }

    //переопределенный метод equals(Object o)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id &&
                priority == client.priority &&
                Objects.equals(name, client.name) &&
                Objects.equals(surname, client.surname) &&
                visitPurpose == client.visitPurpose;
    }

    //переопределенный метод hashCode()
    @Override
    public int hashCode() {
        return Objects.hash(id, priority, name, surname, visitPurpose);
    }

    //переопределенный метод toString()
    @Override
    public String toString() {
        return id + ";" + priority + ";" + name + ";" + surname + ";" + visitPurpose;
    }
}