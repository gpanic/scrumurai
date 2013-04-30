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
@Table(name="tbl_projectmember")
@XmlRootElement
public class ProjectMember implements EntityObject {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;
    
    @NotNull
    private String role;
    
    @ManyToOne()
    @NotNull
    private Project project;
    
    @ManyToOne()
    @NotNull
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "ProjectMember{" + "id=" + id + ", role=" + role + ", project=" + project + ", user=" + user + '}';
    }
    
    
}
