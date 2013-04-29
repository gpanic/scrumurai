package scrumurai;

import java.util.List;
import scrumurai.data.entities.*;
import scrumurai.data.mapping.DataMapper;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {

        DataMapper dm = new DataMapper(User.class);
//        User u = new User();
//        u.setEmail("nekaj");
//        u.setFirstname("nekaj");
//        u.setUsername("uname");
//        u.setPassword("pass");
//        u.setLastname("lastnekaj");
//        
//        
//        dm.create(u);

        List<User> l = (List<User>) dm.list();
        for (User ud : l)
            System.out.println(ud);
        DataMapper dm2 = new DataMapper(Project.class);
        User u = new User();
        u.setId(1);


        Project p = new Project();
        p.setName("Projekt1");
        p.setProduct_owner(u);
        dm2.create(p);

        List<Project> l2 = (List<Project>) dm2.list();
        for (Project ud : l2)
            System.out.println(ud);

    }
}