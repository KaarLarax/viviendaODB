package com.example.vivienda.view;

import com.example.vivienda.controller.PersonaController;
import com.example.vivienda.model.Persona;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class PersonaViewController {

    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidoPaternoField;
    @FXML
    private TextField apellidoMaternoField;
    @FXML
    private TextField curpField;
    @FXML
    private TableView<Persona> personaTable;
    @FXML
    private TableColumn<Persona, Long> idColumn;
    @FXML
    private TableColumn<Persona, String> nombreColumn;
    @FXML
    private TableColumn<Persona, String> apellidoPaternoColumn;
    @FXML
    private TableColumn<Persona, String> apellidoMaternoColumn;
    @FXML
    private TableColumn<Persona, String> curpColumn;

    private final PersonaController personaController = new PersonaController();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apellidoPaternoColumn.setCellValueFactory(new PropertyValueFactory<>("apellidoPaterno"));
        apellidoMaternoColumn.setCellValueFactory(new PropertyValueFactory<>("apellidoMaterno"));
        curpColumn.setCellValueFactory(new PropertyValueFactory<>("curp"));
        loadPersonas();

        personaTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                onTableSelection();
            }
        });
    }

    private void loadPersonas() {
        personaTable.getItems().setAll(personaController.obtenerTodasLasPersonas());
    }

    @FXML
    private void handleCreate() {
        Persona persona = new Persona();
        persona.setNombre(nombreField.getText());
        personaController.crearPersona(persona);
        loadPersonas();
        clearFields();
    }

    @FXML
    private void handleUpdate() {
        Persona selectedPersona = personaTable.getSelectionModel().getSelectedItem();
        if (selectedPersona != null) {
            selectedPersona.setNombre(nombreField.getText());
            personaController.actualizarPersona(selectedPersona);
            loadPersonas();
            clearFields();
        }
    }

    @FXML
    private void handleDelete() {
        Persona selectedPersona = personaTable.getSelectionModel().getSelectedItem();
        if (selectedPersona != null) {
            personaController.eliminarPersonaPorId(selectedPersona.getId());
            loadPersonas();
            clearFields();
        }
    }

    private void onTableSelection() {
        Persona selectedPersona = personaTable.getSelectionModel().getSelectedItem();
        if (selectedPersona != null) {
            nombreField.setText(selectedPersona.getNombre());
        }
    }

    private void clearFields() {
        nombreField.clear();
        apellidoPaternoField.clear();
        apellidoMaternoField.clear();
        curpField.clear();
    }
}

