package scrumurai.ws.resources;

import java.io.File;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/db")
public class DBResource {
    
    @GET
    @Path("/drop")
    @Produces(MediaType.TEXT_PLAIN)
    public String drop() {
        File f = new File("scrumurai.sqlite");
        f.delete();
        return "DROPPED";
    }
    
}
