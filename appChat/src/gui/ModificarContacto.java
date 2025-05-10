package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import controlador.Controlador;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import dominio.Contacto;

/**
 * Clase que representa una ventana de diálogo para modificar el nombre de un contacto.
 * El número de teléfono se muestra pero no se puede editar.
 */
public class ModificarContacto extends JDialog {

    private static final long serialVersionUID = 1L;
    private JFrame frame;
    private Contacto contacto;

    private JLabel lblNombre;
    private JTextField txtNombre;
    private JPanel panelCampoNombre;

    private JLabel lblTelefono;
    private JTextField txtTelefono;
    private JPanel panelCampoTelefono;

    private JButton btnAceptar;
    private JButton btnCancelar;

    /**
     * Constructor que inicializa el diálogo con los datos del contacto.
     * 
     * @param contacto El contacto que se va a modificar.
     */
    public ModificarContacto(Contacto contacto) {
        this.contacto = contacto;
        this.crearPanelModificarContacto();
    }

    /**
     * Muestra la ventana centrada en la pantalla.
     */
    public void mostrarVentana() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Crea y configura la ventana principal y sus componentes.
     */
    private void crearPanelModificarContacto() {
        frame = new JFrame("Modificar Contacto");
        frame.setBounds(100, 100, 450, 250);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        JPanel datosPersonales = new JPanel();
        frame.getContentPane().add(datosPersonales);
        datosPersonales.setBorder(
            new TitledBorder(null, "Datos de Contacto", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        datosPersonales.setLayout(new BoxLayout(datosPersonales, BoxLayout.Y_AXIS));

        datosPersonales.add(crearLineaAlerta());
        datosPersonales.add(crearLineaNombre());
        datosPersonales.add(crearLineaTelefono());

        this.crearPanelBotones();
        frame.revalidate();
        frame.pack();
    }

    /**
     * Crea una línea con un mensaje de advertencia.
     * 
     * @return Panel con mensaje e icono.
     */
    private JPanel crearLineaAlerta() {
        JPanel panelAlerta = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel iconoAdvertencia = new JLabel(UIManager.getIcon("OptionPane.warningIcon"));
        JLabel mensaje = new JLabel("Modifique el nombre del contacto");
        mensaje.setFont(new Font("Arial", Font.PLAIN, 14));

        panelAlerta.add(iconoAdvertencia);
        panelAlerta.add(mensaje);

        return panelAlerta;
    }

    /**
     * Crea la línea de entrada para modificar el nombre del contacto.
     * 
     * @return Panel con campo de nombre.
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

        txtNombre = new JTextField(contacto.getNombre());
        panelCampoNombre.add(txtNombre);
        fixedSize(txtNombre, 270, 20);

        return lineaNombre;
    }

    /**
     * Crea la línea de texto que muestra el teléfono del contacto (no editable).
     * 
     * @return Panel con campo de teléfono.
     */
    private JPanel crearLineaTelefono() {
        JPanel lineaTelefono = new JPanel();
        lineaTelefono.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        lineaTelefono.setLayout(new BorderLayout(0, 0));

        panelCampoTelefono = new JPanel();
        lineaTelefono.add(panelCampoTelefono, BorderLayout.CENTER);

        lblTelefono = new JLabel("Teléfono: ", JLabel.RIGHT);
        panelCampoTelefono.add(lblTelefono);
        fixedSize(lblTelefono, 75, 20);

        String telefono = Controlador.INSTANCE.getTelefono(contacto);
        txtTelefono = new JTextField(telefono);
        txtTelefono.setEditable(false); // No se puede modificar
        txtTelefono.setBackground(new Color(240, 240, 240)); // Fondo gris claro
        panelCampoTelefono.add(txtTelefono);
        fixedSize(txtTelefono, 270, 20);

        return lineaTelefono;
    }

    /**
     * Crea el panel con botones "Aceptar" y "Cancelar".
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
     * Asigna el comportamiento al botón "Aceptar" para modificar el nombre.
     */
    private void crearManejadorBotonAceptar() {
        btnAceptar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nuevoNombre = txtNombre.getText().trim();

                if (nuevoNombre.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                        "El nombre no puede estar vacío",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Controlador.INSTANCE.modificarNombreContacto(contacto, nuevoNombre);
                contacto.setNombre(nuevoNombre);

                JOptionPane.showMessageDialog(frame,
                    "Contacto actualizado correctamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

                frame.dispose();
            }
        });
    }

    /**
     * Asigna el comportamiento al botón "Cancelar", que cierra la ventana.
     */
    private void crearManejadorBotonCancelar() {
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }

    /**
     * Establece un tamaño fijo para un componente.
     * 
     * @param o Componente al que se aplicará el tamaño.
     * @param x Ancho fijo.
     * @param y Alto fijo.
     */
    private void fixedSize(JComponent o, int x, int y) {
        Dimension d = new Dimension(x, y);
        o.setMinimumSize(d);
        o.setMaximumSize(d);
        o.setPreferredSize(d);
    }
}
