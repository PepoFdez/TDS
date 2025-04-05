package dominio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mensaje {

	private int id;
	private final String texto;
	private final int emoticon;
	private final LocalDateTime fecha;
	private final int tipo; // usaremos las constantes enteras de BubbleText SENT y RECEIVED

	// para recuperarlo desde la BDD
	public Mensaje(String texto, int emoticon, LocalDateTime fecha, int tipo) {
		this.texto = texto;
		this.emoticon = emoticon;
		this.fecha = fecha;
		this.tipo = tipo;
	}

	public Mensaje(String texto, int tipo) {
		this(texto, -1, LocalDateTime.now(), tipo);
	}

	public Mensaje(int emoticon, int tipo) {
		this(null, emoticon, LocalDateTime.now(), tipo);
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getTexto() {
		return texto;
	}

	public int getEmoticon() {
		return emoticon;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public int getTipo() {
		return tipo;
	}

	public String getHora() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		return fecha.format(formatter);
	}
}
