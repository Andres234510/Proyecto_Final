package co.edu.uniquindio.proyecto_final.controller;

import co.edu.uniquindio.proyecto_final.model.Usuario;
import co.edu.uniquindio.proyecto_final.singleton.ServiceLocator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class UserDashboardController {

    @FXML private Button btnSolicitar;
    @FXML private Button btnMisEnvios;
    @FXML private Button btnExport;

    private final ServiceLocator sl = ServiceLocator.getInstance();
    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @FXML
    public void onSolicitar(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/solicitar_envio.fxml"));
        Stage stage = (Stage) btnSolicitar.getScene().getWindow();
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        SolicitarEnvioController ctrl = loader.getController();
        ctrl.setUsuario(usuario);
        stage.setScene(scene);
    }

    @FXML
    public void onMisEnvios(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/envios_list.fxml"));
        Stage stage = (Stage) btnMisEnvios.getScene().getWindow();
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        EnviosListController ctrl = loader.getController();
        ctrl.setUsuario(usuario);
        stage.setScene(scene);
    }
}
