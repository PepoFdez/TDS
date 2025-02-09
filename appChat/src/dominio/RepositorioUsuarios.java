package dominio;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import dao.DAOException;
import dao.FactoriaDAO;

public enum RepositorioUsuarios {
	INSTANCE;
	private FactoriaDAO factoria;

	private HashMap<Integer, Usuario> usuariosPorID;
	private HashMap<String, Usuario> usuariosPorMovil;

	private RepositorioUsuarios (){
		usuariosPorID = new HashMap<Integer, Usuario>();
		usuariosPorMovil = new HashMap<String, Usuario>();
		
		try {
			factoria = FactoriaDAO.getInstancia();
			
			List<Usuario> listausuarios = factoria.getUsuarioDAO().getAll();
			for (Usuario usuario : listausuarios) {
				usuariosPorID.put(usuario.getId(), usuario);
				usuariosPorMovil.put(usuario.getMovil(), usuario);
			}
		} catch (DAOException eDAO) {
			   eDAO.printStackTrace();
		}
	}
	
	public List<Usuario> findUsuarios() throws DAOException {
		return new LinkedList<Usuario>(usuariosPorMovil.values());
	}
	
	public Usuario findUsuario(String login) {
		return usuariosPorMovil.get(login);
	}

	public Usuario findUsuario(int id) {
		return usuariosPorID.get(id);
	}
	
	public void addUsuario(Usuario usuario) {
		usuariosPorID.put(usuario.getId(), usuario);
		usuariosPorMovil.put(usuario.getMovil(), usuario);
	}
	
	public void removeUsuario(Usuario usuario) {
		usuariosPorID.remove(usuario.getId());
		usuariosPorMovil.remove(usuario.getMovil());
	}

}
