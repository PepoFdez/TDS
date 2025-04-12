package dao;

import java.util.List;
import dominio.ContactoIndividual;

public interface ContactoIndividualDAO {

		void registrarContactoIndividual(ContactoIndividual contInd);
		void eliminarContactoIndividual(ContactoIndividual contInd);
		void updateContactoIndividual(ContactoIndividual contInd);
		ContactoIndividual getContactoIndividual(int id);
		List<ContactoIndividual> getAll();
		
}
