package co.edu.uniquindio.proyecto_final.viewController;

import co.edu.uniquindio.proyecto_final.model.Direccion;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class DireccionFormDialog extends Dialog<Direccion> {

    private TextField txtAlias = new TextField();
    private TextField txtCalle = new TextField();
    private TextField txtCiudad = new TextField();
    private TextField txtLat = new TextField();
    private TextField txtLon = new TextField();

    public DireccionFormDialog(Direccion d) {
        setTitle(d == null ? "Nueva dirección" : "Editar dirección");
        setHeaderText(null);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(8); g.setVgap(8); g.setPadding(new Insets(10));

        g.add(new Label("Alias:"), 0, 0); g.add(txtAlias, 1, 0);
        g.add(new Label("Calle:"), 0, 1); g.add(txtCalle, 1, 1);
        g.add(new Label("Ciudad:"), 0, 2); g.add(txtCiudad, 1, 2);
        g.add(new Label("Lat:"), 0, 3); g.add(txtLat, 1, 3);
        g.add(new Label("Lon:"), 0, 4); g.add(txtLon, 1, 4);

        if (d != null) {
            txtAlias.setText(d.getAlias());
            txtCalle.setText(d.getCalle());
            txtCiudad.setText(d.getCiudad());
            txtLat.setText(String.valueOf(d.getLatitud()));
            txtLon.setText(String.valueOf(d.getLongitud()));
        }

        getDialogPane().setContent(g);
        setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                double lat = 0, lon = 0;
                try { lat = Double.parseDouble(txtLat.getText()); } catch (Exception ignored) {}
                try { lon = Double.parseDouble(txtLon.getText()); } catch (Exception ignored) {}
                Direccion dir = d == null ? new Direccion(txtAlias.getText(), txtCalle.getText(), txtCiudad.getText(), lat, lon) : d;
                dir.setAlias(txtAlias.getText());
                dir.setCalle(txtCalle.getText());
                dir.setCiudad(txtCiudad.getText());
                dir.setLatitud(lat); dir.setLongitud(lon);
                return dir;
            }
            return null;
        });
    }
}
