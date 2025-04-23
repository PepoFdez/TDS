package dominio;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
	//private Descuento descuento;???
	private final LocalDate fechaRegistro;
	private final Set<Contacto> contactos = new HashSet<Contacto>(); //necesario definir los .equals y hashCode, pero aseguramos no duplicidad
	private final List<ContactoIndividual> contactosIndividuales = new LinkedList<ContactoIndividual>();
	
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

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
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
	/*
	public void setPremium(boolean premium) {
		this.premium = premium;
	}*/

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
	
	public List<ContactoIndividual> getContactosIndividuales() {
		return new LinkedList<>(contactosIndividuales);
	}

	//ver si manejamos grupos y contactos en el mismo conjunto
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
		for (ContactoIndividual contacto : contactosIndividuales)  {  
			if (contacto.getUsuario().getMovil().equals(movil)) {
				return true;
		    }
		}
		return false;
	}
	
	public ContactoIndividual getContactoConMovil(String movil2) {
		for (ContactoIndividual contacto : contactosIndividuales)  {  
			if (contacto.getUsuario().getMovil().equals(movil)) {
				return contacto;
		    }
		}
		return null;
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
	
	
	
}

