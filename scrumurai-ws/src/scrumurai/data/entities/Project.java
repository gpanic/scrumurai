package scrumurai.data.entities;

import javax.persistence.*;

import com.google.appengine.api.datastore.Key;

@Entity
public class Project {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	private String name;
	private String description;
	private int velocity;
	
}
