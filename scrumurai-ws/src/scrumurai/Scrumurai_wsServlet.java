package scrumurai;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.*;

import scrumurai.data.entities.User;
import scrumurai.data.mapping.DataMapper;

@SuppressWarnings("serial")
public class Scrumurai_wsServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		System.out.println("AAAAAAAAAAA");
		/*
		User u = new User();
		u.setEmail("dejan.svetec0@gmail.com");
		u.setFirstName("sdsdsdsdsds");
		u.setLastName("Svetec");
		u.setPassword("pass");
		u.setUsername("dejan");
		DataMapper dm = new DataMapper(User.class);
		dm.create(u);
		*/
		DataMapper dm = new DataMapper(User.class);
		System.out.println(dm.list().get(0).getClass().toString());
		
	}
}
