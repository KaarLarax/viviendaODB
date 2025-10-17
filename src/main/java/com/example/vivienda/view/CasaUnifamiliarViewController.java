package com.example.vivienda.view;

import com.example.vivienda.controller.CasaUnifamiliarController;
import com.example.vivienda.controller.ColoniaController;
import com.example.vivienda.controller.PersonaController;
import com.example.vivienda.model.CasaUnifamiliar;
import com.example.vivienda.model.Colonia;
import com.example.vivienda.model.Persona;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;

public class CasaUnifamiliarViewController {
    @FXML private TextField txtDireccion;
    @FXML private TextField txtSuperficie;
    @FXML private TextField txtNumeroExterior;
    @FXML private TextField txtNumeroPisos;
    @FXML private ComboBox<Colonia> comboColonia;
    @FXML private ComboBox<Persona> comboPropietario;
    @FXML private TableView<CasaUnifamiliar> casaUnifamiliarTable;
    @FXML private TableColumn<CasaUnifamiliar, Long> idColumn;
    @FXML private TableColumn<CasaUnifamiliar, String> direccionColumn;
    @FXML private TableColumn<CasaUnifamiliar, String> superficieColumn;
    @FXML private TableColumn<CasaUnifamiliar, String> numeroExteriorColumn;
    @FXML private TableColumn<CasaUnifamiliar, Integer> numeroPisosColumn;
    @FXML private TableColumn<CasaUnifamiliar, String> coloniaColumn;
    @FXML private TableColumn<CasaUnifamiliar, String> propietarioColumn;
    @FXML private TableColumn<CasaUnifamiliar, Integer> numeroHabitantesColumn;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnListar;

    // 🔹 Filtrado
    @FXML
    private ComboBox<String> filtroComboBox;
    @FXML
    private TextField filtroTextField;

    private final CasaUnifamiliarController casaDAO = new CasaUnifamiliarController();
    private final ColoniaController coloniaDAO = new ColoniaController();
    private final PersonaController personaDAO = new PersonaController();
    private ObservableList<CasaUnifamiliar> todasLasCasas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurar columnas
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleLongProperty(cellData.getValue().getId()).asObject());
        direccionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDireccion()));
        superficieColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getSuperficie())));
        numeroExteriorColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNumeroExterior()));
        numeroPisosColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getNumeroPisos()));
        coloniaColumn.setCellValueFactory(data -> {
            Colonia colonia = data.getValue().getColonia();
            return new SimpleStringProperty(colonia != null ? colonia.getNombre() : "");
        });
        propietarioColumn.setCellValueFactory(data -> {
            Persona propietario = data.getValue().getPropietario();
            return new SimpleStringProperty(propietario != null ? propietario.toString() : "");
        });

        // Columna de número de habitantes
        numeroHabitantesColumn.setCellValueFactory(data -> {
            CasaUnifamiliar casa = data.getValue();
            int numHabitantes = casa.getHabitantes() != null ? casa.getHabitantes().size() : 0;
            return new javafx.beans.property.SimpleObjectProperty<>(numHabitantes);
        });

        // 🔹 Inicializar ComboBox de filtrado
        filtroComboBox.setItems(FXCollections.observableArrayList(
            "ID",
            "Dirección",
            "Superficie",
            "Número Exterior",
            "Nº Pisos",
            "Colonia",
            "Propietario",
            "Nº Habitantes"
        ));

        // 🔹 Listener para filtrar
        filtroTextField.textProperty().addListener((observable, oldValue, newValue) -> filtrarTabla());
        filtroComboBox.valueProperty().addListener((observable, oldValue, newValue) -> filtrarTabla());

        // Cargar datos
        comboColonia.setItems(FXCollections.observableArrayList(coloniaDAO.obtenerTodasLasColonias()));
        comboPropietario.setItems(FXCollections.observableArrayList(personaDAO.obtenerTodasLasPersonas()));

        // Configurar buttonCell para mostrar el prompt text cuando no hay selección
        comboColonia.setButtonCell(new javafx.scene.control.ListCell<Colonia>() {
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

        comboPropietario.setButtonCell(new javafx.scene.control.ListCell<Persona>() {
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
        casaUnifamiliarTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarCasa(newSelection);
                setButtonsState(false, true, true);
            } else {
                setButtonsState(true, false, false);
            }
        });
    }

    private void filtrarTabla() {
        String filtroAtributo = filtroComboBox.getValue();
        String filtroTexto = filtroTextField.getText();

        if (filtroAtributo == null || filtroTexto == null || filtroTexto.trim().isEmpty()) {
            casaUnifamiliarTable.setItems(todasLasCasas);
            return;
        }

        ObservableList<CasaUnifamiliar> casasFiltradas = FXCollections.observableArrayList();
        String textoMinusculas = filtroTexto.toLowerCase().trim();

        for (CasaUnifamiliar casa : todasLasCasas) {
            boolean coincide = false;

            switch (filtroAtributo) {
                case "ID":
                    coincide = String.valueOf(casa.getId()).contains(textoMinusculas);
                    break;
                case "Dirección":
                    coincide = casa.getDireccion() != null &&
                              casa.getDireccion().toLowerCase().contains(textoMinusculas);
                    break;
                case "Superficie":
                    coincide = String.valueOf(casa.getSuperficie()).contains(textoMinusculas);
                    break;
                case "Número Exterior":
                    coincide = casa.getNumeroExterior() != null &&
                              casa.getNumeroExterior().toLowerCase().contains(textoMinusculas);
                    break;
                case "Nº Pisos":
                    coincide = String.valueOf(casa.getNumeroPisos()).contains(textoMinusculas);
                    break;
                case "Colonia":
                    Colonia c = casa.getColonia();
                    coincide = c != null && c.getNombre() != null &&
                              c.getNombre().toLowerCase().contains(textoMinusculas);
                    break;
                case "Propietario":
                    Persona p = casa.getPropietario();
                    if (p != null) {
                        String nombreCompleto = p.getNombre() != null ? p.getNombre() : "";
                        if (p.getFamilia() != null && p.getFamilia().getApellidos() != null) {
                            nombreCompleto += " " + p.getFamilia().getApellidos();
                        }
                        coincide = nombreCompleto.toLowerCase().contains(textoMinusculas);
                    }
                    break;
                case "Nº Habitantes":
                    int numHabitantes = casa.getHabitantes() != null ? casa.getHabitantes().size() : 0;
                    coincide = String.valueOf(numHabitantes).contains(textoMinusculas);
                    break;
            }

            if (coincide) {
                casasFiltradas.add(casa);
            }
        }

        casaUnifamiliarTable.setItems(casasFiltradas);
    }

    private void loadCasas() {
        todasLasCasas.setAll(casaDAO.obtenerTodasLasCasas());
        casaUnifamiliarTable.setItems(todasLasCasas);
    }

    private void mostrarCasa(CasaUnifamiliar casa) {
        if (casa != null) {
            txtDireccion.setText(casa.getDireccion());
            txtSuperficie.setText(String.valueOf(casa.getSuperficie()));
            txtNumeroExterior.setText(casa.getNumeroExterior());
            txtNumeroPisos.setText(String.valueOf(casa.getNumeroPisos()));
            comboColonia.setValue(casa.getColonia());
            comboPropietario.setValue(casa.getPropietario());
        }
    }

    @FXML
    private void onAgregar() {
        if (!validarCampos()) return;

        // Validar número exterior duplicado en la misma dirección
        String direccionIngresada = txtDireccion.getText().trim();
        String numeroExteriorIngresado = txtNumeroExterior.getText().trim();

        boolean numeroDuplicado = casaDAO.obtenerTodasLasCasas().stream()
                .anyMatch(c -> c.getDireccion() != null && c.getDireccion().trim().equalsIgnoreCase(direccionIngresada) &&
                              c.getNumeroExterior() != null && c.getNumeroExterior().trim().equals(numeroExteriorIngresado));

        if (numeroDuplicado) {
            mostrarAlerta("Error al crear casa", "Número Exterior duplicado",
                    "Ya existe una vivienda con el número exterior " + numeroExteriorIngresado + " en la dirección " + direccionIngresada + ".");
            return;
        }

        try {
            String direccion = txtDireccion.getText().trim();
            double superficie = Double.parseDouble(txtSuperficie.getText().trim());
            String numeroExterior = txtNumeroExterior.getText().trim();
            int pisos = Integer.parseInt(txtNumeroPisos.getText().trim());
            Colonia colonia = comboColonia.getValue();
            Persona propietario = comboPropietario.getValue();

            CasaUnifamiliar casa = new CasaUnifamiliar(direccion, superficie, numeroExterior, propietario, colonia, pisos);
            // Mantener consistencia bidireccional: que la persona propietaria apunte a esta vivienda
            if (propietario != null) {
                propietario.setVivienda(casa);
            }

            casaDAO.crearCasa(casa);
            postAction();
        } catch (javax.persistence.PersistenceException e) {
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().contains("Unique constraint")) {
                mostrarAlerta("Error al crear casa", "Número Exterior duplicado",
                        "El número exterior '" + numeroExteriorIngresado + "' ya existe en la dirección '" + direccionIngresada + "'.");
            } else {
                mostrarAlerta("Error al agregar", "Error inesperado", "Ocurrió un error al crear la casa: " + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al agregar", "Error inesperado", "Ocurrió un error al crear la casa: " + e.getMessage());
        }
    }

    @FXML
    private void onEditar() {
        CasaUnifamiliar seleccionada = casaUnifamiliarTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;
        if (!validarCampos()) return;

        // Validar número exterior duplicado (excepto la actual)
        String direccionIngresada = txtDireccion.getText().trim();
        String numeroExteriorIngresado = txtNumeroExterior.getText().trim();
        String direccionOriginal = seleccionada.getDireccion() != null ? seleccionada.getDireccion().trim() : "";
        String numeroExteriorOriginal = seleccionada.getNumeroExterior() != null ? seleccionada.getNumeroExterior().trim() : "";

        if (!direccionOriginal.equalsIgnoreCase(direccionIngresada) || !numeroExteriorOriginal.equals(numeroExteriorIngresado)) {
            boolean numeroDuplicado = casaDAO.obtenerTodasLasCasas().stream()
                    .anyMatch(c -> c.getDireccion() != null && c.getDireccion().trim().equalsIgnoreCase(direccionIngresada) &&
                                   c.getNumeroExterior() != null && c.getNumeroExterior().trim().equals(numeroExteriorIngresado) &&
                                   c.getId() != seleccionada.getId());
            if (numeroDuplicado) {
                mostrarAlerta("Error al actualizar casa", "Número Exterior duplicado",
                        "Ya existe una vivienda con el número exterior " + numeroExteriorIngresado + " en la dirección " + direccionIngresada + ".");
                return;
            }
        }

        try {
            seleccionada.setDireccion(txtDireccion.getText().trim());
            seleccionada.setSuperficie(Double.parseDouble(txtSuperficie.getText().trim()));
            seleccionada.setNumeroExterior(txtNumeroExterior.getText().trim());
            seleccionada.setNumeroPisos(Integer.parseInt(txtNumeroPisos.getText().trim()));
            seleccionada.setColonia(comboColonia.getValue());
            seleccionada.setPropietario(comboPropietario.getValue());
            // Mantener consistencia bidireccional: que la persona propietaria apunte a esta vivienda
            if (seleccionada.getPropietario() != null) {
                seleccionada.getPropietario().setVivienda(seleccionada);
            }
            casaDAO.actualizarCasa(seleccionada);
            postAction();
        } catch (javax.persistence.PersistenceException e) {
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().contains("Unique constraint")) {
                mostrarAlerta("Error al actualizar casa", "Número Exterior duplicado",
                        "El número exterior '" + numeroExteriorIngresado + "' ya existe en la dirección '" + direccionIngresada + "'.");
            } else {
                mostrarAlerta("Error al editar", "Error inesperado", "Ocurrió un error al actualizar la casa: " + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al editar", "Error inesperado", "Ocurrió un error al actualizar la casa: " + e.getMessage());
        }
    }

    @FXML
    private void onEliminar() {
        CasaUnifamiliar seleccionada = casaUnifamiliarTable.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;

        casaDAO.eliminarCasa(seleccionada);
        postAction();
    }

    @FXML
    private void onLimpiar() {
        txtDireccion.clear();
        txtSuperficie.clear();
        txtNumeroExterior.clear();
        txtNumeroPisos.clear();
        comboColonia.setValue(null);
        comboPropietario.setValue(null);
        casaUnifamiliarTable.getSelectionModel().clearSelection();

        // 🔹 Limpiar filtros
        filtroComboBox.getSelectionModel().clearSelection();
        filtroTextField.clear();

        setButtonsState(true, false, false);

        // Forzar refresco visual de ComboBox
        comboColonia.setButtonCell(new javafx.scene.control.ListCell<Colonia>() {
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

        comboPropietario.setButtonCell(new javafx.scene.control.ListCell<Persona>() {
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

    @FXML
    private void onListar() {
        loadCasas();
    }

    @FXML
    private void onVolver() {
        // Implementa la lógica para volver al menú principal o cerrar la ventana
    }

    private void postAction() {
        limpiarCampos();
        loadCasas();
        casaUnifamiliarTable.getSelectionModel().clearSelection();

        // 🔹 Refrescar la tabla de colonias para actualizar el número de viviendas
        if (ColoniaViewController.getInstance() != null) {
            ColoniaViewController.getInstance().refreshTable();
        }
    }

    private void limpiarCampos() {
        txtDireccion.clear();
        txtSuperficie.clear();
        txtNumeroExterior.clear();
        txtNumeroPisos.clear();
        comboColonia.getSelectionModel().clearSelection();
        comboColonia.setValue(null);
        comboPropietario.getSelectionModel().clearSelection();
        comboPropietario.setValue(null);
        casaUnifamiliarTable.getSelectionModel().clearSelection();
        setButtonsState(true, false, false);

        // Forzar refresco visual
        comboColonia.setButtonCell(new javafx.scene.control.ListCell<Colonia>() {
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

        comboPropietario.setButtonCell(new javafx.scene.control.ListCell<Persona>() {
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
        btnAgregar.setDisable(!crear);
        btnEditar.setDisable(!actualizar);
        btnEliminar.setDisable(!eliminar);
    }

    private boolean validarCampos() {
        // Validar campos vacíos
        if (txtDireccion.getText().trim().isEmpty() || txtSuperficie.getText().trim().isEmpty() ||
                txtNumeroExterior.getText().trim().isEmpty() || txtNumeroPisos.getText().trim().isEmpty()) {
            mostrarAlerta("Campos incompletos", "Faltan datos",
                    "Todos los campos de texto son obligatorios.");
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
            double superficie = Double.parseDouble(txtSuperficie.getText().trim());
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

        // Validar número de pisos
        try {
            int pisos = Integer.parseInt(txtNumeroPisos.getText().trim());
            if (pisos <= 0) {
                mostrarAlerta("Dato inválido", "Número de pisos inválido",
                        "El número de pisos debe ser un número positivo.");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Dato inválido", "Número de pisos inválido",
                    "Ingrese un número entero válido para el número de pisos.");
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
