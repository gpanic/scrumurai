package scrumurai.data.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.google.appengine.api.datastore.Key;

public class UserStory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key id;
	
	@ManyToOne
	@JoinColumn(name = "author_id", nullable = false)
	private User author;
	
	@ManyToOne
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;
	
	@ManyToOne
	@JoinColumn(name = "assignee_id")
	private User assignee;
	
	@ManyToOne
	@JoinColumn(name = "sprint_id")
	private Sprint sprint;
	
	private String name;
	private String description;
	private int order;
	private int effort;
	private String business_value;
	private String state;
	
}
