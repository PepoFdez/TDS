package dominio;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Usuario {
	
	private int id;
	private String nombre;
	private String apellidos;
	private String email; 
	private String movil;
	private String password;
	private Date fechaNacimiento;
	private String URLimagen; //falta comprobar este atributo
	private String saludo;
	private boolean premium;
	//private Descuento descuento;???
	private final LocalDate fechaRegistro;
	private final Set<Contacto> contactos = new HashSet<Contacto>(); //necesario definir los .equals y hashCode, pero aseguramos no duplicidad
	//podemos usar listas auxiliares para v.acceso
	
	//completo para usarlo en la persistencia
	public Usuario(String nombre, String apellidos, String email, String movil, String password,
			Date fechaNacimiento, String URLimagen, String saludo, boolean premium) {
		
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.movil = movil;
		this.password = password;
		this.fechaNacimiento = fechaNacimiento;
		this.saludo = saludo;
		this.URLimagen = URLimagen;
		this.premium = premium;
		this.fechaRegistro = LocalDate.now();
		
	}
	
	//por defecto un usuario no es premium, sino que se activa después, ni tendrá contactos
	public Usuario(String nombre, String apellidos, String email, String movil, String password,
			Date fechaNacimiento, String URLimagen, String saludo) {
		this(nombre, apellidos, email, movil, password, fechaNacimiento, URLimagen, saludo, false);
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

	public void setMovil(String movil) {
		this.movil = movil;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getURLImagen() {
		return URLimagen;
	}

	public void setURLImagen(String imagen) {
		this.URLimagen = imagen;
	}

	public String getSaludo() {
		return saludo;
	}

	public void setSaludo(String saludo) {
		this.saludo = saludo;
	}

	public boolean isPremium() {
		return premium;
	}

	public void activarPremium() {
		this.premium = true;
	}
	
	public void desactivarPremium() {
		this.premium = false;
	}
	
	public LocalDate getFechaRegistro() {
		return fechaRegistro;
	}
	
	public LinkedList<Contacto> getContactos() {
		return new LinkedList<>(contactos);
	}
	
	
}

