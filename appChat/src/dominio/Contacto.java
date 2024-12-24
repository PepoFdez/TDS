package dominio;

import java.util.LinkedList;
import java.util.List;

public abstract class Contacto {
	private int codigo; //Esto de momento no sirve
	private String nombre;
	private List<Mensaje> mensajes;
	
	public Contacto(String nombre) {
		this.nombre = nombre;
		this.mensajes = new LinkedList<Mensaje>();
	}
	
	public Contacto(String nombre, List<Mensaje> mensajes) {
		this.nombre = nombre;
		this.mensajes = mensajes;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public List<Mensaje> getMensajesEnviados() {
		return mensajes;
	}
	
	public int getCodigo() {
		return codigo;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
