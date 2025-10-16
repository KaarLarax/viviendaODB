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
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;

public class DepartamentoViewController {

    @FXML private TextField direccionField;
    @FXML private TextField superficieField;
    @FXML private TextField claveCatastralField;
    @FXML private TextField numeroField;
    @FXML private ComboBox<Edificio> comboEdificio;
    @FXML private ComboBox<Colonia> comboColonia;
    @FXML private ComboBox<Persona> comboPropietario;
    @FXML private TableView<Departamento> departamentoTable;
    @FXML private TableColumn<Departamento, Long> idColumn;
    @FXML private TableColumn<Departamento, String> direccionColumn;
    @FXML private TableColumn<Departamento, String> superficieColumn;
    @FXML private TableColumn<Departamento, String> claveCatastralColumn;
    @FXML private TableColumn<Departamento, String> numeroColumn;
    @FXML private TableColumn<Departamento, String> edificioColumn;
    @FXML private TableColumn<Departamento, String> coloniaColumn;
    @FXML private TableColumn<Departamento, String> propietarioColumn;
    @FXML private Button btnCrear;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;

    private final DepartamentoController departamentoController = new DepartamentoController();
    private final PersonaController personaController = new PersonaController();
    private final ColoniaController coloniaController = new ColoniaController();
    private final EdificioController edificioController = new EdificioController();

    @FXML
    public void initialize() {
        // Configurar columnas
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleLongProperty(cellData.getValue().getId()).asObject());
        direccionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDireccion()));
        superficieColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getSuperficie())));
        claveCatastralColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getClaveCatastral()));
        numeroColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNumero()));
        edificioColumn.setCellValueFactory(data -> {
            Edificio edificio = data.getValue().getEdificio();
            return new SimpleStringProperty(edificio != null ? edificio.getDireccion() : "");
        });
        coloniaColumn.setCellValueFactory(data -> {
            Colonia colonia = data.getValue().getColonia();
            return new SimpleStringProperty(colonia != null ? colonia.getNombre() : "");
        });
        propietarioColumn.setCellValueFactory(data -> {
            Persona propietario = data.getValue().getPropietario();
            return new SimpleStringProperty(propietario != null ? propietario.toString() : "");
        });

        // Cargar datos
        cargarDepartamentos();
        comboEdificio.setItems(FXCollections.observableArrayList(edificioController.obtenerTodosLosEdificios()));
        comboColonia.setItems(FXCollections.observableArrayList(coloniaController.obtenerTodasLasColonias()));
        comboPropietario.setItems(FXCollections.observableArrayList(personaController.obtenerTodasLasPersonas()));

        // Configurar CellFactory para mostrar elementos en la lista desplegable
        comboEdificio.setCellFactory(param -> new ListCell<Edificio>() {
            @Override
            protected void updateItem(Edificio item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDireccion());
                }
            }
        });

        comboColonia.setCellFactory(param -> new ListCell<Colonia>() {
            @Override
            protected void updateItem(Colonia item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });

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

        // Configurar buttonCell para mostrar el prompt text cuando no hay selección
        comboEdificio.setButtonCell(new ListCell<Edificio>() {
            @Override
            protected void updateItem(Edificio item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Seleccione edificio");
                } else {
                    setText(item.getDireccion());
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
        departamentoTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarDepartamento(newSelection);
                setButtonsState(false, true, true);
            } else {
                setButtonsState(true, false, false);
            }
        });
    }

    private void cargarDepartamentos() {
        departamentoTable.setItems(FXCollections.observableArrayList(departamentoController.obtenerTodosLosDepartamentos()));
    }

    private void mostrarDepartamento(Departamento depto) {
        if (depto != null) {
            direccionField.setText(depto.getDireccion());
            superficieField.setText(String.valueOf(depto.getSuperficie()));
            claveCatastralField.setText(depto.getClaveCatastral());
            numeroField.setText(depto.getNumero());
            comboEdificio.setValue(depto.getEdificio());
            comboColonia.setValue(depto.getColonia());
            comboPropietario.setValue(depto.getPropietario());
        }
    }

    @FXML
    private void agregarDepartamento() {
        if (!validarCampos()) return;

        // Validar clave catastral duplicada
        boolean claveDuplicada = departamentoController.obtenerTodosLosDepartamentos().stream()
                .anyMatch(d -> d.getClaveCatastral().equals(claveCatastralField.getText()));

        if (claveDuplicada) {
            mostrarAlerta("Error al crear departamento", "Clave Catastral duplicada",
                    "Ya existe un departamento con la clave catastral ingresada.");
            return;
        }

        try {
            String direccion = direccionField.getText().trim();
            double superficie = Double.parseDouble(superficieField.getText().trim());
            String claveCatastral = claveCatastralField.getText().trim();
            String numero = numeroField.getText().trim();
            Edificio edificio = comboEdificio.getValue();
            Colonia colonia = comboColonia.getValue();
            Persona propietario = comboPropietario.getValue();

            Departamento departamento = new Departamento(direccion, superficie, claveCatastral, propietario, colonia, numero);
            departamento.setEdificio(edificio);

            // Asegurar consistencia bidireccional: que la persona propietaria apunte a esta vivienda
            if (propietario != null) {
                propietario.setVivienda(departamento);
            }

            departamentoController.agregarDepartamento(departamento);
            postAction();
        } catch (Exception e) {
            e.printStackTrace(); // Para ver el error en consola
            mostrarAlerta("Error al agregar", "Error inesperado", "Ocurrió un error al crear el departamento: " + e.getMessage());
        }
    }

    @FXML
    private void actualizarDepartamento() {
        Departamento seleccionado = departamentoTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;
        if (!validarCampos()) return;

        // Validar clave catastral duplicada (excepto la actual)
        if (!seleccionado.getClaveCatastral().equals(claveCatastralField.getText())) {
            boolean claveDuplicada = departamentoController.obtenerTodosLosDepartamentos().stream()
                    .anyMatch(d -> d.getClaveCatastral().equals(claveCatastralField.getText()));
            if (claveDuplicada) {
                mostrarAlerta("Error al actualizar departamento", "Clave Catastral duplicada",
                        "Ya existe un departamento con la clave catastral ingresada.");
                return;
            }
        }

        try {
            seleccionado.setDireccion(direccionField.getText().trim());
            seleccionado.setSuperficie(Double.parseDouble(superficieField.getText().trim()));
            seleccionado.setClaveCatastral(claveCatastralField.getText().trim());
            seleccionado.setNumero(numeroField.getText().trim());
            seleccionado.setEdificio(comboEdificio.getValue());
            seleccionado.setColonia(comboColonia.getValue());
            seleccionado.setPropietario(comboPropietario.getValue());

            // Asegurar consistencia bidireccional: que la persona propietaria apunte a esta vivienda
            if (seleccionado.getPropietario() != null) {
                seleccionado.getPropietario().setVivienda(seleccionado);
            }

            departamentoController.actualizarDepartamento(seleccionado);
            postAction();
        } catch (Exception e) {
            mostrarAlerta("Error al editar", "Error inesperado", "Ocurrió un error al actualizar el departamento.");
        }
    }

    @FXML
    private void eliminarDepartamento() {
        Departamento seleccionado = departamentoTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        departamentoController.eliminarDepartamento((int) seleccionado.getId());
        postAction();
    }

    @FXML
    private void limpiarCampos() {
        direccionField.clear();
        superficieField.clear();
        claveCatastralField.clear();
        numeroField.clear();
        comboEdificio.getSelectionModel().clearSelection();
        comboEdificio.setValue(null);
        comboColonia.getSelectionModel().clearSelection();
        comboColonia.setValue(null);
        comboPropietario.getSelectionModel().clearSelection();
        comboPropietario.setValue(null);
        departamentoTable.getSelectionModel().clearSelection();
        setButtonsState(true, false, false);

        // Forzar refresco visual de ComboBox
        comboEdificio.setButtonCell(new ListCell<Edificio>() {
            @Override
            protected void updateItem(Edificio item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Seleccione edificio");
                } else {
                    setText(item.getDireccion());
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
    }

    private void postAction() {
        limpiarCampos();
        cargarDepartamentos();
        departamentoTable.getSelectionModel().clearSelection();
    }

    private void setButtonsState(boolean crear, boolean actualizar, boolean eliminar) {
        btnCrear.setDisable(!crear);
        btnActualizar.setDisable(!actualizar);
        btnEliminar.setDisable(!eliminar);
    }

    private boolean validarCampos() {
        // Validar campos vacíos
        if (direccionField.getText().trim().isEmpty() || superficieField.getText().trim().isEmpty() ||
                claveCatastralField.getText().trim().isEmpty() || numeroField.getText().trim().isEmpty()) {
            mostrarAlerta("Campos incompletos", "Faltan datos",
                    "Todos los campos de texto son obligatorios.");
            return false;
        }

        // Validar que se haya seleccionado edificio
        if (comboEdificio.getValue() == null) {
            mostrarAlerta("Campo incompleto", "Falta seleccionar edificio",
                    "Debe seleccionar un edificio.");
            return false;
        }

        // Validar que se haya seleccionado colonia
        if (comboColonia.getValue() == null) {
            mostrarAlerta("Campo incompleto", "Falta seleccionar colonia",
                    "Debe seleccionar una colonia.");
            return false;
        }

        // Validar que se haya seleccionado propietario
        if (comboPropietario.getValue() == null) {
            mostrarAlerta("Campo incompleto", "Falta seleccionar propietario",
                    "Debe seleccionar un propietario.");
            return false;
        }

        // Validar superficie
        try {
            double superficie = Double.parseDouble(superficieField.getText().trim());
            if (superficie <= 0) {
                mostrarAlerta("Dato inválido", "Superficie inválida",
                        "La superficie debe ser un número positivo.");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Dato inválido", "Superficie inválida",
                    "Ingrese un número válido para la superficie.");
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
