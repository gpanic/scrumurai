package scrumurai;

import java.util.List;
import scrumurai.data.entities.User;
import scrumurai.data.mapping.DataMapper;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        DataMapper dm = new DataMapper(User.class);
        User u = new User();
        u.setEmail("KKKK");
        u.setFirstname("sdsdsdsdsds");
        u.setLastname("Svetec");
        u.setPassword("pass");
        u.setUsername("dejan");
        
        dm.create(u);
        List<User> l = (List<User>) dm.list();
        for(User ud:l)
            System.out.println(ud);
        
    }
}