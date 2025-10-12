package co.edu.uniquindio.proyecto_final.decorator;

public class SeguroDecorator extends EnvioDecorator {
    public SeguroDecorator(EnvioComponent c) { super(c); }

    @Override
    public double getCosto() {
        return componente.getCosto() + 2000;
    }

    @Override
    public String getDescripcion() {
        return componente.getDescripcion() + " + Seguro";
    }
}
