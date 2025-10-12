package co.edu.uniquindio.proyecto_final.viewController;

import co.edu.uniquindio.proyecto_final.model.Direccion;
import co.edu.uniquindio.proyecto_final.service.DireccionService;
import co.edu.uniquindio.proyecto_final.singleton.DataStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.Optional;

public class DireccionTab {

    public static Tab createTab() {
        Tab tab = new Tab("Direcciones");
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        DireccionService service = DataStore.getInstance().getDireccionService();

        TableView<Direccion> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Direccion, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idDireccion"));
        TableColumn<Direccion, String> aliasCol = new TableColumn<>("Alias");
        aliasCol.setCellValueFactory(new PropertyValueFactory<>("alias"));
        TableColumn<Direccion, String> calleCol = new TableColumn<>("Calle");
        calleCol.setCellValueFactory(new PropertyValueFactory<>("calle"));
        TableColumn<Direccion, String> ciudadCol = new TableColumn<>("Ciudad");
        ciudadCol.setCellValueFactory(new PropertyValueFactory<>("ciudad"));

        table.getColumns().addAll(idCol, aliasCol, calleCol, ciudadCol);

        ObservableList<Direccion> items = FXCollections.observableArrayList(service.listarTodos());
        table.setItems(items);

        Button btnNuevo = new Button("Nuevo");
        Button btnEditar = new Button("Editar");
        Button btnEliminar = new Button("Eliminar");
        Button btnRefrescar = new Button("Refrescar");

        HBox controls = new HBox(10, btnNuevo, btnEditar, btnEliminar, btnRefrescar);
        controls.setPadding(new Insets(10));

        btnRefrescar.setOnAction(e -> items.setAll(service.listarTodos()));

        btnNuevo.setOnAction(e -> {
            DireccionFormDialog dialog = new DireccionFormDialog(null);
            Optional<Direccion> res = dialog.showAndWait();
            res.ifPresent(d -> {
                service.crearDireccion(d);
                items.setAll(service.listarTodos());
            });
        });

        btnEditar.setOnAction(e -> {
            Direccion sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { Alerts.showWarning("Selecciona una dirección"); return; }
            DireccionFormDialog dialog = new DireccionFormDialog(sel);
            dialog.showAndWait().ifPresent(d -> {
                sel.setAlias(d.getAlias());
                sel.setCalle(d.getCalle());
                sel.setCiudad(d.getCiudad());
                sel.setLatitud(d.getLatitud());
                sel.setLongitud(d.getLongitud());
                service.actualizarDireccion(sel);
                items.setAll(service.listarTodos());
            });
        });

        btnEliminar.setOnAction(e -> {
            Direccion sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { Alerts.showWarning("Selecciona una dirección"); return; }
            boolean ok = Alerts.confirm("Eliminar dirección", "¿Eliminar dirección " + sel.getAlias() + "?");
            if (ok) {
                service.eliminarDireccion(sel.getIdDireccion());
                items.setAll(service.listarTodos());
            }
        });

        root.setCenter(table);
        root.setBottom(controls);
        tab.setContent(root);
        return tab;
    }
}
