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
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import scrumurai.data.EMF;

import scrumurai.data.entities.Release;
import scrumurai.data.entities.Sprint;
import scrumurai.data.entities.User;
import scrumurai.data.mapping.DataMapper;
import scrumurai.data.queryobjects.ReleaseStartEnd;

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
            List<ReleaseStartEnd> releases = sortSprintsToReleaseStartEnd(rs);
            if (releases.size() > 0) {
                return Response.ok(releases).build();
            }
        }
        return Response.status(404).build();

    }

    public List<ReleaseStartEnd> sortSprintsToReleaseStartEnd(List<Sprint> sprints) {
        List<ReleaseStartEnd> rse = new ArrayList<ReleaseStartEnd>();
        for (Sprint s : sprints) {
            boolean contains = false;
            for (ReleaseStartEnd rs : rse) {
                if (s.getRelease().getId() == rs.getId()) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                ReleaseStartEnd rs = new ReleaseStartEnd(s.getRelease().getId(), s.getRelease().getName(), s.getRelease().getVersion());

                Date start_date = null;
                Date end_date = null;
                for (Sprint s2 : sprints) {
                    if (s2.getRelease().getId() == rs.getId()) {
                        if (start_date != null) {
                            if (s2.getStart_date().compareTo(start_date) < 0)
                                start_date = s2.getStart_date();
                            if (s2.getEnd_date().compareTo(end_date) > 0)
                                end_date = s2.getEnd_date();
                        } else {
                            start_date = s2.getStart_date();
                            end_date = s2.getEnd_date();
                        }
                    }
                }
                rs.setStart_date(start_date);
                rs.setEnd_date(end_date);

                if (end_date != null && end_date.compareTo(new java.sql.Date(Calendar.getInstance().getTimeInMillis())) < 0)
                    rs.setCurrent(false);
                else
                    rs.setCurrent(true);
                rse.add(rs);
            }

        }
        return rse;
    }
}
