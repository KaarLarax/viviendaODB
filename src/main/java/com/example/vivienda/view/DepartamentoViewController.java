package com.example.vivienda.view;

import com.example.vivienda.controller.DepartamentoController;
import com.example.vivienda.controller.PersonaController;
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
    @FXML private TextField numeroField;
    @FXML private ComboBox<Edificio> comboEdificio;
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
    private final EdificioController edificioController = new EdificioController();

    @FXML
    public void initialize() {
        // Configurar columnas
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleLongProperty(cellData.getValue().getId()).asObject());
        direccionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDireccion()));
        superficieColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getSuperficie())));

        // 🔹 La clave catastral se obtiene del edificio al que pertenece el departamento
        claveCatastralColumn.setCellValueFactory(data -> {
            Edificio edificio = data.getValue().getEdificio();
            return new SimpleStringProperty(edificio != null ? edificio.getClaveCatastral() : "N/A");
        });

        numeroColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNumero()));

        // 🔹 Mostrar el NOMBRE del edificio en lugar de la dirección
        edificioColumn.setCellValueFactory(data -> {
            Edificio edificio = data.getValue().getEdificio();
            return new SimpleStringProperty(edificio != null ? edificio.getNombre() : "");
        });

        // 🔹 La colonia se obtiene del edificio al que pertenece el departamento
        coloniaColumn.setCellValueFactory(data -> {
            Edificio edificio = data.getValue().getEdificio();
            Colonia colonia = edificio != null ? edificio.getColonia() : null;
            return new SimpleStringProperty(colonia != null ? colonia.getNombre() : "");
        });

        propietarioColumn.setCellValueFactory(data -> {
            Persona propietario = data.getValue().getPropietario();
            return new SimpleStringProperty(propietario != null ? propietario.toString() : "");
        });

        // Cargar datos
        cargarDepartamentos();
        comboEdificio.setItems(FXCollections.observableArrayList(edificioController.obtenerTodosLosEdificios()));
        comboPropietario.setItems(FXCollections.observableArrayList(personaController.obtenerTodasLasPersonas()));

        // Configurar CellFactory para mostrar elementos en la lista desplegable
        comboEdificio.setCellFactory(param -> new ListCell<Edificio>() {
            @Override
            protected void updateItem(Edificio item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // 🔹 Mostrar el nombre del edificio en el ComboBox
                    setText(item.getNombre());
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
                    // 🔹 Mostrar el nombre del edificio cuando está seleccionado
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
            numeroField.setText(depto.getNumero());
            comboEdificio.setValue(depto.getEdificio());
            comboPropietario.setValue(depto.getPropietario());
        }
    }

    @FXML
    private void agregarDepartamento() {
        if (!validarCampos()) return;

        try {
            String direccion = direccionField.getText().trim();
            double superficie = Double.parseDouble(superficieField.getText().trim());
            String numero = numeroField.getText().trim();
            Edificio edificio = comboEdificio.getValue();
            Persona propietario = comboPropietario.getValue();

            // 🔹 La colonia se obtiene automáticamente del edificio
            Colonia colonia = edificio != null ? edificio.getColonia() : null;

            // 🔹 El departamento no tiene clave catastral propia, usamos null o una clave generada
            // Como Vivienda requiere claveCatastral, generamos una temporal o usamos la del edificio + número
            String claveCatastralGenerada = edificio != null ? edificio.getClaveCatastral() + "-" + numero : "TEMP-" + numero;

            Departamento departamento = new Departamento(direccion, superficie, claveCatastralGenerada, propietario, colonia, numero);
            departamento.setEdificio(edificio);

            // Asegurar consistencia bidireccional: que la persona propietaria apunte a esta vivienda
            if (propietario != null) {
                propietario.setVivienda(departamento);
            }

            departamentoController.agregarDepartamento(departamento);
            postAction();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al agregar", "Error inesperado", "Ocurrió un error al crear el departamento: " + e.getMessage());
        }
    }

    @FXML
    private void actualizarDepartamento() {
        Departamento seleccionado = departamentoTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;
        if (!validarCampos()) return;

        try {
            seleccionado.setDireccion(direccionField.getText().trim());
            seleccionado.setSuperficie(Double.parseDouble(superficieField.getText().trim()));
            seleccionado.setNumero(numeroField.getText().trim());

            Edificio edificio = comboEdificio.getValue();
            seleccionado.setEdificio(edificio);

            // 🔹 La colonia se obtiene automáticamente del edificio
            Colonia colonia = edificio != null ? edificio.getColonia() : null;
            seleccionado.setColonia(colonia);

            // Actualizar clave catastral generada
            String claveCatastralGenerada = edificio != null ? edificio.getClaveCatastral() + "-" + seleccionado.getNumero() : "TEMP-" + seleccionado.getNumero();
            seleccionado.setClaveCatastral(claveCatastralGenerada);

            seleccionado.setPropietario(comboPropietario.getValue());

            // Asegurar consistencia bidireccional
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
        numeroField.clear();
        comboEdificio.getSelectionModel().clearSelection();
        comboEdificio.setValue(null);
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

    private boolean validarCampos() {
        String mensaje = "";

        if (direccionField.getText() == null || direccionField.getText().trim().isEmpty()) {
            mensaje += "La dirección es obligatoria.\n";
        }

        if (superficieField.getText() == null || superficieField.getText().trim().isEmpty()) {
            mensaje += "La superficie es obligatoria.\n";
        } else {
            try {
                double sup = Double.parseDouble(superficieField.getText().trim());
                if (sup <= 0) {
                    mensaje += "La superficie debe ser mayor a 0.\n";
                }
            } catch (NumberFormatException e) {
                mensaje += "La superficie debe ser un número válido.\n";
            }
        }

        if (numeroField.getText() == null || numeroField.getText().trim().isEmpty()) {
            mensaje += "El número interior es obligatorio.\n";
        }

        if (comboEdificio.getValue() == null) {
            mensaje += "Debe seleccionar un edificio.\n";
        }

        if (comboPropietario.getValue() == null) {
            mensaje += "Debe seleccionar un propietario.\n";
        }

        if (!mensaje.isEmpty()) {
            mostrarAlerta("Validación de campos", "Campos incompletos o inválidos", mensaje);
            return false;
        }
        return true;
    }

    private void postAction() {
        cargarDepartamentos();
        limpiarCampos();

        // 🔹 Refrescar la tabla de colonias para actualizar el número de viviendas
        if (ColoniaViewController.getInstance() != null) {
            ColoniaViewController.getInstance().refreshTable();
        }
    }

    private void setButtonsState(boolean crear, boolean actualizar, boolean eliminar) {
        btnCrear.setDisable(!crear);
        btnActualizar.setDisable(!actualizar);
        btnEliminar.setDisable(!eliminar);
    }

    private void mostrarAlerta(String titulo, String encabezado, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
