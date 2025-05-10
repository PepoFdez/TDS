package dao;

import java.util.List;

import dominio.Grupo;


/**
 * Interfaz para el acceso a datos de la entidad Grupo.
 * Define los métodos para interactuar con la persistencia de los objetos Grupo.
 */
public interface GrupoDAO{

	/**
	 * Registra un nuevo grupo en la capa de persistencia.
	 * @param grupo El objeto Grupo a registrar.
	 */
	void registrarGrupo(Grupo grupo);

	/**
	 * Elimina un grupo de la capa de persistencia.
	 * @param grupo El objeto Grupo a eliminar.
	 */
	void eliminarGrupo(Grupo grupo);

	/**
	 * Actualiza la información de un grupo en la capa de persistencia.
	 * @param grupo El objeto Grupo con la información actualizada.
	 */
	void updateGrupo(Grupo grupo);

	/**
	 * Recupera un grupo de la capa de persistencia dado su identificador.
	 * @param id El identificador único del grupo.
	 * @return El objeto Grupo correspondiente al id, o null si no se encuentra.
	 */
	Grupo getGrupo(int id);

	/**
	 * Recupera todos los grupos existentes en la capa de persistencia.
	 * @return Una lista de todos los objetos Grupo.
	 */
	List<Grupo> getAll();
}