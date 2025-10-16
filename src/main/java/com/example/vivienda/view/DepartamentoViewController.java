package com.example.vivienda.view;

import com.example.vivienda.controller.DepartamentoController;
import com.example.vivienda.controller.PersonaController;
import com.example.vivienda.controller.ColoniaController;
import com.example.vivienda.controller.EdificioController;
import com.example.vivienda.model.Departamento;
import com.example.vivienda.model.Persona;
import com.example.vivienda.model.Colonia;
import com.example.vivienda.model.Edificio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class DepartamentoViewController {

    @FXML
    private TableView<Departamento> departamentoTable;
    @FXML
    private TableColumn<Departamento, Integer> idColumn;
    @FXML
    private TableColumn<Departamento, String> direccionColumn;
    @FXML
    private TableColumn<Departamento, Double> superficieColumn;
    @FXML
    private TableColumn<Departamento, String> claveCatastralColumn;
    @FXML
    private TableColumn<Departamento, String> numeroColumn;

    @FXML
    private TextField direccionField;
    @FXML
    private TextField superficieField;
    @FXML
    private TextField claveCatastralField;
    @FXML
    private TextField numeroField;
    @FXML
    private TextField propietarioField;
    @FXML
    private TextField coloniaField;
    @FXML
    private TextField edificioField;

    private final DepartamentoController departamentoController = new DepartamentoController();
    private final PersonaController personaController = new PersonaController();
    private final ColoniaController coloniaController = new ColoniaController();
    private final EdificioController edificioController = new EdificioController();

    private final ObservableList<Departamento> departamentoList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        direccionColumn.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        superficieColumn.setCellValueFactory(new PropertyValueFactory<>("superficie"));
        claveCatastralColumn.setCellValueFactory(new PropertyValueFactory<>("claveCatastral"));
        numeroColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));

        cargarDepartamentos();
    }

    private void cargarDepartamentos() {
        departamentoList.setAll(departamentoController.obtenerTodosLosDepartamentos());
        departamentoTable.setItems(departamentoList);
    }

    @FXML
    private void agregarDepartamento() {
        try {
            String direccion = direccionField.getText();
            double superficie = Double.parseDouble(superficieField.getText());
            String claveCatastral = claveCatastralField.getText();
            String numero = numeroField.getText();
            Persona propietario = personaController.obtenerPersona(Long.parseLong(propietarioField.getText()));
            Colonia colonia = coloniaController.obtenerColonia(Long.parseLong(coloniaField.getText()));
            Edificio edificio = edificioController.obtenerEdificio(Long.parseLong(edificioField.getText()));

            if (propietario != null && colonia != null && edificio != null) {
                Departamento departamento = new Departamento(direccion, superficie, claveCatastral, propietario, colonia, numero);
                departamento.setEdificio(edificio);
                departamentoController.agregarDepartamento(departamento);
                edificio.agregarDepartamento(departamento);
                edificioController.actualizarEdificio(edificio);
                cargarDepartamentos();
                limpiarCampos();
            } else {
                // Manejar caso donde propietario, colonia o edificio no se encuentran
            }
        } catch (NumberFormatException e) {
            // Manejar error de formato de número
        }
    }

    @FXML
    private void actualizarDepartamento() {
        Departamento selected = departamentoTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                selected.setDireccion(direccionField.getText());
                selected.setSuperficie(Double.parseDouble(superficieField.getText()));
                selected.setClaveCatastral(claveCatastralField.getText());
                selected.setNumero(numeroField.getText());
                Persona propietario = personaController.obtenerPersona(Long.parseLong(propietarioField.getText()));
                Colonia colonia = coloniaController.obtenerColonia(Long.parseLong(coloniaField.getText()));
                Edificio edificio = edificioController.obtenerEdificio(Long.parseLong(edificioField.getText()));

                if (propietario != null && colonia != null && edificio != null) {
                    selected.setPropietario(propietario);
                    selected.setColonia(colonia);
                    selected.setEdificio(edificio);
                    departamentoController.actualizarDepartamento(selected);
                    cargarDepartamentos();
                    limpiarCampos();
                } else {
                    // Manejar caso donde propietario, colonia o edificio no se encuentran
                }
            } catch (NumberFormatException e) {
                // Manejar error de formato de número
            }
        }
    }

    @FXML
    private void eliminarDepartamento() {
        Departamento selected = departamentoTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            departamentoController.eliminarDepartamento((int) selected.getId());
            cargarDepartamentos();
        }
    }

    private void limpiarCampos() {
        direccionField.clear();
        superficieField.clear();
        claveCatastralField.clear();
        numeroField.clear();
        propietarioField.clear();
        coloniaField.clear();
        edificioField.clear();
    }
}

