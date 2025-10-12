package co.edu.uniquindio.proyecto_final.viewController;

import co.edu.uniquindio.proyecto_final.singleton.DataStore;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminViewController extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Panel Administrador - Plataforma Logística");

        Label lbl = new Label("Panel Administrador - demo de métricas");
        Button btnMetrics = new Button("Mostrar métricas (demo)");
        TextArea area = new TextArea();

        btnMetrics.setOnAction(e -> {
            var envios = DataStore.getInstance().getEnvioRepo().findAll();
            long entregados = envios.stream().filter(x -> x.getEstado().toString().equals("ENTREGADO")).count();
            area.setText("Total envíos: " + envios.size() + "\nEntregados: " + entregados);
        });

        VBox root = new VBox(8, lbl, btnMetrics, area);
        root.setPadding(new Insets(10));
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }
}
