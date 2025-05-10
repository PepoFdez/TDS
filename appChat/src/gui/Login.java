package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controlador.Controlador;

/**
 * Clase que representa la ventana de login para la aplicación AppChat.
 * Permite al usuario introducir su teléfono y contraseña, e iniciar sesión o registrarse.
 */
public class Login {

	private JFrame frame;
	private JTextField textUsuario;
	private JPasswordField textPassword;

	/**
	 * Muestra la ventana de login centrada en pantalla.
	 */
	public void mostrarVentana() {
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	/**
	 * Constructor de la clase Login. Inicializa la interfaz.
	 */
	public Login() {
		initialize();
	}

	/**
	 * Inicializa y configura la ventana principal del login.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Login AppChat");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		crearPanelTitulo();
		crearPanelLogin();

		frame.setResizable(false);
		frame.pack();
	}

	/**
	 * Crea el panel superior que contiene el logo de la aplicación.
	 */
	private void crearPanelTitulo() {
	    JPanel panel_Norte = new JPanel();
	    frame.getContentPane().add(panel_Norte, BorderLayout.NORTH);
	    panel_Norte.setLayout(new BoxLayout(panel_Norte, BoxLayout.Y_AXIS));

	    panel_Norte.add(Box.createRigidArea(new Dimension(0, 10)));

	    JLabel lblLogo = new JLabel();
	    ImageIcon icono = new ImageIcon("resources/logo3.png");
	    Image imgEscalada = icono.getImage().getScaledInstance(125, 125, Image.SCALE_SMOOTH);
	    lblLogo.setIcon(new ImageIcon(imgEscalada));
	    lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
	    panel_Norte.add(lblLogo);
	}

	/**
	 * Crea el panel central que incluye los campos de login y botones de acción.
	 */
	private void crearPanelLogin() {
		JPanel panelLogin = new JPanel();
		panelLogin.setBorder(new EmptyBorder(10, 10, 10, 10));
		frame.getContentPane().add(panelLogin, BorderLayout.CENTER);
		panelLogin.setLayout(new BorderLayout(0, 0));

		panelLogin.add(crearPanelUsuarioPassw(), BorderLayout.NORTH);
		panelLogin.add(crearPanelBotones(), BorderLayout.SOUTH);
	}

	/**
	 * Crea el panel con los campos de texto para el teléfono y la contraseña.
	 * @return JPanel con los campos de entrada.
	 */
	private JPanel crearPanelUsuarioPassw() {
		JPanel panelCampos = new JPanel();
		panelCampos.setBorder(new TitledBorder(null, "Login", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));

		// Campo Teléfono
		JPanel panelCampoUsuario = new JPanel();
		panelCampos.add(panelCampoUsuario);
		panelCampoUsuario.setLayout(new BorderLayout(0, 0));

		JLabel lblUsuario = new JLabel("Teléfono: ");
		panelCampoUsuario.add(lblUsuario);
		lblUsuario.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUsuario.setFont(new Font("Tahoma", Font.PLAIN, 12));

		textUsuario = new JTextField();
		panelCampoUsuario.add(textUsuario, BorderLayout.EAST);
		textUsuario.setColumns(15);

		// Campo Contraseña
		JPanel panelCampoPassword = new JPanel();
		panelCampos.add(panelCampoPassword);
		panelCampoPassword.setLayout(new BorderLayout(0, 0));

		JLabel lblPassword = new JLabel("Contraseña: ");
		panelCampoPassword.add(lblPassword);
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));

		textPassword = new JPasswordField();
		panelCampoPassword.add(textPassword, BorderLayout.EAST);
		textPassword.setColumns(15);
		
		return panelCampos;
	}

	/**
	 * Crea el panel inferior con los botones de Login, Registro y Salir.
	 * @return JPanel con los botones.
	 */
	private JPanel crearPanelBotones() {
		JPanel panelBotones = new JPanel();
		panelBotones.setBorder(new EmptyBorder(5, 0, 5, 0));
		panelBotones.setLayout(new BorderLayout(0, 0));

		JPanel panelBotonesLoginRegistro = new JPanel();
		panelBotones.add(panelBotonesLoginRegistro, BorderLayout.WEST);

		JButton btnLogin = new JButton("Login");
		panelBotonesLoginRegistro.add(btnLogin);

		JButton btnRegistro = new JButton("Registro");
		panelBotonesLoginRegistro.add(btnRegistro);

		JPanel panelBotonSalir = new JPanel();
		panelBotones.add(panelBotonSalir, BorderLayout.EAST);

		JButton btnSalir = new JButton("Salir");
		panelBotonSalir.add(btnSalir);

		addManejadorBotonLogin(btnLogin);
		addManejadorBotonRegistro(btnRegistro);
		addManejadorBotonSalir(btnSalir);
		
		return panelBotones;
	}

	/**
	 * Asigna acción al botón "Salir" para cerrar la aplicación.
	 * @param btnSalir Botón de salir.
	 */
	private void addManejadorBotonSalir(JButton btnSalir) {
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				System.exit(0);
			}
		});
	}

	/**
	 * Asigna acción al botón "Registro" para abrir la ventana de registro.
	 * @param btnRegistro Botón de registro.
	 */
	private void addManejadorBotonRegistro(JButton btnRegistro) {
		btnRegistro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Registro registro = new Registro(frame);
				registro.setLocationRelativeTo(frame);                              	
				registro.setVisible(true);
				frame.dispose();
			}
		});
	}

	/**
	 * Asigna acción al botón "Login" y al uso de la tecla Enter para iniciar sesión.
	 * @param btnLogin Botón de login.
	 */
	private void addManejadorBotonLogin(JButton btnLogin) {
	    // Acción al hacer clic
	    btnLogin.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            realizarLogin();
	        }
	    });
	    
	    // Configurar Enter para activar el botón Login
	    InputMap inputMap = btnLogin.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	    ActionMap actionMap = btnLogin.getActionMap();
	    
	    inputMap.put(KeyStroke.getKeyStroke("ENTER"), "enterAction");
	    actionMap.put("enterAction", new AbstractAction() {
	        private static final long serialVersionUID = 1L;

	        @Override
	        public void actionPerformed(ActionEvent e) {
	            realizarLogin();
	        }
	    });
	}

	/**
	 * Realiza la validación del login utilizando el controlador.
	 * Si las credenciales son válidas, se abre la ventana principal.
	 * En caso contrario, se muestra un mensaje de error.
	 */
	private void realizarLogin() {
	    boolean login = Controlador.INSTANCE.loginUsuario(
	            textUsuario.getText(),
	            new String(textPassword.getPassword()));

	    if (login) {
	        VentanaPrincipal principal = new VentanaPrincipal();
	        principal.mostrarVentana();
	        frame.dispose();
	    } else {
	        JOptionPane.showMessageDialog(frame, "Teléfono o contraseña no válido",
	                "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}
}
