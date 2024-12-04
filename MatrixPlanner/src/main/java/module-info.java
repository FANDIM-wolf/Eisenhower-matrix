module com.example.matrixplanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires com.fasterxml.jackson.databind;

    opens com.example.matrixplanner to javafx.fxml;
    exports com.example.matrixplanner;
    exports com.example.matrixplanner.test;
    opens com.example.matrixplanner.test to javafx.fxml;
}