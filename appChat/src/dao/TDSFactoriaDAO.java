package dao;

/**
 * Factoria concreta DAO para el Servidor de Persistencia de la asignatura TDS.
 *
 */

public final class TDSFactoriaDAO extends FactoriaDAO {

	/**
	 * Constructor por defecto.
	 */
	public TDSFactoriaDAO() {	}

	/**
	 * Obtiene la implementación DAO para Usuario.
	 * @return La instancia de UsuarioDAO.
	 */
	@Override
	public UsuarioDAO getUsuarioDAO() {
		return (UsuarioDAO) TDSUsuarioDAO.getInstance();
	}

	/**
	 * Obtiene la implementación DAO para Grupo.
	 * @return La instancia de GrupoDAO.
	 */
	@Override
	public GrupoDAO getGrupoDAO() {

		return (TDSGrupoDAO) TDSGrupoDAO.getInstance();
	}

	/**
	 * Obtiene la implementación DAO para Mensaje.
	 * @return La instancia de MensajeDAO.
	 */
	@Override
	public MensajeDAO getMensajeDAO() {

		return (MensajeDAO) TDSMensajeDAO.getInstance();
	}

	/**
	 * Obtiene la implementación DAO para ContactoIndividual.
	 * @return La instancia de ContactoIndividualDAO.
	 */
	@Override
	public ContactoIndividualDAO getContactoIndividualDAO() {

		return (TDSContactoIndividualDAO) TDSContactoIndividualDAO.getInstance();
	}

}