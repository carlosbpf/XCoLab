package org.xcolab.portlets.proposals.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;

import org.xcolab.client.admin.enums.ConfigurationAttributeKey;
import org.xcolab.client.contest.ContestClientUtil;
import org.xcolab.client.contest.exceptions.ContestNotFoundException;
import org.xcolab.client.contest.pojo.Contest;
import org.xcolab.client.contest.pojo.ContestType;
import org.xcolab.client.contest.pojo.phases.ContestPhase;
import org.xcolab.client.contest.pojo.templates.PlanSectionDefinition;
import org.xcolab.client.flagging.FlaggingClient;
import org.xcolab.client.proposals.ProposalClientUtil;
import org.xcolab.client.proposals.ProposalMoveClientUtil;
import org.xcolab.client.proposals.pojo.ContestTypeProposal;
import org.xcolab.client.proposals.pojo.Proposal;
import org.xcolab.client.proposals.pojo.phases.ProposalMoveHistory;
import org.xcolab.enums.ContestPhaseTypeValue;
import org.xcolab.portlets.proposals.permissions.ProposalsPermissions;
import org.xcolab.portlets.proposals.requests.JudgeProposalFeedbackBean;
import org.xcolab.portlets.proposals.requests.UpdateProposalDetailsBean;
import org.xcolab.portlets.proposals.utils.context.ProposalsContext;
import org.xcolab.portlets.proposals.utils.context.ProposalsContextUtil;
import org.xcolab.portlets.proposals.wrappers.MoveHistoryWrapper;
import org.xcolab.portlets.proposals.wrappers.ProposalJudgeWrapper;
import org.xcolab.portlets.proposals.wrappers.ProposalTab;
import org.xcolab.util.enums.flagging.TargetType;
import org.xcolab.util.enums.proposal.MoveType;
import org.xcolab.util.exceptions.DatabaseAccessException;
import org.xcolab.utils.EntityGroupingUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletRequest;

@Controller
@RequestMapping("view")
public class ProposalSectionsTabController extends BaseProposalTabController {

    @Autowired
    private ProposalsContext proposalsContext;

    @RequestMapping(params = "pageToDisplay=proposalDetails")
    public String showProposalDetails(
            @RequestParam Long proposalId,
            @RequestParam String contestUrlName,
            @RequestParam(required = false) Long phaseId,
            @RequestParam(defaultValue = "false") boolean edit,
            @RequestParam(defaultValue = "false") boolean isMove,
            @RequestParam(defaultValue = "false") String moveType,
            @RequestParam(required = false) Long moveFromContestPhaseId,
            @RequestParam(defaultValue = "false") boolean voted,
            Model model, PortletRequest request) throws PortalException {

        setCommonModelAndPageAttributes(request, model, ProposalTab.DESCRIPTION);

        boolean editValidated = false;
        final ProposalsPermissions proposalsPermissions = proposalsContext.getPermissions(request);
        if (edit && proposalsPermissions.getCanEdit()) {
            editValidated = true;
        }
        model.addAttribute("edit", editValidated);
        model.addAttribute("voted", voted);
        model.addAttribute("reportTargets", FlaggingClient.listReportTargets(TargetType.PROPOSAL));

        final Proposal proposal = proposalsContext.getProposal(request);
        final Proposal proposalWrapped = proposalsContext.getProposalWrapped(request);
        try {
            final Contest baseContest = ProposalsContextUtil.getClients(request).getProposalClient().getCurrentContestForProposal(proposal.getProposalId());

            if (voted) {
                setVotingDeadline(model, baseContest);
            }

            if (isMove) {
                // get base proposal from base contest
                ContestPhase baseContestPhase = ContestClientUtil.getActivePhase(baseContest.getContestPK());

                Proposal baseProposalWrapped = new Proposal(proposal, proposal.getCurrentVersion(),
                        baseContest, baseContestPhase, null);
                model.addAttribute("baseProposal", baseProposalWrapped);



                model.addAttribute("baseContest", baseContest);//baseContest


                model.addAttribute("isMove", true);

                UpdateProposalDetailsBean updateProposalDetailsBean = new UpdateProposalDetailsBean(
                        proposalWrapped, baseProposalWrapped, true, MoveType.valueOf(moveType));
                updateProposalDetailsBean.setMoveFromContestPhaseId(moveFromContestPhaseId);
                // find sections that can't be mapped without user interaction

                Set<Long> newContestSections = new HashSet<>();

                for (PlanSectionDefinition section : proposalWrapped.getSections()) {
                    newContestSections.add(section.getSectionDefinitionId());
                }

                boolean hasNotMappedSections = false;
                for (PlanSectionDefinition section : baseProposalWrapped.getSections()) {
                    if (section.getContent() != null && !section.getContent().trim().isEmpty()) {
                        // we have non empty section in base proposal, check if such
                        // section exists in target contest
                        if (!newContestSections.contains(section.getSectionDefinitionId())) {
                            hasNotMappedSections = true;
                        }
                    }

                }

                updateProposalDetailsBean.setMoveToContestId(baseContestPhase.getContestPhasePK());
                model.addAttribute("updateProposalSectionsBean", updateProposalDetailsBean);
                model.addAttribute("hasNotMappedSections", hasNotMappedSections);
            } else {
                model.addAttribute("updateProposalSectionsBean", new UpdateProposalDetailsBean(
                        proposalWrapped));
            }


            if (editValidated || isMove) {
                request.setAttribute("imageUploadServiceUrl",
                        ConfigurationAttributeKey.IMAGE_UPLOAD_EXTERNAL_SERVICE_URL.get());
                request.setAttribute("imageUploadHelpText",
                        ConfigurationAttributeKey.IMAGE_UPLOAD_HELP_TEXT.get());

                model.addAttribute("mustFilterContent", ConfigurationAttributeKey.FILTER_PROFANITY.get());

                return "proposalDetails_edit";
            }
            if (proposalsPermissions.getCanJudgeActions()) {
                setJudgeProposalBean(model, request);
            }
            setLinkedProposals(model, proposal);
            final Contest contest = proposalsContext.getContest(request);
            populateMoveHistory(model, proposal, contest);
        } catch (ContestNotFoundException ignored) {

        } catch (SystemException e) {
            throw new DatabaseAccessException(e);
        }

        return "proposalDetails";
    }

    private void populateMoveHistory(Model model, Proposal proposal, Contest contest) {
        List<ProposalMoveHistory> sourceMoveHistoriesRaw = ProposalMoveClientUtil
                .getBySourceProposalIdContestId(proposal.getProposalId(), contest.getContestPK());
        List<MoveHistoryWrapper> sourceMoveHistories = new ArrayList<>();

        for (ProposalMoveHistory sourceMoveHistory : sourceMoveHistoriesRaw) {
            sourceMoveHistories.add(new MoveHistoryWrapper(sourceMoveHistory));
        }
        model.addAttribute("sourceMoveHistories", sourceMoveHistories);


        ProposalMoveHistory targetMoveHistoryRaw = ProposalMoveClientUtil
                .getByTargetProposalIdContestId(proposal.getProposalId(), contest.getContestPK());
        if (targetMoveHistoryRaw != null) {
            MoveHistoryWrapper targetMoveHistory = new MoveHistoryWrapper(targetMoveHistoryRaw);
            model.addAttribute("targetMoveHistory", targetMoveHistory);
        }

    }

    private void setLinkedProposals(Model model, Proposal proposal)
            throws PortalException, SystemException {
        List<Proposal> linkedProposals = ProposalClientUtil
                .getSubproposals(proposal.getProposalId(), true);
        Map<ContestType, List<Proposal>> proposalsByContestType =
                EntityGroupingUtil.groupByContestType(linkedProposals);
        Map<Long, ContestTypeProposal> contestTypeProposalWrappersByContestTypeId = new HashMap<>();

        for (ContestType contestType : ContestClientUtil.getActiveContestTypes()) {
            contestTypeProposalWrappersByContestTypeId.put(contestType.getId_(),
                    new ContestTypeProposal(contestType));
            final List<Proposal> proposalsInContestType = proposalsByContestType.get(contestType);
            for (Proposal p : proposalsInContestType) {
                contestTypeProposalWrappersByContestTypeId.get(contestType.getId_())
                        .getProposals().add((p));
            }
        }
        model.addAttribute("linkedProposalContestTypeProposalWrappersByContestTypeId",
                contestTypeProposalWrappersByContestTypeId);
    }

    private void setJudgeProposalBean(Model model, PortletRequest request) {
        Proposal proposalWrapper = proposalsContext.getProposalWrapped(request);
        ProposalJudgeWrapper proposalJudgeWrapper = new ProposalJudgeWrapper(
                proposalWrapper, proposalsContext.getMember(request));
        JudgeProposalFeedbackBean judgeProposalBean = new JudgeProposalFeedbackBean(proposalJudgeWrapper);
        long contestPhaseId = proposalsContext.getContestPhase(request).getContestPhasePK();
        judgeProposalBean.setContestPhaseId(contestPhaseId);

        model.addAttribute("judgeProposalBean", judgeProposalBean);
    }

    private void setVotingDeadline(Model model, Contest baseContest) {
        Date votingDeadline = getVotingDeadline(baseContest);
        if (votingDeadline != null) {
            final DateFormat customDateFormat = new SimpleDateFormat("MMMM dd, YYYY", Locale.US);
            model.addAttribute("votingDeadline", customDateFormat.format(votingDeadline));
        } else {
            model.addAttribute("votingDeadline", "");
        }
    }

    private Date getVotingDeadline(Contest contest) {
        List<ContestPhase> contestPhases = ContestClientUtil.getAllContestPhases(contest.getContestPK());
        return getActiveVotingPhase(contestPhases).getPhaseEndDate();
    }

    private ContestPhase getActiveVotingPhase(List<ContestPhase> contestPhases) {
        for (ContestPhase phase : contestPhases) {
            if (phase.getContestPhaseType() == ContestPhaseTypeValue.SELECTION_OF_WINNERS.getTypeId() ||
                    phase.getContestPhaseType() == ContestPhaseTypeValue.SELECTION_OF_WINNERS_NEW.getTypeId() ||
                    phase.getContestPhaseType() == ContestPhaseTypeValue.WINNERS_SELECTION.getTypeId()) {
                if (phase.getPhaseActive()) {
                    return phase;
                }
            }
        }

        throw new IllegalStateException("Active voting phase was not found");
    }
}
