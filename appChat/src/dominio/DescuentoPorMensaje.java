package dominio;

public class DescuentoPorMensaje implements Descuento {

	@Override
	public double getDescuento(double precioInicial) {
		return precioInicial * 0.85;
	}

}
