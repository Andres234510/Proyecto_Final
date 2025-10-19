package co.edu.uniquindio.proyecto_final.viewController;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.text.DecimalFormat;
import java.util.UUID;


public class Parcial2Tab {

    public static Tab createTab(boolean isAdmin) {
        Tab tab = new Tab("Parcial2");
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        VBox topBox = new VBox(6);
        Text title = new Text("Parcial 2 — Simulación de Patrones");
        title.setFont(Font.font("System", FontWeight.BOLD, 18));
        Label subtitle = new Label("Strategy, Decorator, Factory Method (simulación para tarifas). Ademas se hace " +
                "la simulacion del adapter");
        subtitle.setWrapText(true);
        topBox.getChildren().addAll(title, subtitle);
        topBox.setPadding(new Insets(6, 0, 12, 0));
        root.setTop(topBox);

        VBox left = new VBox(8);
        left.setPadding(new Insets(6));
        left.setPrefWidth(320);

        Label chooseLbl = new Label("Patrón a simular:");
        ChoiceBox<String> patternChoice = new ChoiceBox<>(FXCollections.observableArrayList("Strategy",
                "Decorator", "FactoryMethod"));
        patternChoice.setValue("Strategy");

        Label descLbl = new Label("Descripción:");
        TextArea description = new TextArea();
        description.setWrapText(true);
        description.setEditable(false);
        description.setPrefRowCount(6);

        Label codeLbl = new Label("Código simulado (ejemplo):");
        TextArea codeArea = new TextArea();
        codeArea.setEditable(false);
        codeArea.setPrefRowCount(10);
        codeArea.setStyle("-fx-font-family: 'monospace'; -fx-font-size: 11;");

        left.getChildren().addAll(chooseLbl, patternChoice, descLbl, description, codeLbl, codeArea);

        VBox center = new VBox(10);
        center.setPadding(new Insets(6));

        TableView<SimulationResult> table = new TableView<>();
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        TableColumn<SimulationResult, String> colId = new TableColumn<>("ID");
        colId.setPrefWidth(110);
        colId.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.
                getValue().getId()));

        TableColumn<SimulationResult, String> colDesc = new TableColumn<>("Descripción");
        colDesc.setPrefWidth(420);
        colDesc.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.
                getValue().getDescription()));

        TableColumn<SimulationResult, String> colCost = new TableColumn<>("Costo");
        colCost.setPrefWidth(120);
        colCost.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.
                getValue().getCostFormatted()));

        table.getColumns().addAll(colId, colDesc, colCost);

        ObservableList<SimulationResult> results = FXCollections.observableArrayList();
        table.setItems(results);

        VBox dynamicArea = new VBox(8);
        dynamicArea.setPadding(new Insets(6));
        dynamicArea.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 6; -fx-background-color: #fafbfd; " +
                "-fx-background-radius: 6;");

        TextField weightField = new TextField(); weightField.setPromptText("Peso (kg)");
        TextField distField = new TextField(); distField.setPromptText("Distancia (km)");
        CheckBox priorityBox = new CheckBox("Prioridad");
        ChoiceBox<String> stratChoice = new ChoiceBox<>(FXCollections.observableArrayList("Tarifa por peso",
                "Tarifa por distancia", "Tarifa prioritaria"));
        stratChoice.setValue("Tarifa por peso");
        Button stratCompute = new Button("Calcular tarifa");
        stratCompute.getStyleClass().add("primary-btn");
        VBox stratBox = new VBox(8, stratChoice, new Label("Peso:"), weightField, new Label("Distancia:"),
                distField, priorityBox, stratCompute);

        TextField baseCostField = new TextField(); baseCostField.setPromptText("Costo base (ej: 10.0)");
        CheckBox seguroBox = new CheckBox("Seguro (+10%)");
        CheckBox rastreoBox = new CheckBox("Rastreo (+5%)");
        CheckBox nocturnoBox = new CheckBox("Entrega nocturna (+20%)");
        Button decoCompute = new Button("Aplicar decoradores");
        decoCompute.getStyleClass().add("primary-btn");
        VBox decoBox = new VBox(8, new Label("Costo base:"), baseCostField, seguroBox, rastreoBox,
                nocturnoBox, decoCompute);

        TextField facWeight = new TextField(); facWeight.setPromptText("Peso (kg)");
        TextField facDist = new TextField(); facDist.setPromptText("Distancia (km)");
        ChoiceBox<String> facFactoryChoice = new ChoiceBox<>(FXCollections.observableArrayList("PesoFactory",
                "DistanciaFactory", "PrioridadFactory"));
        facFactoryChoice.setValue("PesoFactory");
        Button factoryRun = new Button("Procesar con Factory");
        factoryRun.getStyleClass().add("primary-btn");
        VBox factoryBox = new VBox(8, new Label("Valores para Factory Method:"), facWeight, facDist,
                new Label("Factory:"), facFactoryChoice, factoryRun);

        dynamicArea.getChildren().setAll(stratBox);

        patternChoice.getSelectionModel().selectedItemProperty().addListener((obs,
                                                                              oldV, newV) -> {
            results.clear();
            if ("Strategy".equals(newV)) {
                description.setText("Strategy: permite cambiar algoritmo en tiempo de ejecución. Ejemplo: por " +
                        "peso, por distancia o prioritaria.");
                codeArea.setText(getStrategySampleCode());
                dynamicArea.getChildren().setAll(stratBox);
            } else if ("Decorator".equals(newV)) {
                description.setText("Decorator: añadir servicios opcionales a una tarifa base.");
                codeArea.setText(getDecoratorSampleCode());
                dynamicArea.getChildren().setAll(decoBox);
            } else {
                description.setText("Factory Method: crear procesadores de envío mediante fábricas concretas.");
                codeArea.setText(getFactorySampleCode());
                dynamicArea.getChildren().setAll(factoryBox);
            }
        });

        DecimalFormat fmt = new DecimalFormat("#0.00");

        stratCompute.setOnAction(e -> {
            try {
                double w = Double.parseDouble(weightField.getText().trim());
                double d = Double.parseDouble(distField.getText().trim());
                boolean p = priorityBox.isSelected();
                String sel = stratChoice.getValue();

                TarifaStrategy strategy;
                if ("Tarifa por distancia".equals(sel)) strategy = new TarifaPorDistancia();
                else if ("Tarifa prioritaria".equals(sel)) strategy = new TarifaPrioritaria();
                else strategy = new TarifaPorPeso();

                double cost = strategy.calcular(w, d, p);
                String desc = "Strategy -> " + sel + " (peso=" + w + ", dist=" + d + ", prioridad=" + p + ")";
                addResult(results, new SimulationResult(desc, cost));
            } catch (NumberFormatException ex) {
                showAlert("Entrada inválida", "Peso y distancia deben ser números válidos.");
            }
        });

        decoCompute.setOnAction(e -> {
            try {
                double base = Double.parseDouble(baseCostField.getText().trim());
                TarifaComponent comp = new TarifaBase(base);
                StringBuilder applied = new StringBuilder();
                if (seguroBox.isSelected()) { comp = new SeguroDecorator(comp); applied.append("Seguro "); }
                if (rastreoBox.isSelected()) { comp = new RastreoDecorator(comp); applied.append("Rastreo "); }
                if (nocturnoBox.isSelected()) { comp = new NocturnoDecorator(comp); applied.append("Nocturno "); }
                double cost = comp.getCost();
                String desc = "Decorator -> base=" + fmt.format(base) + " | " + (applied.length() == 0 ? "ninguno" :
                        applied.toString());
                addResult(results, new SimulationResult(desc, cost));
            } catch (NumberFormatException ex) {
                showAlert("Entrada inválida", "Costo base debe ser un número válido.");
            }
        });

        factoryRun.setOnAction(e -> {
            try {
                double w = Double.parseDouble(facWeight.getText().trim());
                double d = Double.parseDouble(facDist.getText().trim());
                String factoryKey = facFactoryChoice.getValue();

                ShipmentProcessorFactory factory;
                switch (factoryKey) {
                    case "DistanciaFactory": factory = new DistanciaShipmentFactory(); break;
                    case "PrioridadFactory": factory = new PrioridadShipmentFactory(); break;
                    default: factory = new PesoShipmentFactory();
                }

                ShipmentProcessor processor = factory.createProcessor();
                FacadeReport report = processor.process(w, d);
                String desc = "FactoryMethod -> " + factoryKey + " (tarifa=" + fmt.format(report.tarifa) +
                        ", pago=" + fmt.format(report.pago) + ")";
                addResult(results, new SimulationResult(desc, report.tarifa + report.pago));
            } catch (NumberFormatException ex) {
                showAlert("Entrada inválida", "Peso y distancia deben ser números válidos.");
            }
        });

        HBox bottom = new HBox(8);
        bottom.setPadding(new Insets(6));
        Button btnClear = new Button("Limpiar resultados");
        Button btnExport = new Button("Exportar (CSV)");
        btnExport.getStyleClass().add("primary-btn");
        btnExport.setDisable(!isAdmin);

        btnClear.setOnAction(e -> results.clear());
        btnExport.setOnAction(e -> {
            StringBuilder sb = new StringBuilder("id,descripcion,costo\n");
            for (SimulationResult r : results) {
                sb.append(r.getId()).append(",\"").append(r.getDescription().replace("\"",
                        "'")).append("\",").append(r.getCost()).append("\n");
            }
            System.out.println("=== Parcial2 Export CSV ===\n" + sb.toString());
            showAlert("Exportado", "Resultados exportados a consola (simulación).");
        });

        bottom.getChildren().addAll(btnClear, btnExport);

        center.getChildren().addAll(dynamicArea, new Label("Resultados:"), table, bottom);

        HBox main = new HBox(12, left, center);
        HBox.setHgrow(center, Priority.ALWAYS);
        root.setCenter(main);

        tab.setContent(root);
        return tab;
    }

    private static void addResult(ObservableList<SimulationResult> list, SimulationResult r) {
        Platform.runLater(() -> list.add(r));
    }

    private static void showAlert(String title, String text) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(text);
        a.showAndWait();
    }

    public static class SimulationResult {
        private final String id;
        private final String description;
        private final double cost;
        private final DecimalFormat fmt = new DecimalFormat("#0.00");

        public SimulationResult(String description, double cost) {
            this.id = UUID.randomUUID().toString().substring(0, 8);
            this.description = description;
            this.cost = cost;
        }

        public String getId() { return id; }
        public String getDescription() { return description; }
        public double getCost() { return cost; }
        public String getCostFormatted() { return fmt.format(cost); }
    }

    public interface TarifaStrategy {
        double calcular(double peso, double distancia, boolean prioridad);
    }

    public static class TarifaPorPeso implements TarifaStrategy {
        @Override public double calcular(double peso, double distancia, boolean prioridad) {
            double base = 5.0;
            double costo = base + 2.0 * peso;
            if (prioridad) costo *= 1.25;
            return costo;
        }
    }

    public static class TarifaPorDistancia implements TarifaStrategy {
        @Override public double calcular(double peso, double distancia, boolean prioridad) {
            double base = 3.0;
            double costo = base + 0.8 * distancia;
            if (prioridad) costo += 10;
            return costo;
        }
    }

    public static class TarifaPrioritaria implements TarifaStrategy {
        @Override public double calcular(double peso, double distancia, boolean prioridad) {
            double c1 = new TarifaPorPeso().calcular(peso, distancia, false);
            double c2 = new TarifaPorDistancia().calcular(peso, distancia, false);
            double costo = Math.max(c1, c2) * 1.5;
            if (!prioridad) costo *= 0.9;
            return costo;
        }
    }

    public interface TarifaComponent {
        double getCost();
    }

    public static class TarifaBase implements TarifaComponent {
        private final double base;
        public TarifaBase(double b)
        {this.base=b;
        }
        @Override
        public double getCost(){return base;} }

    public static abstract class TarifaDecorator implements TarifaComponent {
        protected final TarifaComponent wrap;
        public TarifaDecorator(TarifaComponent w){
            this.wrap=w;
        }
        @Override
        public double getCost(){
            return wrap.getCost();
        }
    }
    public static class SeguroDecorator extends TarifaDecorator
    { public SeguroDecorator(TarifaComponent w){
        super(w);
    }
        @Override
        public double getCost(){
        return wrap.getCost()*1.10;
    }
    }
    public static class RastreoDecorator extends TarifaDecorator {
        public RastreoDecorator(TarifaComponent w){
            super(w);
        }
        @Override
        public double getCost(){
            return wrap.getCost()*1.05;
        }
    }
    public static class NocturnoDecorator extends TarifaDecorator {
        public NocturnoDecorator(TarifaComponent w){
            super(w);
        }
        @Override
        public double getCost(){return wrap.getCost()*1.20;}
    }



    public interface ShipmentProcessor {
        FacadeReport process(double weight, double distance);
    }
    public static abstract class ShipmentProcessorFactory {
        public abstract ShipmentProcessor createProcessor();
    }


    public static class PesoShipmentFactory extends ShipmentProcessorFactory {
        @Override public ShipmentProcessor createProcessor() {
            return (weight, distance) -> {
                TarifaStrategy ts = new TarifaPorPeso();
                double tarifa = ts.calcular(weight, distance, false);
                TarifaComponent comp = new SeguroDecorator(new TarifaBase(tarifa));
                double tarifaFinal = comp.getCost();
                double pago = tarifaFinal * 1.03;
                return new FacadeReport(tarifaFinal, pago, 2);
            };
        }
    }

    public static class DistanciaShipmentFactory extends ShipmentProcessorFactory {
        @Override public ShipmentProcessor createProcessor() {
            return (weight, distance) -> {
                TarifaStrategy ts = new TarifaPorDistancia();
                double tarifa = ts.calcular(weight, distance, false);
                TarifaComponent comp = new RastreoDecorator(new TarifaBase(tarifa));
                double tarifaFinal = comp.getCost();
                double pago = tarifaFinal * 1.03;
                return new FacadeReport(tarifaFinal, pago, 1);
            };
        }
    }

    public static class PrioridadShipmentFactory extends ShipmentProcessorFactory {
        @Override public ShipmentProcessor createProcessor() {
            return (weight, distance) -> {
                TarifaStrategy ts = new TarifaPrioritaria();
                double tarifa = ts.calcular(weight, distance, true);
                TarifaComponent comp = new NocturnoDecorator(new SeguroDecorator(new TarifaBase(tarifa)));
                double tarifaFinal = comp.getCost();
                double pago = tarifaFinal * 1.03;
                return new FacadeReport(tarifaFinal, pago, 3);
            };
        }
    }

    public static class FacadeReport {
        public final double tarifa; public final double pago; public final int notifications;
        public FacadeReport(double tarifa, double pago, int notifications) {
            this.tarifa = tarifa; this.pago = pago;
            this.notifications = notifications;
        }
    }

    private static String getStrategySampleCode() { return "public interface TarifaStrategy {\n" +
            "        double calcular(double peso, double distancia, boolean prioridad);\n" +
            "    }\n" +
            "\n" +
            "    public static class TarifaPorPeso implements TarifaStrategy {\n" +
            "        @Override public double calcular(double peso, double distancia, boolean prioridad) {\n" +
            "            double base = 5.0;\n" +
            "            double costo = base + 2.0 * peso;\n" +
            "            if (prioridad) costo *= 1.25;\n" +
            "            return costo;\n" +
            "        }\n" +
            "    }"; }

    private static String getDecoratorSampleCode() { return "public interface TarifaComponent { double getCost(); }\n" +
            "    public static class TarifaBase implements TarifaComponent { private final double base; public \n" +
            "    TarifaBase(double b){this.base=b;} \n" +
            "        @Override \n" +
            "        public double getCost(){return base;} }\n" +
            "    public static abstract class TarifaDecorator implements TarifaComponent { protected final \n" +
            "    TarifaComponent wrap; public TarifaDecorator(TarifaComponent w){this.wrap=w;} \n" +
            "        @Override \n" +
            "        public double \n" +
            "    getCost(){return wrap.getCost();} }\n" +
            "    public static class SeguroDecorator extends TarifaDecorator { public SeguroDecorator(TarifaComponent " +
            "w){super(w);} \n" +
            "        @Override \n" +
            "        public double getCost(){return wrap.getCost()*1.10;} }\n" +
            "    public static class RastreoDecorator extends TarifaDecorator { public RastreoDecorator(TarifaComponent " +
            "w){super(w);} \n" +
            "        @Override \n" +
            "        public double getCost(){return wrap.getCost()*1.05;} }\n" +
            "    public static class NocturnoDecorator extends TarifaDecorator { public NocturnoDecorator" +
            "(TarifaComponent w){super(w);} \n" +
            "        @Override\n" +
            "        public double getCost(){return wrap.getCost()*1.20;} }\n" +
            "\n" +
            "    public interface ShipmentProcessor { FacadeReport process(double weight, double distance); }\n" +
            "    public static abstract class ShipmentProcessorFactory { public abstract ShipmentProcessor " +
            "createProcessor(); }"; }

    private static String getFactorySampleCode() { return "public interface ShipmentProcessor { FacadeReport" +
            " process(double weight, double distance); }\n" +
            "    public static abstract class ShipmentProcessorFactory { public abstract ShipmentProcessor " +
            "createProcessor(); }\n" +
            "\n" +
            "    public static class PesoShipmentFactory extends ShipmentProcessorFactory {\n" +
            "        @Override public ShipmentProcessor createProcessor() {\n" +
            "            return (weight, distance) -> {\n" +
            "                TarifaStrategy ts = new TarifaPorPeso();\n" +
            "                double tarifa = ts.calcular(weight, distance, false);\n" +
            "                TarifaComponent comp = new SeguroDecorator(new TarifaBase(tarifa));\n" +
            "                double tarifaFinal = comp.getCost();\n" +
            "                double pago = tarifaFinal * 1.03;\n" +
            "                return new FacadeReport(tarifaFinal, pago, 2);\n" +
            "            };\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    public static class DistanciaShipmentFactory extends ShipmentProcessorFactory {\n" +
            "        @Override public ShipmentProcessor createProcessor() {\n" +
            "            return (weight, distance) -> {\n" +
            "                TarifaStrategy ts = new TarifaPorDistancia();\n" +
            "                double tarifa = ts.calcular(weight, distance, false);\n" +
            "                TarifaComponent comp = new RastreoDecorator(new TarifaBase(tarifa));\n" +
            "                double tarifaFinal = comp.getCost();\n" +
            "                double pago = tarifaFinal * 1.03;\n" +
            "                return new FacadeReport(tarifaFinal, pago, 1);\n" +
            "            };\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    public static class PrioridadShipmentFactory extends ShipmentProcessorFactory {\n" +
            "        @Override public ShipmentProcessor createProcessor() {\n" +
            "            return (weight, distance) -> {\n" +
            "                TarifaStrategy ts = new TarifaPrioritaria();\n" +
            "                double tarifa = ts.calcular(weight, distance, true);\n" +
            "                TarifaComponent comp = new NocturnoDecorator(new SeguroDecorator(new TarifaBase(tarifa)));\n" +
            "                double tarifaFinal = comp.getCost();\n" +
            "                double pago = tarifaFinal * 1.03;\n" +
            "                return new FacadeReport(tarifaFinal, pago, 3);\n" +
            "            };\n" +
            "        }\n" +
            "    }"; }
}
