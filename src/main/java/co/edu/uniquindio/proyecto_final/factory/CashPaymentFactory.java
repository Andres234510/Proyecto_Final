package co.edu.uniquindio.proyecto_final.factory;

import co.edu.uniquindio.proyecto_final.model.Pago;
import co.edu.uniquindio.proyecto_final.model.ResultadoPago;

public class CashPaymentFactory implements PaymentFactory {
    @Override
    public Pago procesarPago(String idEnvio, double monto) {
        return new Pago(idEnvio, monto, "Efectivo", ResultadoPago.APROBADO);
    }
}
