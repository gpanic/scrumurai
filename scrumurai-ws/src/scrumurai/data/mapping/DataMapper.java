package scrumurai.data.mapping;

import javax.persistence.EntityManager;

import scrumurai.data.EMF;
import scrumurai.data.entities.User;

public class DataMapper {
	
	private static EntityManager em;
	
	public static void create(User u) {
		em = EMF.get().createEntityManager();
		em.getTransaction().begin();
		em.persist(u);
		em.getTransaction().commit();
		em.close();
	}
	
	public static void update(User u) {
		
	}

}
