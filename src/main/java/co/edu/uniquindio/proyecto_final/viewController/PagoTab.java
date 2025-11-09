package co.edu.uniquindio.proyecto_final.viewController;

import co.edu.uniquindio.proyecto_final.model.Pago;
import co.edu.uniquindio.proyecto_final.model.ResultadoPago;
import co.edu.uniquindio.proyecto_final.service.PagoService;
import co.edu.uniquindio.proyecto_final.singleton.DataStore;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
 * PagoTab - versión con formulario integrado (crear/editar/eliminar/refresh)
 * - No se cambian nombres públicos ni firmas existentes.
 * - Añadido botón Guardar que hace CREATE o UPDATE según selección en la tabla.
 */
public class PagoTab {

    public static Tab createTab(boolean isAdmin) {
        Tab tab = new Tab("Pagos");
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        PagoService pagoService = DataStore.getInstance().getPagoService();

        // Tabla
        TableView<Pago> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Pago, String> colId = new TableColumn<>("ID Pago");
        colId.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getIdPago()));

        TableColumn<Pago, String> colEnvio = new TableColumn<>("ID Envío");
        colEnvio.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getIdEnvio()));

        TableColumn<Pago, String> colMonto = new TableColumn<>("Monto");
        colMonto.setCellValueFactory(cell -> {
            // mostrar con dos decimales
            String formatted = String.format("%.2f", cell.getValue().getMonto());
            return new ReadOnlyStringWrapper(formatted);
        });

        TableColumn<Pago, String> colMetodo = new TableColumn<>("Método");
        colMetodo.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getMetodo()));

        TableColumn<Pago, String> colResultado = new TableColumn<>("Resultado");
        colResultado.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                cell.getValue().getResultado() != null ? cell.getValue().getResultado().toString() : ""));

        table.getColumns().addAll(colId, colEnvio, colMonto, colMetodo, colResultado);

        // Datos iniciales
        ObservableList<Pago> data = FXCollections.observableArrayList(pagoService.listarTodos());
        table.setItems(data);

        // ---------------------
        // Formulario integrado
        // ---------------------
        // Campos: idEnvio (texto), monto (texto validado en double), metodo (texto), resultado (combo)
        TextField txtIdEnvio = new TextField();
        txtIdEnvio.setPromptText("ID Envío");

        TextField txtMonto = new TextField();
        txtMonto.setPromptText("Monto (ej. 12.50)");

        TextField txtMetodo = new TextField();
        txtMetodo.setPromptText("Tarjeta / Efectivo / ...");

        ComboBox<ResultadoPago> cmbResultado = new ComboBox<>();
        cmbResultado.getItems().addAll(ResultadoPago.values());
        cmbResultado.setPromptText("Resultado");

        // Status label
        Label lblStatus = new Label();
        lblStatus.getStyleClass().add("small");

        // Botones (mantenemos nombres antiguos y agregamos Guardar)
        Button btnNuevo = new Button("Nuevo");
        Button btnGuardar = new Button("Guardar"); // nuevo
        Button btnEditar = new Button("Editar");
        Button btnEliminar = new Button("Eliminar");
        Button btnRefrescar = new Button("Refrescar");

        btnEditar.setDisable(!isAdmin);
        btnEliminar.setDisable(!isAdmin);

        // Barra de búsqueda simple
        TextField buscador = new TextField();
        buscador.setPromptText("Buscar por ID pago o ID envío...");
        buscador.setMinWidth(200);
        HBox topBox = new HBox(10, buscador);
        topBox.setPadding(new Insets(6));

        // Layout controles
        HBox controls = new HBox(10, btnNuevo, btnGuardar, btnEditar, btnEliminar, btnRefrescar);
        controls.setPadding(new Insets(10));
        controls.setAlignment(Pos.CENTER_LEFT);

        // Form row (simple HBox to save vertical space)
        HBox formRow = new HBox(10,
                new VBox(new Label("ID Envío"), txtIdEnvio),
                new VBox(new Label("Monto"), txtMonto),
                new VBox(new Label("Método"), txtMetodo),
                new VBox(new Label("Resultado"), cmbResultado)
        );
        formRow.setPadding(new Insets(8));

        VBox centerBox = new VBox(8, table, formRow, controls, lblStatus);
        centerBox.setPadding(new Insets(6));

        root.setTop(topBox);
        root.setCenter(centerBox);
        tab.setContent(root);

        // ---------------------
        // Comportamiento / Lógica
        // ---------------------

        // Cuando se selecciona fila -> cargar en formulario
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtIdEnvio.setText(newSel.getIdEnvio());
                txtMonto.setText(String.valueOf(newSel.getMonto()));
                txtMetodo.setText(newSel.getMetodo());
                cmbResultado.setValue(newSel.getResultado());
                lblStatus.setText("Editando pago " + newSel.getIdPago());
            } else {
                clearForm(txtIdEnvio, txtMonto, txtMetodo, cmbResultado, lblStatus);
            }
        });

        // Buscador simple (filtrado por idPago o idEnvio)
        buscador.textProperty().addListener((obs, oldV, newV) -> {
            String q = newV == null ? "" : newV.trim().toLowerCase();
            List<Pago> all = pagoService.listarTodos();
            if (q.isEmpty()) {
                data.setAll(all);
            } else {
                data.setAll(all.stream()
                        .filter(p -> (p.getIdPago() != null && p.getIdPago().toLowerCase().contains(q))
                                || (p.getIdEnvio() != null && p.getIdEnvio().toLowerCase().contains(q)))
                        .collect(Collectors.toList()));
            }
        });

        // Refrescar
        btnRefrescar.setOnAction(e -> {
            try {
                reloadItems(data, pagoService, buscador);
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
            clearForm(txtIdEnvio, txtMonto, txtMetodo, cmbResultado, lblStatus);
            txtIdEnvio.requestFocus();
        });

        // Guardar -> crear o actualizar según selección
        btnGuardar.setOnAction(e -> {
            try {
                String idEnvio = txtIdEnvio.getText().trim();
                String sMonto = txtMonto.getText().trim();
                String metodo = txtMetodo.getText().trim();
                ResultadoPago resultado = cmbResultado.getValue();

                // Validaciones mínimas
                if (idEnvio.isEmpty()) {
                    Alerts.showWarning("El ID de envío es obligatorio.");
                    markIfEmpty(txtIdEnvio);
                    lblStatus.setText("ID de envío obligatorio.");
                    return;
                }
                if (sMonto.isEmpty()) {
                    Alerts.showWarning("El monto es obligatorio.");
                    markIfEmpty(txtMonto);
                    lblStatus.setText("Monto obligatorio.");
                    return;
                }

                double monto;
                try {
                    monto = Double.parseDouble(sMonto);
                    if (monto < 0) throw new NumberFormatException("Monto negativo");
                } catch (NumberFormatException ex) {
                    Alerts.showWarning("Monto inválido. Usa un número válido (p.ej. 12.50).");
                    markIfEmpty(txtMonto);
                    lblStatus.setText("Monto inválido.");
                    return;
                }

                Pago sel = table.getSelectionModel().getSelectedItem();
                if (sel == null) {
                    // CREATE
                    Pago nuevo = new Pago(idEnvio, monto, metodo, resultado);
                    pagoService.crearPago(nuevo); // firma usada en código previo
                    lblStatus.setText("Pago creado correctamente.");
                    reloadItems(data, pagoService, buscador);
                    clearForm(txtIdEnvio, txtMonto, txtMetodo, cmbResultado, lblStatus);
                } else {
                    // UPDATE -> modificar objeto y persistir
                    sel.setIdEnvio(idEnvio);
                    sel.setMonto(monto);
                    sel.setMetodo(metodo);
                    sel.setResultado(resultado);
                    pagoService.actualizarPago(sel);
                    lblStatus.setText("Pago actualizado correctamente.");
                    reloadItems(data, pagoService, buscador);

                    // mantener selección en el registro actualizado
                    Platform.runLater(() -> {
                        for (Pago p : data) {
                            if (p.getIdPago().equals(sel.getIdPago())) {
                                table.getSelectionModel().select(p);
                                table.scrollTo(p);
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

        // Editar -> instruye al usuario (ya puede editar en el formulario y guardar)
        btnEditar.setOnAction(e -> {
            Pago sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                Alerts.showWarning("Selecciona un pago");
                return;
            }
            lblStatus.setText("Modifica los campos y presiona Guardar para guardar los cambios.");
        });

        // Eliminar -> confirma y elimina
        btnEliminar.setOnAction(e -> {
            Pago sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                Alerts.showWarning("Selecciona un pago");
                return;
            }
            boolean ok = Alerts.confirm("Eliminar pago", "¿Eliminar pago " + sel.getIdPago() + "?");
            if (!ok) return;
            try {
                pagoService.eliminarPago(sel.getIdPago());
                reloadItems(data, pagoService, buscador);
                Alerts.showInfo("Éxito", "Pago eliminado correctamente.");
                lblStatus.setText("Pago eliminado.");
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
        tf.setStyle("-fx-border-color: red; -fx-border-width: 1.5;");
        // limpiar estilo tras 1.5s
        Platform.runLater(() -> {
            new Thread(() -> {
                try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
                Platform.runLater(() -> tf.setStyle(null));
            }).start();
        });
    }

    private static void clearForm(TextField txtIdEnvio, TextField txtMonto, TextField txtMetodo,
                                  ComboBox<ResultadoPago> cmbResultado, Label lblStatus) {
        if (txtIdEnvio != null) txtIdEnvio.clear();
        if (txtMonto != null) txtMonto.clear();
        if (txtMetodo != null) txtMetodo.clear();
        if (cmbResultado != null) cmbResultado.setValue(null);
        if (lblStatus != null) lblStatus.setText("");
    }

    private static void reloadItems(ObservableList<Pago> data, PagoService pagoService, TextField buscador) {
        List<Pago> all = pagoService.listarTodos();
        String q = (buscador == null || buscador.getText() == null) ? "" : buscador.getText().trim().toLowerCase();
        if (q.isEmpty()) {
            data.setAll(all);
        } else {
            data.setAll(all.stream().filter(p ->
                    (p.getIdPago() != null && p.getIdPago().toLowerCase().contains(q)) ||
                            (p.getIdEnvio() != null && p.getIdEnvio().toLowerCase().contains(q))
            ).collect(Collectors.toList()));
        }
    }
}
