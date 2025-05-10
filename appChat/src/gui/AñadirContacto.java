package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import controlador.Controlador;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Clase que representa una ventana modal para añadir un nuevo contacto.
 * Utiliza el patrón singleton para asegurarse de que solo haya una instancia.
 */
public class AñadirContacto extends JDialog {

	private static final long serialVersionUID = 1L;

	/** Instancia única de la clase */
	private static AñadirContacto instance;

	private JFrame frame;
	private JLabel lblNombre;
	private JTextField txtNombre;
	private JPanel panelCampoNombre;

	private JLabel lblUsuario;
	private JTextField txtUsuario;
	private JLabel lblUsuarioError;
	private JPanel panelCamposUsuario;

	private JButton btnAceptar;
	private JButton btnCancelar;

	/**
	 * Constructor por defecto. Inicializa la ventana y sus componentes.
	 */
	public AñadirContacto() {
		this.crearPanelAnadirContacto();
	}

	/**
	 * Obtiene la instancia única de la clase. Si no existe, la crea.
	 * 
	 * @return Instancia de AñadirContacto
	 */
	public static AñadirContacto getInstance() {
		if (instance == null) {
			instance = new AñadirContacto();
		}
		return instance;
	}

	/**
	 * Muestra la ventana de añadir contacto. Si ya está visible, la trae al frente.
	 */
	public void mostrarVentana() {
		if (frame != null && frame.isVisible()) {
			frame.toFront(); // Traer al frente si ya está abierta
			return;
		}
		frame.setLocationRelativeTo(null);
		// Añadir listener para recargar contactos al cerrarse
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosed(java.awt.event.WindowEvent e) {
				VentanaContactos.getInstance().cargarContactosExternamente();
			}

			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				frame.dispose();
			}
		});

		frame.setVisible(true);
	}

	/**
	 * Crea y configura el panel principal de la ventana de añadir contacto.
	 */
	private void crearPanelAnadirContacto() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		JPanel datosPersonales = new JPanel();
		frame.getContentPane().add(datosPersonales);
		datosPersonales.setBorder(new TitledBorder(null, "Datos de Contacto", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		datosPersonales.setLayout(new BoxLayout(datosPersonales, BoxLayout.Y_AXIS));

		datosPersonales.add(crearLineaAlerta());
		datosPersonales.add(crearLineaNombre());
		datosPersonales.add(crearLineaUsuario());

		this.crearPanelBotones();
		this.ocultarErrores();

		frame.revalidate();
		frame.pack();
	}

	/**
	 * Crea la línea superior con una alerta informativa.
	 * 
	 * @return JPanel con la alerta
	 */
	private JPanel crearLineaAlerta() {
		JPanel panelAlerta = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel iconoAdvertencia = new JLabel(UIManager.getIcon("OptionPane.warningIcon"));
		JLabel mensaje = new JLabel("Introduzca el nombre del contacto y su teléfono");
		mensaje.setFont(new Font("Arial", Font.PLAIN, 14));

		panelAlerta.add(iconoAdvertencia);
		panelAlerta.add(mensaje);

		return panelAlerta;
	}

	/**
	 * Crea el panel con el campo de nombre.
	 * 
	 * @return JPanel con el campo nombre
	 */
	private JPanel crearLineaNombre() {
		JPanel lineaNombre = new JPanel();
		lineaNombre.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		lineaNombre.setLayout(new BorderLayout(0, 0));

		panelCampoNombre = new JPanel();
		lineaNombre.add(panelCampoNombre, BorderLayout.CENTER);

		lblNombre = new JLabel("Nombre: ", JLabel.RIGHT);
		panelCampoNombre.add(lblNombre);
		fixedSize(lblNombre, 75, 20);
		txtNombre = new JTextField();
		panelCampoNombre.add(txtNombre);
		fixedSize(txtNombre, 270, 20);

		return lineaNombre;
	}

	/**
	 * Crea el panel con el campo de usuario (teléfono).
	 * 
	 * @return JPanel con el campo usuario
	 */
	private JPanel crearLineaUsuario() {
		JPanel lineaUsuario = new JPanel();
		lineaUsuario.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		lineaUsuario.setLayout(new BorderLayout(0, 0));

		panelCamposUsuario = new JPanel();
		lineaUsuario.add(panelCamposUsuario, BorderLayout.CENTER);

		lblUsuario = new JLabel("Teléfono: ", JLabel.RIGHT);
		panelCamposUsuario.add(lblUsuario);
		fixedSize(lblUsuario, 75, 20);
		txtUsuario = new JTextField();
		panelCamposUsuario.add(txtUsuario);
		fixedSize(txtUsuario, 270, 20);
		lblUsuarioError = new JLabel("El teléfono no está registrado", SwingConstants.CENTER);
		fixedSize(lblUsuarioError, 150, 15);
		lblUsuarioError.setForeground(Color.RED);
		lineaUsuario.add(lblUsuarioError, BorderLayout.SOUTH);

		return lineaUsuario;
	}

	/**
	 * Crea el panel con los botones Aceptar y Cancelar.
	 */
	private void crearPanelBotones() {
		JPanel lineaBotones = new JPanel();
		frame.getContentPane().add(lineaBotones, BorderLayout.SOUTH);
		lineaBotones.setBorder(new EmptyBorder(5, 0, 0, 0));
		lineaBotones.setLayout(new FlowLayout(FlowLayout.CENTER));

		btnAceptar = new JButton("Aceptar");
		lineaBotones.add(btnAceptar);

		btnCancelar = new JButton("Cancelar");
		lineaBotones.add(btnCancelar);

		this.crearManejadorBotonAceptar();
		this.crearManejadorBotonCancelar();
	}

	/**
	 * Asocia el comportamiento al botón Aceptar, validando e intentando registrar el contacto.
	 */
	private void crearManejadorBotonAceptar() {
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean OK = false;
				OK = checkFields();
				if (OK) {
					boolean registrado = false;
					registrado = Controlador.INSTANCE.esUsuarioRegistrado(txtUsuario.getText());
					if (registrado) {
						String info = Controlador.INSTANCE.crearContacto(txtNombre.getText(), txtUsuario.getText());
						JOptionPane.showMessageDialog(AñadirContacto.this, info,
								"Contacto", JOptionPane.INFORMATION_MESSAGE);
						frame.dispose();
					} else {
						JOptionPane.showMessageDialog(AñadirContacto.this, "El teléfono no se encuentra registrado.\n",
								"Contacto", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
	}

	/**
	 * Asocia el comportamiento al botón Cancelar, cerrando la ventana.
	 */
	private void crearManejadorBotonCancelar() {
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
	}

	/**
	 * Oculta los errores de validación mostrados en pantalla.
	 */
	private void ocultarErrores() {
		lblUsuarioError.setVisible(false);

		txtNombre.setBorder(new JTextField().getBorder());
		txtUsuario.setBorder(new JTextField().getBorder());

		lblNombre.setForeground(Color.BLACK);
		lblUsuario.setForeground(Color.BLACK);
	}

	/**
	 * Comprueba que los campos requeridos estén rellenos.
	 * 
	 * @return true si todos los campos son válidos, false si hay errores
	 */
	private boolean checkFields() {
		boolean salida = true;
		ocultarErrores();
		if (txtUsuario.getText().trim().isEmpty()) {
			lblUsuarioError.setVisible(true);
			lblNombre.setForeground(Color.RED);
			txtNombre.setBorder(BorderFactory.createLineBorder(Color.RED));
			salida = false;
		}
		frame.revalidate();
		frame.pack();

		return salida;
	}

	/**
	 * Establece un tamaño fijo para un componente.
	 * 
	 * @param o componente al que se aplica
	 * @param x ancho deseado
	 * @param y alto deseado
	 */
	private void fixedSize(JComponent o, int x, int y) {
		Dimension d = new Dimension(x, y);
		o.setMinimumSize(d);
		o.setMaximumSize(d);
		o.setPreferredSize(d);
	}
}
