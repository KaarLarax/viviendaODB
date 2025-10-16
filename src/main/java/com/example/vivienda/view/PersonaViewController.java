package com.example.vivienda.view;

import com.example.vivienda.controller.FamiliaController;
import com.example.vivienda.controller.PersonaController;
import com.example.vivienda.model.Familia;
import com.example.vivienda.model.Persona;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import java.util.List;

public class PersonaViewController {

    @FXML
    private TextField nombreField;
    @FXML
    private ComboBox<Familia> familiaComboBox;
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
    private final FamiliaController familiaController = new FamiliaController();

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

        // Configurar ComboBox de familias
        configurarFamiliaComboBox();
        loadFamilias();
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

    private void configurarFamiliaComboBox() {
        familiaComboBox.setConverter(new StringConverter<Familia>() {
            @Override
            public String toString(Familia familia) {
                if (familia == null) {
                    return null;
                }
                return familia.getApellidos() + " (ID: " + familia.getId() + ")";
            }

            @Override
            public Familia fromString(String string) {
                return null;
            }
        });
    }

    private void loadFamilias() {
        List<Familia> familias = familiaController.obtenerTodasLasFamilias();
        familiaComboBox.getItems().setAll(familias);
    }

    private void loadPersonas() {
        List<Persona> personas = personaController.obtenerTodasLasPersonas();
        personaTable.getItems().setAll(personas);
    }

    @FXML
    private void handleCreate() {
        if (!validateInput()) return;

        Familia familiaSeleccionada = familiaComboBox.getValue();

        // Validar que solo haya un jefe de familia por apellidos
        if (esJefeDeFamiliaCheckBox.isSelected()) {
            Persona jefeFamiliaExistente = personaController.obtenerJefeDeFamiliaPorApellidos(familiaSeleccionada.getApellidos());
            if (jefeFamiliaExistente != null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Ya existe un jefe de familia");
                alert.setContentText("La familia con apellidos '" + familiaSeleccionada.getApellidos() +
                                   "' ya tiene un jefe de familia: " + jefeFamiliaExistente.getNombre());
                alert.showAndWait();
                return;
            }
        }

        Persona persona = new Persona(
                nombreField.getText(),
                rfcField.getText(),
                esJefeDeFamiliaCheckBox.isSelected(),
                Integer.parseInt(edadField.getText()),
                familiaSeleccionada
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
            Familia familiaSeleccionada = familiaComboBox.getValue();

            // Validar que solo haya un jefe de familia por apellidos
            if (esJefeDeFamiliaCheckBox.isSelected()) {
                Persona jefeFamiliaExistente = personaController.obtenerJefeDeFamiliaPorApellidos(familiaSeleccionada.getApellidos());
                // Si existe otro jefe de familia y NO es la persona actual que estamos editando
                if (jefeFamiliaExistente != null && jefeFamiliaExistente.getId() != selectedPersona.getId()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Ya existe un jefe de familia");
                    alert.setContentText("La familia con apellidos '" + familiaSeleccionada.getApellidos() +
                                       "' ya tiene un jefe de familia: " + jefeFamiliaExistente.getNombre());
                    alert.showAndWait();
                    return;
                }
            }

            selectedPersona.setNombre(nombreField.getText());
            selectedPersona.setRfc(rfcField.getText());
            selectedPersona.setEdad(Integer.parseInt(edadField.getText()));
            selectedPersona.setEsJefeDeFamilia(esJefeDeFamiliaCheckBox.isSelected());
            selectedPersona.setFamilia(familiaSeleccionada);

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
            familiaComboBox.setValue(selectedPersona.getFamilia());
        } else {
            familiaComboBox.setValue(null);
        }
    }

    private boolean validateInput() {
        String errorMessage = "";

        if (nombreField.getText() == null || nombreField.getText().trim().isEmpty()) {
            errorMessage += "Nombre no válido.\n";
        }
        if (familiaComboBox.getValue() == null) {
            errorMessage += "Debe seleccionar una familia.\n";
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
        familiaComboBox.setValue(null);
        rfcField.clear();
        edadField.clear();
        esJefeDeFamiliaCheckBox.setSelected(false);
    }
}
