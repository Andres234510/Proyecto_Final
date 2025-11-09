package co.edu.uniquindio.proyecto_final.viewController;

import co.edu.uniquindio.proyecto_final.model.Direccion;
import co.edu.uniquindio.proyecto_final.service.DireccionService;
import co.edu.uniquindio.proyecto_final.singleton.DataStore;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * DireccionTab - versión con formulario integrado (crear/editar/eliminar/refresh)
 * - Mantiene firmas, nombres y compatibilidad con el resto del proyecto.
 */
public class DireccionTab {

    public static Tab createTab(boolean isAdmin) {
        Tab tab = new Tab("Direcciones");
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        DireccionService direccionService = DataStore.getInstance().getDireccionService();

        // Tabla
        TableView<Direccion> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Direccion, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getIdDireccion()));

        TableColumn<Direccion, String> colAlias = new TableColumn<>("Alias");
        colAlias.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getAlias()));

        // En tu modelo no existe 'descripcion' — usamos calle/ciudad para la descripción
        TableColumn<Direccion, String> colDesc = new TableColumn<>("Dirección");
        colDesc.setCellValueFactory(cell -> {
            Direccion d = cell.getValue();
            String addr = (d.getCalle() != null ? d.getCalle() : "") +
                    (d.getCiudad() != null && !d.getCiudad().isEmpty() ? " - " + d.getCiudad() : "");
            return new ReadOnlyStringWrapper(addr);
        });

        TableColumn<Direccion, String> colLat = new TableColumn<>("Latitud");
        colLat.setCellValueFactory(cell -> new ReadOnlyStringWrapper(String.valueOf(cell.getValue().getLatitud())));

        TableColumn<Direccion, String> colLon = new TableColumn<>("Longitud");
        colLon.setCellValueFactory(cell -> new ReadOnlyStringWrapper(String.valueOf(cell.getValue().getLongitud())));

        table.getColumns().addAll(colId, colAlias, colDesc, colLat, colLon);

        // Datos iniciales
        ObservableList<Direccion> data = FXCollections.observableArrayList(direccionService.listarTodos());
        table.setItems(data);

        // Formulario integrado
        TextField txtAlias = new TextField();
        txtAlias.setPromptText("Alias");

        TextField txtCalle = new TextField();
        txtCalle.setPromptText("Calle / Descripción");

        TextField txtCiudad = new TextField();
        txtCiudad.setPromptText("Ciudad");

        TextField txtLat = new TextField();
        txtLat.setPromptText("Latitud (ej. 4.65)");

        TextField txtLon = new TextField();
        txtLon.setPromptText("Longitud (ej. -74.05)");

        Label lblStatus = new Label();
        lblStatus.getStyleClass().add("small");

        // Botones: mantenemos Nuevo, Editar, Eliminar, Refrescar y agregamos Guardar
        Button btnNuevo = new Button("Nuevo");
        Button btnGuardar = new Button("Guardar");
        Button btnEditar = new Button("Editar");
        Button btnEliminar = new Button("Eliminar");
        Button btnRefrescar = new Button("Refrescar");

        btnEditar.setDisable(!isAdmin);
        btnEliminar.setDisable(!isAdmin);

        // Layout
        HBox controls = new HBox(10, btnNuevo, btnGuardar, btnEditar, btnEliminar, btnRefrescar);
        controls.setPadding(new Insets(10));
        controls.setAlignment(Pos.CENTER_LEFT);

        HBox formRow = new HBox(10,
                new VBox(new Label("Alias"), txtAlias),
                new VBox(new Label("Calle"), txtCalle),
                new VBox(new Label("Ciudad"), txtCiudad),
                new VBox(new Label("Latitud"), txtLat),
                new VBox(new Label("Longitud"), txtLon)
        );
        formRow.setPadding(new Insets(8));

        // Buscador simple
        TextField buscador = new TextField();
        buscador.setPromptText("Buscar por ID o alias...");
        buscador.setMinWidth(200);
        HBox topBox = new HBox(10, buscador);
        topBox.setPadding(new Insets(6));

        VBox centerBox = new VBox(8, table, formRow, controls, lblStatus);
        centerBox.setPadding(new Insets(6));

        root.setTop(topBox);
        root.setCenter(centerBox);
        tab.setContent(root);

        // --------------------
        // Lógica / comportamiento
        // --------------------

        // Selección: cargar en formulario
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtAlias.setText(newSel.getAlias());
                txtCalle.setText(newSel.getCalle());
                txtCiudad.setText(newSel.getCiudad());
                txtLat.setText(String.valueOf(newSel.getLatitud()));
                txtLon.setText(String.valueOf(newSel.getLongitud()));
                lblStatus.setText("Editando dirección " + newSel.getIdDireccion());
            } else {
                clearForm(txtAlias, txtCalle, txtCiudad, txtLat, txtLon, lblStatus);
            }
        });

        // Buscador: filtra por id o alias
        buscador.textProperty().addListener((obs, oldV, newV) -> {
            String q = newV == null ? "" : newV.trim().toLowerCase();
            List<Direccion> all = direccionService.listarTodos();
            if (q.isEmpty()) {
                data.setAll(all);
            } else {
                data.setAll(all.stream()
                        .filter(d -> (d.getIdDireccion() != null && d.getIdDireccion().toLowerCase().contains(q))
                                || (d.getAlias() != null && d.getAlias().toLowerCase().contains(q)))
                        .collect(Collectors.toList()));
            }
        });

        // Refrescar
        btnRefrescar.setOnAction(e -> {
            try {
                reloadItems(data, direccionService, buscador);
                Alerts.showInfo("Información", "Lista actualizada.");
                lblStatus.setText("Lista actualizada.");
            } catch (Exception ex) {
                Alerts.showInfo("Error", "Error al refrescar: " + ex.getMessage());
                lblStatus.setText("Error al refrescar.");
            }
        });

        // Nuevo -> limpia formulario y quita selección
        btnNuevo.setOnAction(e -> {
            table.getSelectionModel().clearSelection();
            clearForm(txtAlias, txtCalle, txtCiudad, txtLat, txtLon, lblStatus);
            txtAlias.requestFocus();
        });

        // Guardar -> crea o actualiza según selección
        btnGuardar.setOnAction(e -> {
            try {
                String alias = txtAlias.getText().trim();
                String calle = txtCalle.getText().trim();
                String ciudad = txtCiudad.getText().trim();
                String sLat = txtLat.getText().trim();
                String sLon = txtLon.getText().trim();

                // Validaciones
                if (alias.isEmpty() || calle.isEmpty()) {
                    Alerts.showWarning("Alias y Calle son obligatorios.");
                    markIfEmpty(txtAlias);
                    markIfEmpty(txtCalle);
                    lblStatus.setText("Alias y Calle obligatorios.");
                    return;
                }

                double lat = 0.0;
                double lon = 0.0;
                if (!sLat.isEmpty()) {
                    try {
                        lat = Double.parseDouble(sLat);
                    } catch (NumberFormatException ex) {
                        Alerts.showWarning("Latitud inválida.");
                        markIfEmpty(txtLat);
                        lblStatus.setText("Latitud inválida.");
                        return;
                    }
                }
                if (!sLon.isEmpty()) {
                    try {
                        lon = Double.parseDouble(sLon);
                    } catch (NumberFormatException ex) {
                        Alerts.showWarning("Longitud inválida.");
                        markIfEmpty(txtLon);
                        lblStatus.setText("Longitud inválida.");
                        return;
                    }
                }

                Direccion sel = table.getSelectionModel().getSelectedItem();
                if (sel == null) {
                    // CREATE -> usa exactamente la firma que ya existe (crearDireccion(Direccion))
                    Direccion nuevo = new Direccion(alias, calle, ciudad, lat, lon);
                    direccionService.crearDireccion(nuevo);
                    lblStatus.setText("Dirección creada correctamente.");
                    reloadItems(data, direccionService, buscador);
                    clearForm(txtAlias, txtCalle, txtCiudad, txtLat, txtLon, lblStatus);
                } else {
                    // UPDATE -> modificar el objeto y persistir
                    sel.setAlias(alias);
                    sel.setCalle(calle);
                    sel.setCiudad(ciudad);
                    sel.setLatitud(lat);
                    sel.setLongitud(lon);
                    direccionService.actualizarDireccion(sel);
                    lblStatus.setText("Dirección actualizada correctamente.");
                    reloadItems(data, direccionService, buscador);

                    // mantener selección en el registro actualizado
                    Platform.runLater(() -> {
                        for (Direccion d : data) {
                            if (d.getIdDireccion().equals(sel.getIdDireccion())) {
                                table.getSelectionModel().select(d);
                                table.scrollTo(d);
                                break;
                            }
                        }
                    });
                }
            } catch (Exception ex) {
                Alerts.showWarning("Error al guardar: " + ex.getMessage());
                lblStatus.setText("Error: " + ex.getMessage());
            }
        });

        // Editar -> instruye al usuario (el formulario ya permite editar)
        btnEditar.setOnAction(e -> {
            Direccion sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                Alerts.showWarning("Selecciona una dirección");
                return;
            }
            lblStatus.setText("Modifica los campos y presiona Guardar para guardar los cambios.");
        });

        // Eliminar -> confirmar y eliminar
        btnEliminar.setOnAction(e -> {
            Direccion sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                Alerts.showWarning("Selecciona una dirección");
                return;
            }
            boolean ok = Alerts.confirm("Eliminar dirección", "¿Eliminar dirección " + sel.getAlias() + "?");
            if (!ok) return;
            try {
                direccionService.eliminarDireccion(sel.getIdDireccion());
                reloadItems(data, direccionService, buscador);
                Alerts.showInfo("Éxito", "Dirección eliminada correctamente.");
                lblStatus.setText("Dirección eliminada.");
            } catch (Exception ex) {
                Alerts.showInfo("Error", "Error al eliminar: " + ex.getMessage());
                lblStatus.setText("Error al eliminar.");
            }
        });

        return tab;
    }

    // -------------------------
    // Helpers
    // -------------------------
    private static void markIfEmpty(TextField tf) {
        if (tf == null) return;
        tf.setStyle("-fx-border-color: red; -fx-border-width: 1.4;");
        // limpiar estilo tras 1.5s
        Platform.runLater(() -> {
            new Thread(() -> {
                try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
                Platform.runLater(() -> tf.setStyle(null));
            }).start();
        });
    }

    private static void clearForm(TextField alias, TextField calle, TextField ciudad,
                                  TextField lat, TextField lon, Label lblStatus) {
        if (alias != null) alias.clear();
        if (calle != null) calle.clear();
        if (ciudad != null) ciudad.clear();
        if (lat != null) lat.clear();
        if (lon != null) lon.clear();
        if (lblStatus != null) lblStatus.setText("");
    }

    private static void reloadItems(ObservableList<Direccion> data,
                                    DireccionService direccionService, TextField buscador) {
        List<Direccion> all = direccionService.listarTodos();
        String q = (buscador == null || buscador.getText() == null) ? "" : buscador.getText().trim().toLowerCase();
        if (q.isEmpty()) {
            data.setAll(all);
        } else {
            data.setAll(all.stream().filter(d ->
                    (d.getIdDireccion() != null && d.getIdDireccion().toLowerCase().contains(q)) ||
                            (d.getAlias() != null && d.getAlias().toLowerCase().contains(q))
            ).collect(Collectors.toList()));
        }
    }
}
