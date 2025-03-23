package gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//TODO: LLamadas al controlador
import javax.swing.JFrame;

public class VentanaContactos {

	private JFrame frame;
    private JList<String> listaContactos;
    private JList<String> listaGrupo;
    private DefaultListModel<String> modeloContactos;
    private DefaultListModel<String> modeloGrupo;
    private JButton btnAñadirContacto, btnAñadirGrupo, btnMoverDerecha, btnMoverIzquierda;

	/**
	 * Create the application.
	 */
	public VentanaContactos() {
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
		frame = new JFrame("Gestión de Contactos y Grupos");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Panel izquierdo: Lista de contactos
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setBorder(new TitledBorder("Lista Contactos"));
        modeloContactos = new DefaultListModel<>();
        modeloContactos.addElement("contacto1");
        modeloContactos.addElement("contacto2");
        modeloContactos.addElement("grupo1");
        modeloContactos.addElement("contacto3");
        modeloContactos.addElement("grupo2");
        listaContactos = new JList<>(modeloContactos);
        panelIzquierdo.add(new JScrollPane(listaContactos), BorderLayout.CENTER);
        btnAñadirContacto = new JButton("Añadir Contacto");
        panelIzquierdo.add(btnAñadirContacto, BorderLayout.SOUTH);

        // Panel derecho: Lista de grupo
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBorder(new TitledBorder("Grupo"));
        modeloGrupo = new DefaultListModel<>();
        listaGrupo = new JList<>(modeloGrupo);
        panelDerecho.add(new JScrollPane(listaGrupo), BorderLayout.CENTER);
        btnAñadirGrupo = new JButton("Añadir Grupo");
        panelDerecho.add(btnAñadirGrupo, BorderLayout.SOUTH);

        // Panel central: Botones para mover elementos
        JPanel panelCentral = new JPanel(new GridLayout(2, 1, 5, 5));
        btnMoverDerecha = new JButton(">>");
        btnMoverIzquierda = new JButton("<<");
        panelCentral.add(btnMoverDerecha);
        panelCentral.add(btnMoverIzquierda);

        // Agregar los paneles a la ventana principal
        frame.add(panelIzquierdo, BorderLayout.WEST);
        frame.add(panelCentral, BorderLayout.CENTER);
        frame.add(panelDerecho, BorderLayout.EAST);

        // Eventos de los botones
        btnMoverDerecha.addActionListener(e -> moverElementoDerecha());
        btnMoverIzquierda.addActionListener(e -> moverElementoIzquierda());
        btnAñadirContacto.addActionListener(e -> añadirContacto());
        btnAñadirGrupo.addActionListener(e -> añadirGrupo());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}
	
	private void moverElementoDerecha() {
        String seleccionado = listaContactos.getSelectedValue();
        if (seleccionado != null) {
            modeloContactos.removeElement(seleccionado);
            modeloGrupo.addElement(seleccionado);
        }
	}
	private void moverElementoIzquierda() {
        String seleccionado = listaGrupo.getSelectedValue();
        if (seleccionado != null) {
            modeloGrupo.removeElement(seleccionado);
            modeloContactos.addElement(seleccionado);
        }
    }

    private void añadirContacto() {
        String nuevoContacto = JOptionPane.showInputDialog(frame, "Ingrese el nombre del contacto:", "Añadir Contacto", JOptionPane.PLAIN_MESSAGE);
        if (nuevoContacto != null && !nuevoContacto.trim().isEmpty()) {
            modeloContactos.addElement(nuevoContacto.trim());
        }
    }

    private void añadirGrupo() {
        String nuevoGrupo = JOptionPane.showInputDialog(frame, "Ingrese el nombre del grupo:", "Añadir Grupo", JOptionPane.PLAIN_MESSAGE);
        if (nuevoGrupo != null && !nuevoGrupo.trim().isEmpty()) {
            modeloContactos.addElement(nuevoGrupo.trim());
        }
    }

}
