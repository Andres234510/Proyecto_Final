package co.edu.uniquindio.proyecto_final.viewController;

import co.edu.uniquindio.proyecto_final.model.Usuario;
import co.edu.uniquindio.proyecto_final.service.UsuarioService;
import co.edu.uniquindio.proyecto_final.singleton.DataStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.Optional;

public class UserTab {

    public static Tab createTab(boolean isAdmin) {
        Tab tab = new Tab("Usuarios");
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        UsuarioService usuarioService = DataStore.getInstance().getUsuarioService();

        TableView<Usuario> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Usuario, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        TableColumn<Usuario, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<Usuario, String> colCorreo = new TableColumn<>("Correo");
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));

        table.getColumns().addAll(colId, colNombre, colCorreo);

        ObservableList<Usuario> items = FXCollections.observableArrayList(usuarioService.listarTodos());
        table.setItems(items);

        Button btnNuevo = new Button("Nuevo");
        Button btnEditar = new Button("Editar");
        Button btnEliminar = new Button("Eliminar");
        Button btnRefrescar = new Button("Refrescar");

        btnNuevo.setDisable(!isAdmin);
        btnEditar.setDisable(!isAdmin);
        btnEliminar.setDisable(!isAdmin);

        HBox controls = new HBox(10, btnNuevo, btnEditar, btnEliminar, btnRefrescar);
        controls.setPadding(new Insets(10));

        btnRefrescar.setOnAction(e -> items.setAll(usuarioService.listarTodos()));

        btnNuevo.setOnAction(e -> {
            UsuarioFormDialog dialog = new UsuarioFormDialog(null);
            Optional<Usuario> res = dialog.showAndWait();
            res.ifPresent(u -> {
                usuarioService.crearUsuario(u.getNombre(), u.getCorreo(), u.getTelefono(), u.getPassword());
                items.setAll(usuarioService.listarTodos());
            });
        });

        btnEditar.setOnAction(e -> {
            Usuario sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { Alerts.showWarning("Selecciona un usuario"); return; }
            UsuarioFormDialog dialog = new UsuarioFormDialog(sel);
            dialog.showAndWait().ifPresent(u -> {
                sel.setNombre(u.getNombre());
                sel.setCorreo(u.getCorreo());
                sel.setTelefono(u.getTelefono());
                usuarioService.actualizarUsuario(sel);
                items.setAll(usuarioService.listarTodos());
            });
        });

        btnEliminar.setOnAction(e -> {
            Usuario sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { Alerts.showWarning("Selecciona un usuario"); return; }
            boolean ok = Alerts.confirm("Eliminar usuario", "¿Eliminar usuario " +
                    sel.getNombre() + "?");
            if (ok) {
                usuarioService.eliminarUsuario(sel.getIdUsuario());
                items.setAll(usuarioService.listarTodos());
            }
        });

        root.setCenter(table);
        root.setBottom(controls);
        tab.setContent(root);
        return tab;
    }
}
