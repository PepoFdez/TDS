package gui;

import javax.swing.*;

import controlador.Controlador;

import java.awt.*;
import java.net.URL;
import java.text.SimpleDateFormat;

public class InfoUsuario {
    private JFrame frame;

    public void mostrarVentana() {
        frame = new JFrame("Información de Usuario");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 350);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Línea 1: Imagen de perfil a la izquierda y nombre y apellidos a la derecha
        JPanel line1 = new JPanel(new BorderLayout(10, 0));
        
        // Cargar imagen de perfil
        String URLimagenContacto = Controlador.INSTANCE.getURLImagenUsuario();
        ImageIcon icono = new ImageIcon(URLimagenContacto);
        Image imgEscalada = getImagenContactoEscalada(URLimagenContacto, icono);
        JLabel profileImage = new JLabel(new ImageIcon(imgEscalada));
        
        // Nombre y apellidos
        String nombreCompleto = Controlador.INSTANCE.getNombreUsuario() + " " + Controlador.INSTANCE.getApellidosUsuario();
        JLabel nameLabel = new JLabel(nombreCompleto);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));

        line1.add(profileImage, BorderLayout.WEST);
        line1.add(nameLabel, BorderLayout.CENTER);
        mainPanel.add(line1);

        // Espacio entre líneas
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Línea 2: Saludo
        String saludo = Controlador.INSTANCE.getSaludoUsuario();
        JLabel greetingLabel = new JLabel(saludo);
        greetingLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(greetingLabel);

        // Línea 3: Número de teléfono
        JLabel phoneLabel = new JLabel("Teléfono: " + Controlador.INSTANCE.getTelefonoUsuario());
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(phoneLabel);

        // Línea 4: Email
        JLabel emailLabel = new JLabel("Email: " + Controlador.INSTANCE.getEmailUsuario());
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(emailLabel);

        // Línea 5: Fecha de nacimiento y creación de cuenta
        JPanel line5 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        
        // Formatear fechas
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fechaNacimiento = dateFormat.format(Controlador.INSTANCE.getFechaNacimientoUsuario());
        String fechaCreacion = dateFormat.format(Controlador.INSTANCE.getFechaCreacionCuentaUsuario());
        
        JLabel birthLabel = new JLabel("Nacimiento: " + fechaNacimiento);
        birthLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel creationLabel = new JLabel("Miembro desde: " + fechaCreacion);
        creationLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        line5.add(birthLabel);
        line5.add(creationLabel);
        mainPanel.add(line5);

        // Botón de cerrar
        JButton closeButton = new JButton("Cerrar");
        closeButton.addActionListener(e -> frame.dispose());
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(closeButton);

        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    private Image getImagenContactoEscalada(String URLimagenContacto, ImageIcon icono) {
		try {
			if (URLimagenContacto != null && !URLimagenContacto.isEmpty()) {
				URL url = new URL(URLimagenContacto);
				icono = new ImageIcon(url);
			} else {
				icono = new ImageIcon("phphotos/pfp.jpg");
			}
			Image imgEscalada = icono.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);

			return imgEscalada;
		} catch (Exception e) {
			System.err.println("Error al cargar la imagen del contacto: " + e.getMessage());
			return null;
		}

	}
}