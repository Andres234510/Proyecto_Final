package co.edu.uniquindio.proyecto_final.viewController;

import co.edu.uniquindio.proyecto_final.model.*;
import co.edu.uniquindio.proyecto_final.singleton.DataStore;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.stream.Collectors;

public class EnvioFormDialog extends Dialog<Envio> {

    private ComboBox<Usuario> cbUsuario = new ComboBox<>();
    private ComboBox<Direccion> cbOrigen = new ComboBox<>();
    private ComboBox<Direccion> cbDestino = new ComboBox<>();
    private TextField txtPeso = new TextField();
    private TextField txtVolumen = new TextField();
    private CheckBox chkPrioridad = new CheckBox("Prioridad");
    private CheckBox chkSeguro = new CheckBox("Seguro");

    public EnvioFormDialog(Envio envio) {
        setTitle(envio == null ? "Nuevo Envío" : "Editar Envío");
        setHeaderText(null);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(8);
        g.setVgap(8);
        g.setPadding(new Insets(10));

        DataStore ds = DataStore.getInstance();
        List<Usuario> usuarios = ds.getUsuarioService().listarTodos();

        cbUsuario.setItems(FXCollections.observableArrayList(usuarios));
        cbUsuario.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Usuario item, boolean empty) {
                super.updateItem(item, empty); setText(empty || item == null ? null : item.getNombre());
            }
        });
        cbUsuario.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(Usuario item, boolean empty) {
                super.updateItem(item, empty); setText(empty || item == null ? null : item.getNombre());
            }
        });

        List<Direccion> direcciones = ds.getDireccionService().listarTodos();
        cbOrigen.setItems(FXCollections.observableArrayList(direcciones));
        cbDestino.setItems(FXCollections.observableArrayList(direcciones));
        cbOrigen.setCellFactory(lv -> new ListCell<>() { @Override protected void updateItem(Direccion item, boolean empty) { super.updateItem(item, empty); setText(empty||item==null?null: item.getAlias()); }});
        cbDestino.setCellFactory(cbOrigen.getCellFactory());
        cbOrigen.setButtonCell(cbOrigen.getCellFactory().call(null));
        cbDestino.setButtonCell(cbDestino.getCellFactory().call(null));

        g.add(new Label("Usuario:"), 0, 0);
        g.add(cbUsuario, 1, 0);
        g.add(new Label("Origen:"), 0, 1);
        g.add(cbOrigen, 1, 1);
        g.add(new Label("Destino:"), 0, 2);
        g.add(cbDestino, 1, 2);
        g.add(new Label("Peso (kg):"), 0, 3);
        g.add(txtPeso, 1, 3);
        g.add(new Label("Volumen (m3):"), 0, 4);
        g.add(txtVolumen, 1, 4);
        g.add(chkPrioridad, 0, 5);
        g.add(chkSeguro, 1, 5);

        if (envio != null) {
            cbUsuario.getSelectionModel().select(envio.getUsuario());
            cbOrigen.getSelectionModel().select(envio.getOrigen());
            cbDestino.getSelectionModel().select(envio.getDestino());
            txtPeso.setText(String.valueOf(envio.getPeso()));
            txtVolumen.setText(String.valueOf(envio.getVolumen()));
            chkPrioridad.setSelected(envio.getServicios().contains(ServicioAdicional.PRIORIDAD));
            chkSeguro.setSelected(envio.getServicios().contains(ServicioAdicional.SEGURO));
        }

        getDialogPane().setContent(g);
        setResultConverter(b -> {
            if (b == ButtonType.OK) {
                Envio e = envio == null ? new Envio() : envio;
                e.setUsuario(cbUsuario.getSelectionModel().getSelectedItem());
                e.setOrigen(cbOrigen.getSelectionModel().getSelectedItem());
                e.setDestino(cbDestino.getSelectionModel().getSelectedItem());
                try { e.setPeso(Double.parseDouble(txtPeso.getText())); } catch (Exception ex) { e.setPeso(0); }
                try { e.setVolumen(Double.parseDouble(txtVolumen.getText())); } catch (Exception ex) { e.setVolumen(0); }
                if (chkPrioridad.isSelected()) e.addServicio(ServicioAdicional.PRIORIDAD);
                if (chkSeguro.isSelected()) e.addServicio(ServicioAdicional.SEGURO);
                return e;
            }
            return null;
        });
    }
}
