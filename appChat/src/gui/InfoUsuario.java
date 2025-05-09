package gui;

import javax.swing.*;
import controlador.Controlador;
import dominio.Contacto;
import java.awt.*;
import java.net.URL;
import java.text.SimpleDateFormat;

public class InfoUsuario {
	private static InfoUsuario instance;
	private JDialog dialog;
	private boolean mostrarUsuarioActual;

	private InfoUsuario(Contacto contacto) {
		this.mostrarUsuarioActual = (contacto == null);
		initialize(contacto);
	}

	public static void mostrarInfoContacto(Contacto contacto) {
		if (instance != null && instance.dialog != null) {
			instance.dialog.dispose();
		}
		instance = new InfoUsuario(contacto);
		instance.mostrarVentana();
	}

	public static void mostrarInfoUsuarioActual() {
		mostrarInfoContacto(null);
	}

	private void initialize(Contacto contacto) {
		dialog = new JDialog();
		dialog.setTitle(mostrarUsuarioActual ? "Información de Usuario" : "Información de Contacto");
		dialog.setModal(true);
		dialog.setSize(600, 500); // Ventana más grande
		dialog.setLocationRelativeTo(null);
		dialog.setResizable(true); // Permitir redimensionar

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

		// Panel de contenido con scroll por si el contenido es muy largo
		JScrollPane scrollPane = new JScrollPane(mainPanel);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		dialog.add(scrollPane);

		// Panel superior con imagen y nombre
		JPanel topPanel = new JPanel(new BorderLayout(15, 0));
		topPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Imagen de perfil
		String URLimagenContacto = mostrarUsuarioActual ? Controlador.INSTANCE.getURLImagenUsuario()
				: Controlador.INSTANCE.getURLImagenContacto(contacto);
		ImageIcon icono = new ImageIcon(URLimagenContacto);
		Image imgEscalada = getImagenContactoEscalada(URLimagenContacto, icono, 120);
		JLabel profileImage = new JLabel(new ImageIcon(imgEscalada));
		profileImage.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 20));
		topPanel.add(profileImage, BorderLayout.WEST);

		// Panel con información de texto
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		// Nombre
		String nombreCompleto = mostrarUsuarioActual
				? Controlador.INSTANCE.getNombreUsuario() + " " + Controlador.INSTANCE.getApellidosUsuario()
				: contacto.getNombre();
		JLabel nameLabel = new JLabel(nombreCompleto);
		nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
		nameLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
		infoPanel.add(nameLabel);

		// Saludo
		String saludo = mostrarUsuarioActual ? Controlador.INSTANCE.getSaludoUsuario()
				: Controlador.INSTANCE.getSaludoContacto(contacto);
		if (saludo != null && !saludo.isEmpty()) {
			JLabel greetingLabel = new JLabel(
					"<html><div style='width:300px;word-wrap:break-word;'>\"" + saludo + "\"</div></html>");
			greetingLabel.setFont(new Font("Arial", Font.PLAIN, 14));
			greetingLabel.setForeground(new Color(70, 70, 70));
			greetingLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
			infoPanel.add(greetingLabel);
		}

		topPanel.add(infoPanel, BorderLayout.CENTER);
		mainPanel.add(topPanel);

		// Separador
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		mainPanel.add(new JSeparator());
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		// Panel de detalles con GridBagLayout para mejor control
		JPanel detailsPanel = new JPanel(new GridBagLayout());
		detailsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;

		// Teléfono
		String telefono = mostrarUsuarioActual ? Controlador.INSTANCE.getTelefonoUsuario()
				: Controlador.INSTANCE.getTelefono(contacto);
		addDetail(detailsPanel, "Teléfono", telefono, gbc);

		// Email (solo usuario actual)
		if (mostrarUsuarioActual) {
			addDetail(detailsPanel, "Email", Controlador.INSTANCE.getEmailUsuario(), gbc);
		}

		// Fechas (solo usuario actual)
		if (mostrarUsuarioActual) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			addDetail(detailsPanel, "Nacimiento", dateFormat.format(Controlador.INSTANCE.getFechaNacimientoUsuario()),
					gbc);
			addDetail(detailsPanel, "Miembro desde",
					dateFormat.format(Controlador.INSTANCE.getFechaCreacionCuentaUsuario()), gbc);
		}

		mainPanel.add(detailsPanel);
		mainPanel.add(Box.createVerticalGlue());

		// Botón de cerrar
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

	private void addDetail(JPanel panel, String label, String value, GridBagConstraints gbc) {
		// Etiqueta
		JLabel labelLbl = new JLabel(label + ":");
		labelLbl.setFont(new Font("Arial", Font.BOLD, 14));
		labelLbl.setPreferredSize(new Dimension(150, 20)); // Más ancho para etiquetas largas
		gbc.gridx = 0;
		panel.add(labelLbl, gbc);

		// Valor en un JTextArea no editable para mejor manejo de texto largo
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

	public void mostrarVentana() {
		if (dialog != null && dialog.isVisible()) {
			dialog.toFront();
			return;
		}
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	private Image getImagenContactoEscalada(String URLimagenContacto, ImageIcon icono, int size) {
		try {
			if (URLimagenContacto != null && !URLimagenContacto.isEmpty()) {
				URL url = new URL(URLimagenContacto);
				icono = new ImageIcon(url);
			} else {
				icono = new ImageIcon("phphotos/pfp.jpg");
			}
			Image imgEscalada = icono.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
			return imgEscalada;
		} catch (Exception e) {
			System.err.println("Error al cargar la imagen del contacto: " + e.getMessage());
			return null;
		}
	}
}