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

    public static Tab createTab(boolean isAdmin) {
        Tab tab = new Tab("Pagos");
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        PagoService pagoService = DataStore.getInstance().getPagoService();

        TableView<Pago> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Pago, String> colId = new TableColumn<>("ID Pago");
        colId.setCellValueFactory(new PropertyValueFactory<>("idPago"));
        TableColumn<Pago, String> colEnvio = new TableColumn<>("ID Envío");
        colEnvio.setCellValueFactory(new PropertyValueFactory<>("idEnvio"));
        TableColumn<Pago, Double> colMonto = new TableColumn<>("Monto");
        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        TableColumn<Pago, String> colMetodo = new TableColumn<>("Método");
        colMetodo.setCellValueFactory(new PropertyValueFactory<>("metodo"));
        TableColumn<Pago, Boolean> colConfirm = new TableColumn<>("Resultado");
        colConfirm.setCellValueFactory(new PropertyValueFactory<>("confirmado"));

        table.getColumns().addAll(colId, colEnvio, colMonto, colMetodo, colConfirm);

        ObservableList<Pago> data = FXCollections.observableArrayList(pagoService.listarTodos());
        table.setItems(data);

        Button btnNuevo = new Button("Nuevo");
        Button btnEditar = new Button("Editar");
        Button btnEliminar = new Button("Eliminar");
        Button btnRefrescar = new Button("Refrescar");

        btnEditar.setDisable(!isAdmin);
        btnEliminar.setDisable(!isAdmin);

        HBox controls = new HBox(10, btnNuevo, btnEditar, btnEliminar, btnRefrescar);
        controls.setPadding(new Insets(10));

        btnRefrescar.setOnAction(e -> data.setAll(pagoService.listarTodos()));

        btnNuevo.setOnAction(e -> {
            PagoFormDialog d = new PagoFormDialog(null);
            Optional<Pago> res = d.showAndWait();
            res.ifPresent(pagoService::crearPago);
            data.setAll(pagoService.listarTodos());
        });

        btnEditar.setOnAction(e -> {
            Pago sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) return;
            PagoFormDialog d = new PagoFormDialog(sel);
            Optional<Pago> res = d.showAndWait();
            res.ifPresent(p -> {
                sel.setMetodo(p.getMetodo());
                sel.setMonto(p.getMonto());
                sel.setResultado(p.getResultado());
                pagoService.actualizarPago(sel);
                data.setAll(pagoService.listarTodos());
            });
        });

        btnEliminar.setOnAction(e -> {
            Pago sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                pagoService.eliminarPago(sel.getIdPago());
                data.setAll(pagoService.listarTodos());
            }
        });

        root.setCenter(table);
        root.setBottom(controls);
        tab.setContent(root);
        return tab;
    }
}
