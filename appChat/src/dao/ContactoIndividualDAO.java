package dao;

import java.util.List;
import dominio.ContactoIndividual;

/**
 * Interfaz para el acceso a datos de la entidad ContactoIndividual.
 * Define los métodos para interactuar con la persistencia de los objetos ContactoIndividual.
 */
public interface ContactoIndividualDAO {

		/**
		 * Registra un nuevo contacto individual en la capa de persistencia.
		 * @param contInd El objeto ContactoIndividual a registrar.
		 */
		void registrarContactoIndividual(ContactoIndividual contInd);

		/**
		 * Elimina un contacto individual de la capa de persistencia.
		 * @param contInd El objeto ContactoIndividual a eliminar.
		 */
		void eliminarContactoIndividual(ContactoIndividual contInd);

		/**
		 * Actualiza la información de un contacto individual en la capa de persistencia.
		 * @param contInd El objeto ContactoIndividual con la información actualizada.
		 */
		void updateContactoIndividual(ContactoIndividual contInd);

		/**
		 * Recupera un contacto individual de la capa de persistencia dado su identificador.
		 * @param id El identificador único del contacto individual.
		 * @return El objeto ContactoIndividual correspondiente al id, o null si no se encuentra.
		 */
		ContactoIndividual getContactoIndividual(int id);

		/**
		 * Recupera todos los contactos individuales existentes en la capa de persistencia.
		 * @return Una lista de todos los objetos ContactoIndividual.
		 */
		List<ContactoIndividual> getAll();

}