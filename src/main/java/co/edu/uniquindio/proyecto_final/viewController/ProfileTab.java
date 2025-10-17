package co.edu.uniquindio.proyecto_final.viewController;

import co.edu.uniquindio.proyecto_final.model.Usuario;
import co.edu.uniquindio.proyecto_final.singleton.DataStore;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

public class ProfileTab {

    public static Tab createTab() {
        Tab tab = new Tab("Perfil");
        VBox v = new VBox(10);
        v.setPadding(new Insets(20));
        Usuario u = DataStore.getInstance().getCurrentUser();
        if (u != null) {
            Label name = new Label("Nombre: " + u.getNombre());
            Label email = new Label("Correo: " + u.getCorreo());
            Label phone = new Label("Teléfono: " + u.getTelefono());
            v.getChildren().addAll(name, email, phone);
        } else {
            v.getChildren().add(new Label("Usuario no autenticado"));
        }
        tab.setContent(v);
        return tab;
    }
}
