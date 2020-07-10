package by.bsuir.service.queue;

import by.bsuir.entity.Client;

public class Heap {

    //массив узлов
    private final Node [] array;

    //размер массива
    private final int maxsize = 500;

    //текущий размер
    private int currentSize;

    public Node[] getArray() {
        return array;
    }

    //конструктор
    public Heap() {
        array = new Node[maxsize];
    }


    public boolean isEmpty() {
        return currentSize == 0;
    }

    //метод вставки клиента в очередь
    //если клиент добавлен, возвращает true
    //иначе false
    public boolean insertKey(Client client) {
        if (currentSize == maxsize) return false;
        Node newNode = new Node(client);
        array[currentSize] = newNode;
        trickleUp(currentSize++);
        return true;
    }

    //метод подъема ключа по куче
    private void trickleUp(int index) {
        int parent = (index - 1) / 2;
        Node bottom = array[index];
        while (index > 0 && (array[parent].getData().getPriority() < bottom.getData().getPriority()
                || (array[parent].getData().getPriority() == bottom.getData().getPriority()
                && array[parent].getData().getId() > bottom.getData().getId()))) {
            array[index] = array[parent];
            index = parent;
            parent = (parent - 1) / 2;
        }
        array[index] = bottom;
    }

    //метод удаления первого в очереди
    public Node remove() {
        Node root = array[0];
        array[0] = array[--currentSize];
        array[currentSize] = null;
        trickleDown(0);
        return root;
    }

    //метод спуска ключа вниз по куче
    private void trickleDown(int index) {
        int largeChild;
        Node top = array[index];
        while (index < currentSize / 2) {
            int leftChild = 2 * index + 1;
            int rightChild = leftChild + 1;
            if (rightChild < currentSize && (array[leftChild].getData().getPriority() < array[rightChild].getData().getPriority()
                    || (array[leftChild].getData().getPriority() == array[rightChild].getData().getPriority()
                    && array[leftChild].getData().getId() > array[rightChild].getData().getId()))) {
                largeChild = rightChild;
            } else {
                largeChild = leftChild;
            }
            if ((top.getData().getPriority() > array[largeChild].getData().getPriority()
                    || (top.getData().getPriority() == array[largeChild].getData().getPriority() && top.getData().getId() < array[largeChild].getData().getId()))) {
                break;
            }
            array[index] = array[largeChild];
            index = largeChild;
        }
        array[index] = top;
    }
}
