module org.example.interfaccia_grafica {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens org.example.interfaccia_grafica to javafx.fxml;
    opens cinema_Infrastructure.sala to javafx.base;
    exports org.example.interfaccia_grafica;
}