package dominio;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import tds.BubbleText;


/**
 * Clase abstracta que representa un contacto en la agenda del usuario.
 * Un contacto puede ser un {@link ContactoIndividual} o un {@link Grupo}.
 * Mantiene un historial de mensajes intercambiados.
 */
public abstract class Contacto {

	private static String DEFAULT_TEXTO = "Haz click para enviar un mensaje";
	
	private int id;
	private String nombre;
	private List<Mensaje> mensajes;
	
	
	/**
     * Constructor para un nuevo contacto.
     *
     * @param nombre El nombre del contacto. No debe ser nulo.
     */
	public Contacto(String nombre) {
		this.nombre = Objects.requireNonNull(nombre, "El nombre del contacto no puede ser nulo");
		this.mensajes = new LinkedList<Mensaje>();
	}

	/**
     * Constructor para crear un contacto a partir de datos existentes (ej. persistencia).
     *
     * @param nombre   El nombre del contacto.
     * @param mensajes La lista de mensajes intercambiados con este contacto.
     * Si es nula, se inicializará una lista vacía.
     */
	protected Contacto(String nombre, List<Mensaje> mensajes) {
		this.nombre = nombre;
		 this.mensajes = (mensajes != null) ? new LinkedList<>(mensajes) : new LinkedList<>();
	}
	
	/**
     * Obtiene el identificador único del contacto.
     * @return El id del contacto.
     */
	public int getId() {
		return id;
	}

	/**
     * Establece el identificador único del contacto.
     * Usado principalmente por la capa de persistencia.
     * @param id El nuevo id del contacto.
     */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
     * Obtiene el nombre del contacto.
     * @return El nombre del contacto.
     */
	public String getNombre() {
		return nombre;
	}
	
	/**
     * Establece el nombre del contacto.
     * @param nombre El nuevo nombre del contacto. No debe ser nulo.
     */
	public void setNombre(String nombre) {
		this.nombre = Objects.requireNonNull(nombre, "El nombre de un contacto no puede ser nulo");
	}
	
	/**
     * Devuelve una vista inmutable del historial de mensajes intercambiados con este contacto.
     * La lista contiene tanto mensajes enviados por el usuario actual como recibidos de este contacto.
     *
     * @return Una lista inmutable de {@link Mensaje}.
     */
	public List<Mensaje> getMensajesEnviados() {
		return Collections.unmodifiableList(mensajes);
	}
	
	/**
     * Añade un mensaje al historial de este contacto.
     *
     * @param mensaje El {@link Mensaje} a añadir. No debe ser nulo.
     */
	public Mensaje addMensaje(Object mensaje, int tipo) {
		Mensaje mensajeret = null;
		if (mensaje instanceof String) {
			mensajeret = new Mensaje((String) mensaje, tipo);
		} else if (mensaje instanceof Integer) {
			mensajeret = new Mensaje((Integer) mensaje, tipo);
		} else {
			throw new IllegalArgumentException("El mensaje debe ser un String o un Integer");
		}
		this.mensajes.add(mensajeret);
		return mensajeret;
	}
	
	public void addMensaje(Mensaje mensaje) {
		Objects.requireNonNull(mensaje, "El mensaje no puede ser nulo");
		this.mensajes.add(mensaje);
	}
	
	
	/**
     * Método abstracto para obtener la URL de la imagen del contacto.
     * Debe ser implementado por las subclases.
     *
     * @return La URL de la imagen como un String.
     */
	public abstract String getURLImagen();
	
	
	/**
     * Obtiene una lista del contenido principal de cada mensaje (texto o código de emoticono).
     * Este método parece estar orientado a la visualización en la interfaz gráfica.
     *
     * @return Una lista de {@link Object} donde cada elemento es un {@link String} (texto)
     * o un {@link Integer} (código de emoticono).
     */
	public  List<Object> getTextoMensajesEnviados(){
		return mensajes.stream()
			    .map(m -> m.getTexto().isEmpty() ? m.getEmoticono() : m.getTexto())
			    .collect(Collectors.toList());
	}
	
	/**
     * Obtiene una lista de información formateada (hora y tipo) para cada mensaje.
     * Útil para la visualización en la interfaz gráfica.
     *
     * @return Una lista de Strings con la información formateada de los mensajes.
     */
	public List<String> getInfoMensajesEnviados() {
		return mensajes.stream()
				.map(m -> m.getInfoFormateada())
				.collect(Collectors.toList());
	}
	
	/**
     * Obtiene el texto del último mensaje intercambiado o un mensaje predeterminado si no hay mensajes.
     *
     * @return El texto del último mensaje, o "Haz click para enviar un mensaje" si no hay mensajes.
     */
	public String getUltimoMensaje() {
		if (this.mensajes.isEmpty()) {
			return DEFAULT_TEXTO;
		}
		return this.mensajes.get(this.mensajes.size()-1).getTexto();	
	}
	
	/**
     * Filtra y devuelve todos los mensajes que fueron enviados por el usuario actual
     * (tipo {@code BubbleText.SENT}) en este chat.
     *
     * @return Una lista de {@link Mensaje} enviados por el usuario actual.
     */
	public List<Mensaje> getTodosLosMensajesEnviados() {
		
		return mensajes.stream()
					.filter(m -> m.getTipo() == BubbleText.SENT)
					.collect(Collectors.toList());
	};
	
	/**
	 * Filtra los mensajes de este contacto que contienen el texto especificado.
	 * La búsqueda de texto no distingue entre mayúsculas y minúsculas.
	 *
	 * @param textoBusqueda El texto a buscar en los mensajes. Si es nulo o está vacío,
	 * se podrían devolver todos los mensajes o una lista vacía,
	 * dependiendo del comportamiento deseado. Para este ejemplo,
	 * si textoBusqueda es vacío, filtra mensajes que son literalmente vacíos o nulos.
	 * Si se desea devolver todos, la lógica cambiaría.
	 * @return Una nueva lista de {@link Mensaje} que coinciden con el criterio de texto.
	 */
	public List<Mensaje> buscarMensajesPorTexto(String textoBusqueda) {
	    if (textoBusqueda == null) { // Si textoBusqueda es null, no hay nada que buscar
	        return new LinkedList<>();
	    }
	    String textoLower = textoBusqueda.toLowerCase().trim();
	    if (textoLower.isEmpty()) { // Si después de trim queda vacío, solo buscar mensajes vacíos
	        return this.getMensajesEnviados().stream() // getMensajesEnviados() ya devuelve una lista
	            .filter(mensaje -> mensaje.getTexto() == null || mensaje.getTexto().trim().isEmpty())
	            .collect(Collectors.toList());
	    }
	    return this.getMensajesEnviados().stream() // getMensajesEnviados() ya devuelve una lista
	            .filter(mensaje -> mensaje.getTexto() != null && 
	                               mensaje.getTexto().toLowerCase().contains(textoLower))
	            .collect(Collectors.toList());
	}
	

}
