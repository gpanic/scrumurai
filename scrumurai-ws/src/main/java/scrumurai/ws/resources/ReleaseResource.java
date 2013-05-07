package scrumurai.ws.resources;

import java.net.URI;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import scrumurai.data.EMF;

import scrumurai.data.entities.Release;
import scrumurai.data.entities.User;
import scrumurai.data.mapping.DataMapper;

@Path("/releases")
public class ReleaseResource implements Resource<Release> {

    @Context
    UriInfo uriInfo;
    private DataMapper dm = new DataMapper(Release.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Release obj) {
        int id = dm.create(obj);
        if (id > -1) {
            URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(id)).build();
            return Response.created(uri).build();
        } else {
            return Response.status(400).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response read(@PathParam("id") int id) {
        Release obj = (Release) dm.read(id);
        if (obj != null) {
            return Response.ok(dm.read(id)).build();
        } else {
            return Response.status(404).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Release obj) {
        obj.setId(id);
        if (dm.update(obj)) {
            return Response.status(204).build();
        } else {
            return Response.status(404).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
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

    @GET
    @Path("/proj/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(@PathParam("id") int id) {
        System.out.println("LIST PROJ");
        EntityManager em = EMF.get().createEntityManager();
        TypedQuery<Release> query = em.createQuery("SELECT e FROM " + Release.class.getSimpleName() + " e WHERE e.project.id = :project_id", Release.class);
        query.setParameter("project_id", id);
        List<Release> rs = query.getResultList();
        em.close();
        if (rs.size() > 0) {
            return Response.ok(rs).build();
        } else {
            return Response.status(404).build();
        }
    }
}
