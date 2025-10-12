package co.edu.uniquindio.proyecto_final.viewController;

import co.edu.uniquindio.proyecto_final.model.*;
import co.edu.uniquindio.proyecto_final.service.EnvioService;
import co.edu.uniquindio.proyecto_final.service.RepartidorService;
import co.edu.uniquindio.proyecto_final.service.TarifaService;
import co.edu.uniquindio.proyecto_final.singleton.DataStore;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class EnvioTab {

    public static Tab createTab() {
        Tab tab = new Tab("Envíos");
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        DataStore ds = DataStore.getInstance();
        EnvioService envioService = ds.getEnvioService();
        TarifaService tarifaService = ds.getTarifaService();
        RepartidorService repartidorService = ds.getRepartidorService();

        TableView<Envio> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Envio, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idEnvio"));
        TableColumn<Envio, String> usuarioCol = new TableColumn<>("Usuario");
        usuarioCol.setCellValueFactory(cell -> {
            Usuario u = cell.getValue().getUsuario();
            return new ReadOnlyStringWrapper(u != null ? u.getNombre() : "N/A");
        });
        TableColumn<Envio, String> estadoCol = new TableColumn<>("Estado");
        estadoCol.setCellValueFactory(new PropertyValueFactory<>("estado"));
        TableColumn<Envio, Double> pesoCol = new TableColumn<>("Peso");
        pesoCol.setCellValueFactory(new PropertyValueFactory<>("peso"));
        TableColumn<Envio, Double> costoCol = new TableColumn<>("Costo");
        costoCol.setCellValueFactory(new PropertyValueFactory<>("costo"));

        table.getColumns().addAll(idCol, usuarioCol, estadoCol, pesoCol, costoCol);

        ObservableList<Envio> items = FXCollections.observableArrayList(envioService.listarTodos());
        table.setItems(items);

        Button btnNuevo = new Button("Nuevo");
        Button btnEditar = new Button("Editar");
        Button btnEliminar = new Button("Eliminar");
        Button btnAsignar = new Button("Asignar repartidor");
        Button btnRefrescar = new Button("Refrescar");

        HBox controls = new HBox(10, btnNuevo, btnEditar, btnEliminar, btnAsignar, btnRefrescar);
        controls.setPadding(new Insets(10));

        btnRefrescar.setOnAction(e -> items.setAll(envioService.listarTodos()));

        btnNuevo.setOnAction(e -> {
            EnvioFormDialog dialog = new EnvioFormDialog(null);
            Optional<Envio> res = dialog.showAndWait();
            res.ifPresent(envio -> {
                envio.setFechaEstimadaEntrega(LocalDateTime.now().plusHours(3));
                envioService.crearEnvio(envio);
                items.setAll(envioService.listarTodos());
            });
        });

        btnEditar.setOnAction(e -> {
            Envio sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { Alerts.showWarning("Selecciona un envío para editar"); return; }
            EnvioFormDialog dialog = new EnvioFormDialog(sel);
            dialog.showAndWait().ifPresent(updated -> {
                sel.setPeso(updated.getPeso());
                sel.setVolumen(updated.getVolumen());
                envioService.recalcularCosto(sel.getIdEnvio());
                items.setAll(envioService.listarTodos());
            });
        });

        btnEliminar.setOnAction(e -> {
            Envio sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { Alerts.showWarning("Selecciona un envío para eliminar"); return; }
            boolean ok = Alerts.confirm("Eliminar envío", "¿Eliminar envío " + sel.getIdEnvio() + "?");
            if (ok) {
                DataStore.getInstance().getEnvioRepo().delete(sel.getIdEnvio());
                items.setAll(envioService.listarTodos());
            }
        });

        btnAsignar.setOnAction(e -> {
            Envio sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { Alerts.showWarning("Selecciona un envío para asignar"); return; }
            boolean assigned = envioService.asignarRepartidor(sel);
            if (assigned) Alerts.showInfo("Asignado", "Repartidor asignado al envío");
            else Alerts.showWarning("No hay repartidores disponibles");
            items.setAll(envioService.listarTodos());
        });

        root.setCenter(table);
        root.setBottom(controls);
        tab.setContent(root);
        return tab;
    }
}
