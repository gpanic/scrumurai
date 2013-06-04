package scrumurai.ws.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import scrumurai.data.EMF;

import scrumurai.data.entities.Project;
import scrumurai.data.entities.ProjectMember;
import scrumurai.data.entities.User;
import scrumurai.data.mapping.DataMapper;

@Path("/projects")
public class ProjectResource implements Resource<Project> {

    @Context
    UriInfo uriInfo;
    private DataMapper dm = new DataMapper(Project.class);
    private DataMapper dmpm = new DataMapper(ProjectMember.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Project obj) {
        int id = dm.create(obj);
        
        Project p = new Project();
        p.setId(id);
        ProjectMember pm = new ProjectMember();
        pm.setProject(p);
        pm.setRole("owner");
        User u = new User();
        u.setId(obj.getProduct_owner().getId());
        pm.setUser(u);
        dmpm.create(pm);
        
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
        Project obj = (Project) dm.read(id);
        if (obj != null) {
            return Response.ok(dm.read(id)).build();
        } else {
            return Response.status(404).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Project obj) {
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
        System.err.println("DELETE PROJECT");
        EntityManager em = EMF.get().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery("DELETE FROM ProjectMember pm WHERE pm.project.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
        em.getTransaction().commit();
        em.close();
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
    @Path("/user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listByUser(@PathParam("id") int id) {
        List<Project> list = (List<Project>) dm.list();
        List<Project> selection = new ArrayList<Project>();
        for(Project p : list) {
            if(p.getProduct_owner().getId() == id) {
                selection.add(p);
            }
        }
        return Response.ok(selection).build();
    }
}
