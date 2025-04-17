package controlador;
import dao.UsuarioDAO;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTextField;

import dao.ContactoIndividualDAO;
import dao.DAOException;
import dao.FactoriaDAO;
import dao.GrupoDAO;
import dao.MensajeDAO;
import dominio.Usuario;
import utils.Utils;
import dominio.Contacto;
import dominio.ContactoIndividual;
import dominio.Mensaje;
import dominio.RepositorioUsuarios;

public enum Controlador {
	INSTANCE;
	private Usuario usuarioActual;
	private FactoriaDAO factoria;
	
	//adaptadores
	private UsuarioDAO usuarioDAO;
	private ContactoIndividualDAO contactoIndividualDAO;
	private MensajeDAO mensajeDAO;
	private GrupoDAO grupoDAO;
	
	//repo
	private RepositorioUsuarios repositorioUsuarios;

	private Controlador() {
		usuarioActual = null;
		try {
			//por defecto usará la factoría TDS, pero sería simple crear un método sobrecargado para usar otro tipo de persistencia
			factoria = FactoriaDAO.getInstancia();
			
			usuarioDAO = factoria.getUsuarioDAO();
			contactoIndividualDAO = factoria.getContactoIndividualDAO();
			mensajeDAO = factoria.getMensajeDAO();
			grupoDAO = factoria.getGrupoDAO();
			
			repositorioUsuarios = RepositorioUsuarios.INSTANCE;
		} catch (DAOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error al inicializar el controlador", e);
		}
	}

	public Usuario getUsuarioActual() {
		return usuarioActual;
	}
	
	public String getNombreUsuario() {
		if (usuarioActual != null) {
			return usuarioActual.getNombre();
		}
		return null;
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
			String fechaNacimiento, String saludo, String imagen) {

		if (esUsuarioRegistrado(movil)) {
			System.err.println("Numero de teléfono ya está registrado");
			return false;
		}
			
		Usuario.Builder userBuilder = new Usuario.Builder(nombre, apellidos, email, movil, password, 
				LocalDate.parse(fechaNacimiento, Utils.formatoFecha));
		if (imagen != null) userBuilder.addURLimagen(imagen);
		if (saludo != null) userBuilder.addSaludo(saludo);
		
		Usuario usuario = userBuilder.build();
		//Usuario usuario = new Usuario(nombre, apellidos, email, movil, password, fechaNacimiento, saludo, imagen, isPremium);
		
		usuarioDAO.registrarUsuario(usuario);
		repositorioUsuarios.addUsuario(usuario);

		return true;
	}
/* se puede borrar cuenta de usuario???
 * 
	public boolean borrarUsuario(Usuario usuario) {
		if (!esUsuarioRegistrado(usuario.getMovil()))
			return false;

		UsuarioDAO usuarioDAO = factoria.getUsuarioDAO();  //Adaptador DAO para borrar el Usuario de la BD 
		usuarioDAO.delete(usuario);

		RepositorioUsuarios.INSTANCE.removeUsuario(usuario);
		return true;
	}
	*/
	public LinkedList<Contacto> getContactosUsuario() {
		this.usuarioActual.getContactos();
		return new LinkedList<Contacto>();
	}

	public String getURLImagenContacto(Contacto contacto) {
		
		return contacto.getURLImagen();
	}

	public List<Object> getContenidoMensajes(Contacto contacto) {
		return contacto.getTextoMensajesEnviados();
		
	}

	public List<String> getInfoMensajes(Contacto contacto) {
		return contacto.getInfoMensajesEnviados();
	}

	public void enviarMensaje(int id, String contenido) {
		// TODO Auto-generated method stub
		
	}

	public void enviarEmoji(int id, int emojiId) {
		// TODO Auto-generated method stub
		
	}

	public LinkedList<Contacto> buscarContactos(String textoBusqueda) {
		// TODO Auto-generated method stub
		return null;
	}

	public LinkedList<Mensaje> buscarMensajes(String texto, String telefono, Integer contactoId) {
		// TODO Auto-generated method stub
		return null;
	}

	public String crearContacto(String nombre, String movil) {		
		//Comprobamos que no existe un contacto con el número 
		if (this.usuarioActual.tieneContactoConMovil(movil)) {
		    return "Ya existe un contacto con este número de móvil.";
		} else {
			Contacto contacto = new ContactoIndividual(RepositorioUsuarios.INSTANCE.findUsuario(movil), nombre);
			contactoIndividualDAO.registrarContactoIndividual((ContactoIndividual)contacto);
			this.usuarioActual.addContacto(contacto);
			usuarioDAO.updateUsuario(usuarioActual);
			return "Contacto creado correctamente.";
		}
	}
}
