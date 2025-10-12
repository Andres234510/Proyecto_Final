package co.edu.uniquindio.proyecto_final.decorator;

import co.edu.uniquindio.proyecto_final.model.Envio;

public class EnvioBase implements EnvioComponent {
    private Envio envio;

    public EnvioBase(Envio envio) { this.envio = envio; }

    @Override
    public double getCosto() {
        return envio.getCosto();
    }

    @Override
    public String getDescripcion() {
        return "Envio base " + envio.getIdEnvio();
    }
}
