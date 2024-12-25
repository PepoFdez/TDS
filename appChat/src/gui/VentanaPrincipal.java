package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;


public class VentanaPrincipal {
	
	private JFrame frmVentanaPrincipal;

	private JPanel panelIzquierda;
    private JPanel panelDerecha;
    private JPanel panelSuperior;

    private JList<String> listaMensajes;
    private JTextArea areaMensajes;
    private JTextField campoMensaje;
    private JButton botonEnviar;

    private JTextField campoContacto;
    private JButton botonBuscar;
    private JButton botonGestionContactos;
    private JButton botonPremium;
    private JLabel etiquetaUsuario;
    private JLabel imagenUsuario;

    public VentanaPrincipal() {
        initialize();
    }


	public void mostrarVentana() {
		frmVentanaPrincipal.setLocationRelativeTo(null);
		frmVentanaPrincipal.setVisible(true);
	}
	
	public void initialize() {
		frmVentanaPrincipal.setTitle("AppChat - Ventana Principal");
        frmVentanaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmVentanaPrincipal.setSize(800, 600);
        frmVentanaPrincipal.setLayout(new BorderLayout());

        // Crear panel superior
        panelSuperior = new JPanel();
        panelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        campoContacto = new JTextField(20);
        botonEnviar = new JButton("Enviar");
        botonBuscar = new JButton("Buscar");
        botonGestionContactos = new JButton("Gestión de Contactos");
        botonPremium = new JButton("Hazte Premium");
        etiquetaUsuario = new JLabel("Usuario Actual");
        imagenUsuario = new JLabel(new ImageIcon("ruta/a/imagen/usuario.png")); // Cambiar la ruta

        panelSuperior.add(campoContacto);
        panelSuperior.add(botonEnviar);
        panelSuperior.add(botonBuscar);
        panelSuperior.add(botonGestionContactos);
        panelSuperior.add(botonPremium);
        panelSuperior.add(etiquetaUsuario);
        panelSuperior.add(imagenUsuario);

        // Crear panel izquierdo
        panelIzquierda = new JPanel();
        panelIzquierda.setLayout(new BorderLayout());
        panelIzquierda.setPreferredSize(new Dimension(250, 100));

        listaMensajes = new JList<>();
        JScrollPane scrollIzquierda = new JScrollPane(listaMensajes);
        panelIzquierda.add(scrollIzquierda, BorderLayout.CENTER);

        // Crear panel derecho
        panelDerecha = new JPanel();
        panelDerecha.setLayout(new BorderLayout());

        areaMensajes = new JTextArea();
        areaMensajes.setEditable(false);
        JScrollPane scrollDerecha = new JScrollPane(areaMensajes);

        JPanel panelEnviarMensaje = new JPanel();
        panelEnviarMensaje.setLayout(new BorderLayout());
        
        campoMensaje = new JTextField();
        JButton botonEnviarMensaje = new JButton("Enviar Mensaje");

        panelEnviarMensaje.add(campoMensaje, BorderLayout.CENTER);
        panelEnviarMensaje.add(botonEnviarMensaje, BorderLayout.EAST);

        panelDerecha.add(scrollDerecha, BorderLayout.CENTER);
        panelDerecha.add(panelEnviarMensaje, BorderLayout.SOUTH);

        // Agregar paneles a la ventana principal
        frmVentanaPrincipal.add(panelSuperior, BorderLayout.NORTH);
        frmVentanaPrincipal.add(panelIzquierda, BorderLayout.WEST);
        frmVentanaPrincipal.add(panelDerecha, BorderLayout.CENTER);
	}
	
}
