package gui;

import javax.swing.*;

import controlador.Controlador;

import java.awt.*;
import java.awt.event.ActionEvent;

public class VentanaPremium {

    private JFrame frame;

    public VentanaPremium() {
        initialize();
    }

    public void mostrarVentana() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void initialize() {
        // Configuración básica de la ventana
        frame = new JFrame("Actualización a Premium");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setMinimumSize(new Dimension(400, 250));
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(240, 240, 240));

        // Panel principal con bordes
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(255, 255, 255));

        // Panel de texto
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(new Color(255, 255, 255));

        // Título
        JLabel titleLabel = new JLabel("¡Actualiza a Premium!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        double precioBase = Controlador.INSTANCE.getPrecioBase();
        double precioDescuento = Controlador.INSTANCE.getPrecioDescuento();
        // Texto descriptivo
        String textoDescriptivo = String.format(
            "Si te conviertes en usuario premium podrás exportar cualquier chat en formato PDF por solo %.2f€ (precio base sin aplicar descuentos)\n\n" +
            "¿A qué esperas para empezar esta nueva experiencia?\n\n" +
            "El precio final (aplicando el mejor descuento) es de %.2f€.", precioBase ,precioDescuento
        );
        
        JTextArea descriptionText = new JTextArea(textoDescriptivo);
        descriptionText.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionText.setForeground(new Color(70, 70, 70));
        descriptionText.setWrapStyleWord(true);
        descriptionText.setLineWrap(true);
        descriptionText.setEditable(false);
        descriptionText.setFocusable(false);
        descriptionText.setBackground(new Color(255, 255, 255));
        descriptionText.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionText.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 10));

        // Añadir componentes al panel de texto
        textPanel.add(titleLabel);
        textPanel.add(descriptionText);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(new Color(255, 255, 255));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Botón de pagar
        JButton payButton = new JButton("¡Hazme Premium!");
        payButton.setFont(new Font("Arial", Font.BOLD, 14));
        payButton.setForeground(Color.WHITE);
        payButton.setBackground(new Color(0, 153, 51));
        payButton.setFocusPainted(false);
        payButton.setPreferredSize(new Dimension(180, 40));
        payButton.addActionListener(this::convertirPremium);
        payButton.setOpaque(true);
        payButton.setBorderPainted(false);

        // Botón de cancelar
        JButton cancelButton = new JButton("Cancelar");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelButton.setForeground(new Color(70, 70, 70));
        cancelButton.setBackground(new Color(240, 240, 240));
        cancelButton.setFocusPainted(false);
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.addActionListener(e -> frame.dispose());

        // Añadir botones al panel
        buttonPanel.add(payButton);
        buttonPanel.add(cancelButton);

        // Añadir paneles al panel principal
        mainPanel.add(textPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Añadir panel principal a la ventana
        frame.add(mainPanel, BorderLayout.CENTER);

        // Centrar ventana
        frame.setLocationRelativeTo(null);
    }

    private void convertirPremium(ActionEvent e) {
        // Lógica para convertir al usuario en premium
        boolean exito = Controlador.INSTANCE.convertirPremium();
        
        if (exito) {
            JOptionPane.showMessageDialog(frame,
                "¡Felicidades! Ahora eres un usuario Premium.",
                "Actualización exitosa",
                JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
        } else {
            JOptionPane.showMessageDialog(frame,
                "No se pudo completar la actualización a Premium. Por favor, inténtalo de nuevo más tarde.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPremium ventana = new VentanaPremium();
            ventana.mostrarVentana();
        });
    }
}