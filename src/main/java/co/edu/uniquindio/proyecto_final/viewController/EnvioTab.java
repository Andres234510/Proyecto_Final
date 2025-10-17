package co.edu.uniquindio.proyecto_final.viewController;

import co.edu.uniquindio.proyecto_final.model.Envio;
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
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EnvioTab {


    public static Tab createTab(boolean isAdmin) {
        Tab tab = new Tab(isAdmin ? "Envíos" : "Mis envíos");
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        EnvioService envioService = DataStore.getInstance().getEnvioService();

        TableView<Envio> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Envio, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idEnvio"));
        TableColumn<Envio, String> usuarioCol = new TableColumn<>("Usuario");
        usuarioCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getUsuario() != null ? cell.getValue().getUsuario().getNombre() : "N/A"));
        TableColumn<Envio, String> estadoCol = new TableColumn<>("Estado");
        estadoCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getEstado() != null ? cell.getValue().getEstado().toString() : ""));
        TableColumn<Envio, Double> pesoCol = new TableColumn<>("Peso");
        pesoCol.setCellValueFactory(new PropertyValueFactory<>("peso"));
        TableColumn<Envio, Double> costoCol = new TableColumn<>("Costo");
        costoCol.setCellValueFactory(new PropertyValueFactory<>("costo"));

        table.getColumns().addAll(idCol, usuarioCol, estadoCol, pesoCol, costoCol);

        List<Envio> lista = envioService.listarTodos();
        if (!isAdmin) {
            Usuario current = DataStore.getInstance().getCurrentUser();
            lista = lista.stream().filter(e -> e.getUsuario() != null && current != null && e.getUsuario().getIdUsuario().equals(current.getIdUsuario())).collect(Collectors.toList());
        }

        ObservableList<Envio> items = FXCollections.observableArrayList(lista);
        table.setItems(items);

        Button btnNuevo = new Button("Nuevo");
        Button btnEditar = new Button("Editar");
        Button btnEliminar = new Button("Eliminar");
        Button btnAsignar = new Button("Asignar");
        Button btnRefrescar = new Button("Refrescar");

        btnEditar.setDisable(!isAdmin);
        btnEliminar.setDisable(!isAdmin);
        btnAsignar.setDisable(!isAdmin);

        HBox controls = new HBox(10, btnNuevo, btnEditar, btnEliminar, btnAsignar, btnRefrescar);
        controls.setPadding(new Insets(10));

        btnRefrescar.setOnAction(e -> {
            List<Envio> refreshed = envioService.listarTodos();
            if (!isAdmin) {
                Usuario cur = DataStore.getInstance().getCurrentUser();
                refreshed = refreshed.stream().filter(ev -> ev.getUsuario()!=null && ev.getUsuario().getIdUsuario().equals(cur.getIdUsuario())).collect(Collectors.toList());
            }
            items.setAll(refreshed);
        });

        btnNuevo.setOnAction(e -> {
            EnvioFormDialog d = new EnvioFormDialog(null);
            Optional<Envio> r = d.showAndWait();
            r.ifPresent(en -> {
                if (!isAdmin) en.setUsuario(DataStore.getInstance().getCurrentUser());
                envioService.crearEnvio(en);
                items.setAll(envioService.listarTodos().stream().filter(ev -> isAdmin || (ev.getUsuario()!=null && ev.getUsuario().getIdUsuario().equals(DataStore.getInstance().getCurrentUser().getIdUsuario()))).collect(Collectors.toList()));
            });
        });

        root.setCenter(table);
        root.setBottom(controls);
        tab.setContent(root);
        return tab;
    }
}
