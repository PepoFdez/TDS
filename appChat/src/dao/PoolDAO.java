package dao;

import java.util.HashMap;

public class PoolDAO {
/*Hay que crear una colección genérica para almacenar todos los objetos que se hayan recuperado de la 
 * base de datos. Para comprobar si están recuperados y no recuperarlos de nuevo*/
	private static PoolDAO instance = null;
	private HashMap<Integer, Object> pool;
	
	private PoolDAO () {
		pool = new HashMap<Integer, Object>();
		
	}
	
	public static PoolDAO getInstance() {
		if (instance == null) {
			instance = new PoolDAO();
		}
		return instance;
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
