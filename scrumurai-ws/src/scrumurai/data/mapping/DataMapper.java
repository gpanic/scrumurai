package scrumurai.data.mapping;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import scrumurai.data.EMF;
import scrumurai.data.entities.EntityObject;

public class DataMapper {
	
	private EntityManager em;
	private Class<? extends EntityObject> c;
	
	public DataMapper(Class<? extends EntityObject> c) {
		this.c = c;
	}
	
	public long create(EntityObject eo) {
		em = EMF.get().createEntityManager();
		em.getTransaction().begin();
		em.persist(eo);
		em.getTransaction().commit();
		em.close();
		return eo.getId().getId();
	}
	
	public EntityObject read(long id) {
		em = EMF.get().createEntityManager();
		EntityObject eo = em.find(c, id);
		em.close();
		return eo;
	}
	
	public void update(EntityObject eo) {
		em = EMF.get().createEntityManager();
		em.getTransaction().begin();
		em.merge(eo);
		em.getTransaction().commit();
		em.close();
	}
	
	public void delete(long id) {
		em = EMF.get().createEntityManager();
		EntityObject eo = em.find(c, id);
		em.getTransaction().begin();
		em.remove(eo);
		em.getTransaction().commit();
		em.close();
	}
	
	public List<EntityObject> list() {
		em = EMF.get().createEntityManager();
		TypedQuery<EntityObject> query = em.createQuery("select e from User e", c);
		List<EntityObject> rs = query.getResultList();
		em.close();
		return rs;
	}

}
