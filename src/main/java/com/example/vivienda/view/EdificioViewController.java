package com.example.vivienda.view;

import com.example.vivienda.controller.EdificioController;
import com.example.vivienda.controller.ColoniaController;
import com.example.vivienda.controller.PersonaController;
import com.example.vivienda.model.Edificio;
import com.example.vivienda.model.Colonia;
import com.example.vivienda.model.Persona;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;

public class EdificioViewController {

    @FXML
    private TextField nombreField;
    @FXML
    private TextField direccionField;
    @FXML
    private TextField superficieField;
    @FXML
    private TextField claveCatastralField;
    @FXML
    private TextField numApartamentosField;
    @FXML
    private ComboBox<Colonia> comboColonia;
    @FXML
    private ComboBox<Persona> comboPropietario;
    @FXML
    private TableView<Edificio> edificioTable;
    @FXML
    private TableColumn<Edificio, Long> idColumn;
    @FXML
    private TableColumn<Edificio, String> nombreColumn;
    @FXML
    private TableColumn<Edificio, String> direccionColumn;
    @FXML
    private TableColumn<Edificio, Double> superficieColumn;
    @FXML
    private TableColumn<Edificio, String> claveCatastralColumn;
    @FXML
    private TableColumn<Edificio, String> coloniaColumn;
    @FXML
    private TableColumn<Edificio, String> propietarioColumn;
    @FXML
    private TableColumn<Edificio, Integer> numApartamentosColumn;

    @FXML
    private Button crearButton;
    @FXML
    private Button actualizarButton;
    @FXML
    private Button eliminarButton;
    @FXML
    private Button limpiarButton;

    private final EdificioController edificioController = new EdificioController();
    private final ColoniaController coloniaController = new ColoniaController();
    private final PersonaController personaController = new PersonaController();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        direccionColumn.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        superficieColumn.setCellValueFactory(new PropertyValueFactory<>("superficie"));
        claveCatastralColumn.setCellValueFactory(new PropertyValueFactory<>("claveCatastral"));

        // Columna de colonia
        coloniaColumn.setCellValueFactory(data -> {
            Colonia colonia = data.getValue().getColonia();
            return new SimpleStringProperty(colonia != null ? colonia.getNombre() : "");
        });

        // Columna de propietario
        propietarioColumn.setCellValueFactory(data -> {
            Persona propietario = data.getValue().getPropietario();
            return new SimpleStringProperty(propietario != null ? propietario.toString() : "");
        });

        numApartamentosColumn.setCellValueFactory(new PropertyValueFactory<>("totalDepartamentos"));

        // Cargar datos
        loadEdificios();
        comboColonia.setItems(FXCollections.observableArrayList(coloniaController.obtenerTodasLasColonias()));
        comboPropietario.setItems(FXCollections.observableArrayList(personaController.obtenerTodasLasPersonas()));

        // Configurar CellFactory para el ComboBox de Colonia
        comboColonia.setCellFactory(param -> new ListCell<Colonia>() {
            @Override
            protected void updateItem(Colonia item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNombre());
                }
            }
        });

        comboColonia.setButtonCell(new ListCell<Colonia>() {
            @Override
            protected void updateItem(Colonia item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Seleccione colonia");
                } else {
                    setText(item.getNombre());
                }
            }
        });

        // Configurar CellFactory para el ComboBox de Propietario
        comboPropietario.setCellFactory(param -> new ListCell<Persona>() {
            @Override
            protected void updateItem(Persona item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });

        comboPropietario.setButtonCell(new ListCell<Persona>() {
            @Override
            protected void updateItem(Persona item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Seleccione propietario");
                } else {
                    setText(item.toString());
                }
            }
        });

        // Inicializar botones
        setButtonsState(true, false, false);

        // Listener de selección de tabla
        edificioTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                onTableSelection(newSelection);
                setButtonsState(false, true, true);
            } else {
                limpiarCampos();
            }
        });

        // Botón limpiar
        limpiarButton.setOnAction(e -> limpiarCampos());
    }

    private void loadEdificios() {
        edificioTable.getItems().setAll(edificioController.obtenerTodosLosEdificios());
    }

    @FXML
    private void handleCreate() {
        if (!validarCampos()) return;

        // Validar clave catastral duplicada
        boolean claveDuplicada = edificioController.obtenerTodosLosEdificios().stream()
                .anyMatch(e -> e.getClaveCatastral().equals(claveCatastralField.getText()));

        if (claveDuplicada) {
            mostrarAlerta("Error al crear edificio", "Clave Catastral duplicada",
                    "Ya existe un edificio con la clave catastral ingresada.");
            return;
        }

        Edificio edificio = new Edificio();
        edificio.setNombre(nombreField.getText());
        edificio.setDireccion(direccionField.getText());
        edificio.setSuperficie(Double.parseDouble(superficieField.getText()));
        edificio.setClaveCatastral(claveCatastralField.getText());
        edificio.setColonia(comboColonia.getValue());
        edificio.setPropietario(comboPropietario.getValue());
        edificio.setTotalDepartamentos(Integer.parseInt(numApartamentosField.getText()));

        // Mantener consistencia bidireccional
        if (edificio.getPropietario() != null) {
            edificio.getPropietario().setVivienda(edificio);
        }

        edificioController.crearEdificio(edificio);
        postAction();
    }

    @FXML
    private void handleUpdate() {
        Edificio selectedEdificio = edificioTable.getSelectionModel().getSelectedItem();
        if (selectedEdificio == null) return;
        if (!validarCampos()) return;

        if (!selectedEdificio.getClaveCatastral().equals(claveCatastralField.getText())) {
            boolean claveDuplicada = edificioController.obtenerTodosLosEdificios().stream()
                    .anyMatch(e -> e.getClaveCatastral().equals(claveCatastralField.getText()));
            if (claveDuplicada) {
                mostrarAlerta("Error al actualizar edificio", "Clave Catastral duplicada",
                        "Ya existe un edificio con la clave catastral ingresada.");
                return;
            }
        }

        selectedEdificio.setNombre(nombreField.getText());
        selectedEdificio.setDireccion(direccionField.getText());
        selectedEdificio.setSuperficie(Double.parseDouble(superficieField.getText()));
        selectedEdificio.setClaveCatastral(claveCatastralField.getText());
        selectedEdificio.setColonia(comboColonia.getValue());
        selectedEdificio.setPropietario(comboPropietario.getValue());
        selectedEdificio.setTotalDepartamentos(Integer.parseInt(numApartamentosField.getText()));

        // Mantener consistencia bidireccional
        if (selectedEdificio.getPropietario() != null) {
            selectedEdificio.getPropietario().setVivienda(selectedEdificio);
        }

        edificioController.actualizarEdificio(selectedEdificio);
        postAction();
    }

    @FXML
    private void handleDelete() {
        Edificio selectedEdificio = edificioTable.getSelectionModel().getSelectedItem();
        if (selectedEdificio == null) return;

        edificioController.eliminarEdificioPorId(selectedEdificio.getId());
        postAction();
    }

    private void postAction() {
        limpiarCampos();
        loadEdificios();
        edificioTable.getSelectionModel().clearSelection();

        // 🔹 Refrescar la tabla de colonias para actualizar el número de viviendas
        if (ColoniaViewController.getInstance() != null) {
            ColoniaViewController.getInstance().refreshTable();
        }
    }

    private void onTableSelection(Edificio selectedEdificio) {
        nombreField.setText(selectedEdificio.getNombre());
        direccionField.setText(selectedEdificio.getDireccion());
        superficieField.setText(String.valueOf(selectedEdificio.getSuperficie()));
        claveCatastralField.setText(selectedEdificio.getClaveCatastral());
        comboColonia.setValue(selectedEdificio.getColonia());
        comboPropietario.setValue(selectedEdificio.getPropietario());
        numApartamentosField.setText(String.valueOf(selectedEdificio.getTotalDepartamentos()));
    }

    // Limpiar campos y restablecer botones (misma dinámica que Persona)
    private void limpiarCampos() {
        nombreField.clear();
        direccionField.clear();
        superficieField.clear();
        claveCatastralField.clear();
        numApartamentosField.clear();
        comboColonia.getSelectionModel().clearSelection();
        comboColonia.setValue(null);
        comboPropietario.getSelectionModel().clearSelection();
        comboPropietario.setValue(null);
        edificioTable.getSelectionModel().clearSelection();
        setButtonsState(true, false, false);

        // Forzar refresco visual de los ComboBox
        comboColonia.setButtonCell(new ListCell<Colonia>() {
            @Override
            protected void updateItem(Colonia item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Seleccione colonia");
                } else {
                    setText(item.getNombre());
                }
            }
        });

        comboPropietario.setButtonCell(new ListCell<Persona>() {
            @Override
            protected void updateItem(Persona item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Seleccione propietario");
                } else {
                    setText(item.toString());
                }
            }
        });
    }

    private void setButtonsState(boolean crear, boolean actualizar, boolean eliminar) {
        crearButton.setDisable(!crear);
        actualizarButton.setDisable(!actualizar);
        eliminarButton.setDisable(!eliminar);
    }

    private boolean validarCampos() {
        if (nombreField.getText().isEmpty() || direccionField.getText().isEmpty() || superficieField.getText().isEmpty() ||
                claveCatastralField.getText().isEmpty() || numApartamentosField.getText().isEmpty()) {
            mostrarAlerta("Campos incompletos", "Faltan datos",
                    "Todos los campos son obligatorios.");
            return false;
        }

        if (comboColonia.getValue() == null) {
            mostrarAlerta("Campos incompletos", "Faltan datos",
                    "Debe seleccionar una colonia.");
            return false;
        }

        if (comboPropietario.getValue() == null) {
            mostrarAlerta("Campos incompletos", "Faltan datos",
                    "Debe seleccionar un propietario.");
            return false;
        }

        try {
            Double.parseDouble(superficieField.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Dato inválido", "Superficie inválida",
                    "Ingrese un número válido para superficie.");
            return false;
        }

        try {
            Integer.parseInt(numApartamentosField.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Dato inválido", "Número de departamentos inválido",
                    "Ingrese un número válido para total de departamentos.");
            return false;
        }

        return true;
    }

    private void mostrarAlerta(String titulo, String header, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
