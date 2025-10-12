package co.edu.uniquindio.proyecto_final.decorator;

public abstract class EnvioDecorator implements EnvioComponent {
    protected EnvioComponent componente;
    public EnvioDecorator(EnvioComponent c) { this.componente = c; }
}
