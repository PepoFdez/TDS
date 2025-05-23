package dominio;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Representa un grupo de contactos (lista de difusión) en la agenda del usuario.
 * Un grupo es un tipo de {@link Contacto} y tiene su propio nombre, imagen y
 * un historial de mensajes que se han enviado a través de él.
 * Los mensajes enviados a un grupo se distribuyen individualmente a cada uno de sus miembros.
 * Los miembros de un grupo deben ser instancias de {@link AgregableGrupos} (actualmente {@link ContactoIndividual}).
 */
public class Grupo extends Contacto {

    // Se usa Set para asegurar que no haya miembros duplicados.
    private final Set<Contacto> miembros;
    private String URLImagen;

    /**
     * Crea un nuevo grupo con un nombre y una lista inicial de miembros.
     * La URL de la imagen del grupo se inicializa como vacía.
     *
     * @param nombre   El nombre del grupo. No debe ser nulo.
     * @param miembros Varargs de {@link AgregableGrupos} que serán los miembros iniciales del grupo.
     * Si no se proporcionan miembros, el grupo se crea vacío.
     * Los miembros nulos en el array varargs serán ignorados por el stream.
     */
    public Grupo(String nombre, AgregableGrupos... miembros) {
        super(nombre); // Llama al constructor de Contacto, que valida el nombre.
        this.miembros = (miembros == null) ? new HashSet<>() :
                Arrays.stream(miembros)
                      .filter(Objects::nonNull) // Ignorar miembros nulos en la entrada
                      .map(miembro -> (Contacto) miembro) // Cast a Contacto
                      .collect(Collectors.toCollection(HashSet::new));
        this.URLImagen = ""; // Por defecto, el grupo no tiene imagen.
    }

    /**
     * Constructor para crear un grupo a partir de datos existentes (ej. persistencia),
     * incluyendo su historial de mensajes.
     * La lista de miembros se inicializa vacía y debe poblarse por separado.
     *
     * @param nombre   El nombre del grupo.
     * @param mensajes La lista de mensajes previamente enviados a este grupo.
     */
    public Grupo(String nombre, List<Mensaje> mensajes) {
        super(nombre, mensajes);
        this.miembros = new HashSet<>();
        this.URLImagen = ""; // Asumimos que la imagen también se carga por separado si existe.
    }

    /**
     * Añade un miembro al grupo. El miembro debe ser una instancia de {@link AgregableGrupos}.
     *
     * @param miembro El contacto a añadir como miembro. No debe ser nulo.
     * @return {@code true} si el miembro fue añadido exitosamente (es decir, no estaba ya presente),
     * {@code false} en caso contrario.
     * @throws NullPointerException si {@code miembro} es nulo.
     * @throws IllegalArgumentException si el miembro no es una instancia de {@link Contacto}. (Aunque AgregableGrupos lo es via ContactoIndividual)
     */
    public boolean addMiembro(AgregableGrupos miembro) {
        Objects.requireNonNull(miembro, "El miembro a añadir no puede ser nulo.");
        return this.miembros.add((Contacto) miembro);
    }

    /**
     * Obtiene una nueva lista de los miembros de este grupo.
     *
     * @return Una nueva lista ({@link LinkedList}) conteniendo los miembros del grupo.
     * Devuelve una lista vacía si el grupo no tiene miembros.
     */
    public List<Contacto> getMiembros() {
        // Devolver una copia para proteger la encapsulación del Set interno.
        return new LinkedList<>(this.miembros);
    }

    /**
     * Elimina un miembro del grupo.
     *
     * @param miembro El {@link Contacto} a eliminar del grupo. No debe ser nulo.
     * @return {@code true} si el miembro fue encontrado y eliminado, {@code false} en caso contrario.
     * @throws NullPointerException si {@code miembro} es nulo.
     * Actualmente, no permite que el grupo quede vacío; si se intenta eliminar
     * el último miembro, la operación fallará (devolverá {@code false}).
     * Considerar lanzar {@link IllegalStateException} si la regla es que un grupo no puede estar vacío.
     */
    public boolean removeMiembro(Contacto miembro) {
        Objects.requireNonNull(miembro, "El miembro a eliminar no puede ser nulo.");

        if (this.miembros.contains(miembro) && this.miembros.size() == 1) {
            return false;
        }
        return this.miembros.remove(miembro);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Al añadir un mensaje a un grupo, el mensaje también se propaga y se añade
     * al historial de cada uno de los miembros individuales del grupo.
     * </p>
     *
     * @param mensaje El {@link Mensaje} a añadir y distribuir. No debe ser nulo.
     */
    @Override
    public Mensaje addMensaje(Object mensaje, int tipo) {
        Mensaje m = super.addMensaje(mensaje, tipo); // Añade el mensaje al historial del propio grupo.
        Objects.requireNonNull(mensaje, "El mensaje a propagar no puede ser nulo.");
        // Propaga el mensaje a todos los miembros.
        this.miembros.forEach(miembro -> miembro.addMensaje(mensaje, tipo));
        return m;
    }

    /**
     * Establece la URL de la imagen de perfil para este grupo.
     *
     * @param urlImagen La nueva URL de la imagen. Se recomienda una cadena vacía si no hay imagen.
     */
    public void setURLImagen(String urlImagen) {
        this.URLImagen = (urlImagen != null) ? urlImagen : "";
    }

    /**
     * Obtiene la URL de la imagen de perfil de este grupo.
     *
     * @return La URL de la imagen del grupo. Puede ser una cadena vacía si no tiene imagen.
     */
    @Override
    public String getURLImagen() {
        return (this.URLImagen != null) ? this.URLImagen : "";
    }

	public void setMiembros(LinkedList<ContactoIndividual> nuevosMiembros) {
		this.miembros.clear();
		this.miembros.addAll(nuevosMiembros);
	}

	@Override
	public String toString() {
		return "Grupo{" +
				"nombre='" + getNombre() + '\'' +
				", miembros=" + miembros +
				", URLImagen='" + URLImagen + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Grupo)) return false;
		Grupo grupo = (Grupo) o;
		return Objects.equals(getNombre(), grupo.getNombre()) &&
			   Objects.equals(miembros, grupo.miembros) &&
			   Objects.equals(URLImagen, grupo.URLImagen);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getNombre(), miembros, URLImagen);
		
	}

}