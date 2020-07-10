package by.bsuir.service.myLinkedList;

public class MyLinkedList<T> {

    //содержит длину списка
    private int length;

    //ссылка на первый объект в списке
    private Link<T> first;

    //ссылка на последний объект в списке
    private Link<T> last;

    public int getLength() {
        return length;
    }

    public boolean isEmpty() {
        return first == null;
    }

    //метод, который позволяет добавить объект первым в список
    public void insertFirst(T item) {
        Link<T> newLink = new Link<>(item);
        if (isEmpty()) last = newLink;
        else first.setPrevious(newLink);
        newLink.setNext(first);
        first = newLink;
        length += 1;
        Link<T> current = newLink.getNext();
        while (current != null) {
            current.setPlace(current.getPlace() + 1);
            current = current.getNext();
        }
    }

    //метод, который позволяет добавить объект последним в список
    public void insertLast(T item) {
        Link<T> newLink = new Link<>(item);
        newLink.setPlace(length);
        if (isEmpty()) first = newLink;
        else {
            last.setNext(newLink);
            newLink.setPrevious(last);
        }
        last = newLink;
        length += 1;
    }

    //метод, который позволяет удалить первый объект из списка
    public void deleteFirst() {
        if (first.getNext() == null) last = null;
        else first.getNext().setPrevious(null);
        first = first.getNext();
        length -= 1;
        Link<T> current = first;
        while (current != null) {
            current.setPlace(current.getPlace() - 1);
            current = current.getNext();
        }
    }

    //метод, который позволяет удалить последний объект из списка
    public void deleteLast() {
        if (first.getNext() == null) first = null;
        else last.getPrevious().setNext(null);
        last = last.getPrevious();
        length -= 1;
    }

    //метод, который позволяет вставить объект по указанному ключу (нумерация с 0)
    public void insert(int key, T item) {
        if (key == 0) insertFirst(item);
        else if (key == length) insertLast(item);
        else {
            Link<T> current = first;
            while (current.getPlace() != key) {
                current = current.getNext();
            }
            Link<T> newLink = new Link<>(item);
            newLink.setPlace(current.getPlace());
            newLink.setPrevious(current.getPrevious());
            newLink.setNext(current);
            current.getPrevious().setNext(newLink);
            current.setPrevious(newLink);
            length += 1;
            while (current != null) {
                current.setPlace(current.getPlace() + 1);
                current = current.getNext();
            }
        }
    }

    //метод, который позволяет удалить объект по указанному ключу (нумерация с 0)
    public void delete(int key) {
        if (key == 0) deleteFirst();
        else if (key == length - 1) deleteLast();
        else {
            Link<T> current = first;
            while (current.getPlace() != key) {
                current = current.getNext();
            }
            current.getPrevious().setNext(current.getNext());
            current.getNext().setPrevious(current.getPrevious());
            length -= 1;
            current = current.getNext();
            while (current != null) {
                current.setPlace(current.getPlace() - 1);
                current = current.getNext();
            }
        }
    }

    //метод, который позволяет изменить объект по указанному ключу (нумерация с 0)
    public void update(int key, T item) {
        Link<T> current = first;
        while (current.getPlace() != key) {
            current = current.getNext();
        }
        current.setItem(item);
    }

    //метод, который позволяет получить объект по указанному ключу (нумерация с 0)
    public T get(int key) {
        Link<T> current = first;
        while (current.getPlace() != key) {
            current = current.getNext();
        }
        return current.getItem();
    }
}
