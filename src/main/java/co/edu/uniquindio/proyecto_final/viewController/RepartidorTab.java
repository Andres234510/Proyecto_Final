package co.edu.uniquindio.proyecto_final.viewController;

import co.edu.uniquindio.proyecto_final.model.Disponibilidad;
import co.edu.uniquindio.proyecto_final.model.Repartidor;
import co.edu.uniquindio.proyecto_final.service.RepartidorService;
import co.edu.uniquindio.proyecto_final.service.RepartidorServiceImpl;
import co.edu.uniquindio.proyecto_final.singleton.DataStore;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Tab encargado de gestionar los repartidores del sistema.
 * Mantiene compatibilidad con la API existente (nombres de métodos/variables).
 * Ahora incluye un formulario integrado (crear/editar/eliminar desde la misma pestaña)
 * y un botón Guardar que actúa como CREATE o UPDATE según la selección.
 */
public class RepartidorTab {

    public static Tab createTab(boolean isAdmin) {

        Tab tab = new Tab("Repartidores");
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Servicio (tipo interface para compatibilidad). En llamadas puntuales se hará cast si es necesario.
        RepartidorService repartidorService = DataStore.getInstance().getRepartidorService();

        // Tabla
        TableView<Repartidor> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Repartidor, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getIdRepartidor()));

        TableColumn<Repartidor, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getNombre()));

        TableColumn<Repartidor, String> colDocumento = new TableColumn<>("Documento");
        colDocumento.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getDocumento()));

        TableColumn<Repartidor, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getTelefono()));

        TableColumn<Repartidor, String> colDisp = new TableColumn<>("Disponibilidad");
        colDisp.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                cell.getValue().getDisponibilidad() != null ? cell.getValue().getDisponibilidad().toString() : ""));

        TableColumn<Repartidor, String> colZona = new TableColumn<>("Zona");
        colZona.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getZona()));

        table.getColumns().addAll(colId, colNombre, colDocumento, colTelefono, colDisp, colZona);

        // Datos iniciales
        ObservableList<Repartidor> data = FXCollections.observableArrayList(repartidorService.listarTodos());
        table.setItems(data);

        // --------------------------
        // Formulario integrado (debajo de la tabla)
        // --------------------------
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);
        form.setPadding(new Insets(10));
        form.setStyle("-fx-background-radius:8; -fx-border-radius:8; -fx-border-color: transparent;");

        Label lblNombre = new Label("Nombre:");
        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre completo");

        Label lblDocumento = new Label("Documento:");
        TextField txtDocumento = new TextField();
        txtDocumento.setPromptText("Documento");

        Label lblTelefono = new Label("Teléfono:");
        TextField txtTelefono = new TextField();
        txtTelefono.setPromptText("Teléfono");

        Label lblDisponibilidad = new Label("Disponibilidad:");
        ComboBox<Disponibilidad> cmbDisponibilidad = new ComboBox<>();
        cmbDisponibilidad.getItems().addAll(Disponibilidad.values());
        cmbDisponibilidad.setPromptText("Seleccione");

        Label lblZona = new Label("Zona:");
        TextField txtZona = new TextField();
        txtZona.setPromptText("Zona");

        // ubicamos en grid (2 columnas)
        form.add(lblNombre, 0, 0);
        form.add(txtNombre, 1, 0);
        form.add(lblDocumento, 0, 1);
        form.add(txtDocumento, 1, 1);
        form.add(lblTelefono, 0, 2);
        form.add(txtTelefono, 1, 2);
        form.add(lblDisponibilidad, 0, 3);
        form.add(cmbDisponibilidad, 1, 3);
        form.add(lblZona, 0, 4);
        form.add(txtZona, 1, 4);

        // --------------------------
        // Controles CRUD (mismos nombres) + Guardar
        // --------------------------
        Button btnNuevo = new Button("Nuevo");
        Button btnGuardar = new Button("Guardar"); // NUEVO botón solicitado
        Button btnEditar = new Button("Editar");
        Button btnEliminar = new Button("Eliminar");
        Button btnRefrescar = new Button("Refrescar");

        btnEditar.setDisable(!isAdmin);
        btnEliminar.setDisable(!isAdmin);

        HBox controls = new HBox(10, btnNuevo, btnGuardar, btnEditar, btnEliminar, btnRefrescar);
        controls.setPadding(new Insets(10));
        controls.setAlignment(Pos.CENTER_LEFT);

        // lbl status para mensajes breves
        Label lblStatus = new Label();
        lblStatus.setPadding(new Insets(4));
        lblStatus.getStyleClass().add("small");

        // Buscador (pequeño filtro opcional)
        TextField buscador = new TextField();
        buscador.setPromptText("Buscar por nombre...");
        buscador.setMinWidth(200);
        HBox topControls = new HBox(10, buscador);
        topControls.setPadding(new Insets(6));

        // Layout: tabla arriba, formulario y controls debajo
        VBox centerBox = new VBox(8, table, form, controls, lblStatus);
        centerBox.setPadding(new Insets(6));

        root.setTop(topControls);
        root.setCenter(centerBox);

        // --------------------------
        // Lógica de interacción
        // --------------------------

        // cuando se selecciona una fila -> cargar al formulario
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtNombre.setText(newSel.getNombre());
                txtDocumento.setText(newSel.getDocumento());
                txtTelefono.setText(newSel.getTelefono());
                cmbDisponibilidad.setValue(newSel.getDisponibilidad());
                txtZona.setText(newSel.getZona());
                lblStatus.setText("Editando: " + newSel.getNombre());
            } else {
                clearFormFields(txtNombre, txtDocumento, txtTelefono, cmbDisponibilidad, txtZona, lblStatus);
            }
        });

        // filtro simple (por nombre)
        buscador.textProperty().addListener((obs, oldV, newV) -> {
            String q = newV == null ? "" : newV.trim().toLowerCase();
            List<Repartidor> all = repartidorService.listarTodos();
            if (q.isEmpty()) {
                data.setAll(all);
            } else {
                data.setAll(all.stream()
                        .filter(r -> r.getNombre() != null && r.getNombre().toLowerCase().contains(q))
                        .collect(Collectors.toList()));
            }
        });

        // Refrescar: recarga la lista completa desde el servicio
        btnRefrescar.setOnAction(e -> {
            try {
                reloadItems(data, repartidorService, buscador);
                Alerts.showInfo("Información", "Lista actualizada.");
                lblStatus.setText("Lista actualizada.");
            } catch (Exception ex) {
                Alerts.showInfo("Error", "Error al refrescar los datos: " + ex.getMessage());
                lblStatus.setText("Error al refrescar.");
            }
        });

        // Nuevo: limpia formulario y prepara para crear
        btnNuevo.setOnAction(e -> {
            table.getSelectionModel().clearSelection();
            clearFormFields(txtNombre, txtDocumento, txtTelefono, cmbDisponibilidad, txtZona, lblStatus);
            txtNombre.requestFocus();
        });

        // GUARDAR: CREATE o UPDATE según si hay seleccionado
        btnGuardar.setOnAction(e -> {
            try {
                // validations (mínimas)
                String nombre = txtNombre.getText().trim();
                String documento = txtDocumento.getText().trim();
                String telefono = txtTelefono.getText().trim();
                Disponibilidad disp = cmbDisponibilidad.getValue();
                String zona = txtZona.getText().trim();

                if (nombre.isEmpty()) {
                    Alerts.showWarning("El nombre es obligatorio.");
                    markIfEmpty(txtNombre);
                    lblStatus.setText("El nombre es obligatorio.");
                    return;
                }

                Repartidor sel = table.getSelectionModel().getSelectedItem();
                if (sel == null) {
                    // CREATE: uso la firma que ya empleabas
                    repartidorService.crearRepartidor(nombre, documento, telefono, disp, zona);
                    lblStatus.setText("Repartidor creado correctamente.");
                    reloadItems(data, repartidorService, buscador);
                    clearFormFields(txtNombre, txtDocumento, txtTelefono, cmbDisponibilidad, txtZona, lblStatus);
                } else {
                    // UPDATE: modifico el objeto y llamo al servicio existente (cast seguro)
                    sel.setNombre(nombre);
                    sel.setDocumento(documento);
                    sel.setTelefono(telefono);
                    sel.setDisponibilidad(disp);
                    sel.setZona(zona);

                    if (repartidorService instanceof RepartidorServiceImpl) {
                        ((RepartidorServiceImpl) repartidorService).actualizarRepartidor(sel);
                        lblStatus.setText("Repartidor actualizado correctamente.");
                        reloadItems(data, repartidorService, buscador);

                        // mantener selección en el registro actualizado (buscar por id)
                        Platform.runLater(() -> {
                            for (Repartidor r : data) {
                                if (r.getIdRepartidor().equals(sel.getIdRepartidor())) {
                                    table.getSelectionModel().select(r);
                                    table.scrollTo(r);
                                    break;
                                }
                            }
                        });
                    } else {
                        Alerts.showInfo("Error", "La operación de actualización no está disponible (servicio).");
                        lblStatus.setText("Imposible actualizar (servicio).");
                    }
                }
            } catch (Exception ex) {
                Alerts.showWarning("Error al guardar: " + ex.getMessage());
                lblStatus.setText("Error: " + ex.getMessage());
            }
        });

        // Editar (mantengo comportamiento: abrir dialogo + actualizar) - lo dejamos por compatibilidad
        btnEditar.setOnAction(e -> {
            Repartidor sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                Alerts.showWarning("Selecciona un repartidor");
                return;
            }
            // Rellenar formulario ya hace la selección; informar al usuario que puede editar y presionar Guardar
            lblStatus.setText("Modifica los campos y presiona Guardar para aplicar cambios.");
        });

        // Eliminar: confirma y elimina
        btnEliminar.setOnAction(e -> {
            Repartidor sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                Alerts.showWarning("Selecciona un repartidor");
                return;
            }
            boolean ok = Alerts.confirm("Eliminar repartidor", "¿Eliminar repartidor " + sel.getNombre() + "?");
            if (!ok) return;

            try {
                if (repartidorService instanceof RepartidorServiceImpl) {
                    ((RepartidorServiceImpl) repartidorService).eliminarRepartidor(sel.getIdRepartidor());
                    reloadItems(data, repartidorService, buscador);
                    Alerts.showInfo("Éxito", "Repartidor eliminado correctamente.");
                    lblStatus.setText("Repartidor eliminado.");
                } else {
                    Alerts.showInfo("Error", "La operación de eliminación no está disponible (servicio).");
                    lblStatus.setText("Imposible eliminar (servicio).");
                }
            } catch (Exception ex) {
                Alerts.showInfo("Error", "Error al eliminar repartidor: " + ex.getMessage());
                lblStatus.setText("Error al eliminar.");
            }
        });

        // Comportamiento Enter en nombre: crear si no hay selección (opcional/similar a tu idea)
        txtNombre.setOnAction(ev -> {
            if (table.getSelectionModel().getSelectedItem() == null) {
                btnGuardar.fire();
            }
        });

        // Finalmente, montamos la UI
        tab.setContent(root);
        return tab;
    }

    // --------------------------
    // Helpers privados (no degradan la API pública)
    // --------------------------

    private static void markIfEmpty(TextField tf) {
        if (tf == null) return;
        if (tf.getText() == null || tf.getText().trim().isEmpty()) {
            tf.setStyle("-fx-border-color: red; -fx-border-width: 1.5;");
            // quitar el estilo después de 2s para no quedar marcado permanentemente
            Platform.runLater(() -> {
                // programar limpieza leve (no precisa scheduler complejo)
                new Thread(() -> {
                    try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
                    Platform.runLater(() -> tf.setStyle(null));
                }).start();
            });
        }
    }

    private static void clearFormFields(TextField nombre, TextField documento, TextField telefono,
                                        ComboBox<Disponibilidad> disponibilidad, TextField zona, Label lblStatus) {
        if (nombre != null) nombre.clear();
        if (documento != null) documento.clear();
        if (telefono != null) telefono.clear();
        if (disponibilidad != null) disponibilidad.setValue(null);
        if (zona != null) zona.clear();
        if (lblStatus != null) lblStatus.setText("");
    }

    private static void reloadItems(ObservableList<Repartidor> data, RepartidorService repartidorService, TextField buscador) {
        List<Repartidor> all = repartidorService.listarTodos();
        String q = (buscador == null || buscador.getText() == null) ? "" : buscador.getText().trim().toLowerCase();
        if (q.isEmpty()) {
            data.setAll(all);
        } else {
            data.setAll(all.stream().filter(r -> r.getNombre() != null && r.getNombre().toLowerCase().contains(q)).collect(Collectors.toList()));
        }
    }
}
