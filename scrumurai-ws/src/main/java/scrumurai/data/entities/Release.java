package scrumurai.data.entities;

import javax.persistence.*;


@Entity
public class Release implements EntityObject {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String version;
	private String name;
	private String description;
	private String change_log;
	
	@ManyToOne()
	 private Project project;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
	
	

	public void setProject(Project project) {
		this.project = project;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public String getChange_log() {
		return change_log;
	}

	public void setChange_log(String change_log) {
		this.change_log = change_log;
	}

	@Override
	public String toString() {
		return "Release [id=" + id + ", project=" + project + ", version="
				+ version + ", name=" + name + ", description=" + description
				+ ", change_log=" + change_log + "]";
	}
}