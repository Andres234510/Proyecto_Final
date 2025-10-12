package co.edu.uniquindio.proyecto_final.viewController;

import co.edu.uniquindio.proyecto_final.model.Usuario;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class UsuarioFormDialog extends Dialog<Usuario> {

    private TextField txtNombre = new TextField();
    private TextField txtCorreo = new TextField();
    private TextField txtTelefono = new TextField();

    public UsuarioFormDialog(Usuario usuario) {
        setTitle(usuario == null ? "Nuevo Usuario" : "Editar Usuario");
        setHeaderText(null);

        ButtonType guardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(guardar, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setPadding(new Insets(10));

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("Correo:"), 0, 1);
        grid.add(txtCorreo, 1, 1);
        grid.add(new Label("Teléfono:"), 0, 2);
        grid.add(txtTelefono, 1, 2);

        if (usuario != null) {
            txtNombre.setText(usuario.getNombre());
            txtCorreo.setText(usuario.getCorreo());
            txtTelefono.setText(usuario.getTelefono());
        }

        getDialogPane().setContent(grid);

        setResultConverter(button -> {
            if (button == guardar) {
                Usuario u = usuario == null ? new Usuario(txtNombre.getText(), txtCorreo.getText(),
                        txtTelefono.getText())
                        : usuario;
                u.setNombre(txtNombre.getText());
                u.setCorreo(txtCorreo.getText());
                u.setTelefono(txtTelefono.getText());
                return u;
            }
            return null;
        });
    }
}
