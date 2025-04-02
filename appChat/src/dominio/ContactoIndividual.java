package dominio;

import java.util.LinkedList;


public class ContactoIndividual extends Contacto implements agregableGrupos {

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
	//sin get/set usuario. Tiene sentido cambiar el usuario¿?¿ cambiar el nombre más bien, no?
	
	public String getInfo() {
        return "Nombre: " + this.getNombre() + "\n" +
        		"Telefono: " + this.usuario.getMovil() + "\n" ;
    }
	
	

}
