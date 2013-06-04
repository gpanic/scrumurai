package scrumurai.ws.resources;

import java.io.File;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import scrumurai.data.EMF;

@Path("/db")
public class DBResource {
    
    @GET
    @Path("/drop")
    @Produces(MediaType.TEXT_PLAIN)
    public String drop() {
        EntityManager em = EMF.get().createEntityManager();
        em.getTransaction().begin();
        Query query;
        query = em.createQuery("DELETE FROM ProjectMember");
        query.executeUpdate();
        query = em.createQuery("DELETE FROM UserStory");
        query.executeUpdate();
        query = em.createQuery("DELETE FROM Sprint");
        query.executeUpdate();
        query = em.createQuery("DELETE FROM Release");
        query.executeUpdate();
        query = em.createQuery("DELETE FROM Project");
        query.executeUpdate();
        query = em.createQuery("DELETE FROM User");
        query.executeUpdate();
        query = em.createQuery("DELETE FROM UserEffortVote");
        query.executeUpdate();
        em.getTransaction().commit();
        return "DROPPED";
    }
    
}
