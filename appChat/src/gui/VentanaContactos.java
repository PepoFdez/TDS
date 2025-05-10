package gui;

import dominio.ContactoIndividual;
import controlador.Controlador;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.LinkedList;

/**
 * Ventana principal para la gestión de contactos individuales y la creación de grupos.
 * Permite visualizar los contactos existentes, seleccionar miembros para un grupo
 * y crear un nuevo grupo con los miembros seleccionados.
 * Implementa el patrón Singleton para asegurar una única instancia de la ventana.
 */
public class VentanaContactos {

	/** La única instancia de la clase VentanaContactos (Singleton). */
	private static VentanaContactos instance;
	/** El marco principal de la ventana. */
	private JFrame frame;
	/** Lista visual que muestra los contactos individuales disponibles. */
	private JList<ContactoIndividual> listaContactos;
	/** Lista visual que muestra los contactos seleccionados para el grupo. */
	private JList<ContactoIndividual> listaGrupo;
	/** Modelo de datos para la lista de contactos disponibles. */
	private DefaultListModel<ContactoIndividual> modeloContactos;
	/** Modelo de datos para la lista de miembros del grupo. */
	private DefaultListModel<ContactoIndividual> modeloGrupo;
	/** Botón para abrir la ventana de añadir un nuevo contacto. */
	private JButton btnAñadirContacto, /** Botón para crear un nuevo grupo con los miembros seleccionados. */btnAñadirGrupo, /** Botón para mover un contacto de la lista de contactos a la lista del grupo. */btnMoverDerecha, /** Botón para mover un contacto de la lista del grupo a la lista de contactos. */btnMoverIzquierda;

	/**
	 * Constructor privado para implementar el patrón Singleton.
	 * Inicializa los componentes de la interfaz gráfica y carga los contactos existentes.
	 */
	private VentanaContactos() {
		initialize();
		cargarContactos();
	}

	/**
	 * Devuelve la única instancia de la clase VentanaContactos.
	 * Si la instancia no existe, la crea.
	 * @return La instancia única de VentanaContactos.
	 */
	public static VentanaContactos getInstance() {
		if (instance == null) {
			instance = new VentanaContactos();
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
	 * Configura el layout, los paneles, las listas, los botones y añade los listeners.
	 */
	private void initialize() {
		frame = new JFrame("Gestión de Contactos y Grupos");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerrar solo esta ventana
		frame.setSize(650, 450);
		frame.setMinimumSize(new Dimension(500, 350));
		frame.setLayout(new BorderLayout(10, 10));
		frame.getContentPane().setBackground(new Color(240, 240, 240));

		// Panel izquierdo: Lista de contactos
		JPanel panelIzquierdo = new JPanel(new BorderLayout(5, 5));
		panelIzquierdo.setBorder(BorderFactory.createCompoundBorder(
				new TitledBorder("Lista de Contactos"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		modeloContactos = new DefaultListModel<>();
		listaContactos = new JList<>(modeloContactos);
		listaContactos.setCellRenderer(new ContactoListCellRenderer()); // Usar renderer personalizado
		listaContactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Permitir solo una selección

		panelIzquierdo.add(new JScrollPane(listaContactos), BorderLayout.CENTER);

		btnAñadirContacto = new JButton("Añadir Contacto");
		// Intenta usar un icono por defecto de Swing. Si falla, no se asigna icono.
		try {
			btnAñadirContacto.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
		} catch (Exception e) {
			// Si falla, simplemente no ponemos icono
		}
		panelIzquierdo.add(createButtonPanel(btnAñadirContacto), BorderLayout.SOUTH);

		// Panel derecho: Lista de grupo
		JPanel panelDerecho = new JPanel(new BorderLayout(5, 5));
		panelDerecho.setBorder(BorderFactory.createCompoundBorder(
				new TitledBorder("Miembros del Grupo"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		modeloGrupo = new DefaultListModel<>();
		listaGrupo = new JList<>(modeloGrupo);
		listaGrupo.setCellRenderer(new ContactoListCellRenderer()); // Usar renderer personalizado
		listaGrupo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Permitir solo una selección

		panelDerecho.add(new JScrollPane(listaGrupo), BorderLayout.CENTER);

		btnAñadirGrupo = new JButton("Crear Grupo");
		// Intenta usar un icono por defecto de Swing. Si falla, no se asigna icono.
		try {
			btnAñadirGrupo.setIcon(UIManager.getIcon("OptionPane.questionIcon"));
		} catch (Exception e) {
			// Si falla, simplemente no ponemos icono
		}
		panelDerecho.add(createButtonPanel(btnAñadirGrupo), BorderLayout.SOUTH);

		// Panel central: Botones para mover elementos
		JPanel panelCentral = new JPanel(new GridBagLayout());
		panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER; // Cada componente en una nueva fila
		gbc.fill = GridBagConstraints.HORIZONTAL; // Expandir horizontalmente
		gbc.insets = new Insets(5, 0, 5, 0); // Espacio entre componentes

		btnMoverDerecha = new JButton(">");
		btnMoverDerecha.setPreferredSize(new Dimension(50, 30));
		btnMoverDerecha.setToolTipText("Añadir al grupo"); // Tooltip
		panelCentral.add(btnMoverDerecha, gbc);

		btnMoverIzquierda = new JButton("<");
		btnMoverIzquierda.setPreferredSize(new Dimension(50, 30));
		btnMoverIzquierda.setToolTipText("Quitar del grupo"); // Tooltip
		panelCentral.add(btnMoverIzquierda, gbc);

		// Agregar los paneles a la ventana principal
		frame.add(panelIzquierdo, BorderLayout.WEST);
		frame.add(panelCentral, BorderLayout.CENTER);
		frame.add(panelDerecho, BorderLayout.EAST);

		// Eventos de los botones (usando method references)
		btnMoverDerecha.addActionListener(this::moverElementoDerecha);
		btnMoverIzquierda.addActionListener(this::moverElementoIzquierda);
		btnAñadirContacto.addActionListener(this::añadirContacto);
		btnAñadirGrupo.addActionListener(this::añadirGrupo);
	}

	/**
	 * Crea un panel simple centrado para contener un botón.
	 * @param button El botón que se añadirá al panel.
	 * @return Un JPanel que contiene el botón centrado.
	 */
	private JPanel createButtonPanel(JButton button) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.setBackground(null); // Hacer el panel transparente
		panel.add(button);
		return panel;
	}

	/**
	 * Método público para solicitar la recarga de contactos desde fuera de la clase.
	 */
	public void cargarContactosExternamente() {
		cargarContactos();
	}

	/**
	 * Carga los contactos individuales desde el controlador y actualiza el modelo de la lista de contactos disponibles.
	 */
	private void cargarContactos() {
		LinkedList<ContactoIndividual> contactos = Controlador.INSTANCE.getContactosIndividualesUsuario();
		modeloContactos.clear(); // Limpiar el modelo actual
		
		if (contactos != null && !contactos.isEmpty()) {
			contactos.forEach(modeloContactos::addElement); // Añadir todos los contactos al modelo
		}
		// Si contactos es null o vacío, el modelo queda vacío, lo cual es correcto.
	}

	/**
	 * Maneja el evento del botón para mover un contacto seleccionado de la lista de contactos a la lista del grupo.
	 * Muestra un mensaje de advertencia si no hay ningún contacto seleccionado.
	 * @param e El evento de acción.
	 */
	private void moverElementoDerecha(ActionEvent e) {
		ContactoIndividual seleccionado = listaContactos.getSelectedValue();
		if (seleccionado != null) {
			modeloContactos.removeElement(seleccionado); // Quitar de la lista de contactos
			modeloGrupo.addElement(seleccionado); // Añadir a la lista del grupo
		} else {
			JOptionPane.showMessageDialog(frame,
					"Selecciona un contacto primero",
					"Advertencia", JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * Maneja el evento del botón para mover un contacto seleccionado de la lista del grupo a la lista de contactos.
	 * Muestra un mensaje de advertencia si no hay ningún contacto seleccionado en la lista del grupo.
	 * @param e El evento de acción.
	 */
	private void moverElementoIzquierda(ActionEvent e) {
		ContactoIndividual seleccionado = listaGrupo.getSelectedValue();
		if (seleccionado != null) {
			modeloGrupo.removeElement(seleccionado); // Quitar de la lista del grupo
			modeloContactos.addElement(seleccionado); // Añadir a la lista de contactos
		} else {
			JOptionPane.showMessageDialog(frame,
					"Selecciona un contacto del grupo primero",
					"Advertencia", JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * Maneja el evento del botón para añadir un nuevo contacto.
	 * Abre la ventana {@link AñadirContacto}.
	 * @param e El evento de acción.
	 */
	private void añadirContacto(ActionEvent e) {
		AñadirContacto.getInstance().mostrarVentana();
	}

	/**
	 * Maneja el evento del botón para crear un nuevo grupo.
	 * Solicita al usuario un nombre para el grupo y, si se proporciona,
	 * intenta crear el grupo con los contactos actualmente en la lista del grupo.
	 * Muestra mensajes de éxito o error.
	 * @param e El evento de acción.
	 */
	private void añadirGrupo(ActionEvent e) {
		String nombreGrupo = JOptionPane.showInputDialog(
				frame,
				"Introduce el nombre del grupo:",
				"Crear Grupo",
				JOptionPane.PLAIN_MESSAGE);

		// Verificar si el usuario introdujo un nombre válido (no nulo y no vacío/solo espacios)
		if (nombreGrupo != null && !nombreGrupo.trim().isEmpty()) {
			// Verificar si hay miembros en el grupo
			if (modeloGrupo.isEmpty()) {
				JOptionPane.showMessageDialog(frame,
						"Debes añadir al menos un contacto al grupo",
						"Advertencia", JOptionPane.WARNING_MESSAGE);
				return; // Salir del método si no hay miembros
			}

			// Recopilar los miembros del grupo
			LinkedList<ContactoIndividual> miembros = new LinkedList<>();
			for (int i = 0; i < modeloGrupo.size(); i++) {
				miembros.add(modeloGrupo.getElementAt(i));
			}

			// Intentar crear el grupo usando el controlador
			boolean creado = Controlador.INSTANCE.crearGrupo(nombreGrupo.trim(), miembros);
			if (creado) {
				JOptionPane.showMessageDialog(frame,
						"Grupo creado exitosamente",
						"Éxito", JOptionPane.INFORMATION_MESSAGE);
				modeloGrupo.clear(); // Limpiar la lista del grupo después de crear
				cargarContactos(); // Recargar la lista de contactos (puede que haya cambios)
			} else {
				JOptionPane.showMessageDialog(frame,
						"Error al crear el grupo", // Mensaje de error más específico si es posible (ej. nombre duplicado)
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		// Si nombreGrupo es null o vacío, el diálogo simplemente se cierra sin hacer nada.
	}

	/**
	 * Renderer personalizado para mostrar objetos {@link ContactoIndividual} en las listas.
	 * Muestra el nombre del contacto y opcionalmente un icono genérico.
	 */
	private static class ContactoListCellRenderer extends DefaultListCellRenderer {
		/**
		 * Identificador de versión para serialización.
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Devuelve el componente utilizado para renderizar un elemento de la lista.
		 * Muestra el nombre del {@link ContactoIndividual} y un icono.
		 * @param list La JList que pide el renderer.
		 * @param value El valor del elemento a renderizar.
		 * @param index El índice del elemento.
		 * @param isSelected Verdadero si el elemento está seleccionado.
		 * @param cellHasFocus Verdadero si la celda tiene el foco.
		 * @return El componente configurado para renderizar el elemento.
		 */
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index,
														boolean isSelected, boolean cellHasFocus) {
			// Configuración básica del renderer por defecto
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			// Si el valor es una instancia de ContactoIndividual
			if (value instanceof ContactoIndividual) {
				ContactoIndividual contacto = (ContactoIndividual) value;
				setText(contacto.getNombre()); // Mostrar el nombre del contacto
				// Opcional: Añadir un icono genérico si no hay imagen específica
				try {
					setIcon(UIManager.getIcon("OptionPane.informationIcon")); // Intenta usar un icono por defecto
				} catch (Exception e) {
					// No hacer nada si no se puede cargar el icono
				}
			}
			// Si el valor no es un ContactoIndividual, se renderizará usando el comportamiento por defecto.

			return this; // Devolver el propio componente configurado
		}
	}
}