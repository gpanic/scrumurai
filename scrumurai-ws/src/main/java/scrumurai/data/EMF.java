package scrumurai.data;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EMF {
	
	private static final EntityManagerFactory emInstance =
			Persistence.createEntityManagerFactory("scrumurai");
	
	private EMF() {
		
	}
	
	public static EntityManagerFactory get() {
		return emInstance;
	}

}
