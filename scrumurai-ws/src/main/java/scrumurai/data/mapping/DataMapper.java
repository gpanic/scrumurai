package scrumurai.data.mapping;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import scrumurai.data.EMF;
import scrumurai.data.entities.EntityObject;

public class DataMapper {
	
	private EntityManager em;
	private Class<? extends EntityObject> c;
	
	public DataMapper(Class<? extends EntityObject> c) {
		this.c = c;
	}
	
	public int create(EntityObject eo) {
		em = EMF.get().createEntityManager();
		em.getTransaction().begin();
		try {
			em.persist(eo);
			em.getTransaction().commit();
		} catch (PersistenceException e) {
			em.getTransaction().rollback();
			System.out.println(e.toString());
		}
		em.close();
		return eo.getId();
	}
	
	public EntityObject read(String id) {
		em = EMF.get().createEntityManager();
		EntityObject eo = em.find(c, id);
		em.close();
		return eo;
	}
	
	public boolean update(EntityObject eo) {
		em = EMF.get().createEntityManager();
		if (em.find(c, eo.getId()) != null)
		{
			em.getTransaction().begin();
			em.merge(eo);
			em.getTransaction().commit();
			em.close();
			return true;
		} else {
			return false;
		}
	}
	
	public boolean delete(String id) {
		em = EMF.get().createEntityManager();
		EntityObject eo = em.find(c, id);
		if (eo != null) {
			em.getTransaction().begin();
			em.remove(eo);
			em.getTransaction().commit();
			em.close();
			return true;
		} else {
			return false;
		}
	}
	
	public List<? extends EntityObject> list() {
		em = EMF.get().createEntityManager();
		TypedQuery<? extends EntityObject> query = em.createQuery("select e from " + c.getName() + " e", c);
		List<? extends EntityObject> rs = query.getResultList();
		em.close();
		return rs;
	}

}
