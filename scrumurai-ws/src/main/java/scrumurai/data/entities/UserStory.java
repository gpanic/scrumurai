package scrumurai.data.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="tbl_userstory")
@XmlRootElement
public class UserStory implements EntityObject {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;
    @NotNull
    private String name;
    private String description;
    private int arrangement;
    private int effort;
    private String business_value;
    private String state;
    
    @ManyToOne()
    @NotNull
    private User author;
    
    @ManyToOne()
    @NotNull
    private Project project;
    
    @ManyToOne()
    private User assignee;
    
    @ManyToOne()
    private Sprint sprint;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
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

    public int getArrangement() {
        return arrangement;
    }

    public void setArrangement(int arrangement) {
        this.arrangement = arrangement;
    }

    public int getEffort() {
        return effort;
    }

    public void setEffort(int effort) {
        this.effort = effort;
    }

    public String getBusiness_value() {
        return business_value;
    }

    public void setBusiness_value(String business_value) {
        this.business_value = business_value;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "UserStory{" + "id=" + id + ", name=" + name + ", description=" + description + ", arrangement=" + arrangement + ", effort=" + effort + ", business_value=" + business_value + ", state=" + state + ", author=" + author + ", project=" + project + ", assignee=" + assignee + ", sprint=" + sprint + '}';
    }
    
    
}
