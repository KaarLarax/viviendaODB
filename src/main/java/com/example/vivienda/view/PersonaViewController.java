package com.example.vivienda.view;

import com.example.vivienda.controller.CasaUnifamiliarController;
import com.example.vivienda.controller.DepartamentoController;
import com.example.vivienda.controller.FamiliaController;
import com.example.vivienda.controller.PersonaController;
import com.example.vivienda.model.CasaUnifamiliar;
import com.example.vivienda.model.Departamento;
import com.example.vivienda.model.Familia;
import com.example.vivienda.model.Persona;
import com.example.vivienda.model.Vivienda;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
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
    private ComboBox<Vivienda> viviendaComboBox;
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
    private TableColumn<Persona, String> esJefeDeFamiliaColumn;  // 🔹 Cambiar de Boolean a String
    @FXML
    private TableColumn<Persona, String> viviendaColumn;
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
    @FXML
    private Button listarButton;

    // 🔹 Filtrado
    @FXML
    private ComboBox<String> filtroComboBox;
    @FXML
    private TextField filtroTextField;

    private final PersonaController personaController = new PersonaController();
    private final FamiliaController familiaController = new FamiliaController();
    private final CasaUnifamiliarController casaUnifamiliarController = new CasaUnifamiliarController();
    private final DepartamentoController departamentoController = new DepartamentoController();
    private ObservableList<Persona> todasLasPersonas = FXCollections.observableArrayList();

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

        // 🔹 Mostrar "Sí" o "No" en lugar de true/false
        esJefeDeFamiliaColumn.setCellValueFactory(cellData -> {
            boolean esJefe = cellData.getValue().isEsJefeDeFamilia();
            return new SimpleStringProperty(esJefe ? "Sí" : "No");
        });

        viviendaColumn.setCellValueFactory(cellData -> {
            Vivienda v = cellData.getValue().getVivienda();
            if (v == null) {
                return new SimpleStringProperty("Sin vivienda");
            }
            if (v instanceof CasaUnifamiliar) {
                CasaUnifamiliar casa = (CasaUnifamiliar) v;
                return new SimpleStringProperty("Casa: " + casa.getDireccion());
            } else if (v instanceof Departamento) {
                Departamento depto = (Departamento) v;
                String edificioInfo = "";
                if (depto.getEdificio() != null) {
                    edificioInfo = " - Edif: " + depto.getEdificio().getNombre();
                }
                return new SimpleStringProperty("Depto #" + depto.getNumero() + edificioInfo);
            }
            return new SimpleStringProperty(v.getDireccion());
        });
        numPropiedadesColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(personaController.obtenerNumeroDePropiedades(cellData.getValue())).asObject()
        );

        // 🔹 Inicializar ComboBox de filtrado
        filtroComboBox.setItems(FXCollections.observableArrayList(
            "ID",
            "Nombre",
            "Apellidos",
            "RFC",
            "Edad",
            "Jefe de Familia",
            "Vivienda",
            "N. Prop."
        ));

        // 🔹 Listener para filtrar
        filtroTextField.textProperty().addListener((observable, oldValue, newValue) -> filtrarTabla());
        filtroComboBox.valueProperty().addListener((observable, oldValue, newValue) -> filtrarTabla());

        // Configurar ComboBox de familias
        configurarFamiliaComboBox();
        configurarViviendaComboBox();
        loadFamilias();
        loadViviendas();
        // loadPersonas(); // Eliminado el llamado automático a loadPersonas

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

    private void filtrarTabla() {
        String filtroAtributo = filtroComboBox.getValue();
        String filtroTexto = filtroTextField.getText();

        if (filtroAtributo == null || filtroTexto == null || filtroTexto.trim().isEmpty()) {
            personaTable.setItems(todasLasPersonas);
            return;
        }

        ObservableList<Persona> personasFiltradas = FXCollections.observableArrayList();
        String textoMinusculas = filtroTexto.toLowerCase().trim();

        for (Persona persona : todasLasPersonas) {
            boolean coincide = false;

            switch (filtroAtributo) {
                case "ID":
                    coincide = String.valueOf(persona.getId()).contains(textoMinusculas);
                    break;
                case "Nombre":
                    coincide = persona.getNombre() != null &&
                              persona.getNombre().toLowerCase().contains(textoMinusculas);
                    break;
                case "Apellidos":
                    Familia f = persona.getFamilia();
                    coincide = f != null && f.getApellidos() != null &&
                              f.getApellidos().toLowerCase().contains(textoMinusculas);
                    break;
                case "RFC":
                    coincide = persona.getRfc() != null &&
                              persona.getRfc().toLowerCase().contains(textoMinusculas);
                    break;
                case "Edad":
                    coincide = String.valueOf(persona.getEdad()).contains(textoMinusculas);
                    break;
                case "Jefe de Familia":
                    String esJefe = persona.isEsJefeDeFamilia() ? "si" : "no";
                    coincide = esJefe.contains(textoMinusculas);
                    break;
                case "Vivienda":
                    Vivienda v = persona.getVivienda();
                    if (v != null) {
                        if (v instanceof CasaUnifamiliar) {
                            CasaUnifamiliar casa = (CasaUnifamiliar) v;
                            coincide = casa.getDireccion() != null &&
                                      casa.getDireccion().toLowerCase().contains(textoMinusculas);
                        } else if (v instanceof Departamento) {
                            Departamento depto = (Departamento) v;
                            coincide = (depto.getNumero() != null &&
                                      depto.getNumero().toLowerCase().contains(textoMinusculas)) ||
                                      (depto.getEdificio() != null && depto.getEdificio().getNombre() != null &&
                                      depto.getEdificio().getNombre().toLowerCase().contains(textoMinusculas));
                        }
                    }
                    break;
                case "N. Prop.":
                    int numProp = personaController.obtenerNumeroDePropiedades(persona);
                    coincide = String.valueOf(numProp).contains(textoMinusculas);
                    break;
            }

            if (coincide) {
                personasFiltradas.add(persona);
            }
        }

        personaTable.setItems(personasFiltradas);
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

    private void configurarViviendaComboBox() {
        viviendaComboBox.setConverter(new StringConverter<Vivienda>() {
            @Override
            public String toString(Vivienda vivienda) {
                if (vivienda == null) {
                    return null;
                }
                if (vivienda instanceof CasaUnifamiliar) {
                    CasaUnifamiliar casa = (CasaUnifamiliar) vivienda;
                    return "Casa: " + casa.getDireccion() + " (Clave: " + casa.getNumeroExterior() + ")";
                } else if (vivienda instanceof Departamento) {
                    Departamento depto = (Departamento) vivienda;
                    String edificioInfo = "";
                    if (depto.getEdificio() != null) {
                        edificioInfo = " - Edificio: " + depto.getEdificio().getNombre();
                    }
                    return "Depto #" + depto.getNumero() + ": " + depto.getDireccion() + edificioInfo + " (Clave: " + depto.getNumeroExterior() + ")";
                }
                return vivienda.getDireccion();
            }

            @Override
            public Vivienda fromString(String string) {
                return null;
            }
        });
    }

    private void loadViviendas() {
        List<Vivienda> viviendas = new ArrayList<>();

        // Cargar todas las casas unifamiliares
        List<CasaUnifamiliar> casas = casaUnifamiliarController.obtenerTodasLasCasas();
        if (casas != null) {
            viviendas.addAll(casas);
        }

        // Cargar todos los departamentos
        List<Departamento> departamentos = departamentoController.obtenerTodosLosDepartamentos();
        if (departamentos != null) {
            viviendas.addAll(departamentos);
        }

        viviendaComboBox.getItems().setAll(viviendas);
    }

    private void loadFamilias() {
        List<Familia> familias = familiaController.obtenerTodasLasFamilias();
        familiaComboBox.getItems().setAll(familias);
    }

    private void loadPersonas() {
        List<Persona> personas = personaController.obtenerTodasLasPersonas();
        todasLasPersonas.setAll(personas);
        personaTable.setItems(todasLasPersonas);
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

        // Asignar la vivienda seleccionada
        Vivienda viviendaSeleccionada = viviendaComboBox.getValue();
        if (viviendaSeleccionada != null) {
            persona.setVivienda(viviendaSeleccionada);
        }

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

            // Asignar la vivienda seleccionada
            Vivienda viviendaSeleccionada = viviendaComboBox.getValue();
            selectedPersona.setVivienda(viviendaSeleccionada);

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

        // 🔹 Limpiar filtros
        filtroComboBox.getSelectionModel().clearSelection();
        filtroTextField.clear();

        // Restaurar botones
        crearButton.setDisable(false);
        actualizarButton.setDisable(true);
        eliminarButton.setDisable(true);
    }

    @FXML
    private void handleListar() {
        loadPersonas();
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

        if (selectedPersona.getVivienda() != null) {
            viviendaComboBox.setValue(selectedPersona.getVivienda());
        } else {
            viviendaComboBox.setValue(null);
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
        viviendaComboBox.setValue(null);

        // Forzar refresco visual de ComboBox
        familiaComboBox.setButtonCell(new ListCell<Familia>() {
            @Override
            protected void updateItem(Familia item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Seleccione familia");
                } else {
                    setText(item.getApellidos() + " (ID: " + item.getId() + ")");
                }
            }
        });

        viviendaComboBox.setButtonCell(new ListCell<Vivienda>() {
            @Override
            protected void updateItem(Vivienda item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Seleccione vivienda");
                } else {
                    if (item instanceof CasaUnifamiliar) {
                        CasaUnifamiliar casa = (CasaUnifamiliar) item;
                        setText("Casa: " + casa.getDireccion() + " (Clave: " + casa.getNumeroExterior() + ")");
                    } else if (item instanceof Departamento) {
                        Departamento depto = (Departamento) item;
                        String edificioInfo = "";
                        if (depto.getEdificio() != null) {
                            edificioInfo = " - Edificio: " + depto.getEdificio().getNombre();
                        }
                        setText("Depto #" + depto.getNumero() + ": " + depto.getDireccion() + edificioInfo + " (Clave: " + depto.getNumeroExterior() + ")");
                    } else {
                        setText(item.getDireccion());
                    }
                }
            }
        });
    }
}
