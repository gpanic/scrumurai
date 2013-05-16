package scrumurai.data.queryobjects;

import java.sql.Date;

public class ReleaseStartEnd {

    private int id;
    private String name;
    private String version;
    private Date start_date;
    private Date end_date;
    private Boolean current;
    private Long total_effort;

    public ReleaseStartEnd() {
    }

    public ReleaseStartEnd(int id, String name, String version, Date start_date, Date end_date, Long total_effort) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.start_date = start_date;
        this.end_date = end_date;
        this.total_effort = total_effort;
    }

    public ReleaseStartEnd(int id, String name, String version, Date start_date, Date end_date, Boolean current) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.start_date = start_date;
        this.end_date = end_date;
        this.current = current;
    }

    public ReleaseStartEnd(int id, String name, String version) {
        this.id = id;
        this.name = name;
        this.version = version;
    }

    public Boolean getCurrent() {
        return current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }

    public Long getTotal_effort() {
        return total_effort;
    }

    public void setTotal_effort(Long total_effort) {
        this.total_effort = total_effort;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
}
