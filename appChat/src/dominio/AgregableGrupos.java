package dominio;

/**
 * Interfaz marcadora para definir los tipos de objetos que pueden ser agregados
 * a un {@link Grupo}.
 * <p>
 * Actualmente, se utiliza para permitir que instancias de {@link ContactoIndividual}
 * puedan ser miembros de un grupo. El diseño permite una futura extensión
 * si se deseara, por ejemplo, que grupos puedan contener otros grupos.
 * </p>
 */
public interface AgregableGrupos {
    // Interfaz marcadora, no define métodos.
}