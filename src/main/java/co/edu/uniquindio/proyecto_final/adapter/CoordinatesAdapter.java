package co.edu.uniquindio.proyecto_final.adapter;

import co.edu.uniquindio.proyecto_final.model.Direccion;


public class CoordinatesAdapter {
    private Direccion direccion;
    public CoordinatesAdapter(Direccion direccion) { this.direccion = direccion; }

    public String toLatLonString() {
        return direccion.getLatitud() + "," + direccion.getLongitud();
    }

}
