package dominio;

import java.time.LocalDateTime;
import java.time.LocalTime;
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

	private static final LocalDateTime FECHA_INICIO = LocalDateTime.of(2025, 04, 21, 0, 0, 0);
	private static final LocalDateTime FECHA_FIN = LocalDateTime.of(2025, 05, 22, 23, 59, 59);
	private static final double DESC = 0.18;
	
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
		LocalDateTime fechaRegistro = LocalDateTime.of(user.getFechaRegistro(), LocalTime.of(0, 0, 1));
		return (fechaRegistro.isAfter(FECHA_INICIO) && fechaRegistro.isBefore(FECHA_FIN));
	}

}
