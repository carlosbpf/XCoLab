package org.xcolab.client.contest.pojo.templates;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.xcolab.client.contest.ContestClient;
import org.xcolab.client.contest.OntologyClientUtil;
import org.xcolab.client.contest.pojo.ontology.FocusArea;
import org.xcolab.client.contest.pojo.ontology.OntologyTerm;
import org.xcolab.client.proposals.ProposalClientUtil;
import org.xcolab.client.proposals.exceptions.ProposalNotFoundException;
import org.xcolab.client.proposals.pojo.Proposal;
import org.xcolab.client.proposals.pojo.attributes.ProposalAttribute;
import org.xcolab.util.IdListUtil;
import org.xcolab.util.clients.CoLabService;
import org.xcolab.util.enums.Plurality;
import org.xcolab.util.enums.proposal.PlanSectionTypeKeys;
import org.xcolab.util.html.HtmlUtil;
import org.xcolab.util.http.client.RestService;

import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlanSectionDefinition extends AbstractPlanSectionDefinition {

    private final Proposal wrappedProposal;

    private final RestService restService;

    public PlanSectionDefinition() {
        wrappedProposal = null;
        restService = null;
    }

    public PlanSectionDefinition(PlanSectionDefinition value) {
        super(value);
        if(value.getRestService() != null){
            restService = value.getRestService();
        }else{
            restService = null;
        }
        wrappedProposal = null;
    }

    public PlanSectionDefinition(PlanSectionDefinition value, Proposal proposal) {
        super(value);
        if(value.getRestService() != null){
            restService = value.getRestService();
        }else{
            if(proposal.getRestService()!=null){
                restService = proposal.getRestService();
            }else {
                restService = null;
            }
        }
        this.wrappedProposal = proposal;
    }
    public PlanSectionDefinition(AbstractPlanSectionDefinition abstractPlanSectionDefinition,
            RestService restService) {
        super(abstractPlanSectionDefinition);
        wrappedProposal = null;
        this.restService = restService;
    }

    public List<Long> getAdditionalIdsAsList() {
        List<Long> longIds = new ArrayList<>();
        final String stringOfStringIds = this.getAdditionalIds();
        if (stringOfStringIds != null) {
            String[] stringIds = stringOfStringIds.split(",");
            for (String stringId : stringIds) {
                if (!stringId.isEmpty()) {
                    try {
                        longIds.add(Long.parseLong(stringId));
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }
        return longIds;
    }

    public String getContent() {
        ProposalAttribute attr = getSectionAttribute();

        if (attr == null) {
            return null;
        }
        return attr.getStringValue().trim();
    }

    public String getContentFormatted() throws URISyntaxException {
        String content = getContent();
        if (content == null) {
            //default text if available
            return ( !StringUtils.isEmpty(this.getDefaultText())) ? this.getDefaultText() : null;
        }
        Document contentDocument = Jsoup.parse(content.trim());
        contentDocument = HtmlUtil.addNoFollowToLinkTagsInDocument(contentDocument);

        for (Element aTagElements : contentDocument.select("a")) {
            String curURL = aTagElements.attr("href");
            final String[] youtubeAddresses = {"http://youtu.be", "https://youtu.be", "http://www.youtube.com",
                    "https://www.youtube.com", "http://youtube.com", "https://youtube.com"};
            boolean isYoutube = false;
            for (String youtubePrefix: youtubeAddresses) {
                if (curURL.startsWith(youtubePrefix)) {
                    isYoutube = true;
                    break;
                }
            }
            if (!isYoutube) {
                continue;
            }
            if (!(aTagElements.hasClass("utube") || aTagElements.text().toLowerCase().startsWith("embed") || aTagElements.text().equalsIgnoreCase("v"))) {
                // only links with "embed" text or "utube" class should be replaced by an iframe
                continue;
            }
            String videoId = null;
            if (curURL.indexOf("?") > 0 && curURL.indexOf("v=") > 0) {
                // legacy url support
                List<NameValuePair> params = URLEncodedUtils
                        .parse(curURL.substring(curURL.indexOf("?") + 1), Charset.defaultCharset());
                for (NameValuePair nvp : params) {
                    if (nvp.getName().equals("v")) {
                        videoId = nvp.getValue();
                    }
                }
            } else {
                final Pattern videoIdPattern = Pattern.compile("\\/([\\p{Alnum}\\-_]{11})");
                Matcher m = videoIdPattern.matcher(curURL);
                if (m.find()) {
                    videoId = m.group(1);
                }
            }
            if (videoId != null) {

                aTagElements.after("<iframe width=\"560\" height=\"315\" src=\"//www.youtube.com/embed/" + videoId + "\" frameborder=\"0\" allowfullscreen></iframe><br/>");
                aTagElements.remove();
            }
        }

        // Regex pattern originated from
        // http://stackoverflow.com/questions/1806017/extracting-urls-from-a-text-document-using-java-regular-expressions
        Pattern pattern = Pattern.compile(
                "\\b(((ht|f)tp(s?)\\:\\/\\/|~\\/|\\/)|www.)" +
                        "(((\\w+:\\w+@)?(([-\\w]+\\.)+(com|org|net|gov" +
                        "|mil|biz|info|mobi|name|aero|jobs|museum" +
                        "|travel|[a-z]{2})))|localhost)(:[\\d]{1,5})?" +
                        "(((\\/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?" +
                        "((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" +
                        "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)" +
                        "(&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" +
                        "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*" +
                        "(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?\\b");


        for (Element pTagElements : contentDocument.select("p")) {
            // Separate the <p> tags by the space character and process potential URLs
            String html = pTagElements.html();

            // Eliminates weird &nbsp; ASCII val 160 characters
            String text = pTagElements.text().replaceAll("[\\u00A0]", " ");
            String[] words = text.split("\\s");
            for (int i = 0; i < words.length; i++) {
                final String word = words[i];
                final Matcher matcher = pattern.matcher(word);

                // If a match is found create a new <a> tag
                if (matcher.find()) {
                    final String link = word.substring(matcher.start(), matcher.end());
                    //TODO: TEST FOR COMPILING
                    final Proposal linkedProposal = ProposalClientUtil.getProposalFromLinkUrl(link);

                    String elementName;
                    if (linkedProposal != null) {
                        //TODO: TEST FOR COMPILING
                        elementName = (linkedProposal).getName();
                    } else {
                        elementName = link;
                    }

                    String newLinkElementWithNoFollow = HtmlUtil.createLink(link, elementName);
                    // Replace exactly this word in the HTML code with leading and trailing spaces
                    if (words.length == 1) { // In this case there are no leading and trailing spaces in the html code
                        if (!html.contains("<")) {
                            html = html.replaceFirst(Pattern.quote(word), newLinkElementWithNoFollow);
                        }
                    } else if (i == 0) {
                        html = html.replaceFirst(Pattern.quote(word) + "(\\s|&nbsp;)", newLinkElementWithNoFollow);
                    } else if (i == words.length - 1) {
                        html = html.replaceFirst("(\\s|&nbsp;)" + Pattern.quote(word), newLinkElementWithNoFollow);
                    } else {
                        html = html.replaceFirst("(\\s|&nbsp;)" + Pattern.quote(word) + "(\\s|&nbsp;)", newLinkElementWithNoFollow);
                    }
                }
            }

            // Rebuild the whole value string of the <p> tag and exchange the old one
            pTagElements.after("<p>"+html+"</p>");
            pTagElements.remove();
        }

        return contentDocument.select("body").html();
    }

    public PlanSectionTypeKeys getType() {
        if (StringUtils.isBlank(this.getType_())) {
            return PlanSectionTypeKeys.TEXT;
        }
        return PlanSectionTypeKeys.valueOf(this.getType_());
    }

    public Long getSectionDefinitionId() {
        return this.getId_();
    }

    public boolean isLocked() {
        return this.getLocked();
    }



    public OntologyTerm getNumericValueAsOntologyTerm() {
        ProposalAttribute attr = getSectionAttribute();
        if (attr == null || attr.getNumericValue() <= 0) {
            return null;
        }

        return OntologyClientUtil.getOntologyTerm(attr.getNumericValue());
    }

    public Proposal getNumericValueAsProposal() throws ProposalNotFoundException {
        ProposalAttribute attr = getSectionAttribute();
        if (attr == null || attr.getNumericValue() <= 0) {
            return null;
        }
        return (ProposalClientUtil.getProposal(attr.getNumericValue()));
    }

    public Proposal[] getStringValueAsProposalArray() {
        ProposalAttribute attr = getSectionAttribute();
        if (attr == null || attr.getStringValue() == null || attr.getStringValue().equals("")) {
            return null;
        }

        String[] props = attr.getStringValue().split(",");
        Proposal[] ret = new Proposal[props.length];
        for (int i = 0; i < props.length; i++) {
            try {
                ret[i] = new Proposal(ProposalClientUtil.getProposal(Long.parseLong(props[i])));
            } catch (NumberFormatException e) {
                //_log.error(String.format("Could not parse proposalId %s as a number", props[i]));
            } catch (ProposalNotFoundException e) {
                //_log.error(String.format("Could not retrieve proposal with id %s", props[i]), e);
            }
        }
        return ret;
    }

    public long getNumericValue() {
        ProposalAttribute attr = getSectionAttribute();
        if (attr == null) {
            return 0;
        }
        return attr.getNumericValue();
    }

    public String getStringValue() {
        ProposalAttribute attr = getSectionAttribute();
        if (attr == null) {
            return "";
        }
        return attr.getStringValue();
    }

    public List<OntologyTerm> getFocusAreaTerms() {
        if (this.getFocusAreaId() <= 0) {
            return null;
        }

        FocusArea area = OntologyClientUtil.getFocusArea(this.getFocusAreaId());

        return OntologyClientUtil.getOntologyTermsForFocusArea(area);
    }

    public List<String> getOptionsForDropdownMenu() {
        return Arrays.asList(this.getAllowedValues().split(";"));
    }

    public List<Long> getAllowedContestTypeIdsList() {
        return IdListUtil.getIdsFromString(this.getAllowedContestTypeIds());
    }

    public String getProposalNames() {
        RestService contestservice =  restService.withServiceName(CoLabService.CONTEST.getServiceName());
        return ContestClient.fromService(contestservice)
                .getProposalNames(getAllowedContestTypeIdsList(), Plurality.SINGULAR.name(), "or");
    }


    public String getProposalNamesPlural() {
        RestService contestservice =  restService.withServiceName(CoLabService.CONTEST.getServiceName());
        return ContestClient.fromService(contestservice)
                .getProposalNames(getAllowedContestTypeIdsList(), Plurality.PLURAL.name(), "and");
    }

    public String getContestNames() {
        RestService contestservice =  restService.withServiceName(CoLabService.CONTEST.getServiceName());
        return ContestClient.fromService(contestservice)
                .getContestNames(getAllowedContestTypeIdsList(), Plurality.SINGULAR.name(), "or");
    }

    public String getContestNamesPlural() {
        RestService contestservice =  restService.withServiceName(CoLabService.CONTEST.getServiceName());
        return ContestClient.fromService(contestservice).getContestNames(getAllowedContestTypeIdsList(), Plurality.PLURAL.name(), "or");
    }

    public RestService getRestService() {
        return restService;
    }


    private ProposalAttribute getSectionAttribute() {
        return this.wrappedProposal.getProposalAttributeHelper().getAttributeOrNull("SECTION", this.getId_());
    }
}
