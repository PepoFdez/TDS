package dominio;

import java.util.Objects;

/**
 * Implementación de la estrategia {@link Descuento} que representa la ausencia
 * de un descuento aplicable.
 * <p>
 * Esta clase actúa como un Objeto Nulo (Null Object Pattern) para el sistema de descuentos,
 * asegurando que siempre haya una estrategia de descuento válida incluso si ninguna
 * condición de descuento específica se cumple.
 * </p>
 * Siempre es aplicable y no modifica el precio original.
 */
public class DescuentoSinDescuento implements Descuento {

    /**
     * Devuelve el precio inicial sin aplicar ninguna modificación.
     *
     * @param precioInicial El precio original.
     * @return El mismo {@code precioInicial}.
     */
    @Override
    public double getPrecio(double precioInicial) {
        return precioInicial;
    }

    /**
     * Indica que esta estrategia (sin descuento) es siempre aplicable.
     *
     * @param user El {@link Usuario} para el cual se verifica la aplicabilidad.
     * Aunque no se utiliza en esta implementación, se incluye por conformidad con la interfaz.
     * @return Siempre {@code true}.
     * @throws NullPointerException si {@code user} es nulo, por coherencia contractual con la interfaz.
     */
    @Override
    public boolean isAplicable(Usuario user) {
        Objects.requireNonNull(user, "El usuario no puede ser nulo para verificar la aplicabilidad del descuento.");
        return true;
    }
}