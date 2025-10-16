package com.example.vivienda.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WelcomeViewController {

    @FXML
    private Button enterButton;

    @FXML
    private void initialize() {
        // Initialize any required logic here
        enterButton.setOnAction(event -> onEnterButtonClicked());
    }

    private void onEnterButtonClicked() {
        System.out.println("Entrar button clicked!");
        // Add logic to handle the button click event
    }
}
