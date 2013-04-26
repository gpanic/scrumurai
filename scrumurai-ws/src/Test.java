import javax.persistence.EntityManager;

import scrumurai.data.EMF;
import scrumurai.data.entities.User;


public class Test {
	public static void main(String[] args) {
		EntityManager em = EMF.get().createEntityManager();
		try {
			User u = new User();
			u.setEmail("dejan.svetec0@gmail.com");
			u.setFirstName("Dejan");
			u.setLastName("Svetec");
			u.setPassword("pass");
			u.setUsername("dejan");
			em.getTransaction().begin();
			em.persist(u);
			em.getTransaction().commit();
			System.out.println("DONE");
		} finally {
		}
	}
}
