package com.example.vivienda.view;

import com.example.vivienda.controller.EdificioController;
import com.example.vivienda.model.Edificio;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

    @FXML
    private Button crearButton;
    @FXML
    private Button actualizarButton;
    @FXML
    private Button eliminarButton;
    @FXML
    private Button limpiarButton;

    private final EdificioController edificioController = new EdificioController();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        direccionColumn.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        superficieColumn.setCellValueFactory(new PropertyValueFactory<>("superficie"));
        claveCatastralColumn.setCellValueFactory(new PropertyValueFactory<>("claveCatastral"));
        numApartamentosColumn.setCellValueFactory(new PropertyValueFactory<>("totalDepartamentos"));

        loadEdificios();

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
        edificio.setDireccion(direccionField.getText());
        edificio.setSuperficie(Double.parseDouble(superficieField.getText()));
        edificio.setClaveCatastral(claveCatastralField.getText());
        edificio.setTotalDepartamentos(Integer.parseInt(numApartamentosField.getText()));

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

        selectedEdificio.setDireccion(direccionField.getText());
        selectedEdificio.setSuperficie(Double.parseDouble(superficieField.getText()));
        selectedEdificio.setClaveCatastral(claveCatastralField.getText());
        selectedEdificio.setTotalDepartamentos(Integer.parseInt(numApartamentosField.getText()));

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
    }

    private void onTableSelection(Edificio selectedEdificio) {
        direccionField.setText(selectedEdificio.getDireccion());
        superficieField.setText(String.valueOf(selectedEdificio.getSuperficie()));
        claveCatastralField.setText(selectedEdificio.getClaveCatastral());
        numApartamentosField.setText(String.valueOf(selectedEdificio.getTotalDepartamentos()));
    }

    // Limpiar campos y restablecer botones (misma dinámica que Persona)
    private void limpiarCampos() {
        direccionField.clear();
        superficieField.clear();
        claveCatastralField.clear();
        numApartamentosField.clear();
        edificioTable.getSelectionModel().clearSelection();
        setButtonsState(true, false, false);
    }

    private void setButtonsState(boolean crear, boolean actualizar, boolean eliminar) {
        crearButton.setDisable(!crear);
        actualizarButton.setDisable(!actualizar);
        eliminarButton.setDisable(!eliminar);
    }

    private boolean validarCampos() {
        if (direccionField.getText().isEmpty() || superficieField.getText().isEmpty() ||
                claveCatastralField.getText().isEmpty() || numApartamentosField.getText().isEmpty()) {
            mostrarAlerta("Campos incompletos", "Faltan datos",
                    "Todos los campos son obligatorios.");
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
