package dao;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;
import dominio.Contacto;
import dominio.ContactoIndividual;
import dominio.Grupo;
import dominio.Mensaje;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

/**
 * Implementación de la interfaz GrupoDAO para el Servicio de Persistencia TDS.
 */
public class TDSGrupoDAO implements GrupoDAO {


	private static final String GRUPO = "Grupo";
	private static final String NOMBRE = "nombre";
	private static final String MENSAJES = "mensajes";
	private static final String MIEMBROS = "miembros";

	private ServicioPersistencia servPersistencia;
	private static TDSGrupoDAO unicaInstancia = null;

	/**
	 * Constructor privado para asegurar una única instancia (Singleton).
	 */
	private TDSGrupoDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}

	/**
	 * Obtiene la instancia única de TDSGrupoDAO.
	 * @return La instancia única de TDSGrupoDAO.
	 */
	public static TDSGrupoDAO getInstance() {
		if (unicaInstancia == null) unicaInstancia = new TDSGrupoDAO();
		return unicaInstancia;
	}


	/**
	 * Registra un grupo en la capa de persistencia.
	 * @param grupo El objeto Grupo a registrar.
	 */
	@Override
	public void registrarGrupo(Grupo grupo) {
		Optional<Entidad> eGrupo = Optional.ofNullable(servPersistencia.recuperarEntidad(grupo.getId()));
		if (eGrupo.isPresent()) return;

		//mensajes
		grupo.getMensajesEnviados().stream().forEach(TDSMensajeDAO.getInstance()::registrarMensaje);

		//como interfaz agragables, preparamos por si se modificase la implementación de grupos
		grupo.getMiembros().stream()
				.forEach(m -> {
					if (m instanceof ContactoIndividual) {
						TDSContactoIndividualDAO.getInstance().registrarContactoIndividual((ContactoIndividual) m);
					} else {
						System.err.println("Método de persistencia no implementado para este tipo de miembro: " + m.getClass().getSimpleName());
					}
				});

		Entidad eNuevoGrupo = new Entidad();
		eNuevoGrupo.setNombre(GRUPO);

		//sería mejor crear función get ID de usuario del contacto? o llamar a varias funciones? -> más acoplamiento
		eNuevoGrupo.setPropiedades(
				new ArrayList<Propiedad>(Arrays.asList(
						new Propiedad(NOMBRE, grupo.getNombre()),
						new Propiedad(MENSAJES, obtenerCodigosMensajes(grupo.getMensajesEnviados())),
						new Propiedad(MIEMBROS, obtenerCodigosMiembros(grupo.getMiembros()))
						)));

		eNuevoGrupo = servPersistencia.registrarEntidad(eNuevoGrupo);
		grupo.setId(eNuevoGrupo.getId());
		PoolDAO.INSTANCE.addObject(grupo.getId(), grupo);
	}

	/**
	 * Elimina un grupo de la capa de persistencia.
	 * @param grupo El objeto Grupo a eliminar.
	 */
	@Override
	public void eliminarGrupo(Grupo grupo) {
		Optional<Entidad> eGrupo = Optional.ofNullable(servPersistencia.recuperarEntidad(grupo.getId()));
		if (!eGrupo.isPresent()) return;
		// eliminamos mensajes pero no miembros
		grupo.getMensajesEnviados().stream().forEach(TDSMensajeDAO.getInstance()::eliminarMensaje);

		servPersistencia.borrarEntidad(eGrupo.get());
		if (PoolDAO.INSTANCE.contains(grupo.getId())) PoolDAO.INSTANCE.removeObjet(grupo.getId());
	}

	//consideramos necesario comprobar que exista?
	/**
	 * Actualiza la información de un grupo en la capa de persistencia.
	 * @param grupo El objeto Grupo con la información actualizada.
	 */
	@Override
	public void updateGrupo(Grupo grupo) {

		Entidad eGrupo = servPersistencia.recuperarEntidad(grupo.getId());
		for(Propiedad prop : eGrupo.getPropiedades()) {
			if(prop.getNombre().equals(NOMBRE)) {
				prop.setValor(grupo.getNombre());
			} else if (prop.getNombre().equals(MIEMBROS)) {
				prop.setValor(obtenerCodigosMiembros(grupo.getMiembros()));
			} else if (prop.getNombre().equals(MENSAJES)) {
				prop.setValor(obtenerCodigosMensajes(grupo.getMensajesEnviados()));
			}

			servPersistencia.modificarPropiedad(prop);
		}

	}

	/**
	 * Recupera un grupo de la capa de persistencia dado su identificador.
	 * @param id El identificador único del grupo.
	 * @return El objeto Grupo correspondiente al id, o null si no se encuentra.
	 */
	@Override
	public Grupo getGrupo(int id) {
		if(PoolDAO.INSTANCE.contains(id)) return (Grupo) PoolDAO.INSTANCE.getObject(id);

		Entidad eGrupo = servPersistencia.recuperarEntidad(id);

		String nombre = servPersistencia.recuperarPropiedadEntidad(eGrupo, NOMBRE);
		//según lo tenemos realmente un mensaje no contiene usuario ni contactos
		List<Mensaje> mensajes =obtenerMensajesDeCodigos(servPersistencia.recuperarPropiedadEntidad(eGrupo, MENSAJES));
		List<Contacto> miembros = null;

		Grupo grupo = new Grupo(nombre, mensajes);
		grupo.setId(id);

		PoolDAO.INSTANCE.addObject(id, grupo);

		miembros = obtenerMiembrosDeCodigos(servPersistencia.recuperarPropiedadEntidad(eGrupo, MIEMBROS));
		miembros.stream().forEach(m -> grupo.addMiembro((ContactoIndividual)m));


		return grupo;
	}

	/**
	 * Recupera todos los grupos existentes en la capa de persistencia.
	 * @return Una lista de todos los objetos Grupo.
	 */
	@Override
	public List<Grupo> getAll() {
		List<Grupo> grupos = new ArrayList<>();
		List<Entidad> eGrupos = servPersistencia.recuperarEntidades(GRUPO);

		eGrupos.stream()
			   .forEach(e -> grupos.add(getGrupo(e.getId())));
		return grupos;
	}

	//duplicamos código para no exponer este tipo de funciones que pueden revelar información sensible de la BDD
	/**
	 * Convierte una lista de mensajes a una cadena de texto con sus códigos separados por espacios.
	 * @param mensajes La lista de mensajes.
	 * @return Una cadena de texto con los códigos de los mensajes.
	 */
	private String obtenerCodigosMensajes(List<Mensaje> mensajes) {

			return mensajes.stream().map(m -> String.valueOf(m.getId())).reduce("", (i, m) -> i + m + " ").trim();
		}

	/**
	 * Convierte una cadena de texto con códigos de mensajes separados por espacios a una lista de mensajes.
	 * @param codigos La cadena de texto con los códigos.
	 * @return Una lista de objetos Mensaje.
	 */
	private List<Mensaje> obtenerMensajesDeCodigos(String codigos) {
	    return Arrays.stream(codigos.split(" "))
	                 .filter(c -> !c.isEmpty())
	                 .map(c -> TDSMensajeDAO.getInstance().getMensaje(Integer.parseInt(c)))
	                 .collect(Collectors.toList());
	}

	/**
	 * Convierte una lista de contactos a una cadena de texto con sus códigos separados por espacios.
	 * @param cont La lista de contactos.
	 * @return Una cadena de texto con los códigos de los contactos.
	 */
	private String obtenerCodigosMiembros(List<Contacto> cont) {

		return cont.stream().map(m -> String.valueOf(m.getId())).reduce("", (i, m) -> i + m + " ").trim();
	}

	/**
	 * Convierte una cadena de texto con códigos de miembros separados por espacios a una lista de contactos.
	 * @param codigos La cadena de texto con los códigos.
	 * @return Una lista de objetos Contacto.
	 */
	private List<Contacto> obtenerMiembrosDeCodigos(String codigos) {
	    return Arrays.stream(codigos.split(" "))
	                 .filter(c -> !c.isEmpty())
	                 .map(c -> TDSContactoIndividualDAO.getInstance().getContactoIndividual(Integer.parseInt(c)))
	                 .collect(Collectors.toList());
	}

}