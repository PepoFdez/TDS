package controlador;
import dao.UsuarioDAO;

import java.util.LinkedList;
import java.util.List;

import dao.DAOException;
import dao.FactoriaDAO;
import dominio.Usuario;
import dominio.Contacto;
import dominio.RepositorioUsuarios;

public enum Controlador {
	INSTANCE;
	private Usuario usuarioActual;
	private FactoriaDAO factoria;

	private Controlador() {
		usuarioActual = null;
		try {
			factoria = FactoriaDAO.getInstancia();
		} catch (DAOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error al inicializar la factoría DAO", e);
		}
	}

	public Usuario getUsuarioActual() {
		return usuarioActual;
	}

	public boolean esUsuarioRegistrado(String movil) {
		return RepositorioUsuarios.INSTANCE.findUsuario(movil) != null;
	}

	public boolean loginUsuario(String movil, String password) {
		Usuario usuario = RepositorioUsuarios.INSTANCE.findUsuario(movil);
		if (usuario != null && usuario.getPassword().equals(password)) {
			this.usuarioActual = usuario;
			return true;
		}
		return false;
	}

	public boolean registrarUsuario(String nombre, String apellidos, String email, String movil, String password,
			String fechaNacimiento, String saludo, String imagen, String isPremium) {

		if (esUsuarioRegistrado(movil))
			return false;
		Usuario usuario = new Usuario(nombre, apellidos, email, movil, password, fechaNacimiento, saludo, imagen, isPremium);

		UsuarioDAO usuarioDAO = factoria
				.getUsuarioDAO(); /* Adaptador DAO para almacenar el nuevo Usuario en la BD */
		usuarioDAO.create(usuario);

		RepositorioUsuarios.INSTANCE.addUsuario(usuario);
		return true;
	}

	public boolean borrarUsuario(Usuario usuario) {
		if (!esUsuarioRegistrado(usuario.getMovil()))
			return false;

		UsuarioDAO usuarioDAO = factoria.getUsuarioDAO(); /* Adaptador DAO para borrar el Usuario de la BD */
		usuarioDAO.delete(usuario);

		RepositorioUsuarios.INSTANCE.removeUsuario(usuario);
		return true;
	}
	
	public LinkedList<Contacto> getContactosUsuario() {
		this.usuarioActual.getContactos();
		return new LinkedList<Contacto>();
	}
}
