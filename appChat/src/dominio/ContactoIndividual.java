package dominio;

import java.util.LinkedList;
import java.util.List;

public class ContactoIndividual extends Contacto {

	private Usuario usuario;
	
	public ContactoIndividual(Usuario usuario, String nombre) {
		super(nombre);
		this.usuario = usuario;
	}
	
	public ContactoIndividual(Usuario usuario, String nombre, LinkedList<Mensaje> mensajes) {
		super(nombre, mensajes);
		this.usuario = usuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	

}
