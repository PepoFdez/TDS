package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
 * Diálogo para modificar los datos del usuario.
 * Permite al usuario actualizar su información personal como nombre, apellidos, email, contraseña, etc.
 */
public class ModificarUsuario extends JDialog {

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
    private JTextField txtEmail;
    private JTextField txtUsuario; // Teléfono (no editable)
    private JTextField txtSaludo;
    private JTextField txtImagen;
    private JPasswordField txtPassword;
    private JPasswordField txtPasswordChk;
    private JButton btnGuardar;
    private JButton btnCancelar;

    // Componentes para mostrar errores
    private JLabel lblNombreError;
    private JLabel lblApellidosError;
    private JLabel lblFechaNacimientoError;
    private JLabel lblEmailError;
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

    private JFrame owner;
    private JDateChooser dateChooser;
    
    /**
     * Constructor que crea el diálogo de modificación de usuario.
     * 
     * @param owner Ventana padre sobre la que se centrará este diálogo
     */
    public ModificarUsuario(JFrame owner) {
        super(owner, "Modificar Perfil", true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.owner = owner;
        
        // Primero crear todos los componentes
        crearPanelModificacion();
        
        // Luego cargar los datos
        cargarDatosUsuario();
    }

    /**
     * Carga los datos actuales del usuario desde el controlador y los muestra en los campos.
     */
    private void cargarDatosUsuario() {
        // Obtener datos actuales del usuario desde el controlador
        List<String> datosUsuario = Controlador.INSTANCE.getDatosUsuario();
        
        // Asignar valores a los campos
        txtNombre.setText(datosUsuario.get(0));
        txtApellidos.setText(datosUsuario.get(1));
        txtEmail.setText(datosUsuario.get(2));
        txtUsuario.setText(datosUsuario.get(3));
        txtUsuario.setEditable(false); // Teléfono no editable
        
        // Asignar fecha al dateChooser
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            dateChooser.setDate(sdf.parse(datosUsuario.get(5)));
        } catch (Exception e) {
            System.err.println("Error al parsear fecha de nacimiento: " + e.getMessage());
        }
        
        txtSaludo.setText(datosUsuario.get(6));
        txtImagen.setText(datosUsuario.size() > 7 ? datosUsuario.get(7) : ""); // Imagen es opcional
    }

    /**
     * Crea el panel principal con todos los componentes de la interfaz.
     */
    private void crearPanelModificacion() {
        this.getContentPane().setLayout(new BorderLayout());

        JPanel datosPersonales = new JPanel();
        this.getContentPane().add(datosPersonales);
        datosPersonales.setBorder(
                new TitledBorder(null, "Modificar Perfil", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        datosPersonales.setLayout(new BoxLayout(datosPersonales, BoxLayout.Y_AXIS));

        // Crear todos los componentes primero
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
     * Crea la línea con el campo para el teléfono del usuario (no editable).
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
        txtUsuario.setEditable(false); // Teléfono no editable
        panelCamposUsuario.add(txtUsuario);
        fixedSize(txtUsuario, 270, 20);

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

        lblPassword = new JLabel("Nuevo Password: ", JLabel.RIGHT);
        panel.add(lblPassword);
        fixedSize(lblPassword, 100, 20);
        txtPassword = new JPasswordField();
        panel.add(txtPassword);
        fixedSize(txtPassword, 100, 20);
        lblPasswordChk = new JLabel("Confirmar:", JLabel.RIGHT);
        panel.add(lblPasswordChk);
        fixedSize(lblPasswordChk, 60, 20);
        txtPasswordChk = new JPasswordField();
        panel.add(txtPasswordChk);
        fixedSize(txtPasswordChk, 100, 20);

        lblPasswordError = new JLabel("Las contraseñas no coinciden", JLabel.CENTER);
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

        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        panelCamposFechaNacimiento.add(dateChooser);
        fixedSize(dateChooser, 215, 20);

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
     * Crea el panel con los botones de guardar y cancelar.
     */
    private void crearPanelBotones() {
        JPanel lineaBotones = new JPanel();
        this.getContentPane().add(lineaBotones, BorderLayout.SOUTH);
        lineaBotones.setBorder(new EmptyBorder(5, 0, 0, 0));
        lineaBotones.setLayout(new FlowLayout(FlowLayout.CENTER));

        btnGuardar = new JButton("Guardar Cambios");
        lineaBotones.add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        lineaBotones.add(btnCancelar);

        this.crearManejadorBotonGuardar();
        this.crearManejadorBotonCancelar();
    }

    /**
     * Crea el manejador de eventos para el botón Guardar.
     * Valida los campos y actualiza los datos del usuario si son correctos.
     */
    private void crearManejadorBotonGuardar() {
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean OK = checkFields();
                if (OK) {
                    // Solo actualizar password si se ha introducido uno nuevo
                    String nuevoPassword = "";
                    if (txtPassword.getPassword().length > 0) {
                        nuevoPassword = new String(txtPassword.getPassword());
                    }

                    // Obtener fecha del dateChooser en formato dd/MM/yyyy
                    String fechaNacimiento = "";
                    if (dateChooser.getDate() != null) {
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                        fechaNacimiento = sdf.format(dateChooser.getDate());
                    }

                    boolean actualizado = Controlador.INSTANCE.actualizarUsuario(
                            txtNombre.getText(),
                            txtApellidos.getText(),
                            txtEmail.getText(),
                            nuevoPassword,
                            fechaNacimiento,
                            txtSaludo.getText(),
                            txtImagen.getText()
                    );

                    if (actualizado) {
                        JOptionPane.showMessageDialog(ModificarUsuario.this, 
                                "Datos actualizados correctamente.",
                                "Modificación", JOptionPane.INFORMATION_MESSAGE);
                        ModificarUsuario.this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(ModificarUsuario.this, 
                                "Error al actualizar los datos.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    /**
     * Crea el manejador de eventos para el botón Cancelar.
     * Cierra el diálogo sin guardar cambios.
     */
    private void crearManejadorBotonCancelar() {
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ModificarUsuario.this.dispose();
            }
        });
    }

    /**
     * Valida los campos del formulario.
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
        
        // Validar contraseñas solo si se ha introducido alguna
        String password = new String(txtPassword.getPassword());
        String password2 = new String(txtPasswordChk.getPassword());
        
        if (!password.isEmpty() || !password2.isEmpty()) {
            if (!password.equals(password2)) {
                lblPasswordError.setText("Las contraseñas no coinciden");
                lblPasswordError.setVisible(true);
                lblPassword.setForeground(Color.RED);
                lblPasswordChk.setForeground(Color.RED);
                txtPassword.setBorder(BorderFactory.createLineBorder(Color.RED));
                txtPasswordChk.setBorder(BorderFactory.createLineBorder(Color.RED));
                salida = false;
            }
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
        lblPasswordError.setVisible(false);

        txtNombre.setBorder(new JTextField().getBorder());
        txtApellidos.setBorder(new JTextField().getBorder());
        txtEmail.setBorder(new JTextField().getBorder());
        txtPassword.setBorder(new JTextField().getBorder());
        txtPasswordChk.setBorder(new JTextField().getBorder());
        dateChooser.setBorder(new JTextField().getBorder());

        lblNombre.setForeground(Color.BLACK);
        lblApellidos.setForeground(Color.BLACK);
        lblEmail.setForeground(Color.BLACK);
        lblPassword.setForeground(Color.BLACK);
        lblPasswordChk.setForeground(Color.BLACK);
        lblFechaNacimiento.setForeground(Color.BLACK);
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

    /**
     * Muestra la ventana centrada respecto a su ventana padre.
     */
    public void mostrarVentana() {
        this.setLocationRelativeTo(owner);
        this.setVisible(true);
    }
}