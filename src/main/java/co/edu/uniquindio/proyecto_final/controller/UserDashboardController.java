package co.edu.uniquindio.proyecto_final.controller;

import co.edu.uniquindio.proyecto_final.model.Usuario;
import co.edu.uniquindio.proyecto_final.singleton.ServiceLocator;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class UserDashboardController {

    @FXML private Button btnSolicitar;
    @FXML private Button btnMisEnvios;
    @FXML private Button btnExport;

    private final ServiceLocator sl = ServiceLocator.getInstance();
    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    private URL findFxml(String baseName) {
        String[] candidates = new String[] {
                "/co/edu/uniquindio/proyecto_final/view/" + baseName,
                "co/edu/uniquindio/proyecto_final/view/" + baseName,
                "/view/" + baseName,
                "view/" + baseName,
                "/" + baseName,
                baseName
        };

        for (String c : candidates) {
            URL url = UserDashboardController.class.getResource(c);
            if (url == null) url = Thread.currentThread().getContextClassLoader().getResource(c.startsWith("/") ? c.substring(1) : c);
            if (url != null) {
                System.out.println("[INFO] FXML encontrado: " + c + " -> " + url);
                return url;
            }
        }
        System.err.println("[ERROR] No se encontró FXML: " + baseName);
        System.err.println("Classpath: " + System.getProperty("java.class.path"));
        return null;
    }

    @FXML
    public void onSolicitar(ActionEvent e) {
        try {
            String name = "solicitar_envio.fxml";
            URL fxmlUrl = findFxml(name);
            if (fxmlUrl == null) {
                showError("Vista no encontrada: " + name);
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Stage stage = (Stage) btnSolicitar.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            Object ctrl = loader.getController();
            if (ctrl != null) {
                try {
                    var method = ctrl.getClass().getMethod("setUsuario", Usuario.class);
                    if (method != null) method.invoke(ctrl, usuario);
                } catch (NoSuchMethodException ex) {
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            URL cssUrl = UserDashboardController.class.getResource("/css/style.css");
            if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());

            stage.setScene(scene);
        } catch (IOException ex) {
            ex.printStackTrace();
            showError("Error cargando la vista: " + ex.getMessage());
        }
    }

    @FXML
    public void onMisEnvios(ActionEvent e) {
        try {
            String name = "envios_list.fxml";
            URL fxmlUrl = findFxml(name);
            if (fxmlUrl == null) {
                showError("Vista no encontrada: " + name);
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Stage stage = (Stage) btnMisEnvios.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            Object ctrl = loader.getController();
            if (ctrl != null) {
                try {
                    var method = ctrl.getClass().getMethod("setUsuario", Usuario.class);
                    if (method != null) method.invoke(ctrl, usuario);
                } catch (NoSuchMethodException ex) {
                } catch (Exception ex) { ex.printStackTrace(); }
            }
            URL cssUrl = UserDashboardController.class.getResource("/css/style.css");
            if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());
            stage.setScene(scene);
        } catch (IOException ex) {
            ex.printStackTrace();
            showError("Error cargando envíos: " + ex.getMessage());
        }
    }

    private void showError(String msg) {
        try {
            javafx.scene.control.Alert a = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, msg, javafx.scene.control.ButtonType.OK);
            a.showAndWait();
        } catch (Exception ex) {
            System.err.println(msg);
        }
    }
}
