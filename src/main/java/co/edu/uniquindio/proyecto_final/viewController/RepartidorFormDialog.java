package co.edu.uniquindio.proyecto_final.viewController;

import co.edu.uniquindio.proyecto_final.model.Disponibilidad;
import co.edu.uniquindio.proyecto_final.model.Repartidor;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class RepartidorFormDialog extends Dialog<Repartidor> {

    private TextField txtNombre = new TextField();
    private TextField txtDocumento = new TextField();
    private TextField txtTelefono = new TextField();
    private TextField txtZona = new TextField();
    private ComboBox<Disponibilidad> cbDisponibilidad = new ComboBox<>();

    public RepartidorFormDialog(Repartidor r) {
        setTitle(r == null ? "Nuevo Repartidor" : "Editar Repartidor");
        setHeaderText(null);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(8); g.setVgap(8); g.setPadding(new Insets(10));

        g.add(new Label("Nombre:"), 0, 0);
        g.add(txtNombre, 1, 0);
        g.add(new Label("Documento:"), 0, 1);
        g.add(txtDocumento, 1, 1);
        g.add(new Label("Teléfono:"), 0, 2);
        g.add(txtTelefono, 1, 2);
        g.add(new Label("Zona:"), 0, 3);
        g.add(txtZona, 1, 3);
        g.add(new Label("Disponibilidad:"), 0, 4);
        cbDisponibilidad.getItems().addAll(Disponibilidad.ACTIVO, Disponibilidad.INACTIVO, Disponibilidad.EN_RUTA);
        g.add(cbDisponibilidad, 1, 4);

        if (r != null) {
            txtNombre.setText(r.getNombre());
            txtDocumento.setText(r.getDocumento());
            txtTelefono.setText(r.getTelefono());
            txtZona.setText(r.getZona());
            cbDisponibilidad.getSelectionModel().select(r.getDisponibilidad());
        }

        getDialogPane().setContent(g);
        setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                Repartidor rep = r == null ? new Repartidor(txtNombre.getText(), txtDocumento.getText(), txtTelefono.getText(), cbDisponibilidad.getValue(), txtZona.getText()) : r;
                rep.setNombre(txtNombre.getText());
                rep.setDocumento(txtDocumento.getText());
                rep.setTelefono(txtTelefono.getText());
                rep.setZona(txtZona.getText());
                rep.setDisponibilidad(cbDisponibilidad.getValue());
                return rep;
            }
            return null;
        });
    }
}
