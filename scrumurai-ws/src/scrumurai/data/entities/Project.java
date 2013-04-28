package scrumurai.data.entities;

import javax.persistence.*;

@Entity
public class Project implements EntityObject {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	private String name;
	private String description;
	private int velocity;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private User user;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	@Override
	public String toString() {
		return "Project [id=" + id + ", user=" + user + ", name=" + name
				+ ", description=" + description + ", velocity=" + velocity
				+ "]";
	}

}
