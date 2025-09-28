package co.edu.uniquindio.proyecto_final.decorator;

public class PrioridadDecorator extends TarifaDecorator {
    public PrioridadDecorator(TarifaComponent wrappee) { super(wrappee); }
    @Override
    public double getCosto() { return super.getCosto() * 1.25; } // +25%
}
