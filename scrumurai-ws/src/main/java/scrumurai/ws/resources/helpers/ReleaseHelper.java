package scrumurai.ws.resources.helpers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import scrumurai.data.EMF;
import scrumurai.data.entities.Release;
import scrumurai.data.entities.Sprint;
import scrumurai.data.entities.UserStory;
import scrumurai.data.mapping.DataMapper;
import scrumurai.data.queryobjects.ReleaseDetailed;
import scrumurai.data.queryobjects.ReleaseObj;
import scrumurai.data.queryobjects.ReleaseStartEnd;
import scrumurai.ws.resources.ReleaseResource;
import scrumurai.ws.resources.SprintResource;
import scrumurai.ws.resources.UserStoryResource;

public class ReleaseHelper {

    public static List<ReleaseStartEnd> sortSprintsToReleaseStartEnd(List<Sprint> sprints) {
        List<ReleaseStartEnd> rse = new ArrayList<ReleaseStartEnd>();
        for (Sprint s : sprints) {
            boolean contains = false;
            for (ReleaseStartEnd rs : rse) {
                if (s.getRelease().getId() == rs.getId()) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                ReleaseStartEnd rs = new ReleaseStartEnd(s.getRelease().getId(), s.getRelease().getName());
                setStartEndDate(sprints, rs);
                if (rs.getEnd_date() != null && rs.getEnd_date().compareTo(new java.sql.Date(Calendar.getInstance().getTimeInMillis())) < 0)
                    rs.setCurrent(false);
                else
                    rs.setCurrent(true);
                rse.add(rs);
            }

        }
        return rse;
    }

    public static ReleaseDetailed setReleaseDetailed(Release release) {
        ReleaseDetailed rls = new ReleaseDetailed(release.getId(), release.getName(), release.getDescription(), release.getChange_log());

        rls.setSprints(listSprints(release.getId()));

        setStartEndDate(rls.getSprints(), rls);
        setEffortTotalDone(listUserStories(rls.getId()), rls.getSprints(), rls);
        return rls;
    }

    private static void setStartEndDate(List<Sprint> sprints, ReleaseObj release) {
        Date start_date = null;
        Date end_date = null;
        for (Sprint s : sprints) {
            if (s.getRelease().getId() == release.getId()) {
                if (start_date != null) {
                    if (s.getStart_date().compareTo(start_date) < 0)
                        start_date = s.getStart_date();
                    if (s.getEnd_date().compareTo(end_date) > 0)
                        end_date = s.getEnd_date();
                } else {
                    start_date = s.getStart_date();
                    end_date = s.getEnd_date();
                }
            }
        }
        release.setStart_date(start_date);
        release.setEnd_date(end_date);
    }

    private static void setEffortTotalDone(List<UserStory> userstories, List<Sprint> sprints, ReleaseDetailed release) {
        int total = 0;
        for (Sprint s : sprints) {
            total += s.getTotal_effort();
        }

        int done = 0;
        for (UserStory u : userstories) {
            if (u.getEnd_date() != null)
                done += u.getEffort();
        }

        release.setEffort_total(total);
        release.setEffort_done(done);
    }

    private static List<Sprint> listSprints(int release_id) {
        EntityManager em = EMF.get().createEntityManager();
        TypedQuery<Sprint> query = em.createQuery("SELECT e FROM " + Sprint.class.getSimpleName() + " e WHERE e.release.id = :release_id", Sprint.class);
        query.setParameter("release_id", release_id);
        List<Sprint> rs = query.getResultList();
        em.close();

        return rs;
    }

    private static List<UserStory> listUserStories(int release_id) {
        EntityManager em = EMF.get().createEntityManager();
        TypedQuery<UserStory> query = em.createQuery("SELECT e FROM " + UserStory.class.getSimpleName() + " e WHERE e.sprint.id IN (SELECT s.id FROM Sprint s WHERE s.release.id = :release_id) ORDER BY e.end_date", UserStory.class);
        query.setParameter("release_id", release_id);
        List<UserStory> rs = query.getResultList();
        em.close();

        return rs;
    }

    public static List<ReleaseStartEnd> sortReleasesAddStartEnd(List<Release> rs) {
        List<ReleaseStartEnd> rsel = new ArrayList<ReleaseStartEnd>();


        for (Release r : rs) {
            ReleaseStartEnd rse = new ReleaseStartEnd(r.getId(), r.getName());
            List<Sprint> sprints = listSprints(r.getId());
            setStartEndDate(sprints, rse);
            if (rse.getEnd_date() != null && rse.getEnd_date().compareTo(new java.sql.Date(Calendar.getInstance().getTimeInMillis())) < 0)
                rse.setCurrent(false);
            else
                rse.setCurrent(true);
            rsel.add(rse);
        }
        return rsel;
    }

    public static int delete(int id) {
        EntityManager em = EMF.get().createEntityManager();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM UserStory e WHERE e.sprint.id IN (SELECT s.id FROM Sprint s WHERE s.release.id = :release_id)").setParameter("release_id", id).executeUpdate();
        em.createQuery("DELETE FROM Sprint e WHERE e.release.id = :release_id").setParameter("release_id", id).executeUpdate();
        int no_deleted = em.createQuery("DELETE FROM Release e WHERE e.id = :release_id").setParameter("release_id", id).executeUpdate();
        em.getTransaction().commit();
        em.close();

        return no_deleted;
    }
}
