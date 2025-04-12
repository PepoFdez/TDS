package dominio;

import java.util.LinkedList;


public class ContactoIndividual extends Contacto implements AgregableGrupos {

	private Usuario usuario;
	
	public ContactoIndividual(Usuario usuario, String nombre) {
		super(nombre);
		this.usuario = usuario;
	}
	
	//constructor para cuando usemos la persistencia
	public ContactoIndividual(Usuario usuario, String nombre, LinkedList<Mensaje> mensajes) {
		super(nombre, mensajes);
		this.usuario = usuario;
	}
	//para la persistencia
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public String getInfo() {
        return "Nombre: " + this.getNombre() + "\n" +
        		"Telefono: " + this.usuario.getMovil() + "\n" ;
    }
	
	

}
