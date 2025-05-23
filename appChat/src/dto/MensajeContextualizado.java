package dto;

import dominio.Mensaje;

// Nueva clase DTO interna o en el paquete dominio para pasar el contexto a la GUI
// Esto ayuda a la GUI a formatear sin tener que re-buscar el contacto.
public class MensajeContextualizado {
    private final Mensaje mensaje;
    private final String nombreInterlocutorDirecto; // Nombre del contacto o "Tú" (usuario actual)
    private final String nombreUsuarioActual; // Para saber quién es "Tú"

    public MensajeContextualizado(Mensaje mensaje, String nombreInterlocutorDirecto, String nombreUsuarioActual) {
        this.mensaje = mensaje;
        this.nombreInterlocutorDirecto = nombreInterlocutorDirecto;
        this.nombreUsuarioActual = nombreUsuarioActual;
    }

    public Mensaje getMensaje() { return mensaje; }
    public String getNombreInterlocutorDirecto() { return nombreInterlocutorDirecto; }
    public String getNombreUsuarioActual() { return nombreUsuarioActual; }
}