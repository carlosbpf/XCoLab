package org.xcolab.view.pages.proposals.view;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import org.xcolab.client.contest.OntologyClientUtil;
import org.xcolab.client.contest.pojo.Contest;
import org.xcolab.client.contest.pojo.ontology.FocusArea;
import org.xcolab.client.contest.pojo.ontology.OntologyTerm;
import org.xcolab.client.members.pojo.Member;
import org.xcolab.client.proposals.ProposalAttributeClientUtil;
import org.xcolab.client.proposals.pojo.Proposal;
import org.xcolab.client.proposals.pojo.attributes.ProposalAttribute;
import org.xcolab.client.proposals.pojo.attributes.ProposalUnversionedAttribute;
import org.xcolab.entity.utils.enums.ProposalUnversionedAttributeName;
import org.xcolab.util.html.HtmlUtil;
import org.xcolab.view.pages.proposals.exceptions.ProposalImpactDataParserException;
import org.xcolab.view.pages.proposals.impact.ProposalImpactDataParser;
import org.xcolab.view.pages.proposals.impact.ProposalImpactSeries;
import org.xcolab.view.pages.proposals.impact.ProposalImpactSeriesList;
import org.xcolab.view.pages.proposals.impact.ProposalImpactUtil;
import org.xcolab.view.pages.proposals.permissions.ProposalsPermissions;
import org.xcolab.view.pages.proposals.utils.context.ProposalsContext;
import org.xcolab.view.pages.proposals.utils.context.ProposalsContextUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ProposalImpactJSONController {

    private final static Logger _log = LoggerFactory.getLogger(ProposalImpactJSONController.class);

    @Autowired
    private ProposalsContext proposalsContext;




    @GetMapping("/contests/{contestYear}/{contestUrlName}/c/{proposalUrlString}/{proposalId}/tab/IMPACT/proposalImpactGetRegions")
    public void proposalImpactGetRegions(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        Map<OntologyTerm, List<OntologyTerm>> ontologyMap = getOntologyMap(request);
        List<OntologyTerm> regionTerms = new ArrayList<>(ontologyMap.keySet());

        response.getOutputStream().write(ontologyTermListToJSONArray(regionTerms).toString().getBytes());
    }


    @GetMapping("/contests/{contestYear}/{contestUrlName}/c/{proposalUrlString}/{proposalId}/tab/IMPACT/proposalImpactGetSectorsForRegion")
    public void proposalImpactGetSectorForRegions(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "regionTermId") Long regionTermId) throws IOException {

        Map<OntologyTerm, List<OntologyTerm>> ontologyMap = getOntologyMap(request);

        List<OntologyTerm> sectorTerms = ontologyMap.get(OntologyClientUtil.getOntologyTerm(regionTermId));
        response.getOutputStream().write(ontologyTermListToJSONArray(sectorTerms).toString().getBytes());
    }



    @GetMapping("/contests/{contestYear}/{contestUrlName}/c/{proposalUrlString}/{proposalId}/tab/IMPACT/proposalImpactGetDataSeries")
    public void proposalImpactGetDataSeries(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "sectorTermId") Long sectorTermId,
            @RequestParam(value = "regionTermId") Long regionTermId) throws IOException {

        if (sectorTermId == 0 || regionTermId == 0) {
            return;
        }

        try {
            Contest contest = proposalsContext.getContest(request);
            OntologyTerm sectorOntologyTerm = OntologyClientUtil.getOntologyTerm(sectorTermId);
            OntologyTerm regionOntologyTerm = OntologyClientUtil.getOntologyTerm(regionTermId);

            // ProposalImpactSeriesList impactSeriesList = getProposalImpactSeriesList(request);
            FocusArea selectedFocusArea = new ProposalImpactUtil(contest).getFocusAreaAssociatedWithTerms(sectorOntologyTerm, regionOntologyTerm);

            // Create a impact series with all data series for one sector-region pair
            ProposalImpactSeries impactSeries = new ProposalImpactSeries(contest, proposalsContext.getProposal(request), selectedFocusArea);

            response.getOutputStream().write(impactSeries.toJSONObject() .toString().getBytes());
        } catch (IOException e) {
            _log.error("Could not load impact series for contestId {}",
                    proposalsContext.getContest(request).getContestPK(), e);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("success", false);
            response.getOutputStream().write(responseJSON.toString().getBytes());
        }
    }


    @PostMapping("/contests/{contestYear}/{contestUrlName}/c/{proposalUrlString}/{proposalId}/tab/IMPACT/proposalImpactSaveDataSeries")
    public void proposalImpactSaveDataSeries(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "focusAreaId") Long focusAreaId) throws IOException {

        JSONObject responseJSON = new JSONObject();
        ProposalsPermissions permissions = proposalsContext.getPermissions(request);

        if (!permissions.getCanEdit() && !permissions.getCanFellowActions() && !permissions.getCanIAFActions()) {
            responseJSON.put("success", false);
            response.getOutputStream().write(responseJSON.toString().getBytes());
            return;
        }

        FocusArea focusArea = OntologyClientUtil.getFocusArea(focusAreaId);
        Contest contest = proposalsContext.getContest(request);

        JSONObject requestJson = new JSONObject(request.getParameter("json"));
        ProposalImpactSeries impactSeries = new ProposalImpactSeries(contest, proposalsContext.getProposal(request), focusArea, requestJson);
        Member member = proposalsContext.getMember(request);
        impactSeries.persistWithAuthor(member);

        responseJSON.put("success", true);
        response.getOutputStream().write(responseJSON.toString().getBytes());
    }



    @PostMapping("/contests/{contestYear}/{contestUrlName}/c/{proposalUrlString}/{proposalId}/tab/IMPACT/proposalImpactDeleteDataSeries")
    public void proposalImpactDeleteDataSeries(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "focusAreaId") Long focusAreaId) throws IOException {

        JSONObject responseJSON = new JSONObject();
        ProposalsPermissions permissions = proposalsContext.getPermissions(request);

        if (!permissions.getCanEdit() && !permissions.getCanFellowActions() && !permissions.getCanIAFActions()) {
            responseJSON.put("success", false);
            response.getOutputStream().write(responseJSON.toString().getBytes());
            return;
        }

        FocusArea focusArea = OntologyClientUtil.getFocusArea(focusAreaId);
        Proposal proposal = proposalsContext.getProposal(request);

        for (ProposalAttribute proposalAttribute : ProposalAttributeClientUtil
                .getImpactProposalAttributes(proposal, focusArea)) {
            ProposalsContextUtil.getClients(request).getProposalAttributeClient().deleteProposalAttribute(proposalAttribute.getId_());
        }

        responseJSON.put("success", true);
        response.getOutputStream().write(responseJSON.toString().getBytes());
    }



    @PostMapping("/contests/{contestYear}/{contestUrlName}/c/{proposalUrlString}/{proposalId}/tab/IMPACT/proposalImpactUpdateAllSeries")
    public void proposalImpactUpdateAllDataSeries(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        JSONObject responseJSON = new JSONObject();
        ProposalsPermissions permissions = proposalsContext.getPermissions(request);

        if (!permissions.getCanIAFActions() && !permissions.getCanAdminProposal()) {
            responseJSON.put("success", false);
            responseJSON.put("message", "You don't have the required permission to perform this action!");
            response.getOutputStream().write(responseJSON.toString().getBytes());
            return;
        }

        Proposal proposal = proposalsContext.getProposal(request);
        Contest contest = proposalsContext.getContest(request);
        JSONObject requestJson = new JSONObject(request.getParameter("json"));

        try {
            ProposalImpactDataParser dataParser = new ProposalImpactDataParser(requestJson.getString("data"), proposal, contest);
            ProposalImpactSeriesList impactSeriesList = dataParser.parse();

            Member member = proposalsContext.getMember(request);
            impactSeriesList.persistImpactSeriesesWithAuthor(member);

            responseJSON.put("success", true);
        } catch(ProposalImpactDataParserException e) {
            _log.info("Could not parse input", e);
            responseJSON.put("success", false);
            responseJSON.put("message", e.getMessage());
        }

        response.getOutputStream().write(responseJSON.toString().getBytes());
    }


    @PostMapping("/contests/{contestYear}/{contestUrlName}/c/{proposalUrlString}/{proposalId}/tab/IMPACT/proposalImpactSaveBasicProposalComment")
    public void proposalImpactSaveBasicProposalComment(HttpServletRequest request, HttpServletResponse response,
                @RequestParam(required = false) String impactAuthorComment,
                @RequestParam(required = false) String impactIAFComment)
            throws IOException {

        JSONObject responseJSON = new JSONObject();
        ProposalsPermissions permissions = proposalsContext.getPermissions(request);

        if (!permissions.getCanEdit() && !permissions.getCanFellowActions() && !permissions.getCanIAFActions()) {
            responseJSON.put("success", false);
            response.getOutputStream().write(responseJSON.toString().getBytes());
            return;
        }
        Proposal proposal = proposalsContext.getProposalWrapped(request);

        List<ProposalUnversionedAttribute> unversionedAttributes = ProposalAttributeClientUtil
                .getProposalUnversionedAttributesByProposalId(proposal.getProposalId());

        if (impactAuthorComment != null || impactIAFComment != null) {
            if(impactAuthorComment != null) {

                ProposalsContextUtil.getClients(request).getProposalAttributeClient().createOrUpdateProposalUnversionedAttribute(proposalsContext.getMember(request).getUserId(),
                        HtmlUtil.cleanAll(impactAuthorComment),
                        ProposalUnversionedAttributeName.IMPACT_AUTHOR_COMMENT.toString(),
                        proposal.getProposalId());
            }
            if (impactIAFComment != null) {
                ProposalsContextUtil.getClients(request).getProposalAttributeClient().createOrUpdateProposalUnversionedAttribute(proposalsContext.getMember(request).getUserId(), HtmlUtil.cleanAll(impactIAFComment),
                        ProposalUnversionedAttributeName.IMPACT_IAF_COMMENT.toString(),
                        proposal.getProposalId());
            }
        }


        responseJSON.put("success", true);
        response.getOutputStream().write(responseJSON.toString().getBytes());
    }


    private JSONArray ontologyTermListToJSONArray(List<OntologyTerm> terms) {
        JSONArray array = new JSONArray();

        if (terms == null || terms.isEmpty()) {
            return array;
        }

        // Sort by order and id, which reflects the order in the outline view
        Collections.sort(terms, new Comparator<OntologyTerm>() {
            @Override
            public int compare(OntologyTerm o1, OntologyTerm o2) {
                if (o1.getOrder_() == o2.getOrder_().longValue()) {
                    return (int)(o1.getId_() - o2.getId_());
                } else {
                    return (o1.getOrder_() - o2.getOrder_());
                }

            }
        });
        for (OntologyTerm term: terms) {
            JSONObject termJson = new JSONObject();
            termJson.put("id", term.getId_());
            termJson.put("name", term.getName());
            array.put(termJson);
        }

        return array;
    }

    private Map<OntologyTerm, List<OntologyTerm>> getOntologyMap(HttpServletRequest request) {
        Contest contest = proposalsContext.getContest(request);
        Proposal proposal = proposalsContext.getProposal(request);

        ProposalImpactSeriesList proposalImpactSeriesList = new ProposalImpactSeriesList(contest, proposal);
        return new ProposalImpactUtil(contest).calculateAvailableOntologyMap(proposalImpactSeriesList.getImpactSerieses());
    }

    private ProposalImpactSeriesList getProposalImpactSeriesList(HttpServletRequest request) {
        Contest contest = proposalsContext.getContest(request);
        Proposal proposal = proposalsContext.getProposalWrapped(request);

        return new ProposalImpactSeriesList(contest, proposal.getWrapped());
    }
}
