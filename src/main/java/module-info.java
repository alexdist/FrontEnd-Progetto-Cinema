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
    opens cinema_Infrastructure.film to javafx.base;
    exports org.example.interfaccia_grafica;
    exports org.example.interfaccia_grafica.spettacoli;
    opens org.example.interfaccia_grafica.spettacoli to javafx.fxml;
    exports org.example.interfaccia_grafica.spettacoli.utility_classes;
    opens org.example.interfaccia_grafica.spettacoli.utility_classes to javafx.fxml;
}