package scrumurai;

import java.util.List;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import scrumurai.data.entities.User;
import scrumurai.data.mapping.DataMapper;
import scrumurai.model.UserDetails;

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