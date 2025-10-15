package com.example.vivienda.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainViewController {

    @FXML
    private StackPane contentPane;

    @FXML
    private void initialize() {
        loadWelcomeView();
    }

    private void loadWelcomeView() {
        loadView("welcome-view.fxml");
    }

    @FXML
    private void handleCasaUnifamiliar(ActionEvent event) {
        loadView("view/casaunifamiliar-view.fxml");
    }

    @FXML
    private void handleColonia(ActionEvent event) {
        loadView("view/colonia-view.fxml");
    }

    @FXML
    private void handleDepartamento(ActionEvent event) {
        loadView("view/departamento-view.fxml");
    }

    @FXML
    private void handleEdificio(ActionEvent event) {
        loadView("view/edificio-view.fxml");
    }

    @FXML
    private void handlePersona(ActionEvent event) {
        loadView("view/persona-view.fxml");
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vivienda/" + fxmlFile));
            Parent view = loader.load();
            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

