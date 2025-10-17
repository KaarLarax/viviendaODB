package com.example.vivienda.view;

import com.example.vivienda.controller.ColoniaController;
import com.example.vivienda.model.Colonia;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ColoniaViewController {

    private static ColoniaViewController instance;

    @FXML
    private TextField nombreField;
    @FXML
    private TextField codigoPostalField;
    @FXML
    private TableView<Colonia> coloniaTable;
    @FXML
    private TableColumn<Colonia, Long> idColumn;
    @FXML
    private TableColumn<Colonia, String> nombreColumn;
    @FXML
    private TableColumn<Colonia, String> codigoPostalColumn;
    @FXML
    private TableColumn<Colonia, Integer> numViviendasColumn;

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

    private final ColoniaController coloniaController = new ColoniaController();
    private ObservableList<Colonia> todasLasColonias = FXCollections.observableArrayList();

    public static ColoniaViewController getInstance() {
        return instance;
    }

    @FXML
    public void initialize() {
        instance = this;

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        codigoPostalColumn.setCellValueFactory(new PropertyValueFactory<>("codigoPostal"));
        numViviendasColumn.setCellValueFactory(new PropertyValueFactory<>("numeroViviendas"));

        // 🔹 Inicializar ComboBox con los atributos de la tabla
        filtroComboBox.setItems(FXCollections.observableArrayList(
            "ID",
            "Nombre",
            "Código Postal",
            "Nº Viviendas"
        ));

        // 🔹 Listener para filtrar cuando se escribe en el TextField
        filtroTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarTabla();
        });

        // 🔹 Listener para cuando se cambia el atributo en el ComboBox
        filtroComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            filtrarTabla();
        });

        // 🔹 Estado inicial de botones
        crearButton.setDisable(false);
        actualizarButton.setDisable(true);
        eliminarButton.setDisable(true);

        // 🔹 Cuando se selecciona una fila
        coloniaTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
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

    private void loadColonias() {
        todasLasColonias.setAll(coloniaController.obtenerTodasLasColonias());
        coloniaTable.setItems(todasLasColonias);
        coloniaTable.refresh();
    }

    private void filtrarTabla() {
        String filtroAtributo = filtroComboBox.getValue();
        String filtroTexto = filtroTextField.getText();

        if (filtroAtributo == null || filtroTexto == null || filtroTexto.trim().isEmpty()) {
            coloniaTable.setItems(todasLasColonias);
            return;
        }

        ObservableList<Colonia> coloniasFiltradas = FXCollections.observableArrayList();
        String textoMinusculas = filtroTexto.toLowerCase().trim();

        for (Colonia colonia : todasLasColonias) {
            boolean coincide = false;

            switch (filtroAtributo) {
                case "ID":
                    coincide = String.valueOf(colonia.getId()).contains(textoMinusculas);
                    break;
                case "Nombre":
                    coincide = colonia.getNombre() != null &&
                              colonia.getNombre().toLowerCase().contains(textoMinusculas);
                    break;
                case "Código Postal":
                    coincide = colonia.getCodigoPostal() != null &&
                              colonia.getCodigoPostal().toLowerCase().contains(textoMinusculas);
                    break;
                case "Nº Viviendas":
                    coincide = String.valueOf(colonia.getNumeroViviendas()).contains(textoMinusculas);
                    break;
            }

            if (coincide) {
                coloniasFiltradas.add(colonia);
            }
        }

        coloniaTable.setItems(coloniasFiltradas);
    }

    // Método público para refrescar la tabla desde otros controladores
    public void refreshTable() {
        loadColonias();
    }

    @FXML
    private void handleCreate() {
        String nombre = nombreField.getText().trim();
        String codigoPostal = codigoPostalField.getText().trim();

        if (nombre.isEmpty() || codigoPostal.isEmpty()) {
            System.out.println("Por favor, complete todos los campos");
            return;
        }

        Colonia colonia = new Colonia();
        colonia.setNombre(nombre);
        colonia.setCodigoPostal(codigoPostal);
        coloniaController.crearColonia(colonia);
        loadColonias();
        clearFields();

        // 🔹 Restaurar botones y limpiar selección
        crearButton.setDisable(false);
        actualizarButton.setDisable(true);
        eliminarButton.setDisable(true);
        coloniaTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleUpdate() {
        Colonia selectedColonia = coloniaTable.getSelectionModel().getSelectedItem();
        if (selectedColonia != null) {
            String nombre = nombreField.getText().trim();
            String codigoPostal = codigoPostalField.getText().trim();

            if (nombre.isEmpty() || codigoPostal.isEmpty()) {
                System.out.println("Por favor, complete todos los campos");
                return;
            }

            selectedColonia.setNombre(nombre);
            selectedColonia.setCodigoPostal(codigoPostal);
            coloniaController.actualizarColonia(selectedColonia);
            loadColonias();
            clearFields();

            // 🔹 Restaurar botones y limpiar selección
            crearButton.setDisable(false);
            actualizarButton.setDisable(true);
            eliminarButton.setDisable(true);
            coloniaTable.getSelectionModel().clearSelection();
        } else {
            System.out.println("Por favor, seleccione una colonia de la tabla");
        }
    }

    @FXML
    private void handleDelete() {
        Colonia selectedColonia = coloniaTable.getSelectionModel().getSelectedItem();
        if (selectedColonia != null) {
            coloniaController.eliminarColoniaPorId(selectedColonia.getId());
            loadColonias();
            clearFields();

            // 🔹 Restaurar botones y limpiar selección
            crearButton.setDisable(false);
            actualizarButton.setDisable(true);
            eliminarButton.setDisable(true);
            coloniaTable.getSelectionModel().clearSelection();
        } else {
            System.out.println("Por favor, seleccione una colonia de la tabla");
        }
    }

    @FXML
    private void handleLimpiar() {
        clearFields();
        coloniaTable.getSelectionModel().clearSelection();

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
        loadColonias();
    }

    private void onTableSelection(Colonia selectedColonia) {
        nombreField.setText(selectedColonia.getNombre());
        codigoPostalField.setText(selectedColonia.getCodigoPostal());
    }

    private void clearFields() {
        nombreField.clear();
        codigoPostalField.clear();
    }
}
