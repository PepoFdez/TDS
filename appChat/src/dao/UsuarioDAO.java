package dao;

import java.util.List;
import dominio.Usuario;

public interface UsuarioDAO {
	
	void registrarUsuario(Usuario usuario);
	void eliminarUsuario(Usuario usuario);
	void updateUsuario(Usuario usuario);
	Usuario getUsuario(int id);
	List<Usuario> getAll();
	
}
