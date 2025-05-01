package gui;

import dominio.Contacto;
import controlador.Controlador;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.LinkedList;

public class VentanaPremiumActivo {
	
	private static VentanaPremiumActivo instance;
    private JFrame frame;
    private JComboBox<String> contactosComboBox;
    private JButton exportarButton;
    private JButton cancelarSuscripcionButton;

    public VentanaPremiumActivo() {
        initialize();
        cargarContactos();
    }
    
    public static VentanaPremiumActivo getInstance() {
        if (instance == null) {
            instance = new VentanaPremiumActivo();
        }
        return instance;
    }
    
    public void mostrarVentana() {
        if (frame != null && frame.isVisible()) {
            frame.toFront(); // Traer al frente si ya está abierta
            return;
        }
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void initialize() {
        // Configuración básica de la ventana
        frame = new JFrame("Beneficios Premium");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setMinimumSize(new Dimension(450, 250));
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(240, 240, 240));

        // Panel principal con bordes
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(255, 255, 255));

        // Panel de contenido
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Título
        JLabel titleLabel = new JLabel("Exportar chats en formato PDF");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Panel de selección de contacto
        JPanel contactPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        contactPanel.setBackground(Color.WHITE);
        
        JLabel contactLabel = new JLabel("Selecciona un contacto:");
        contactLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        contactosComboBox = new JComboBox<>();
        contactosComboBox.setPreferredSize(new Dimension(200, 30));
        contactosComboBox.addActionListener(e -> {
            exportarButton.setEnabled(contactosComboBox.getSelectedItem() != null);
        });

        contactPanel.add(contactLabel);
        contactPanel.add(contactosComboBox);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        exportarButton = new JButton("Exportar PDF");
        exportarButton.setFont(new Font("Arial", Font.BOLD, 14));
        exportarButton.setForeground(Color.WHITE);
        exportarButton.setBackground(new Color(0, 153, 51));
        exportarButton.setFocusPainted(false);
        exportarButton.setPreferredSize(new Dimension(150, 35));
        exportarButton.setEnabled(false);
        exportarButton.addActionListener(this::exportarPDF);

        cancelarSuscripcionButton = new JButton("Anular Suscripción");
        cancelarSuscripcionButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelarSuscripcionButton.setForeground(new Color(70, 70, 70));
        cancelarSuscripcionButton.setBackground(new Color(240, 240, 240));
        cancelarSuscripcionButton.setFocusPainted(false);
        cancelarSuscripcionButton.setPreferredSize(new Dimension(150, 35));
        cancelarSuscripcionButton.addActionListener(this::anularSuscripcion);

        buttonPanel.add(exportarButton);
        buttonPanel.add(cancelarSuscripcionButton);

        // Añadir componentes al panel de contenido
        contentPanel.add(titleLabel);
        contentPanel.add(contactPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(buttonPanel);

        // Añadir panel principal a la ventana
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        frame.add(mainPanel, BorderLayout.CENTER);
    }

    private void cargarContactos() {
        LinkedList<Contacto> contactos = Controlador.INSTANCE.getContactosUsuario();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        
        if (contactos != null && !contactos.isEmpty()) {
            //contactos.forEach(model::addElement);
        	for (Contacto contacto : contactos) {
				model.addElement(contacto.getNombre() + ":" + Controlador.INSTANCE.getTelefono(contacto));
			}
        } else {
            JOptionPane.showMessageDialog(frame, 
                "No tienes contactos disponibles", 
                "Información", JOptionPane.INFORMATION_MESSAGE);
        }
        
        contactosComboBox.setModel(model);
    }

    private void exportarPDF(ActionEvent e) {
    	String telefono = contactosComboBox.getSelectedItem().toString().split(":")[1];
    	String nombre = contactosComboBox.getSelectedItem().toString().split(":")[0];
    	Contacto contactoSeleccionado = null;
    	System.out.println("Telefono: " + telefono);
    	if (!telefono.equals("Grupo")) {
    		contactoSeleccionado = Controlador.INSTANCE.getContactoConMovil(telefono);
    	} else {
    		System.out.println("Grupo seleccionado: " + nombre);
    		contactoSeleccionado = Controlador.INSTANCE.getGrupoConNombre(nombre);
    	}
        
        System.out.println("Exportando chat de " + contactoSeleccionado.getNombre() + "...");
        if (contactoSeleccionado != null) {
            boolean exito = Controlador.INSTANCE.exportarChatPDF(contactoSeleccionado);
            
            if (exito) {
                JOptionPane.showMessageDialog(frame,
                    "Chat exportado correctamente como PDF",
                    "Exportación exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame,
                    "Error al exportar el chat a PDF",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void anularSuscripcion(ActionEvent e) {
        int confirmacion = JOptionPane.showConfirmDialog(frame,
            "¿Estás seguro de que quieres anular tu suscripción Premium?\n" +
            "Perderás acceso a todas las funciones exclusivas.",
            "Confirmar anulación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean exito = Controlador.INSTANCE.anularPremium();
            
            if (exito) {
                JOptionPane.showMessageDialog(frame,
                    "Suscripción Premium anulada correctamente",
                    "Anulación exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame,
                    "Error al anular la suscripción Premium",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPremiumActivo ventana = new VentanaPremiumActivo();
            ventana.mostrarVentana();
        });
    }
}