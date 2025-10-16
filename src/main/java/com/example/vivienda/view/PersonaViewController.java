package com.example.vivienda.view;

import com.example.vivienda.controller.PersonaController;
import com.example.vivienda.model.Familia;
import com.example.vivienda.model.Persona;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

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

    // 🔹 Botones con fx:id
    @FXML
    private Button crearButton;
    @FXML
    private Button actualizarButton;
    @FXML
    private Button eliminarButton;
    @FXML
    private Button limpiarButton;

    private final PersonaController personaController = new PersonaController();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apellidosColumn.setCellValueFactory(cellData -> {
            Familia f = cellData.getValue().getFamilia();
            return new SimpleStringProperty(f != null ? f.getApellidos() : "");
        });
        rfcColumn.setCellValueFactory(new PropertyValueFactory<>("rfc"));
        edadColumn.setCellValueFactory(new PropertyValueFactory<>("edad"));
        esJefeDeFamiliaColumn.setCellValueFactory(new PropertyValueFactory<>("esJefeDeFamilia"));
        numPropiedadesColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(personaController.obtenerNumeroDePropiedades(cellData.getValue())).asObject()
        );

        loadPersonas();

        // 🔹 Estado inicial de botones
        crearButton.setDisable(false);
        actualizarButton.setDisable(true);
        eliminarButton.setDisable(true);

        // 🔹 Cuando se selecciona una fila
        personaTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
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

    private void loadPersonas() {
        List<Persona> personas = personaController.obtenerTodasLasPersonas();
        personaTable.getItems().setAll(personas);
    }

    @FXML
    private void handleCreate() {
        if (!validateInput()) return;

        Familia familia = new Familia(apellidosField.getText());
        Persona persona = new Persona(
                nombreField.getText(),
                rfcField.getText(),
                esJefeDeFamiliaCheckBox.isSelected(),
                Integer.parseInt(edadField.getText()),
                familia
        );

        personaController.crearPersona(persona);
        loadPersonas();
        clearFields();

        // 🔹 Restaurar botones y limpiar selección
        crearButton.setDisable(false);
        actualizarButton.setDisable(true);
        eliminarButton.setDisable(true);
        personaTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleUpdate() {
        if (!validateInput()) return;

        Persona selectedPersona = personaTable.getSelectionModel().getSelectedItem();
        if (selectedPersona != null) {
            selectedPersona.setNombre(nombreField.getText());
            selectedPersona.setRfc(rfcField.getText());
            selectedPersona.setEdad(Integer.parseInt(edadField.getText()));
            selectedPersona.setEsJefeDeFamilia(esJefeDeFamiliaCheckBox.isSelected());

            if (selectedPersona.getFamilia() == null) {
                selectedPersona.setFamilia(new Familia(apellidosField.getText()));
            } else {
                selectedPersona.getFamilia().setApellidos(apellidosField.getText());
            }

            personaController.actualizarPersona(selectedPersona);
            loadPersonas();
            clearFields();

            // 🔹 Restaurar botones y limpiar selección
            crearButton.setDisable(false);
            actualizarButton.setDisable(true);
            eliminarButton.setDisable(true);
            personaTable.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void handleDelete() {
        Persona selectedPersona = personaTable.getSelectionModel().getSelectedItem();
        if (selectedPersona != null) {
            personaController.eliminarPersonaPorId(selectedPersona.getId());
            loadPersonas();
            clearFields();

            // 🔹 Restaurar botones y limpiar selección
            crearButton.setDisable(false);
            actualizarButton.setDisable(true);
            eliminarButton.setDisable(true);
            personaTable.getSelectionModel().clearSelection();
        }
    }
    @FXML
    private void handleLimpiar() {

        clearFields();
        personaTable.getSelectionModel().clearSelection();

        // Restaurar botones
        crearButton.setDisable(false);
        actualizarButton.setDisable(true);
        eliminarButton.setDisable(true);
    }

    private void onTableSelection(Persona selectedPersona) {
        nombreField.setText(selectedPersona.getNombre());
        rfcField.setText(selectedPersona.getRfc());
        edadField.setText(String.valueOf(selectedPersona.getEdad()));
        esJefeDeFamiliaCheckBox.setSelected(selectedPersona.isEsJefeDeFamilia());

        if (selectedPersona.getFamilia() != null) {
            apellidosField.setText(selectedPersona.getFamilia().getApellidos());
        } else {
            apellidosField.clear();
        }
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
