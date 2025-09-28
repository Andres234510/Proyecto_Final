module co.edu.uniquindio.proyecto_final {
    requires javafx.controls;
    requires javafx.fxml;

    // Abrimos los paquetes que contienen controladores y clases que FXMLLoader necesita instanciar por reflexión
    opens co.edu.uniquindio.proyecto_final.controller to javafx.fxml;
    opens co.edu.uniquindio.proyecto_final.model to javafx.fxml;
    opens co.edu.uniquindio.proyecto_final.dto to javafx.fxml;
    opens co.edu.uniquindio.proyecto_final.builder to javafx.fxml;
    opens co.edu.uniquindio.proyecto_final.decorator to javafx.fxml;
    opens co.edu.uniquindio.proyecto_final.factory to javafx.fxml;
    opens co.edu.uniquindio.proyecto_final.singleton to javafx.fxml;
    opens co.edu.uniquindio.proyecto_final.util to javafx.fxml;

    // Abrimos el paquete raiz al runtime gráfico (si tu clase principal está aquí)
    opens co.edu.uniquindio.proyecto_final to javafx.graphics, javafx.fxml;

    // Exporta los paquetes públicos que quieras exponer (opcional)
    exports co.edu.uniquindio.proyecto_final;
    exports co.edu.uniquindio.proyecto_final.controller;
    exports co.edu.uniquindio.proyecto_final.model;
}
