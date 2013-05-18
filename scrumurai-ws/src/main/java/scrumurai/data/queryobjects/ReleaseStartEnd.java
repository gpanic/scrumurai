package scrumurai.data.queryobjects;

import java.sql.Date;

public class ReleaseStartEnd extends ReleaseObj{

    
    private Boolean current;
    private Long total_effort;

    public ReleaseStartEnd() {
        super();
    }

    public ReleaseStartEnd(int id, String name, Date start_date, Date end_date, Long total_effort) {
        super(id, name, start_date, end_date);
        this.total_effort = total_effort;
    }

    public ReleaseStartEnd(int id, String name,  Date start_date, Date end_date, Boolean current) {
        super(id, name, start_date, end_date);
        this.current = current;
    }

    public ReleaseStartEnd(int id, String name) {
        super(id, name);
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

    @Override
    public String toString() {
        return super.toString()+"ReleaseStartEnd{" + "current=" + current + ", total_effort=" + total_effort + '}';
    }

}
