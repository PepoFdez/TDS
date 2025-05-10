package dao;

import java.util.HashMap;

/**
 * Enumeración que actúa como un pool de objetos DAO recuperados de la base de datos
 * para evitar recuperaciones duplicadas.
 */
public enum PoolDAO {
/*Hay que crear una colección genérica para almacenar todos los objetos que se hayan recuperado de la
 * base de datos. Para comprobar si están recuperados y no recuperarlos de nuevo*/

	/**
	 * Instancia única del PoolDAO.
	 */
	INSTANCE;

	private HashMap<Integer, Object> pool;

	/**
	 * Constructor privado de la enumeración. Inicializa el mapa para almacenar objetos.
	 */
	private PoolDAO () {
		pool = new HashMap<Integer, Object>();
	}

	/**
	 * Añade un objeto al pool con su identificador único.
	 * @param id El identificador único del objeto.
	 * @param object El objeto a añadir al pool.
	 */
	public void addObject(int id, Object object) {
		pool.put(id, object);
	}

	/**
	 * Recupera un objeto del pool dado su identificador.
	 * @param id El identificador único del objeto a recuperar.
	 * @return El objeto del pool, o null si no se encuentra.
	 */
	public Object getObject(int id) {
		return pool.get(id);
	}

	/**
	 * Comprueba si un objeto con el identificador dado se encuentra en el pool.
	 * @param id El identificador único a comprobar.
	 * @return true si el pool contiene un objeto con el id, false en caso contrario.
	 */
	public boolean contains(int id) {
		return pool.containsKey(id);
	}

	/**
	 * Elimina un objeto del pool dado su identificador.
	 * @param id El identificador único del objeto a eliminar.
	 */
	public void removeObjet(int id) {
		pool.remove(id);
	}

}