package dominio;

public interface Descuento { //patrón Strategy

	public double getPrecio(double precioInicial);
	
	public boolean isAplicable(Usuario user);
}
