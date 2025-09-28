package co.edu.uniquindio.proyecto_final.controller;

import co.edu.uniquindio.proyecto_final.dto.EnvioDTO;
import co.edu.uniquindio.proyecto_final.model.Envio;
import co.edu.uniquindio.proyecto_final.model.Usuario;
import co.edu.uniquindio.proyecto_final.singleton.ServiceLocator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class EnviosListController {

    @FXML private TableView<EnvioDTO> table;
    @FXML private TableColumn<EnvioDTO, String> colId, colOrigen, colDestino, colEstado;
    @FXML private TableColumn<EnvioDTO, Number> colPeso, colCosto;
    @FXML private Button btnExport;
    @FXML private Button btnVolver;

    private ServiceLocator sl = ServiceLocator.getInstance();
    private Usuario usuario; // opcional si se filtra

    public void setUsuario(Usuario u) { this.usuario = u; loadData(); }
    @FXML
    private void initialize() {
        colId.setCellValueFactory(d -> javafx.beans.property.SimpleStringProperty.stringExpression(new javafx.beans.property.SimpleStringProperty(d.getValue().getId())));
        colOrigen.setCellValueFactory(d -> javafx.beans.property.SimpleStringProperty.stringExpression(new javafx.beans.property.SimpleStringProperty(d.getValue().getOrigen())));
        colDestino.setCellValueFactory(d -> javafx.beans.property.SimpleStringProperty.stringExpression(new javafx.beans.property.SimpleStringProperty(d.getValue().getDestino())));
        colEstado.setCellValueFactory(d -> javafx.beans.property.SimpleStringProperty.stringExpression(new javafx.beans.property.SimpleStringProperty(d.getValue().getEstado())));
        colPeso.setCellValueFactory(d -> new javafx.beans.property.SimpleDoubleProperty(d.getValue().getPeso()));
        colCosto.setCellValueFactory(d -> new javafx.beans.property.SimpleDoubleProperty(d.getValue().getCosto()));
    }

    private void loadData() {
        List<Envio> envs = usuario == null ? sl.envioService.listarTodos() : sl.envioService.listarPorUsuario(usuario.getId());
        List<EnvioDTO> dtos = envs.stream().map(e -> {
            EnvioDTO dto = new EnvioDTO();
            dto.setId(e.getId());
            dto.setOrigen(e.getOrigen().toString());
            dto.setDestino(e.getDestino().toString());
            dto.setPeso(e.getPeso());
            dto.setCosto(e.getCosto());
            dto.setEstado(e.getEstado().name());
            dto.setFecha(e.getFechaCreacion());
            return dto;
        }).collect(Collectors.toList());
        table.setItems(FXCollections.observableArrayList(dtos));
    }

    @FXML
    public void onExport() {
        FileChooser fc = new FileChooser();
        fc.setInitialFileName("envios.csv");
        File file = fc.showSaveDialog(btnExport.getScene().getWindow());
        if (file != null) {
            try {
                var exporter = co.edu.uniquindio.proyecto_final.factory.ReportFactory.getExporter("CSV");
                exporter.export(table.getItems(), file);
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Exportado: " + file.getAbsolutePath(), ButtonType.OK);
                a.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    public void onVolver() {
        try {
            btnVolver.getScene().setRoot(javafx.fxml.FXMLLoader.load(getClass().getResource("/view/user_dashboard.fxml")));
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}
