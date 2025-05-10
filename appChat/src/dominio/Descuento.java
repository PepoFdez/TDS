package dominio;

/**
 * Interfaz que define la estrategia para aplicar descuentos sobre un precio.
 * Sigue el Patrón de Diseño Strategy, permitiendo que diferentes algoritmos de descuento
 * sean intercambiables.
 * <p>
 * Las implementaciones de esta interfaz determinarán si un descuento es aplicable
 * a un {@link Usuario} específico y cómo calcular el precio final.
 * </p>
 */
public interface Descuento {

    /**
     * Calcula el precio final después de aplicar este descuento.
     *
     * @param precioInicial El precio original antes de aplicar el descuento.
     * @return El precio con el descuento aplicado.
     */
    double getPrecio(double precioInicial);

    /**
     * Verifica si esta estrategia de descuento es aplicable al usuario proporcionado.
     *
     * @param user El {@link Usuario} para el cual se verifica la aplicabilidad del descuento.
     * No debe ser nulo.
     * @return {@code true} si el descuento es aplicable, {@code false} en caso contrario.
     */
    boolean isAplicable(Usuario user);
}