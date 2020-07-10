package by.bsuir.service.myLinkedList;

class Link<T> {

    //поле содержит номер объекта Link в списке
    private int place;

    //содержит объект указанного класса
    private T item;

    //ссылка на следующий объект класса Link
    private Link<T> next;

    //ссылка на предыдущий объект класса Link
    private Link<T> previous;

    //конструктор
    public Link(T item) {
        this.item = item;
    }

    //геттеры и сеттеры
    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public Link<T> getNext() {
        return next;
    }

    public void setNext(Link<T> next) {
        this.next = next;
    }

    public Link<T> getPrevious() {
        return previous;
    }

    public void setPrevious(Link<T> previous) {
        this.previous = previous;
    }
}
