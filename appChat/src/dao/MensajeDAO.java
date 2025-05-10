package dao;

import java.util.List;
import dominio.Mensaje;

/**
 * Interfaz para el acceso a datos de la entidad Mensaje.
 * Define los métodos para interactuar con la persistencia de los objetos Mensaje.
 */
public interface MensajeDAO {

	/**
	 * Registra un nuevo mensaje en la capa de persistencia.
	 * @param msj El objeto Mensaje a registrar.
	 */
	void registrarMensaje(Mensaje msj);

	/**
	 * Elimina un mensaje de la capa de persistencia.
	 * @param msj El objeto Mensaje a eliminar.
	 */
	void eliminarMensaje(Mensaje msj);

	//void updateMensaje(Mensaje msj); // Método comentado en el código original.

	/**
	 * Recupera un mensaje de la capa de persistencia dado su identificador.
	 * @param id El identificador único del mensaje.
	 * @return El objeto Mensaje correspondiente al id, o null si no se encuentra.
	 */
	Mensaje getMensaje(int id);

	/**
	 * Recupera todos los mensajes existentes en la capa de persistencia.
	 * @return Una lista de todos los objetos Mensaje.
	 */
	List<Mensaje> getAll();

}