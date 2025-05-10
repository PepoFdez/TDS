package dominio;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import dao.DAOException;
import dao.FactoriaDAO;

/**
 * Repositorio Singleton que gestiona la colección de objetos {@link Usuario} en memoria.
 * Carga todos los usuarios desde la capa de persistencia al iniciarse y proporciona
 * métodos para acceder a ellos.
 * <p>
 * Las operaciones de añadir o eliminar usuarios actualmente solo afectan
 * a la caché en memoria y no se propagan a la capa de persistencia a través
 * de este repositorio. La persistencia de los cambios debe ser manejada
 * externamente.
 * </p>
 */
public enum RepositorioUsuarios {
	/**
     * Única instancia del repositorio.
     */
	INSTANCE;
	
	private FactoriaDAO factoria;
	
	private HashMap<Integer, Usuario> usuariosPorID;
	private HashMap<String, Usuario> usuariosPorMovil;

	/**
     * Constructor privado para el Singleton.
     * Inicializa las cachés de usuarios y carga todos los usuarios desde la
     * capa de persistencia.
     */
	private RepositorioUsuarios (){
		usuariosPorID = new HashMap<Integer, Usuario>();
		usuariosPorMovil = new HashMap<String, Usuario>();
		//facil de aplicar aquí streams
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
	
	/**
     * Devuelve una lista de todos los usuarios actualmente en el repositorio.
     * La lista devuelta es una copia, por lo que las modificaciones a esta lista
     * no afectan la caché interna del repositorio.
     *
     * @return Una nueva {@link List} con todos los usuarios. Puede estar vacía si no hay usuarios
     * o si la carga inicial falló.
     */
	public List<Usuario> findUsuarios() throws DAOException {
		return new LinkedList<Usuario>(usuariosPorMovil.values());
	}
	
	/**
     * Busca un usuario en la caché por su número de móvil.
     *
     * @param movil El número de móvil del usuario a buscar. No debe ser nulo.
     * @return El {@link Usuario} si se encuentra, o {@code null} si no existe.
     * @throws NullPointerException si {@code movil} es nulo.
     */
	public Usuario findUsuario(String movil) {
		return usuariosPorMovil.get(movil);
	}

	/**
     * Busca un usuario en la caché por su ID.
     *
     * @param id El ID del usuario a buscar.
     * @return El {@link Usuario} si se encuentra, o {@code null} si no existe.
     */
	public Usuario findUsuario(int id) {
		return usuariosPorID.get(id);
	}
	
	/**
     * Añade un usuario a la caché del repositorio.
     * Este método solo actualiza la caché en memoria. No persiste el usuario.
     *
     * @param usuario El {@link Usuario} a añadir. No debe ser nulo.
     * @throws NullPointerException si {@code usuario} o su móvil son nulos.
     */
	public void addUsuario(Usuario usuario) {
		usuariosPorID.put(usuario.getId(), usuario);
		usuariosPorMovil.put(usuario.getMovil(), usuario);
	}
	
	/**
     * Elimina un usuario de la caché del repositorio.
     * Este método solo actualiza la caché en memoria. No elimina el usuario de la persistencia.
     *
     * @param usuario El {@link Usuario} a eliminar. No debe ser nulo.
     * @throws NullPointerException si {@code usuario} o su móvil son nulos.
     */
	public void removeUsuario(Usuario usuario) {
		usuariosPorID.remove(usuario.getId());
		usuariosPorMovil.remove(usuario.getMovil());
	}

}
