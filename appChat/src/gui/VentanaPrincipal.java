package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import controlador.Controlador;
import dominio.Contacto;
import dominio.Usuario;

public class VentanaPrincipal {
    
    private JFrame frame;
    private LinkedList<Contacto> contactos;
    private Usuario usuarioActual; // No sé hasta qué punto esto viola cosas

    public VentanaPrincipal() {
    	this.usuarioActual = Controlador.INSTANCE.getUsuarioActual();
        initialize();
    }

    public void mostrarVentana() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    public void initialize() {
        // Crear el marco principal
        frame = new JFrame("AppChat - Ventana principal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Panel superior
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField contactField = new JTextField(15);
        JButton sendButton = new JButton("Enviar");
        JButton searchButton = new JButton("Buscar");
        JButton contactsButton = new JButton("Contactos");
        JButton premiumButton = new JButton("Premium");
        //JLabel userLabel = new JLabel("Usuario Actual");
        JLabel userLabel = new JLabel(usuarioActual.getNombre()); //Igual hay que pedírselo al contolador SEGURAMENTE MIRAR ESTO
        
        //if (usuarioActual.getImagen() == null) {
	        ImageIcon icono = new ImageIcon("pfphotos/pfp.jpg");        
	        Image imgEscalada = icono.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
	        userLabel.setIcon(new ImageIcon(imgEscalada));
        /*} else {
        	String path = this.usuarioActual.getImagen();
        	URL url = null;
        	BufferedImage image = null;
        	try {
				url = new URL(path);
				image = ImageIO.read(url);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO: handle exception
			}
        	ImageIcon icono = new ImageIcon(image);        
	        Image imgEscalada = icono.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
	        userLabel.setIcon(new ImageIcon(imgEscalada));
        }*/
        // Acción para abrir la ventana de contactos
        contactsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaContactos ventanaContactos = new VentanaContactos();
                ventanaContactos.mostrarVentana();
            }
        });
        
        // Acción para abrir la ventana de búsqueda
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Buscador buscador = new Buscador();
                buscador.mostrarVentana();
            }
        });

        topPanel.add(contactField);
        topPanel.add(sendButton);
        topPanel.add(searchButton);
        topPanel.add(contactsButton);
        topPanel.add(premiumButton);
        topPanel.add(userLabel);

        // Panel izquierdo (lista de contactos/mensajes recientes)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        JScrollPane leftScrollPane = new JScrollPane(leftPanel);
        /*
        for (int i = 1; i <= 10; i++) {
            JPanel contactPanel = new JPanel(new BorderLayout());
            JLabel contactLabel = new JLabel("Contacto " + i);
            JLabel messagePreview = new JLabel("Mensaje...", JLabel.RIGHT);

            contactPanel.add(contactLabel, BorderLayout.WEST);
            contactPanel.add(messagePreview, BorderLayout.EAST);
            contactPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            leftPanel.add(contactPanel);
        }*/
        // Contactos reales
        contactos = Controlador.INSTANCE.getContactosUsuario();
        for (Contacto c : contactos) {
            JPanel contactPanel = new JPanel(new BorderLayout());
            JLabel contactLabel = new JLabel(c.getNombre());
            contactPanel.add(contactLabel, BorderLayout.WEST);
            contactPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            leftPanel.add(contactPanel);
        }

        // Panel derecho (chat con el contacto seleccionado)
        JPanel rightPanel = new JPanel(new BorderLayout());
        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);

        JPanel messagePanel = new JPanel(new BorderLayout());
        JTextField messageField = new JTextField();
        JButton sendMessageButton = new JButton("Enviar");

        messagePanel.add(messageField, BorderLayout.CENTER);
        messagePanel.add(sendMessageButton, BorderLayout.EAST);

        rightPanel.add(chatScrollPane, BorderLayout.CENTER);
        rightPanel.add(messagePanel, BorderLayout.SOUTH);

        // Añadir los paneles al marco
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(leftScrollPane, BorderLayout.WEST);
        frame.add(rightPanel, BorderLayout.CENTER);

        // Mostrar el marco
        frame.setVisible(true);
    }
}
