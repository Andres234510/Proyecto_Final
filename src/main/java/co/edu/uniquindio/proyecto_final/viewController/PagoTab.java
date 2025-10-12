package co.edu.uniquindio.proyecto_final.viewController;

import co.edu.uniquindio.proyecto_final.model.Pago;
import co.edu.uniquindio.proyecto_final.service.PagoService;
import co.edu.uniquindio.proyecto_final.singleton.DataStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.Optional;

public class PagoTab {

    public static Tab createTab() {
        Tab tab = new Tab("Pagos");
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        PagoService service = DataStore.getInstance().getPagoService();

        TableView<Pago> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Pago, String> idCol = new TableColumn<>("ID Pago");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idPago"));
        TableColumn<Pago, String> envioCol = new TableColumn<>("ID Envío");
        envioCol.setCellValueFactory(new PropertyValueFactory<>("idEnvio"));
        TableColumn<Pago, Double> montoCol = new TableColumn<>("Monto");
        montoCol.setCellValueFactory(new PropertyValueFactory<>("monto"));
        TableColumn<Pago, String> metodoCol = new TableColumn<>("Método");
        metodoCol.setCellValueFactory(new PropertyValueFactory<>("metodo"));

        table.getColumns().addAll(idCol, envioCol, montoCol, metodoCol);

        ObservableList<Pago> items = FXCollections.observableArrayList(service.listarTodos());
        table.setItems(items);

        Button btnNuevo = new Button("Nuevo");
        Button btnEliminar = new Button("Eliminar");
        Button btnRefrescar = new Button("Refrescar");

        HBox controls = new HBox(10, btnNuevo, btnEliminar, btnRefrescar);
        controls.setPadding(new Insets(10));

        btnRefrescar.setOnAction(e -> items.setAll(service.listarTodos()));

        btnNuevo.setOnAction(e -> {
            PagoFormDialog dialog = new PagoFormDialog();
            Optional<Pago> res = dialog.showAndWait();
            res.ifPresent(p -> {
                service.crearPago(p);
                items.setAll(service.listarTodos());
            });
        });

        btnEliminar.setOnAction(e -> {
            Pago sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { Alerts.showWarning("Selecciona un pago"); return; }
            boolean ok = Alerts.confirm("Eliminar pago", "¿Eliminar pago " + sel.getIdPago() + "?");
            if (ok) {
                service.eliminarPago(sel.getIdPago());
                items.setAll(service.listarTodos());
            }
        });

        root.setCenter(table);
        root.setBottom(controls);
        tab.setContent(root);
        return tab;
    }
}
