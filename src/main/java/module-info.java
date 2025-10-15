module com.example.vivienda {
    // Requerimientos de JavaFX y otras librerías UI
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

    // Requerimientos para la persistencia
    requires javax.persistence;
    requires java.sql;
    requires objectdb;

    // Permisos para JavaFX
    opens com.example.vivienda to javafx.fxml;
    exports com.example.vivienda;

    // --- Permisos para ObjectDB ---

    // 1. Permite a ObjectDB usar REFLEXIÓN en tus entidades
    opens com.example.vivienda.model to objectdb;

    // 2. Permite a ObjectDB usar y HEREDAR de tus entidades
    exports com.example.vivienda.model;
    exports com.example.vivienda.view;
    opens com.example.vivienda.view to javafx.fxml;
}