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
	

	public static FactoriaDAO getInstancia() throws DAOException{
		return getInstancia(FactoriaDAO.DAO_TDS);
	}

	protected FactoriaDAO (){}
	
	// Metodos factoria para obtener adaptadores
	
	public abstract UsuarioDAO getUsuarioDAO();
	public abstract GrupoDAO getGrupoDAO();
	public abstract MensajeDAO getMensajeDAO();
	public abstract ContactoIndividualDAO getContactoIndividualDAO();
}
