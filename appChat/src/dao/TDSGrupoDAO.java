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

public class TDSGrupoDAO implements GrupoDAO {

	
	private static final String GRUPO = "Grupo";
	private static final String NOMBRE = "nombre";
	private static final String MENSAJES = "mensajes";
	private static final String MIEMBROS = "miembros";
	
	private ServicioPersistencia servPersistencia;
	private static TDSGrupoDAO unicaInstancia = null;
	
	private TDSGrupoDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	public static TDSGrupoDAO getInstance() {
		if (unicaInstancia == null) unicaInstancia = new TDSGrupoDAO();
		return unicaInstancia;
	}
	
	
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
	@Override
	public void updateGrupo(Grupo grupo) {

		Entidad eGrupo = servPersistencia.recuperarEntidad(grupo.getId());
		for(Propiedad prop : eGrupo.getPropiedades()) {
			if(prop.getNombre().equals(NOMBRE)) {
				prop.setValor(grupo.getNombre());
			} else if (prop.getNombre().equals(MIEMBROS)) {
				prop.setValor(obtenerCodigosMiembros(grupo.getMiembros()));
			}
			
			servPersistencia.modificarPropiedad(prop);
		}
		
	}

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

	@Override
	public List<Grupo> getAll() {
		List<Grupo> grupos = new ArrayList<>();
		List<Entidad> eGrupos = servPersistencia.recuperarEntidades(GRUPO);
		
		eGrupos.stream()
			   .forEach(e -> grupos.add(getGrupo(e.getId())));
		return grupos;
	}
	
	//duplicamos código para no exponer este tipo de funciones que pueden revelar información sensible de la BDD
	private String obtenerCodigosMensajes(List<Mensaje> mensajes) {
			
			return mensajes.stream().map(m -> String.valueOf(m.getId())).reduce("", (i, m) -> i + m + " ").trim();
		}
	
	private List<Mensaje> obtenerMensajesDeCodigos(String codigos) {
	    return Arrays.stream(codigos.split(" "))
	                 .filter(c -> !c.isEmpty()) 
	                 .map(c -> TDSMensajeDAO.getInstance().getMensaje(Integer.parseInt(c)))
	                 .collect(Collectors.toList()); 
	}
	
	private String obtenerCodigosMiembros(List<Contacto> cont) {
		
		return cont.stream().map(m -> String.valueOf(m.getId())).reduce("", (i, m) -> i + m + " ").trim();
	}
	
	private List<Contacto> obtenerMiembrosDeCodigos(String codigos) {
	    return Arrays.stream(codigos.split(" "))
	                 .filter(c -> !c.isEmpty()) 
	                 .map(c -> TDSContactoIndividualDAO.getInstance().getContactoIndividual(Integer.parseInt(c)))
	                 .collect(Collectors.toList()); 
	}

}
