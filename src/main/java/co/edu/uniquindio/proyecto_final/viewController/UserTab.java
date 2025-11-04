package co.edu.uniquindio.proyecto_final.viewController;

import co.edu.uniquindio.proyecto_final.model.Usuario;
import co.edu.uniquindio.proyecto_final.service.UsuarioService;
import co.edu.uniquindio.proyecto_final.singleton.DataStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.application.Platform;

import java.util.Optional;

/**
 * UserTab (mejorado)
 *
 * - Mantiene la firma pública: public static Tab createTab(boolean isAdmin)
 * - Mantiene las llamadas existentes a UsuarioService (listarTodos, crearUsuario, actualizarUsuario, eliminarUsuario)
 * - Añade un formulario embebido (lado derecho) para crear/editar sin abrir Dialogs.
 * - Añade botón "Guardar" (no existente antes) pero NO renombra ni elimina btnNuevo/btnEditar/btnEliminar/btnRefrescar.
 * - Usa lambdas para cell factories (evita problemas de reflexión en proyectos modulares).
 *
 * Nota: Si tu proyecto depende todavía de UsuarioFormDialog en otras partes, no se touch; aquí evitamos su uso.
 */
public class UserTab {

    public static Tab createTab(boolean isAdmin) {
        Tab tab = new Tab("Usuarios");
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Servicio (idéntico al código original)
        UsuarioService usuarioService = DataStore.getInstance().getUsuarioService();

        // Tabla: se usa lambda cell factories para mayor robustez (en vez de PropertyValueFactory directo).
        TableView<Usuario> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Usuario, String> colId = new TableColumn<>("ID");
        // usamos lambda para obtener el id (compatibilidad con módulos/reflection)
        colId.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getIdUsuario()));
        colId.setPrefWidth(180);

        TableColumn<Usuario, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getNombre()));
        colNombre.setPrefWidth(240);

        TableColumn<Usuario, String> colCorreo = new TableColumn<>("Correo");
        colCorreo.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getCorreo()));
        colCorreo.setPrefWidth(220);

        table.getColumns().addAll(colId, colNombre, colCorreo);

        // Items observables
        ObservableList<Usuario> items = FXCollections.observableArrayList(usuarioService.listarTodos());
        table.setItems(items);

        // ----------------------
        // Formulario embebido (lado derecho)
        // ----------------------
        GridPane form = new GridPane();
        form.setHgap(8);
        form.setVgap(8);
        form.setPadding(new Insets(8));
        form.setStyle("-fx-background-color: #fbfbff; -fx-border-color: #e0e0e0; -fx-border-radius: 6; -fx-background-radius: 6;");

        Label lblName = new Label("Nombre:");
        TextField txtName = new TextField();
        txtName.setPromptText("Nombre completo");

        Label lblCorreo = new Label("Correo:");
        TextField txtCorreo = new TextField();
        txtCorreo.setPromptText("correo@ejemplo.com");

        Label lblTelefono = new Label("Teléfono:");
        TextField txtTelefono = new TextField();
        txtTelefono.setPromptText("3101234567");

        Label lblPassword = new Label("Contraseña:");
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("********");

        // status label (mensajes de validación / éxito)
        Label lblStatus = new Label();
        lblStatus.setPadding(new Insets(6, 0, 0, 0));

        // add to grid
        form.add(lblName, 0, 0);     form.add(txtName, 1, 0);
        form.add(lblCorreo, 0, 1);   form.add(txtCorreo, 1, 1);
        form.add(lblTelefono, 0, 2); form.add(txtTelefono, 1, 2);
        form.add(lblPassword, 0, 3); form.add(txtPassword, 1, 3);
        form.add(lblStatus, 0, 4, 2, 1);

        // ----------------------
        // Botones (mantengo nombres originales y añado btnGuardar)
        // ----------------------
        Button btnNuevo = new Button("Nuevo");
        Button btnEditar = new Button("Editar");
        Button btnEliminar = new Button("Eliminar");
        Button btnRefrescar = new Button("Refrescar");
        Button btnGuardar = new Button("Guardar"); // nuevo botón para confirmar create/update

        // Conservamos la política original del admin: si no es admin, deshabilitar las acciones de mutación.
        btnNuevo.setDisable(!isAdmin);
        btnEditar.setDisable(!isAdmin);
        btnEliminar.setDisable(!isAdmin);
        btnGuardar.setDisable(!isAdmin);

        HBox controls = new HBox(10, btnNuevo, btnEditar, btnGuardar, btnEliminar, btnRefrescar);
        controls.setPadding(new Insets(10));

        // ----------------------
        // Integración layout: tabla a la izquierda, form a la derecha
        // ----------------------
        HBox center = new HBox(12);
        center.getChildren().addAll(table, form);
        HBox.setHgrow(table, Priority.ALWAYS);
        form.setPrefWidth(360);

        root.setCenter(center);
        root.setBottom(controls);

        // ----------------------
        // Lógica: selección -> cargar en formulario
        // ----------------------
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                // cargar datos en el formulario (editar local)
                txtName.setText(newSel.getNombre());
                txtCorreo.setText(newSel.getCorreo());
                txtTelefono.setText(newSel.getTelefono());
                // la contraseña no se carga por seguridad; dejar vacía para cambiar si se desea
                txtPassword.setText("");
                lblStatus.setText("Edición: " + newSel.getNombre());
            } else {
                // limpiar si no hay selección
                clearFormFields(txtName, txtCorreo, txtTelefono, txtPassword, lblStatus);
            }
        });

        // ----------------------
        // Funciones auxiliares (lambda)
        // ----------------------
        Runnable reloadItems = () -> {
            // recargar desde el servicio (lista canónica)
            Platform.runLater(() -> items.setAll(usuarioService.listarTodos()));
        };

        // ----------------------
        // Botones comportamiento
        // ----------------------
        btnRefrescar.setOnAction(e -> reloadItems.run());

        // Nuevo -> limpiar formulario y quitar selección para crear
        btnNuevo.setOnAction(e -> {
            table.getSelectionModel().clearSelection();
            clearFormFields(txtName, txtCorreo, txtTelefono, txtPassword, lblStatus);
            lblStatus.setText("Ingrese datos y pulse Guardar para crear.");
        });

        // Editar -> cargar selección en formulario (si hay)
        btnEditar.setOnAction(e -> {
            Usuario sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                // preferimos usar Alerts existing helper (ya usado en el código original)
                Alerts.showWarning("Selecciona un usuario");
                return;
            }
            txtName.setText(sel.getNombre());
            txtCorreo.setText(sel.getCorreo());
            txtTelefono.setText(sel.getTelefono());
            txtPassword.setText("");
            lblStatus.setText("Edite los datos y pulse Guardar para actualizar.");
        });

        // Guardar -> crear o actualizar según si hay selección
        btnGuardar.setOnAction(e -> {
            try {
                // validations (mínimas)
                String nombre = txtName.getText().trim();
                String correo = txtCorreo.getText().trim();
                String telefono = txtTelefono.getText().trim();
                String password = txtPassword.getText();

                if (nombre.isEmpty() || correo.isEmpty()) {
                    Alerts.showWarning("Nombre y correo son obligatorios.");
                    // se puede marcar visualmente campos: ejemplo:
                    markIfEmpty(txtName);
                    markIfEmpty(txtCorreo);
                    return;
                }

                Usuario sel = table.getSelectionModel().getSelectedItem();
                if (sel == null) {
                    // CREATE: uso EXACTAMENTE la firma que ya empleabas
                    usuarioService.crearUsuario(nombre, correo, telefono, password == null ? "" : password);
                    lblStatus.setText("Usuario creado correctamente.");
                    reloadItems.run();
                    clearFormFields(txtName, txtCorreo, txtTelefono, txtPassword, lblStatus);
                } else {
                    // UPDATE: modifico el objeto y llamo al servicio existente
                    sel.setNombre(nombre);
                    sel.setCorreo(correo);
                    sel.setTelefono(telefono);
                    if (password != null && !password.isEmpty()) sel.setPassword(password);
                    usuarioService.actualizarUsuario(sel);
                    lblStatus.setText("Usuario actualizado correctamente.");
                    reloadItems.run();
                    // mantener selección en el registro actualizado (buscar por id)
                    Platform.runLater(() -> {
                        for (Usuario u : items) {
                            if (u.getIdUsuario().equals(sel.getIdUsuario())) {
                                table.getSelectionModel().select(u);
                                break;
                            }
                        }
                    });
                }
            } catch (Exception ex) {
                // no alteramos los nombres de helpers; informamos con Alerts y el estado
                Alerts.showWarning("Error al guardar: " + ex.getMessage());
                lblStatus.setText("Error: " + ex.getMessage());
            }
        });

        // Eliminar -> confirmar y borrar via servicio (misma firma que antes)
        btnEliminar.setOnAction(e -> {
            Usuario sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                Alerts.showWarning("Selecciona un usuario");
                return;
            }
            boolean ok = Alerts.confirm("Eliminar usuario", "¿Eliminar usuario " + sel.getNombre() + "?");
            if (ok) {
                usuarioService.eliminarUsuario(sel.getIdUsuario());
                lblStatus.setText("Usuario eliminado.");
                reloadItems.run();
                clearFormFields(txtName, txtCorreo, txtTelefono, txtPassword, lblStatus);
            }
        });

        // final: asignar contenido y retornar
        tab.setContent(root);
        return tab;
    }

    // ---------------------
    // Helpers privados - no cambian APIs externas
    // ---------------------
    private static void clearFormFields(TextField nombre, TextField correo, TextField telefono, PasswordField password, Label status) {
        nombre.setText("");
        correo.setText("");
        telefono.setText("");
        password.setText("");
        if (status != null) status.setText("");
        // remover estilos de error si se añadieron
        nombre.getStyleClass().remove("error-field");
        correo.getStyleClass().remove("error-field");
        telefono.getStyleClass().remove("error-field");
    }

    private static void markIfEmpty(TextField t) {
        if (t.getText() == null || t.getText().trim().isEmpty()) {
            if (!t.getStyleClass().contains("error-field")) t.getStyleClass().add("error-field");
        } else {
            t.getStyleClass().remove("error-field");
        }
    }
}
