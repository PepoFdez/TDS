package dominio;

import java.time.LocalDate;

public class DescuentoPorFecha implements Descuento {

	private static final LocalDate FECHA_INICIO = LocalDate.of(2025, 04, 1);
	private static final LocalDate FECHA_FIN = LocalDate.of(2025, 05, 1);
	private static final double DESC = 0.37;
	
	@Override
	public double getPrecio(double precioInicial) {
		return precioInicial * (1 - DESC);
	}
	
	@Override
	public boolean isAplicable(Usuario user) {
		return (user.getFechaRegistro().isAfter(FECHA_INICIO) && user.getFechaRegistro().isBefore(FECHA_FIN));
	}

}
