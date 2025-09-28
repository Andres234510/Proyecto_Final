package co.edu.uniquindio.proyecto_final;

import co.edu.uniquindio.proyecto_final.singleton.DataInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

public class ProyectoFinalApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Inicializar datos y servicios
        DataInitializer.init();

        // Buscamos el FXML en varias formas (más tolerante y con diagnóstico)
        String[] candidates = { "/view/login.fxml", "view/login.fxml", "/login.fxml", "login.fxml" };
        URL fxmlUrl = null;
        for (String c : candidates) {
            // probamos con la clase y con el classloader del hilo
            fxmlUrl = ProyectoFinalApp.class.getResource(c);
            if (fxmlUrl == null) fxmlUrl = Thread.currentThread().getContextClassLoader().getResource(c.startsWith("/") ? c.substring(1) : c);
            if (fxmlUrl != null) {
                System.out.println("[INFO] FXML encontrado en: " + c + " -> " + fxmlUrl);
                break;
            }
        }

        if (fxmlUrl == null) {
            // diagnóstico: imprimir classpath para ayudar a encontrar por qué no está
            String classpath = System.getProperty("java.class.path");
            String msg = "No se pudo localizar el FXML 'login.fxml'.\n" +
                    "Se buscaron: /view/login.fxml, view/login.fxml, /login.fxml, login.fxml\n" +
                    "Comprueba que exista en src/main/resources/view/login.fxml y que esté siendo copiado al classpath/target/classes.\n" +
                    "Classpath actual:\n" + classpath;
            System.err.println(msg);
            throw new IllegalStateException(msg);
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(loader.load());
        // Añade CSS si lo tienes en resources
        URL cssUrl = ProyectoFinalApp.class.getResource("/css/style.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.out.println("[WARN] style.css no encontrado en /css/style.css (opcional).");
        }

        primaryStage.setTitle("Plataforma de Logística");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
