package scrumurai.ws.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import scrumurai.data.EMF;

import scrumurai.data.entities.ProjectMember;
import scrumurai.data.entities.User;
import scrumurai.data.mapping.DataMapper;

@Path("/project-members")
public class ProjectMemberResource implements Resource<ProjectMember> {

    @Context
    UriInfo uriInfo;
    private DataMapper dm = new DataMapper(ProjectMember.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(ProjectMember obj) {
        boolean member = false;
        List<ProjectMember> list = (List<ProjectMember>) dm.list();
        for(ProjectMember pm : list) {
            if(pm.getUser().getId() == obj.getUser().getId() && pm.getProject().getId() == obj.getProject().getId()) {
                member = true;
                break;
            }
        }
        if(!member) {
            int id = dm.create(obj);
            if (id > -1) {
                URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(id)).build();
                return Response.created(uri).build();
            }
        }
        return Response.status(400).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response read(@PathParam("id") int id) {
        ProjectMember obj = (ProjectMember) dm.read(id);
        if (obj != null) {
            return Response.ok(dm.read(id)).build();
        } else {
            return Response.status(404).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, ProjectMember obj) {
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
    
    @DELETE
    @Path("/project/{id_project}/user/{id_user}")
    public Response delete(@PathParam("id_project") int id_project, @PathParam("id_user") int id_user) {
        EntityManager em = EMF.get().createEntityManager();
        TypedQuery<ProjectMember> query = em.createQuery("SELECT x FROM ProjectMember x WHERE x.project.id = :project_id AND x.user.id = :user_id", ProjectMember.class);
        query.setParameter("project_id", id_project);
        query.setParameter("user_id", id_user);
        List<ProjectMember> rs = query.getResultList();
        if(rs.size() == 1) {
            if(dm.delete(rs.get(0).getId())) {
                return Response.status(204).build();
            }
        }
        return Response.status(404).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list() {
        return Response.ok(dm.list()).build();
    }
    
    @GET
    @Path("/by-project/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listByProject(@PathParam("id") int id) {
        List<ProjectMember> list = (List<ProjectMember>)dm.list();
        List<ProjectMember> selection = new ArrayList<ProjectMember>();
        for(ProjectMember pm : list) {
            if(pm.getProject().getId() == id) {
                selection.add(pm);
            }
        }
        return Response.ok(selection).build();
    }
}
