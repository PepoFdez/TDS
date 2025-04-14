package dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;
import dominio.ContactoIndividual;
import dominio.Mensaje;
import dominio.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;


public class TDSContactoIndividualDAO implements ContactoIndividualDAO {

	private static final String CONTACTOINDIVIDUAL = "ContactoIndividual";
	private static final String USUARIO = "usuario";
	private static final String NOMBRE = "nombre";
	private static final String MENSAJES = "mensajes";
	
	
	
	private ServicioPersistencia servPersistencia;
	private static TDSContactoIndividualDAO unicaInstancia = null;
	
	private TDSContactoIndividualDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	public static TDSContactoIndividualDAO getInstance() {
		if (unicaInstancia == null) unicaInstancia = new TDSContactoIndividualDAO();
		return unicaInstancia;
	}
	
	
	@Override
	public void registrarContactoIndividual(ContactoIndividual contInd) {
		//comprobamos que no esté registrada
		Optional<Entidad> eContInd = Optional.ofNullable(servPersistencia.recuperarEntidad(contInd.getId()));
		if (eContInd.isPresent()) return;
		
		//se registran sus Objetos agregados
		//realmente tiene sentido registrar el Usuario que representa el contacto en la BDD?? porque se controla desde controlador que no se puede añadir como contacto un usuario que no esté registrado
		//igual mejor hacerlo por seguridad
		//USUARIO
		//Mensajes
		//comprobar si llamamos a TDSMEnsajeDAO por ser TDSContacto o si deberíamos de llamar a FactoriaDAO...
		contInd.getMensajesEnviados().stream().forEach(TDSMensajeDAO.getInstance()::registrarMensaje);
		
		Entidad eNuevoContInd = new Entidad();
		eNuevoContInd.setNombre(CONTACTOINDIVIDUAL);
		
		//sería mejor crear función get ID de usuario del contacto? o llamar a varias funciones? -> más acoplamiento
		eNuevoContInd.setPropiedades(
				new ArrayList<Propiedad>(Arrays.asList(
						new Propiedad(NOMBRE, contInd.getNombre()),
						new Propiedad(USUARIO, String.valueOf(contInd.getUsuario().getId())),
						new Propiedad(MENSAJES, obtenerCodigosMensajes(contInd.getMensajesEnviados()))
						)));
		
		eNuevoContInd = servPersistencia.registrarEntidad(eNuevoContInd);
		contInd.setId(eNuevoContInd.getId());
		PoolDAO.INSTANCE.addObject(contInd.getId(), contInd);
	}


	@Override
	public void eliminarContactoIndividual(ContactoIndividual contInd) {
		Optional<Entidad> eContInd = Optional.ofNullable(servPersistencia.recuperarEntidad(contInd.getId()));
		if (!eContInd.isPresent())  return;
		
		contInd.getMensajesEnviados().stream()
										.forEach(TDSMensajeDAO.getInstance()::eliminarMensaje);
		servPersistencia.borrarEntidad(eContInd.get());
		if (PoolDAO.INSTANCE.contains(contInd.getId())) PoolDAO.INSTANCE.removeObjet(eContInd.get().getId());
		//supongo que mejor no usar la entidad que acabamos de borrar
	}

	//consideramos necesario comprobar que para modificar deba existir?
	
	//mantenemos actualizar el Usuario asociado al contacto?
	@Override
	public void updateContactoIndividual(ContactoIndividual contInd) {
		Entidad eContInd = servPersistencia.recuperarEntidad(contInd.getId());
		
		for(Propiedad prop : eContInd.getPropiedades()) {
			if(prop.getNombre().equals(NOMBRE)) {
				prop.setValor(contInd.getNombre());
			} else if (prop.getNombre().equals(USUARIO)) {
				prop.setValor(String.valueOf(contInd.getUsuario().getId()));
			} else if (prop.getNombre().equals(MENSAJES)) {
				prop.setValor(obtenerCodigosMensajes(contInd.getMensajesEnviados()));
			}
			
			servPersistencia.modificarPropiedad(prop);
		}
	}

	@Override
	public ContactoIndividual getContactoIndividual(int id) {
		if(PoolDAO.INSTANCE.contains(id)) return (ContactoIndividual) PoolDAO.INSTANCE.getObject(id);
		
		Entidad eContaEntidad = servPersistencia.recuperarEntidad(id);
		
		String nombre = servPersistencia.recuperarPropiedadEntidad(eContaEntidad, NOMBRE);
		Usuario user = null;
		
		//comprobar que si nuestro enfoque de mensajes está ok, podemos crear directamente el contacto individual con los mensajes
		
		//creamos con user null para evitar bulces
		ContactoIndividual contInd = new ContactoIndividual(user, nombre);
		contInd.setId(id);
		PoolDAO.INSTANCE.addObject(id, contInd);
		
		//recuperamos agregados
		user = TDSUsuarioDAO.getInstance().getUsuario(Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eContaEntidad, USUARIO)));
		contInd.setUsuario(user);
		
		List<Mensaje> mensajes =obtenerMensajesDeCodigos(servPersistencia.recuperarPropiedadEntidad(eContaEntidad, MENSAJES));
		mensajes.stream().forEach(contInd::addMensaje);
		
		return contInd;
	}

	@Override
	public List<ContactoIndividual> getAll() {
		List<ContactoIndividual> contactos = new ArrayList<>();
		List<Entidad> eContactos = servPersistencia.recuperarEntidades(CONTACTOINDIVIDUAL);
		
		eContactos.stream()
					.forEach(e -> contactos.add(getContactoIndividual(e.getId())));;
		return contactos;
	}
	
	private String obtenerCodigosMensajes(List<Mensaje> mensajes) {
		
		return mensajes.stream().map(m -> String.valueOf(m.getId())).reduce("", (i, m) -> i + m + " ").trim();
	}
	
	private List<Mensaje> obtenerMensajesDeCodigos(String codigos) {
	    return Arrays.stream(codigos.split(" "))
	                 .filter(c -> !c.isEmpty()) 
	                 .map(c -> TDSMensajeDAO.getInstance().getMensaje(Integer.parseInt(c)))
	                 .collect(Collectors.toList()); 
	}

	
}
