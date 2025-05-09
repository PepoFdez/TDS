package utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import dominio.Mensaje;
import tds.BubbleText;

import java.io.FileOutputStream;
import java.util.List;

public class ExportPDF {
    
    public static void exportChatToPDF(List<Mensaje> messages, String outputPath, String contactName) {
        Document document = new Document();
        
        try {
            // Crear el writer de PDF
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();
            
            // Añadir título
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Historial de Chat con el contacto " + contactName, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);
            
            // Añadir cada mensaje
            Font senderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font messageFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Font timeFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10);
            
            for (Mensaje msg : messages) {
            	Paragraph sender = null;
        		Paragraph message = null;
            	if (msg.getTipo() == BubbleText.RECEIVED) {
            		sender = new Paragraph(contactName + ":", senderFont);
            		message = new Paragraph(msg.getTexto(), messageFont);
            		messageFont.setColor(BaseColor.BLUE);
            	} else {
					sender = new Paragraph("Yo:", senderFont);
					message = new Paragraph(msg.getTexto(), messageFont);
					messageFont.setColor(BaseColor.GREEN);
				}
            	
            	if (msg.getEmoticono() != -1) {
            		message = new Paragraph("Emoticono " + String.valueOf(msg.getEmoticono()), messageFont);
            	}
				
				// Añadir el mensaje al documento
				// Sender
				sender.setSpacingAfter(5f);
				sender.setIndentationLeft(20);
				document.add(sender);
				
				// Mensaje
				message.setIndentationLeft(40);
				message.setSpacingAfter(5f);
				document.add(message);
				
				// Hora
				Paragraph time = new Paragraph(msg.getFecha().format(Utils.formatoFechaHora), timeFont);
				time.setIndentationLeft(40);
				time.setSpacingAfter(15f);
				document.add(time);
                
            }
            
            document.close();
            System.out.println("Chat exportado correctamente a: " + outputPath);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}