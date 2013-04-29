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
        DataMapper dm = new DataMapper(Project.class);
        User u = new User();
        u.setId(1);


        Project p = new Project();
        p.setName("Projekt1");
        p.setProduct_owner(u);
        dm.create(p);

        List<Project> l = (List<Project>) dm.list();
        for (Project ud : l)
            System.out.println(ud);

    }
}