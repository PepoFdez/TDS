package dao;

/**
 * Factoria abstracta DAO- > crea factorías DAO concretas.
 */

public abstract class FactoriaDAO {

	public static final String DAO_TDS = "dao.TDSFactoriaDAO";

	private static FactoriaDAO unicaInstancia = null;

	/**
	 * Crea un tipo de factoria DAO.
	 * Solo existe el tipo TDSFactoriaDAO
	 * @param tipo El tipo de factoría a crear (actualmente solo soporta DAO_TDS).
	 * @return Una instancia de la factoría DAO especificada.
	 * @throws DAOException Si ocurre un error al instanciar la factoría.
	 */
	public static FactoriaDAO getInstancia(String tipo) throws DAOException{
		if (unicaInstancia == null)
			try {
				System.out.println("Intentando cargar la factoría: " + tipo);
				unicaInstancia = (FactoriaDAO) Class.forName(tipo).getDeclaredConstructor().newInstance();

			} catch (Exception e) {
				throw new DAOException("Error al instanciar la factoría DAO: " + e.getMessage());
		}
		return unicaInstancia;
	}


	/**
	 * Obtiene la instancia única de la factoría DAO por defecto (TDSFactoriaDAO).
	 * @return La instancia única de FactoriaDAO.
	 * @throws DAOException Si ocurre un error al instanciar la factoría.
	 */
	public static FactoriaDAO getInstancia() throws DAOException{
		return getInstancia(FactoriaDAO.DAO_TDS);
	}

	/**
	 * Constructor protegido para evitar la instanciación directa.
	 */
	protected FactoriaDAO (){}

	// Metodos factoria para obtener adaptadores

	/**
	 * Método abstracto para obtener la implementación DAO de Usuario.
	 * @return La implementación de UsuarioDAO.
	 */
	public abstract UsuarioDAO getUsuarioDAO();

	/**
	 * Método abstracto para obtener la implementación DAO de Grupo.
	 * @return La implementación de GrupoDAO.
	 */
	public abstract GrupoDAO getGrupoDAO();

	/**
	 * Método abstracto para obtener la implementación DAO de Mensaje.
	 * @return La implementación de MensajeDAO.
	 */
	public abstract MensajeDAO getMensajeDAO();

	/**
	 * Método abstracto para obtener la implementación DAO de ContactoIndividual.
	 * @return La implementación de ContactoIndividualDAO.
	 */
	public abstract ContactoIndividualDAO getContactoIndividualDAO();
}