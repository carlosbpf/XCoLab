package org.xcolab.view.pages.proposals.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.xcolab.client.contest.pojo.phases.ContestPhase;
import org.xcolab.client.proposals.ProposalJudgeRatingClientUtil;
import org.xcolab.client.proposals.ProposalPhaseClientUtil;
import org.xcolab.client.proposals.pojo.Proposal;
import org.xcolab.client.proposals.pojo.evaluation.judges.ProposalRating;
import org.xcolab.client.proposals.pojo.proposals.ProposalRatings;
import org.xcolab.util.enums.contest.ProposalContestPhaseAttributeKeys;
import org.xcolab.util.enums.promotion.JudgingSystemActions;
import org.xcolab.view.pages.proposals.exceptions.ProposalsAuthorizationException;
import org.xcolab.view.pages.proposals.permissions.ProposalsPermissions;
import org.xcolab.view.pages.proposals.requests.FellowProposalScreeningBean;
import org.xcolab.view.pages.proposals.requests.ProposalAdvancingBean;
import org.xcolab.view.pages.proposals.utils.context.ProposalsContext;
import org.xcolab.view.pages.proposals.wrappers.ProposalFellowWrapper;
import org.xcolab.view.pages.proposals.wrappers.ProposalTab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProposalJudgesTabController extends BaseProposalTabController {
    @Autowired
    private ProposalsContext proposalsContext;

    //-- @RequestMapping(params = {"pageToDisplay=proposalDetails_ADVANCING"})
    @GetMapping({"/contests/{contestYear}/{contestUrlName}/phase/{phaseId}/{proposalUrlString}/{proposalId}/tab/ADVANCING",
            "/contests/{contestYear}/{contestUrlName}/c/{proposalUrlString}/{proposalId}/tab/ADVANCING"})

    public String showJudgesPanel(HttpServletRequest request, Model model)
            throws ProposalsAuthorizationException {

        setCommonModelAndPageAttributes(request, model, ProposalTab.ADVANCING);

        ProposalsPermissions permissions = proposalsContext.getPermissions(request);
        if (!permissions.getCanSeeAdvancingTab()) {
            throw new ProposalsAuthorizationException(ACCESS_TAB_DENIED_MESSAGE);
        }

        Proposal proposal = proposalsContext.getProposal(request);
        ContestPhase contestPhase = proposalsContext.getContestPhase(request);
        Proposal proposalWrapper = new Proposal(proposal, contestPhase);
        ProposalAdvancingBean bean = new ProposalAdvancingBean(proposalWrapper);
        bean.setContestPhaseId(contestPhase.getContestPhasePK());
        model.addAttribute("proposalAdvancingBean", bean);

        setCommonAdvancingAttributes(request, bean, model);
        return "proposals/proposalAdvancing";
    }

    //-- @RequestMapping(params = {"pageToDisplay=proposalDetails_ADVANCING", "error=true"})
    public String showJudgesPanelError(HttpServletRequest request, Model model)
            throws ProposalsAuthorizationException {
        setCommonModelAndPageAttributes(request, model, ProposalTab.ADVANCING);

        ProposalsPermissions permissions = proposalsContext.getPermissions(request);
        if (!permissions.getCanSeeAdvancingTab()) {
            throw new ProposalsAuthorizationException(ACCESS_TAB_DENIED_MESSAGE);
        }

        Proposal proposal = proposalsContext.getProposal(request);
        ContestPhase contestPhase = proposalsContext.getContestPhase(request);
        Proposal proposalWrapper = new Proposal(proposal, contestPhase);
        ProposalAdvancingBean bean = new ProposalAdvancingBean(proposalWrapper);
        bean.setContestPhaseId(contestPhase.getContestPhasePK());

        setCommonAdvancingAttributes(request, bean, model);
        return "proposalAdvancing";
    }

    private void setCommonAdvancingAttributes(HttpServletRequest request, ProposalAdvancingBean bean, Model model) {
        Proposal proposal = proposalsContext.getProposal(request);
        ContestPhase contestPhase = proposalsContext.getContestPhase(request);

        model.addAttribute("discussionId", proposal.getJudgeDiscussionId());
        model.addAttribute("emailTemplates", bean.getEmailTemplateBean().getEmailTemplates());
        model.addAttribute("advanceOptions", JudgingSystemActions.AdvanceDecision.values());


        List<ProposalRating> fellowRatingsUnWrapped = ProposalJudgeRatingClientUtil.getFellowRatingsForProposal(
                proposal.getProposalId(), contestPhase.getContestPhasePK());
        List<ProposalRatings> fellowRatings = wrapProposalRatings(fellowRatingsUnWrapped);

        List<ProposalRating> judgesRatingsUnWrapped = ProposalJudgeRatingClientUtil.getJudgeRatingsForProposal(
                proposal.getProposalId(), contestPhase.getContestPhasePK());

        for (Iterator i = judgesRatingsUnWrapped.iterator(); i.hasNext(); ){
            ProposalRating judgesRatingUnWrapped = (ProposalRating) i.next();
            if(judgesRatingUnWrapped.getOnlyForInternalUsage()) {
                i.remove();
            }
        }

        List<ProposalRatings> judgeRatings = wrapProposalRatings(judgesRatingsUnWrapped);
        boolean isFrozen = ProposalPhaseClientUtil.isProposalContestPhaseAttributeSetAndTrue(
                proposal.getProposalId(),
                contestPhase.getContestPhasePK(),
                ProposalContestPhaseAttributeKeys.FELLOW_ADVANCEMENT_FROZEN
        );
        boolean hasAlreadyBeenPromoted = ProposalPhaseClientUtil.isProposalContestPhaseAttributeSetAndTrue(
                proposal.getProposalId(),
                contestPhase.getContestPhasePK(),
                ProposalContestPhaseAttributeKeys.PROMOTE_DONE
        );

        model.addAttribute("isFrozen", isFrozen);
        model.addAttribute("hasAlreadyBeenPromoted", hasAlreadyBeenPromoted);

        model.addAttribute("fellowRatings", fellowRatings);
        model.addAttribute("judgeRatings", judgeRatings);
    }


    private static List<ProposalRatings> wrapProposalRatings(List<ProposalRating> ratings) {
        List<ProposalRatings> wrappers = new ArrayList<>();
        Map<Long, List<ProposalRating>> ratingsByUserId = new HashMap<>();

        for (ProposalRating r : ratings) {
                if (ratingsByUserId.get(r.getUserId()) == null) {
                    ratingsByUserId.put(r.getUserId(), new ArrayList<ProposalRating>());
                }
                ratingsByUserId.get(r.getUserId()).add(r);
            }

            for (Map.Entry<Long, List<ProposalRating>> entry : ratingsByUserId.entrySet()) {
                List<ProposalRating> userRatings = entry.getValue();
                ProposalRatings wrapper = new ProposalRatings(entry.getKey(), userRatings);
                wrappers.add(wrapper);
        }
        return wrappers;
    }
    

    @GetMapping({"/contests/{contestYear}/{contestUrlName}/phase/{phaseId}/{proposalUrlString}/{proposalId}/tab/SCREENING",
            "/contests/{contestYear}/{contestUrlName}/c/{proposalUrlString}/{proposalId}/tab/SCREENING"})
    public String showFellowsPanel(HttpServletRequest request, Model model) {
        setCommonModelAndPageAttributes(request, model, ProposalTab.SCREENING);

        Proposal proposal = proposalsContext.getProposal(request);
        ContestPhase contestPhase = proposalsContext.getContestPhase(request);
        Proposal proposalWrapper = new Proposal(proposal, contestPhase);
        ProposalFellowWrapper proposalFellowWrapper = new ProposalFellowWrapper(
                proposalWrapper, proposalsContext.getMember(request), request);

        boolean hasAlreadyBeenPromoted = ProposalPhaseClientUtil.isProposalContestPhaseAttributeSetAndTrue(
                proposal.getProposalId(),
                contestPhase.getContestPhasePK(),
                ProposalContestPhaseAttributeKeys.PROMOTE_DONE
        );

        FellowProposalScreeningBean bean = new FellowProposalScreeningBean(proposalFellowWrapper);
        bean.setContestPhaseId(contestPhase.getContestPhasePK());

        model.addAttribute("hasAlreadyBeenPromoted", hasAlreadyBeenPromoted);
        model.addAttribute("fellowProposalScreeningBean", bean);
        model.addAttribute("emailTemplates", bean.getEmailTemplateBean().getEmailTemplates());
        model.addAttribute("judgingOptions", JudgingSystemActions.FellowAction.values());
        model.addAttribute("discussionId", proposal.getFellowDiscussionId());

        return "proposals/proposalScreening";
    }
    
}
