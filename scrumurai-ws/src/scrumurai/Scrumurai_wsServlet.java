package scrumurai;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.Key;

import scrumurai.data.entities.Project;
import scrumurai.data.entities.Release;
import scrumurai.data.entities.User;
import scrumurai.data.entities.UserStory;
import scrumurai.data.mapping.DataMapper;

@SuppressWarnings("serial")
public class Scrumurai_wsServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		/*
		System.out.println("WT3");
		User u = new User();
		u.setEmail("dejan.svetec0@gmail.com");
		u.setFirstName("sdsdsdsdsds");
		u.setLastName("Svetec");
		u.setPassword("pass");
		u.setUsername("dejan");
		DataMapper dm = new DataMapper(User.class);
		dm.create(u);
		*/
		
		
//		Release r = new Release();
//		r.setName("Nekaj13");
//		
//		Project p = new Project();
//		p.setName("Projekt");
//		r.setProject(p);
//		DataMapper dm = new DataMapper(Release.class);
//		dm.create(r);
		
		
//		DataMapper dm = new DataMapper(Release.class);
//		Release r = (Release) dm.read(39);
//		System.out.println(r.toString());
//		DataMapper dm = new DataMapper(User.class);
//		System.out.println(dm.list().get(0).getClass().toString());
		
	}
}
