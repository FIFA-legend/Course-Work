package by.bsuir.service.queue;

import by.bsuir.entity.Client;

public class PriorityQueue { //класс приоритетной очереди

    //по сути все методы приоритетной очереди
    //это просто обертки аналогичных методов кучи
    private final Heap heap = new Heap();

    public boolean insert(Client client) {
        return heap.insertKey(client);
    }

    public Client remove() {
        return heap.remove().getData();
    }

    public Client peek() {
        if (heap.getArray()[0] == null) return null;
        return heap.getArray()[0].getData();
    }
}
