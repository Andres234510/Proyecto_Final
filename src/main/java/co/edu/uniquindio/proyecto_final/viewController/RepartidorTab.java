package co.edu.uniquindio.proyecto_final.viewController;

import co.edu.uniquindio.proyecto_final.model.Disponibilidad;
import co.edu.uniquindio.proyecto_final.model.Repartidor;
import co.edu.uniquindio.proyecto_final.service.RepartidorService;
import co.edu.uniquindio.proyecto_final.singleton.DataStore;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.Optional;

public class RepartidorTab {

    public static Tab createTab() {
        Tab tab = new Tab("Repartidores");
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        RepartidorService service = DataStore.getInstance().getRepartidorService();

        TableView<Repartidor> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Repartidor, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idRepartidor"));
        TableColumn<Repartidor, String> nameCol = new TableColumn<>("Nombre");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<Repartidor, String> telCol = new TableColumn<>("Teléfono");
        telCol.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        TableColumn<Repartidor, String> zonaCol = new TableColumn<>("Zona");
        zonaCol.setCellValueFactory(new PropertyValueFactory<>("zona"));
        TableColumn<Repartidor, String> dispCol = new TableColumn<>("Disponibilidad");
        dispCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getDisponibilidad().toString()));

        table.getColumns().addAll(idCol, nameCol, telCol, zonaCol, dispCol);

        ObservableList<Repartidor> items = FXCollections.observableArrayList(service.listarTodos());
        table.setItems(items);

        Button btnNuevo = new Button("Nuevo");
        Button btnEditar = new Button("Editar");
        Button btnEliminar = new Button("Eliminar");
        Button btnRefrescar = new Button("Refrescar");

        HBox controls = new HBox(10, btnNuevo, btnEditar, btnEliminar, btnRefrescar);
        controls.setPadding(new Insets(10));

        btnRefrescar.setOnAction(e -> items.setAll(service.listarTodos()));

        btnNuevo.setOnAction(e -> {
            RepartidorFormDialog dialog = new RepartidorFormDialog(null);
            Optional<Repartidor> res = dialog.showAndWait();
            res.ifPresent(r -> {
                service.crearRepartidor(r.getNombre(), r.getDocumento(), r.getTelefono(), r.getDisponibilidad(), r.getZona());
                items.setAll(service.listarTodos());
            });
        });

        btnEditar.setOnAction(e -> {
            Repartidor sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { Alerts.showWarning("Selecciona un repartidor"); return; }
            RepartidorFormDialog dialog = new RepartidorFormDialog(sel);
            dialog.showAndWait().ifPresent(r -> {
                sel.setNombre(r.getNombre());
                sel.setTelefono(r.getTelefono());
                sel.setZona(r.getZona());
                sel.setDisponibilidad(r.getDisponibilidad());
                service.registrarRepartidor(sel);
                items.setAll(service.listarTodos());
            });
        });

        btnEliminar.setOnAction(e -> {
            Repartidor sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { Alerts.showWarning("Selecciona un repartidor"); return; }
            boolean ok = Alerts.confirm("Eliminar repartidor", "¿Eliminar repartidor " + sel.getNombre() + "?");
            if (ok) {
                DataStore.getInstance().getRepartidorRepo().delete(sel.getIdRepartidor());
                items.setAll(service.listarTodos());
            }
        });

        root.setCenter(table);
        root.setBottom(controls);
        tab.setContent(root);
        return tab;
    }
}
