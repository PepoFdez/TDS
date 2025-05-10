package dominio;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Factoría Singleton para obtener la estrategia de {@link Descuento} más ventajosa
 * para un {@link Usuario} y un precio determinados.
 * <p>
 * Utiliza el patrón Singleton implementado mediante un {@code enum}.
 * Evalúa una lista predefinida de descuentos y selecciona aquel que resulte
 * en el precio final más bajo para el usuario.
 * </p>
 */
public enum FactoriaDescuentos {
	/**
     * Única instancia de la factoría.
     */
	INSTANCE;
	
	private static final Descuento DESCUENTO_POR_DEFECTO = new DescuentoSinDescuento();
	
	private static final List<Descuento> TODOS_DESCUENTOS = Arrays.asList(
			new DescuentoSinDescuento(),
			new DescuentoPorFecha(),
			new DescuentoPorMensaje()
			);
	
	/**
     * Obtiene el mejor {@link Descuento} aplicable para un {@link Usuario} y un precio dados.
     * <p>
     * El "mejor" descuento es aquel que, siendo aplicable al usuario, produce el
     * precio final más bajo. Si varios descuentos ofrecen el mismo precio más bajo,
     * la selección entre ellos no está garantizada más allá de lo que {@link Comparator#min(Comparator)} determine.
     * </p>
     * <p>
     * Si ningún descuento específico (diferente de {@link DescuentoSinDescuento}) es aplicable
     * o más ventajoso, se devolverá una instancia de {@code DescuentoSinDescuento}.
     * </p>
     *
     * @param usuario El {@link Usuario} para el cual se determina el descuento. No debe ser nulo.
     * @param precioBase El precio original sobre el cual se calcularía el descuento.
     * @return La estrategia de {@link Descuento} más ventajosa. Nunca será {@code null}.
     * @throws NullPointerException si {@code usuario} es nulo.
     */
	public Descuento getMejorDescuento(Usuario user, double precio) {
		Objects.requireNonNull(user, "El usuario no puede ser nulo para determinar el mejor descuento.");
		
		return TODOS_DESCUENTOS.stream()
				.filter(d -> d.isAplicable(user))
				.min(Comparator.comparingDouble(d -> d.getPrecio(precio)))
				.orElse(DESCUENTO_POR_DEFECTO);
	}
}
