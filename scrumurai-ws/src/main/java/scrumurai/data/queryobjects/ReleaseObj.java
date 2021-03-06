package scrumurai.data.queryobjects;

import java.util.Date;

public class ReleaseObj {

    private int id;
    private String name;
    private Date start_date;
    private Date end_date;

    public ReleaseObj() {
    }

    public ReleaseObj(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public ReleaseObj(int id, String name, Date start_date, Date end_date) {
        this.id = id;
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "ReleaseObj{" + "id=" + id + ", name=" + name + ", start_date=" + start_date + ", end_date=" + end_date + '}';
    }
}
