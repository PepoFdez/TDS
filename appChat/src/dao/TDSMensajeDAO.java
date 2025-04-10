package dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import beans.Entidad;
import beans.Propiedad;
import dominio.Mensaje;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import utils.Utils;

public class TDSMensajeDAO implements MensajeDAO {
	
	private static final String MENSAJE = "Mensaje";
	private static final String TEXTO = "texto";
	private static final String EMOTICONO = "emoticono";
	private static final String FECHA = "fecha";
	private static final String TIPO = "tipo";
	
	private ServicioPersistencia servPersistencia;
	private static TDSMensajeDAO unicaInstancia = null;
	
	
	private TDSMensajeDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	public static TDSMensajeDAO getInstance() {
		if(unicaInstancia == null) unicaInstancia = new TDSMensajeDAO();
		return unicaInstancia;
	}
	
	@Override
	public void registrarMensaje(Mensaje msj) {
		//comprobamos si el mensaje está ya en la base de datos
		Optional<Entidad> eMensaje = Optional.ofNullable(servPersistencia.recuperarEntidad(msj.getId()));
		if (eMensaje.isPresent()) {
			System.out.println("El mensaje ya existe en la base de datos");
			return;
		};
		
		Entidad eNuevoMensaje = new Entidad();
		eNuevoMensaje.setNombre(MENSAJE);
		
		eNuevoMensaje.setPropiedades(
				new ArrayList<Propiedad>(Arrays.asList(
						new Propiedad(TEXTO, msj.getTexto()),
						new Propiedad(EMOTICONO, String.valueOf(msj.getEmoticono())),
						new Propiedad(FECHA, msj.getFecha().format(Utils.formatoFecha)),
						new Propiedad(TIPO, String.valueOf(msj.getTipo()))
						)));
		
		eNuevoMensaje = servPersistencia.registrarEntidad(eNuevoMensaje);
		msj.setId(eNuevoMensaje.getId());
		PoolDAO.INSTANCE.addObject(eNuevoMensaje.getId(), msj);
	}
	
//comprobar si salta error al intentar eliminar un mensaje que no existe en la base de datos
	@Override
	public void eliminarMensaje(Mensaje msj) {
		Optional<Entidad> eMensaje = Optional.ofNullable(servPersistencia.recuperarEntidad(msj.getId()));
		if (!eMensaje.isPresent()) {
			System.out.println("El mensaje no existe en la base de datos");
			return;
		};
		servPersistencia.borrarEntidad(eMensaje.get());
		
		if(PoolDAO.INSTANCE.contains(msj.getId())) PoolDAO.INSTANCE.removeObjet(msj.getId());
	}
/*
	@Override
	public void updateMensaje(Mensaje msj) {
//cambiar update si realmente no se pueden cambiar las propiedades de los mensajes
		Entidad eMensaje = servPersistencia.recuperarEntidad(msj.getId());
		
		for (Propiedad prop : eMensaje.getPropiedades()) {
			if(prop.getNombre().equals(TEXTO)) {
				prop.setValor(msj.getTexto());
			} else if(prop.getNombre().equals(EMOTICONO)) {
				prop.setValor(String.valueOf(msj.getEmoticono()));
			} else if (prop.getNombre().equals(FECHA)) {
				prop.setValor(msj.getFecha().format(Utils.formatoFecha));
			} else if (prop.getNombre().equals(TIPO)) {
				prop.setValor(String.valueOf(msj.getTipo()));
			}
		}
		
		servPersistencia.modificarEntidad(eMensaje);
		PoolDAO.INSTANCE.addObject(msj.getId(), msj);//addObject también sirve para actualizar el pool
		//no 100%seguro de si hay que actualizar le pool pero diría que sí.
		
	}
*/
	@Override
	public Mensaje getMensaje(int id) {
		if (PoolDAO.INSTANCE.contains(id)) return (Mensaje) PoolDAO.INSTANCE.getObject(id);
		 
		Entidad eMensaje = servPersistencia.recuperarEntidad(id);
		//recuperamos propiedades
		String texto = servPersistencia.recuperarPropiedadEntidad(eMensaje, TEXTO);
		int emoticono = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, EMOTICONO));
		String fecha = servPersistencia.recuperarPropiedadEntidad(eMensaje, FECHA);
		LocalDateTime fechaMsj = LocalDateTime.parse(fecha, Utils.formatoFecha);
		int tipo = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, TIPO));
		
		Mensaje mensaje = new Mensaje(texto, emoticono, fechaMsj, tipo);
		mensaje.setId(id);
		PoolDAO.INSTANCE.addObject(id, mensaje);
		
		return mensaje;
	}

	@Override
	public List<Mensaje> getAll() {
		List<Mensaje> mensajes = new ArrayList<>();
		List<Entidad> eMensajes = servPersistencia.recuperarEntidades(MENSAJE);
		
		eMensajes.stream().forEach(e -> mensajes.add(getMensaje(e.getId())));
		
		return mensajes;
	}

}
