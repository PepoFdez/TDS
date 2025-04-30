package dominio;

public class DescuentoPorMensaje implements Descuento {

	private static final double DESC = 0.2;
	private static final int MENSAJES_MIN = 0;
	
	@Override
	public double getPrecio(double precioInicial) {
		return precioInicial * (1 - DESC);
	}

	@Override
	public boolean isAplicable(Usuario user) {
		return user.getNumeroMensajesEnviadosMesPasado() >= MENSAJES_MIN;
	}

}
