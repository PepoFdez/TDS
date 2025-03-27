package dao;

import java.util.HashMap;

public enum PoolDAO {
/*Hay que crear una colección genérica para almacenar todos los objetos que se hayan recuperado de la 
 * base de datos. Para comprobar si están recuperados y no recuperarlos de nuevo*/
	INSTANCE;
	
	private HashMap<Integer, Object> pool;
	
	private PoolDAO () {
		pool = new HashMap<Integer, Object>();
	}
	
	public void addObject(int id, Object object) {
		pool.put(id, object);	
	}
	
	public Object getObject(int id) {
		return pool.get(id);
	}
	
	public boolean contains(int id) {
		return pool.containsKey(id);
	}
	
}
