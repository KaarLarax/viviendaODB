package com.example.vivienda.view;

import com.example.vivienda.controller.EdificioController;
import com.example.vivienda.model.Edificio;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class EdificioViewController {

    @FXML
    private TextField direccionField;
    @FXML
    private TextField superficieField;
    @FXML
    private TextField claveCatastralField;
    @FXML
    private TextField numApartamentosField;
    @FXML
    private TableView<Edificio> edificioTable;
    @FXML
    private TableColumn<Edificio, Long> idColumn;
    @FXML
    private TableColumn<Edificio, String> direccionColumn;
    @FXML
    private TableColumn<Edificio, Double> superficieColumn;
    @FXML
    private TableColumn<Edificio, String> claveCatastralColumn;
    @FXML
    private TableColumn<Edificio, Integer> numApartamentosColumn;

    private final EdificioController edificioController = new EdificioController();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        direccionColumn.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        superficieColumn.setCellValueFactory(new PropertyValueFactory<>("superficie"));
        claveCatastralColumn.setCellValueFactory(new PropertyValueFactory<>("claveCatastral"));
        numApartamentosColumn.setCellValueFactory(new PropertyValueFactory<>("numApartamentos"));
        loadEdificios();

        edificioTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                onTableSelection();
            }
        });
    }

    private void loadEdificios() {
        edificioTable.getItems().setAll(edificioController.obtenerTodosLosEdificios());
    }

    @FXML
    private void handleCreate() {
        Edificio edificio = new Edificio();
        edificio.setDireccion(direccionField.getText());
        edificio.setSuperficie(Double.parseDouble(superficieField.getText()));
        edificio.setClaveCatastral(claveCatastralField.getText());
        edificio.setTotalDepartamentos(Integer.parseInt(numApartamentosField.getText()));
        edificioController.crearEdificio(edificio);
        loadEdificios();
        clearFields();
    }

    @FXML
    private void handleUpdate() {
        Edificio selectedEdificio = edificioTable.getSelectionModel().getSelectedItem();
        if (selectedEdificio != null) {
            selectedEdificio.setDireccion(direccionField.getText());
            selectedEdificio.setSuperficie(Double.parseDouble(superficieField.getText()));
            selectedEdificio.setClaveCatastral(claveCatastralField.getText());
            selectedEdificio.setTotalDepartamentos(Integer.parseInt(numApartamentosField.getText()));
            edificioController.actualizarEdificio(selectedEdificio);
            loadEdificios();
            clearFields();
        }
    }

    @FXML
    private void handleDelete() {
        Edificio selectedEdificio = edificioTable.getSelectionModel().getSelectedItem();
        if (selectedEdificio != null) {
            edificioController.eliminarEdificioPorId(selectedEdificio.getId());
            loadEdificios();
            clearFields();
        }
    }

    private void onTableSelection() {
        Edificio selectedEdificio = edificioTable.getSelectionModel().getSelectedItem();
        if (selectedEdificio != null) {
            direccionField.setText(selectedEdificio.getDireccion());
            superficieField.setText(String.valueOf(selectedEdificio.getSuperficie()));
            claveCatastralField.setText(selectedEdificio.getClaveCatastral());
            numApartamentosField.setText(String.valueOf(selectedEdificio.getTotalDepartamentos()));
        }
    }

    private void clearFields() {
        direccionField.clear();
        superficieField.clear();
        claveCatastralField.clear();
        numApartamentosField.clear();
    }
}

