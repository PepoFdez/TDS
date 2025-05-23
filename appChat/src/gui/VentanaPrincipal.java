package gui;

import tds.BubbleText;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import controlador.Controlador;
import dominio.Contacto;

/**
 * Ventana principal de la aplicación de chat.
 * Muestra la lista de contactos, el área de chat para el contacto seleccionado,
 * y opciones de menú como buscar mensajes, gestionar contactos y activar premium.
 */
public class VentanaPrincipal {

	/** El marco principal de la ventana. */
	private JFrame frame;
	/** Lista de contactos del usuario actual. */
	private LinkedList<Contacto> contactos;
	/** Panel izquierdo que contiene la lista de contactos. */
	private JPanel leftPanel;
	/** Panel central que muestra el área de chat. */
	private ChatPanel chatPanel; // Cambiado de JTextArea a JPanel
	/** Scroll pane para el área de chat. */
	private JScrollPane chatScrollPane;
	/** Panel derecho que contiene el área de chat y el campo de mensaje. */
	private JPanel rightPanel;
	/** Panel superior del área de chat que muestra la información del contacto seleccionado. */
	private JPanel contactInfoPanel;
	/** Etiqueta que muestra el nombre del contacto seleccionado. */
	private JLabel currentContactLabel;
	/** Etiqueta que muestra la imagen del contacto seleccionado. */
	private JLabel currentContactImage;
	/** Campo de texto para escribir mensajes. */
	private JTextField messageField;
	/** El contacto actualmente seleccionado en la lista de contactos. */
	private Contacto contactoSeleccionado;
	/** Botón para abrir el selector de emojis. */
	private JButton emojiButton;
	/** Botón para gestionar la suscripción premium. */
	private JButton premiumButton;

	/**
	 * Constructor de la clase VentanaPrincipal.
	 * Inicializa la interfaz gráfica, carga los contactos y inicia el timer de actualización.
	 */
	public VentanaPrincipal() {
		BubbleText.noZoom(); // Desactivar zoom automático para HiDPI
		initialize();
		loadContacts(); // Cargar contactos al iniciar

		// Iniciar timer para actualización periódica
		iniciarTimerActualizacionContactos(2000); // 2000 ms = 2 segundos
	}

	/**
	 * Hace visible la ventana principal y la centra en la pantalla.
	 */
	public void mostrarVentana() {
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	/**
	 * Inicia un timer que actualiza periódicamente la lista de contactos
	 * y el estado del botón premium.
	 * @param delayMillis El retardo en milisegundos entre cada actualización.
	 */
	private void iniciarTimerActualizacionContactos(int delayMillis) {
		Timer timer = new Timer(delayMillis, e -> {
			// Esto se ejecutará periódicamente en el EDT (Event Dispatch Thread)
			loadContacts();
			// Actualizar el estado del botón premium
			if (Controlador.INSTANCE.isUsuarioPremium()) {
				premiumButton.setText("Premium Activo");
				premiumButton.setForeground(new Color(0, 102, 204)); // Azul
			} else {
				premiumButton.setText("Activar Premium");
				premiumButton.setForeground(Color.RED); // Rojo
			}
		});
		timer.setRepeats(true); // Para que se repita indefinidamente
		timer.start(); // Iniciar el timer
	}

	/**
	 * Inicializa todos los componentes de la interfaz gráfica de la ventana principal.
	 * Configura el marco, los paneles, los botones y los listeners.
	 */
	private void initialize() {
		// Crear el marco principal
		frame = new JFrame("AppChat");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cerrar la aplicación al cerrar la ventana
		frame.setSize(1300, 700);
		frame.setMinimumSize(new Dimension(1300, 700)); // Tamaño mínimo
		frame.getContentPane().setLayout(new BorderLayout()); // Layout principal

		// Panel superior con barra de herramientas
		JPanel topPanel = createTopPanel();
		// Crear línea separadora
		JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
		separator.setForeground(new Color(180, 180, 180)); // Color gris claro

		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.add(topPanel, BorderLayout.CENTER);
		northPanel.add(separator, BorderLayout.SOUTH);

		frame.getContentPane().add(northPanel, BorderLayout.NORTH);
		//frame.getContentPane().add(topPanel, BorderLayout.NORTH); // Esta línea parece duplicada

		// Panel principal (izquierda + derecha)
		JPanel mainPanel = new JPanel(new BorderLayout());

		// Panel izquierdo (lista de contactos)
		leftPanel = createContactsPanel();
		JScrollPane leftScrollPane = new JScrollPane(leftPanel);
		leftScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		// ajustar el tamaño de los contactos, para que no aparezca el scroll horizontal
		leftScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Desactivar scroll horizontal
		leftScrollPane.setPreferredSize(new Dimension(250, 0)); // Ancho preferido

		// Panel derecho (chat)
		rightPanel = createChatPanel();

		mainPanel.add(leftScrollPane, BorderLayout.WEST); // Panel izquierdo a la izquierda
		mainPanel.add(rightPanel, BorderLayout.CENTER); // Panel derecho en el centro

		frame.getContentPane().add(mainPanel, BorderLayout.CENTER); // Añadir panel principal al centro del frame

		// Cargar datos iniciales (ya se hace en el constructor)
		// loadContacts();
	}

	/**
	 * Crea el panel superior que contiene el logo, título, botones de menú y la información del usuario actual.
	 * @return El JPanel configurado para la barra superior.
	 */
	private JPanel createTopPanel() {
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margen interior

		// Panel izquierdo con logo y título
		JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); // Alinear a la izquierda

		// Cargar logo
		try {
			ImageIcon logoIcon = new ImageIcon("resources/logo3.png"); // Ruta del logo
			Image logoImage = logoIcon.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH); // Escalar imagen
			JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
			leftPanel.add(logoLabel);
		} catch (Exception e) {
			System.err.println("Error al cargar el logo: " + e.getMessage());
			// Opcional: añadir un texto alternativo si la imagen no carga
			JLabel logoLabel = new JLabel("AppChat");
			logoLabel.setFont(new Font("Arial", Font.BOLD, 18));
			leftPanel.add(logoLabel);
		}

		// Añadir título "CHATS"
		JLabel chatsLabel = new JLabel("CHATS");
		chatsLabel.setFont(new Font("Arial", Font.BOLD, 18));
		chatsLabel.setForeground(new Color(0, 102, 204)); // Color azul
		leftPanel.add(chatsLabel);

		// Panel central con botones de menú
		JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0)); // Centrar botones

		// Botón de buscar mensajes
		JButton searchMessagesButton = new JButton("Buscar mensajes");
		searchMessagesButton.addActionListener(e -> {
			// Acción: abrir ventana de búsqueda
			Buscador.getInstance().cargarContactosExternamente();
			Buscador.getInstance().mostrarVentana();
		});
		centerPanel.add(searchMessagesButton);

		// Botón de contactos
		JButton contactsButton = new JButton("Contactos");
		contactsButton.addActionListener(e -> {
			// Acción: abrir ventana de contactos
			VentanaContactos.getInstance().cargarContactosExternamente();
			VentanaContactos.getInstance().mostrarVentana();
		});
		centerPanel.add(contactsButton);

		// Botón premium
		premiumButton = new JButton("Premium");
		// Configurar texto y color inicial según el estado premium
		if (Controlador.INSTANCE.isUsuarioPremium()) {
			premiumButton.setText("Premium Activo");
			premiumButton.setForeground(new Color(0, 102, 204)); // Azul
		} else {
			premiumButton.setText("Activar Premium");
			premiumButton.setForeground(Color.RED); // Rojo
		}
		premiumButton.addActionListener(e -> {
			// Acción: abrir ventana premium (activo o para activar)
			if (Controlador.INSTANCE.isUsuarioPremium()) {
				VentanaPremiumActivo.getInstance().cargarContactosExternamente();
				VentanaPremiumActivo.getInstance().mostrarVentana();
			} else {
				VentanaPremium.getInstance().mostrarVentana();
			}
		});
		centerPanel.add(premiumButton);

		// Panel de usuario actual (con borde)
		JPanel userPanel = createUserPanel();
		userPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), // Borde de línea
				BorderFactory.createEmptyBorder(5, 10, 5, 10))); // Margen interior

		// Añadir componentes al panel superior
		topPanel.add(leftPanel, BorderLayout.WEST); // Logo y título a la izquierda
		topPanel.add(centerPanel, BorderLayout.CENTER); // Botones en el centro
		topPanel.add(userPanel, BorderLayout.EAST); // Información de usuario a la derecha

		return topPanel;
	}

	/**
	 * Crea el panel que muestra la información del usuario actual (nombre, imagen de perfil y botones de acción).
	 * @return El JPanel configurado para la información del usuario.
	 */
	private JPanel createUserPanel() {
		JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); // Alinear a la derecha

		// Botón de cerrar sesión
		JButton logoutButton = new JButton("Cerrar sesión");
		logoutButton.setFont(new Font("Arial", Font.PLAIN, 12));
		logoutButton.addActionListener(e -> {
			// Acción: mostrar diálogo de confirmación y cerrar sesión si se confirma
			int confirm = JOptionPane.showConfirmDialog(frame, "¿Estás seguro de que quieres cerrar sesión?",
					"Confirmar cierre de sesión", JOptionPane.YES_NO_OPTION);

			if (confirm == JOptionPane.YES_OPTION) {
				frame.dispose(); // Cerrar la ventana principal
				new Login().mostrarVentana(); // Abrir la ventana de login
			}
		});
		userPanel.add(logoutButton);

		// Botón de configuración (icono o texto)
		try {
			ImageIcon settingIcon = new ImageIcon("resources/settings.png"); // Ruta del icono
			Image settingImage = settingIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH); // Escalar
			JLabel settingLabel = new JLabel(new ImageIcon(settingImage));
			JButton settingsButton = new JButton(settingLabel.getIcon()); // Botón con icono

			settingsButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Margen
			settingsButton.setContentAreaFilled(false); // Fondo transparente
			settingsButton.addActionListener(e -> {
				// Acción: abrir ventana para modificar usuario
				new ModificarUsuario(frame).mostrarVentana();
			});
			userPanel.add(settingsButton);
		} catch (Exception e) {
			// Si falla la carga de la imagen, usar el texto como fallback
			JButton settingsButton = new JButton("⚙"); // Carácter unicode para engranaje
			settingsButton.setFont(new Font("Arial", Font.PLAIN, 14));
			settingsButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			settingsButton.setContentAreaFilled(false);
			settingsButton.addActionListener(pe -> {
				new ModificarUsuario(frame).mostrarVentana();
			});
			userPanel.add(settingsButton);
		}

		String nombreUsuario = Controlador.INSTANCE.getNombreUsuario(); // Obtener nombre del controlador

		JLabel userNameLabel = new JLabel(nombreUsuario); // Etiqueta con el nombre de usuario
		userNameLabel.setFont(new Font("Arial", Font.BOLD, 12));

		// Cargar imagen de perfil y convertirla en botón
		ImageIcon icono = null;
		String URLimagenUsuario = Controlador.INSTANCE.getURLImagenUsuario(); // Obtener URL del controlador
		Image imgEscalada = getImagenContactoEscalada(URLimagenUsuario, icono); // Escalar imagen

		// Crear botón con la imagen de perfil
		JButton profileButton = new JButton(new ImageIcon(imgEscalada));
		profileButton.setBorder(BorderFactory.createEmptyBorder()); // Sin borde
		profileButton.setContentAreaFilled(false); // Fondo transparente
		profileButton.addActionListener(e -> {
			// Acción: abrir la pantalla de información del usuario actual
			InfoUsuario.mostrarInfoUsuarioActual();
		});

		userPanel.add(userNameLabel); // Añadir nombre
		userPanel.add(profileButton); // Añadimos el botón en lugar del JLabel

		return userPanel;
	}

	/**
	 * Crea el panel izquierdo que contiene la lista de contactos.
	 * @return El JPanel configurado para la lista de contactos.
	 */
	private JPanel createContactsPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Layout vertical
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Margen interior

		// Añadir un "pegamento" para evitar que los contactos se expandan y se peguen arriba
		panel.add(Box.createVerticalGlue());
		return panel;
	}

	/**
	 * Crea el panel derecho que contiene el área de chat y el campo de mensaje.
	 * @return El JPanel configurado para el área de chat.
	 */
	private JPanel createChatPanel() {
		JPanel panel = new JPanel(new BorderLayout()); // Layout principal del chat

		// Panel superior con información del contacto seleccionado
		contactInfoPanel = new JPanel(new BorderLayout());
		contactInfoPanel.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY), // Borde inferior
						BorderFactory.createEmptyBorder(10, 15, 10, 15))); // Margen interior

		currentContactLabel = new JLabel("Selecciona un contacto"); // Etiqueta inicial
		currentContactLabel.setFont(new Font("Arial", Font.BOLD, 16));

		currentContactImage = new JLabel(); // Etiqueta para la imagen del contacto
		currentContactImage.setPreferredSize(new Dimension(40, 40)); // Tamaño fijo para la imagen
		currentContactImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Acción: abrir información del contacto seleccionado al hacer clic en la imagen
				if (contactoSeleccionado != null) {
					InfoUsuario.mostrarInfoContacto(contactoSeleccionado);
				}
			}
		});

		JPanel contactPanel = new JPanel(new BorderLayout(10, 0)); // Panel para el nombre y la imagen
		contactPanel.add(currentContactLabel, BorderLayout.CENTER);
		contactPanel.add(currentContactImage, BorderLayout.EAST);

		contactInfoPanel.add(contactPanel, BorderLayout.NORTH);
		panel.add(contactInfoPanel, BorderLayout.NORTH); // Añadir panel de info al norte del panel de chat

		// Área de chat con ChatPanel
		chatPanel = new ChatPanel(); // Instancia de ChatPanel
		chatScrollPane = new JScrollPane(chatPanel); // Scroll para el chat
		chatScrollPane.setBorder(null); // Sin borde
		panel.add(chatScrollPane, BorderLayout.CENTER); // Área de chat en el centro

		// Panel inferior para escribir y enviar mensajes
		JPanel messagePanel = new JPanel(new BorderLayout(5, 5));
		messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margen interior


		// Panel para botón de emoticonos y campo de texto
		JPanel inputPanel = new JPanel(new BorderLayout(5, 0));

		emojiButton = new JButton("😊"); // Botón de emoji
		emojiButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16)); // Fuente para emojis
		emojiButton.setPreferredSize(new Dimension(40, 30)); // Tamaño preferido
		emojiButton.addActionListener(this::mostrarSelectorEmojis); // Listener para mostrar selector

		inputPanel.add(emojiButton, BorderLayout.WEST); // Botón de emoji a la izquierda

		messageField = new JTextField(); // Campo de texto para mensajes
		messageField.setToolTipText("Escribe tu mensaje aquí"); // Tooltip
		messageField.addActionListener(this::enviarMensaje); // Enviar al presionar Enter
		inputPanel.add(messageField, BorderLayout.CENTER); // Campo de texto en el centro
		// inputPanel.add(messageField, BorderLayout.CENTER); // Esta línea parece duplicada

		JButton sendButton = new JButton("Enviar"); // Botón de enviar
		sendButton.setPreferredSize(new Dimension(80, 0)); // Ancho preferido
		sendButton.addActionListener(this::enviarMensaje); // Listener para enviar mensaje

		messagePanel.add(inputPanel, BorderLayout.CENTER); // Panel de entrada en el centro del panel de mensaje
		messagePanel.add(sendButton, BorderLayout.EAST); // Botón enviar a la derecha
		panel.add(messagePanel, BorderLayout.SOUTH); // Panel de mensaje al sur del panel de chat

		// Mensaje inicial
		mostrarMensajeInicial();

		return panel;
	}

	/**
	 * Muestra un mensaje inicial en el área de chat indicando que se seleccione un contacto.
	 */
	private void mostrarMensajeInicial() {
		chatPanel.removeAll(); // Limpiar chat actual
		JLabel initLabel = new JLabel("Selecciona un contacto para comenzar a chatear"); // Mensaje
		initLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centrar texto
		initLabel.setForeground(Color.GRAY); // Color gris
		chatPanel.add(initLabel); // Añadir al panel de chat
		chatPanel.revalidate(); // Revalidar layout
		chatPanel.repaint(); // Repintar
	}

	/**
	 * Selecciona un contacto para iniciar o continuar una conversación.
	 * Actualiza la interfaz para mostrar la información del contacto y cargar sus mensajes.
	 * @param contacto El contacto seleccionado.
	 */
	private void seleccionarContacto(Contacto contacto) {
		this.contactoSeleccionado = contacto; // Establecer el contacto seleccionado

		// Actualizar panel de información del contacto
		currentContactLabel.setText(contacto.getNombre()); // Mostrar nombre
		ImageIcon icono = null;
		String URLimagenContacto = Controlador.INSTANCE.getURLImagenContacto(contacto); // Obtener URL de imagen
		Image imgEscalada = getImagenContactoEscalada(URLimagenContacto, icono); // Escalar imagen
		currentContactImage.setIcon(new ImageIcon(imgEscalada)); // Mostrar imagen

		// Cargar mensajes del chat para el contacto seleccionado
		cargarMensajes(contacto);
	}

	/**
	 * Carga y muestra los mensajes de la conversación con el contacto especificado en el área de chat.
	 * @param contacto El contacto cuyos mensajes se van a cargar.
	 */
	private void cargarMensajes(Contacto contacto) {
		chatPanel.mostrarChat(contacto); // Llamar al método del ChatPanel
	}

	/**
	 * Maneja el evento de enviar un mensaje (ya sea por botón o por Enter en el campo de texto).
	 * Envía el mensaje si hay un contacto seleccionado y el campo de texto no está vacío.
	 * @param e El evento de acción.
	 */
	private void enviarMensaje(ActionEvent e) {
		// Verificar si hay un contacto seleccionado
		if (contactoSeleccionado == null) {
			JOptionPane.showMessageDialog(frame,
					"Selecciona un contacto primero",
					"Error", JOptionPane.WARNING_MESSAGE);
			return; // Salir si no hay contacto seleccionado
		}

		String contenido = messageField.getText().trim(); // Obtener texto y eliminar espacios
		if (contenido.isEmpty()) {
			return; // No enviar si el mensaje está vacío
		}

		// Enviar el mensaje a través del controlador
		Controlador.INSTANCE.enviarMensaje(contactoSeleccionado.getId(), contenido);
		// Actualizar la interfaz de chat para mostrar el mensaje enviado
		chatPanel.enviarMensaje(contenido);
		messageField.setText(""); // Limpiar el campo de texto
	}

	/**
	 * Muestra una pequeña ventana para seleccionar un emoji para enviar.
	 * @param e El evento de acción.
	 */
	private void mostrarSelectorEmojis(ActionEvent e) {
		// Verificar si hay un contacto seleccionado
		if (contactoSeleccionado == null) {
			JOptionPane.showMessageDialog(frame, "Selecciona un contacto primero", "Error",
					JOptionPane.WARNING_MESSAGE);
			return; // Salir si no hay contacto seleccionado
		}

		JFrame emojiFrame = new JFrame("Seleccionar Emoji"); // Nuevo frame para el selector
		emojiFrame.setSize(400, 300);
		emojiFrame.setLayout(new BorderLayout());

		JPanel emojiPanel = new JPanel(new GridBagLayout()); // Panel para los botones de emoji
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5); // Espacio entre botones

		// Mostrar los primeros 12 emojis como ejemplo (se podría expandir)
		for (int i = 0; i < 12; i++) {
			final int emojiId = i; // Crear una variable final para usar en el lambda
			JButton emojiBtn = new JButton();
			emojiBtn.setIcon(BubbleText.getEmoji(emojiId)); // Obtener icono del emoji
			emojiBtn.addActionListener(ev -> {
				enviarEmoji(emojiId); // Enviar el emoji seleccionado
				emojiFrame.dispose(); // Cerrar la ventana del selector
			});

			gbc.gridx = emojiId % 4; // Posición en la cuadrícula (columna)
			gbc.gridy = emojiId / 4; // Posición en la cuadrícula (fila)
			emojiPanel.add(emojiBtn, gbc); // Añadir botón al panel
		}

		emojiFrame.add(new JScrollPane(emojiPanel), BorderLayout.CENTER); // Añadir scroll al panel de emojis
		emojiFrame.setLocationRelativeTo(frame); // Posicionar relativo a la ventana principal
		emojiFrame.setVisible(true); // Hacer visible el selector
	}

	/**
	 * Envía un emoji al contacto seleccionado.
	 * @param emojiId El ID del emoji a enviar.
	 */
	private void enviarEmoji(int emojiId) {
		chatPanel.enviarEmoji(emojiId); // Mostrar el emoji en la interfaz de chat
		Controlador.INSTANCE.enviarEmoji(contactoSeleccionado.getId(), emojiId); // Enviar a través del controlador
	}

	/**
	 * Carga la lista de contactos del usuario actual desde el controlador
	 * y actualiza el panel izquierdo para mostrarlos.
	 */
	private void loadContacts() {
		contactos = Controlador.INSTANCE.getContactosUsuario(); // Obtener contactos del controlador
		leftPanel.removeAll(); // Limpiar el panel actual

		if (contactos.isEmpty()) {
			// Mostrar mensaje si no hay contactos
			JLabel emptyLabel = new JLabel("No tienes contactos");
			emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
			leftPanel.add(emptyLabel);
		} else {
			// Crear y añadir un panel para cada contacto
			for (Contacto contacto : contactos) {
				JPanel contactPanel = createContactPanel(contacto);
				leftPanel.add(contactPanel);
			}
		}

		// Añadir pegamento al final para empujar los contactos hacia arriba
		leftPanel.add(Box.createVerticalGlue());


		leftPanel.revalidate(); // Revalidar layout
		leftPanel.repaint(); // Repintar
	}

	/**
	 * Crea un JPanel que representa un contacto individual en la lista de contactos.
	 * Incluye la imagen, nombre, último mensaje y un botón de edición.
	 * @param contacto El objeto Contacto a representar.
	 * @return Un JPanel configurado para mostrar el contacto.
	 */
	private JPanel createContactPanel(Contacto contacto) {
		JPanel panel = new JPanel(new BorderLayout(10, 0)); // Layout para el panel del contacto
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)), // Borde inferior ligero
				BorderFactory.createEmptyBorder(10, 10, 10, 10))); // Margen interior
		panel.setBackground(Color.WHITE); // Fondo blanco

		// Establecer tamaño fijo
		panel.setPreferredSize(new Dimension(250, 60)); // Ancho 250px, Alto 60px
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); // Altura fija, ancho flexible

		// Configurar el hover effect y el listener de clic
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				panel.setBackground(new Color(245, 245, 245)); // Cambiar color al pasar el ratón
			}

			@Override
			public void mouseExited(MouseEvent e) {
				panel.setBackground(Color.WHITE); // Restaurar color al salir del ratón
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				seleccionarContacto(contacto); // Seleccionar este contacto al hacer clic
			}
		});

		// Imagen del contacto
		JLabel imageLabel = new JLabel();
		ImageIcon icono = null;
		String URLimagenContacto = Controlador.INSTANCE.getURLImagenContacto(contacto); // Obtener URL de imagen
		Image imgEscalada = getImagenContactoEscalada(URLimagenContacto, icono); // Escalar imagen
		imageLabel.setIcon(new ImageIcon(imgEscalada)); // Mostrar imagen

		// Nombre del contacto
		JLabel nameLabel = new JLabel(contacto.getNombre());
		nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));

		// Último mensaje (obtenido del controlador)
		JLabel lastMsgLabel = new JLabel(Controlador.INSTANCE.getUltimoMensaje(contacto));
		lastMsgLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		lastMsgLabel.setForeground(Color.GRAY); // Color gris para el último mensaje

		// Panel para el nombre y el último mensaje (layout vertical)
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		textPanel.setOpaque(false); // Fondo transparente
		textPanel.add(nameLabel);
		textPanel.add(lastMsgLabel);

		// Botón de edición (texto "Editar" o icono de tres puntos)
		JButton editButton = new JButton("Editar"); // Texto del botón
		editButton.setFont(new Font("Arial", Font.PLAIN, 14));
		editButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		editButton.setContentAreaFilled(false);
		editButton.setFocusPainted(false);
		editButton.setOpaque(false);
		editButton.addActionListener(e -> {
			// Acción: abrir ventana para modificar este contacto
			if (Controlador.INSTANCE.isContactoIndividual(contacto)) {
				new ModificarContacto(contacto).mostrarVentana();
			} else {
				new ModificarGrupo(contacto).mostrarVentana();
			}
			
		});

		// Panel para el botón de edición
		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.setOpaque(false);
		buttonPanel.add(editButton, BorderLayout.CENTER);

		// Añadir componentes al panel del contacto
		panel.add(imageLabel, BorderLayout.WEST); // Imagen a la izquierda
		panel.add(textPanel, BorderLayout.CENTER); // Texto en el centro
		panel.add(buttonPanel, BorderLayout.EAST); // Botón de edición a la derecha

		return panel;
	}

	/**
	 * Carga una imagen desde una URL o una ruta local, la escala a un tamaño fijo y la devuelve.
	 * Si la URL es nula o vacía, o si hay un error, carga una imagen de placeholder.
	 * @param URLimagenContacto La URL o ruta local de la imagen del contacto.
	 * @param icono Un ImageIcon (puede ser nulo, se usará para crear uno nuevo).
	 * @return La imagen escalada.
	 */
	private Image getImagenContactoEscalada(String URLimagenContacto, ImageIcon icono) {
		try {
			if (URLimagenContacto != null && !URLimagenContacto.isEmpty()) {
				// Intentar cargar desde URL
				URL url = new URL(URLimagenContacto);
				icono = new ImageIcon(url);
			} else {
				// Cargar imagen por defecto si no hay URL
				icono = new ImageIcon("phphotos/pfp.jpg"); // Ruta de la imagen por defecto
			}
			// Escalar la imagen
			Image imgEscalada = icono.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);

			return imgEscalada;
		} catch (Exception e) {
			System.err.println("Error al cargar la imagen del contacto: " + e.getMessage());
			// Devolver una imagen de placeholder o null en caso de error
			// Aquí se podría devolver un ImageIcon con un icono genérico si se prefiere
			return new ImageIcon("phphotos/pfp.jpg").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); // Fallback a imagen por defecto
		}
	}
}
