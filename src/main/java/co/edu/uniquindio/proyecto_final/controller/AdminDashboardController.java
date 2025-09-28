package co.edu.uniquindio.proyecto_final.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminDashboardController {

    @FXML private Button btnVerEnvios;
    @FXML private Button btnReportes;

    @FXML
    public void onVerEnvios(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/envios_list.fxml"));
        Stage stage = (Stage) btnVerEnvios.getScene().getWindow();
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        stage.setScene(scene);
    }

    @FXML
    public void onReportes(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user_dashboard.fxml"));
        Stage stage = (Stage) btnReportes.getScene().getWindow();
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        stage.setScene(scene);
    }
}
