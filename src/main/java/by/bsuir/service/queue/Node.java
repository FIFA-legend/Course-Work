package by.bsuir.service.queue;

import by.bsuir.entity.Client;

public class Node { //класс узла приоритетной очереди

    //поле, содержащее данные
    private Client data;

    public Node(Client client) {
        this.data = client;
    }

    //геттер и сеттер
    public Client getData() {
        return data;
    }

    public void setData(Client client) {
        this.data = client;
    }
}
