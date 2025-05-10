package gui;

import dominio.Contacto;
import dominio.ContactoIndividual;
import dominio.Grupo;
import controlador.Controlador;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.LinkedList;

/**
 * Ventana para modificar un grupo existente, permitiendo cambiar su nombre y miembros.
 */
public class ModificarGrupo {
    private JFrame frame;
    private JList<ContactoIndividual> listaContactos;
    private JList<ContactoIndividual> listaGrupo;
    private DefaultListModel<ContactoIndividual> modeloContactos;
    private DefaultListModel<ContactoIndividual> modeloGrupo;
    private JTextField nombreGrupoField;
    private Grupo grupoOriginal;
    private JButton btnGuardar;

    /**
     * Constructor que recibe el grupo a modificar.
     * @param grupo El grupo que se va a modificar.
     */
    public ModificarGrupo(Contacto grupo) {
        if (!(grupo instanceof Grupo)) {
            throw new IllegalArgumentException("El contacto no es un grupo");
        }
        this.grupoOriginal = (Grupo) grupo;
        initialize();
        cargarDatosGrupo();
    }

    /**
     * Muestra la ventana de modificación de grupo.
     */
    public void mostrarVentana() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Inicializa los componentes de la interfaz gráfica.
     */
    private void initialize() {
        frame = new JFrame("Modificar Grupo");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 550); // Ventana más grande
        frame.setMinimumSize(new Dimension(700, 450));
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(240, 240, 240));

        // Panel superior para el nombre del grupo
        JPanel panelNombre = new JPanel(new BorderLayout(10, 10));
        panelNombre.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelNombre.setBackground(new Color(240, 240, 240));
        
        nombreGrupoField = new JTextField();
        nombreGrupoField.setFont(new Font("Arial", Font.PLAIN, 14));
        nombreGrupoField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        JLabel lblNombre = new JLabel("Nombre del grupo:");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        panelNombre.add(lblNombre, BorderLayout.NORTH);
        panelNombre.add(nombreGrupoField, BorderLayout.CENTER);

        // Panel principal para las listas de contactos
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPrincipal.setBackground(new Color(240, 240, 240));

        // Panel izquierdo: Contactos disponibles (no en el grupo)
        JPanel panelIzquierdo = new JPanel(new BorderLayout(5, 5));
        panelIzquierdo.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder("Contactos disponibles"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        panelIzquierdo.setPreferredSize(new Dimension(300, 400)); // Ancho fijo

        modeloContactos = new DefaultListModel<>();
        listaContactos = new JList<>(modeloContactos);
        listaContactos.setCellRenderer(new ContactoListCellRenderer());
        listaContactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaContactos.setFixedCellHeight(30); // Altura fija para cada elemento

        JScrollPane scrollContactos = new JScrollPane(listaContactos);
        scrollContactos.setPreferredSize(new Dimension(300, 400));
        panelIzquierdo.add(scrollContactos, BorderLayout.CENTER);

        // Panel derecho: Miembros actuales del grupo
        JPanel panelDerecho = new JPanel(new BorderLayout(5, 5));
        panelDerecho.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder("Miembros del grupo"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        panelDerecho.setPreferredSize(new Dimension(300, 400)); // Ancho fijo

        modeloGrupo = new DefaultListModel<>();
        listaGrupo = new JList<>(modeloGrupo);
        listaGrupo.setCellRenderer(new ContactoListCellRenderer());
        listaGrupo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaGrupo.setFixedCellHeight(30); // Altura fija para cada elemento

        JScrollPane scrollGrupo = new JScrollPane(listaGrupo);
        scrollGrupo.setPreferredSize(new Dimension(300, 400));
        panelDerecho.add(scrollGrupo, BorderLayout.CENTER);

        // Panel central: Botones para mover contactos
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        panelCentral.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        JButton btnMoverDerecha = new JButton(">");
        btnMoverDerecha.setFont(new Font("Arial", Font.BOLD, 14));
        btnMoverDerecha.setPreferredSize(new Dimension(60, 35));
        btnMoverDerecha.setToolTipText("Añadir al grupo");
        btnMoverDerecha.setBackground(new Color(220, 220, 220));
        btnMoverDerecha.addActionListener(this::moverElementoDerecha);
        panelCentral.add(btnMoverDerecha, gbc);

        JButton btnMoverIzquierda = new JButton("<");
        btnMoverIzquierda.setFont(new Font("Arial", Font.BOLD, 14));
        btnMoverIzquierda.setPreferredSize(new Dimension(60, 35));
        btnMoverIzquierda.setToolTipText("Quitar del grupo");
        btnMoverIzquierda.setBackground(new Color(220, 220, 220));
        btnMoverIzquierda.addActionListener(this::moverElementoIzquierda);
        panelCentral.add(btnMoverIzquierda, gbc);

        // Panel inferior: Botón de guardar
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panelInferior.setBackground(new Color(240, 240, 240));
        
        btnGuardar = new JButton("Guardar cambios");
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 12));
        btnGuardar.setPreferredSize(new Dimension(150, 35));
        btnGuardar.setBackground(new Color(0, 102, 204));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(this::guardarCambios);
        panelInferior.add(btnGuardar);

        // Agregar los paneles a la ventana principal
        panelPrincipal.add(panelIzquierdo, BorderLayout.WEST);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(panelDerecho, BorderLayout.EAST);

        frame.add(panelNombre, BorderLayout.NORTH);
        frame.add(panelPrincipal, BorderLayout.CENTER);
        frame.add(panelInferior, BorderLayout.SOUTH);
    }

    /**
     * Carga los datos del grupo en la interfaz.
     */
    private void cargarDatosGrupo() {
        // Establecer el nombre actual del grupo
        nombreGrupoField.setText(grupoOriginal.getNombre());

        // Obtener todos los contactos individuales del usuario
        LinkedList<ContactoIndividual> todosContactos = Controlador.INSTANCE.getContactosIndividualesUsuario();
        
        // Obtener los miembros actuales del grupo
        LinkedList<Contacto> miembros = Controlador.INSTANCE.getMiembrosGrupo(grupoOriginal);

        // Separar los contactos que están en el grupo de los que no
        for (ContactoIndividual contacto : todosContactos) {
            if (miembros.contains(contacto)) {
                modeloGrupo.addElement(contacto);
            } else {
                modeloContactos.addElement(contacto);
            }
        }
    }

    /**
     * Maneja el evento de mover un contacto a la lista del grupo.
     */
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

    /**
     * Maneja el evento de quitar un contacto del grupo.
     */
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

    /**
     * Maneja el evento de guardar los cambios realizados en el grupo.
     */
    private void guardarCambios(ActionEvent e) {
        String nuevoNombre = nombreGrupoField.getText().trim();
        
        if (nuevoNombre.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "El nombre del grupo no puede estar vacío",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (modeloGrupo.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "El grupo debe tener al menos un miembro",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Recopilar los nuevos miembros del grupo
        LinkedList<ContactoIndividual> nuevosMiembros = new LinkedList<>();
        for (int i = 0; i < modeloGrupo.size(); i++) {
            nuevosMiembros.add(modeloGrupo.getElementAt(i));
        }

        // Llamar al controlador para actualizar el grupo
        boolean exito = Controlador.INSTANCE.modificarGrupo(
                grupoOriginal, 
                nuevoNombre, 
                nuevosMiembros);

        if (exito) {
            JOptionPane.showMessageDialog(frame,
                    "Grupo modificado exitosamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Error al modificar el grupo",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Renderer personalizado para mostrar los contactos en las listas.
     */
    private static class ContactoListCellRenderer extends DefaultListCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                    boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof ContactoIndividual) {
                ContactoIndividual contacto = (ContactoIndividual) value;
                setText(contacto.getNombre());
                setFont(new Font("Arial", Font.PLAIN, 13));
                
                // Estilo para elementos seleccionados
                if (isSelected) {
                    setBackground(new Color(200, 220, 255));
                    setForeground(Color.BLACK);
                }
                
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