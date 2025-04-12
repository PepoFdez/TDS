package dao;

import java.util.List;

import dominio.Grupo;


public interface GrupoDAO{

	void registrarGrupo(Grupo grupo);
	void eliminarGrupo(Grupo grupo);
	void updateGrupo(Grupo grupo);
	Grupo getGrupo(int id);
	List<Grupo> getAll();
}
