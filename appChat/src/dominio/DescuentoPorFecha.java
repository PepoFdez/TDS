package dominio;

import java.time.LocalDate;
import java.util.Objects;


/**
 * Implementación de la estrategia {@link Descuento} que aplica una reducción
 * de precio a los usuarios cuya fecha de registro se encuentre dentro de un
 * intervalo específico.
 * <p>
 * El intervalo actual para la promoción es para registros realizados estrictamente
 * después del {@link #FECHA_INICIO} y estrictamente antes de la {@link #FECHA_FIN}.
 * </p>
 */
public class DescuentoPorFecha implements Descuento {

	private static final LocalDate FECHA_INICIO = LocalDate.of(2025, 04, 1);
	private static final LocalDate FECHA_FIN = LocalDate.of(2025, 05, 1);
	private static final double DESC = 0.37;
	
	/**
     * Calcula el precio final aplicando un 37% de descuento.
     *
     * @param precioInicial El precio original antes de aplicar el descuento.
     * @return El precio con el descuento aplicado.
     */
	@Override
	public double getPrecio(double precioInicial) {
		return precioInicial * (1 - DESC);
	}
	
	/**
     * Verifica si este descuento es aplicable al usuario.
     * El descuento se aplica si la fecha de registro del usuario es posterior a
     * {@code FECHA_INICIO} y anterior a {@code FECHA_FIN}.
     *
     * @param user El {@link Usuario} para el cual se verifica la aplicabilidad.
     * Debe tener una fecha de registro válida.
     * @return {@code true} si la fecha de registro del usuario está dentro del
     * intervalo de la promoción, {@code false} en caso contrario.
     * @throws NullPointerException si {@code user} o {@code user.getFechaRegistro()} es nulo.
     */
    @Override
	public boolean isAplicable(Usuario user) {
		Objects.requireNonNull(user, "El usuario no puede ser nulo.");
		Objects.requireNonNull(user.getFechaRegistro(), "La fecha de registro del usuario no puede ser nula.");
		return (user.getFechaRegistro().isAfter(FECHA_INICIO) && user.getFechaRegistro().isBefore(FECHA_FIN));
	}

}
