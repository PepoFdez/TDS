package dominio;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Grupo extends Contacto {

	private HashSet<Contacto> miembros; //para asegurarnos desde la implementación que no hay repes
	private String URLImagen;
	
	public Grupo(String nombre, AgregableGrupos...miembros) { //aseguramos que sólo se metan miembros que sean contactos individuales
		super(nombre);//usamos este stream en lugar de un forEach.
		this.miembros = Arrays.stream(miembros)
				.map(miembro -> (Contacto) miembro)
				.collect(Collectors.toCollection(HashSet::new));
		this.URLImagen = ""; //por defecto no tiene imagen
		
		
	}
	//creamos un grupo con mensajes para la persistencia
	//asumimos que no se han añadido contactos no agregables
	public Grupo(String nombre, List<Mensaje> mensajes) {
		super(nombre, mensajes);
		this.miembros = new HashSet<Contacto>();
	}
	
	public Boolean addMiembro(AgregableGrupos miembro) {
		return this.miembros.add((Contacto) miembro);
    }
	
	public List<Contacto> getMiembros() {
		return new LinkedList<>(miembros);
	}
	

	public boolean removeMiembro(Contacto miembro) {
	    if (miembro == null) {
	        throw new NullPointerException("El miembro no puede ser nulo");
	    }
	    if (miembros.contains(miembro) && miembros.size() == 1) {
	        return false; // No puede haber un grupo vacío | deberíamos lanzar excepción?¿?¿?¿
	    }
	    return this.miembros.remove(miembro);
	}

	
	@Override
	public void addMensaje(Mensaje mensaje) {
		super.addMensaje(mensaje);
		for (Contacto miembro : miembros) {
			miembro.addMensaje(mensaje);
		}
	}

	public void setURLImagen(String urlImagen) {
		this.URLImagen = urlImagen;
	}
	
	@Override
	public String getURLImagen() {
		return URLImagen;
	}
	
	
	
	
}

