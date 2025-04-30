package dominio;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public enum FactoriaDescuentos {
	INSTANCE;
	
	private static final List<Descuento> TODOS_DESCUENTOS = Arrays.asList(
			new DescuentoSinDescuento(),
			new DescuentoPorFecha(),
			new DescuentoPorMensaje()
			);
	
	public Descuento getMejorDescuento(Usuario user, double precio) {
		return TODOS_DESCUENTOS.stream()
				.filter(d -> d.isAplicable(user))
				.min(Comparator.comparingDouble(d -> d.getPrecio(precio)))
				.orElse(new DescuentoSinDescuento());
	}
}
