package co.edu.uniquindio.proyecto_final;

import co.edu.uniquindio.proyecto_final.model.Usuario;
import co.edu.uniquindio.proyecto_final.service.AuthService;
import co.edu.uniquindio.proyecto_final.singleton.DataStore;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class LoginView {

    private final AuthService authService = DataStore.getInstance().getAuthService();

    public Optional<Usuario> showAndWait() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Iniciar sesión");

        VBox root = new VBox(12);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Bienvenido");
        title.getStyleClass().add("login-title");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Correo");

        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Contraseña");

        Label lblError = new Label();
        lblError.getStyleClass().add("login-error");

        Button btnLogin = new Button("Ingresar");
        btnLogin.getStyleClass().add("primary-btn");
        Button btnCancelar = new Button("Cancelar");
        HBox buttons = new HBox(10, btnLogin, btnCancelar);
        buttons.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, txtEmail, txtPass, lblError, buttons);

        Scene scene = new Scene(root, 380, 260);

        String cssPath = "/co/edu/uniquindio/proyecto_final/css/style.css";

        java.net.URL cssUrl = getClass().getResource(cssPath);

        if (cssUrl == null) cssUrl = getClass().getClassLoader().getResource("co/edu/uniquindio/proyecto_final/css/style.css");

        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {

            System.err.println("WARNING: styles.css not found at " + cssPath + ". Comprueba src/main/resources y la ruta.");
        }

        dialog.setScene(scene);


        final Optional<Usuario>[] result = new Optional[]{Optional.empty()};

        btnLogin.setOnAction(e -> {
            String correo = txtEmail.getText().trim();
            String pass = txtPass.getText().trim();
            if (correo.isEmpty() || pass.isEmpty()) {
                lblError.setText("Completa correo y contraseña");
                return;
            }
            Optional<Usuario> opt = authService.login(correo, pass);
            if (opt.isPresent()) {
                DataStore.getInstance().setCurrentUser(opt.get());
                result[0] = opt;
                dialog.close();
            } else {
                lblError.setText("Credenciales inválidas");
            }
        });

        btnCancelar.setOnAction(e -> {
            result[0] = Optional.empty();
            dialog.close();
        });

        dialog.showAndWait();
        return result[0];
    }
}
