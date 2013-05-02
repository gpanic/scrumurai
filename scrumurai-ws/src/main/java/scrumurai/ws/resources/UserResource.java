package scrumurai.ws.resources;

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.api.json.JSONWithPadding;
import java.net.URI;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import scrumurai.data.EMF;

import scrumurai.data.entities.User;
import scrumurai.data.mapping.DataMapper;

@Path("/users")
public class UserResource implements Resource<User> {

    @Context
    UriInfo uriInfo;
    private DataMapper dm = new DataMapper(User.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(User obj) {
        System.out.println("žalost");
        int id = dm.create(obj);
        if (id > -1) {
            URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(id)).build();
            return Response.created(uri).entity(Integer.toString(id)).build();
        } else {
            return Response.status(400).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response read(@PathParam("id") int id) {
        User u = (User) dm.read(id);
        if (u != null) {
            return Response.ok(dm.read(id)).build();
        } else {
            return Response.status(404).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, User obj) {
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

//    @GET
//    @Produces({"application/javascript"})
//    public JSONWithPadding list2(@QueryParam("callback") String callback) {
//        System.out.println("nekaj");
//        return new JSONWithPadding((List<User>) dm.list());
//    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> list2() {
        System.out.println("Nekaj");
        return (List<User>) dm.list();
    }

//    @GET
//    @Path("/login")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response login(User u) {
//        EntityManager em = EMF.get().createEntityManager();
//        TypedQuery<User> query = em.createQuery("SELECT id,username,lastname,firstname,email FROM c WHERE username = :uname AND password = :pw", User.class);
//        List<User> rs = query.getResultList();
//        em.close();
//        if (rs.size() == 1)
//            return Response.ok(rs.get(0)).build();
//        else
//            return Response.status(404).build();
//            
//    }
    public Response list() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
