package co.edu.uniquindio.proyecto_final.decorator;

public class BaseTarifa implements TarifaComponent {

    private final double base;

    public BaseTarifa(double base) { this.base = base; }

    @Override
    public double getCosto() { return base; }
}

