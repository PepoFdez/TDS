package gui;

import controlador.Controlador;
import dominio.Contacto;
import dominio.Mensaje;
import dto.MensajeContextualizado;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

/**
 * Clase que representa una ventana para buscar mensajes según texto, teléfono o contacto.
 * Proporciona una interfaz gráfica para filtrar mensajes a través de distintos criterios.
 */
public class Buscador {
    /** Instancia única del buscador (patrón Singleton) */
    private static Buscador instance;

    /** Ventana principal del buscador */
    private JFrame frame;

    /** Campo de texto para buscar por contenido del mensaje */
    private JTextField txtTexto;

    /** Campo de texto para buscar por teléfono */
    private JTextField txtTelefono;

    /** Desplegable para seleccionar un contacto */
    private JComboBox<String> cbContacto;

    /** Botón para lanzar la búsqueda */
    private JButton btnBuscar;

    /** Modelo de lista para mostrar los mensajes encontrados */
    private DefaultListModel<String> modeloMensajes;

    /** Componente de lista donde se muestran los mensajes */
    private JList<String> listaMensajes;

    /**
     * Constructor que inicializa la ventana del buscador.
     */
    public Buscador() {
        initialize();
    }

    /**
     * Obtiene la instancia única del buscador.
     * @return Instancia del buscador.
     */
    public static Buscador getInstance() {
        if (instance == null) {
            instance = new Buscador();
        }
        return instance;
    }

    /**
     * Muestra la ventana del buscador, trayéndola al frente si ya está visible.
     */
    public void mostrarVentana() {
        if (frame != null && frame.isVisible()) {
            frame.toFront();
            return;
        }
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Inicializa los componentes gráficos de la ventana del buscador.
     */
    private void initialize() {
        frame = new JFrame("Buscador de Mensajes");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setMinimumSize(new Dimension(600, 400));
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(240, 240, 240));

        JPanel panelBusqueda = new JPanel(new GridBagLayout());
        panelBusqueda.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder("Criterios de Búsqueda"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        panelBusqueda.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTexto = new JLabel("Texto del mensaje:");
        lblTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 0;
        panelBusqueda.add(lblTexto, gbc);

        txtTexto = new JTextField(20);
        txtTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        panelBusqueda.add(txtTexto, gbc);

        JLabel lblTelefono = new JLabel("Teléfono relacionado:");
        lblTelefono.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.0;
        panelBusqueda.add(lblTelefono, gbc);

        txtTelefono = new JTextField(20);
        txtTelefono.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0;
        panelBusqueda.add(txtTelefono, gbc);

        JLabel lblContacto = new JLabel("Contacto:");
        lblContacto.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.0;
        panelBusqueda.add(lblContacto, gbc);

        cbContacto = new JComboBox<>();
        cbContacto.setFont(new Font("Arial", Font.PLAIN, 14));
        cargarContactos();
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weightx = 1.0;
        panelBusqueda.add(cbContacto, gbc);
        cbContacto.addItem("Selecciona un contacto");
        cbContacto.setSelectedItem("Selecciona un contacto");

        btnBuscar = new JButton("Buscar Mensajes");
        btnBuscar.setFont(new Font("Arial", Font.BOLD, 14));
        btnBuscar.setBackground(new Color(70, 130, 180));
        btnBuscar.setForeground(Color.WHITE);

        // --- Soluciones para macOS ---
        btnBuscar.setOpaque(true); // Clave para que setBackground funcione bien en Mac
        btnBuscar.setBorderPainted(false); // Ayuda a evitar que el L&F de Mac interfiera

        btnBuscar.setFocusPainted(false);

        gbc.gridx = 1; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST; 
        panelBusqueda.add(btnBuscar, gbc);

        frame.add(panelBusqueda, BorderLayout.NORTH);

        JPanel panelResultados = new JPanel(new BorderLayout());
        panelResultados.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder("Resultados de la Búsqueda"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        panelResultados.setBackground(Color.WHITE);

        modeloMensajes = new DefaultListModel<>();
        listaMensajes = new JList<>(modeloMensajes);
        listaMensajes.setFont(new Font("Arial", Font.PLAIN, 14));
        listaMensajes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(listaMensajes);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panelResultados.add(scrollPane, BorderLayout.CENTER);

        frame.add(panelResultados, BorderLayout.CENTER);

        btnBuscar.addActionListener(this::realizarBusqueda);
    }

    /**
     * Carga los contactos del usuario actual en el combo desplegable.
     */
    private void cargarContactos() {
        LinkedList<Contacto> contactos = Controlador.INSTANCE.getContactosUsuario();
        cbContacto.addItem("Selecciona un contacto");
        for (Contacto contacto : contactos) {
            cbContacto.addItem(contacto.getNombre());
        }
    }
    
    public void cargarContactosExternamente() {
    	initialize(); // Re-inicializa la ventana
    	cbContacto.removeAllItems();
    	cargarContactos();
    }

    /**
     * Ejecuta la búsqueda de mensajes según los criterios introducidos y muestra los resultados.
     * @param e Evento de acción disparado por el botón de búsqueda.
     */
    private void realizarBusqueda(ActionEvent e) {
        String texto = txtTexto.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String contactoSeleccionadoNombre = (String) cbContacto.getSelectedItem();

        // No es necesario convertir "Selecciona un contacto" a null aquí, 
        // ya que Usuario.buscarMisMensajes lo maneja.

        if (texto.isEmpty() && telefono.isEmpty() && 
            ("Selecciona un contacto".equalsIgnoreCase(contactoSeleccionadoNombre) || contactoSeleccionadoNombre == null || contactoSeleccionadoNombre.isEmpty())) {
            JOptionPane.showMessageDialog(frame,
                "Por favor, ingrese al menos un criterio de búsqueda",
                "Búsqueda vacía", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Llamada al controlador, ahora devuelve List<Usuario.MensajeContextualizado>
        java.util.List<MensajeContextualizado> resultados = Controlador.INSTANCE.buscarMensajes(
            texto, telefono, contactoSeleccionadoNombre); //

        modeloMensajes.clear();

        if (resultados.isEmpty()) {
            modeloMensajes.addElement("No se encontraron mensajes con los criterios especificados.");
        } else {
            DateTimeFormatter formatter = utils.Utils.formatoFechaHora; // Asumiendo que existe en Utils
                                                                     // o DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            for (MensajeContextualizado mc : resultados) {
                Mensaje mensaje = mc.getMensaje();
                String nombreEmisor;

                // Determinar quién es el emisor para la visualización
                if (mensaje.getTipo() == tds.BubbleText.SENT) {
                     // Si el mensaje fue enviado por el usuario actual, el interlocutor es el contacto.
                     // Pero para la búsqueda, queremos mostrar "Tú" o el nombre del usuario actual.
                     nombreEmisor = mc.getNombreUsuarioActual(); // O simplemente "Tú"
                } else {
                     // Si fue recibido, el interlocutor directo es el nombre del contacto que lo envió.
                     nombreEmisor = mc.getNombreInterlocutorDirecto();
                }

                String mensajeDisplay;
                if (mensaje.getEmoticono() != Mensaje.SIN_EMOTICONO) { //
                    mensajeDisplay = String.format("%s (%s): [Emoji %d]",
                        nombreEmisor,
                        mensaje.getFecha().format(formatter),
                        mensaje.getEmoticono());
                } else {
                    mensajeDisplay = String.format("%s (%s): %s",
                        nombreEmisor,
                        mensaje.getFecha().format(formatter),
                        mensaje.getTexto());
                }
                modeloMensajes.addElement(mensajeDisplay);
            }
        }
    }
}
