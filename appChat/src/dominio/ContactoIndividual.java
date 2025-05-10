package dominio;

/**
 * Representa un contacto individual en la agenda del usuario, asociado a un
 * {@link Usuario} específico del sistema.
 * Este tipo de contacto puede ser agregado a {@link Grupo}s.
 */
public class ContactoIndividual extends Contacto implements AgregableGrupos {

	
	private Usuario usuario; // El Usuario del sistema que este contacto representa
	private String URLImagen;
	
	/**
     * Crea un nuevo ContactoIndividual.
     * El nombre es el alias con el que el usuario actual identifica a este contacto.
     * La URL de la imagen se toma del perfil del {@link Usuario} proporcionado.
     *
     * @param usuario El {@link Usuario} al que este contacto representa. No debe ser nulo.
     * @param nombre  El nombre asignado a este contacto por el usuario actual. No debe ser nulo.
     */
    public ContactoIndividual(Usuario usuario, String nombre) {
        super(nombre); // Llama al constructor de Contacto, que valida el nombre
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario asociado a un ContactoIndividual no puede ser nulo.");
        }
        this.usuario = usuario;
        this.URLImagen = usuario.getURLImagen();
    }
	
    /**
     * Constructor utilizado principalmente para la carga desde persistencia.
     * Permite crear una instancia de {@code ContactoIndividual} solo con su nombre (alias).
     * El {@link Usuario} asociado y su {@code URLImagen} deben establecerse posteriormente
     * mediante {@link #setUsuario(Usuario)}.
     *
     * @param nombre El nombre asignado a este contacto por el usuario actual. No debe ser nulo.
     */
    public ContactoIndividual(String nombre) {
        super(nombre);
        // this.usuario queda como null
        // this.URLImagen queda como null (o el valor por defecto de String)
    }
	

	/**
     * Establece o actualiza el {@link Usuario} asociado a este contacto individual.
     * También actualiza la {@code URLImagen} basándose en el {@link Usuario} proporcionado.
     *
     * @param usuario El {@link Usuario} a asociar. Si es nulo, la {@code URLImagen} se borrará.
     */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
		this.URLImagen = usuario.getURLImagen();
	}
	
	 /**
     * Obtiene el {@link Usuario} asociado a este contacto individual.
     *
     * @return El {@link Usuario} asociado, o {@code null} si no se ha establecido.
     */
	public Usuario getUsuario() {
		return usuario;
	}

	 /**
     * Obtiene la URL de la imagen de perfil para este contacto individual.
     * Esta URL se deriva de la imagen de perfil del {@link Usuario} asociado
     * en el momento de la creación o la última llamada a {@link #setUsuario(Usuario)}.
     *
     * @return La URL de la imagen como un String. Puede ser una cadena vacía o nula
     * si no hay imagen asociada o usuario establecido.
     */
	@Override
	public String getURLImagen() {
		return URLImagen;
	}
	
	

}
