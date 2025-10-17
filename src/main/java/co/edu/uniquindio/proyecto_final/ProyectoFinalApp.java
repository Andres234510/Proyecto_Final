package co.edu.uniquindio.proyecto_final;

import co.edu.uniquindio.proyecto_final.model.Usuario;
import co.edu.uniquindio.proyecto_final.singleton.DataStore;
import co.edu.uniquindio.proyecto_final.viewController.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;

public class ProyectoFinalApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        DataStore ds = DataStore.getInstance();
        ds.initSampleData();

        // Mostrar login modal
        LoginView loginView = new LoginView();
        Optional<Usuario> logged = loginView.showAndWait();

        if (logged.isEmpty()) {
            System.exit(0);
            return;
        }

        Usuario user = logged.get();
        primaryStage.setTitle("Plataforma - Usuario: " + user.getNombre());

        TabPane tabPane = new TabPane();
        boolean isAdmin = user.isAdmin();

        if (isAdmin) {
            tabPane.getTabs().add(UserTab.createTab(true));
            tabPane.getTabs().add(EnvioTab.createTab(true));
            tabPane.getTabs().add(RepartidorTab.createTab(true));
            tabPane.getTabs().add(PagoTab.createTab(true));
            tabPane.getTabs().add(DireccionTab.createTab(true));
        } else {
            tabPane.getTabs().add(EnvioTab.createTab(false));
            tabPane.getTabs().add(ProfileTab.createTab());
        }

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Scene scene = new Scene(tabPane, 1000, 700);

        String cssPath = "/co/edu/uniquindio/proyecto_final/css/style.css";
        URL cssUrl = getClass().getResource(cssPath);

        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("⚠️ No se encontró " + cssPath + ". Verifica src/main/resources.");
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
