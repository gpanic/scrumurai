package scrumurai.data.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.datanucleus.api.jpa.annotations.Extension;

@Entity
public class UserEffortVote implements EntityObject {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private String id;

	private int effort;
	
	@ManyToOne()
	private User user;
	
	@ManyToOne()
	private UserStory user_story;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserStory getUser_story() {
		return user_story;
	}

	public void setUser_story(UserStory user_story) {
		this.user_story = user_story;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getEffort() {
		return effort;
	}

	public void setEffort(int effort) {
		this.effort = effort;
	}
}
