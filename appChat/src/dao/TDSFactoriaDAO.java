package dao;

/** 
 * Factoria concreta DAO para el Servidor de Persistencia de la asignatura TDS.
 * 
 */

public final class TDSFactoriaDAO extends FactoriaDAO {
	
	public TDSFactoriaDAO() {	}
	
	@Override
	public UsuarioDAO getUsuarioDAO() {	
		return (UsuarioDAO) TDSUsuarioDAO.getInstance();
	}
	@Override
	public GrupoDAO getGrupoDAO() {
		
		return (TDSGrupoDAO) TDSGrupoDAO.getInstance();
	}
	@Override
	public MensajeDAO getMensajeDAO() {

		return (MensajeDAO) TDSMensajeDAO.getInstance();
	}
	@Override
	public ContactoIndividualDAO getContactoIndividualDAO() {
		
		return (TDSContactoIndividualDAO) TDSContactoIndividualDAO.getInstance();
	}

}
