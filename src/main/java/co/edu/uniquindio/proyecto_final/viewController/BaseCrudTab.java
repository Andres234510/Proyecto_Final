package co.edu.uniquindio.proyecto_final.viewController;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * BaseCrudTab<T>
 * - Tab genérico que contiene: tabla (con filtro y orden), área de formulario embebida (vacía por defecto),
 *   botones Nuevo / Guardar / Eliminar / Refrescar / Buscar.
 * - No asume nombres de servicios: recibe lambdas para listar/crear/actualizar/eliminar.
 *
 * Constructor params:
 *  title: texto de la pestaña
 *  clazzToColumns: callback para que la clase hija configure las columnas (TableView)
 *  entityToForm: la clase hija debe implementar 'loadToForm' y 'writeFromForm' (ver métodos protegidos)
 */
public abstract class BaseCrudTab<T> extends Tab {

    protected final TableView<T> tableView = new TableView<>();
    protected final ObservableList<T> masterData = FXCollections.observableArrayList();
    protected final FilteredList<T> filteredData = new FilteredList<>(masterData, p -> true);

    // Controls comunes
    protected final TextField txtFilter = new TextField();
    protected final Button btnNuevo = new Button("Nuevo");
    protected final Button btnGuardar = new Button("Guardar");
    protected final Button btnEliminar = new Button("Eliminar");
    protected final Button btnRefrescar = new Button("Refrescar");
    protected final Label lblStatus = new Label();

    // Service lambdas (inyectadas por la implementación concreta)
    private final Supplier<List<T>> supplierList;
    private final Function<T, T> createFunction;   // return created instance (or same)
    private final Function<T, T> updateFunction;   // return updated instance (or same)
    private final Consumer<T> deleteConsumer;

    /**
     * @param title           texto de la pestaña
     * @param supplierList    lambda que devuelve List<T> (datos actuales)
     * @param createFunction  lambda que crea T (por ejemplo: srv.crearX(..) o repo.save)
     * @param updateFunction  lambda que actualiza T (por ejemplo: srv.actualizarX(..))
     * @param deleteConsumer  lambda que elimina T (por ejemplo: srv.eliminarX(..))
     */
    protected BaseCrudTab(String title,
                          Supplier<List<T>> supplierList,
                          Function<T, T> createFunction,
                          Function<T, T> updateFunction,
                          Consumer<T> deleteConsumer) {
        super(title);
        this.supplierList = supplierList;
        this.createFunction = createFunction;
        this.updateFunction = updateFunction;
        this.deleteConsumer = deleteConsumer;

        buildLayout();
        attachHandlers();
    }

    // Layout: tabla arriba/izquierda, formulario a la derecha, botones abajo
    private void buildLayout() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Top: filtro + títulos
        HBox topBar = new HBox(8);
        txtFilter.setPromptText("Buscar...");
        topBar.getChildren().addAll(new Text(getText()), txtFilter, lblStatus);
        topBar.setPadding(new Insets(4, 0, 8, 0));

        // Center: tabla + formulario área (hbox)
        VBox leftBox = new VBox(8, tableView);
        leftBox.setVgrow(tableView, Priority.ALWAYS);
        leftBox.setPrefWidth(640);

        VBox formBox = new VBox(8); // la subclase añadirá los campos llamando buildForm(formBox)
        formBox.setPrefWidth(360);
        formBox.setPadding(new Insets(6));
        formBox.setStyle("-fx-background-color: #fbfbff; -fx-border-color: #ddd; -fx-border-radius: 8; -fx-background-radius: 8;");

        // subclase construye su formulario aquí
        buildForm(formBox);

        HBox center = new HBox(12, leftBox, formBox);
        center.setHgrow(leftBox, Priority.ALWAYS);

        // Bottom: botones
        HBox buttons = new HBox(8, btnNuevo, btnGuardar, btnEliminar, btnRefrescar);
        buttons.setPadding(new Insets(8));

        root.setTop(topBar);
        root.setCenter(center);
        root.setBottom(buttons);

        setContent(root);
    }

    private void attachHandlers() {
        // set items
        tableView.setItems(filteredData);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                loadToForm(newSel);
            } else {
                clearForm();
            }
        });

        // Filter
        txtFilter.textProperty().addListener((obs, oldV, newV) -> {
            String lower = newV == null ? "" : newV.toLowerCase();
            filteredData.setPredicate(item -> filterPredicate(item, lower));
        });

        // Buttons
        btnNuevo.setOnAction(ev -> {
            tableView.getSelectionModel().clearSelection();
            clearForm();
            lblStatus.setText("");
        });

        btnGuardar.setOnAction(ev -> {
            try {
                T model = createModelFromForm();
                T selected = tableView.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    // Create
                    T created = createFunction.apply(model);
                    safeAdd(created);
                    lblStatus.setText("Creado correctamente.");
                } else {
                    // Update - copy fields into selected, but we let subclass manage copy if needed
                    copyFormToModel(selected);
                    T updated = updateFunction.apply(selected);
                    // refresh list visually
                    tableView.refresh();
                    lblStatus.setText("Actualizado correctamente.");
                }
            } catch (RuntimeException ex) {
                showError("Error", ex.getMessage());
            }
        });

        btnEliminar.setOnAction(ev -> {
            T sel = tableView.getSelectionModel().getSelectedItem();
            if (sel == null) {
                showError("Eliminar", "Seleccione un registro para eliminar.");
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar el registro seleccionado?", ButtonType.YES, ButtonType.NO);
            confirm.setHeaderText(null);
            if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                deleteConsumer.accept(sel);
                safeRemove(sel);
                clearForm();
                lblStatus.setText("Eliminado.");
            }
        });

        btnRefrescar.setOnAction(ev -> refresh());

        // initial load
        refresh();
    }

    private void clearForm() {
    }

    // reload list from supplier
    protected void refresh() {
        List<T> list = supplierList.get();
        Platform.runLater(() -> {
            masterData.setAll(list);
            tableView.refresh();
        });
    }

    // utilities to add/remove safely
    private void safeAdd(T item) {
        if (item != null) Platform.runLater(() -> masterData.add(0, item));
        tableView.getSelectionModel().clearSelection();
    }

    private void safeRemove(T item) {
        Platform.runLater(() -> masterData.remove(item));
        tableView.getSelectionModel().clearSelection();
    }

    protected void showError(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.setTitle(title);
        a.showAndWait();
    }

    // ---------- Methods that subclasses must implement ----------
    /**
     * Construye el formulario: añade controles al pane 'formBox'.
     */
    protected abstract void buildForm(VBox formBox);

    /**
     * Dado un modelo 'item', debe cargar sus valores en los campos del formulario.
     */
    protected abstract void loadToForm(T item);

    /**
     * Copia los valores del formulario al modelo (para actualizar un registro existente).
     * Implementación típica: setX(...) en el objeto 'model'.
     */
    protected abstract void copyFormToModel(T model);

    /**
     * Crea una nueva instancia T a partir de los valores del formulario (para crear).
     * No debe insertar en repositorio; la base invocará createFunction.apply(model).
     */
    protected abstract T createModelFromForm();

    /**
     * Predicado de filtrado (busqueda). Por defecto obliga match en toString.
     * Subclasses pueden sobrescribir.
     */
    protected boolean filterPredicate(T item, String lowerFilter) {
        return item == null ? false : item.toString().toLowerCase().contains(lowerFilter);
    }
}
