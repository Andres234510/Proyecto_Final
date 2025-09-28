package co.edu.uniquindio.proyecto_final.decorator;

public class SeguroDecorator extends TarifaDecorator {
    public SeguroDecorator(TarifaComponent wrappee) { super(wrappee); }
    @Override
    public double getCosto() { return super.getCosto() + 500; } // recargo fijo
}
