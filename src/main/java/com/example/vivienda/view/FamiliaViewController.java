package com.example.vivienda.view;

import com.example.vivienda.controller.FamiliaController;
import com.example.vivienda.model.Familia;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class FamiliaViewController {

    @FXML
    private TextField apellidosField;
    @FXML
    private TableView<Familia> familiaTable;
    @FXML
    private TableColumn<Familia, Long> idColumn;
    @FXML
    private TableColumn<Familia, String> apellidosColumn;
    @FXML
    private TableColumn<Familia, Integer> numMiembrosColumn;

    @FXML
    private Button crearButton;
    @FXML
    private Button actualizarButton;
    @FXML
    private Button eliminarButton;
    @FXML
    private Button limpiarButton;

    private final FamiliaController familiaController = new FamiliaController();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        apellidosColumn.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        numMiembrosColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(familiaController.obtenerNumeroMiembros(cellData.getValue())).asObject()
        );

        loadFamilias();

        // Estado inicial de botones
        crearButton.setDisable(false);
        actualizarButton.setDisable(true);
        eliminarButton.setDisable(true);

        // Listener para selección de tabla
        familiaTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                onTableSelection(newSelection);
                crearButton.setDisable(true);
                actualizarButton.setDisable(false);
                eliminarButton.setDisable(false);
            } else {
                crearButton.setDisable(false);
                actualizarButton.setDisable(true);
                eliminarButton.setDisable(true);
            }
        });
    }

    private void loadFamilias() {
        List<Familia> familias = familiaController.obtenerTodasLasFamilias();
        familiaTable.getItems().setAll(familias);
    }

    @FXML
    private void handleCreate() {
        if (!validateInput()) return;

        // Verificar si ya existe una familia con los mismos apellidos
        Familia familiaExistente = familiaController.buscarPorApellidos(apellidosField.getText());
        if (familiaExistente != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Familia ya existe");
            alert.setContentText("Ya existe una familia con los apellidos '" + apellidosField.getText() + "'");
            alert.showAndWait();
            return;
        }

        Familia familia = new Familia(apellidosField.getText());
        familiaController.crearFamilia(familia);
        loadFamilias();
        clearFields();

        crearButton.setDisable(false);
        actualizarButton.setDisable(true);
        eliminarButton.setDisable(true);
        familiaTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleUpdate() {
        if (!validateInput()) return;

        Familia selectedFamilia = familiaTable.getSelectionModel().getSelectedItem();
        if (selectedFamilia != null) {
            // Verificar si ya existe otra familia con los mismos apellidos
            Familia familiaExistente = familiaController.buscarPorApellidos(apellidosField.getText());
            if (familiaExistente != null && familiaExistente.getId() != selectedFamilia.getId()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Apellidos duplicados");
                alert.setContentText("Ya existe otra familia con los apellidos '" + apellidosField.getText() + "'");
                alert.showAndWait();
                return;
            }

            selectedFamilia.setApellidos(apellidosField.getText());
            familiaController.actualizarFamilia(selectedFamilia);
            loadFamilias();
            clearFields();

            crearButton.setDisable(false);
            actualizarButton.setDisable(true);
            eliminarButton.setDisable(true);
            familiaTable.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void handleDelete() {
        Familia selectedFamilia = familiaTable.getSelectionModel().getSelectedItem();
        if (selectedFamilia != null) {
            // Verificar si la familia tiene miembros
            int numMiembros = familiaController.obtenerNumeroMiembros(selectedFamilia);
            if (numMiembros > 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No se puede eliminar la familia");
                alert.setContentText("La familia tiene " + numMiembros + " miembro(s). Elimine primero a todos los miembros.");
                alert.showAndWait();
                return;
            }

            // Confirmación antes de eliminar
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmar eliminación");
            confirmAlert.setHeaderText("¿Está seguro de eliminar esta familia?");
            confirmAlert.setContentText("Familia: " + selectedFamilia.getApellidos());

            if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                familiaController.eliminarFamiliaPorId(selectedFamilia.getId());
                loadFamilias();
                clearFields();

                crearButton.setDisable(false);
                actualizarButton.setDisable(true);
                eliminarButton.setDisable(true);
                familiaTable.getSelectionModel().clearSelection();
            }
        }
    }

    @FXML
    private void handleLimpiar() {
        clearFields();
        familiaTable.getSelectionModel().clearSelection();

        crearButton.setDisable(false);
        actualizarButton.setDisable(true);
        eliminarButton.setDisable(true);
    }

    private void onTableSelection(Familia selectedFamilia) {
        apellidosField.setText(selectedFamilia.getApellidos());
    }

    private boolean validateInput() {
        String errorMessage = "";

        if (apellidosField.getText() == null || apellidosField.getText().trim().isEmpty()) {
            errorMessage += "Apellidos no válidos.\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Campos no válidos");
            alert.setHeaderText("Por favor, corrija los campos no válidos");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }

    private void clearFields() {
        apellidosField.clear();
    }
}

