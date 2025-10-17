package co.edu.uniquindio.proyecto_final.viewController;

import co.edu.uniquindio.proyecto_final.model.Repartidor;
import co.edu.uniquindio.proyecto_final.service.RepartidorServiceImpl;
import co.edu.uniquindio.proyecto_final.singleton.DataStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.Optional;

public class RepartidorTab {

    public static Tab createTab(boolean isAdmin) {
        Tab tab = new Tab("Repartidores");
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        RepartidorServiceImpl repartidorService = DataStore.getInstance().getRepartidorService();

        TableView<Repartidor> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Repartidor, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idRepartidor"));
        TableColumn<Repartidor, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<Repartidor, String> colTel = new TableColumn<>("Teléfono");
        colTel.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        TableColumn<Repartidor, String> colZona = new TableColumn<>("Zona");
        colZona.setCellValueFactory(new PropertyValueFactory<>("zona"));
        TableColumn<Repartidor, Boolean> colDisp = new TableColumn<>("Disponible");
        colDisp.setCellValueFactory(new PropertyValueFactory<>("disponible"));

        table.getColumns().addAll(colId, colNombre, colTel, colZona, colDisp);

        ObservableList<Repartidor> data = FXCollections.observableArrayList(repartidorService.listarTodos());
        table.setItems(data);

        Button btnNuevo = new Button("Nuevo");
        Button btnEditar = new Button("Editar");
        Button btnEliminar = new Button("Eliminar");
        Button btnRefrescar = new Button("Refrescar");

        btnEditar.setDisable(!isAdmin);
        btnEliminar.setDisable(!isAdmin);
        btnNuevo.setDisable(!isAdmin);

        HBox controls = new HBox(10, btnNuevo, btnEditar, btnEliminar, btnRefrescar);
        controls.setPadding(new Insets(10));

        btnRefrescar.setOnAction(e -> data.setAll(repartidorService.listarTodos()));

        btnNuevo.setOnAction(e -> {
            RepartidorFormDialog d = new RepartidorFormDialog(null);
            Optional<Repartidor> r = d.showAndWait();
            r.ifPresent(repartidorService::registrarRepartidor);
            data.setAll(repartidorService.listarTodos());
        });

        btnEditar.setOnAction(e -> {
            Repartidor sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) return;
            RepartidorFormDialog d = new RepartidorFormDialog(sel);
            Optional<Repartidor> r = d.showAndWait();
            r.ifPresent(nuevo -> {
                sel.setNombre(nuevo.getNombre());
                sel.setTelefono(nuevo.getTelefono());
                sel.setZona(nuevo.getZona());
                sel.setDisponibilidad(nuevo.getDisponibilidad());
                repartidorService.actualizarRepartidor(sel);
                data.setAll(repartidorService.listarTodos());
            });
        });

        btnEliminar.setOnAction(e -> {
            Repartidor sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                repartidorService.eliminarRepartidor(sel.getIdRepartidor());
                data.setAll(repartidorService.listarTodos());
            }
        });

        root.setCenter(table);
        root.setBottom(controls);
        tab.setContent(root);
        return tab;
    }
}
