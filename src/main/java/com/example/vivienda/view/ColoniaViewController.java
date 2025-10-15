package com.example.vivienda.view;

import com.example.vivienda.controller.ColoniaController;
import com.example.vivienda.model.Colonia;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ColoniaViewController {

    @FXML
    private TextField nombreField;
    @FXML
    private TextField codigoPostalField;
    @FXML
    private TableView<Colonia> coloniaTable;
    @FXML
    private TableColumn<Colonia, Long> idColumn;
    @FXML
    private TableColumn<Colonia, String> nombreColumn;
    @FXML
    private TableColumn<Colonia, String> codigoPostalColumn;

    private final ColoniaController coloniaController = new ColoniaController();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        codigoPostalColumn.setCellValueFactory(new PropertyValueFactory<>("codigoPostal"));
        loadColonias();
    }

    private void loadColonias() {
        coloniaTable.getItems().setAll(coloniaController.obtenerTodasLasColonias());
    }

    @FXML
    private void handleCreate() {
        Colonia colonia = new Colonia();
        colonia.setNombre(nombreField.getText());
        colonia.setCodigoPostal(codigoPostalField.getText());
        coloniaController.crearColonia(colonia);
        loadColonias();
        clearFields();
    }

    @FXML
    private void handleUpdate() {
        Colonia selectedColonia = coloniaTable.getSelectionModel().getSelectedItem();
        if (selectedColonia != null) {
            selectedColonia.setNombre(nombreField.getText());
            selectedColonia.setCodigoPostal(codigoPostalField.getText());
            coloniaController.actualizarColonia(selectedColonia);
            loadColonias();
            clearFields();
        }
    }

    @FXML
    private void handleDelete() {
        Colonia selectedColonia = coloniaTable.getSelectionModel().getSelectedItem();
        if (selectedColonia != null) {
            coloniaController.eliminarColoniaPorId(selectedColonia.getId());
            loadColonias();
            clearFields();
        }
    }

    @FXML
    private void onTableSelection() {
        Colonia selectedColonia = coloniaTable.getSelectionModel().getSelectedItem();
        if (selectedColonia != null) {
            nombreField.setText(selectedColonia.getNombre());
            codigoPostalField.setText(selectedColonia.getCodigoPostal());
        }
    }

    private void clearFields() {
        nombreField.clear();
        codigoPostalField.clear();
    }
}

