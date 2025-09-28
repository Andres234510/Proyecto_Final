package co.edu.uniquindio.proyecto_final.decorator;

public abstract class TarifaDecorator implements TarifaComponent {
    protected final TarifaComponent wrappee;
    public TarifaDecorator(TarifaComponent wrappee) { this.wrappee = wrappee; }
    public double getCosto() { return wrappee.getCosto(); }
}
