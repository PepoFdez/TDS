package dominio;

public class DescuentoPorFecha implements Descuento {

	@Override
	public double getDescuento(double precioInicial) {
		return precioInicial * 0.7;
	}

}
