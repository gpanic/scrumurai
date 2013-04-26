package scrumurai.data.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.google.appengine.api.datastore.Key;

@Entity
public class UserEffortVote implements EntityObject {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key id;
	@ManyToOne(cascade = CascadeType.ALL)
	private UserStory user_story;
	@ManyToOne(cascade = CascadeType.ALL)
	private User user;
	private int effort;

	public Key getId() {
		return id;
	}

	public void setId(Key id) {
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
