package gui;

import dominio.Contacto;
import controlador.Controlador;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.LinkedList;

/**
 * Ventana que muestra los beneficios de la suscripción Premium,
 * permitiendo al usuario exportar chats a PDF y anular la suscripción.
 * Implementa el patrón Singleton para asegurar una única instancia.
 */
public class VentanaPremiumActivo {

	/** La única instancia de la clase VentanaPremiumActivo (Singleton). */
	private static VentanaPremiumActivo instance;
	/** El marco principal de la ventana. */
	private JFrame frame;
	/** ComboBox para seleccionar el contacto cuyo chat se desea exportar. */
	private JComboBox<String> contactosComboBox;
	/** Botón para iniciar el proceso de exportación a PDF. */
	private JButton exportarButton;
	/** Botón para anular la suscripción Premium. */
	private JButton cancelarSuscripcionButton;

	/**
	 * Constructor privado para implementar el patrón Singleton.
	 * Inicializa los componentes de la interfaz gráfica y carga la lista de contactos.
	 */
	private VentanaPremiumActivo() {
		initialize();
		cargarContactos();
	}

	/**
	 * Devuelve la única instancia de la clase VentanaPremiumActivo.
	 * Si la instancia no existe, la crea.
	 * @return La instancia única de VentanaPremiumActivo.
	 */
	public static VentanaPremiumActivo getInstance() {
		if (instance == null) {
			instance = new VentanaPremiumActivo();
		}
		return instance;
	}

	/**
	 * Hace visible la ventana. Si la ventana ya está abierta, la trae al frente.
	 */
	public void mostrarVentana() {
		if (frame != null && frame.isVisible()) {
			frame.toFront(); // Traer al frente si ya está abierta
			return;
		}
		frame.setLocationRelativeTo(null); // Centrar la ventana
		frame.setVisible(true);
	}

	/**
	 * Inicializa los componentes de la interfaz gráfica de la ventana.
	 * Configura el layout, paneles, ComboBox, botones y añade los listeners.
	 */
	private void initialize() {
		// Configuración básica de la ventana
		frame = new JFrame("Beneficios Premium");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerrar solo esta ventana
		frame.setSize(500, 300);
		frame.setMinimumSize(new Dimension(450, 250));
		frame.setLayout(new BorderLayout());
		frame.getContentPane().setBackground(new Color(240, 240, 240)); // Color de fondo

		// Panel principal con bordes
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margen interior
		mainPanel.setBackground(new Color(255, 255, 255)); // Fondo blanco

		// Panel de contenido
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Layout vertical
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margen interior

		// Título
		JLabel titleLabel = new JLabel("Exportar chats en formato PDF");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
		titleLabel.setForeground(new Color(0, 102, 204)); // Color azul
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrar horizontalmente
		titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); // Espacio inferior

		// Panel de selección de contacto
		JPanel contactPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Layout centrado
		contactPanel.setBackground(Color.WHITE);

		JLabel contactLabel = new JLabel("Selecciona un contacto:");
		contactLabel.setFont(new Font("Arial", Font.PLAIN, 14));

		contactosComboBox = new JComboBox<>();
		contactosComboBox.setPreferredSize(new Dimension(200, 30)); // Tamaño preferido
		// Habilitar el botón de exportar solo si hay un elemento seleccionado
		contactosComboBox.addActionListener(e -> {
			exportarButton.setEnabled(contactosComboBox.getSelectedItem() != null);
		});

		contactPanel.add(contactLabel);
		contactPanel.add(contactosComboBox);

		// Panel de botones
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Layout centrado con espacio
		buttonPanel.setBackground(Color.WHITE);

		exportarButton = new JButton("Exportar PDF");
		exportarButton.setFont(new Font("Arial", Font.BOLD, 14));
		exportarButton.setForeground(Color.WHITE);
		exportarButton.setBackground(new Color(0, 153, 51)); // Color verde
		exportarButton.setFocusPainted(false); // Quitar el borde de foco
		exportarButton.setPreferredSize(new Dimension(150, 35)); // Tamaño preferido
		exportarButton.setEnabled(false); // Deshabilitado inicialmente
		exportarButton.addActionListener(this::exportarPDF); // Listener para exportar

		cancelarSuscripcionButton = new JButton("Anular Suscripción");
		cancelarSuscripcionButton.setFont(new Font("Arial", Font.PLAIN, 14));
		cancelarSuscripcionButton.setForeground(new Color(70, 70, 70)); // Color gris oscuro
		cancelarSuscripcionButton.setBackground(new Color(240, 240, 240)); // Color gris claro
		cancelarSuscripcionButton.setFocusPainted(false); // Quitar el borde de foco
		cancelarSuscripcionButton.setPreferredSize(new Dimension(150, 35)); // Tamaño preferido
		cancelarSuscripcionButton.addActionListener(this::anularSuscripcion); // Listener para anular suscripción

		buttonPanel.add(exportarButton);
		buttonPanel.add(cancelarSuscripcionButton);

		// Añadir componentes al panel de contenido
		contentPanel.add(titleLabel);
		contentPanel.add(contactPanel);
		contentPanel.add(Box.createVerticalStrut(20)); // Espacio vertical
		contentPanel.add(buttonPanel);

		// Añadir panel principal a la ventana
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		frame.add(mainPanel, BorderLayout.CENTER);
	}

	/**
	 * Carga la lista de contactos del usuario desde el controlador
	 * y los añade al ComboBox para su selección.
	 */
	private void cargarContactos() {
		LinkedList<Contacto> contactos = Controlador.INSTANCE.getContactosUsuario(); // Obtener contactos
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(); // Modelo para el ComboBox

		if (contactos != null && !contactos.isEmpty()) {
			// Añadir cada contacto al modelo del ComboBox, incluyendo nombre y teléfono/indicador de grupo
			for (Contacto contacto : contactos) {
				// Se asume que getTelefono devuelve el número o "Grupo" para grupos
				model.addElement(contacto.getNombre() + ":" + Controlador.INSTANCE.getTelefono(contacto));
			}
		} else {
			// Mostrar mensaje si no hay contactos disponibles
			JOptionPane.showMessageDialog(frame,
					"No tienes contactos disponibles",
					"Información", JOptionPane.INFORMATION_MESSAGE);
		}

		contactosComboBox.setModel(model); // Establecer el modelo en el ComboBox
		// Habilitar/deshabilitar el botón de exportar al cargar los contactos
		exportarButton.setEnabled(contactosComboBox.getSelectedItem() != null);
	}

	/**
	 * Maneja el evento del botón Exportar PDF.
	 * Obtiene el contacto seleccionado, abre un diálogo para guardar el archivo
	 * y llama al controlador para exportar el chat a PDF.
	 * @param e El evento de acción.
	 */
	private void exportarPDF(ActionEvent e) {
		// Obtener el elemento seleccionado del ComboBox y parsear nombre y teléfono/tipo
		String selectedItem = contactosComboBox.getSelectedItem().toString();
		String[] parts = selectedItem.split(":");
		String nombre = parts[0];
		String identificador = parts[1]; // Puede ser teléfono o "Grupo"

		Contacto contactoSeleccionado = null;

		// Buscar el contacto o grupo correspondiente usando el controlador
		if (!identificador.equals("Grupo")) {
			contactoSeleccionado = Controlador.INSTANCE.getContactoConMovil(identificador);
		} else {
			contactoSeleccionado = Controlador.INSTANCE.getGrupoConNombre(nombre);
		}

		if (contactoSeleccionado != null) {
			// Crear el diálogo para seleccionar la ubicación y nombre del archivo
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Guardar chat como PDF");
			// Nombre de archivo por defecto
			fileChooser.setSelectedFile(new File(contactoSeleccionado.getNombre() + "_chat.pdf"));
			// Filtro para mostrar solo archivos PDF
			fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos PDF (*.pdf)", "pdf"));

			// Mostrar el diálogo de guardar archivo
			int userSelection = fileChooser.showSaveDialog(frame);

			// Procesar la selección del usuario
			if (userSelection == JFileChooser.APPROVE_OPTION) {
				File fileToSave = fileChooser.getSelectedFile();
				// Asegurarse de que tenga la extensión .pdf
				String filePath = fileToSave.getAbsolutePath();
				if (!filePath.toLowerCase().endsWith(".pdf")) {
					filePath += ".pdf";
					fileToSave = new File(filePath);
				}

				// Verificar si el archivo ya existe y pedir confirmación para sobrescribir
				if (fileToSave.exists()) {
					int overwrite = JOptionPane.showConfirmDialog(frame,
							"El archivo ya existe. ¿Desea sobrescribirlo?",
							"Archivo existente",
							JOptionPane.YES_NO_OPTION);

					if (overwrite != JOptionPane.YES_OPTION) {
						return; // Cancelar la exportación si el usuario no quiere sobrescribir
					}
				}

				System.out.println("Exportando chat de " + contactoSeleccionado.getNombre() + " a: " + filePath);
				// Llamar al controlador para realizar la exportación
				boolean exito = Controlador.INSTANCE.exportarChatPDF(contactoSeleccionado, filePath);

				// Mostrar mensaje de éxito o error
				if (exito) {
					JOptionPane.showMessageDialog(frame,
							"Chat exportado correctamente como PDF\n" + filePath,
							"Exportación exitosa",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(frame,
							"Error al exportar el chat a PDF",
							"Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} else {
			// Esto no debería ocurrir si cargarContactos funciona correctamente, pero es una seguridad
			JOptionPane.showMessageDialog(frame,
					"No se pudo encontrar el contacto seleccionado.",
					"Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Maneja el evento del botón Anular Suscripción.
	 * Muestra un diálogo de confirmación y llama al controlador para anular la suscripción Premium.
	 * @param e El evento de acción.
	 */
	private void anularSuscripcion(ActionEvent e) {
		// Mostrar diálogo de confirmación para anular la suscripción
		int confirmacion = JOptionPane.showConfirmDialog(frame,
				"¿Estás seguro de que quieres anular tu suscripción Premium?\n" +
				"Perderás acceso a todas las funciones exclusivas.",
				"Confirmar anulación",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE); // Icono de advertencia

		// Si el usuario confirma
		if (confirmacion == JOptionPane.YES_OPTION) {
			// Llamar al controlador para anular la suscripción
			boolean exito = Controlador.INSTANCE.anularPremium();

			// Mostrar mensaje de éxito o error
			if (exito) {
				JOptionPane.showMessageDialog(frame,
						"Suscripción Premium anulada correctamente",
						"Anulación exitosa",
						JOptionPane.INFORMATION_MESSAGE);
				frame.dispose(); // Cerrar la ventana después de la anulación exitosa
			} else {
				JOptionPane.showMessageDialog(frame,
						"Error al anular la suscripción Premium",
						"Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		// Si el usuario selecciona NO, el diálogo simplemente se cierra sin hacer nada.
	}
}
