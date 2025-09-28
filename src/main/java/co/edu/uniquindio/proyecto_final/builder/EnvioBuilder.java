package co.edu.uniquindio.proyecto_final.builder;

import co.edu.uniquindio.proyecto_final.model.Direccion;
import co.edu.uniquindio.proyecto_final.model.Envio;

public class EnvioBuilder {
    private final Envio envio = new Envio();

    public EnvioBuilder origen(Direccion dir) { envio.setOrigen(dir); return this; }
    public EnvioBuilder destino(Direccion dir) { envio.setDestino(dir); return this; }
    public EnvioBuilder peso(double p) { envio.setPeso(p); return this; }
    public EnvioBuilder volumen(double v) { envio.setVolumen(v); return this; }
    public EnvioBuilder prioridad(boolean p) { envio.setPrioridad(p); return this; }
    public Envio build() { return envio; }
}
