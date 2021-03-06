package org.xcolab.service.contest.service.contestphase;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.xcolab.client.members.MembersClient;
import org.xcolab.client.members.exceptions.MemberNotFoundException;
import org.xcolab.client.members.pojo.Member;
import org.xcolab.client.proposals.ProposalClientUtil;
import org.xcolab.client.proposals.ProposalMemberRatingClientUtil;
import org.xcolab.client.proposals.ProposalPhaseClientUtil;
import org.xcolab.client.proposals.exceptions.ProposalNotFoundException;
import org.xcolab.client.proposals.pojo.Proposal;
import org.xcolab.client.proposals.pojo.evaluation.members.ProposalSupporter;
import org.xcolab.model.tables.pojos.Contest;
import org.xcolab.model.tables.pojos.ContestPhase;
import org.xcolab.service.contest.domain.contest.ContestDao;
import org.xcolab.service.contest.domain.contestphase.ContestPhaseDao;
import org.xcolab.service.contest.domain.contestphasetype.ContestPhaseTypeDao;
import org.xcolab.service.contest.exceptions.NotFoundException;
import org.xcolab.service.contest.service.contest.ContestService;
import org.xcolab.service.contest.utils.promotion.PhasePromotionHelper;
import org.xcolab.service.contest.utils.promotion.enums.ContestPhaseTypeValue;
import org.xcolab.service.contest.utils.promotion.enums.ContestStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ContestPhaseService {

    private static final Logger _log = LoggerFactory.getLogger(ContestPhaseService.class);


    @Autowired
    private ContestService contestService;


    @Autowired
    private ContestPhaseTypeDao contestPhaseTypeDao;

    @Autowired
    private ContestPhaseDao contestPhaseDao;

    @Autowired
    private ContestDao contestDao;


    public ContestStatus getContestStatus(ContestPhase contestPhase) {
        String status = contestPhaseTypeDao.get(contestPhase.getContestPhaseType()).get().getStatus();
        return status == null ? null : ContestStatus.valueOf(status);
    }

    public ContestPhase getNextContestPhase(ContestPhase contestPhase) throws NotFoundException {
        // First sort by contest phase type (the list has to be initialized as modifiable..)
        List<ContestPhase> contestPhases = new ArrayList<>(contestService.getAllContestPhases(contestPhase.getContestPK()));
        Collections.sort(contestPhases, new Comparator<ContestPhase>() {
            @Override
            public int compare(ContestPhase o1, ContestPhase o2) {
                return o1.getPhaseStartDate().compareTo(o2.getPhaseStartDate());
            }
        });

        boolean currentFound = false;
        for (ContestPhase phase : contestPhases) {
            if (currentFound) {
                return phase;
            }
            if (phase.getContestPhasePK().longValue() == contestPhase.getContestPhasePK().longValue()) {
                currentFound = true;
            }
        }
        throw new NotFoundException("Can't find next phase for phase with id: " + contestPhase.getContestPhasePK());
    }

    public void transferSupportsToVote(Contest contest) throws NotFoundException {
        ContestPhase lastOrActivePhase = contestService.getActiveOrLastPhase(contest.getContestPK());
        // Vote is only possible in Winner Selection phase
        if (lastOrActivePhase.getContestPhaseType() != ContestPhaseTypeValue.SELECTION_OF_WINNERS.getTypeId() &&
                lastOrActivePhase.getContestPhaseType() != ContestPhaseTypeValue.WINNERS_SELECTION.getTypeId() &&
                lastOrActivePhase.getContestPhaseType() != ContestPhaseTypeValue.SELECTION_OF_WINNERS_NEW.getTypeId()) {
            return;
        }

        Set<Long> proposalsUserCanVoteOn = new HashSet<>();
        for (Proposal proposal : ProposalClientUtil.getProposalsInContestPhase(lastOrActivePhase.getContestPhasePK())) {
            proposalsUserCanVoteOn.add(proposal.getProposalId());
        }
        Map<Member, List<Proposal>> userToSupportsMap = getContestSupportingUser(contest);
        for (Member user : userToSupportsMap.keySet()) {
            // Do nothing if the user has already voted
            if (ProposalMemberRatingClientUtil.hasUserVoted(lastOrActivePhase.getContestPhasePK(), user.getUserId())) {
                continue;
            }

            List<Proposal> proposals = new ArrayList<>();
            if (userToSupportsMap.containsKey(user)) {
                for (Proposal p : userToSupportsMap.get(user)) {
                    if (proposalsUserCanVoteOn.contains(p.getProposalId())) {
                        proposals.add(p);
                    }
                }
            }
            if (proposals.isEmpty()) {
                continue;
            }

            /*
            // Directly transfer the support to a vote
            try {
                Member member = MembersClient.getMember(user.getUserId());
                if (proposals.size() == 1) {
                    voteForProposal(user.getUserId(), proposals.get(0).getProposalId(), lastOrActivePhase.getContestPhasePK());
                    try{
                        org.xcolab.client.contest.pojo.Contest contestMicro = ContestClient.getContest(contest.getContestPK());
                        new ContestVoteNotification(member, contestMicro, proposals.get(0), serviceContext).sendMessage();
                    }catch (ContestNotFoundException ignored){

                    }

                }
                // Send a notification to the user
                else {
                    try {
                        org.xcolab.client.contest.pojo.Contest contestMicro = ContestClient.getContest(contest.getContestPK());
                        new ContestVoteQuestionNotification(member, contestMicro, proposals, serviceContext).sendMessage();
                    }catch (ContestNotFoundException ignored){

                    }
                }
            } catch (MemberNotFoundException e) {
                //ignore, we know it exists
            }
            */

        }
    }
    private Map<Member, List<Proposal>> getContestSupportingUser(Contest contest) {
        List<Proposal> proposalsInContest = ProposalClientUtil.getProposalsInContest(contest.getContestPK());

        HashMap<Member, List<Proposal>> userToSupportsMap = new HashMap<>();
        // Generate a list of supporting proposals for each user
        for(Proposal prop : proposalsInContest)
        for (ProposalSupporter supporter : ProposalMemberRatingClientUtil.getProposalSupporters(prop.getProposalId())) {
            try {
                Member user = MembersClient.getMember(supporter.getUserId());


                List<Proposal> proposals = userToSupportsMap.get(user);
                if (proposals == null) {
                    proposals = new ArrayList<>();
                    userToSupportsMap.put(user, proposals);
                }

                proposals.add(ProposalClientUtil.getProposal(supporter.getProposalId()));

            }catch (MemberNotFoundException | ProposalNotFoundException ignored){
            }

        }

        return userToSupportsMap;
    }

    public void forcePromotionOfProposalInPhase(Long proposalId, Long phaseId) throws NotFoundException {
        ContestPhase phase = contestPhaseDao.get(phaseId).get();
        try {
            Proposal p = ProposalClientUtil.getProposal(proposalId);
            ContestPhase nextPhase = getNextContestPhase(phase);
            PhasePromotionHelper phasePromotionHelper = new PhasePromotionHelper(phase, contestDao);
            //skip already promoted proposal
            if (phasePromotionHelper.hasProposalAlreadyBeenPromoted(p)) {
                return;
            }

            // Decide about the promotion
            if (phasePromotionHelper.didJudgeDecideToPromote(p)) {
                ProposalPhaseClientUtil.promoteProposal(p.getProposalId(), nextPhase.getContestPhasePK(), phase.getContestPhasePK());
            }

            // Enable this line to post promotion comment to Evaluation tab comment section
            // proposalLocalService.contestPhasePromotionCommentNotifyProposalContributors(p, phase);

            //TODO: Migrate logic to send email , the method is blank so there may be no need for this
        /*try {
            proposalLocalService.contestPhasePromotionEmailNotifyProposalContributors(p,  phase, null);
        } catch (MailEngineException | AddressException | UnsupportedEncodingException e) {
            _log.error("Could not send proposal promotion colab messaging notification", e);
        }*/

            _log.info("done forcefully promoting proposal " + p.getProposalId() + " from phase " + phase.getContestPhasePK());
        }catch(ProposalNotFoundException pnfe){
            _log.error("could not find proposal :" + proposalId + " in promotion from phase " + phase.getContestPhasePK());
        }
    }


}
