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

import scrumurai.data.entities.Sprint;
import scrumurai.data.mapping.DataMapper;

@Path("/sprints")
public class SprintResource implements Resource<Sprint> {

    @Context
    UriInfo uriInfo;
    private DataMapper dm = new DataMapper(Sprint.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Sprint obj) {
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
        Sprint obj = (Sprint) dm.read(id);
        if (obj != null) {
            return Response.ok(dm.read(id)).build();
        } else {
            return Response.status(404).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Sprint obj) {
        obj.setId(id);
        if (dm.update(obj)) {
            return Response.status(204).build();
        } else {
            return Response.status(404).build();
        }
    }
    
    @PUT
    @Path("/{id}/details")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateDetail(@PathParam("id") int id, Sprint obj) {
        EntityManager em = EMF.get().createEntityManager();
        Sprint s;
        if ((s = em.find(Sprint.class, id)) != null) {
            em.getTransaction().begin();
            s.setName(obj.getName());
            s.setStart_date(obj.getStart_date());
            s.setEnd_date(obj.getEnd_date());
            Release r = em.find(Release.class, obj.getRelease().getId());
            s.setRelease(r);
            em.getTransaction().commit();
            em.close();
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
    @Path("/rls/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(@PathParam("id") int id) {
        System.out.println("LIST RLS");
        EntityManager em = EMF.get().createEntityManager();
        TypedQuery<Sprint> query = em.createQuery("SELECT e FROM " + Sprint.class.getSimpleName() + " e WHERE e.release.id = :release_id", Sprint.class);
        query.setParameter("release_id", id);
        List<Sprint> rs = query.getResultList();
        em.close();

        if (rs.size() > 0) {
            return Response.ok(rs).build();
        } else {
            return Response.status(404).build();
        }
    }
    
    @GET
    @Path("/project/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listByProject(@PathParam("id") int id) {
        EntityManager em = EMF.get().createEntityManager();
        TypedQuery<Sprint> query = em.createQuery("SELECT e FROM Sprint e WHERE e.project.id = :project_id", Sprint.class);
        query.setParameter("project_id", id);
        List<Sprint> rs = query.getResultList();
        em.close();

        if (rs.size() > 0) {
            return Response.ok(rs).build();
        } else {
            return Response.status(404).build();
        }
    }
}
