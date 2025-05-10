package dao;

@SuppressWarnings("serial")
/**
 * Excepción personalizada para manejar errores en la capa de acceso a datos (DAO).
 */
public class DAOException extends Exception {

	/**
	 * Constructor de DAOException.
	 * @param mensaje El mensaje de error asociado a la excepción.
	 */
	public DAOException(final String mensaje) {
		super(mensaje);
	}

}