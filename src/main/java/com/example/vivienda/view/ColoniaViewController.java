package com.example.vivienda.view;

import com.example.vivienda.controller.ColoniaController;
import com.example.vivienda.model.Colonia;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;

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

    private final ColoniaController coloniaController = new ColoniaController();

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
        loadColonias();

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
        coloniaTable.getItems().setAll(coloniaController.obtenerTodasLasColonias());
        coloniaTable.refresh();
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

        // Restaurar botones
        crearButton.setDisable(false);
        actualizarButton.setDisable(true);
        eliminarButton.setDisable(true);
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
