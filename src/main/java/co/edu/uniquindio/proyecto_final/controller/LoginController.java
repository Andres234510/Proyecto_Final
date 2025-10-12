package co.edu.uniquindio.proyecto_final.controller;

import co.edu.uniquindio.proyecto_final.model.Usuario;
import co.edu.uniquindio.proyecto_final.service.UsuarioService;
import co.edu.uniquindio.proyecto_final.singleton.DataStore;
import co.edu.uniquindio.proyecto_final.viewController.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Optional;

public class LoginController {

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtContrasena;

    @FXML
    private TextField txtNombreRegistro;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnRegistrar;

    @FXML
    private Label lblMensaje;

    private final UsuarioService usuarioService = DataStore.getInstance().getUsuarioService();

    @FXML
    public void initialize() {
        btnLogin.setOnAction(e -> doLogin());
        btnRegistrar.setOnAction(e -> doRegistro());
    }

    private void doLogin() {
        String correo = txtCorreo.getText().trim();
        String pass = txtContrasena.getText().trim();
        if (correo.isEmpty() || pass.isEmpty()) {
            Alerts.showWarning("Completa correo y contraseña");
            return;
        }

        Optional<Usuario> opt = usuarioService.login(correo, pass);
        opt.ifPresentOrElse(user -> {
            lblMensaje.setText("Bienvenido " + user.getNombre());
        }, () -> {
            lblMensaje.setText("Credenciales incorrectas");
            Alerts.showWarning("Correo o contraseña incorrectos");
        });
    }

    private void doRegistro() {
        String nombre = txtNombreRegistro != null ? txtNombreRegistro.getText().trim() : null;
        String correo = txtCorreo.getText().trim();
        String pass = txtContrasena.getText().trim();

        if (nombre == null || nombre.isBlank()) Alerts.showWarning("Ingresa tu nombre");
        if (correo.isEmpty() || pass.isEmpty()) {
            Alerts.showWarning("Ingresa correo y contraseña");
            return;
        }

        try {
            Usuario nuevo = usuarioService.registrar(nombre, correo, pass);
            Alerts.showInfo("Registro", "Usuario registrado: " + nuevo.getNombre());
            lblMensaje.setText("Usuario registrado: " + nuevo.getNombre());
        } catch (IllegalArgumentException ex) {
            Alerts.showWarning("No se pudo registrar: " + ex.getMessage());
        } catch (Exception ex) {
            Alerts.showWarning("Error al registrar usuario");
            ex.printStackTrace();
        }
    }
}
