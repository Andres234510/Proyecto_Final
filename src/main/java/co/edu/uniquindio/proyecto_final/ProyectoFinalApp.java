package co.edu.uniquindio.proyecto_final;

import co.edu.uniquindio.proyecto_final.singleton.DataStore;
import co.edu.uniquindio.proyecto_final.viewController.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class ProyectoFinalApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        DataStore ds = DataStore.getInstance();
        ds.initSampleData();

        primaryStage.setTitle("Plataforma de Logística - Panel (Tabs)");

        TabPane tabPane = new TabPane();

        Tab usuariosTab = UserTab.createTab();
        Tab enviosTab   = EnvioTab.createTab();
        Tab repartidoresTab = RepartidorTab.createTab();
        Tab pagosTab    = PagoTab.createTab();
        Tab direccionesTab = DireccionTab.createTab();

        tabPane.getTabs().addAll(usuariosTab, enviosTab, repartidoresTab, pagosTab, direccionesTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Scene scene = new Scene(tabPane, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
