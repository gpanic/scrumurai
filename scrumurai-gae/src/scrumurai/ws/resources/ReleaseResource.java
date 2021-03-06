package scrumurai.ws.resources;

import java.net.URI;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import scrumurai.data.entities.Release;
import scrumurai.data.mapping.DataMapper;

@Path("/releases")
public class ReleaseResource {
	@Context
	UriInfo uriInfo;

	private DataMapper dm = new DataMapper(Release.class);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Release obj) {
		String id = dm.create(obj);
		if (id != null) {
			URI uri = uriInfo.getAbsolutePathBuilder().path(id).build();
			return Response.created(uri).build();
		} else {
			return Response.status(400).build();
		}
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response read(@PathParam("id") String id) {
		Release u = (Release) dm.read(id);
		if (u != null) {
			return Response.ok(dm.read(id)).build();
		} else {
			return Response.status(404).build();
		}
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") String id, Release obj) {
		obj.setId(id);
		if (dm.update(obj)) {
			return Response.status(204).build();
		} else {
			return Response.status(404).build();
		}
	}

	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") String id) {
		if (dm.delete(id)) {
			return Response.status(204).build();
		} else {
			return Response.status(404).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response list() {
		return Response.ok(dm.list()).build();
	}
}
