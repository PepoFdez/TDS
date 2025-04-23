package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import controlador.Controlador;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import dominio.Contacto;

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

    public ModificarContacto(Contacto contacto) {
        this.contacto = contacto;
        this.crearPanelModificarContacto();
    }

    public void mostrarVentana() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
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
    
    private JPanel crearLineaAlerta() {
        JPanel panelAlerta = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel iconoAdvertencia = new JLabel(UIManager.getIcon("OptionPane.warningIcon"));
        JLabel mensaje = new JLabel("Modifique el nombre del contacto");
        mensaje.setFont(new Font("Arial", Font.PLAIN, 14));

        panelAlerta.add(iconoAdvertencia);
        panelAlerta.add(mensaje);

        return panelAlerta;
    }
    
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
        txtTelefono.setEditable(false); // El teléfono no se puede modificar
        txtTelefono.setBackground(new Color(240, 240, 240)); // Fondo gris claro para indicar que no es editable
        panelCampoTelefono.add(txtTelefono);
        fixedSize(txtTelefono, 270, 20);
        
        return lineaTelefono;
    }
    
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
                // Actualizar el contacto
                contacto.setNombre(nuevoNombre);
                
                // Aquí deberías llamar al controlador para guardar los cambios
                // Controlador.INSTANCE.actualizarContacto(contacto);
                
                JOptionPane.showMessageDialog(frame, 
                    "Contacto actualizado correctamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
                frame.dispose();
            }
        });
    }
    
    private void crearManejadorBotonCancelar() {
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }
    
    private void fixedSize(JComponent o, int x, int y) {
        Dimension d = new Dimension(x, y);
        o.setMinimumSize(d);
        o.setMaximumSize(d);
        o.setPreferredSize(d);
    }
}