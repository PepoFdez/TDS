package gui;

import tds.BubbleText;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import controlador.Controlador;
import dominio.Contacto;

public class VentanaPrincipal {
    
    private JFrame frame;
    private LinkedList<Contacto> contactos;
    private JPanel leftPanel;
    private JPanel chatPanel; // Cambiado de JTextArea a JPanel
    private JScrollPane chatScrollPane;
    private JPanel rightPanel;
    private JPanel contactInfoPanel;
    private JLabel currentContactLabel;
    private JLabel currentContactImage;
    private JTextField messageField;
    private Contacto contactoSeleccionado;
    private JButton emojiButton;

    public VentanaPrincipal() {
        BubbleText.noZoom(); // Desactivar zoom automático para HiDPI
        initialize();
    }
    
    public void mostrarVentana() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private void initialize() {
        // Crear el marco principal
        frame = new JFrame("AppChat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.getContentPane().setLayout(new BorderLayout());

        // Panel superior con barra de herramientas
        JPanel topPanel = createTopPanel();
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);

        // Panel principal (izquierda + derecha)
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Panel izquierdo (lista de contactos)
        leftPanel = createContactsPanel();
        JScrollPane leftScrollPane = new JScrollPane(leftPanel);
        leftScrollPane.setPreferredSize(new Dimension(250, 0));
        
        // Panel derecho (chat)
        rightPanel = createChatPanel();
        
        mainPanel.add(leftScrollPane, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        
        // Cargar datos iniciales
        loadContacts();
    }
    
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Campo de búsqueda
        JTextField searchField = new JTextField(20);
        searchField.setToolTipText("Buscar contactos");
        
        // Botón de búsqueda (con icono)
        JButton searchContactButton = new JButton("🔍");
        searchContactButton.setToolTipText("Buscar contactos");
        searchContactButton.addActionListener(e -> buscarContactos(searchField.getText()));
        
        // Botón de buscar mensajes
        JButton searchMessagesButton = new JButton("Buscar mensajes");
        searchMessagesButton.addActionListener(e -> {
            Buscador buscador = new Buscador();
            buscador.mostrarVentana();
        });
        
        // Botón de contactos
        JButton contactsButton = new JButton("Contactos");
        contactsButton.addActionListener(e -> {
            VentanaContactos ventanaContactos = new VentanaContactos();
            ventanaContactos.mostrarVentana();
        });
        
        // Botón premium
        JButton premiumButton = new JButton("Premium");
        premiumButton.setForeground(new Color(0, 102, 204));
        
        // Panel de usuario actual
        JPanel userPanel = createUserPanel();
        
        // Añadir componentes al panel superior
        topPanel.add(searchField);
        topPanel.add(searchContactButton);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(searchMessagesButton);
        topPanel.add(contactsButton);
        topPanel.add(premiumButton);
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(userPanel);
        
        return topPanel;
    }
    
    private JPanel createUserPanel() {
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        
        String nombreUsuario = Controlador.INSTANCE.getNombreUsuario();
        JLabel userNameLabel = new JLabel(nombreUsuario);
        userNameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Cargar imagen de perfil
        ImageIcon icono = new ImageIcon("phpphotos/pfp.jpg");
        Image imgEscalada = icono.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel userImage = new JLabel(new ImageIcon(imgEscalada));
        
        userPanel.add(userNameLabel);
        userPanel.add(userImage);
        
        return userPanel;
    }
    
    private JPanel createContactsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return panel;
    }
    
    private JPanel createChatPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel superior con información del contacto
        contactInfoPanel = new JPanel(new BorderLayout());
        contactInfoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        
        currentContactLabel = new JLabel("Selecciona un contacto");
        currentContactLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        currentContactImage = new JLabel();
        currentContactImage.setPreferredSize(new Dimension(40, 40));
        
        JPanel contactPanel = new JPanel(new BorderLayout(10, 0));
        contactPanel.add(currentContactLabel, BorderLayout.CENTER);
        contactPanel.add(currentContactImage, BorderLayout.EAST);
        
        contactInfoPanel.add(contactPanel, BorderLayout.NORTH);
        panel.add(contactInfoPanel, BorderLayout.NORTH);
        
        // Área de chat con BubbleText
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        chatPanel.setBackground(Color.WHITE);
        
        chatScrollPane = new JScrollPane(chatPanel);
        chatScrollPane.setBorder(null);
        panel.add(chatScrollPane, BorderLayout.CENTER);
        
        // Panel de envío de mensajes
        JPanel messagePanel = new JPanel(new BorderLayout(5, 5));
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel para botón de emoticonos y campo de texto
        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        
        emojiButton = new JButton("😊");
        emojiButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        emojiButton.setPreferredSize(new Dimension(40, 30));
        emojiButton.addActionListener(this::mostrarSelectorEmojis);
        
        inputPanel.add(emojiButton, BorderLayout.WEST);
        
        messageField = new JTextField();
        messageField.setToolTipText("Escribe tu mensaje aquí");
        inputPanel.add(messageField, BorderLayout.CENTER);
        
        JButton sendButton = new JButton("Enviar");
        sendButton.setPreferredSize(new Dimension(80, 0));
        sendButton.addActionListener(this::enviarMensaje);
        
        messagePanel.add(inputPanel, BorderLayout.CENTER);
        messagePanel.add(sendButton, BorderLayout.EAST);
        panel.add(messagePanel, BorderLayout.SOUTH);
        
        // Mensaje inicial
        mostrarMensajeInicial();
        
        return panel;
    }

    private void mostrarMensajeInicial() {
        chatPanel.removeAll();
        JLabel initLabel = new JLabel("Selecciona un contacto para comenzar a chatear");
        initLabel.setHorizontalAlignment(SwingConstants.CENTER);
        initLabel.setForeground(Color.GRAY);
        chatPanel.add(initLabel);
        chatPanel.revalidate();
        chatPanel.repaint();
    }

    private void seleccionarContacto(Contacto contacto) {
        this.contactoSeleccionado = contacto;
        
        // Actualizar panel de información del contacto
        currentContactLabel.setText(contacto.getNombre());
        
        Image imgEscalada = getImagenContactoEscalada(contacto);
        if (imgEscalada != null) {
            currentContactImage.setIcon(new ImageIcon(imgEscalada));
        } else {
            ImageIcon icono = new ImageIcon("phpphotos/pfp.jpg");
            imgEscalada = icono.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            currentContactImage.setIcon(new ImageIcon(imgEscalada));
        }
        
        // Cargar mensajes del chat
        cargarMensajes(contacto);
    }
    
    private void cargarMensajes(Contacto contacto) {
        chatPanel.removeAll();
        
        List<Object> textoMensajes = Controlador.INSTANCE.getContenidoMensajes(contacto);
        List<String> infoMensajes = Controlador.INSTANCE.getInfoMensajes(contacto);
        
        if (textoMensajes.isEmpty()) {
            JLabel emptyLabel = new JLabel("No hay mensajes con este contacto");
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            emptyLabel.setForeground(Color.GRAY);
            chatPanel.add(emptyLabel);
        } else {
            for (int i = 0; i < textoMensajes.size(); i++) {
                Object texto = textoMensajes.get(i);
                String[] info = infoMensajes.get(i).split("\\|");
                String fechaHora = info[0];
                String tipo = info[1];
                
                Color color = tipo.equals("SENT") ? new Color(220, 248, 198) : Color.WHITE;
                String nombre = tipo.equals("SENT") ? "Tú" : contacto.getNombre();
                int tipoBurbuja = tipo.equals("SENT") ? BubbleText.SENT : BubbleText.RECEIVED;
                
                // Crear burbuja de chat
                BubbleText burbuja = new BubbleText(
                    chatPanel, 
                    texto.toString(), //MIRA ESTO QUE LO HE PUESTO PARA QUE NO DE ERROR
                    color, 
                    nombre + " - " + fechaHora, 
                    tipoBurbuja,
                    14 // Tamaño de fuente
                );
                chatPanel.add(burbuja);
            }
        }
        
        chatPanel.revalidate();
        chatPanel.repaint();
        
        // Mover el scroll al final
        chatScrollPane.getVerticalScrollBar().setValue(chatScrollPane.getVerticalScrollBar().getMaximum());
    }
    
    private void enviarMensaje(ActionEvent e) {
        if (contactoSeleccionado == null) {
            JOptionPane.showMessageDialog(frame, 
                "Selecciona un contacto primero", 
                "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String contenido = messageField.getText().trim();
        if (contenido.isEmpty()) {
            return;
        }
        
        Controlador.INSTANCE.enviarMensaje(contactoSeleccionado.getId(), contenido);
        
        // Actualizar el chat
        cargarMensajes(contactoSeleccionado);
        messageField.setText("");
    }
    
    private void mostrarSelectorEmojis(ActionEvent e) {
        if (contactoSeleccionado == null) {
            JOptionPane.showMessageDialog(frame, 
                "Selecciona un contacto primero", 
                "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFrame emojiFrame = new JFrame("Seleccionar Emoji");
        emojiFrame.setSize(400, 300);
        emojiFrame.setLayout(new BorderLayout());
        
        JPanel emojiPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Mostrar los primeros 12 emojis como ejemplo
        for (int i = 0; i < 12; i++) {
            final int emojiId = i; // Crear una variable final para usar en el lambda
            JButton emojiBtn = new JButton();
            emojiBtn.setIcon(BubbleText.getEmoji(emojiId));
            emojiBtn.addActionListener(ev -> {
                enviarEmoji(emojiId);
                emojiFrame.dispose();
            });
            
            gbc.gridx = emojiId % 4;
            gbc.gridy = emojiId / 4;
            emojiPanel.add(emojiBtn, gbc);
        }
        
        emojiFrame.add(new JScrollPane(emojiPanel), BorderLayout.CENTER);
        emojiFrame.setLocationRelativeTo(frame);
        emojiFrame.setVisible(true);
    }
    
    private void enviarEmoji(int emojiId) {
        Color color = new Color(220, 248, 198); // Color para mensajes enviados
        BubbleText burbuja = new BubbleText(
            chatPanel, 
            emojiId, 
            color, 
            "Tú", 
            BubbleText.SENT,
            24 // Tamaño del emoji
        );
        chatPanel.add(burbuja);
        
        // Enviar el emoji al controlador
        Controlador.INSTANCE.enviarEmoji(contactoSeleccionado.getId(), emojiId);
        
        chatPanel.revalidate();
        chatPanel.repaint();
        chatScrollPane.getVerticalScrollBar().setValue(chatScrollPane.getVerticalScrollBar().getMaximum());
    }
    
    private void loadContacts() {
        contactos = Controlador.INSTANCE.getContactosUsuario();
        leftPanel.removeAll();
        
        if (contactos.isEmpty()) {
            JLabel emptyLabel = new JLabel("No tienes contactos");
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            leftPanel.add(emptyLabel);
        } else {
            for (Contacto contacto : contactos) {
                JPanel contactPanel = createContactPanel(contacto);
                leftPanel.add(contactPanel);
            }
        }
        
        leftPanel.revalidate();
        leftPanel.repaint();
    }
    
    private JPanel createContactPanel(Contacto contacto) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        panel.setBackground(Color.WHITE);
        
        // Configurar el hover effect
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(245, 245, 245));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(Color.WHITE);
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarContacto(contacto);
            }
        });
        
        // Imagen del contacto
        JLabel imageLabel = new JLabel();
        
        Image imgEscalada = getImagenContactoEscalada(contacto);
        if(imgEscalada != null) imageLabel.setIcon(new ImageIcon(imgEscalada));
        else {
        	ImageIcon icono = new ImageIcon("phpphotos/pfp.jpg");
            imgEscalada = icono.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(imgEscalada));
        }
        
        
        // Nombre del contacto
        JLabel nameLabel = new JLabel(contacto.getNombre());
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Último mensaje (podrías obtenerlo del controlador)
        JLabel lastMsgLabel = new JLabel("Último mensaje...");
        lastMsgLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        lastMsgLabel.setForeground(Color.GRAY);
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(nameLabel);
        textPanel.add(lastMsgLabel);
        
        panel.add(imageLabel, BorderLayout.WEST);
        panel.add(textPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private Image getImagenContactoEscalada(Contacto contacto) {
    	ImageIcon icono;
        String URLimagenContacto = Controlador.INSTANCE.getURLImagenContacto(contacto);
        try {
        	if (URLimagenContacto != null && !URLimagenContacto.isEmpty()) {
                URL url = new URL(URLimagenContacto);
                icono = new ImageIcon(url);
            } else {
                icono = new ImageIcon("phpphotos/pfp.jpg");
            }
            Image imgEscalada = icono.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            
    		return imgEscalada;
		} catch (Exception e) {
			System.err.println("Error al cargar la imagen del contacto: " + e.getMessage());
			return  null;
		}
        
	}
    
    /*private void enviarMensaje() {
        if (contactoSeleccionado == null || messageField.getText().trim().isEmpty()) {
            return;
        }
        
        String contenido = messageField.getText().trim();
        Controlador.INSTANCE.enviarMensaje(contactoSeleccionado.getId(), contenido);
        
        // Actualizar el chat
        cargarMensajes(contactoSeleccionado);
        messageField.setText("");
    }*/
    
    private void buscarContactos(String textoBusqueda) {
        if (textoBusqueda.trim().isEmpty()) {
            loadContacts();
            return;
        }
        
        LinkedList<Contacto> resultados = Controlador.INSTANCE.buscarContactos(textoBusqueda);
        leftPanel.removeAll();
        
        if (resultados.isEmpty()) {
            JLabel emptyLabel = new JLabel("No se encontraron contactos");
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            leftPanel.add(emptyLabel);
        } else {
            for (Contacto contacto : resultados) {
                JPanel contactPanel = createContactPanel(contacto);
                leftPanel.add(contactPanel);
            }
        }
        
        leftPanel.revalidate();
        leftPanel.repaint();
    }
}