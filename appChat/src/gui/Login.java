package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JRadioButton;
import java.awt.BorderLayout;
import javax.swing.JButton;

public class Login {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("Botón de prueba");
		frame.getContentPane().add(rdbtnNewRadioButton, BorderLayout.CENTER);
		
		JButton btnNewButton = new JButton("JC Gay");
		frame.getContentPane().add(btnNewButton, BorderLayout.EAST);
		
		JButton btnNewButton_1 = new JButton("Pepo más gay");
		frame.getContentPane().add(btnNewButton_1, BorderLayout.SOUTH);
	}

}
