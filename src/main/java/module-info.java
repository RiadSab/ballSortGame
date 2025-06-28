module org.example.ballsort {
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
    requires java.desktop;
    requires java.sql;
    requires jdk.compiler;
    requires javafx.media;
    requires jbcrypt;


    opens org.example.ballsort to javafx.fxml;
    opens org.example.ballsort.controller to javafx.fxml;

    exports org.example.ballsort;
    exports org.example.ballsort.controller to javafx.fxml;
}
