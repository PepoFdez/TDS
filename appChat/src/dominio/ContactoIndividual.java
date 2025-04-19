package dominio;

import java.util.LinkedList;


public class ContactoIndividual extends Contacto implements AgregableGrupos {

	private Usuario usuario;
	private String URLImagen;
	
	public ContactoIndividual(Usuario usuario, String nombre) {
		super(nombre);
		this.usuario = usuario;
		this.URLImagen = usuario.getURLImagen();
	}
	
	//constructor para cuando usemos la persistencia
	public ContactoIndividual(String nombre) {
		super(nombre);
	}
	//para la persistencia
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
		this.URLImagen = usuario.getURLImagen();
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	
	@Override
	public String getURLImagen() {
		return URLImagen;
	}
	
	

}
