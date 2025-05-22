package dominio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

// Se asume que utils.Utils es una clase de utilidad del proyecto.
import utils.Utils;

/**
 * Representa un mensaje intercambiado dentro de la aplicación.
 * Un mensaje puede contener texto, un emoticono, o ambos (aunque los constructores
 * actuales facilitan texto o emoticono por separado).
 * Es inmutable respecto a su contenido (texto, emoticono, fecha, tipo) una vez creado;
 * solo el {@code id} (para persistencia) es mutable.
 */
public class Mensaje {

    private static final DateTimeFormatter HORA_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    /**
     * Valor utilizado en el campo {@code emoticono} para indicar la ausencia de un emoticono.
     */
    public static final int SIN_EMOTICONO = -1;

    private int id; // Identificador único, asignado por la persistencia.
    private final String texto;
    private final int emoticono; // Código del emoticono, o SIN_EMOTICONO si no hay.
    private final LocalDateTime fecha;
    private final int tipo; // Tipo de mensaje (ej. enviado, recibido).

    /**
     * Constructor principal para crear un mensaje con todos sus atributos especificados.
     * Utilizado típicamente para recuperar mensajes desde la base de datos o para creaciones detalladas.
     *
     * @param texto     El contenido textual del mensaje. No debe ser nulo (puede ser vacío).
     * @param emoticono El código numérico del emoticono. Usar {@link #SIN_EMOTICONO} si no aplica.
     * @param fecha     La fecha y hora en que se envió o recibió el mensaje. No debe ser nula.
     * @param tipo      El tipo de mensaje (ej. constante para ENVIADO o RECIBIDO).
     * @throws NullPointerException si {@code texto} o {@code fecha} son nulos.
     */
    public Mensaje(String texto, int emoticono, LocalDateTime fecha, int tipo) {
        this.texto = Objects.requireNonNull(texto, "El texto del mensaje no puede ser nulo.");
        this.emoticono = emoticono;
        this.fecha = Objects.requireNonNull(fecha, "La fecha del mensaje no puede ser nula.");
        this.tipo = tipo;
    }

    /**
     * Crea un nuevo mensaje que contiene solo texto.
     * El emoticono se establece a {@link #SIN_EMOTICONO} y la fecha a la actual.
     *
     * @param texto El contenido textual del mensaje. No debe ser nulo.
     * @param tipo  El tipo de mensaje.
     * @throws NullPointerException si {@code texto} es nulo.
     */
    public Mensaje(String texto, int tipo) {
        this(texto, SIN_EMOTICONO, LocalDateTime.now(), tipo);
    }

    /**
     * Crea un nuevo mensaje que contiene solo un emoticono.
     * El texto se establece a una cadena vacía y la fecha a la actual.
     *
     * @param emoticono El código numérico del emoticono.
     * @param tipo      El tipo de mensaje.
     */
    public Mensaje(int emoticono, int tipo) {
        this("", emoticono, LocalDateTime.now(), tipo);
    }

    /**
     * Establece el identificador único del mensaje.
     * Usado principalmente por la capa de persistencia después de guardar el mensaje.
     *
     * @param id El nuevo id del mensaje.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el identificador único del mensaje.
     * @return El id del mensaje (puede ser 0 si no ha sido persistido).
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el contenido textual del mensaje.
     * @return El texto del mensaje (puede ser una cadena vacía).
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Obtiene el código numérico del emoticono.
     * @return El código del emoticono, o {@link #SIN_EMOTICONO} si el mensaje no tiene emoticono.
     */
    public int getEmoticono() {
        return emoticono;
    }

    /**
     * Obtiene la fecha y hora en que se creó (envió/recibió) el mensaje.
     * @return La {@link LocalDateTime} del mensaje.
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * Obtiene el tipo de mensaje (ej. enviado o recibido).
     * Actualmente un valor entero que corresponde a constantes de la UI (ej. {@code tds.BubbleText.SENT}).
     * @return El tipo de mensaje.
     */
    public int getTipo() {
        return tipo;
    }

    /**
     * Formatea la hora de la fecha del mensaje al formato "HH:mm".
     * @return La hora del mensaje como un String (ej. "14:35").
     */
    public String getHora() {
        return this.fecha.format(HORA_FORMATTER);
    }

    /**
     * Genera una cadena de información formateada sobre el mensaje,
     * combinando la hora y el tipo del mensaje, separados por {@code Utils.SEPARATOR}.
     * Útil para la visualización en la interfaz gráfica o logs.
     *
     * @return Una cadena con la hora y el tipo del mensaje formateados.
     */
    public String getInfoFormateada() {
        return this.getFechaHora() + Utils.SEPARATOR + this.tipo;
    }

    public String getFechaHora() {
		return this.fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
	}
 
}