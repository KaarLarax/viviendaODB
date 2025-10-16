package com.example.vivienda.view;

import com.example.vivienda.controller.PersonaController;
import com.example.vivienda.model.Persona;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class PersonaViewController {

    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidosField;
    @FXML
    private TextField rfcField;
    @FXML
    private TextField edadField;
    @FXML
    private CheckBox esJefeDeFamiliaCheckBox;
    @FXML
    private TableView<Persona> personaTable;
    @FXML
    private TableColumn<Persona, Long> idColumn;
    @FXML
    private TableColumn<Persona, String> nombreColumn;
    @FXML
    private TableColumn<Persona, String> apellidosColumn;
    @FXML
    private TableColumn<Persona, String> rfcColumn;
    @FXML
    private TableColumn<Persona, Integer> edadColumn;
    @FXML
    private TableColumn<Persona, Boolean> esJefeDeFamiliaColumn;
    @FXML
    private TableColumn<Persona, Integer> numPropiedadesColumn;

    private final PersonaController personaController = new PersonaController();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apellidosColumn.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        rfcColumn.setCellValueFactory(new PropertyValueFactory<>("rfc"));
        edadColumn.setCellValueFactory(new PropertyValueFactory<>("edad"));
        esJefeDeFamiliaColumn.setCellValueFactory(new PropertyValueFactory<>("esJefeDeFamilia"));
        numPropiedadesColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue() != null) {
                return new SimpleIntegerProperty(personaController.obtenerNumeroDePropiedades(cellData.getValue())).asObject();
            } else {
                return new SimpleIntegerProperty(0).asObject();
            }
        });
        loadPersonas();

        personaTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                onTableSelection(newSelection);
            }
        });
    }

    private void loadPersonas() {
        personaTable.getItems().setAll(personaController.obtenerTodasLasPersonas());
    }

    @FXML
    private void handleCreate() {
        if (!validateInput()) {
            return;
        }
        Persona persona = new Persona(
                nombreField.getText(),
                rfcField.getText(),
                esJefeDeFamiliaCheckBox.isSelected(),
                Integer.parseInt(edadField.getText()),
                apellidosField.getText()
        );
        personaController.crearPersona(persona);
        loadPersonas();
        clearFields();
    }

    @FXML
    private void handleUpdate() {
        if (!validateInput()) {
            return;
        }
        Persona selectedPersona = personaTable.getSelectionModel().getSelectedItem();
        if (selectedPersona != null) {
            selectedPersona.setNombre(nombreField.getText());
            selectedPersona.setApellidos(apellidosField.getText());
            selectedPersona.setRfc(rfcField.getText());
            selectedPersona.setEdad(Integer.parseInt(edadField.getText()));
            selectedPersona.setEsJefeDeFamilia(esJefeDeFamiliaCheckBox.isSelected());
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

    private void onTableSelection(Persona selectedPersona) {
        nombreField.setText(selectedPersona.getNombre());
        apellidosField.setText(selectedPersona.getApellidos());
        rfcField.setText(selectedPersona.getRfc());
        edadField.setText(String.valueOf(selectedPersona.getEdad()));
        esJefeDeFamiliaCheckBox.setSelected(selectedPersona.isEsJefeDeFamilia());
    }

    private boolean validateInput() {
        String errorMessage = "";

        if (nombreField.getText() == null || nombreField.getText().trim().isEmpty()) {
            errorMessage += "Nombre no válido.\n";
        }
        if (apellidosField.getText() == null || apellidosField.getText().trim().isEmpty()) {
            errorMessage += "Apellidos no válidos.\n";
        }
        if (rfcField.getText() == null || rfcField.getText().trim().isEmpty()) {
            errorMessage += "RFC no válido.\n";
        }
        if (edadField.getText() == null || edadField.getText().trim().isEmpty()) {
            errorMessage += "Edad no válida.\n";
        } else {
            try {
                Integer.parseInt(edadField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "La edad debe ser un número entero.\n";
            }
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Campos no válidos");
            alert.setHeaderText("Por favor, corrija los campos no válidos");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    private void clearFields() {
        nombreField.clear();
        apellidosField.clear();
        rfcField.clear();
        edadField.clear();
        esJefeDeFamiliaCheckBox.setSelected(false);
    }
}

