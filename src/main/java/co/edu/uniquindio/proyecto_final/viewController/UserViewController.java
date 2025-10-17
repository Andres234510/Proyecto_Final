package co.edu.uniquindio.proyecto_final.viewController;

import co.edu.uniquindio.proyecto_final.singleton.DataStore;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserViewController extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Panel Usuario - Plataforma Logística");

        Label lbl = new Label("Panel de Usuario - funcionalidad demo");
        Button btnListarEnvios = new Button("Listar envíos existentes");

        TextArea area = new TextArea();
        area.setPrefRowCount(15);

        btnListarEnvios.setOnAction(e -> {
            var envios = DataStore.getInstance().getEnvioRepo().findAll();
            StringBuilder sb = new StringBuilder();
            envios.forEach(en -> sb.append(en.getIdEnvio()).append(" - ").append(en.getEstado()).
                    append("\n"));
            area.setText(sb.toString());
        });

        VBox root = new VBox(8, lbl, btnListarEnvios, area);
        root.setPadding(new Insets(10));
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }
}
