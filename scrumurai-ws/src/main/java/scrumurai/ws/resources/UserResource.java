package scrumurai.ws.resources;

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.api.json.JSONWithPadding;
import java.math.BigInteger;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(User obj) {
        System.out.println("create");
        obj.setPassword(sha1(obj.getPassword()));
        obj.setNumber(uuid());
        
        int id = dm.create(obj);
        if (id > -1) {
            String[] send = {Integer.toString(id), obj.getNumber()};
            URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(id)).build();
            return Response.created(uri).entity(send).build();
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
    public Response list() {
        System.out.println("LIST");
        return Response.ok(dm.list()).build();
    }
    
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(User u) {
        EntityManager em = EMF.get().createEntityManager();
        TypedQuery<User> query = em.createQuery("SELECT e FROM " + User.class.getSimpleName() + " e WHERE e.username = :uname AND e.password = :pw", User.class);
        query.setParameter("uname", u.getUsername());
        query.setParameter("pw", sha1(u.getPassword()));
        List<User> rs = query.getResultList();
        em.close();
        if (rs.size() == 1) {
            rs.get(0).setPassword("");
            return Response.ok().entity(rs.get(0)).build();
        } else
            return Response.status(404).build();
    }
    
    @POST
    @Path("/loginremember")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginRemember(String loginData) {
        loginData = loginData.substring(1,loginData.length()-1);
        String[] loginsplit = loginData.split("[|]");
        //ker doda nulo na konec??
        System.out.println(loginsplit[1]);
        if (loginsplit.length < 2)
            return Response.status(404).build();
        else {
            EntityManager em = EMF.get().createEntityManager();
            TypedQuery<User> query = em.createQuery("SELECT e FROM " + User.class.getSimpleName() + " e WHERE e.username = :uname AND e.number = :number", User.class);
            query.setParameter("uname", loginsplit[0]);
            query.setParameter("number", loginsplit[1]);
            List<User> rs = query.getResultList();
            em.close();
            if (rs.size() == 1){
                rs.get(0).setPassword("");
                rs.get(0).setNumber("");
                return Response.ok().entity(rs.get(0)).build();
            }else
                return Response.status(404).build();
        }
    }
    
    private String sha1(String pw) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.reset();
            byte[] pwByte = pw.getBytes();
            md.update(pwByte, 0, pwByte.length);
            
            pw = new BigInteger(1, md.digest()).toString(2);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return pw;
    }
    
    private String uuid() {
        String sud = UUID.randomUUID().toString();
        sud = sud.replaceAll("-", "");
        return new BigInteger(sud, 16).toString();
    }
}
