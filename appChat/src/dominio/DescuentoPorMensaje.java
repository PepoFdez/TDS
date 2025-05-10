package dominio;

import java.util.Objects;

/**
 * Implementación de la estrategia {@link Descuento} que aplica una reducción
 * de precio a los usuarios que hayan enviado un número mínimo de mensajes
 * durante el mes anterior.
 */
public class DescuentoPorMensaje implements Descuento {

    private static final double PORCENTAJE_DESCUENTO = 0.2; // 20%
    private static final int UMBRAL_MENSAJES_MES_PASADO = 0;

    /**
     * Calcula el precio final aplicando un 20% de descuento.
     *
     * @param precioInicial El precio original antes de aplicar el descuento.
     * @return El precio con el descuento aplicado.
     */
    @Override
    public double getPrecio(double precioInicial) {
        return precioInicial * (1 - PORCENTAJE_DESCUENTO);
    }

    /**
     * Verifica si este descuento es aplicable al usuario.
     * El descuento se aplica si el número de mensajes enviados por el usuario
     * el mes pasado es mayor o igual a {@link #UMBRAL_MENSAJES_MES_PASADO}.
     *
     * @param user El {@link Usuario} para el cual se verifica la aplicabilidad.
     * Se espera que el método {@code user.getNumeroMensajesEnviadosMesPasado()}
     * devuelva un conteo válido.
     * @return {@code true} si el usuario cumple con el umbral de mensajes,
     * {@code false} en caso contrario.
     * @throws NullPointerException si {@code user} es nulo.
     */
    @Override
    public boolean isAplicable(Usuario user) {
        Objects.requireNonNull(user, "El usuario no puede ser nulo.");
        // Se asume que user.getNumeroMensajesEnviadosMesPasado() nunca es negativo.
        return user.getNumeroMensajesEnviadosMesPasado() >= UMBRAL_MENSAJES_MES_PASADO;
    }
}