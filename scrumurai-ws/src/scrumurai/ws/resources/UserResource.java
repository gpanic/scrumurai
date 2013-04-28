package scrumurai.ws.resources;

import java.net.URI;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import scrumurai.data.entities.User;
import scrumurai.data.mapping.DataMapper;

@Path("/users")
public class UserResource {
	
	@Context UriInfo uriInfo;

	private DataMapper dm = new DataMapper(User.class);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(User obj) {
		long id = dm.create(obj);
		URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(id)).build();
		return Response.created(uri).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response read(@PathParam("id") long id) {
		User u = (User) dm.read(id);
		if (u != null) {
			return Response.ok().entity(dm.read(id)).build();
		} else {
			return Response.status(404).build();
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(User obj) {
		dm.update(obj);
		return Response.ok("User updated.").build();
	}

	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") long id) {
		dm.delete(id);
		return Response.ok("User deleted.").build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response list() {
		return Response.ok().entity(dm.list()).build();
	}
}
