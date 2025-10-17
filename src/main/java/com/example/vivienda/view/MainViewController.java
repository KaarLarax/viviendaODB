package com.example.vivienda.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
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
    private void handleInicio(ActionEvent event) {
        loadWelcomeView();
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

    @FXML
    private void handleFamilia(ActionEvent event) {
        loadView("view/familia-view.fxml");
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vivienda/" + fxmlFile));
            Parent view = loader.load();
            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al cargar vista");
            alert.setHeaderText("No se pudo cargar la vista");
            alert.setContentText("Error al cargar: " + fxmlFile + "\n" + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error inesperado");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}
