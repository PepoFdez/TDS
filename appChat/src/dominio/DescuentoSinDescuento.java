package dominio;

public class DescuentoSinDescuento implements Descuento{

	@Override
	public double getPrecio(double precioInicial) {
		return precioInicial;
	}

	@Override
	public boolean isAplicable(Usuario user) {
		return true;
	}

}
