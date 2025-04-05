package dominio;

//import controlador.Controlador;

public class DescuentoPorMensaje implements Descuento {

	@Override
	public double getDescuento(double precioInicial) {
		//Usuario user = Controlador.INSTANCE.getNumeroDeMensajes();
		return precioInicial * 0.85;
	}

}
