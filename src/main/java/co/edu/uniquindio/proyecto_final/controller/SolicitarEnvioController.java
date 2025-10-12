package co.edu.uniquindio.proyecto_final.controller;

import co.edu.uniquindio.proyecto_final.builder.EnvioBuilder;
import co.edu.uniquindio.proyecto_final.model.Direccion;
import co.edu.uniquindio.proyecto_final.model.Envio;
import co.edu.uniquindio.proyecto_final.model.Usuario;
import co.edu.uniquindio.proyecto_final.singleton.ServiceLocator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SolicitarEnvioController {

    @FXML private TextField origenAlias;
    @FXML private TextField origenCalle;
    @FXML private TextField origenCiudad;
    @FXML private TextField destinoAlias;
    @FXML private TextField destinoCalle;
    @FXML private TextField destinoCiudad;
    @FXML private TextField pesoField;
    @FXML private TextField volumenField;
    @FXML private CheckBox prioridadCheck;
    @FXML private Button btnEnviar;
    @FXML private Label lblMsg;

    private final ServiceLocator sl = ServiceLocator.getInstance();
    private Usuario usuario;

    public void setUsuario(Usuario u) { this.usuario = u; }

    @FXML
    public void onEnviar(ActionEvent e) {
        try {
            Direccion o = new Direccion(origenAlias.getText(), origenCalle.getText(), origenCiudad.getText());
            Direccion d = new Direccion(destinoAlias.getText(), destinoCalle.getText(), destinoCiudad.getText());
            double peso = Double.parseDouble(pesoField.getText());
            double volumen = Double.parseDouble(volumenField.getText());
            boolean prioridad = prioridadCheck.isSelected();

            Envio envio = new EnvioBuilder()
                    .origen(o)
                    .destino(d)
                    .peso(peso)
                    .volumen(volumen)
                    .prioridad(prioridad)
                    .build();

            envio.setUsuarioId(usuario.getId());

            sl.envioService.crear(envio);
            lblMsg.setText("Envío creado. Costo: " + (int)envio.getCosto());
            sl.envioService.asignarRepartidor(envio);
        } catch (Exception ex) {
            lblMsg.setText("Error: datos inválidos");
        }
    }

    @FXML
    public void onVolver() {
        Stage st = (Stage) btnEnviar.getScene().getWindow();
        try {
            st.setScene(new javafx.scene.Scene(javafx.fxml.FXMLLoader.load(getClass().getResource("/view/user_dashboard.fxml"))));
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}
