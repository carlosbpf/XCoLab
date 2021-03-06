package org.xcolab.view.pages.proposals.view.action;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.xcolab.client.contest.exceptions.ContestNotFoundException;
import org.xcolab.client.contest.pojo.Contest;
import org.xcolab.client.proposals.ProposalMemberRatingClient;
import org.xcolab.entity.utils.analytics.AnalyticsUtil;
import org.xcolab.view.pages.loginregister.SharedColabUtil;
import org.xcolab.view.pages.proposals.exceptions.ProposalsAuthorizationException;
import org.xcolab.view.pages.proposals.utils.context.ProposalsContext;
import org.xcolab.view.pages.proposals.utils.context.ProposalsContextUtil;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SupportProposalActionController {

    @Autowired
    private ProposalsContext proposalsContext;
    
    private final static String SUPPORT_ANALYTICS_KEY = "SUPPORTED_ENTRIES";
    private final static String SUPPORT_ANALYTICS_CATEGORY = "User";
    private final static String SUPPORT_ANALYTICS_ACTION = "Support contest entry";
    private final static String SUPPORT_ANALYTICS_LABEL = "";


    //-- @RequestMapping(params = {"action=supportProposalAction"})
    @GetMapping("/contests/{contestYear}/{contestUrlName}/c/{proposalUrlString}/{proposalId}/supportProposalAction")
    public synchronized void handleAction(HttpServletRequest request, Model model, HttpServletResponse response,
            @RequestParam(required = false) String forwardToTab)
            throws ProposalsAuthorizationException, IOException {
        
        if (proposalsContext.getPermissions(request).getCanSupportProposal()) {
            long memberId = proposalsContext.getMember(request).getUserId();
            long proposalId = proposalsContext.getProposal(request).getProposalId();
            ProposalMemberRatingClient proposalMemberRatingClient = proposalsContext.getClients(request).getProposalMemberRatingClient();
            if (proposalMemberRatingClient.isMemberProposalSupporter(proposalId, memberId)) {
                proposalMemberRatingClient.removeProposalSupporter(proposalId, memberId);
            }
            else {
                proposalMemberRatingClient.addProposalSupporter(proposalId, memberId);
                int supportedCount = proposalMemberRatingClient.getProposalSupportersCount(memberId);
                if (supportedCount > 0) {
                    int analyticsValue = AnalyticsUtil.getAnalyticsValueForCount(supportedCount);
                    AnalyticsUtil.publishEvent(request, memberId, SUPPORT_ANALYTICS_KEY + analyticsValue,
            			SUPPORT_ANALYTICS_CATEGORY,
            			SUPPORT_ANALYTICS_ACTION,
            			SUPPORT_ANALYTICS_LABEL,
            			analyticsValue);
                }
                try {
                    Contest contest = ProposalsContextUtil.getClients(request).getProposalClient().getLatestContestInProposal(proposalId);
                    SharedColabUtil.checkTriggerForAutoUserCreationInContest(contest.getContestPK(), memberId);
                }catch (ContestNotFoundException ignore){

                }
            }
            String proposalLinkUrl = proposalsContext.getProposal(request).getProposalLinkUrl(proposalsContext.getContest(request));
            if (!StringUtils.isBlank(forwardToTab)) {
                proposalLinkUrl += "/tab/" + forwardToTab;
            }
            response.sendRedirect(proposalLinkUrl);
        }
        else {
            throw new ProposalsAuthorizationException("User isn't allowed to toggle support for proposal");
        }
    }

}
