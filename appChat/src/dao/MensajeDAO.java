package dao;

import java.util.List;
import dominio.Mensaje;

public interface MensajeDAO {

	void registrarMensaje(Mensaje msj);
	void eliminarMensaje(Mensaje msj);
	void updateMensaje(Mensaje msj);
	Mensaje getMensaje(int id);
	List<Mensaje> getAll();
	
}
