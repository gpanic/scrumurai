
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import test.User;

public class Test {
    
    public static void main(String[] args) {
        User u = new User();
        u.setUsername("user1");
        
        EntityManagerFactory fact = Persistence.createEntityManagerFactory("scrumurai");
        EntityManager em = fact.createEntityManager();
        em.getTransaction().begin();
        em.persist(u);
        em.getTransaction().commit();
        em.close();
        fact.close();
    }

}
