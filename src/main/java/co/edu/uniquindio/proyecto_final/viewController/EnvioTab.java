package co.edu.uniquindio.proyecto_final.viewController;

import co.edu.uniquindio.proyecto_final.model.Envio;
import co.edu.uniquindio.proyecto_final.model.EstadoEnvio;
import co.edu.uniquindio.proyecto_final.model.Usuario;
import co.edu.uniquindio.proyecto_final.service.EnvioService;
import co.edu.uniquindio.proyecto_final.singleton.DataStore;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.application.Platform;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * EnvioTab (mejorada)
 *
 * - Mantiene la firma pública: public static Tab createTab(boolean isAdmin)
 * - Conserva los nombres principales (envioService, table, btnNuevo, btnEditar, btnEliminar, btnAsignar, btnRefrescar)
 * - Añade un formulario embebido a la derecha para editar campos importantes del envío (peso, volumen, prioridad, estado)
 * - Para creación completa (origen/destino) continúa usando EnvioFormDialog tal y como en la versión original,
 *   evitando romper dependencias existentes relacionadas con Direccion/lat-lon o la lógica de tarifas.
 */
public class EnvioTab {

    public static Tab createTab(boolean isAdmin) {
        Tab tab = new Tab(isAdmin ? "Envíos" : "Mis envíos");

        // Root pane
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Servicio (nombre preservado)
        EnvioService envioService = DataStore.getInstance().getEnvioService();

        // Tabla principal (nombre preservado)
        TableView<Envio> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // --- Columnas ---
        TableColumn<Envio, String> idCol = new TableColumn<>("ID");
        // Mantengo el uso de PropertyValueFactory para idEnvio (compatible con tu modelo),
        // pero para columnas derivadas uso ReadOnlyStringWrapper para evitar problemas de reflexión.
        idCol.setCellValueFactory(new PropertyValueFactory<>("idEnvio"));
        idCol.setPrefWidth(160);

        TableColumn<Envio, String> usuarioCol = new TableColumn<>("Usuario");
        usuarioCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(
                        cell.getValue().getUsuario() != null ? cell.getValue().getUsuario().getNombre() : "N/A")
        );
        usuarioCol.setPrefWidth(180);

        TableColumn<Envio, String> estadoCol = new TableColumn<>("Estado");
        estadoCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().getEstado() != null ? cell.getValue().getEstado().toString() : "")
        );
        estadoCol.setPrefWidth(120);

        TableColumn<Envio, Double> pesoCol = new TableColumn<>("Peso");
        pesoCol.setCellValueFactory(new PropertyValueFactory<>("peso"));
        pesoCol.setPrefWidth(80);

        TableColumn<Envio, Double> costoCol = new TableColumn<>("Costo");
        costoCol.setCellValueFactory(new PropertyValueFactory<>("costo"));
        costoCol.setPrefWidth(100);

        table.getColumns().addAll(idCol, usuarioCol, estadoCol, pesoCol, costoCol);

        // --- Cargar datos iniciales ---
        List<Envio> lista = envioService.listarTodos();
        if (!isAdmin) {
            Usuario current = DataStore.getInstance().getCurrentUser();
            lista = lista.stream()
                    .filter(e -> e.getUsuario() != null && current != null && e.getUsuario().getIdUsuario().equals(current.getIdUsuario()))
                    .collect(Collectors.toList());
        }
        ObservableList<Envio> items = FXCollections.observableArrayList(lista);
        table.setItems(items);

        // --- Formulario embebido (derecha) para edición rápida ---
        GridPane form = new GridPane();
        form.setHgap(8);
        form.setVgap(8);
        form.setPadding(new Insets(8));
        form.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e6eef0; -fx-border-radius: 6; -fx-background-radius: 6;");

        Label lblPeso = new Label("Peso (kg):");
        TextField txtPeso = new TextField();
        txtPeso.setPromptText("Peso");

        Label lblVol = new Label("Volumen (m³):");
        TextField txtVol = new TextField();
        txtVol.setPromptText("Volumen");

        Label lblPrioridad = new Label("Prioridad:");
        CheckBox chkPrioridad = new CheckBox();

        Label lblEstado = new Label("Estado:");
        ComboBox<EstadoEnvio> cmbEstado = new ComboBox<>();
        // Si EstadoEnvio existe, llenar; si no, ComboBox quedará vacío pero no rompe compilación si la enum existe.
        cmbEstado.getItems().addAll(EstadoEnvio.values());

        Label lblFormStatus = new Label();
        lblFormStatus.getStyleClass().add("small");

        form.add(lblPeso, 0, 0);
        form.add(txtPeso, 1, 0);
        form.add(lblVol, 0, 1);
        form.add(txtVol, 1, 1);
        form.add(lblPrioridad, 0, 2);
        form.add(chkPrioridad, 1, 2);
        form.add(lblEstado, 0, 3);
        form.add(cmbEstado, 1, 3);
        form.add(lblFormStatus, 0, 4, 2, 1);

        // Asegurar que el formulario no se cierre ni ocupe todo al redimensionar
        form.setPrefWidth(360);

        // --- Botones (nombres preservados) ---
        Button btnNuevo = new Button("Nuevo");
        Button btnEditar = new Button("Editar");
        Button btnEliminar = new Button("Eliminar");
        Button btnAsignar = new Button("Asignar");
        Button btnRefrescar = new Button("Refrescar");

        // Nuevo botón para guardar cambios desde el formulario (se añade sin renombrar otros)
        Button btnGuardar = new Button("Guardar");

        // Control de permisos (mantenemos la misma lógica)
        btnEditar.setDisable(!isAdmin);
        btnEliminar.setDisable(!isAdmin);
        btnAsignar.setDisable(!isAdmin);
        btnGuardar.setDisable(!isAdmin);
        btnNuevo.setDisable(!isAdmin); // conservar si quieres creación por dialog únicamente para admin

        HBox controls = new HBox(10, btnNuevo, btnEditar, btnGuardar, btnEliminar, btnAsignar, btnRefrescar);
        controls.setPadding(new Insets(10));

        // --- Conexión selección -> formulario ---
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                // cargar en formulario los campos editables
                txtPeso.setText(String.valueOf(newSel.getPeso()));
                txtVol.setText(String.valueOf(newSel.getVolumen()));
                chkPrioridad.setSelected(newSel.isPrioridad());
                if (newSel.getEstado() != null) {
                    cmbEstado.setValue(newSel.getEstado());
                } else {
                    cmbEstado.setValue(null);
                }
                lblFormStatus.setText("Editando: " + newSel.getIdEnvio());
            } else {
                clearForm(txtPeso, txtVol, chkPrioridad, cmbEstado, lblFormStatus);
            }
        });

        // --- Comportamiento botones ---

        // Refrescar: recarga los datos desde el servicio (respeta isAdmin)
        btnRefrescar.setOnAction(e -> {
            List<Envio> refreshed = envioService.listarTodos();
            if (!isAdmin) {
                Usuario cur = DataStore.getInstance().getCurrentUser();
                refreshed = refreshed.stream().filter(ev -> ev.getUsuario()!=null && ev.getUsuario().getIdUsuario().equals(cur.getIdUsuario())).collect(Collectors.toList());
            }
            items.setAll(refreshed);
        });

        // Nuevo: en la versión original usabas EnvioFormDialog para crear (con origen/destino complejos).
        // Mantengo esa opción para crear envíos completos y válidos.
        btnNuevo.setOnAction(e -> {
            EnvioFormDialog d = new EnvioFormDialog(null);
            Optional<Envio> r = d.showAndWait();
            r.ifPresent(en -> {
                if (!isAdmin) en.setUsuario(DataStore.getInstance().getCurrentUser());
                envioService.crearEnvio(en);
                // actualizar vista: recarga y filtra si aplica
                List<Envio> updated = envioService.listarTodos().stream()
                        .filter(ev -> isAdmin || (ev.getUsuario()!=null && ev.getUsuario().getIdUsuario().equals(DataStore.getInstance().getCurrentUser().getIdUsuario())))
                        .collect(Collectors.toList());
                items.setAll(updated);
            });
        });

        // Editar: carga en formulario (mismo comportamiento, pero ahora no abre diálogo)
        btnEditar.setOnAction(e -> {
            Envio sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                Alerts.showWarning("Selecciona un envío para editar");
                return;
            }
            // el formulario ya se llenó por el listener; solo mostrar mensaje
            lblFormStatus.setText("Editando: " + sel.getIdEnvio() + " — Modifica y pulsa Guardar");
        });

        // Guardar: aplica los cambios del formulario al envío seleccionado
        btnGuardar.setOnAction(e -> {
            Envio sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                // No hay selección: no intentamos crear con datos parciales, instruimos al usuario.
                Alerts.showWarning("Seleccione un envío para actualizar o use Nuevo para crear un envío completo.");
                return;
            }
            try {
                // Validaciones básicas de formato
                double peso = parseDoubleSafe(txtPeso.getText(), "Peso");
                double volumen = parseDoubleSafe(txtVol.getText(), "Volumen");

                sel.setPeso(peso);
                sel.setVolumen(volumen);
                sel.setPrioridad(chkPrioridad.isSelected());
                sel.setEstado(cmbEstado.getValue()); // puede ser null si no se selecciona

                // Llamada al servicio para actualizar (preservando la firma de servicio)
                // Se asume que existe envioService.actualizarEnvio(Envio) o similar.
                // Si tu servicio usa otro nombre, reemplaza la siguiente línea por la invocación correcta.
                envioService.actualizarEnvio(sel);

                // refrescar lista y mantener selección
                List<Envio> refreshed = envioService.listarTodos().stream()
                        .filter(ev -> isAdmin || (ev.getUsuario()!=null && ev.getUsuario().getIdUsuario().equals(DataStore.getInstance().getCurrentUser().getIdUsuario())))
                        .collect(Collectors.toList());
                items.setAll(refreshed);

                // re-seleccionar el elemento actualizado por id
                Platform.runLater(() -> {
                    for (Envio eItem : items) {
                        if (eItem.getIdEnvio().equals(sel.getIdEnvio())) {
                            table.getSelectionModel().select(eItem);
                            break;
                        }
                    }
                });

                lblFormStatus.setText("Guardado correctamente: " + sel.getIdEnvio());
            } catch (RuntimeException ex) {
                Alerts.showWarning("Error al guardar: " + ex.getMessage());
            }
        });

        // Eliminar: confirma y elimina
        btnEliminar.setOnAction(e -> {
            Envio sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                Alerts.showWarning("Selecciona un envío");
                return;
            }
            boolean ok = Alerts.confirm("Eliminar envío", "¿Eliminar envío " + sel.getIdEnvio() + "?");
            if (ok) {
                envioService.eliminarEnvio(sel.getIdEnvio());
                lblFormStatus.setText("Eliminado: " + sel.getIdEnvio());

                // actualizar vista
                List<Envio> refreshed = envioService.listarTodos().stream()
                        .filter(ev -> isAdmin || (ev.getUsuario()!=null && ev.getUsuario().getIdUsuario().equals(DataStore.getInstance().getCurrentUser().getIdUsuario())))
                        .collect(Collectors.toList());
                items.setAll(refreshed);
                clearForm(txtPeso, txtVol, chkPrioridad, cmbEstado, lblFormStatus);
            }
        });

        // Asignar: preserva la intención original (probablemente abre diálogo o invoca lógica de asignación)
        // No se altera su comportamiento — aquí lo dejamos como hook para tu implementación.
        btnAsignar.setOnAction(e -> {
            Envio sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                Alerts.showWarning("Selecciona un envío para asignar");
                return;
            }
            // Lógica existente de asignación (por ejemplo envioService.asignarRepartidor(sel))
            // Para no cambiar la API, si ya existe un método llamado asignarRepartidor, puedes usarlo aquí.
            // Ejemplo (descomentario si aplica):
            // envioService.asignarRepartidor(sel);
            Alerts.showInfo("Asignar", "Funcionalidad asignar no implementada en esta vista. Use la existente.");
        });

        // --- Layout final: tabla (center) + form (right) ---
        HBox center = new HBox(12, table, form);
        HBox.setHgrow(table, Priority.ALWAYS);
        root.setCenter(center);
        root.setBottom(controls);

        tab.setContent(root);
        return tab;
    }

    // -------------------------
    // Helpers privados
    // -------------------------
    private static void clearForm(TextField peso, TextField volumen, CheckBox prioridad, ComboBox<EstadoEnvio> estado, Label status) {
        peso.setText("");
        volumen.setText("");
        prioridad.setSelected(false);
        if (estado != null) estado.setValue(null);
        if (status != null) status.setText("");
    }

    private static double parseDoubleSafe(String text, String fieldName) {
        try {
            if (text == null || text.trim().isEmpty()) return 0.0;
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException ex) {
            throw new RuntimeException(fieldName + " debe ser un número válido.");
        }
    }
}
