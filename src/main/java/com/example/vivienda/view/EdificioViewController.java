package com.example.vivienda.view;

import com.example.vivienda.controller.EdificioController;
import com.example.vivienda.controller.ColoniaController;
import com.example.vivienda.controller.PersonaController;
import com.example.vivienda.controller.DepartamentoController;
import com.example.vivienda.model.Edificio;
import com.example.vivienda.model.Colonia;
import com.example.vivienda.model.Persona;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.beans.property.SimpleIntegerProperty;

public class EdificioViewController {

    private static EdificioViewController instance;

    @FXML
    private TextField nombreField;
    @FXML
    private TextField direccionField;
    @FXML
    private TextField superficieField;
    @FXML
    private TextField numeroExteriorField;
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
    private TableColumn<Edificio, String> numeroExteriorColumn;
    @FXML
    private TableColumn<Edificio, String> coloniaColumn;
    @FXML
    private TableColumn<Edificio, String> propietarioColumn;
    @FXML
    private TableColumn<Edificio, Integer> numApartamentosColumn;
    @FXML
    private TableColumn<Edificio, Integer> numeroHabitantesColumn;

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

    private final EdificioController edificioController = new EdificioController();
    private final ColoniaController coloniaController = new ColoniaController();
    private final PersonaController personaController = new PersonaController();
    private final DepartamentoController departamentoController = new DepartamentoController();
    private ObservableList<Edificio> todosLosEdificios = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        instance = this;

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        direccionColumn.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        superficieColumn.setCellValueFactory(new PropertyValueFactory<>("superficie"));
        numeroExteriorColumn.setCellValueFactory(new PropertyValueFactory<>("numeroExterior"));

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

        // 🔹 Columna de número de apartamentos - calculado automáticamente
        numApartamentosColumn.setCellValueFactory(data -> {
            Edificio edificio = data.getValue();
            long count = departamentoController.obtenerTodosLosDepartamentos().stream()
                    .filter(depto -> depto.getEdificio() != null && depto.getEdificio().getId() == edificio.getId())
                    .count();
            return new SimpleIntegerProperty((int) count).asObject();
        });

        // 🔹 Columna de número de habitantes - suma de habitantes de todos los departamentos
        numeroHabitantesColumn.setCellValueFactory(data -> {
            Edificio edificio = data.getValue();
            int totalHabitantes = departamentoController.obtenerTodosLosDepartamentos().stream()
                    .filter(depto -> depto.getEdificio() != null && depto.getEdificio().getId() == edificio.getId())
                    .mapToInt(depto -> depto.getHabitantes() != null ? depto.getHabitantes().size() : 0)
                    .sum();
            return new SimpleIntegerProperty(totalHabitantes).asObject();
        });

        // 🔹 Inicializar ComboBox de filtrado
        filtroComboBox.setItems(FXCollections.observableArrayList(
            "ID",
            "Nombre",
            "Dirección",
            "Superficie",
            "Número Exterior",
            "Colonia",
            "Propietario",
            "Nº Apartamentos",
            "Nº Habitantes"
        ));

        // 🔹 Listener para filtrar
        filtroTextField.textProperty().addListener((observable, oldValue, newValue) -> filtrarTabla());
        filtroComboBox.valueProperty().addListener((observable, oldValue, newValue) -> filtrarTabla());

        // Cargar datos
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

    private void filtrarTabla() {
        String filtroAtributo = filtroComboBox.getValue();
        String filtroTexto = filtroTextField.getText();

        if (filtroAtributo == null || filtroTexto == null || filtroTexto.trim().isEmpty()) {
            edificioTable.setItems(todosLosEdificios);
            return;
        }

        ObservableList<Edificio> edificiosFiltrados = FXCollections.observableArrayList();
        String textoMinusculas = filtroTexto.toLowerCase().trim();

        for (Edificio edificio : todosLosEdificios) {
            boolean coincide = false;

            switch (filtroAtributo) {
                case "ID":
                    coincide = String.valueOf(edificio.getId()).contains(textoMinusculas);
                    break;
                case "Nombre":
                    coincide = edificio.getNombre() != null &&
                              edificio.getNombre().toLowerCase().contains(textoMinusculas);
                    break;
                case "Dirección":
                    coincide = edificio.getDireccion() != null &&
                              edificio.getDireccion().toLowerCase().contains(textoMinusculas);
                    break;
                case "Superficie":
                    coincide = String.valueOf(edificio.getSuperficie()).contains(textoMinusculas);
                    break;
                case "Número Exterior":
                    coincide = edificio.getNumeroExterior() != null &&
                              edificio.getNumeroExterior().toLowerCase().contains(textoMinusculas);
                    break;
                case "Colonia":
                    Colonia c = edificio.getColonia();
                    coincide = c != null && c.getNombre() != null &&
                              c.getNombre().toLowerCase().contains(textoMinusculas);
                    break;
                case "Propietario":
                    Persona p = edificio.getPropietario();
                    if (p != null) {
                        String nombreCompleto = p.getNombre() != null ? p.getNombre() : "";
                        if (p.getFamilia() != null && p.getFamilia().getApellidos() != null) {
                            nombreCompleto += " " + p.getFamilia().getApellidos();
                        }
                        coincide = nombreCompleto.toLowerCase().contains(textoMinusculas);
                    }
                    break;
                case "Nº Apartamentos":
                    long count = departamentoController.obtenerTodosLosDepartamentos().stream()
                            .filter(depto -> depto.getEdificio() != null && depto.getEdificio().getId() == edificio.getId())
                            .count();
                    coincide = String.valueOf(count).contains(textoMinusculas);
                    break;
                case "Nº Habitantes":
                    int totalHabitantes = departamentoController.obtenerTodosLosDepartamentos().stream()
                            .filter(depto -> depto.getEdificio() != null && depto.getEdificio().getId() == edificio.getId())
                            .mapToInt(depto -> depto.getHabitantes() != null ? depto.getHabitantes().size() : 0)
                            .sum();
                    coincide = String.valueOf(totalHabitantes).contains(textoMinusculas);
                    break;
            }

            if (coincide) {
                edificiosFiltrados.add(edificio);
            }
        }

        edificioTable.setItems(edificiosFiltrados);
    }

    private void loadEdificios() {
        todosLosEdificios.setAll(edificioController.obtenerTodosLosEdificios());
        edificioTable.setItems(todosLosEdificios);
        edificioTable.refresh();
    }

    @FXML
    private void handleCreate() {
        if (!validarCampos()) return;

        // Validar número exterior duplicado en la misma dirección
        String direccionIngresada = direccionField.getText().trim();
        String numeroExteriorIngresado = numeroExteriorField.getText().trim();

        boolean numeroDuplicado = edificioController.obtenerTodosLosEdificios().stream()
                .anyMatch(e -> e.getDireccion() != null && e.getDireccion().trim().equalsIgnoreCase(direccionIngresada) &&
                              e.getNumeroExterior() != null && e.getNumeroExterior().equals(numeroExteriorIngresado));

        if (numeroDuplicado) {
            mostrarAlerta("Error al crear edificio", "Número Exterior duplicado",
                    "Ya existe una vivienda con el número exterior " + numeroExteriorIngresado + " en la dirección " + direccionIngresada + ".");
            return;
        }

        Edificio edificio = new Edificio();
        edificio.setNombre(nombreField.getText());
        edificio.setDireccion(direccionField.getText());
        edificio.setSuperficie(Double.parseDouble(superficieField.getText()));
        edificio.setNumeroExterior(numeroExteriorField.getText());
        edificio.setColonia(comboColonia.getValue());
        edificio.setPropietario(comboPropietario.getValue());

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

        String direccionIngresada = direccionField.getText().trim();
        String numeroExteriorIngresado = numeroExteriorField.getText().trim();
        String direccionOriginal = selectedEdificio.getDireccion() != null ? selectedEdificio.getDireccion().trim() : "";
        String numeroExteriorOriginal = selectedEdificio.getNumeroExterior() != null ? selectedEdificio.getNumeroExterior() : "";

        if (!direccionOriginal.equalsIgnoreCase(direccionIngresada) || !numeroExteriorOriginal.equals(numeroExteriorIngresado)) {
            boolean numeroDuplicado = edificioController.obtenerTodosLosEdificios().stream()
                    .anyMatch(e -> e.getDireccion() != null && e.getDireccion().trim().equalsIgnoreCase(direccionIngresada) &&
                                   e.getNumeroExterior() != null && e.getNumeroExterior().equals(numeroExteriorIngresado) &&
                                   e.getId() != selectedEdificio.getId());
            if (numeroDuplicado) {
                mostrarAlerta("Error al actualizar edificio", "Número Exterior duplicado",
                        "Ya existe una vivienda con el número exterior " + numeroExteriorIngresado + " en la dirección " + direccionIngresada + ".");
                return;
            }
        }

        selectedEdificio.setNombre(nombreField.getText());
        selectedEdificio.setDireccion(direccionField.getText());
        selectedEdificio.setSuperficie(Double.parseDouble(superficieField.getText()));
        selectedEdificio.setNumeroExterior(numeroExteriorField.getText());
        selectedEdificio.setColonia(comboColonia.getValue());
        selectedEdificio.setPropietario(comboPropietario.getValue());

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

    @FXML
    private void handleListar() {
        loadEdificios();
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
        numeroExteriorField.setText(selectedEdificio.getNumeroExterior());
        comboColonia.setValue(selectedEdificio.getColonia());
        comboPropietario.setValue(selectedEdificio.getPropietario());
    }

    // Limpiar campos y restablecer botones (misma dinámica que Persona)
    private void limpiarCampos() {
        nombreField.clear();
        direccionField.clear();
        superficieField.clear();
        numeroExteriorField.clear();
        comboColonia.setValue(null);
        comboPropietario.setValue(null);
        setButtonsState(true, false, false);
    }

    private void setButtonsState(boolean crear, boolean actualizar, boolean eliminar) {
        crearButton.setDisable(!crear);
        actualizarButton.setDisable(!actualizar);
        eliminarButton.setDisable(!eliminar);
    }

    private boolean validarCampos() {
        if (nombreField.getText().isEmpty() || direccionField.getText().isEmpty() || superficieField.getText().isEmpty() ||
                numeroExteriorField.getText().isEmpty()) {
            mostrarAlerta("Campos incompletos", "Error de validación", "Por favor complete todos los campos obligatorios.");
            return false;
        }
        try {
            Double.parseDouble(superficieField.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "Campos numéricos inválidos", "La superficie debe ser un valor numérico.");
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

    public static EdificioViewController getInstance() {
        return instance;
    }

    public void refreshTable() {
        loadEdificios();
    }
}
