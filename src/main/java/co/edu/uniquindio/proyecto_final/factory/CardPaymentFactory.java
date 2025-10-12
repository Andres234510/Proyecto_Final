package co.edu.uniquindio.proyecto_final.factory;

import co.edu.uniquindio.proyecto_final.model.Pago;
import co.edu.uniquindio.proyecto_final.model.ResultadoPago;

public class CardPaymentFactory implements PaymentFactory {
    @Override
    public Pago procesarPago(String idEnvio, double monto) {
        ResultadoPago r = ResultadoPago.APROBADO;
        return new Pago(idEnvio, monto, "TarjetaSimulada", r);
    }
}
