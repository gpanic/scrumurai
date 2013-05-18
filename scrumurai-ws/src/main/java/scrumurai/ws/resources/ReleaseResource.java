package scrumurai.ws.resources;

import java.net.URI;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import scrumurai.data.entities.UserStory;
import scrumurai.data.mapping.DataMapper;
import scrumurai.data.queryobjects.ReleaseDetailed;
import scrumurai.data.queryobjects.ReleaseStartEnd;
import scrumurai.ws.resources.helpers.ReleaseHelper;

@Path("/releases")
public class ReleaseResource implements Resource<Release> {

    @Context
    UriInfo uriInfo;
    private DataMapper dm = new DataMapper(Release.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Release obj) {
        System.out.println(obj);
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

    @GET
    @Path("/detailed/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readDetailed(@PathParam("id") int id) {
        System.out.println("LIST RELEASE DETAILED");
        EntityManager em = EMF.get().createEntityManager();
        TypedQuery<UserStory> query = em.createQuery("SELECT e FROM " + UserStory.class.getSimpleName() + " e WHERE e.sprint.id IN (SELECT s.id FROM Sprint s WHERE s.release.id = :release_id)", UserStory.class);
        query.setParameter("release_id", id);
        List<UserStory> rs = query.getResultList();
        em.close();
        if (rs.size() > 0) {
            ReleaseDetailed release = ReleaseHelper.setReleaseDetailed(rs);
            if (release != null) {
                return Response.ok(release).build();
            }
        }
        return Response.status(404).build();
    }

    @GET
    @Path("/datatotal/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readBurndown(@PathParam("id") int id) {
        System.out.println("LIST RELEASE BURNDOWN");
        EntityManager em = EMF.get().createEntityManager();
        TypedQuery<Object[]> query = em.createQuery("SELECT r.id, r.name, "
                + "MIN(s.start_date), MAX(s.end_date), SUM(s.total_effort) "
                + "FROM Release r, Sprint s "
                + "WHERE r.id = s.release.id "
                + "AND r.id = :release_id", Object[].class);
        query.setParameter("release_id", id);
        List<Object[]> rs = query.getResultList();
        em.close();
        if (rs.size() > 0) {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            ReleaseStartEnd r = null;
            try {
                r = new ReleaseStartEnd((Integer) rs.get(0)[0],
                        (String) rs.get(0)[1],
                        new java.sql.Date(sd.parse((String) rs.get(0)[2]).getTime()),
                        new java.sql.Date(sd.parse((String) rs.get(0)[3]).getTime()),
                        (Long) rs.get(0)[4]);
            } catch (Exception ex) {
                System.out.println(ex);
                return Response.status(404).build();
            }
            return Response.ok(r).build();
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

    @GET
    @Path("/currentdone/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listStartEnd(@PathParam("id") int id) {
        System.out.println("LIST RELEASE CURRENT DONE");
        EntityManager em = EMF.get().createEntityManager();
        TypedQuery<Sprint> query = em.createQuery("SELECT e FROM " + Sprint.class.getSimpleName() + " e WHERE e.project.id = :project_id", Sprint.class);
        query.setParameter("project_id", id);
        List<Sprint> rs = query.getResultList();
        em.close();
        if (rs.size() > 0) {
            List<ReleaseStartEnd> releases = ReleaseHelper.sortSprintsToReleaseStartEnd(rs);
            if (releases.size() > 0) {
                return Response.ok(releases).build();
            }
        }
        return Response.status(404).build();

    }

    

    
}
