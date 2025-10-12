package co.edu.uniquindio.proyecto_final.builder;

import co.edu.uniquindio.proyecto_final.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EnvioBuilder {

    private String id;
    private Usuario usuario;
    private Direccion origen;
    private Direccion destino;
    private double peso;
    private double volumen;
    private LocalDateTime fechaEstimada;
    private List<ServicioAdicional> servicios = new ArrayList<>();

    public EnvioBuilder id(String id) {
        this.id = id;
        return this;
    }

    public EnvioBuilder usuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public EnvioBuilder origen(Direccion origen) {
        this.origen = origen;
        return this;
    }

    public EnvioBuilder destino(Direccion destino) {
        this.destino = destino;
        return this;
    }

    public EnvioBuilder peso(double peso) {
        this.peso = peso;
        return this;
    }

    public EnvioBuilder volumen(double volumen) {
        this.volumen = volumen;
        return this;
    }

    public EnvioBuilder fechaEstimada(LocalDateTime fecha) {
        this.fechaEstimada = fecha;
        return this;
    }

    public EnvioBuilder prioridad(boolean activar) {
        if (activar && !servicios.contains(ServicioAdicional.PRIORIDAD)) {
            servicios.add(ServicioAdicional.PRIORIDAD);
        }
        return this;
    }

    public EnvioBuilder seguro(boolean activar) {
        if (activar && !servicios.contains(ServicioAdicional.SEGURO)) {
            servicios.add(ServicioAdicional.SEGURO);
        }
        return this;
    }

    public Envio build() {
        Envio envio = new Envio();
        if (this.id != null) {
            try {
                envio.getClass().getMethod("setIdEnvio", String.class).invoke(envio, this.id);
            } catch (Exception ex) {
                try { envio.getClass().getMethod("setId", String.class).invoke(envio, this.id); } catch (Exception ignore) {}
            }
        } else {
            try {
                if (envio.getClass().getMethod("getIdEnvio") == null) { }
            } catch (NoSuchMethodException ignored) {}
        }

        if (this.usuario != null) envio.setUsuario(this.usuario);
        if (this.origen != null) envio.setOrigen(this.origen);
        if (this.destino != null) envio.setDestino(this.destino);
        envio.setPeso(this.peso);
        envio.setVolumen(this.volumen);
        if (this.fechaEstimada != null) envio.setFechaEstimadaEntrega(this.fechaEstimada);

        for (ServicioAdicional s : servicios) {
            envio.addServicio(s);
        }

        envio.setEstado(EstadoEnvio.SOLICITADO);
        return envio;
    }
}
