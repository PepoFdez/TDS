package dominio;

import java.util.LinkedList;
import java.util.List;

public abstract class Contacto {

	private int id;
	private String nombre;
	private List<Mensaje> mensajes;
	
	public Contacto(String nombre) {
		this.nombre = nombre;
		this.mensajes = new LinkedList<Mensaje>();
	}
	//pers
	public Contacto(String nombre, List<Mensaje> mensajes) {
		this.nombre = nombre;
		this.mensajes = mensajes;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public List<Mensaje> getMensajesEnviados() {
		return mensajes;
	}
	
	public void addMensaje(Mensaje mensaje) {
		this.mensajes.add(mensaje);
	}
	
	public abstract String getInfo();
	
	
	

}
