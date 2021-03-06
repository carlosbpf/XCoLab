package org.xcolab.entity.utils;

import org.xcolab.client.activities.ActivitiesClientUtil;
import org.xcolab.client.activities.pojo.ActivityEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ActivityUtil {

    public static final long AGGREGATION_TIME_WINDOW = (long) 1000 * 60 * 60; // 1h

    public static List<ActivityEntry> retrieveAllActivities(int pagestart, int next) {
       return  ActivitiesClientUtil.getActivityEntries(pagestart, next, null, null);
    }

    public static List<ActivityEntry> groupActivities(List<ActivityEntry> activities) {
        //find all activities of same type
        Map<String, List<ActivityEntry>> activitiesMap = new HashMap<>(10000);
        for (ActivityEntry a : activities) {
            if (!activitiesMap.containsKey(getSocialActivityKey(a))) {
                activitiesMap.put(getSocialActivityKey(a), new LinkedList<ActivityEntry>());
            }
            activitiesMap.get(getSocialActivityKey(a)).add(a);
        }
        return clusterActivities(activitiesMap);
    }


    public static int getAllActivitiesCount() {
        return ActivitiesClientUtil.countActivities(null, null);
    }

    public static int getActivitiesCount(long userId) {
        return ActivitiesClientUtil.countActivities(userId, null);
    }

    private static List<ActivityEntry> clusterActivities(Map<String, List<ActivityEntry>> activitiesMap) {
        //cluster
        List<ActivityEntry> aggregatedActivities = new LinkedList<>();
        Comparator<ActivityEntry> sorter = new Comparator<ActivityEntry>() {
            @Override
            public int compare(ActivityEntry o1, ActivityEntry o2) {
                return new Long(o1.getCreateDate().getTime()).compareTo(o2.getCreateDate().getTime());
            }
        };
        for (Collection<ActivityEntry> activitiesMapValue : activitiesMap.values()) {
            List<ActivityEntry> socialActivities = new ArrayList<>(activitiesMapValue); //convert to array for sorting
            Collections.sort(socialActivities, sorter);

            ActivityEntry curMin = null;
            for (ActivityEntry socialActivity : socialActivities) {
                if (curMin == null || socialActivity.getCreateDate().getTime() - curMin.getCreateDate().getTime() < AGGREGATION_TIME_WINDOW) {
                    curMin = socialActivity;
                } else {
                    aggregatedActivities.add(curMin);
                    curMin = socialActivity;
                }
            }
            aggregatedActivities.add(curMin);
        }

        // Sort the activities in reverse order (sort by date DESC)
        Collections.sort(aggregatedActivities, Collections.reverseOrder(sorter));

        return aggregatedActivities;
    }

    private static String getSocialActivityKey(ActivityEntry sa) {
        return sa.getPrimaryType() + "_" + sa.getClassPrimaryKey() + "_" + sa.getSecondaryType() + "_" + sa.getMemberId();
    }

}