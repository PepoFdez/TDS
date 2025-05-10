package dominio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;


/**
 * Representa un usuario de la aplicación AppChat.
 * Contiene información personal, de autenticación, estado premium,
 * y la gestión de sus contactos y mensajes.
 * <p>
 * La creación de instancias de Usuario se realiza a través de la clase interna {@link Builder}.
 * </p>
 * <p>
 * <b>Nota sobre seguridad:</b> Actualmente, esta clase maneja contraseñas en texto plano.
 * En un entorno de producción, esto debería cambiarse para almacenar un hash seguro de la contraseña.
 * </p>
 */
public class Usuario {
	
	private int id;
	private String nombre;
	private String apellidos;
	private String email; 
	private String movil;
	private String password;
	private LocalDate fechaNacimiento;
	private String URLimagen; 
	private String saludo;
	private boolean premium;
	private final LocalDate fechaRegistro;
	private final Set<Contacto> contactos = new HashSet<Contacto>(); //necesario definir los .equals y hashCode, pero aseguramos no duplicidad
	private final List<ContactoIndividual> contactosIndividuales = new LinkedList<ContactoIndividual>();
	
	/**
     * Constructor privado. Utilizar {@link Usuario.Builder} para crear instancias.
     *
     * @param builder El builder con la configuración del usuario.
     */
	private Usuario(Builder builder) {
        this.nombre = builder.nombre;
        this.apellidos = builder.apellidos;
        this.email = builder.email;
        this.movil = builder.movil;
        this.password = builder.password;
        this.fechaNacimiento = builder.fechaNacimiento;
        this.URLimagen = builder.URLimagen;
        this.saludo = builder.saludo;
        this.premium = builder.premium;
        this.fechaRegistro = builder.fechaRegistro;
    }
	
	/**
     * Obtiene el ID único del usuario (generalmente asignado por la persistencia).
     * @return El ID del usuario.
     */
	public int getId() {
		return id;
	}

	/**
     * Establece el ID único del usuario.
     * Este método es utilizado principalmente por la capa de persistencia.
     * @param id El nuevo ID del usuario.
     */
	public void setId(int id) {
		this.id = id;
	}

	/**
     * Obtiene el nombre de pila del usuario.
     * @return El nombre del usuario.
     */
	public String getNombre() {
		return nombre;
	}

	/**
     * Establece el nombre de pila del usuario.
     * @param nombre El nuevo nombre. No debe ser nulo.
     * @throws NullPointerException si el nombre es nulo.
     */
    public void setNombre(String nombre) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo.");
    }

	/**
     * Obtiene los apellidos del usuario.
     * @return Los apellidos del usuario.
     */
	public String getApellidos() {
		return apellidos;
	}

	/**
     * Establece los apellidos del usuario.
     * @param apellidos Los nuevos apellidos. No deben ser nulos.
     * @throws NullPointerException si los apellidos son nulos.
     */
	public void setApellidos(String apellidos) {
		this.apellidos = Objects.requireNonNull(apellidos, "Los apellidos no puede ser nulo.");;
	}
	
	 /**
     * Obtiene la dirección de correo electrónico del usuario.
     * @return El email del usuario.
     */
	public String getEmail() {
		return email;
	}

	/**
     * Establece la dirección de correo electrónico del usuario.
     * @param email El nuevo email. No debe ser nulo.
     * TODO: Considerar añadir validación de formato de email.
     * @throws NullPointerException si el email es nulo.
     */
	public void setEmail(String email) {
		this.email = Objects.requireNonNull(email, "El email no puede ser nulo.");;
	}
	
	/**
     * Obtiene el número de teléfono móvil del usuario.
     * Este número suele usarse como identificador para el login.
     * @return El número de móvil del usuario.
     */
	public String getMovil() {
		return movil;
	}

	public void setMovil(String movil) {
		this.movil = Objects.requireNonNull(movil, "El movil no puede ser nulo.");;
	}


	/**
     * Obtiene la contraseña del usuario en texto plano.
     * <p>
     * <b>Advertencia de Seguridad:</b> Exponer contraseñas en texto plano es una mala práctica.
     * Este método se mantiene por compatibilidad con la estructura original del proyecto.
     * </p>
     * @return La contraseña del usuario.
     */
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password =  Objects.requireNonNull(password, "La contraseña no puede ser nula.");;
	}

	/**
     * Obtiene la fecha de nacimiento del usuario.
     * @return La fecha de nacimiento, o {@code null} si no se ha establecido.
     */
	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	 /**
     * Obtiene la URL de la imagen de perfil del usuario.
     * @return La URL de la imagen, o una cadena vacía si no se ha establecido.
     */
	public String getURLImagen() {
		return URLimagen;
	}

	public void setURLImagen(String imagen) {
		this.URLimagen = imagen;
	}

	 /**
     * Obtiene el mensaje de saludo del perfil del usuario.
     * @return El mensaje de saludo, o una cadena vacía si no se ha establecido.
     */
	public String getSaludo() {
		return saludo;
	}

	public void setSaludo(String saludo) {
		this.saludo = saludo;
	}

	/**
     * Verifica si el usuario tiene una cuenta Premium.
     * @return {@code true} si el usuario es Premium, {@code false} en caso contrario.
     */
	public boolean isPremium() {
		return premium;
	}

	public void activarPremium() {
		this.premium = true;
	}
	
	public void desactivarPremium() {
		this.premium = false;
	}
	
	/**
     * Obtiene la fecha en que el usuario se registró en el sistema.
     * Este campo es final y se establece durante la creación del usuario.
     * @return La fecha de registro del usuario.
     */
	public LocalDate getFechaRegistro() {
		return fechaRegistro;
	}
	
	/**
     * Devuelve una lista del conjunto de todos los contactos (individuales y grupos)
     * de este usuario.
     * @return Un {@link LinkedList} de {@link Contacto}.
     */
	public LinkedList<Contacto> getContactos() {
		return new LinkedList<>(contactos);
	}
	
	/**
     * Devuelve una lista del conjunto de contactos que son individuales (no grupos)
     * de este usuario.
     * @return Un {@link List} de {@link ContactoIndividual}.
     */
	public List<ContactoIndividual> getContactosIndividuales() {
		return new LinkedList<>(contactosIndividuales);
	}

	public void addContacto(Contacto contacto) {
		this.contactos.add(contacto);
		if (contacto instanceof ContactoIndividual) {
			this.contactosIndividuales.add((ContactoIndividual) contacto);
		}
	}
	
	public void removeContacto(Contacto contacto) {
		this.contactos.remove(contacto);
		if (contacto instanceof ContactoIndividual) {
			this.contactosIndividuales.remove(contacto);
		}
	}
	
	 // Clase Builder interna
    public static class Builder {
        private String nombre;
        private String apellidos;
        private String email;
        private String movil;
        private String password;
        private LocalDate fechaNacimiento;
        private String URLimagen = "";
        private String saludo = "";
        private boolean premium = false;
        private LocalDate fechaRegistro = LocalDate.now();

        public Builder(String nombre, String apellidos, String email, String movil, String password, LocalDate fechaNacimiento) {
            this.nombre = nombre;
            this.apellidos = apellidos;
            this.email = email;
            this.movil = movil;
            this.password = password;
            this.fechaNacimiento = fechaNacimiento;
        }

        public Builder addURLimagen(String URLimagen) {
            this.URLimagen = URLimagen;
            return this;
        }

        public Builder addSaludo(String saludo) {
            this.saludo = saludo;
            return this;
        }

        public Builder addPremium(boolean premium) {
            this.premium = premium;
            return this;
        }

        public Builder addFechaRegistro(LocalDate fechaRegistro) {
            this.fechaRegistro = fechaRegistro;
            return this;
        }

        public Usuario build() {
            // Validaciones de seguridad
            if (nombre == null ||apellidos==null || email == null || movil == null || password == null || fechaNacimiento == null) {
                throw new IllegalArgumentException("Faltan campos obligatorios");
            }
            return new Usuario(this);
        }
    }
    
	public boolean tieneContactoConMovil(String movil2) {
		
		return contactosIndividuales.stream()
									.map(c -> c.getUsuario().getMovil())
									.anyMatch(m -> m.equals(movil2));
	}
	

	public ContactoIndividual getContactoConMovil(String movil2) {
	    return contactosIndividuales.stream()
	        .filter(contacto -> contacto.getUsuario().getMovil().equals(movil2))
	        .findFirst()
	        .orElse(null);
	}


	public Contacto enviarMensaje(Mensaje mensaje, int idContacto) {
		// TODO Auto-generated method stub
		Contacto contacto = this.contactos.stream()
				.filter(c -> c.getId() == idContacto)
				.findFirst()
				.orElse(null);
		
		if (contacto != null) {
			System.out.println("Enviando mensaje a " + contacto.getNombre() + ": " + mensaje.getTexto());
			contacto.addMensaje(mensaje);
			/*for (Contacto c : this.contactos) {
				for (Mensaje m : c.getMensajesEnviados()) {
					System.out.println("Mensaje: " + m.getTexto());
				}
			}*/
			// Aquí puedes agregar la lógica para enviar el mensaje al contact
		}
		return contacto;
	}

	public String getUltimoMensaje(Contacto contacto) {
		// TODO Auto-generated method stub
		return contacto.getUltimoMensaje();
	}

	public Contacto getGrupoConNombre(String nombre) {
	    return this.contactos.stream()
	            .filter(c -> c instanceof Grupo && c.getNombre().equals(nombre))
	            .findFirst()
	            .orElse(null);
	}

	public int getNumeroMensajesEnviadosMesPasado() {
		return (int) contactos.stream()
				.flatMap(c -> c.getTodosLosMensajesEnviados().stream())
				.filter(m-> m.getFecha().isEqual(LocalDateTime.now().minusMonths(1)))
				.count();
	}
}

