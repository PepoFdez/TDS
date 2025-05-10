package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;
import com.toedter.calendar.JDateChooser;

import controlador.Controlador;

/**
 * Diálogo para el registro de nuevos usuarios.
 * Permite introducir todos los datos necesarios para crear una nueva cuenta de usuario.
 */
public class Registro extends JDialog {

    private static final long serialVersionUID = 1L;
    
    // Componentes de la interfaz
    private JLabel lblNombre;
    private JLabel lblApellidos;
    private JLabel lblFechaNacimiento;
    private JLabel lblEmail;
    private JLabel lblUsuario;
    private JLabel lblPassword;
    private JLabel lblPasswordChk;
    private JLabel lblSaludo;
    private JLabel lblImagen;
    private JTextField txtNombre;
    private JTextField txtApellidos;
    private JTextField txtFechaNacimiento;
    private JTextField txtEmail;
    private JTextField txtUsuario;
    private JTextField txtSaludo;
    private JTextField txtImagen;
    private JPasswordField txtPassword;
    private JPasswordField txtPasswordChk;
    private JButton btnRegistrar;
    private JButton btnCancelar;

    // Componentes para mostrar errores
    private JLabel lblNombreError;
    private JLabel lblApellidosError;
    private JLabel lblFechaNacimientoError;
    private JLabel lblEmailError;
    private JLabel lblUsuarioError;
    private JLabel lblPasswordError;
    
    // Paneles para organizar los componentes
    private JPanel panelCampoNombre;
    private JPanel panel;
    private JPanel panelCampoApellidos;
    private JPanel panelCamposEmail;
    private JPanel panelCamposUsuario;
    private JPanel panelCamposFechaNacimiento;
    private JPanel panelCamposSaludo;
    private JPanel panelCamposImagen;

    private JDateChooser dateChooser; // Campo para la selección de la fecha
    
    /**
     * Constructor que crea el diálogo de registro de usuario.
     * 
     * @param owner Ventana padre sobre la que se centrará este diálogo
     */
    public Registro(JFrame owner) {
        super(owner, "Registro Usuario", true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.crearPanelRegistro();
    }

    /**
     * Crea el panel principal con todos los componentes de la interfaz de registro.
     */
    private void crearPanelRegistro() {
        this.getContentPane().setLayout(new BorderLayout());

        JPanel datosPersonales = new JPanel();
        this.getContentPane().add(datosPersonales);
        datosPersonales.setBorder(
                new TitledBorder(null, "Datos de Registro", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        datosPersonales.setLayout(new BoxLayout(datosPersonales, BoxLayout.Y_AXIS));

        datosPersonales.add(creaLineaNombre());
        datosPersonales.add(crearLineaApellidos());
        datosPersonales.add(crearLineaEmail());
        datosPersonales.add(crearLineaUsuario());
        datosPersonales.add(crearLineaPassword());
        datosPersonales.add(crearLineaFechaNacimiento());
        datosPersonales.add(crearLineaSaludo());
        datosPersonales.add(crearLineaImagen());

        this.crearPanelBotones();
        this.ocultarErrores();

        this.revalidate();
        this.pack();
    }

    /**
     * Crea la línea con el campo para el nombre del usuario.
     * 
     * @return Panel con los componentes del campo nombre
     */
    private JPanel creaLineaNombre() {
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

        lblNombreError = new JLabel("El nombre es obligatorio", SwingConstants.CENTER);
        lineaNombre.add(lblNombreError, BorderLayout.SOUTH);
        fixedSize(lblNombreError, 224, 15);
        lblNombreError.setForeground(Color.RED);

        return lineaNombre;
    }

    /**
     * Crea la línea con el campo para los apellidos del usuario.
     * 
     * @return Panel con los componentes del campo apellidos
     */
    private JPanel crearLineaApellidos() {
        JPanel lineaApellidos = new JPanel();
        lineaApellidos.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        lineaApellidos.setLayout(new BorderLayout(0, 0));

        panelCampoApellidos = new JPanel();
        lineaApellidos.add(panelCampoApellidos);

        lblApellidos = new JLabel("Apellidos: ", JLabel.RIGHT);
        panelCampoApellidos.add(lblApellidos);
        fixedSize(lblApellidos, 75, 20);
        txtApellidos = new JTextField();
        panelCampoApellidos.add(txtApellidos);
        fixedSize(txtApellidos, 270, 20);

        lblApellidosError = new JLabel("Los apellidos son obligatorios", SwingConstants.CENTER);
        lineaApellidos.add(lblApellidosError, BorderLayout.SOUTH);
        fixedSize(lblApellidosError, 255, 15);
        lblApellidosError.setForeground(Color.RED);

        return lineaApellidos;
    }

    /**
     * Crea la línea con el campo para el email del usuario.
     * 
     * @return Panel con los componentes del campo email
     */
    private JPanel crearLineaEmail() {
        JPanel lineaEmail = new JPanel();
        lineaEmail.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        lineaEmail.setLayout(new BorderLayout(0, 0));

        panelCamposEmail = new JPanel();
        lineaEmail.add(panelCamposEmail, BorderLayout.CENTER);

        lblEmail = new JLabel("Email: ", JLabel.RIGHT);
        panelCamposEmail.add(lblEmail);
        fixedSize(lblEmail, 75, 20);
        txtEmail = new JTextField();
        panelCamposEmail.add(txtEmail);
        fixedSize(txtEmail, 270, 20);
        lblEmailError = new JLabel("El Email es obligatorio", SwingConstants.CENTER);
        fixedSize(lblEmailError, 150, 15);
        lblEmailError.setForeground(Color.RED);
        lineaEmail.add(lblEmailError, BorderLayout.SOUTH);

        return lineaEmail;
    }

    /**
     * Crea la línea con el campo para el teléfono del usuario.
     * 
     * @return Panel con los componentes del campo teléfono
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
        lblUsuarioError = new JLabel("El teléfono ya está registrado", SwingConstants.CENTER);
        fixedSize(lblUsuarioError, 150, 15);
        lblUsuarioError.setForeground(Color.RED);
        lineaUsuario.add(lblUsuarioError, BorderLayout.SOUTH);

        return lineaUsuario;
    }

    /**
     * Crea la línea con los campos para la contraseña y su confirmación.
     * 
     * @return Panel con los componentes de los campos de contraseña
     */
    private JPanel crearLineaPassword() {
        JPanel lineaPassword = new JPanel();
        lineaPassword.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        lineaPassword.setLayout(new BorderLayout(0, 0));

        panel = new JPanel();
        lineaPassword.add(panel, BorderLayout.CENTER);

        lblPassword = new JLabel("Password: ", JLabel.RIGHT);
        panel.add(lblPassword);
        fixedSize(lblPassword, 75, 20);
        txtPassword = new JPasswordField();
        panel.add(txtPassword);
        fixedSize(txtPassword, 100, 20);
        lblPasswordChk = new JLabel("Otra vez:", JLabel.RIGHT);
        panel.add(lblPasswordChk);
        fixedSize(lblPasswordChk, 60, 20);
        txtPasswordChk = new JPasswordField();
        panel.add(txtPasswordChk);
        fixedSize(txtPasswordChk, 100, 20);

        lblPasswordError = new JLabel("Error al introducir las contraseñas", JLabel.CENTER);
        lineaPassword.add(lblPasswordError, BorderLayout.SOUTH);
        lblPasswordError.setForeground(Color.RED);

        return lineaPassword;
    }
    
    /**
     * Crea la línea con el campo para la fecha de nacimiento del usuario.
     * 
     * @return Panel con los componentes del campo fecha de nacimiento
     */
    private JPanel crearLineaFechaNacimiento() {
        JPanel lineaFechaNacimiento = new JPanel();
        lineaFechaNacimiento.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        lineaFechaNacimiento.setLayout(new BorderLayout(0, 0));

        panelCamposFechaNacimiento = new JPanel();
        lineaFechaNacimiento.add(panelCamposFechaNacimiento, BorderLayout.CENTER);

        lblFechaNacimiento = new JLabel("Fecha de Nacimiento: ", JLabel.RIGHT);
        panelCamposFechaNacimiento.add(lblFechaNacimiento);
        fixedSize(lblFechaNacimiento, 130, 20);

        // Sustituimos el JTextField por JDateChooser
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy"); // Formato de la fecha
        panelCamposFechaNacimiento.add(dateChooser);
        fixedSize(dateChooser, 215, 20);

        // Evento para reflejar la fecha seleccionada en el cuadro de texto
        dateChooser.addPropertyChangeListener("date", evt -> {
            if (dateChooser.getDate() != null) {
                txtFechaNacimiento.setText(((JTextField) dateChooser.getDateEditor().getUiComponent()).getText());
            }
        });

        // Campo de texto oculto para almacenamiento (si es necesario)
        txtFechaNacimiento = new JTextField();
        txtFechaNacimiento.setVisible(false); // Lo ocultamos porque ahora usamos JDateChooser

        lblFechaNacimientoError = new JLabel("Introduce la fecha de nacimiento", SwingConstants.CENTER);
        fixedSize(lblFechaNacimientoError, 150, 15);
        lblFechaNacimientoError.setForeground(Color.RED);
        lineaFechaNacimiento.add(lblFechaNacimientoError, BorderLayout.SOUTH);

        return lineaFechaNacimiento;
    }

    /**
     * Crea la línea con el campo para el saludo personal del usuario.
     * 
     * @return Panel con los componentes del campo saludo
     */
    private JPanel crearLineaSaludo() {
        JPanel lineaSaludo = new JPanel();
        lineaSaludo.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        lineaSaludo.setLayout(new BorderLayout(0, 0));

        panelCamposSaludo = new JPanel();
        lineaSaludo.add(panelCamposSaludo, BorderLayout.CENTER);

        lblSaludo = new JLabel("Saludo: ", JLabel.RIGHT);
        panelCamposSaludo.add(lblSaludo);
        fixedSize(lblSaludo, 75, 20);
        txtSaludo = new JTextField();
        panelCamposSaludo.add(txtSaludo);
        fixedSize(txtSaludo, 270, 20);

        return lineaSaludo;
    }
    
    /**
     * Crea la línea con el campo para la imagen de perfil del usuario.
     * 
     * @return Panel con los componentes del campo imagen de perfil
     */
    private JPanel crearLineaImagen() {
        JPanel lineaImagen = new JPanel();
        lineaImagen.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        lineaImagen.setLayout(new BorderLayout(0, 0));

        panelCamposImagen = new JPanel();
        lineaImagen.add(panelCamposImagen, BorderLayout.CENTER);

        lblImagen = new JLabel("Imagen de perfil: ", JLabel.RIGHT);
        panelCamposImagen.add(lblImagen);
        fixedSize(lblImagen, 75, 20);
        txtImagen = new JTextField();
        panelCamposImagen.add(txtImagen);
        fixedSize(txtImagen, 270, 20);

        return lineaImagen;
    }

    /**
     * Crea el panel con los botones de registrar y cancelar.
     */
    private void crearPanelBotones() {
        JPanel lineaBotones = new JPanel();
        this.getContentPane().add(lineaBotones, BorderLayout.SOUTH);
        lineaBotones.setBorder(new EmptyBorder(5, 0, 0, 0));
        lineaBotones.setLayout(new FlowLayout(FlowLayout.CENTER));

        btnRegistrar = new JButton("Registrar");
        lineaBotones.add(btnRegistrar);

        btnCancelar = new JButton("Cancelar");
        lineaBotones.add(btnCancelar);

        this.crearManejadorBotonRegistrar();
        this.crearManejadorBotonCancelar();
    }

    /**
     * Crea el manejador de eventos para el botón Registrar.
     * Valida los campos y registra al usuario si son correctos.
     */
    private void crearManejadorBotonRegistrar() {
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean OK = false;
                OK = checkFields();
                if (OK) {
                    boolean registrado = false;
                    registrado = Controlador.INSTANCE.registrarUsuario(txtNombre.getText(), txtApellidos.getText(),
                            txtEmail.getText(), txtUsuario.getText(), new String(txtPassword.getPassword()),
                            txtFechaNacimiento.getText(), txtSaludo.getText(), txtImagen.getText());
                    if (registrado) {
                        JOptionPane.showMessageDialog(Registro.this, "Usuario registrado correctamente.",
                                "Registro", JOptionPane.INFORMATION_MESSAGE);

                        Login loginView = new Login();
                        loginView.mostrarVentana();
                        Registro.this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(Registro.this, "No se ha podido llevar a cabo el registro.\n",
                                "Registro", JOptionPane.ERROR_MESSAGE);
                        Registro.this.setTitle("Login Gestor Eventos");
                    }
                }
            }
        });
    }

    /**
     * Crea el manejador de eventos para el botón Cancelar.
     * Cierra el diálogo y muestra la ventana de login.
     */
    private void crearManejadorBotonCancelar() {
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Login loginView = new Login();
                loginView.mostrarVentana();
                Registro.this.dispose();
            }
        });
    }
    
    /**
     * Valida los campos del formulario de registro.
     * 
     * @return true si todos los campos obligatorios son válidos, false en caso contrario
     */
    private boolean checkFields() {
        boolean salida = true;
        ocultarErrores();
        if (txtNombre.getText().trim().isEmpty()) {
            lblNombreError.setVisible(true);
            lblNombre.setForeground(Color.RED);
            txtNombre.setBorder(BorderFactory.createLineBorder(Color.RED));
            salida = false;
        }
        if (txtApellidos.getText().trim().isEmpty()) {
            lblApellidosError.setVisible(true);
            lblApellidos.setForeground(Color.RED);
            txtApellidos.setBorder(BorderFactory.createLineBorder(Color.RED));
            salida = false;
        }
        if (txtEmail.getText().trim().isEmpty()) {
            lblEmailError.setVisible(true);
            lblEmail.setForeground(Color.RED);
            txtEmail.setBorder(BorderFactory.createLineBorder(Color.RED));
            salida = false;
        }
        if (txtUsuario.getText().trim().isEmpty()) {
            lblUsuarioError.setText("El teléfono es obligatorio");
            lblUsuarioError.setVisible(true);
            lblUsuario.setForeground(Color.RED);
            txtUsuario.setBorder(BorderFactory.createLineBorder(Color.RED));
            salida = false;
        }
        String password = new String(txtPassword.getPassword());
        String password2 = new String(txtPasswordChk.getPassword());
        if (password.isEmpty()) {
            lblPasswordError.setText("El password no puede estar vacio");
            lblPasswordError.setVisible(true);
            lblPassword.setForeground(Color.RED);
            txtPassword.setBorder(BorderFactory.createLineBorder(Color.RED));
            salida = false;
        }
        if (password2.isEmpty()) {
            lblPasswordError.setText("El password no puede estar vacio");
            lblPasswordError.setVisible(true);
            lblPasswordChk.setForeground(Color.RED);
            txtPasswordChk.setBorder(BorderFactory.createLineBorder(Color.RED));
            salida = false;
        }
        if (!password.equals(password2)) {
            lblPasswordError.setText("Los dos passwords no coinciden");
            lblPasswordError.setVisible(true);
            lblPassword.setForeground(Color.RED);
            lblPasswordChk.setForeground(Color.RED);
            txtPassword.setBorder(BorderFactory.createLineBorder(Color.RED));
            txtPasswordChk.setBorder(BorderFactory.createLineBorder(Color.RED));
            salida = false;
        }

        this.revalidate();
        this.pack();

        return salida;
    }

    /**
     * Oculta todos los mensajes de error y restablece los estilos visuales de los campos.
     */
    private void ocultarErrores() {
        lblNombreError.setVisible(false);
        lblApellidosError.setVisible(false);
        lblFechaNacimientoError.setVisible(false);
        lblEmailError.setVisible(false);
        lblUsuarioError.setVisible(false);
        lblPasswordError.setVisible(false);
        lblFechaNacimientoError.setVisible(false);

        txtNombre.setBorder(new JTextField().getBorder());
        txtApellidos.setBorder(new JTextField().getBorder());
        txtEmail.setBorder(new JTextField().getBorder());
        txtUsuario.setBorder(new JTextField().getBorder());
        txtPassword.setBorder(new JTextField().getBorder());
        txtPasswordChk.setBorder(new JTextField().getBorder());
        txtFechaNacimiento.setBorder(new JTextField().getBorder());
        txtSaludo.setBorder(new JTextField().getBorder());
        txtImagen.setBorder(new JTextField().getBorder());

        lblNombre.setForeground(Color.BLACK);
        lblApellidos.setForeground(Color.BLACK);
        lblEmail.setForeground(Color.BLACK);
        lblUsuario.setForeground(Color.BLACK);
        lblPassword.setForeground(Color.BLACK);
        lblPasswordChk.setForeground(Color.BLACK);
        lblFechaNacimiento.setForeground(Color.BLACK);
        lblSaludo.setForeground(Color.BLACK);
        lblImagen.setForeground(Color.BLACK);
    }

    /**
     * Establece un tamaño fijo para un componente.
     * 
     * @param o Componente al que se le aplicará el tamaño fijo
     * @param x Ancho del componente
     * @param y Alto del componente
     */
    private void fixedSize(JComponent o, int x, int y) {
        Dimension d = new Dimension(x, y);
        o.setMinimumSize(d);
        o.setMaximumSize(d);
        o.setPreferredSize(d);
    }
}