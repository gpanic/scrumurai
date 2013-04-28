package scrumurai.ws.resources;

import java.net.URI;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import scrumurai.data.EMF;
import scrumurai.data.entities.User;
import scrumurai.data.mapping.DataMapper;

@Path("/users")
public class UserResource {

	@Context
	UriInfo uriInfo;

	private DataMapper dm = new DataMapper(User.class);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(User obj) {
		long id = dm.create(obj);
		if (id != -1) {
			URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(id))
					.build();
			return Response.created(uri).build();
		} else {
			return Response.status(400).build();
		}
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response read(@PathParam("id") long id) {
		User u = (User) dm.read(id);
		if (u != null) {
			return Response.ok(dm.read(id)).build();
		} else {
			return Response.status(404).build();
		}
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") long id, User obj) {
		EntityManager em = EMF.get().createEntityManager();
		User u = em.find(User.class, obj.getId());
		if (u != null) {
			em.getTransaction().begin();
			if (obj.getEmail() != null)
				u.setEmail(obj.getEmail());
			if ((obj.getPassword() != null))
				u.setPassword(obj.getPassword());
			em.getTransaction().commit();
			em.close();
			return Response.status(204).build();
		}
		em.close();
		return Response.status(404).build();
	}

	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") long id) {
		if (dm.delete(id)) {
			return Response.status(204).build();
		} else {
			return Response.status(404).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response list() {
		System.out.println(dm.list());
		return Response.ok(dm.list()).build();
	}
}
