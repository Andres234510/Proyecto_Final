package co.edu.uniquindio.proyecto_final.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ReportesController {

    @FXML private Button btnBack;

    @FXML public void onBack() {
        try {
            btnBack.getScene().setRoot(javafx.fxml.FXMLLoader.load(getClass().getResource("/view/admin_dashboard.fxml")));
        } catch (Exception e) { e.printStackTrace(); }
    }
}
