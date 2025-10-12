package co.edu.uniquindio.proyecto_final.factory;

import co.edu.uniquindio.proyecto_final.model.Pago;
import co.edu.uniquindio.proyecto_final.model.ResultadoPago;

public interface PaymentFactory {
    Pago procesarPago(String idEnvio, double monto);
}
