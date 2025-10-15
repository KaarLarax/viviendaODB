package com.example.vivienda.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.example.vivienda.model.CasaUnifamiliar;
import com.example.vivienda.dao.CasaUnifamiliarDAO;
import com.example.vivienda.dao.CasaUnifamiliarDAOImpl;

public class CasaUnifamiliarViewController {
    @FXML private TextField direccionField;
    @FXML private TextField superficieField;
    @FXML private TextField claveCatastralField;
    @FXML private TextField numeroPisosField;

    @FXML private TableView<CasaUnifamiliar> casaTable;
    @FXML private TableColumn<CasaUnifamiliar, String> direccionColumn;
    @FXML private TableColumn<CasaUnifamiliar, String> superficieColumn;
    @FXML private TableColumn<CasaUnifamiliar, String> claveCatastralColumn;
    @FXML private TableColumn<CasaUnifamiliar, String> numeroPisosColumn;

    private final CasaUnifamiliarDAO casaDAO = new CasaUnifamiliarDAOImpl();
    private final ObservableList<CasaUnifamiliar> casaList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        direccionColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDireccion()));
        superficieColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getSuperficie())));
        claveCatastralColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getClaveCatastral()));
        numeroPisosColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getNumeroPisos())));
        casaTable.setItems(casaList);
        loadCasas();
    }

    private void loadCasas() {
        casaList.setAll(casaDAO.findAll());
    }

    @FXML
    private void onCreate() {
        CasaUnifamiliar casa = new CasaUnifamiliar(
            direccionField.getText(),
            Double.parseDouble(superficieField.getText()),
            claveCatastralField.getText(),
            Integer.parseInt(numeroPisosField.getText())
        );
        casaDAO.save(casa);
        loadCasas();
        clearFields();
    }

    @FXML
    private void onUpdate() {
        CasaUnifamiliar selected = casaTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setDireccion(direccionField.getText());
            selected.setSuperficie(Double.parseDouble(superficieField.getText()));
            selected.setClaveCatastral(claveCatastralField.getText());
            selected.setNumeroPisos(Integer.parseInt(numeroPisosField.getText()));
            casaDAO.update(selected);
            loadCasas();
            clearFields();
        }
    }

    @FXML
    private void onDelete() {
        CasaUnifamiliar selected = casaTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            casaDAO.delete(selected.getClaveCatastral());
            loadCasas();
            clearFields();
        }
    }

    @FXML
    private void onSelect(MouseEvent event) {
        CasaUnifamiliar selected = casaTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            direccionField.setText(selected.getDireccion());
            superficieField.setText(String.valueOf(selected.getSuperficie()));
            claveCatastralField.setText(selected.getClaveCatastral());
            numeroPisosField.setText(String.valueOf(selected.getNumeroPisos()));
        }
    }

    private void clearFields() {
        direccionField.clear();
        superficieField.clear();
        claveCatastralField.clear();
        numeroPisosField.clear();
    }
}

