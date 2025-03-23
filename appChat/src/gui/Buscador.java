package gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Buscador {

	private JFrame frame;
	private JTextField txtTexto, txtTelefono, txtContacto;
    private JButton btnBuscar;
    private DefaultListModel<String> modeloMensajes;
    private JList<String> listaMensajes;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the application.
	 */
	public Buscador() {
		initialize();
	}

	public void mostrarVentana() {
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Buscador de Mensajes");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLayout(new BorderLayout());

     // Panel superior: Búsqueda
        JPanel panelBusqueda = new JPanel(new GridBagLayout());
        panelBusqueda.setBorder(new TitledBorder("Buscar"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo de texto
        JLabel lblTexto = new JLabel("Texto:");
        gbc.gridx = 0; gbc.gridy = 0; // Position at column 0, row 0
        panelBusqueda.add(lblTexto, gbc);

        txtTexto = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 0; // Position at column 1, row 0
        panelBusqueda.add(txtTexto, gbc);

        // Campo de teléfono
        JLabel lblTelefono = new JLabel("Teléfono:");
        gbc.gridx = 0; gbc.gridy = 1; // Position at column 0, row 1
        panelBusqueda.add(lblTelefono, gbc);

        txtTelefono = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1; // Position at column 1, row 1
        panelBusqueda.add(txtTelefono, gbc);

        // Campo de contacto
        JLabel lblContacto = new JLabel("Contacto:");
        gbc.gridx = 0; gbc.gridy = 2; // Position at column 0, row 2
        panelBusqueda.add(lblContacto, gbc);

        txtContacto = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 2; // Position at column 1, row 2
        panelBusqueda.add(txtContacto, gbc);

        // Botón Buscar
        btnBuscar = new JButton("Buscar");
        gbc.gridx = 1; gbc.gridy = 3; // Position at column 1, row 3
        panelBusqueda.add(btnBuscar, gbc);

        frame.add(panelBusqueda, BorderLayout.NORTH);

        // Panel central: Resultados
        JPanel panelResultados = new JPanel(new BorderLayout());
        panelResultados.setBorder(new TitledBorder("Resultados"));

        modeloMensajes = new DefaultListModel<>();
        listaMensajes = new JList<>(modeloMensajes);
        panelResultados.add(new JScrollPane(listaMensajes), BorderLayout.CENTER);

        frame.add(panelResultados, BorderLayout.CENTER);

        // Acción del botón Buscar
        btnBuscar.addActionListener(e -> realizarBusqueda());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}
	
	private void realizarBusqueda() {
        String texto = txtTexto.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String contacto = txtContacto.getText().trim();

        // Simulación de búsqueda (reemplazar con lógica real)
        modeloMensajes.clear();
        if (!texto.isEmpty()) {
            modeloMensajes.addElement("Mensaje que contiene: '" + texto + "'");
        }
        if (!telefono.isEmpty()) {
            modeloMensajes.addElement("Mensaje relacionado con teléfono: " + telefono);
        }
        if (!contacto.isEmpty()) {
            modeloMensajes.addElement("Mensaje relacionado con contacto: " + contacto);
        }
        if (modeloMensajes.isEmpty()) {
            modeloMensajes.addElement("No se encontraron mensajes.");
        }
    }

}
