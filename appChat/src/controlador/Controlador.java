package controlador;
import dao.UsuarioDAO;


import java.time.LocalDate;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;


import dao.ContactoIndividualDAO;
import dao.DAOException;
import dao.FactoriaDAO;
import dao.GrupoDAO;
import dao.MensajeDAO;
import dominio.Usuario;
import tds.BubbleText;
import utils.Utils;
import dominio.Contacto;
import dominio.ContactoIndividual;
import dominio.Grupo;
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
		LocalDate fecha = null;
		if (!fechaNacimiento.equals("")) {
			fecha = LocalDate.parse(fechaNacimiento, Utils.formatoFecha);	
		} else {
			fecha = LocalDate.parse("01/01/1970", Utils.formatoFecha);
		}	
		Usuario.Builder userBuilder = new Usuario.Builder(nombre, apellidos, email, movil, password, 
				fecha);
		if (imagen != null) userBuilder.addURLimagen(imagen);
		if (saludo != null) userBuilder.addSaludo(saludo);
		
		Usuario usuario = userBuilder.build();
		
		usuarioDAO.registrarUsuario(usuario);
		repositorioUsuarios.addUsuario(usuario);

		return true;
	}
/* se puede borrar cuenta de usuario???
 * 
	public boolean borrarUsuario(Usuario usuario) {
			return false;

		UsuarioDAO usuarioDAO = factoria.getUsuarioDAO();  //Adaptador DAO para borrar el Usuario de la BD 
		usuarioDAO.delete(usuario);

		RepositorioUsuarios.INSTANCE.removeUsuario(usuario);
		return true;
	}
	*/
	public LinkedList<Contacto> getContactosUsuario() {
		return this.usuarioActual.getContactos();
	}
	
	public LinkedList<ContactoIndividual> getContactosIndividualesUsuario() {
		return (LinkedList<ContactoIndividual>) this.usuarioActual.getContactosIndividuales();
	}

	public String getURLImagenContacto(Contacto contacto) {
		
		return contacto.getURLImagen();
	}
	
	public String getURLImagenUsuario() {
		return this.usuarioActual.getURLImagen();
	}

	public List<Object> getContenidoMensajes(Contacto contacto) {
		return contacto.getTextoMensajesEnviados();
	}

	public List<String> getInfoMensajes(Contacto contacto) {
		return contacto.getInfoMensajesEnviados();
	}

	public void enviarMensaje(int id, String contenido) {
		Mensaje mensajeSent = new Mensaje(contenido, BubbleText.SENT);
		mensajeDAO.registrarMensaje(mensajeSent);

		usuarioDAO.updateUsuario(usuarioActual);
		//Añadir el contacto al usuario actual como enviado, se recibe el contacto por su id
		Contacto receptor = this.usuarioActual.enviarMensaje(mensajeSent, id);

		//Añadir el mensaje al usuario asociado al contacto como recibido
		//Si es un grupo, se añade a todos los miembros del grupo
		//Si es un contacto individual, se añade al contacto, si existe.
		//Si no existe, creamos el contacto con el número de teléfono del emisor como nombre
		if (receptor instanceof ContactoIndividual contactoIndividual) {
			contactoIndividualDAO.updateContactoIndividual((ContactoIndividual) receptor);
			recibirMensaje(contenido, -1, contactoIndividual);
		} else if (receptor instanceof Grupo grupo) {
			grupoDAO.updateGrupo((Grupo) receptor);
			for (Contacto miembro : grupo.getMiembros()) {
				contactoIndividualDAO.updateContactoIndividual((ContactoIndividual) miembro);
				if (miembro instanceof ContactoIndividual contactoIndividualMiembro) {
					//Si el miembro es un contacto individual, se añade el mensaje al contacto como recibido
					recibirMensaje(contenido, -1, contactoIndividualMiembro);
				} 
			}
		}
		
	}
	
	public void enviarEmoji(int id, int emojiId) {
		Mensaje mensajeSent = new Mensaje(emojiId, BubbleText.SENT);
		mensajeDAO.registrarMensaje(mensajeSent);
		//Añadir el contacto al usuario actual como enviado
		Contacto receptor = this.usuarioActual.enviarMensaje(mensajeSent, id);
		if (receptor instanceof ContactoIndividual contactoIndividual) {
			contactoIndividualDAO.updateContactoIndividual((ContactoIndividual) receptor);
			recibirMensaje("", emojiId, contactoIndividual);
		} else if (receptor instanceof Grupo grupo) {
			grupoDAO.updateGrupo((Grupo) receptor);
			for (Contacto miembro : grupo.getMiembros()) {
				if (miembro instanceof ContactoIndividual contactoIndividualMiembro) {
					//Si el miembro es un contacto individual, se añade el mensaje al contacto como recibido
					recibirMensaje("", emojiId, contactoIndividualMiembro);
				} 
			}
		}
	}

	//Recibe un mensaje enviado por el usuario actual y hace que aparezca en el usuario receptor
	private void recibirMensaje (String contenido, int emoji, ContactoIndividual receptor) {
		Mensaje mensajeRec = null;
		if (emoji != -1) {
			mensajeRec = new Mensaje(emoji, BubbleText.RECEIVED);
		} else {
			mensajeRec = new Mensaje(contenido, BubbleText.RECEIVED);
		}
		
		//mensajeDAO.registrarMensaje(mensajeRec);
		//Añadir el mensaje al usuario asociado al contacto como recibido
		//Si es un grupo, se añade a todos los miembros del grupo
		//Si es un contacto individual, se añade al contacto, si existe.
		//Si no existe, creamos el contacto con el número de teléfono del emisor como nombre
		boolean existe = receptor.getUsuario().tieneContactoConMovil(usuarioActual.getMovil());
		//Si existe, se añade el mensaje al contacto como recibido
		if (existe) {
			Contacto opuestoContacto = receptor.getUsuario().getContactoConMovil(usuarioActual.getMovil());
			opuestoContacto.addMensaje(mensajeRec);
			mensajeDAO.registrarMensaje(mensajeRec);
			contactoIndividualDAO.updateContactoIndividual((ContactoIndividual) opuestoContacto);
			//usuarioDAO.updateUsuario(receptor.getUsuario());
		} else {
			//Si no existe, se crea el contacto con el número de teléfono del emisor como nombre
			// y se añade el mensaje al contacto como recibido
			//Contacto nuevoContacto = new ContactoIndividual(usuarioActual, this.usuarioActual.getNombre());
			Contacto nuevoContacto = new ContactoIndividual(usuarioActual, this.usuarioActual.getMovil());
			contactoIndividualDAO.registrarContactoIndividual((ContactoIndividual) nuevoContacto);
			nuevoContacto.addMensaje(mensajeRec);
			mensajeDAO.registrarMensaje(mensajeRec);
			receptor.getUsuario().addContacto(nuevoContacto);
			contactoIndividualDAO.updateContactoIndividual((ContactoIndividual) nuevoContacto);
			usuarioDAO.updateUsuario(receptor.getUsuario());
		}
	}
		

	public LinkedList<String> buscarMensajes(String texto, String telefono, String nContacto) {
		LinkedList<String> mensajes = new LinkedList<>();
		if (texto != null && !texto.isEmpty()) {
			for (Contacto contacto : this.usuarioActual.getContactos()) {
				for (Mensaje mensaje : contacto.getMensajesEnviados()) {
					if (mensaje.getTexto().contains(texto)) {
						mensajes.add(contacto.getNombre() + " " + mensaje.getFecha() + ": " +  mensaje.getTexto());
					}
				}
			}
		} else if (telefono != null && !telefono.isEmpty()) {
			for (Contacto contacto : this.usuarioActual.getContactos()) {
				if (((ContactoIndividual) contacto).getUsuario().getMovil().equals(telefono)) {
					for (Mensaje mensaje : contacto.getMensajesEnviados()) {
						mensajes.add(contacto.getNombre() + " " + mensaje.getFecha() + ": " +  mensaje.getTexto());
					}
				}
			}
		} else if (nContacto != null && !nContacto.isEmpty()) {
			for (Contacto contacto : this.usuarioActual.getContactos()) {
				if (contacto.getNombre().contains(nContacto)) {
					for (Mensaje mensaje : contacto.getMensajesEnviados()) {
						mensajes.add(contacto.getNombre() + " " + mensaje.getFecha() + ": " +  mensaje.getTexto());
					}
				}
			}
		}
		return mensajes;
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
	
	public boolean crearGrupo(String nombreGrupo, LinkedList<ContactoIndividual> miembros) {
		//Comprobamos que no existe un grupo con el mismo nombre
		Grupo grupo = new Grupo(nombreGrupo, miembros.toArray(new ContactoIndividual[0]));
		grupoDAO.registrarGrupo(grupo);
		this.usuarioActual.addContacto(grupo);
		usuarioDAO.updateUsuario(usuarioActual);
		return true;
	}

	public boolean isUsuarioPremium() {
		return this.usuarioActual.isPremium();
	}

	public boolean convertirPremium() {
		this.usuarioActual.activarPremium();
		usuarioDAO.updateUsuario(usuarioActual);
		return true;
	}

	public boolean exportarChatPDF(Contacto contactoSeleccionado) {
		utils.ExportPDF.exportChatToPDF(contactoSeleccionado.getMensajesEnviados(), 
				"resources/" + contactoSeleccionado.getNombre() + ".pdf", contactoSeleccionado.getNombre());
		return true;
	}

	public boolean anularPremium() {
		this.usuarioActual.desactivarPremium();
		usuarioDAO.updateUsuario(usuarioActual);
		return true;
	}

	public String getUltimoMensaje(Contacto contacto) {
		return this.usuarioActual.getUltimoMensaje(contacto);
	}

	public String getTelefono(Contacto contacto) {
		if (contacto instanceof ContactoIndividual) {
			return ((ContactoIndividual) contacto).getUsuario().getMovil();
		} else if (contacto instanceof Grupo) {
			return ("Grupo: " + ((Grupo) contacto).getNombre());
		}
		return null;
	}

	public void modificarNombreContacto(Contacto contacto, String nuevoNombre) {
		contacto.setNombre(nuevoNombre);
		
		if (contacto instanceof ContactoIndividual) {
			contactoIndividualDAO.updateContactoIndividual((ContactoIndividual) contacto);
		} else if (contacto instanceof Grupo) {
			grupoDAO.updateGrupo((Grupo) contacto);
		};
		
		usuarioDAO.updateUsuario(usuarioActual);
	}

	public boolean actualizarUsuario(String nombre, String apellidos, String email, String nuevoPassword, String fechaNacimiento,
			String saludo, String imagen) {
		if (nombre != null && !nombre.isEmpty()) {
			usuarioActual.setNombre(nombre);
		}
		if (apellidos != null && !apellidos.isEmpty()) {
			usuarioActual.setApellidos(apellidos);
		}
		if (email != null && !email.isEmpty()) {
			usuarioActual.setEmail(email);
		}
		if (nuevoPassword != null && !nuevoPassword.isEmpty()) {
			usuarioActual.setPassword(nuevoPassword);
		}
		if (fechaNacimiento != null && !fechaNacimiento.isEmpty()) {
			usuarioActual.setFechaNacimiento(LocalDate.parse(fechaNacimiento, Utils.formatoFecha));
		}
		if (saludo != null && !saludo.isEmpty()) {
			usuarioActual.setSaludo(saludo);
		}
		if (imagen != null && !imagen.isEmpty()) {
			usuarioActual.setURLImagen(imagen);
		}
		usuarioDAO.updateUsuario(usuarioActual);
		return true;
	}

	public List<String> getDatosUsuario() {
		List<String> datos = new LinkedList<>();
		datos.add(usuarioActual.getNombre());
		datos.add(usuarioActual.getApellidos());
		datos.add(usuarioActual.getEmail());
		datos.add(usuarioActual.getMovil());
		datos.add(usuarioActual.getPassword());
		datos.add(usuarioActual.getFechaNacimiento().format(Utils.formatoFecha));
		datos.add(usuarioActual.getSaludo());
		datos.add(usuarioActual.getURLImagen());
		return datos;
	}
	
	public Contacto getContactoConMovil(String movil) {
		return this.usuarioActual.getContactoConMovil(movil);
	}

	public Contacto getGrupoConNombre(String string) {
	 	return this.usuarioActual.getGrupoConNombre(string);
	}

	public String getApellidosUsuario() {
		if (usuarioActual != null) {
			return usuarioActual.getApellidos();
		}
		return null;
	}

	public String getSaludoUsuario() {
		if (usuarioActual != null) {
			return usuarioActual.getSaludo();
		}
		return null;
	}

	public String getTelefonoUsuario() {
		if (usuarioActual != null) {
			return usuarioActual.getMovil();
		}
		return null;
	}

	public String getEmailUsuario() {
		if (usuarioActual != null) {
			return usuarioActual.getEmail();
		}
		return null;
	}

	public Date getFechaNacimientoUsuario() {
		if (usuarioActual != null) {
			return java.sql.Date.valueOf(usuarioActual.getFechaNacimiento());
		}
		return null;
	}

	public Date getFechaCreacionCuentaUsuario() {
		if (usuarioActual != null) {
			return java.sql.Date.valueOf(usuarioActual.getFechaRegistro());
		}
		return null;
	}
}
