package lanzador;
import dominio.*;
import tds.BubbleText;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;

import dao.*;
public class PruebaPersi {
	public static void main(String[] args) throws DAOException {
		
		Mensaje msj1 = new Mensaje("Hola1", BubbleText.SENT);
		Mensaje msj2 = new Mensaje("Hola2", BubbleText.SENT);
		Mensaje msj3 = new Mensaje("Hola3", BubbleText.RECEIVED);
		Mensaje msj4 = new Mensaje("Hola4", BubbleText.RECEIVED);
		Mensaje msj5 = new Mensaje(5, BubbleText.SENT);
		Mensaje msj6 = new Mensaje(6, BubbleText.RECEIVED);
		/*
		List<Mensaje> lista = Arrays.asList(msj1, msj2, msj3, msj4, msj5, msj6);
		for (Mensaje msj : lista) {
			System.out.println(msj.getContenido());
			System.out.println(msj.getId());
			FactoriaDAO.getInstancia().getMensajeDAO().registrarMensaje(msj1);
		} 
		for (int i = 0; i < 4; i++) {
			FactoriaDAO.getInstancia().getMensajeDAO().registrarMensaje(msj1);
		}*/
		
		List<Mensaje> lista2 = new ArrayList<>();
		lista2 = FactoriaDAO.getInstancia().getMensajeDAO().getAll();
		FactoriaDAO.getInstancia().getMensajeDAO().eliminarMensaje(msj1);
		System.out.println("Mensajes recuperados");
		for (Mensaje msj : lista2) {
			
			System.out.println(msj.getContenido());
			System.out.println(msj.getId());
		} 
		
	}
}
