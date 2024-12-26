package dominio;

public class DescuentoPorMenssaje implements Descuento {

	@Override
	public double getDescuento(double precioInicial) {
		return precioInicial * 0.85;
	}

}
