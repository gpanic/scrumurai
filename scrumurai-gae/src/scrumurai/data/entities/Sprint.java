package scrumurai.data.entities;

import java.sql.Date;

import javax.persistence.*;

import org.datanucleus.api.jpa.annotations.Extension;

@Entity
public class Sprint implements EntityObject {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private String id;
	
	private String name;
	private Date start;
	private Date end;
	private int total_effort;
	private int progress;
	
	@ManyToOne()
	private Project project;
	@ManyToOne()
	private Release release;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Release getRelease() {
		return release;
	}

	public void setRelease(Release release) {
		this.release = release;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public int getTotal_effort() {
		return total_effort;
	}

	public void setTotal_effort(int total_effort) {
		this.total_effort = total_effort;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}
}
