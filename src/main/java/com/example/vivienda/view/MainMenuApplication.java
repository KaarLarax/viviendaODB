package com.example.vivienda.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vivienda/main-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/com/example/vivienda/styles.css").toExternalForm());
        primaryStage.setTitle("Sistema de Gestión de Viviendas");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
