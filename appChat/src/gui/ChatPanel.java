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


public class ChatPanel extends JPanel implements Scrollable {
    
    private static final long serialVersionUID = 1L;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public ChatPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        mostrarMensajeInicial();
    }
    
    private void mostrarMensajeInicial() {
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
    
    public void mostrarChat(Contacto contacto) {
        removeAll();
        List<BubbleText> mensajes = convertirMensajesABurbujas(contacto);
        mensajes.forEach(this::add);
        revalidate();
        repaint();
        scrollToBottom();
    }
    
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
    
    private List<BubbleText> convertirMensajesABurbujas(Contacto contacto) {
        List<Object> textoMensajes = Controlador.INSTANCE.getContenidoMensajes(contacto);
        List<String> infoMensajes = Controlador.INSTANCE.getInfoMensajes(contacto);
        List<BubbleText> burbujas = textoMensajes.stream()
			.map(texto -> {
				String[] info = infoMensajes.get(textoMensajes.indexOf(texto)).split("\\|");
				String fechaHora = info[0];
				Integer tipo = Integer.parseInt(info[1]);
				
				Color color = tipo.equals(BubbleText.SENT) ? new Color(220, 248, 198) : Color.WHITE;
				String nombre = tipo.equals(BubbleText.SENT) ? "Tú" : contacto.getNombre();
				int tipoBurbuja = tipo.equals(BubbleText.SENT) ? BubbleText.SENT : BubbleText.RECEIVED;
				
				return new BubbleText(this, texto.toString(), color, nombre + " - " + fechaHora, tipoBurbuja, 14);
			})
			.collect(Collectors.toList());
        return burbujas;
    }
    
    private void scrollToBottom() {
        revalidate();
        repaint();
        Rectangle visibleRect = getVisibleRect();
        visibleRect.y = getHeight() - visibleRect.height;
        scrollRectToVisible(visibleRect);
    }

    // Implementación de Scrollable
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