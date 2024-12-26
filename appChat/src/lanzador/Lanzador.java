package lanzador;

import java.awt.EventQueue;

import gui.AñadirContacto;
import gui.Login;
import gui.VentanaPrincipal;

public class Lanzador {
	public static void main(final String[] args){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login ventana = new Login();
					ventana.mostrarVentana();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}