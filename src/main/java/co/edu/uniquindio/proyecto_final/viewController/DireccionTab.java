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

    public static Tab createTab(boolean isAdmin) {
        Tab tab = new Tab("Direcciones");
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        DireccionService direccionService = DataStore.getInstance().getDireccionService();

        TableView<Direccion> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Direccion, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idDireccion"));
        TableColumn<Direccion, String> colAlias = new TableColumn<>("Alias");
        colAlias.setCellValueFactory(new PropertyValueFactory<>("alias"));
        TableColumn<Direccion, String> colDesc = new TableColumn<>("Descripción");
        colDesc.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        TableColumn<Direccion, Double> colLat = new TableColumn<>("Latitud");
        colLat.setCellValueFactory(new PropertyValueFactory<>("lat"));
        TableColumn<Direccion, Double> colLon = new TableColumn<>("Longitud");
        colLon.setCellValueFactory(new PropertyValueFactory<>("lon"));

        table.getColumns().addAll(colId, colAlias, colDesc, colLat, colLon);

        ObservableList<Direccion> data = FXCollections.observableArrayList(direccionService.listarTodos());
        table.setItems(data);

        Button btnNuevo = new Button("Nuevo");
        Button btnEditar = new Button("Editar");
        Button btnEliminar = new Button("Eliminar");
        Button btnRefrescar = new Button("Refrescar");

        btnEditar.setDisable(!isAdmin);
        btnEliminar.setDisable(!isAdmin);

        HBox controls = new HBox(10, btnNuevo, btnEditar, btnEliminar, btnRefrescar);
        controls.setPadding(new Insets(10));

        btnRefrescar.setOnAction(e -> data.setAll(direccionService.listarTodos()));

        btnNuevo.setOnAction(e -> {
            DireccionFormDialog d = new DireccionFormDialog(null);
            Optional<Direccion> res = d.showAndWait();
            res.ifPresent(direccionService::crearDireccion);
            data.setAll(direccionService.listarTodos());
        });

        btnEditar.setOnAction(e -> {
            Direccion sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) return;
            DireccionFormDialog d = new DireccionFormDialog(sel);
            Optional<Direccion> res = d.showAndWait();
            res.ifPresent(dir -> {
                sel.setAlias(dir.getAlias());
                sel.setLatitud(dir.getLatitud());
                sel.setLongitud(dir.getLongitud());
                direccionService.actualizarDireccion(sel);
                data.setAll(direccionService.listarTodos());
            });
        });

        btnEliminar.setOnAction(e -> {
            Direccion sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                direccionService.eliminarDireccion(sel.getIdDireccion());
                data.setAll(direccionService.listarTodos());
            }
        });

        root.setCenter(table);
        root.setBottom(controls);
        tab.setContent(root);
        return tab;
    }
}
