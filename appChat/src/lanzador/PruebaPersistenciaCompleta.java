package lanzador;

import dominio.Usuario;
import dominio.ContactoIndividual;
import dominio.Grupo;
import dominio.Mensaje;
import dao.TDSUsuarioDAO;
import dao.TDSContactoIndividualDAO;
import dao.TDSGrupoDAO;
import dao.TDSMensajeDAO;
import dominio.RepositorioUsuarios;
import tds.BubbleText;
import java.time.LocalDate;
import java.util.List;

public class PruebaPersistenciaCompleta {

    public static void main(String[] args) {
        // 1. Inicialización de DAOs y Repositorio
        TDSUsuarioDAO usuarioDAO = TDSUsuarioDAO.getInstance();
        TDSContactoIndividualDAO contactoDAO = TDSContactoIndividualDAO.getInstance();
        TDSGrupoDAO grupoDAO = TDSGrupoDAO.getInstance();
        TDSMensajeDAO mensajeDAO = TDSMensajeDAO.getInstance();
/*
        // 2. Creación y registro de usuarios
        Usuario usuarioAdmin = crearUsuario(
            "Admin", 
            "Sistema", 
            "admin@chat.com", 
            "+34000000000", 
            "admin123", 
            LocalDate.of(2000, 1, 1), 
            "admin.png", 
            "Bienvenido al sistema"
        );
        usuarioDAO.registrarUsuario(usuarioAdmin);

        Usuario usuarioMaria = crearUsuario(
            "María", 
            "López", 
            "maria@chat.com", 
            "+34111111111", 
            "maria456", 
            LocalDate.of(1995, 8, 22), 
            "maria.jpg", 
            "¡Hola a todos!"
        );
        usuarioDAO.registrarUsuario(usuarioMaria);

        // 3. Creación de contactos individuales
        ContactoIndividual contactoAdmin = crearContacto(usuarioAdmin, "Admin Principal", contactoDAO);
        ContactoIndividual contactoMaria = crearContacto(usuarioMaria, "María Personal", contactoDAO);

        // 4. Creación de grupos con múltiples miembros
        Grupo grupoTrabajo = crearGrupo("Equipo de Trabajo", grupoDAO, contactoAdmin, contactoMaria);
        Grupo grupoAmigos = crearGrupo("Amigos", grupoDAO, contactoMaria);

        // 5. Asociación de grupos a usuarios
        usuarioAdmin.addContacto(grupoTrabajo);
        usuarioMaria.addContacto(grupoAmigos);
        usuarioDAO.updateUsuario(usuarioAdmin);
        usuarioDAO.updateUsuario(usuarioMaria);

        // 6. Creación y asociación de mensajes
        Mensaje mensajeSaludo = crearMensaje("Hola equipo, ¡empezamos!", BubbleText.SENT, mensajeDAO);
        Mensaje mensajeEmoticon = crearMensaje(128526, BubbleText.RECEIVED, mensajeDAO); // 😎

        grupoTrabajo.addMensaje(mensajeSaludo);
        grupoAmigos.addMensaje(mensajeEmoticon);
*/
        // 7. Recuperación y verificación de datos (descomentar tras primera ejecución)
       /* 
        System.out.println("\n--- Recuperación de datos ---");
        Usuario adminRecuperado = RepositorioUsuarios.INSTANCE.findUsuario("+34000000000");
        if (adminRecuperado != null) {
            System.out.println("Usuario recuperado: " + adminRecuperado.getNombre());
            System.out.println("Contactos asociados: " + adminRecuperado.getContactos().size());
            adminRecuperado.getContactos().forEach(c -> {
                if (c instanceof Grupo) {
                    System.out.println("Grupo: " + c.getNombre() + " - Miembros: " + ((Grupo) c).getMiembros().size());
				} else {
					System.out.println(c.getClass().getSimpleName()+": " + c.getNombre());
				}
            });
        }
        
        //añado un contacto individual
        Usuario nuevoUser = crearUsuario(
                "nuevo", 
                "para probear", 
                "nuevo@chat.com", 
                "+34000000123", 
                "12345", 
                LocalDate.of(2000, 1, 1), 
                "nuev.png", 
                "Hey! soy una prueba"
            );
        usuarioDAO.registrarUsuario(nuevoUser);
        ContactoIndividual contactoNuevo = crearContacto(nuevoUser, "Nuevo Contacto", contactoDAO);

        Mensaje mensajeP1 = crearMensaje("Hola JC, ¡empezamos!", BubbleText.SENT, mensajeDAO);
        Mensaje mensajeP2 = crearMensaje("Hola PEPO, ¡empezamos!", BubbleText.RECEIVED, mensajeDAO);
        
        contactoNuevo.addMensaje(mensajeP1);
        contactoNuevo.addMensaje(mensajeP2);
        
        contactoDAO.updateContactoIndividual(contactoNuevo);
        adminRecuperado.activarPremium();
        adminRecuperado.addContacto(contactoNuevo);
        
        
        if (usuarioDAO.getUsuario(adminRecuperado.getId()) != null) {
            usuarioDAO.updateUsuario(adminRecuperado);
        } else {
            System.out.println("Error: Usuario no encontrado en BD.");
        }
        */

        // 8. Eliminación en cascada (descomentar para probar)
        
        usuarioDAO.eliminarUsuario(RepositorioUsuarios.INSTANCE.findUsuario("+34000000000"));
        System.out.println("\n--- Usuario eliminado ---");
        System.out.println("Grupos restantes: " + grupoDAO.getAll().size());
        
    }

    // -------------------- Métodos auxiliares --------------------
    private static Usuario crearUsuario(String nombre, String apellidos, String email, 
                                       String movil, String password, LocalDate fechaNacimiento, 
                                       String imagen, String saludo) {
        return new Usuario(nombre, apellidos, email, movil, password, fechaNacimiento, imagen, saludo);
    }

    private static ContactoIndividual crearContacto(Usuario usuario, String nombre, 
                                                   TDSContactoIndividualDAO contactoDAO) {
        ContactoIndividual contacto = new ContactoIndividual(usuario, nombre);
        contactoDAO.registrarContactoIndividual(contacto);
        System.out.println("[Registro] Contacto creado: " + contacto.getId());
        return contacto;
    }

    private static Grupo crearGrupo(String nombre, TDSGrupoDAO grupoDAO, ContactoIndividual... miembros) {
        Grupo grupo = new Grupo(nombre, miembros);
        grupoDAO.registrarGrupo(grupo);
        System.out.println("[Registro] Grupo creado: " + grupo.getId());
        return grupo;
    }

    private static Mensaje crearMensaje(String texto, int tipo, TDSMensajeDAO mensajeDAO) {
        Mensaje mensaje = new Mensaje(texto, tipo);
        mensajeDAO.registrarMensaje(mensaje);
        System.out.println("[Registro] Mensaje creado: " + mensaje.getId());
        return mensaje;
    }

    private static Mensaje crearMensaje(int emoticon, int tipo, TDSMensajeDAO mensajeDAO) {
        Mensaje mensaje = new Mensaje(emoticon, tipo);
        mensajeDAO.registrarMensaje(mensaje);
        System.out.println("[Registro] Emoticon creado: " + mensaje.getId());
        return mensaje;
    }
}