package scrumurai;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.*;

import scrumurai.data.EMF;
import scrumurai.data.entities.User;

@SuppressWarnings("serial")
public class Scrumurai_wsServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		System.out.println("AAAAAAAAAAA");
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
			Query q1 = em.createQuery("SELECT c FROM User c");
			System.out.println(q1.getResultList().size());
			resp.getWriter().println("Hello, world");
		} finally {
		}
	}
}
