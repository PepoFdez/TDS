package gui;

import dominio.ContactoIndividual;
import controlador.Controlador;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.LinkedList;

public class VentanaContactos {
	
	private static VentanaContactos instance;
    private JFrame frame;
    private JList<ContactoIndividual> listaContactos;
    private JList<ContactoIndividual> listaGrupo;
    private DefaultListModel<ContactoIndividual> modeloContactos;
    private DefaultListModel<ContactoIndividual> modeloGrupo;
    private JButton btnAñadirContacto, btnAñadirGrupo, btnMoverDerecha, btnMoverIzquierda;

    public VentanaContactos() {
        initialize();
        cargarContactos();
    }

    public static VentanaContactos getInstance() {
        if (instance == null) {
            instance = new VentanaContactos();
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
        frame = new JFrame("Gestión de Contactos y Grupos");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(650, 450);
        frame.setMinimumSize(new Dimension(500, 350));
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(240, 240, 240));

        // Panel izquierdo: Lista de contactos
        JPanel panelIzquierdo = new JPanel(new BorderLayout(5, 5));
        panelIzquierdo.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder("Lista de Contactos"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        modeloContactos = new DefaultListModel<>();
        listaContactos = new JList<>(modeloContactos);
        listaContactos.setCellRenderer(new ContactoListCellRenderer());
        listaContactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        panelIzquierdo.add(new JScrollPane(listaContactos), BorderLayout.CENTER);
        
        btnAñadirContacto = new JButton("Añadir Contacto");
        // Solución: Usar iconos por defecto de Swing o manejar la ausencia de imágenes
        try {
            btnAñadirContacto.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
        } catch (Exception e) {
            // Si falla, simplemente no ponemos icono
        }
        panelIzquierdo.add(createButtonPanel(btnAñadirContacto), BorderLayout.SOUTH);

        // Panel derecho: Lista de grupo
        JPanel panelDerecho = new JPanel(new BorderLayout(5, 5));
        panelDerecho.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder("Miembros del Grupo"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        modeloGrupo = new DefaultListModel<>();
        listaGrupo = new JList<>(modeloGrupo);
        listaGrupo.setCellRenderer(new ContactoListCellRenderer());
        listaGrupo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        panelDerecho.add(new JScrollPane(listaGrupo), BorderLayout.CENTER);
        
        btnAñadirGrupo = new JButton("Crear Grupo");
        try {
            btnAñadirGrupo.setIcon(UIManager.getIcon("OptionPane.questionIcon"));
        } catch (Exception e) {
            // Si falla, simplemente no ponemos icono
        }
        panelDerecho.add(createButtonPanel(btnAñadirGrupo), BorderLayout.SOUTH);

        // Panel central: Botones para mover elementos
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        
        btnMoverDerecha = new JButton(">");
        btnMoverDerecha.setPreferredSize(new Dimension(50, 30));
        btnMoverDerecha.setToolTipText("Añadir al grupo");
        panelCentral.add(btnMoverDerecha, gbc);
        
        btnMoverIzquierda = new JButton("<");
        btnMoverIzquierda.setPreferredSize(new Dimension(50, 30));
        btnMoverIzquierda.setToolTipText("Quitar del grupo");
        panelCentral.add(btnMoverIzquierda, gbc);

        // Agregar los paneles a la ventana principal
        frame.add(panelIzquierdo, BorderLayout.WEST);
        frame.add(panelCentral, BorderLayout.CENTER);
        frame.add(panelDerecho, BorderLayout.EAST);

        // Eventos de los botones
        btnMoverDerecha.addActionListener(this::moverElementoDerecha);
        btnMoverIzquierda.addActionListener(this::moverElementoIzquierda);
        btnAñadirContacto.addActionListener(this::añadirContacto);
        btnAñadirGrupo.addActionListener(this::añadirGrupo);
    }
    
    private JPanel createButtonPanel(JButton button) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel.setBackground(null);
        panel.add(button);
        return panel;
    }
    
    public void cargarContactosExternamente() {
        cargarContactos();
    }

    private void cargarContactos() {
        LinkedList<ContactoIndividual> contactos = Controlador.INSTANCE.getContactosIndividualesUsuario();
        modeloContactos.clear();
        
        if (contactos == null || contactos.isEmpty()) {
        } else {
            contactos.forEach(modeloContactos::addElement);
        }
    }

    private void moverElementoDerecha(ActionEvent e) {
        ContactoIndividual seleccionado = listaContactos.getSelectedValue();
        if (seleccionado != null) {
            modeloContactos.removeElement(seleccionado);
            modeloGrupo.addElement(seleccionado);
        } else {
            JOptionPane.showMessageDialog(frame, 
                "Selecciona un contacto primero", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void moverElementoIzquierda(ActionEvent e) {
        ContactoIndividual seleccionado = listaGrupo.getSelectedValue();
        if (seleccionado != null) {
            modeloGrupo.removeElement(seleccionado);
            modeloContactos.addElement(seleccionado);
        } else {
            JOptionPane.showMessageDialog(frame, 
                "Selecciona un contacto del grupo primero", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void añadirContacto(ActionEvent e) {
        AñadirContacto.getInstance().mostrarVentana();
    }

    private void añadirGrupo(ActionEvent e) {
        String nombreGrupo = JOptionPane.showInputDialog(
            frame, 
            "Introduce el nombre del grupo:", 
            "Crear Grupo", 
            JOptionPane.PLAIN_MESSAGE);
        
        if (nombreGrupo != null && !nombreGrupo.trim().isEmpty()) {
            if (modeloGrupo.isEmpty()) {
                JOptionPane.showMessageDialog(frame, 
                    "Debes añadir al menos un contacto al grupo", 
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            LinkedList<ContactoIndividual> miembros = new LinkedList<>();
            for (int i = 0; i < modeloGrupo.size(); i++) {
                miembros.add(modeloGrupo.getElementAt(i));
            }
            
            boolean creado = Controlador.INSTANCE.crearGrupo(nombreGrupo.trim(), miembros);
            if (creado) {
                JOptionPane.showMessageDialog(frame, 
                    "Grupo creado exitosamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                modeloGrupo.clear();
                cargarContactos(); // Recargar contactos
            } else {
                JOptionPane.showMessageDialog(frame, 
                    "Error al crear el grupo", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private static class ContactoListCellRenderer extends DefaultListCellRenderer {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                                                    boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof ContactoIndividual) {
                ContactoIndividual contacto = (ContactoIndividual) value;
                setText(contacto.getNombre());
                // Opcional: Añadir icono genérico si no hay imagen específica
                try {
                    setIcon(UIManager.getIcon("OptionPane.informationIcon"));
                } catch (Exception e) {
                    // No hacer nada si no se puede cargar el icono
                }
            }
            
            return this;
        }
    }
}