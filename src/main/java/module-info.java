module by.bsuir {
    requires javafx.controls;
    requires javafx.fxml;

    opens by.bsuir to javafx.fxml;
    exports by.bsuir;
    exports by.bsuir.service.queue;
    exports by.bsuir.service.queueThreads;
    exports by.bsuir.service.myLinkedList;
    exports by.bsuir.entity;
}