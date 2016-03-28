package org.xcolab.portlets.contestmanagement.beans;

import com.ext.portlet.model.Contest;
import com.ext.portlet.model.ContestPhase;
import com.ext.portlet.service.ContestLocalServiceUtil;
import com.ext.portlet.service.ContestPhaseLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import org.xcolab.portlets.contestmanagement.utils.ContestResourcesHtmlParserUtil;
import org.xcolab.portlets.contestmanagement.wrappers.SectionDefinitionWrapper;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Thomas on 2/13/2015.
 */
public class ContestResourcesBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<SectionDefinitionWrapper> baseSections;
    private List<SectionDefinitionWrapper> additionalSections;
    private List<SectionDefinitionWrapper> sections;
    private HashMap<String, String> overviewSectionValues;
    private final ContestResourcesHtmlParserUtil contestResourcesHtmlParserUtil;
    private int numberOfSections;

    private final static String[] BASE_SECTION_TITLES =
            {"Background", "Key Issues", "Judging Criteria", "Prizes", "Resources for Proposal Authors"};
    private final static List<String> IGNORE_SECTION_TITLES = Arrays.asList("Overview", "\u00a0");
    private final static int START_INDEX_OF_BOTTOM_SECTIONS = 4;

    private final static String SECTION_TEXT_DEFAULT = "";

    private final static String SECTION_HELPTEXT_BACKGROUND =
            "This section expands on the homepage description and includes:<br />\n" +
                    "- An overview of the sector/topic and why it is important for addressing climate change;<br />\n" +
                    "- Relevant facts and figures (please note that it is important this section is rooted in scientific facts and is consistent with the findings of the scientific community, such as those found in the <a href=\"http://www.ipcc.ch/report/ar5/\">IPCC Fifth Assessment Report</a>.);<br />\n"
                    +
                    "- Why are ideas being sought and what could be possible if these challenges were overcome;<br />\n"
                    +
                    "- A brief statement of the contest’s focus (which will be expanded upon in the Judging Criteria section, below).<br />";


    private final static String SECTION_HELPTEXT_KEY_ISSUES =
            "This section provides readers with a sense of where the leaders in this field are looking – what are the current challenges or opportunities that people in this sector are talking about and working to solve? What recent developments have impacted the sector? For example, this section could reference the impact of the U.S.-China climate agreement on the sector, the automotive industry’s focus on self-driving cars, or the dramatic increase of investment in energy storage.";

    private final static String SECTION_HELPTEXT_PRIZES =
            "All contest winners will be recognized by the Climate CoLab and eligible for its Grand Prize.  If your contest team is offering any additional prizes, please state them here.";

    private final static String SECTION_HELPTEXT_JUDGING_CRITERIA =
            "<p>This section clarifies what kinds of proposals the contest is seeking and is usually written in bullet points.</p>\n"
                    +
                    "<p><strong>For Climate CoLab contests:</strong> The judging criteria are typically standard across all Climate CoLab contests, though your contest team is welcome to add text below the section, “The Judges will favor proposals that also effectively…</p>\n"
                    +
                    "<p><strong>For partner contests:</strong> Here is where you can define: what kinds of solutions are being sought? (community projects, business models, web interfaces/apps, policy solutions, new technologies, etc.); what is the scope of use? (local, national, international, etc.); what stage of development? (all ideas welcome, fully implementable and scalable proposals only, etc.)</p>";

    private final static String SECTION_HELPTEXT_REFERENCES =
            "Free, web-accessible references to help guide authors in preparing their proposals.";

    private final static String[] BASE_SECTIONS_HELPTEXT = {
            SECTION_HELPTEXT_BACKGROUND,
            SECTION_HELPTEXT_KEY_ISSUES,
            SECTION_HELPTEXT_JUDGING_CRITERIA,
            SECTION_HELPTEXT_PRIZES,
            SECTION_HELPTEXT_REFERENCES};

    public ContestResourcesBean() {
        sections = new ArrayList<>();
        baseSections = new ArrayList<>();
        additionalSections = new ArrayList<>();
        contestResourcesHtmlParserUtil = new ContestResourcesHtmlParserUtil();
        contestResourcesHtmlParserUtil.setBaseSectionTitles(BASE_SECTION_TITLES);
        contestResourcesHtmlParserUtil.setIgnoreSectionTitles(IGNORE_SECTION_TITLES);
    }

    public void splitSections() {
        int startOfAdditionalSections = START_INDEX_OF_BOTTOM_SECTIONS;
        int endOfAdditionalSections = numberOfSections - START_INDEX_OF_BOTTOM_SECTIONS;
        List<SectionDefinitionWrapper> baseSectionsTop = sections.subList(0, START_INDEX_OF_BOTTOM_SECTIONS);
        List<SectionDefinitionWrapper> baseSectionBottom = sections.subList(endOfAdditionalSections, sections.size());
        baseSections.addAll(baseSectionsTop);
        baseSections.addAll(baseSectionBottom);
        additionalSections = sections.subList(startOfAdditionalSections, endOfAdditionalSections);
    }

    public String getSectionsAsHtml() {
        removeDeletedSections();
        incooperateNewSections();

        StringBuilder sectionsAsHtmlString = new StringBuilder();
        String overviewSection = ContestResourcesHtmlParserUtil.getOverviewSectionAsHtmlString(overviewSectionValues);

        sectionsAsHtmlString.append(overviewSection);
        for (SectionDefinitionWrapper sectionBaseDefinition : sections) {
            if (!sectionBaseDefinition.getContent().isEmpty()) {
                String sectionAsHtmlString =
                        ContestResourcesHtmlParserUtil.getSectionAsHtmlString(sectionBaseDefinition);
                sectionsAsHtmlString.append(sectionAsHtmlString);
            }
        }
        return sectionsAsHtmlString.toString();
    }

    private void incooperateNewSections() {
        SectionDefinitionWrapper templateSectionDefinitionWrapper = new SectionDefinitionWrapper();
        for (SectionDefinitionWrapper sectionBaseDefinition : sections) {
            if (sectionBaseDefinition.isTemplateSection()) {
                templateSectionDefinitionWrapper = sectionBaseDefinition;
                break;
            }
        }
        int indexOfDummySectionBaseDefinition = sections.indexOf(templateSectionDefinitionWrapper);
        sections.remove(templateSectionDefinitionWrapper);
        Collections.rotate(sections.subList(indexOfDummySectionBaseDefinition - 1, sections.size()), -1);
    }

    private void removeDeletedSections() {
        List<SectionDefinitionWrapper> removedSectionDefinitions = new ArrayList<>();
        for (SectionDefinitionWrapper sectionBaseDefinition : sections) {
            if (sectionBaseDefinition.getTitle().isEmpty()
                    && sectionBaseDefinition.getContent().isEmpty()
                    && !sectionBaseDefinition.isTemplateSection()) {
                removedSectionDefinitions.add(sectionBaseDefinition);
            }
        }

        for (SectionDefinitionWrapper removedSectionDefinition : removedSectionDefinitions) {
            sections.remove(removedSectionDefinition);
        }
    }


    public void fillSectionsWithContent(String resourcesContent) throws Exception {
        contestResourcesHtmlParserUtil.parseDocument(resourcesContent);
        fillBaseSectionsWithContent();
        fillAdditionalSectionsWithContent();
        composeSectionsList();
    }

    private void fillBaseSectionsWithContent() {
        HashMap<String, String> baseSectionsContent = contestResourcesHtmlParserUtil.getBaseSections();
        int index = 0;
        for (String baseContestSectionTitle : baseSectionsContent.keySet()) {
            SectionDefinitionWrapper sectionDefinitionWrapper =
                    new SectionDefinitionWrapper(baseContestSectionTitle, 0,
                            BASE_SECTIONS_HELPTEXT[index], baseSectionsContent.get(baseContestSectionTitle), false);
            baseSections.add(sectionDefinitionWrapper);
            index++;
        }
    }

    private void addBaseSectionsWithDefaultsToSectionsList() {
        HashMap<String, String> baseSectionsContent = contestResourcesHtmlParserUtil.getBaseSections();
        for (String baseContestSectionTitle : baseSectionsContent.keySet()) {
            SectionDefinitionWrapper sectionDefinitionWrapper =
                    new SectionDefinitionWrapper(baseContestSectionTitle, false);
            sections.add(sectionDefinitionWrapper);
        }
    }

    private void fillAdditionalSectionsWithContent() {
        HashMap<String, String> additionalSectionsContent = contestResourcesHtmlParserUtil.getAdditionalSections();
        for (String additionalContestSectionTitle : additionalSectionsContent.keySet()) {
            SectionDefinitionWrapper sectionDefinitionWrapper =
                    new SectionDefinitionWrapper(additionalContestSectionTitle, 0,
                            SECTION_TEXT_DEFAULT, additionalSectionsContent.get(additionalContestSectionTitle));
            additionalSections.add(sectionDefinitionWrapper);
        }
    }

    private void addAdditionalSectionsWithDefaultsToSectionsList() {
        while (sections.size() < numberOfSections) {
            SectionDefinitionWrapper sectionDefinitionWrapper = new SectionDefinitionWrapper();
            sections.add(sectionDefinitionWrapper);
        }
    }

    private void addTemplateSectionWithDefaultsToSectionsList() {
        SectionDefinitionWrapper sectionDefinitionWrapper = new SectionDefinitionWrapper();
        sectionDefinitionWrapper.setTemplateSection(true);
        sections.add(sectionDefinitionWrapper);
    }

    private void composeSectionsList() {
        sections.clear();
        List<SectionDefinitionWrapper> baseSectionsTop = baseSections.subList(0, START_INDEX_OF_BOTTOM_SECTIONS);
        List<SectionDefinitionWrapper> baseSectionBottom =
                baseSections.subList(START_INDEX_OF_BOTTOM_SECTIONS, baseSections.size());
        sections.addAll(baseSectionsTop);
        sections.addAll(additionalSections);
        sections.addAll(baseSectionBottom);
        numberOfSections = sections.size();
        addTemplateSectionWithDefaultsToSectionsList();
    }

    private void createEmptySectionsList() {
        addBaseSectionsWithDefaultsToSectionsList();
        addAdditionalSectionsWithDefaultsToSectionsList();
    }

    public List<SectionDefinitionWrapper> getSections() {
        return sections;
    }

    public void setSections(List<SectionDefinitionWrapper> sections) {
        this.sections = sections;
    }

    public int getNumberOfSections() {
        return numberOfSections;
    }

    public void setNumberOfSections(int numberOfSections) {
        this.numberOfSections = numberOfSections;
        createEmptySectionsList();
    }

    public void fillOverviewSectionContent(Contest contest) throws SystemException, ParseException {
        List<ContestPhase> contestPhaseList = ContestPhaseLocalServiceUtil.getPhasesForContest(contest);
        String proposalSubmissionEndDate = "";
        for (ContestPhase contestPhase : contestPhaseList) {
            Long contestPhaseType = contestPhase.getContestPhaseType();
            if (contestPhaseType == 1L) {
                SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMM dd, yyyy 'at' HH:mm:ss a zzzz");
                formatter.setTimeZone(TimeZone.getTimeZone("EST"));
                boolean phaseHasNoEnd = (contestPhase.getPhaseEndDate() == null);
                if (phaseHasNoEnd) {
                    proposalSubmissionEndDate = "This contest has no deadline.";
                } else {
                    proposalSubmissionEndDate = formatter.format(contestPhase.getPhaseEndDate());
                }
                break;
            }
        }
        proposalSubmissionEndDate = proposalSubmissionEndDate.replace("Standard","");
        overviewSectionValues = new LinkedHashMap<>();
        overviewSectionValues.put("Question:", contest.getContestName());
        final String contestLinkUrl = ContestLocalServiceUtil.getContestLinkUrl(contest);
        overviewSectionValues.put("Submit proposals:",
                "<a href=\"http://climatecolab.org" + contestLinkUrl + "\" target=\"_blank\">http://climatecolab.org"
                        + contestLinkUrl + "</a>");
        overviewSectionValues.put("Rules:",
                "All entrants must agree to the <a href=\"http://climatecolab.org/web/guest/resources/-/wiki/Main/Contest+rules\" target=\"_blank\">Contest Rules</a> and <a href=\"http://climatecolab.org/web/guest/resources/-/wiki/Main/Terms+of+use\" target=\"_blank\">Terms of Use</a>");
        overviewSectionValues.put("Deadline:", proposalSubmissionEndDate);
        overviewSectionValues.put("Judging Criteria & Prizes:", "See below.");
    }
}




