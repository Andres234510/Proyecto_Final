package co.edu.uniquindio.proyecto_final.controller;

import co.edu.uniquindio.proyecto_final.model.Usuario;
import co.edu.uniquindio.proyecto_final.singleton.ServiceLocator;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class LoginController {

    @FXML private TextField correoField;
    @FXML private PasswordField passwordField;
    @FXML private TextField nombreField;
    @FXML private Button btnLogin;
    @FXML private Button btnRegister;
    @FXML private Label lblMessage;

    private final ServiceLocator sl = ServiceLocator.getInstance();

    @FXML
    private void initialize() {
        if (lblMessage != null) lblMessage.setText("");
    }

    // Helper: intenta localizar un FXML probando varias rutas plausibles
    private URL findFxml(String baseName) {
        // candidate paths (ajusta el package si tu proyecto usa otro)
        String[] candidates = new String[] {
                "/co/edu/uniquindio/proyecto_final/view/" + baseName,
                "co/edu/uniquindio/proyecto_final/view/" + baseName,
                "/view/" + baseName,
                "view/" + baseName,
                "/" + baseName,
                baseName
        };

        for (String c : candidates) {
            URL url = LoginController.class.getResource(c);
            if (url == null) url = Thread.currentThread().getContextClassLoader().getResource(c.startsWith("/") ? c.substring(1) : c);
            if (url != null) {
                System.out.println("[INFO] FXML encontrado: " + c + " -> " + url);
                return url;
            }
        }
        // diagnóstico si no se encontró
        System.err.println("[ERROR] No se encontró FXML para: " + baseName);
        System.err.println("Classpath: " + System.getProperty("java.class.path"));
        return null;
    }

    @FXML
    public void onLogin(ActionEvent e) {
        String correo = correoField.getText();
        String pass = passwordField.getText();

        sl.usuarioService.login(correo, pass).ifPresentOrElse(user -> {
            try {
                // Elegimos el fxml según rol
                String fxmlName = user.isAdmin() ? "admin_dashboard.fxml" : "user_dashboard.fxml";
                URL fxmlUrl = findFxml(fxmlName);
                if (fxmlUrl == null) {
                    lblMessage.setText("Error: no se encontró la vista " + fxmlName);
                    return;
                }

                FXMLLoader loader = new FXMLLoader(fxmlUrl);
                Stage stage = (Stage) btnLogin.getScene().getWindow();
                Scene scene = new Scene(loader.load());

                // Intentar pasar datos al controller si existe método setUsuario
                Object ctrl = loader.getController();
                try {
                    if (!user.isAdmin() && ctrl != null) {
                        // reflection ligera para evitar dependencia estricta
                        var method = ctrl.getClass().getMethod("setUsuario", co.edu.uniquindio.proyecto_final.model.Usuario.class);
                        if (method != null) method.invoke(ctrl, user);
                    }
                } catch (NoSuchMethodException ex) {
                    // no pasa nada, el controller no necesita usuario
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                // añadir css si existe (no es crítico)
                URL cssUrl = LoginController.class.getResource("/css/style.css");
                if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());

                stage.setScene(scene);

            } catch (IOException ex) {
                ex.printStackTrace();
                lblMessage.setText("Error al cargar la vista: " + ex.getMessage());
            }
        }, () -> lblMessage.setText("Credenciales inválidas"));
    }

    @FXML
    public void onRegister(ActionEvent e) {
        String nombre = nombreField.getText();
        String correo = correoField.getText();
        String pass = passwordField.getText();
        if (nombre == null || nombre.isBlank() || correo == null || correo.isBlank() || pass == null || pass.isBlank()) {
            lblMessage.setText("Rellena todos los campos");
            return;
        }
        try {
            Usuario u = sl.usuarioService.registrar(nombre, correo, pass);
            lblMessage.setText("Registrado: " + u.getNombre());
        } catch (Exception ex) {
            ex.printStackTrace();
            lblMessage.setText("Error al registrar: " + ex.getMessage());
        }
    }
}
