package scrumurai;

import java.io.IOException;

import javax.servlet.http.*;

import scrumurai.data.entities.User;
import scrumurai.data.mapping.DataMapper;

@SuppressWarnings("serial")
public class Scrumurai_wsServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		System.out.println("AAAAAAAAAAA");
		User u = new User();
		u.setEmail("dejan.svetec0@gmail.com");
		u.setFirstName("Dejanaaaa");
		u.setLastName("Svetec");
		u.setPassword("pass");
		u.setUsername("dejan");
		DataMapper.create(u);
		System.out.println("DONE");
	}
}
