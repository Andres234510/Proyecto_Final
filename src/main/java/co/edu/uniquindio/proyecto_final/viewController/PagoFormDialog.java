package co.edu.uniquindio.proyecto_final.viewController;

import co.edu.uniquindio.proyecto_final.model.Pago;
import co.edu.uniquindio.proyecto_final.model.ResultadoPago;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class PagoFormDialog extends Dialog<Pago> {

    private TextField txtIdEnvio = new TextField();
    private TextField txtMonto = new TextField();
    private TextField txtMetodo = new TextField();
    private ComboBox<ResultadoPago> cbResultado = new ComboBox<>();

    public PagoFormDialog() {
        setTitle("Nuevo Pago");
        setHeaderText(null);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(8); g.setVgap(8); g.setPadding(new Insets(10));
        g.add(new Label("ID Envío:"), 0, 0);
        g.add(txtIdEnvio, 1, 0);
        g.add(new Label("Monto:"), 0, 1);
        g.add(txtMonto, 1, 1);
        g.add(new Label("Método:"), 0, 2);
        g.add(txtMetodo, 1, 2);
        g.add(new Label("Resultado:"), 0, 3);
        cbResultado.getItems().addAll(ResultadoPago.APROBADO, ResultadoPago.RECHAZADO);
        g.add(cbResultado, 1, 3);

        getDialogPane().setContent(g);
        setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                double monto = 0;
                try { monto = Double.parseDouble(txtMonto.getText()); } catch (Exception ignored) {}
                ResultadoPago res = cbResultado.getValue() != null ? cbResultado.getValue() : ResultadoPago.APROBADO;
                return new Pago(txtIdEnvio.getText(), monto, txtMetodo.getText(), res);
            }
            return null;
        });
    }
}
