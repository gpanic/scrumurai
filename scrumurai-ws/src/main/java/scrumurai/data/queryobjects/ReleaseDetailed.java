package scrumurai.data.queryobjects;

import java.sql.Date;
import java.util.List;
import scrumurai.data.entities.Sprint;

public class ReleaseDetailed extends ReleaseObj {

    private String description;
    private String change_log;
    private int effort_total;
    private int effort_done;
    private List<Sprint> sprints;

    public ReleaseDetailed() {
        super();
    }

    public ReleaseDetailed(int id, String name, String description, String change_log) {
        super(id,name);
        this.description = description;
        this.change_log = change_log;
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

    public int getEffort_total() {
        return effort_total;
    }

    public void setEffort_total(int effort_total) {
        this.effort_total = effort_total;
    }

    public int getEffort_done() {
        return effort_done;
    }

    public void setEffort_done(int effort_done) {
        this.effort_done = effort_done;
    }

    public List<Sprint> getSprints() {
        return sprints;
    }

    public void setSprints(List<Sprint> sprints) {
        this.sprints = sprints;
    }

    @Override
    public String toString() {
        return super.toString()+"ReleaseDetailed{" + "description=" + description + ", change_log=" + change_log + ", effort_total=" + effort_total + ", effort_done=" + effort_done + ", sprints=" + sprints + '}';
    }
    
    
}
