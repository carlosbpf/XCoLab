package org.xcolab.view.pages.contestswidget;

import org.json.JSONObject;

import org.xcolab.client.admin.AdminClient;
import org.xcolab.client.admin.enums.ConfigurationAttributeKey;
import org.xcolab.client.admin.pojo.ConfigurationAttribute;
import org.xcolab.client.contest.ContestClient;
import org.xcolab.client.contest.ContestClientUtil;
import org.xcolab.client.contest.pojo.Contest;
import org.xcolab.client.contest.pojo.phases.ContestPhase;
import org.xcolab.client.contest.pojo.phases.ContestPhaseType;
import org.xcolab.util.IdListUtil;
import org.xcolab.util.clients.CoLabService;
import org.xcolab.util.http.client.RefreshingRestService;
import org.xcolab.util.http.client.RestService;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class ContestPreferences {
    private List<Long> selectedContests;
    private final static String SELECTED_CONTESTS_PREFERENCE = "SELECTED_CONTESTS";
    private final static String TITLE_PREFERENCE = "CONTEST_TITLE";
    private final static String FEED_SIZE_PREFERENCE = "CONTEST_FEED_SIZE";
    private final static String ALL_CONTESTS_TITLE = "ALL_CONTESTS_TITLE";
    private final static String ALL_CONTESTS_URL = "ALL_CONTESTS_URL";
    private final static String SHOW_COUNTS = "SHOW_COUNTS";

    private String title;
    private Integer feedSize;
    private String allContestsUrl;
    private String allContestsTitle;
    private Boolean showCounts;
    private Map<Long, String> contestMap;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getFeedSize() {
        return feedSize;
    }

    public void setFeedSize(Integer feedSize) {
        this.feedSize = feedSize;
    }

    
    public ContestPreferences() {
    	JSONObject prefs = new JSONObject(ConfigurationAttributeKey.PORTLET_CONTESTS_PREFERENCES.get());

        selectedContests = IdListUtil
                .getIdsFromString((prefs.has(SELECTED_CONTESTS_PREFERENCE))?(prefs.getString(SELECTED_CONTESTS_PREFERENCE)):(""));
        title = (prefs.has(TITLE_PREFERENCE))?(prefs.getString(TITLE_PREFERENCE)):( "Featured contests");
        allContestsTitle = (prefs.has(ALL_CONTESTS_TITLE))?(prefs.getString(ALL_CONTESTS_TITLE)):( "see all contests");
        showCounts = Boolean.parseBoolean((prefs.has(SHOW_COUNTS))?(prefs.getString(SHOW_COUNTS)):("true"));
        allContestsUrl = (prefs.has(ALL_CONTESTS_URL))?(prefs.getString(ALL_CONTESTS_URL)):("/web/guest/plans");
        try {
            feedSize = Integer.parseInt((prefs.has(FEED_SIZE_PREFERENCE))?(prefs.getString(FEED_SIZE_PREFERENCE)):("4"));
        } catch (NumberFormatException e) {
            feedSize = 4;
        }


        populateContestMap();
    }

    private void populateContestMap() {
            final List<Contest> contests = ContestClientUtil.getAllContests();
            contestMap = new LinkedHashMap<>();

            Collections.sort(contests, new Comparator<Contest>() {
                @Override
                public int compare(Contest o1, Contest o2) {
                    final Date created1 = o1.getCreated();
                    final Date created2 = o2.getCreated();
                    if (created1 != null && created2 != null) {
                        if (created1.before(created2)) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                    return (int) (o2.getContestPK() - o1.getContestPK());
                }
            });

            ContestClient cc;
            for (Contest c: contests) {
                ContestPhase activeOrLastPhase = null;

                if(c.getIsSharedContestInForeignColab()) {
                    RestService contestService = new RefreshingRestService(CoLabService.CONTEST,
                            ConfigurationAttributeKey.PARTNER_COLAB_LOCATION,
                            ConfigurationAttributeKey.PARTNER_COLAB_PORT);
                    cc = ContestClient.fromService(contestService);
                    activeOrLastPhase = cc.getActivePhase(c.getContestPK());
                }else{
                    cc = ContestClientUtil.getClient();
                    activeOrLastPhase =
                            cc.getActivePhase(c.getContestPK());
                }
                final String phaseName;
                if (activeOrLastPhase != null) {
                    final long contestPhaseTypeId = activeOrLastPhase.getContestPhaseType();
                    final ContestPhaseType contestPhaseType= cc.getContestPhaseType(contestPhaseTypeId);
                    phaseName = contestPhaseType.getName();
                } else {
                    phaseName = " ";
                }
                contestMap.put(c.getContestPK(),
                        String.format("%d [%s] %s",
                                c.getContestPK(),
                                phaseName,
                                c.getContestShortName()
                        ));
            }

    }

    public void submit()
            throws  IOException {
        JSONObject prefs = new JSONObject();

        prefs.put(SELECTED_CONTESTS_PREFERENCE, IdListUtil.getStringFromIds(selectedContests));
        prefs.put(TITLE_PREFERENCE, title);
        prefs.put(FEED_SIZE_PREFERENCE, feedSize+"");
        prefs.put(ALL_CONTESTS_TITLE, allContestsTitle);
        prefs.put(SHOW_COUNTS, Boolean.toString(showCounts));
        prefs.put(ALL_CONTESTS_URL, allContestsUrl);
        ConfigurationAttribute configurationAttribute = new ConfigurationAttribute();
        configurationAttribute.setName(ConfigurationAttributeKey.PORTLET_CONTESTS_PREFERENCES.name());
        configurationAttribute.setStringValue(prefs.toString());
        AdminClient.updateConfigurationAttribute(configurationAttribute);
    }

    public List<Long> getSelectedContests() {
        return selectedContests;
    }

    public void setSelectedContests(List<Long> selectedContests) {
        this.selectedContests = selectedContests;
    }

    public Map<Long, String> getContestMap() {
        return contestMap;
    }

    public void setContestMap(Map<Long, String> contestMap) {
        this.contestMap = contestMap;
    }

    public String getAllContestsUrl() {
        return allContestsUrl;
    }

    public void setAllContestsUrl(String allContestsUrl) {
        this.allContestsUrl = allContestsUrl;
    }

    public String getAllContestsTitle() {
        return allContestsTitle;
    }

    public void setAllContestsTitle(String allContestsTitle) {
        this.allContestsTitle = allContestsTitle;
    }

    public Boolean getShowCounts() {
        return showCounts;
    }

    public void setShowCounts(Boolean showCounts) {
        this.showCounts = showCounts;
    }
}
