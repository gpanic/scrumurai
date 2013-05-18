package scrumurai.data.entities;

import java.sql.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "tbl_sprint")
@XmlRootElement
public class Sprint implements EntityObject {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;
    @NotNull
    private String name;
    @NotNull
    private Date start_date;
    @NotNull
    private Date end_date;
    private int total_effort;
    private int progress;
    @ManyToOne()
    @NotNull
    private Project project;
    @ManyToOne()
    @NotNull
    private Release release;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
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

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
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

    @Override
    public String toString() {
        return "Sprint{" + "id=" + id + ", name=" + name + ", start=" + start_date + ", end=" + end_date + ", total_effort=" + total_effort + ", progress=" + progress + ", project=" + project + ", release=" + release + '}';
    }
}
