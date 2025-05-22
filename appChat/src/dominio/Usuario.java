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

	/**
     * Establece el número de teléfono móvil del usuario.
     * @param movil El nuevo número de móvil. No debe ser nulo.
     * TODO: Considerar validación de formato y si se permite el cambio,
     * asegurar la unicidad si es un identificador clave.
     * @throws NullPointerException si el móvil es nulo.
     */
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

	/**
     * Establece la contraseña del usuario (en texto plano).
     * <p>
     * <b>Advertencia de Seguridad:</b> Almacenar o establecer contraseñas en texto plano es inseguro.
     * Este método se mantiene por compatibilidad con la estructura original.
     * </p>
     * @param password La nueva contraseña. No debe ser nula.
     * @throws NullPointerException si la contraseña es nula.
     */
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

	/**
     * Establece la fecha de nacimiento del usuario.
     * @param fechaNacimiento La nueva fecha de nacimiento (puede ser {@code null}).
     */
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

	/**
     * Establece la URL de la imagen de perfil del usuario.
     * Si la URL es {@code null}, se establece como una cadena vacía.
     * @param URLimagen La nueva URL de la imagen.
     */
	public void setURLImagen(String URLimagen) {
        this.URLimagen = (URLimagen != null) ? URLimagen : "";
    }

	 /**
     * Obtiene el mensaje de saludo del perfil del usuario.
     * @return El mensaje de saludo, o una cadena vacía si no se ha establecido.
     */
	public String getSaludo() {
		return saludo;
	}

	/**
     * Establece el mensaje de saludo del perfil del usuario.
     * Si el saludo es {@code null}, se establece como una cadena vacía.
     * @param saludo El nuevo mensaje de saludo.
     */
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

	/**
     * Activa el estado Premium del usuario.
     */
	public void activarPremium() {
		this.premium = true;
	}
	
	/**
     * Desactiva el estado Premium del usuario.
     */
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
     * Devuelve una nueva lista (copia defensiva) que contiene todos los contactos
     * (individuales y grupos) de este usuario.
     * Las modificaciones a la lista devuelta no afectarán a la colección interna de contactos.
     * @return Una nueva {@link List} (actualmente {@link LinkedList}) de {@link Contacto}.
     */
	public LinkedList<Contacto> getContactos() {
		return new LinkedList<>(contactos);
	}
	
	/**
     * Devuelve una nueva lista del conjunto de contactos que son individuales (no grupos)
     * de este usuario.
     * @return Un {@link List} de {@link ContactoIndividual}.
     */
	public List<ContactoIndividual> getContactosIndividuales() {
		return new LinkedList<>(contactosIndividuales);
	}

	/**
     * Añade un contacto a la lista de contactos del usuario.
     * @param contacto El {@link Contacto} a añadir. No debe ser nulo.
     * @throws NullPointerException si {@code contacto} es nulo.
     */
	public void addContacto(Contacto contacto) {
		Objects.requireNonNull(contacto, "El contacto a añadir no puede ser nulo.");
		this.contactos.add(contacto);
		if (contacto instanceof ContactoIndividual) {
			this.contactosIndividuales.add((ContactoIndividual) contacto);
		}
	}
	
	/**
     * Elimina un contacto de la lista de contactos del usuario.
     * @param contacto El {@link Contacto} a eliminar. No debe ser nulo.
     * @throws NullPointerException si {@code contacto} es nulo.
     */
	public void removeContacto(Contacto contacto) {
		this.contactos.remove(contacto);
		if (contacto instanceof ContactoIndividual) {
			this.contactosIndividuales.remove(contacto);
		}
	}
	

	// --- Clase Builder Interna Estática ---
    /**
     * Clase Builder para construir instancias de {@link Usuario} de forma fluida y legible.
     * Permite establecer campos obligatorios en el constructor y campos opcionales
     * mediante métodos encadenados.
     */
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

        /**
         * Constructor del Builder para los campos considerados obligatorios al crear un usuario.
         *
         * @param nombre Nombre del usuario.
         * @param apellidos Apellidos del usuario.
         * @param email Email del usuario.
         * @param movil Número de móvil, usado para identificación.
         * @param password Contraseña del usuario (en texto plano).
         */
        public Builder(String nombre, String apellidos, String email, String movil, String password, LocalDate fechaNacimiento) {
            this.nombre = nombre;
            this.apellidos = apellidos;
            this.email = email;
            this.movil = movil;
            this.password = password;
            this.fechaNacimiento = fechaNacimiento;
        }

        /**
         * Establece la URL de la imagen de perfil (opcional).
         * @param URLimagen La URL de la imagen.
         * @return Este mismo Builder para encadenamiento.
         */
        public Builder addURLimagen(String URLimagen) {
            this.URLimagen = URLimagen;
            return this;
        }

        /**
         * Establece el saludo del perfil (opcional).
         * @param saludo El mensaje de saludo.
         * @return Este mismo Builder para encadenamiento.
         */
        public Builder addSaludo(String saludo) {
            this.saludo = saludo;
            return this;
        }

        /**
         * Establece el estado Premium del usuario (opcional, por defecto {@code false}).
         * @param premium {@code true} si el usuario es premium, {@code false} en caso contrario.
         * @return Este mismo Builder para encadenamiento.
         */
        public Builder addPremium(boolean premium) {
            this.premium = premium;
            return this;
        }

        /**
         * Establece la fecha de registro (opcional, por defecto la fecha actual).
         * @param fechaRegistro La fecha de registro.
         * @return Este mismo Builder para encadenamiento.
         */
        public Builder addFechaRegistro(LocalDate fechaRegistro) {
            this.fechaRegistro = fechaRegistro;
            return this;
        }

        /**
         * Construye y devuelve una instancia de {@link Usuario} con la configuración actual del Builder.
         * Realiza validaciones sobre los campos obligatorios (nombre, apellidos, email, móvil, password).
         * @return La instancia de {@link Usuario} creada.
         * @throws IllegalArgumentException si alguno de los campos obligatorios es nulo o está vacío.
         */
        public Usuario build() {
            if (nombre == null || nombre.trim().isEmpty() ||
                apellidos == null || apellidos.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                movil == null || movil.trim().isEmpty() ||
                password == null || password.isEmpty()) {
                throw new IllegalArgumentException("Faltan campos obligatorios o están vacíos: nombre, apellidos, email, móvil, password.");
            }
            return new Usuario(this);
        }
    }
    
    /**
     * Verifica si el usuario tiene un contacto individual asociado a un número de móvil específico.
     *
     * @param movilABuscar El número de móvil a buscar entre los contactos individuales del usuario.
     * @return {@code true} si existe un {@link ContactoIndividual} cuyo {@link Usuario} asociado
     * tiene el móvil especificado, {@code false} en caso contrario o si {@code movilABuscar} es nulo.
     */
    public boolean tieneContactoConMovil(String movilABuscar) {
        if (movilABuscar == null) {
            return false;
        }
        return contactosIndividuales.stream()
        			.map(c -> c.getUsuario().getMovil())
        			.anyMatch(m -> m.equals(movilABuscar));
    }
	

    /**
     * Obtiene el {@link ContactoIndividual} asociado a un número de móvil específico, si existe.
     *
     * @param movilABuscar El número de móvil a buscar.
     * @return Un {@link ContactoIndividual} si lo encuentra o {@link <code>null</code>} si no lo encuentra. 
     */
	public ContactoIndividual getContactoConMovil(String movil2) {
	    return contactosIndividuales.stream()
	        .filter(contacto -> contacto.getUsuario().getMovil().equals(movil2))
	        .findFirst()
	        .orElse(null);
	}

	/**
     * Obtiene el {@link Contacto} asociado a id, si existe.
     *
     * @param movilABuscar El número de móvil a buscar.
     * @return Un {@link ContactoIndividual} si lo encuentra o {@link <code>null</code>} si no lo encuentra. 
     */
	public Contacto enviarMensaje(Mensaje mensaje, int idContacto) {
		Contacto contacto = this.contactos.stream()
				.filter(c -> c.getId() == idContacto)
				.findFirst()
				.orElse(null);
		
		if (contacto != null) {
			System.out.println("Enviando mensaje a " + contacto.getNombre() + ": " + mensaje.getTexto());
			contacto.addMensaje(mensaje);
		}
		return contacto;
	}

	/**
     * Obtiene el texto del último mensaje de un {@link Contacto} dado.
     * Este método asume que el contacto proporcionado es relevante para el usuario actual.
     *
     * @param contacto El {@link Contacto} del cual obtener el último mensaje. No debe ser nulo.
     * @return El texto del último mensaje del contacto (según {@link Contacto#getTextoUltimoMensaje()}),
     * o un mensaje placeholder si no hay mensajes.
     * @throws NullPointerException si {@code contacto} es nulo.
     */
	public String getUltimoMensaje(Contacto contacto) {
		Objects.requireNonNull(contacto, "El contacto para obtener el último mensaje no puede ser nulo.");
		return contacto.getUltimoMensaje();
	}

	/**
     * Busca un {@link Grupo} específico por su nombre entre los contactos del usuario.
     *
     * @param nombreGrupo El nombre del grupo a buscar. La búsqueda es sensible a mayúsculas/minúsculas.
     * @return Un {@link Optional<Grupo>} que contiene el grupo si se encuentra, o un <code>null</code> 
     * si {@code nombreGrupo} es nulo, vacío o no se encuentra un grupo con ese nombre.
     */
	public Contacto getGrupoConNombre(String nombre) {
	    return this.contactos.stream()
	            .filter(c -> c instanceof Grupo && c.getNombre().equals(nombre))
	            .findFirst()
	            .orElse(null);
	}

	/**
     * Calcula el número total de mensajes que este usuario ha enviado (cuyo tipo es {@code BubbleText.SENT})
     * a través de todos sus contactos durante el mes natural anterior al actual.
     *
     * @return El recuento de mensajes enviados el mes pasado.
     */
	public int getNumeroMensajesEnviadosMesPasado() {
		return (int) contactos.stream()
				.flatMap(c -> c.getTodosLosMensajesEnviados().stream())
				.filter(m-> m.getFecha().isAfter(LocalDateTime.now().minusMonths(1)))
				.count();
	}
}

