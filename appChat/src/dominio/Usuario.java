package dominio;


public class Usuario {
	
	private int id;
	private String nombre;
	private String apellidos;
	private String email; 
	private String login; //Se convierte en movil en la interfaz
	private String password;
	private String fechaNacimiento;
	//Añadidos por Pepo
	private int movil;
	private String imagen; //No sé si las imágenes tienen que ser URLS o estar localmente, porque el aula virtual se contradice
	private String saludo;
	private boolean isPremium;

	public Usuario(String nombre, String apellidos, String email, String login, int movil, String saludo, String password,
			String fechaNacimiento, String imagen) {
		this.id = 0;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.login = login;
		this.password = password;
		this.fechaNacimiento = fechaNacimiento;
		this.movil = movil;
		this.saludo = saludo;
		this.imagen = imagen;
		this.isPremium = false;
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
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
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

	public int getMovil() {
		return movil;
	}

	public void setMovil(int movil) {
		this.movil = movil;
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
	
	

}

