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
import scrumurai.data.entities.Sprint;

import scrumurai.data.entities.UserStory;
import scrumurai.data.mapping.DataMapper;

@Path("/userstories")
public class UserStoryResource implements Resource<UserStory> {

    @Context
    UriInfo uriInfo;
    private DataMapper dm = new DataMapper(UserStory.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(UserStory obj) {
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
        UserStory u = (UserStory) dm.read(id);
        if (u != null) {
            return Response.ok(dm.read(id)).build();
        } else {
            return Response.status(404).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, UserStory obj) {
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
    @Path("/sprint/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(@PathParam("id") int id) {
        System.out.println("LIST USER STORY");
        EntityManager em = EMF.get().createEntityManager();
        TypedQuery<UserStory> query = em.createQuery("SELECT e FROM " + UserStory.class.getSimpleName() + " e WHERE e.sprint.id = :sprint_id ORDER BY e.end_date", UserStory.class);
        query.setParameter("sprint_id", id);
        List<UserStory> rs = query.getResultList();
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
        TypedQuery<UserStory> query = em.createQuery("SELECT x FROM UserStory x WHERE x.project.id = :project_id", UserStory.class);
        query.setParameter("project_id", id);
        List<UserStory> rs = query.getResultList();
        em.close();
        return Response.ok(rs).build();
    }
}
