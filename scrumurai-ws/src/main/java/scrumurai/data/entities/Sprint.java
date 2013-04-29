package scrumurai.data.entities;

import java.sql.Date;

import javax.persistence.*;

@Entity
@Table(name="tbl_sprint")
public class Sprint implements EntityObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private Date start;
    private Date end;
    private int total_effort;
    private int progress;
    @ManyToOne()
    private Project project;
    @ManyToOne()
    private Release release;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public String toString() {
        return "Sprint{" + "id=" + id + ", name=" + name + ", start=" + start + ", end=" + end + ", total_effort=" + total_effort + ", progress=" + progress + ", project=" + project + ", release=" + release + '}';
    }
    
    
}
