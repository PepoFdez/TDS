package gui;

import tds.BubbleText;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import controlador.Controlador;
import dominio.Contacto;

/**
 * Panel de chat que muestra los mensajes entre el usuario y un contacto.
 * Admite tanto mensajes de texto como emojis.
 */
public class ChatPanel extends JPanel implements Scrollable {

    private static final long serialVersionUID = 1L;

    /** Formateador de fecha y hora para los mensajes */
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Crea un nuevo panel de chat con un mensaje inicial.
     */
    public ChatPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        mostrarMensajeInicial();
    }

    /**
     * Muestra un mensaje inicial cuando no se ha seleccionado ningún contacto.
     */
    public void mostrarMensajeInicial() {
        removeAll();
        add(Box.createVerticalGlue());
        JLabel lblInicio = new JLabel("Selecciona un contacto para iniciar el chat");
        lblInicio.setFont(new Font("Arial", Font.PLAIN, 16));
        lblInicio.setAlignmentX(CENTER_ALIGNMENT);
        add(lblInicio);
        add(Box.createVerticalGlue());
        revalidate();
        repaint();
    }

    /**
     * Muestra el historial de mensajes del contacto seleccionado.
     * @param contacto El contacto con el que se desea mostrar la conversación.
     */
    public void mostrarChat(Contacto contacto) {
        removeAll();
        List<BubbleText> mensajes = convertirMensajesABurbujas(contacto);
        mensajes.forEach(this::add);
        add(Box.createVerticalGlue());
        revalidate();
        repaint();
        javax.swing.SwingUtilities.invokeLater(this::scrollToBottom);
    }

    /**
     * Envía y muestra un mensaje de texto en el chat.
     * @param texto Texto del mensaje enviado.
     */
    public void enviarMensaje(String texto) {
        String fecha = LocalDateTime.now().format(formatter);
        BubbleText burbuja = new BubbleText(this, texto, 
            new Color(220, 248, 198), // Color para mensajes enviados
            "Tú - " + fecha, 
            BubbleText.SENT, 
            14
        );
        add(burbuja);
        scrollToBottom();
    }

    /**
     * Envía y muestra un emoji en el chat.
     * @param emojiId Identificador del emoji enviado.
     */
    public void enviarEmoji(int emojiId) {
        String fecha = LocalDateTime.now().format(formatter);
        BubbleText burbuja = new BubbleText(this, emojiId, 
            new Color(220, 248, 198), // Color para mensajes enviados
            "Tú - " + fecha, 
            BubbleText.SENT, 
            24
        );
        add(burbuja);
        scrollToBottom();
    }

    /**
     * Convierte los mensajes de un contacto en burbujas de texto o emoji para el chat.
     * @param contacto Contacto cuyos mensajes se van a mostrar.
     * @return Lista de objetos BubbleText representando los mensajes.
     */
    private List<BubbleText> convertirMensajesABurbujas(Contacto contacto) {
        List<Object> textoMensajes = Controlador.INSTANCE.getContenidoMensajes(contacto);
        List<String> infoMensajes = Controlador.INSTANCE.getInfoMensajes(contacto);
        List<BubbleText> burbujas = textoMensajes.stream()
            .map(texto -> {
                String[] info = infoMensajes.get(textoMensajes.indexOf(texto)).split(utils.Utils.SEPARATOR);
                String fechaHora = info[0];
                Integer tipo = Integer.parseInt(info[1]);

                Color color = tipo.equals(BubbleText.SENT) ? new Color(220, 248, 198) : Color.LIGHT_GRAY;
                String nombre = tipo.equals(BubbleText.SENT) ? "Tú" : contacto.getNombre();
                int tipoBurbuja = tipo.equals(BubbleText.SENT) ? BubbleText.SENT : BubbleText.RECEIVED;

                if (texto instanceof Integer) {
                    return new BubbleText(this, (Integer) texto, color, nombre + " - " + fechaHora, tipoBurbuja, 24);
                } else {
                    return new BubbleText(this, (String) texto, color, nombre + " - " + fechaHora, tipoBurbuja, 14);
                }
            })
            .collect(Collectors.toList());
        return burbujas;
    }

    /**
     * Desplaza automáticamente el panel hasta el último mensaje.
     */
    private void scrollToBottom() {
        revalidate();
        repaint();
        Rectangle visibleRect = getVisibleRect();
        visibleRect.y = getHeight() - visibleRect.height;
        scrollRectToVisible(visibleRect);
    }

    // Métodos del interfaz Scrollable

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 16;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 16;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
