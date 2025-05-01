package gui;

import controlador.Controlador;
import dominio.Contacto;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.LinkedList;

public class Buscador {
	private static Buscador instance;
    private JFrame frame;
    private JTextField txtTexto, txtTelefono;
    private JComboBox<String> cbContacto;
    private JButton btnBuscar;
    private DefaultListModel<String> modeloMensajes;
    private JList<String> listaMensajes;

    public Buscador() {
        initialize();
    }

    public static Buscador getInstance() {
		if (instance == null) {
			instance = new Buscador();
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
        frame = new JFrame("Buscador de Mensajes");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setMinimumSize(new Dimension(600, 400));
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(240, 240, 240));

        // Panel superior: Búsqueda
        JPanel panelBusqueda = new JPanel(new GridBagLayout());
        panelBusqueda.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder("Criterios de Búsqueda"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        panelBusqueda.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo de texto
        JLabel lblTexto = new JLabel("Texto del mensaje:");
        lblTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 0;
        panelBusqueda.add(lblTexto, gbc);

        txtTexto = new JTextField(20);
        txtTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        panelBusqueda.add(txtTexto, gbc);

        // Campo de teléfono
        JLabel lblTelefono = new JLabel("Teléfono relacionado:");
        lblTelefono.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.0;
        panelBusqueda.add(lblTelefono, gbc);

        txtTelefono = new JTextField(20);
        txtTelefono.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0;
        panelBusqueda.add(txtTelefono, gbc);

        // ComboBox de contactos
        JLabel lblContacto = new JLabel("Contacto:");
        lblContacto.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.0;
        panelBusqueda.add(lblContacto, gbc);

        cbContacto = new JComboBox<>();
        cbContacto.setFont(new Font("Arial", Font.PLAIN, 14));
        cargarContactos();
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weightx = 1.0;
        panelBusqueda.add(cbContacto, gbc);

        // Botón Buscar
        btnBuscar = new JButton("Buscar Mensajes");
        btnBuscar.setFont(new Font("Arial", Font.BOLD, 14));
        btnBuscar.setBackground(new Color(70, 130, 180));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        panelBusqueda.add(btnBuscar, gbc);

        frame.add(panelBusqueda, BorderLayout.NORTH);

        // Panel central: Resultados
        JPanel panelResultados = new JPanel(new BorderLayout());
        panelResultados.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder("Resultados de la Búsqueda"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        panelResultados.setBackground(Color.WHITE);

        modeloMensajes = new DefaultListModel<>();
        listaMensajes = new JList<>(modeloMensajes);
        listaMensajes.setFont(new Font("Arial", Font.PLAIN, 14));
        listaMensajes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(listaMensajes);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panelResultados.add(scrollPane, BorderLayout.CENTER);

        frame.add(panelResultados, BorderLayout.CENTER);

        // Acción del botón Buscar
        btnBuscar.addActionListener(this::realizarBusqueda);
    }
    
    private void cargarContactos() {
        LinkedList<Contacto> contactos = Controlador.INSTANCE.getContactosUsuario();
        for (Contacto contacto : contactos) {
            cbContacto.addItem(contacto.getNombre());
        }
    }
    
    private void realizarBusqueda(ActionEvent e) {
        String texto = txtTexto.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String contactoSeleccionado = (String) cbContacto.getSelectedItem();
        String contacto = contactoSeleccionado;
        // Validación
        if (texto.isEmpty() && telefono.isEmpty() && contacto == null) {
            JOptionPane.showMessageDialog(frame, 
                "Por favor, ingrese al menos un criterio de búsqueda", 
                "Búsqueda vacía", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Realizar búsqueda a través del controlador
        LinkedList<String> resultados = Controlador.INSTANCE.buscarMensajes(
            texto, telefono, contacto);

        // Mostrar resultados
        modeloMensajes.clear();
        
        if (resultados.isEmpty()) {
            modeloMensajes.addElement("No se encontraron mensajes con los criterios especificados.");
        } else {
            for (String mensaje : resultados) {
                modeloMensajes.addElement(mensaje);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Buscador b = new Buscador();
            b.mostrarVentana();
        });
    }
}