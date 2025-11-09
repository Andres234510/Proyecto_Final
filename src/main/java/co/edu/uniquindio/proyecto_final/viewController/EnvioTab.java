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
 * - Firma pública: public static Tab createTab(boolean isAdmin) (sin cambios)
 * - Conserva nombres: envioService, table, btnNuevo, btnEditar, btnEliminar, btnAsignar, btnRefrescar
 * - Añade formulario embebido (derecha) y búsqueda (arriba)
 * - No altera modelos
 */
public class EnvioTab {

    public static Tab createTab(boolean isAdmin) {
        Tab tab = new Tab(isAdmin ? "Envíos" : "Mis envíos");

        // Root
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Servicio (preservado)
        EnvioService envioService = DataStore.getInstance().getEnvioService();

        // Tabla principal (preservado)
        TableView<Envio> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // --- Columnas ---
        TableColumn<Envio, String> idCol = new TableColumn<>("ID");
        // idEnvio existe en el modelo, PropertyValueFactory está bien, pero usamos prefWidth
        idCol.setCellValueFactory(new PropertyValueFactory<>("idEnvio"));
        idCol.setPrefWidth(160);

        TableColumn<Envio, String> usuarioCol = new TableColumn<>("Usuario");
        usuarioCol.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().getUsuario() != null ? cell.getValue().getUsuario().getNombre() : "N/A")
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

        // --- Búsqueda / filtro (opcional, mejora de usabilidad) ---
        TextField txtSearch = new TextField();
        txtSearch.setPromptText("Buscar por ID, usuario o estado...");
        txtSearch.setMinWidth(220);

        HBox searchBox = new HBox(8, new Label("Buscar:"), txtSearch);
        searchBox.setPadding(new Insets(0, 0, 8, 0));

        // --- Cargar datos iniciales (respeta isAdmin) ---
        List<Envio> lista = envioService.listarTodos();
        if (!isAdmin) {
            Usuario current = DataStore.getInstance().getCurrentUser();
            lista = lista.stream()
                    .filter(e -> e.getUsuario() != null && current != null && e.getUsuario().getIdUsuario().equals(current.getIdUsuario()))
                    .collect(Collectors.toList());
        }
        ObservableList<Envio> masterItems = FXCollections.observableArrayList(lista);
        ObservableList<Envio> items = FXCollections.observableArrayList(masterItems);
        table.setItems(items);

        // --- Formulario embebido (derecha) ---
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
        form.setPrefWidth(360);

        // --- Botones (nombres preservados) ---
        Button btnNuevo = new Button("Nuevo");
        Button btnEditar = new Button("Editar");
        Button btnEliminar = new Button("Eliminar");
        Button btnAsignar = new Button("Asignar");
        Button btnRefrescar = new Button("Refrescar");

        // Botón adicional Guardar para aplicar cambios desde el formulario
        Button btnGuardar = new Button("Guardar");

        btnEditar.setDisable(!isAdmin);
        btnEliminar.setDisable(!isAdmin);
        btnAsignar.setDisable(!isAdmin);
        btnGuardar.setDisable(!isAdmin);
        btnNuevo.setDisable(!isAdmin); // conserva la política original para creación

        HBox controls = new HBox(10, btnNuevo, btnEditar, btnGuardar, btnEliminar, btnAsignar, btnRefrescar);
        controls.setPadding(new Insets(10));

        // --- Selección -> carga formulario ---
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtPeso.setText(String.valueOf(newSel.getPeso()));
                txtVol.setText(String.valueOf(newSel.getVolumen()));
                chkPrioridad.setSelected(newSel.isPrioridad());
                cmbEstado.setValue(newSel.getEstado());
                lblFormStatus.setText("Editando: " + newSel.getIdEnvio());
            } else {
                clearForm(txtPeso, txtVol, chkPrioridad, cmbEstado, lblFormStatus);
            }
        });

        // --- Filtrado simple al escribir en búsqueda ---
        txtSearch.textProperty().addListener((obs, oldV, newV) -> {
            String q = newV == null ? "" : newV.trim().toLowerCase();
            if (q.isEmpty()) {
                items.setAll(masterItems);
            } else {
                items.setAll(masterItems.stream().filter(en ->
                        (en.getIdEnvio() != null && en.getIdEnvio().toLowerCase().contains(q)) ||
                                (en.getUsuario() != null && en.getUsuario().getNombre() != null && en.getUsuario().getNombre().toLowerCase().contains(q)) ||
                                (en.getEstado() != null && en.getEstado().toString().toLowerCase().contains(q))
                ).collect(Collectors.toList()));
            }
        });

        // --- Acciones de botones ---

        // Refrescar
        btnRefrescar.setOnAction(e -> {
            List<Envio> refreshed = envioService.listarTodos();
            if (!isAdmin) {
                Usuario cur = DataStore.getInstance().getCurrentUser();
                refreshed = refreshed.stream().filter(ev -> ev.getUsuario()!=null && ev.getUsuario().getIdUsuario().equals(cur.getIdUsuario())).collect(Collectors.toList());
            }
            masterItems.setAll(refreshed);
            items.setAll(masterItems);
        });

        // Nuevo -> abrir diálogo (mantener comportamiento original para creación completa)
        btnNuevo.setOnAction(e -> {
            EnvioFormDialog d = new EnvioFormDialog(null);
            Optional<Envio> r = d.showAndWait();
            r.ifPresent(en -> {
                if (!isAdmin) en.setUsuario(DataStore.getInstance().getCurrentUser());
                envioService.crearEnvio(en);
                // recargar
                List<Envio> updated = envioService.listarTodos().stream()
                        .filter(ev -> isAdmin || (ev.getUsuario()!=null && ev.getUsuario().getIdUsuario().equals(DataStore.getInstance().getCurrentUser().getIdUsuario())))
                        .collect(Collectors.toList());
                masterItems.setAll(updated);
                items.setAll(masterItems);
            });
        });

        // Editar -> instrucción al usuario (form ya se llenó con selección)
        btnEditar.setOnAction(e -> {
            Envio sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                Alerts.showWarning("Selecciona un envío para editar");
                return;
            }
            lblFormStatus.setText("Editando: " + sel.getIdEnvio() + " — Modifica y pulsa Guardar");
        });

        // Guardar -> aplicar cambios del formulario al envío seleccionado
        btnGuardar.setOnAction(e -> {
            Envio sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                Alerts.showWarning("Seleccione un envío para actualizar o use Nuevo para crear un envío completo.");
                return;
            }
            try {
                double peso = parseDoubleSafe(txtPeso.getText(), "Peso");
                double volumen = parseDoubleSafe(txtVol.getText(), "Volumen");

                sel.setPeso(peso);
                sel.setVolumen(volumen);
                sel.setPrioridad(chkPrioridad.isSelected());
                sel.setEstado(cmbEstado.getValue()); // puede ser null

                // Llamada al servicio para actualizar (preserva la firma esperada)
                envioService.actualizarEnvio(sel);

                // recargar y mantener selección
                List<Envio> refreshed = envioService.listarTodos().stream()
                        .filter(ev -> isAdmin || (ev.getUsuario()!=null && ev.getUsuario().getIdUsuario().equals(DataStore.getInstance().getCurrentUser().getIdUsuario())))
                        .collect(Collectors.toList());
                masterItems.setAll(refreshed);
                items.setAll(masterItems);

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

        // Eliminar -> confirmación
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
                // recargar
                List<Envio> refreshed = envioService.listarTodos().stream()
                        .filter(ev -> isAdmin || (ev.getUsuario()!=null && ev.getUsuario().getIdUsuario().equals(DataStore.getInstance().getCurrentUser().getIdUsuario())))
                        .collect(Collectors.toList());
                masterItems.setAll(refreshed);
                items.setAll(masterItems);
                clearForm(txtPeso, txtVol, chkPrioridad, cmbEstado, lblFormStatus);
            }
        });

        // Asignar -> hook (dejar para tu lógica existente)
        btnAsignar.setOnAction(e -> {
            Envio sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                Alerts.showWarning("Selecciona un envío para asignar");
                return;
            }
            // Si tienes un método envioService.asignarRepartidor(sel) o similar,
            // puedes llamarlo aquí. Por defecto mostramos un info.
            Alerts.showInfo("Asignar", "Funcionalidad de asignación disponible en el servicio.");
        });

        // --- Layout: arriba búsqueda + tabla + form + controles abajo ---
        HBox center = new HBox(12);
        // Tabla y form lado a lado
        center.getChildren().addAll(table, form);
        HBox.setHgrow(table, Priority.ALWAYS);
        root.setTop(searchBox);
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
