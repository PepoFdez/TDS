package dominio;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
	
	public String getMensajesFormateados() {
		
		return null;
	} 
	
	public abstract String getURLImagen();
	
	public  List<Object> getTextoMensajesEnviados(){
		return mensajes.stream()
			    .map(m -> m.getTexto().isEmpty() ? m.getEmoticono() : m.getTexto())
			    .collect(Collectors.toList());
	}
	public List<String> getInfoMensajesEnviados() {
		return mensajes.stream()
				.map(m -> m.getInfoFormateada())
				.collect(Collectors.toList());
	}
	public String getUltimoMensaje() {
		if (this.mensajes.isEmpty()) {
			return "Haz click para enviar un mensaje";
		}
		return this.mensajes.get(this.mensajes.size()-1).getTexto();	
	};
	
	
	

}
