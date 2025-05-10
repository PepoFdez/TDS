package lanzador;

import java.awt.EventQueue;

import gui.Login;

/**
 * Clase principal que sirve como punto de entrada para la aplicación de chat.
 * Utiliza EventQueue.invokeLater para asegurar que la interfaz gráfica
 * se inicialice y muestre en el Event Dispatch Thread (EDT).
 */
public class Lanzador {

	/**
	 * Método principal que inicia la aplicación.
	 * Crea una instancia de la ventana de Login y la hace visible.
	 * @param args Argumentos de la línea de comandos (no utilizados en este caso).
	 */
	public static void main(final String[] args){
		// Ejecuta el código dentro del Runnable en el Event Dispatch Thread (EDT)
		// Esto es necesario para la mayoría de las operaciones de Swing para garantizar la seguridad de los hilos.
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// Crea una nueva instancia de la ventana de Login
					Login ventana = new Login();
					// Muestra la ventana de Login
					ventana.mostrarVentana();
				} catch (Exception e) {
					// Imprime la traza de la pila si ocurre una excepción durante la inicialización
					e.printStackTrace();
				}
			}
		});
	}
}
