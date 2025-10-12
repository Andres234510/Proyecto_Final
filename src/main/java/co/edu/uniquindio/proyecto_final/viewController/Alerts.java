package co.edu.uniquindio.proyecto_final.viewController;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Alerts {

    public static void showWarning(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.showAndWait();
    }

    public static void showInfo(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setContentText(msg);
        a.showAndWait();
    }

    public static boolean confirm(String title, String message) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        a.setTitle(title);
        return a.showAndWait().filter(b -> b == ButtonType.YES).isPresent();
    }
}
