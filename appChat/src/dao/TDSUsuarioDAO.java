package dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import dominio.Contacto;
import dominio.ContactoIndividual;
import dominio.Grupo;
//import tds.driver.ServicioPersistencia;
import dominio.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import utils.Utils;
import beans.Entidad;
import beans.Propiedad;

/**
 * 
 * Clase que implementa el Adaptador DAO concreto de Usuario para el tipo H2.
 * 
 */
public final class TDSUsuarioDAO implements UsuarioDAO {

	private static final String USUARIO = "Usuario";
	private static final String NOMBRE = "nombre";
	private static final String APELLIDOS = "apellidos";
	private static final String EMAIL = "email";
	private static final String MOVIL = "movil";
	private static final String PASSWORD = "password";
	private static final String FECHA_NACIMIENTO = "fechaNacimiento";
	private static final String IMAGEN = "imagen";
	private static final String SALUDO = "saludo";
	private static final String PREMIUM = "premium";
	private static final String CONTACTOS = "contactosIndiv";
	private static final String GRUPOS = "grupos";
	private static final String FECHA_REGISTRO = "fechaRegistro";

	private ServicioPersistencia servPersistencia; 
	private static TDSUsuarioDAO unicaInstancia = null;
	
	private TDSUsuarioDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	public static TDSUsuarioDAO getInstance() {
		if (unicaInstancia == null) {
			unicaInstancia = new TDSUsuarioDAO();
		}
		return unicaInstancia;
	}
	
	@Override
	public void registrarUsuario(Usuario user) {
		//comprobamos que el usuaio no esté registrado
		Optional<Entidad> eUsuario = Optional.ofNullable(servPersistencia.recuperarEntidad(user.getId()));
		if(eUsuario.isPresent()) return;
		
		//REGISTRAMOS AGRAGADOS

		List<Contacto> grupos = user.getContactos().stream()
												   .filter(c -> c instanceof Grupo)
												   .collect(Collectors.toList());
		
		List<Contacto> contactosInd = user.getContactos().stream()
														 .filter(c -> c instanceof ContactoIndividual)
														 .collect(Collectors.toList());
		
		grupos.stream().forEach(g -> TDSGrupoDAO.getInstance().registrarGrupo((Grupo)g));
		contactosInd.forEach(c -> TDSContactoIndividualDAO.getInstance().registrarContactoIndividual((ContactoIndividual) c));
		
		
		//creamos la entidad
		Entidad eNuevoUsuario = new Entidad();
		eNuevoUsuario.setNombre(USUARIO);
		
		eNuevoUsuario.setPropiedades(
				new ArrayList<Propiedad>(Arrays.asList(
						new Propiedad(NOMBRE, user.getNombre()),
						new Propiedad(APELLIDOS, user.getApellidos()),
						new Propiedad(EMAIL, user.getEmail()),
						new Propiedad(MOVIL, user.getMovil()),
						new Propiedad(PASSWORD, user.getPassword()),
						new Propiedad(FECHA_NACIMIENTO, Utils.formatoFecha.format(user.getFechaNacimiento())),
						new Propiedad(IMAGEN, user.getURLImagen()),
						new Propiedad(SALUDO, user.getSaludo()),
						new Propiedad(PREMIUM, String.valueOf(user.isPremium())),
						new Propiedad(FECHA_REGISTRO, Utils.formatoFecha.format(user.getFechaRegistro())),
						new Propiedad(CONTACTOS,  obtenerCodigosContactos(contactosInd)), 
						new Propiedad(GRUPOS,  obtenerCodigosContactos(grupos)) 
		)));
		
		eNuevoUsuario = servPersistencia.registrarEntidad(eNuevoUsuario);
		user.setId(eNuevoUsuario.getId());
		PoolDAO.INSTANCE.addObject(user.getId(), user);
	}

	@Override
	public void eliminarUsuario(Usuario user) {
		Optional<Entidad> eUsuario = Optional.ofNullable(servPersistencia.recuperarEntidad(user.getId()));
		if(!eUsuario.isPresent()) return;
		
		List<Contacto> grupos = user.getContactos().stream()
				   					.filter(c -> c instanceof Grupo)
				   					.collect(Collectors.toList());

		List<Contacto> contactosInd = user.getContactos().stream()
								 						 .filter(c -> c instanceof ContactoIndividual)
								 						 .collect(Collectors.toList());
		
		grupos.stream()
			  .forEach(g -> TDSGrupoDAO.getInstance().eliminarGrupo((Grupo)g));
		contactosInd.stream()
					.forEach(c -> TDSContactoIndividualDAO.getInstance().eliminarContactoIndividual((ContactoIndividual) c));
		
		servPersistencia.borrarEntidad(eUsuario.get());
		if(PoolDAO.INSTANCE.contains(user.getId())) PoolDAO.INSTANCE.removeObjet(user.getId());
	}
	
	/**
	 * Permite que un Usuario modifique su perfil: password y email
	 */
	
	@Override
	public void updateUsuario(Usuario user) {
		Entidad eUsuario = servPersistencia.recuperarEntidad(user.getId());

		//como tenemos esta parte repetida en varias funciones, creamos funciones a parte para no repetir código?
		List<Contacto> grupos = user.getContactos().stream()
					.filter(c -> c instanceof Grupo)
					.collect(Collectors.toList());

		List<Contacto> contactosInd = user.getContactos().stream()
			 						 .filter(c -> c instanceof ContactoIndividual)
			 						 .collect(Collectors.toList());

		for (Propiedad prop : eUsuario.getPropiedades()) {
			if (prop.getNombre().equals(PASSWORD)) {
				prop.setValor(user.getPassword());
			} else if (prop.getNombre().equals(EMAIL)) {
				prop.setValor(user.getEmail());
			} else if (prop.getNombre().equals(NOMBRE)) {
				prop.setValor(user.getNombre());
			} else if (prop.getNombre().equals(APELLIDOS)) {
				prop.setValor(user.getApellidos());
			} else if (prop.getNombre().equals(MOVIL)) {
				prop.setValor(user.getMovil());
			} else if (prop.getNombre().equals(FECHA_NACIMIENTO)) {
				prop.setValor(Utils.formatoFecha.format(user.getFechaNacimiento()));
			} else if (prop.getNombre().equals(SALUDO)) {
				prop.setValor(user.getSaludo());
			} else if (prop.getNombre().equals(IMAGEN)) {
				prop.setValor(user.getURLImagen());
			} else if (prop.getNombre().equals(PREMIUM)) {
				prop.setValor(String.valueOf(user.isPremium()));
			} else if (prop.getNombre().equals(CONTACTOS)) {
				prop.setValor(obtenerCodigosContactos(contactosInd));
			} else if (prop.getNombre().equals(GRUPOS)) {
				prop.setValor(obtenerCodigosContactos(grupos));
			}
			
			servPersistencia.modificarPropiedad(prop);
		}
	}
	
	@Override
	public Usuario getUsuario(int id) {
		if(PoolDAO.INSTANCE.contains(id)) return (Usuario) PoolDAO.INSTANCE.getObject(id);
		Entidad eUsuario = servPersistencia.recuperarEntidad(id);
		
		String nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, NOMBRE);
		String apellidos = servPersistencia.recuperarPropiedadEntidad(eUsuario, APELLIDOS);
		String email = servPersistencia.recuperarPropiedadEntidad(eUsuario, EMAIL);
		String movil = servPersistencia.recuperarPropiedadEntidad(eUsuario, MOVIL);
		String password = servPersistencia.recuperarPropiedadEntidad(eUsuario, PASSWORD);
		LocalDate fechaNacimiento = 
				LocalDate.parse(servPersistencia.recuperarPropiedadEntidad(eUsuario, FECHA_NACIMIENTO), Utils.formatoFecha);
		String URLImagen = servPersistencia.recuperarPropiedadEntidad(eUsuario, IMAGEN);
		String saludo = servPersistencia.recuperarPropiedadEntidad(eUsuario, SALUDO);
		boolean premium = Boolean.parseBoolean(servPersistencia.recuperarPropiedadEntidad(eUsuario, PREMIUM));
		LocalDate fechaRegistro = LocalDate.parse(servPersistencia.recuperarPropiedadEntidad(eUsuario, FECHA_REGISTRO),
				Utils.formatoFecha);
		
		//falta mirar bien para aplicar patrón builder o similar para agilizar creación de usuarios
		Usuario user = new Usuario(nombre, apellidos, email, movil, password, fechaNacimiento, URLImagen, saludo, premium, fechaRegistro);
		user.setId(id);
		PoolDAO.INSTANCE.addObject(id, user);
		
		List<Contacto> contactosInd = obtenerContactosIndDeCodigos(
				servPersistencia.recuperarPropiedadEntidad(eUsuario, CONTACTOS));
		
		List<Contacto> grupos = obtenerGruposDeCodigos(
				servPersistencia.recuperarPropiedadEntidad(eUsuario, GRUPOS));
		
		contactosInd.stream().forEach(user::addContacto);
		grupos.stream().forEach(user::addContacto);
		
		return user;
	}

	@Override
	public List<Usuario> getAll() {
		List<Usuario> usuarios = new LinkedList<Usuario>();
		List<Entidad> entidades = servPersistencia.recuperarEntidades(USUARIO);

		entidades.stream()
				 .forEach(e -> usuarios.add(getUsuario(e.getId())));
		
	
		return usuarios;
	}

	
	private String obtenerCodigosContactos(List<Contacto> cont) {
			
			return cont.stream().map(c -> String.valueOf(c.getId())).reduce("", (i, c) -> i + c + " ").trim();
	}
	
	private List<Contacto> obtenerContactosIndDeCodigos(String codigos) {
	    return Arrays.stream(codigos.split(" "))
	                 .filter(c -> !c.isEmpty()) 
	                 .map(c -> TDSContactoIndividualDAO.getInstance().getContactoIndividual(Integer.parseInt(c)))
	                 .collect(Collectors.toList()); 
	}
	
	private List<Contacto> obtenerGruposDeCodigos(String codigos) {
	    return Arrays.stream(codigos.split(" "))
	                 .filter(c -> !c.isEmpty()) 
	                 .map(c -> TDSGrupoDAO.getInstance().getGrupo(Integer.parseInt(c)))
	                 .collect(Collectors.toList()); 
	}
	
	


}