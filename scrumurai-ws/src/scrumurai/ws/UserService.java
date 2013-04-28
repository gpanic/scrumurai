package scrumurai.ws;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import scrumurai.data.entities.EntityObject;
import scrumurai.data.entities.User;
import scrumurai.data.mapping.DataMapper;

@Path("/users")
public class UserService {

	private DataMapper dm = new DataMapper(User.class);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(User obj) {
		return Response.status(200).entity(dm.create(obj)).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response read(@PathParam("id") long id) {
		return Response.status(200).entity(dm.read(id)).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void update(User obj) {
		dm.update(obj);
	}

	@DELETE
	@Path("/{id}")
	public void delete(@PathParam("id") long id) {
		dm.delete(id);
	}
}
