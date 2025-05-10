package dao;

import java.util.List;
import dominio.Usuario;

/**
 * Interfaz para el acceso a datos de la entidad Usuario.
 * Define los métodos para interactuar con la persistencia de los objetos Usuario.
 */
public interface UsuarioDAO {

	/**
	 * Registra un nuevo usuario en la capa de persistencia.
	 * @param usuario El objeto Usuario a registrar.
	 */
	void registrarUsuario(Usuario usuario);

	/**
	 * Elimina un usuario de la capa de persistencia.
	 * @param usuario El objeto Usuario a eliminar.
	 */
	void eliminarUsuario(Usuario usuario);

	/**
	 * Actualiza la información de un usuario en la capa de persistencia.
	 * @param usuario El objeto Usuario con la información actualizada.
	 */
	void updateUsuario(Usuario usuario);

	/**
	 * Recupera un usuario de la capa de persistencia dado su identificador.
	 * @param id El identificador único del usuario.
	 * @return El objeto Usuario correspondiente al id, o null si no se encuentra.
	 */
	Usuario getUsuario(int id);

	/**
	 * Recupera todos los usuarios existentes en la capa de persistencia.
	 * @return Una lista de todos los objetos Usuario.
	 */
	List<Usuario> getAll();

}