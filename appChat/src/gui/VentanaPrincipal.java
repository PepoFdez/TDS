package gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class VentanaPrincipal {

	private JFrame frmVentanaPrincipal;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal window = new VentanaPrincipal();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the application.
	 */
	public VentanaPrincipal() {
		initialize();
	}
	
	public void mostrarVentana() {
		frmVentanaPrincipal.setLocationRelativeTo(null);
		frmVentanaPrincipal.setVisible(true);
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmVentanaPrincipal = new JFrame();
		frmVentanaPrincipal.setTitle("AppChat - Ventana Principal");
		frmVentanaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel contentPane = (JPanel) frmVentanaPrincipal.getContentPane();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout());
		
		JLabel labelCompartirCoche = new JLabel("Bienvenidos a AppChat");
		labelCompartirCoche.setFont(new Font("Arial", Font.PLAIN, 30));
		labelCompartirCoche.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(labelCompartirCoche, BorderLayout.CENTER);

		frmVentanaPrincipal.pack();
	}

}
