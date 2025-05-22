package gui;

import javax.swing.*;
import controlador.Controlador;
import dominio.Contacto;
import java.awt.*;
import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * Clase que muestra una ventana con información detallada de un contacto o del usuario actual.
 * Se implementa como singleton para evitar múltiples instancias abiertas simultáneamente.
 */
public class InfoUsuario {
    private static InfoUsuario instance;
    private JDialog dialog;
    private boolean mostrarUsuarioActual;

    /**
     * Constructor privado. Inicializa la ventana de información.
     * 
     * @param contacto El contacto del cual mostrar la información, o {@code null} si se trata del usuario actual.
     */
    private InfoUsuario(Contacto contacto) {
        this.mostrarUsuarioActual = (contacto == null);
        initialize(contacto);
    }

    /**
     * Muestra una ventana con la información del contacto especificado.
     * 
     * @param contacto El contacto cuya información se va a mostrar.
     */
    public static void mostrarInfoContacto(Contacto contacto) {
        if (instance != null && instance.dialog != null) {
            instance.dialog.dispose();
        }
        instance = new InfoUsuario(contacto);
        instance.mostrarVentana();
    }

    /**
     * Muestra una ventana con la información del usuario actual.
     */
    public static void mostrarInfoUsuarioActual() {
        mostrarInfoContacto(null);
    }

    /**
     * Inicializa todos los componentes gráficos de la ventana de información.
     * 
     * @param contacto El contacto del cual mostrar la información (o {@code null} si es el usuario actual).
     */
    private void initialize(Contacto contacto) {
        dialog = new JDialog();
        dialog.setTitle(mostrarUsuarioActual ? "Información de Usuario" : "Información de Contacto");
        dialog.setModal(true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(true);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        dialog.add(scrollPane);

        JPanel topPanel = new JPanel(new BorderLayout(15, 0));
        topPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String URLimagenContacto = mostrarUsuarioActual
                ? Controlador.INSTANCE.getURLImagenUsuario()
                : Controlador.INSTANCE.getURLImagenContacto(contacto);
        ImageIcon icono = new ImageIcon(URLimagenContacto);
        Image imgEscalada = getImagenContactoEscalada(URLimagenContacto, icono, 120);
        JLabel profileImage = new JLabel(new ImageIcon(imgEscalada));
        profileImage.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 20));
        topPanel.add(profileImage, BorderLayout.WEST);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        String nombreCompleto = mostrarUsuarioActual
                ? Controlador.INSTANCE.getNombreUsuario() + " " + Controlador.INSTANCE.getApellidosUsuario()
                : contacto.getNombre();
        JLabel nameLabel = new JLabel(nombreCompleto);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        nameLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        infoPanel.add(nameLabel);

        String saludo = mostrarUsuarioActual
                ? Controlador.INSTANCE.getSaludoUsuario()
                : Controlador.INSTANCE.getSaludoContacto(contacto);
        if (saludo != null && !saludo.isEmpty()) {
            JLabel greetingLabel = new JLabel("<html><div style='width:300px;word-wrap:break-word;'>\"" + saludo + "\"</div></html>");
            greetingLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            greetingLabel.setForeground(new Color(70, 70, 70));
            greetingLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            infoPanel.add(greetingLabel);
        }

        topPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(new JSeparator());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;

        String telefono = mostrarUsuarioActual
                ? Controlador.INSTANCE.getTelefonoUsuario()
                : Controlador.INSTANCE.getTelefono(contacto);
        addDetail(detailsPanel, "Teléfono", telefono, gbc);

        if (mostrarUsuarioActual) {
            addDetail(detailsPanel, "Email", Controlador.INSTANCE.getEmailUsuario(), gbc);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            if (dateFormat.format(Controlador.INSTANCE.getFechaNacimientoUsuario()).equals("01/01/1970")) {
				addDetail(detailsPanel, "Nacimiento", "-", gbc);
			} else {
				addDetail(detailsPanel, "Nacimiento", dateFormat.format(Controlador.INSTANCE.getFechaNacimientoUsuario()), gbc);
			}
            addDetail(detailsPanel, "Miembro desde", dateFormat.format(Controlador.INSTANCE.getFechaCreacionCuentaUsuario()), gbc);
        }

        mainPanel.add(detailsPanel);
        mainPanel.add(Box.createVerticalGlue());

        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("Cerrar");
        closeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        closeButton.setPreferredSize(new Dimension(120, 35));
        closeButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeButton);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(buttonPanel);

        dialog.add(mainPanel);
    }

    /**
     * Añade un par etiqueta-valor al panel de detalles.
     * 
     * @param panel Panel al que se añade el dato.
     * @param label Etiqueta del dato.
     * @param value Valor del dato.
     * @param gbc Constraints de posicionamiento en el GridBagLayout.
     */
    private void addDetail(JPanel panel, String label, String value, GridBagConstraints gbc) {
        JLabel labelLbl = new JLabel(label + ":");
        labelLbl.setFont(new Font("Arial", Font.BOLD, 14));
        labelLbl.setPreferredSize(new Dimension(150, 20));
        gbc.gridx = 0;
        panel.add(labelLbl, gbc);

        JTextArea valueArea = new JTextArea(value);
        valueArea.setFont(new Font("Arial", Font.PLAIN, 14));
        valueArea.setEditable(false);
        valueArea.setLineWrap(true);
        valueArea.setWrapStyleWord(true);
        valueArea.setBackground(null);
        valueArea.setBorder(BorderFactory.createEmptyBorder());
        valueArea.setOpaque(false);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(valueArea, gbc);

        gbc.gridy++;
    }

    /**
     * Muestra la ventana de información si no está ya visible.
     */
    public void mostrarVentana() {
        if (dialog != null && dialog.isVisible()) {
            dialog.toFront();
            return;
        }
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    /**
     * Carga una imagen de perfil escalada a un tamaño específico.
     * 
     * @param URLimagenContacto URL de la imagen del contacto.
     * @param icono Icono base.
     * @param size Tamaño deseado para la imagen.
     * @return Imagen escalada.
     */
    private Image getImagenContactoEscalada(String URLimagenContacto, ImageIcon icono, int size) {
        try {
            if (URLimagenContacto != null && !URLimagenContacto.isEmpty()) {
                URL url = new URL(URLimagenContacto);
                icono = new ImageIcon(url);
            } else {
                icono = new ImageIcon("phphotos/pfp.jpg");
            }
            return icono.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen del contacto: " + e.getMessage());
            return null;
        }
    }
}
