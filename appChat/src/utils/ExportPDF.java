package utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import dominio.Mensaje;
import tds.BubbleText;

import java.io.FileOutputStream;
import java.util.List;

/**
 * Clase de utilidad para exportar el historial de chat a un archivo PDF.
 * Utiliza la librería iTextPDF para generar el documento.
 */
public class ExportPDF {

	// Definir el formato de fecha y hora aquí o usar una clase de utilidades si existe
	// Asumiendo que existe una clase Utils con un formatoFechaHora definido
	// private static final DateTimeFormatter formatoFechaHora = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


	/**
	 * Exporta una lista de mensajes de chat a un archivo PDF.
	 *
	 * @param messages La lista de objetos Mensaje a exportar.
	 * @param outputPath La ruta completa del archivo PDF de salida.
	 * @param contactName El nombre del contacto con el que se tuvo la conversación.
	 */
	public static void exportChatToPDF(List<Mensaje> messages, String outputPath, String contactName) {
		Document document = new Document(); // Crear un nuevo documento PDF

		try {
			// Crear el writer de PDF, asociándolo al documento y al archivo de salida
			PdfWriter.getInstance(document, new FileOutputStream(outputPath));
			document.open(); // Abrir el documento para escribir

			// Añadir título al documento
			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18); // Fuente para el título
			Paragraph title = new Paragraph("Historial de Chat con el contacto " + contactName, titleFont);
			title.setAlignment(Element.ALIGN_CENTER); // Centrar el título
			title.setSpacingAfter(20f); // Espacio después del título
			document.add(title); // Añadir el título al documento

			// Fuentes para los diferentes elementos del mensaje
			Font senderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
			Font messageFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
			Font timeFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10);

			// Iterar sobre cada mensaje en la lista
			for (Mensaje msg : messages) {
				Paragraph sender = null;
				Paragraph message = null;
				BaseColor messageColor; // Variable para el color del mensaje

				// Determinar el remitente y el color del mensaje según el tipo
				if (msg.getTipo() == BubbleText.RECEIVED) {
					sender = new Paragraph(contactName + ":", senderFont);
					messageColor = BaseColor.BLUE; // Color para mensajes recibidos
				} else { // Asumiendo que el otro tipo es enviado por "Yo"
					sender = new Paragraph("Yo:", senderFont);
					messageColor = BaseColor.GREEN; // Color para mensajes enviados
				}
				senderFont.setColor(messageColor); // Establecer color al remitente (opcional, podrías quererlo siempre negro)


				// Manejar el contenido del mensaje (texto o emoticono)
				if (msg.getEmoticono() != -1) {
					// Si es un emoticono, mostrar una representación textual
					message = new Paragraph("Emoticono " + String.valueOf(msg.getEmoticono()), messageFont);
				} else {
					// Si es texto, mostrar el texto del mensaje
					message = new Paragraph(msg.getTexto(), messageFont);
				}
				message.setFont(FontFactory.getFont(FontFactory.HELVETICA, 12, messageColor)); // Asegurar que el color se aplica al mensaje

				// Añadir el mensaje al documento
				// Remitente
				sender.setSpacingAfter(5f); // Espacio después del remitente
				sender.setIndentationLeft(20); // Indentación
				document.add(sender);

				// Mensaje
				message.setIndentationLeft(40); // Indentación mayor para el mensaje
				message.setSpacingAfter(5f); // Espacio después del mensaje
				document.add(message);

				// Hora del mensaje
				// Asegurarse de que msg.getFecha() devuelve un tipo compatible (LocalDateTime)
				// y que Utils.formatoFechaHora es accesible y un DateTimeFormatter
				Paragraph time = new Paragraph(msg.getFecha().format(utils.Utils.formatoFechaHora), timeFont);
				time.setIndentationLeft(40); // Indentación para la hora
				time.setSpacingAfter(15f); // Espacio después de la hora (separación entre mensajes)
				document.add(time);

			}

			document.close(); // Cerrar el documento
			System.out.println("Chat exportado correctamente a: " + outputPath); // Confirmación en consola

		} catch (DocumentException de) {
			// Manejar excepciones específicas de iTextPDF
			System.err.println("Error de documento al exportar PDF: " + de.getMessage());
			de.printStackTrace();
		} catch (Exception e) {
			// Manejar otras posibles excepciones (ej. FileOutputStream)
			System.err.println("Error general al exportar PDF: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
