package dominio;

import java.util.LinkedList;

public class Usuario {
	
	private int id;
	private String nombre;
	private String apellidos;
	private String email; 
	private String movil; //Se convierte en movil en la interfaz
	private String password;
	private String fechaNacimiento;
	//Añadidos por Pepo
	private String imagen; //No sé si las imágenes tienen que ser URLS o estar localmente, porque el aula virtual se contradice
	private String saludo;
	private String isPremium;
	
	//Coleccion de contactos
	private LinkedList<Contacto> contactos;

	public Usuario(String nombre, String apellidos, String email, String movil, String password,
			String fechaNacimiento, String imagen, String saludo, String isPremium) {
		this.id = 0;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.movil = movil;
		this.password = password;
		this.fechaNacimiento = fechaNacimiento;
		this.saludo = saludo;
		this.imagen = imagen;
		this.isPremium = isPremium;
		
		this.contactos = new LinkedList<Contacto>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getMovil() {
		return movil;
	}

	public LinkedList<Contacto> getContactos() {
		return contactos;
	}

	public void setMovil(String movil) {
		this.movil = movil;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getSaludo() {
		return saludo;
	}

	public void setSaludo(String saludo) {
		this.saludo = saludo;
	}

	public String getIsPremium() {
		return isPremium;
	}

	public void setIsPremium(String isPremium) {
		this.isPremium = isPremium;
	}

	public void setContactos(LinkedList<Contacto> contactos) {
		this.contactos = contactos;
	}
	
	

}

