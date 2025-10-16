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

public class CasaUnifamiliarViewController {
    @FXML private TextField txtDireccion;
    @FXML private TextField txtSuperficie;
    @FXML private TextField txtClaveCatastral;
    @FXML private TextField txtNumeroPisos;
    @FXML private ComboBox<Colonia> comboColonia;
    @FXML private ComboBox<Persona> comboPropietario;
    @FXML private TableView<CasaUnifamiliar> casaUnifamiliarTable;
    @FXML private TableColumn<CasaUnifamiliar, String> superficieColumn;
    @FXML private TableColumn<CasaUnifamiliar, String> claveCatastralColumn;
    @FXML private TableColumn<CasaUnifamiliar, Integer> numeroPisosColumn;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnVolver;

    private final CasaUnifamiliarController casaDAO = new CasaUnifamiliarController();
    private final ColoniaController coloniaDAO = new ColoniaController();
    private final PersonaController personaDAO = new PersonaController();
    private ObservableList<CasaUnifamiliar> casasList;
    private ObservableList<Colonia> coloniasList;
    private ObservableList<Persona> personasList;

    @FXML
    public void initialize() {
        // Configurar columnas
        superficieColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getSuperficie())));
        claveCatastralColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getClaveCatastral()));
        numeroPisosColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getNumeroPisos()));

        // Cargar datos
        casasList = FXCollections.observableArrayList(casaDAO.obtenerTodasLasCasas());
        casaUnifamiliarTable.setItems(casasList);

        coloniasList = FXCollections.observableArrayList(coloniaDAO.obtenerTodasLasColonias());
        comboColonia.setItems(coloniasList);

        personasList = FXCollections.observableArrayList(personaDAO.obtenerTodasLasPersonas());
        comboPropietario.setItems(personasList);

        // Selección en tabla
        casaUnifamiliarTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> mostrarCasa(newSel));
    }

    private void mostrarCasa(CasaUnifamiliar casa) {
        if (casa != null) {
            txtDireccion.setText(casa.getDireccion());
            txtSuperficie.setText(String.valueOf(casa.getSuperficie()));
            txtClaveCatastral.setText(casa.getClaveCatastral());
            txtNumeroPisos.setText(String.valueOf(casa.getNumeroPisos()));
            comboColonia.setValue(casa.getColonia());
            comboPropietario.setValue(casa.getPropietario());
        } else {
            txtDireccion.clear();
            txtSuperficie.clear();
            txtClaveCatastral.clear();
            txtNumeroPisos.clear();
            comboColonia.setValue(null);
            comboPropietario.setValue(null);
        }
    }

    @FXML
    private void onAgregar() {
        try {
            String direccion = txtDireccion.getText();
            double superficie = Double.parseDouble(txtSuperficie.getText());
            String clave = txtClaveCatastral.getText();
            int pisos = Integer.parseInt(txtNumeroPisos.getText());
            Colonia colonia = comboColonia.getValue();
            Persona propietario = comboPropietario.getValue();
            CasaUnifamiliar casa = new CasaUnifamiliar(direccion, superficie, clave, propietario, colonia, pisos);
            casaDAO.crearCasa(casa);
            casasList.add(casa);
            limpiarCampos();
        } catch (Exception e) {
            mostrarAlerta("Error al agregar", "Verifica los datos ingresados.");
        }
    }

    @FXML
    private void onEditar() {
        CasaUnifamiliar seleccionada = casaUnifamiliarTable.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            try {
                seleccionada.setDireccion(txtDireccion.getText());
                seleccionada.setSuperficie(Double.parseDouble(txtSuperficie.getText()));
                seleccionada.setClaveCatastral(txtClaveCatastral.getText());
                seleccionada.setNumeroPisos(Integer.parseInt(txtNumeroPisos.getText()));
                seleccionada.setColonia(comboColonia.getValue());
                seleccionada.setPropietario(comboPropietario.getValue());
                casaDAO.actualizarCasa(seleccionada);
                casaUnifamiliarTable.refresh();
                limpiarCampos();
            } catch (Exception e) {
                mostrarAlerta("Error al editar", "Verifica los datos ingresados.");
            }
        }
    }

    @FXML
    private void onEliminar() {
        CasaUnifamiliar seleccionada = casaUnifamiliarTable.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            casaDAO.eliminarCasa(seleccionada);
            casasList.remove(seleccionada);
            limpiarCampos();
        }
    }

    @FXML
    private void onVolver() {
        // Implementa la lógica para volver al menú principal o cerrar la ventana
    }

    private void limpiarCampos() {
        mostrarCasa(null);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
