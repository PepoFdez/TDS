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
import dto.MensajeContextualizado;
import tds.BubbleText;
import utils.Utils;
import dominio.Contacto;
import dominio.ContactoIndividual;
import dominio.Descuento;
import dominio.FactoriaDescuentos;
import dominio.Grupo;
import dominio.Mensaje;
import dominio.RepositorioUsuarios;

/**
 * Clase singleton que actúa como controlador principal de la aplicación.
 * Gestiona la interacción entre la capa de presentación, el dominio y la capa
 * de acceso a datos.
 */
public enum Controlador {
	INSTANCE;

	private Usuario usuarioActual;
	private FactoriaDAO factoria;

	// adaptadores
	private UsuarioDAO usuarioDAO;
	private ContactoIndividualDAO contactoIndividualDAO;
	private MensajeDAO mensajeDAO;
	private GrupoDAO grupoDAO;

	// repo
	private RepositorioUsuarios repositorioUsuarios;

	private static final double PRECIO_APLICACION = 100;

	/**
	 * Constructor privado del controlador. Inicializa la factoría DAO y los
	 * adaptadores.
	 */
	private Controlador() {
		usuarioActual = null;
		try {
			// por defecto usará la factoría TDS, pero sería simple crear un método
			// sobrecargado para usar otro tipo de persistencia
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

	/**
	 * Obtiene el usuario actualmente autenticado.
	 * 
	 * @return El objeto Usuario del usuario actual, o null si no hay usuario
	 *         autenticado.
	 */
	public Usuario getUsuarioActual() {
		return usuarioActual;
	}

	/**
	 * Obtiene el nombre del usuario actualmente autenticado.
	 * 
	 * @return El nombre del usuario actual, o null si no hay usuario autenticado.
	 */
	public String getNombreUsuario() {
		if (usuarioActual != null) {
			return usuarioActual.getNombre();
		}
		return null;
	}

	/**
	 * Verifica si un número de móvil ya está registrado en el sistema.
	 * 
	 * @param movil El número de móvil a verificar.
	 * @return true si el número de móvil está registrado, false en caso contrario.
	 */
	public boolean esUsuarioRegistrado(String movil) {
		return RepositorioUsuarios.INSTANCE.findUsuario(movil) != null;
	}

	/**
	 * Intenta autenticar a un usuario con el número de móvil y contraseña
	 * proporcionados. Si la autenticación es exitosa, establece el usuario actual.
	 * 
	 * @param movil    El número de móvil del usuario.
	 * @param password La contraseña del usuario.
	 * @return true si la autenticación fue exitosa, false en caso contrario.
	 */
	public boolean loginUsuario(String movil, String password) {
		Usuario usuario = RepositorioUsuarios.INSTANCE.findUsuario(movil);
		if (usuario != null && usuario.getPassword().equals(password)) {
			this.usuarioActual = usuario;
			return true;
		}
		return false;
	}

	/**
	 * Registra un nuevo usuario en el sistema.
	 * 
	 * @param nombre          El nombre del usuario.
	 * @param apellidos       Los apellidos del usuario.
	 * @param email           El email del usuario.
	 * @param movil           El número de móvil del usuario.
	 * @param password        La contraseña del usuario.
	 * @param fechaNacimiento La fecha de nacimiento del usuario en formato String.
	 * @param saludo          El saludo del usuario.
	 * @param imagen          La URL de la imagen de perfil del usuario.
	 * @return true si el registro fue exitoso, false si el número de móvil ya está
	 *         registrado.
	 */
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
		Usuario.Builder userBuilder = new Usuario.Builder(nombre, apellidos, email, movil, password, fecha);
		if (imagen != null)
			userBuilder.addURLimagen(imagen);
		if (saludo != null)
			userBuilder.addSaludo(saludo);

		Usuario usuario = userBuilder.build();

		usuarioDAO.registrarUsuario(usuario);
		repositorioUsuarios.addUsuario(usuario);

		return true;
	}

	/**
	 * Obtiene la lista de contactos del usuario actual.
	 * 
	 * @return Una LinkedList de objetos Contacto.
	 */
	public LinkedList<Contacto> getContactosUsuario() {
		return this.usuarioActual.getContactos();
	}

	/**
	 * Obtiene la lista de contactos individuales del usuario actual.
	 * 
	 * @return Una LinkedList de objetos ContactoIndividual.
	 */
	public LinkedList<ContactoIndividual> getContactosIndividualesUsuario() {
		return (LinkedList<ContactoIndividual>) this.usuarioActual.getContactosIndividuales();
	}

	/**
	 * Obtiene la URL de la imagen de perfil de un contacto.
	 * 
	 * @param contacto El objeto Contacto.
	 * @return La URL de la imagen del contacto.
	 */
	public String getURLImagenContacto(Contacto contacto) {

		return contacto.getURLImagen();
	}

	/**
	 * Obtiene la URL de la imagen de perfil del usuario actual.
	 * 
	 * @return La URL de la imagen del usuario actual.
	 */
	public String getURLImagenUsuario() {
		return this.usuarioActual.getURLImagen();
	}

	/**
	 * Obtiene el contenido de los mensajes enviados en una conversación con un
	 * contacto.
	 * 
	 * @param contacto El objeto Contacto de la conversación.
	 * @return Una lista de objetos que representan el contenido de los mensajes
	 *         (texto o emoticono).
	 */
	public List<Object> getContenidoMensajes(Contacto contacto) {
		return contacto.getTextoMensajesEnviados();
	}

	/**
	 * Obtiene información detallada sobre los mensajes enviados en una conversación
	 * con un contacto.
	 * 
	 * @param contacto El objeto Contacto de la conversación.
	 * @return Una lista de cadenas de texto con información de los mensajes.
	 */
	public List<String> getInfoMensajes(Contacto contacto) {
		return contacto.getInfoMensajesEnviados();
	}

	/**
	 * Envía un mensaje de texto a un contacto o grupo.
	 * 
	 * @param id        El identificador del contacto o grupo receptor.
	 * @param contenido El texto del mensaje.
	 */
	public void enviarMensaje(int id, String contenido) {
		Mensaje mensajeSent = this.usuarioActual.enviarMensaje(id, contenido, BubbleText.SENT);
		mensajeDAO.registrarMensaje(mensajeSent);

		usuarioDAO.updateUsuario(usuarioActual);
		// Añadir el contacto al usuario actual como enviado, se recibe el contacto por
		// su id
		Contacto receptor = this.usuarioActual.getContactoConId(id);

		// Añadir el mensaje al usuario asociado al contacto como recibido
		// Si es un grupo, se añade a todos los miembros del grupo
		// Si es un contacto individual, se añade al contacto, si existe.
		// Si no existe, creamos el contacto con el número de teléfono del emisor como
		// nombre
		if (receptor instanceof ContactoIndividual contactoIndividual) {
			contactoIndividualDAO.updateContactoIndividual((ContactoIndividual) receptor);
			recibirMensaje(contenido, -1, contactoIndividual);
		} else if (receptor instanceof Grupo grupo) {
			grupoDAO.updateGrupo((Grupo) receptor);
			grupo.getMiembros().forEach(miembro -> {
				contactoIndividualDAO.updateContactoIndividual((ContactoIndividual) miembro);
				recibirMensaje(contenido, -1, (ContactoIndividual) miembro);
			});
		}
	}

	/**
	 * Envía un emoticono a un contacto o grupo.
	 * 
	 * @param id      El identificador del contacto o grupo receptor.
	 * @param emojiId El identificador del emoticono.
	 */
	public void enviarEmoji(int id, int emojiId) {
		Mensaje mensajeSent = this.usuarioActual.enviarMensaje(id, emojiId, BubbleText.SENT);
		mensajeDAO.registrarMensaje(mensajeSent);
		// Añadir el contacto al usuario actual como enviado
		Contacto receptor = this.usuarioActual.getContactoConId(id);
		if (receptor instanceof ContactoIndividual contactoIndividual) {
			contactoIndividualDAO.updateContactoIndividual((ContactoIndividual) receptor);
			recibirMensaje("", emojiId, contactoIndividual);
		} else if (receptor instanceof Grupo grupo) {
			grupoDAO.updateGrupo((Grupo) receptor);
			grupo.getMiembros().forEach(miembro -> {
				contactoIndividualDAO.updateContactoIndividual((ContactoIndividual) miembro);
				recibirMensaje("", emojiId, (ContactoIndividual) miembro);
			});
		}
	}

	/**
	 * Recibe un mensaje enviado por el usuario actual y lo registra en el usuario
	 * receptor.
	 * 
	 * @param contenido El texto del mensaje.
	 * @param emoji     El identificador del emoticono (-1 si es mensaje de texto).
	 * @param receptor  El ContactoIndividual que recibe el mensaje.
	 */
	// Recibe un mensaje enviado por el usuario actual y hace que aparezca en el
	// usuario receptor
	private void recibirMensaje(String contenido, int emoji, ContactoIndividual receptor) {
		/*
		 * Mensaje mensajeRec = null; //TODO: ESTE DIRíA QUE ESTÁ BIEN if (emoji != -1)
		 * { mensajeRec = new Mensaje(emoji, BubbleText.RECEIVED); } else { mensajeRec =
		 * new Mensaje(contenido, BubbleText.RECEIVED); }
		 */

		// mensajeDAO.registrarMensaje(mensajeRec);
		// Añadir el mensaje al usuario asociado al contacto como recibido
		// Si es un grupo, se añade a todos los miembros del grupo
		// Si es un contacto individual, se añade al contacto, si existe.
		// Si no existe, creamos el contacto con el número de teléfono del emisor como
		// nombre

		// boolean existe =
		// receptor.getUsuario().tieneContactoConMovil(usuarioActual.getMovil());
		boolean existe = receptor.tieneContactoConMovil(usuarioActual.getMovil());
		// Si existe, se añade el mensaje al contacto como recibido

		if (existe) {
			Contacto opuestoContacto = receptor.getContactoConMovil(usuarioActual.getMovil());
			Mensaje m;
			if (emoji != Mensaje.SIN_EMOTICONO && (contenido == null || contenido.isEmpty())) {
			    // Si se recibió un emoji válido y el contenido de texto original estaba vacío (caso típico de envío de emoji)
			    m = opuestoContacto.addMensaje(emoji, BubbleText.RECEIVED); // Se pasa el 'emoji' (Integer)
			} else {
			    m = opuestoContacto.addMensaje(contenido, BubbleText.RECEIVED); // Se pasa el 'contenido' (String)
			}
			mensajeDAO.registrarMensaje(m);
			contactoIndividualDAO.updateContactoIndividual((ContactoIndividual) opuestoContacto);
			// usuarioDAO.updateUsuario(receptor.getUsuario());
		} else {
			// Si no existe, se crea el contacto con el número de teléfono del emisor como
			// nombre
			// y se añade el mensaje al contacto como recibido
			Contacto nuevoContacto = receptor.addContactoIndividual(usuarioActual, usuarioActual.getMovil());
			contactoIndividualDAO.registrarContactoIndividual((ContactoIndividual) nuevoContacto);
			Mensaje m_nuevo;
			if (emoji != Mensaje.SIN_EMOTICONO && (contenido == null || contenido.isEmpty())) {
			    m_nuevo = nuevoContacto.addMensaje(emoji, BubbleText.RECEIVED);
			} else {
			    m_nuevo = nuevoContacto.addMensaje(contenido, BubbleText.RECEIVED);
			}
			mensajeDAO.registrarMensaje(m_nuevo); // Usar m_nuevo
			contactoIndividualDAO.updateContactoIndividual((ContactoIndividual) nuevoContacto);
			usuarioDAO.updateUsuario(receptor.getUsuario());
		}
	}

	/**
	 * Busca mensajes en las conversaciones del usuario actual basándose en un
	 * criterio.
	 *
	 * @param texto     Texto a buscar en el contenido de los mensajes.
	 * @param telefono  Número de teléfono del contacto para filtrar la búsqueda.
	 * @param nContacto Nombre del contacto para filtrar la búsqueda.
	 * @return Una {@link List} de objetos {@link Usuario.MensajeContextualizado}
	 *         encontrados.
	 */
	public List<MensajeContextualizado> buscarMensajes(String texto, String telefono, String nContacto) {
		if (this.usuarioActual == null) {
			return new LinkedList<>(); // Devuelve lista vacía si no hay usuario logueado
		}
		// La lógica de si nContacto es "Selecciona un contacto" se maneja dentro de
		// Usuario.buscarMisMensajes
		return this.usuarioActual.buscarMisMensajes(texto, telefono, nContacto);
	}

	/**
	 * Crea un nuevo contacto individual para el usuario actual.
	 * 
	 * @param nombre El nombre del nuevo contacto.
	 * @param movil  El número de móvil del nuevo contacto.
	 * @return Un mensaje indicando el resultado de la operación.
	 */
	public String crearContacto(String nombre, String movil) {
		// Comprobamos que no existe un contacto con el número
		if (this.usuarioActual.tieneContactoConMovil(movil)) {
			return "Ya existe un contacto con este número de móvil.";
		} else {
			// Contacto contacto = new
			// ContactoIndividual(RepositorioUsuarios.INSTANCE.findUsuario(movil), nombre);
			// TODO:
			Contacto contacto = this.usuarioActual
					.addContactoIndividual(RepositorioUsuarios.INSTANCE.findUsuario(movil), nombre);
			contactoIndividualDAO.registrarContactoIndividual((ContactoIndividual) contacto);
			// this.usuarioActual.addContacto(contacto);
			usuarioDAO.updateUsuario(usuarioActual);
			return "Contacto creado correctamente.";
		}
	}

	/**
	 * Crea un nuevo grupo para el usuario actual.
	 * 
	 * @param nombreGrupo El nombre del nuevo grupo.
	 * @param miembros    La lista de contactos individuales que serán miembros del
	 *                    grupo.
	 * @return true si el grupo se creó correctamente, false en caso contrario.
	 */
	public boolean crearGrupo(String nombreGrupo, LinkedList<ContactoIndividual> miembros) {
		// Comprobamos que no existe un grupo con el mismo nombre
		// Grupo grupo = new Grupo(nombreGrupo, miembros.toArray(new
		// ContactoIndividual[0]));
		Grupo grupo = this.usuarioActual.addGrupo(nombreGrupo, miembros.toArray(new ContactoIndividual[0]));
		grupoDAO.registrarGrupo(grupo);
		// this.usuarioActual.addContacto(grupo);
		usuarioDAO.updateUsuario(usuarioActual);
		return true;
	}

	/**
	 * Verifica si el usuario actual es premium.
	 * 
	 * @return true si el usuario actual es premium, false en caso contrario.
	 */
	public boolean isUsuarioPremium() {
		return this.usuarioActual.isPremium();
	}

	/**
	 * Convierte al usuario actual en premium.
	 * 
	 * @return true si la operación fue exitosa.
	 */
	public boolean convertirPremium() {
		this.usuarioActual.activarPremium();
		usuarioDAO.updateUsuario(usuarioActual);
		return true;
	}

	/**
	 * Exporta el chat con un contacto seleccionado a un archivo PDF.
	 * 
	 * @param contactoSeleccionado El contacto cuya conversación se exportará.
	 * @param filePath             La ruta del archivo donde se guardará el PDF.
	 * @return true si la exportación fue exitosa.
	 */
	public boolean exportarChatPDF(Contacto contactoSeleccionado, String filePath) {
		utils.ExportPDF.exportChatToPDF(contactoSeleccionado.getMensajesEnviados(), filePath + ".pdf",
				contactoSeleccionado.getNombre());
		return true;
	}

	/**
	 * Anula la suscripción premium del usuario actual.
	 * 
	 * @return true si la operación fue exitosa.
	 */
	public boolean anularPremium() {
		this.usuarioActual.desactivarPremium();
		usuarioDAO.updateUsuario(usuarioActual);
		return true;
	}

	/**
	 * Obtiene el último mensaje de una conversación con un contacto.
	 * 
	 * @param contacto El objeto Contacto de la conversación.
	 * @return El texto del último mensaje, o una cadena vacía si no hay mensajes.
	 */
	public String getUltimoMensaje(Contacto contacto) {
		return this.usuarioActual.getUltimoMensaje(contacto);
	}

	/**
	 * Obtiene el número de teléfono de un contacto individual o indica que es un
	 * grupo.
	 * 
	 * @param contacto El objeto Contacto.
	 * @return El número de teléfono si es un ContactoIndividual, "Grupo" si es un
	 *         Grupo, o null.
	 */
	public String getTelefono(Contacto contacto) {
		if (contacto instanceof ContactoIndividual) {
			return ((ContactoIndividual) contacto).getMovil();
		} else if (contacto instanceof Grupo) {
			return ("Grupo");
		}
		return null;
	}

	/**
	 * Modifica el nombre de un contacto (individual o grupo).
	 * 
	 * @param contacto    El objeto Contacto a modificar.
	 * @param nuevoNombre El nuevo nombre para el contacto.
	 */
	public void modificarNombreContacto(Contacto contacto, String nuevoNombre) {
		contacto.setNombre(nuevoNombre);

		if (contacto instanceof ContactoIndividual) {
			contactoIndividualDAO.updateContactoIndividual((ContactoIndividual) contacto);
		} else if (contacto instanceof Grupo) {
			grupoDAO.updateGrupo((Grupo) contacto);
		}
		;

		usuarioDAO.updateUsuario(usuarioActual);
	}

	/**
	 * Actualiza la información del perfil del usuario actual.
	 * 
	 * @param nombre          El nuevo nombre del usuario.
	 * @param apellidos       Los nuevos apellidos del usuario.
	 * @param email           El nuevo email del usuario.
	 * @param nuevoPassword   La nueva contraseña del usuario.
	 * @param fechaNacimiento La nueva fecha de nacimiento del usuario en formato
	 *                        String.
	 * @param saludo          El nuevo saludo del usuario.
	 * @param imagen          La nueva URL de la imagen de perfil del usuario.
	 * @return true si la actualización fue exitosa.
	 */
	public boolean actualizarUsuario(String nombre, String apellidos, String email, String nuevoPassword,
			String fechaNacimiento, String saludo, String imagen) {
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

	/**
	 * Obtiene los datos del perfil del usuario actual.
	 * 
	 * @return Una lista de cadenas de texto con los datos del usuario.
	 */
	public List<String> getDatosUsuario() {
		List<String> datos = this.usuarioActual.getDatosUsuario();
		return datos;
	}

	/**
	 * Obtiene un contacto individual del usuario actual dado su número de móvil.
	 * 
	 * @param movil El número de móvil del contacto.
	 * @return El objeto Contacto si se encuentra, o null.
	 */
	public Contacto getContactoConMovil(String movil) {
		return this.usuarioActual.getContactoConMovil(movil);
	}

	/**
	 * Obtiene un grupo del usuario actual dado su nombre.
	 * 
	 * @param string El nombre del grupo.
	 * @return El objeto Contacto (Grupo) si se encuentra, o null.
	 */
	public Contacto getGrupoConNombre(String string) {
		return this.usuarioActual.getGrupoConNombre(string);
	}

	/**
	 * Obtiene los apellidos del usuario actual.
	 * 
	 * @return Los apellidos del usuario actual, o null si no hay usuario
	 *         autenticado.
	 */
	public String getApellidosUsuario() {
		if (usuarioActual != null) {
			return usuarioActual.getApellidos();
		}
		return null;
	}

	/**
	 * Obtiene el saludo del usuario actual.
	 * 
	 * @return El saludo del usuario actual, o null si no hay usuario autenticado.
	 */
	public String getSaludoUsuario() {
		if (usuarioActual != null) {
			return usuarioActual.getSaludo();
		}
		return null;
	}

	/**
	 * Obtiene el número de teléfono del usuario actual.
	 * 
	 * @return El número de teléfono del usuario actual, o null si no hay usuario
	 *         autenticado.
	 */
	public String getTelefonoUsuario() {
		if (usuarioActual != null) {
			return usuarioActual.getMovil();
		}
		return null;
	}

	/**
	 * Obtiene el email del usuario actual.
	 * 
	 * @return El email del usuario actual, o null si no hay usuario autenticado.
	 */
	public String getEmailUsuario() {
		if (usuarioActual != null) {
			return usuarioActual.getEmail();
		}
		return null;
	}

	/**
	 * Obtiene la fecha de nacimiento del usuario actual.
	 * 
	 * @return La fecha de nacimiento del usuario actual, o null si no hay usuario
	 *         autenticado.
	 */
	public Date getFechaNacimientoUsuario() {
		if (usuarioActual != null) {
			return java.sql.Date.valueOf(usuarioActual.getFechaNacimiento());
		}
		return null;
	}

	/**
	 * Obtiene la fecha de registro de la cuenta del usuario actual.
	 * 
	 * @return La fecha de registro del usuario actual, o null si no hay usuario
	 *         autenticado.
	 */
	public Date getFechaCreacionCuentaUsuario() {
		if (usuarioActual != null) {
			return java.sql.Date.valueOf(usuarioActual.getFechaRegistro());
		}
		return null;
	}

	/**
	 * Obtiene el precio base de la aplicación.
	 * 
	 * @return El precio base.
	 */
	public double getPrecioBase() {
		return PRECIO_APLICACION;
	}

	/**
	 * Calcula el precio de la aplicación aplicando el mejor descuento disponible
	 * para el usuario actual.
	 * 
	 * @return El precio con descuento.
	 */
	public double getPrecioDescuento() {
		Descuento mejorDescuento = FactoriaDescuentos.INSTANCE.getMejorDescuento(usuarioActual, PRECIO_APLICACION);
		return mejorDescuento.getPrecio(PRECIO_APLICACION);
	}

	/**
	 * Obtiene el saludo de un contacto individual.
	 * 
	 * @param contacto El objeto Contacto.
	 * @return El saludo si es un ContactoIndividual, o una cadena vacía si es un
	 *         Grupo.
	 */
	public String getSaludoContacto(Contacto contacto) {
		if (contacto instanceof ContactoIndividual) {
			return ((ContactoIndividual) contacto).getSaludo();
		} else {
			return "";
		}
	}

	/**
	 * Verifica si un contacto es un contacto individual.
	 * 
	 * @param contacto El objeto Contacto.
	 * @return true si el contacto es un ContactoIndividual, false en caso
	 *         contrario.
	 */
	public boolean isContactoIndividual(Contacto contacto) {
		return contacto instanceof ContactoIndividual;
	}

	/**
	 * Modifica la información de un grupo existente.
	 * 
	 * @param grupo          El objeto Grupo a modificar.
	 * @param nuevoNombre    El nuevo nombre para el grupo.
	 * @param nuevosMiembros La nueva lista de miembros del grupo.
	 * @return true si la modificación fue exitosa, false si el grupo es null.
	 */
	public boolean modificarGrupo(Grupo grupo, String nuevoNombre, LinkedList<ContactoIndividual> nuevosMiembros) {
		if (grupo != null) {
			grupo.setNombre(nuevoNombre);
			grupo.setMiembros(nuevosMiembros);
			grupoDAO.updateGrupo(grupo);
			return true;
		}
		return false;
	}

	/**
	 * Obtiene la lista de miembros de un grupo.
	 * 
	 * @param grupo El objeto Grupo.
	 * @return Una LinkedList de objetos Contacto que son miembros del grupo.
	 */
	public LinkedList<Contacto> getMiembrosGrupo(Grupo grupo) {
		return (LinkedList<Contacto>) grupo.getMiembros();
	}
}