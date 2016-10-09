package org.xcolab.portlets.proposals.utils.edit;


import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.theme.ThemeDisplay;

import org.xcolab.client.contest.ContestClientUtil;
import org.xcolab.client.contest.exceptions.ContestNotFoundException;
import org.xcolab.client.contest.pojo.Contest;
import org.xcolab.client.contest.pojo.phases.ContestPhase;
import org.xcolab.client.proposals.Proposal2PhaseClient;
import org.xcolab.client.proposals.ProposalAttributeClient;
import org.xcolab.client.proposals.ProposalMoveHistoryClient;
import org.xcolab.client.proposals.ProposalsClient;
import org.xcolab.client.proposals.enums.ProposalAttributeKeys;
import org.xcolab.client.proposals.exceptions.Proposal2PhaseNotFoundException;
import org.xcolab.client.proposals.exceptions.ProposalNotFoundException;
import org.xcolab.client.proposals.pojo.Proposal;
import org.xcolab.client.proposals.pojo.Proposal2Phase;
import org.xcolab.client.proposals.pojo.ProposalAttribute;
import org.xcolab.portlets.proposals.requests.UpdateProposalDetailsBean;
import org.xcolab.portlets.proposals.wrappers.ProposalWrapper;
import org.xcolab.utils.emailnotification.proposal.ProposalCreationNotification;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

public final class ProposalCreationUtil {

    private final static Set<String> attributesNotToBeCopiedFromBaseProposal = new HashSet<>();

    static {
        attributesNotToBeCopiedFromBaseProposal.add(ProposalAttributeKeys.SECTION);
        attributesNotToBeCopiedFromBaseProposal.add(ProposalAttributeKeys.DESCRIPTION);
        attributesNotToBeCopiedFromBaseProposal.add(ProposalAttributeKeys.NAME);
        attributesNotToBeCopiedFromBaseProposal.add(ProposalAttributeKeys.PITCH);
        attributesNotToBeCopiedFromBaseProposal.add(ProposalAttributeKeys.TEAM);
    }

    private ProposalCreationUtil() {
    }

    public static ProposalWrapper createProposal(long userId,
                                                 @Valid UpdateProposalDetailsBean updateProposalSectionsBean, Contest contest, ThemeDisplay themeDisplay,
                                                 ContestPhase contestPhase) throws PortalException,
            SystemException {
        try {
            Proposal newProposal = ProposalsClient.createProposal(userId, contestPhase.getContestPhasePK(), true);
            Proposal2Phase newProposal2Phase = Proposal2PhaseClient.getProposal2PhaseByProposalIdContestPhaseId(
                    newProposal.getProposalId(), contestPhase.getContestPhasePK());

            ProposalWrapper proposalWrapper = new ProposalWrapper(newProposal, 0, contest, contestPhase, newProposal2Phase);

            final long baseProposalId = updateProposalSectionsBean.getBaseProposalId();
            if (baseProposalId > 0) {
                ProposalAttributeClient.setProposalAttribute(
                        themeDisplay.getUserId(), proposalWrapper.getProposalId(), ProposalAttributeKeys.BASE_PROPOSAL_ID,0l,
                        baseProposalId);
                final long baseContestId = updateProposalSectionsBean.getBaseProposalContestId();
                ProposalAttributeClient.setProposalAttribute(themeDisplay.getUserId(), proposalWrapper.getProposalId(),
                        ProposalAttributeKeys.BASE_PROPOSAL_CONTEST_ID,
                        0l,baseContestId);
                ProposalMoveHistoryClient.createForkProposalMoveHistory(baseProposalId, proposalWrapper.getProposalId(),
                        baseContestId, contest.getContestPK(), 0L, contestPhase.getContestPhasePK(), userId);

                for (ProposalAttribute attribute : ProposalAttributeClient
                        .getAllProposalAttributes(baseProposalId)) {
                    if (attributesNotToBeCopiedFromBaseProposal.contains(attribute.getName())) {
                        continue;
                    }
                    ProposalAttributeClient.setProposalAttribute(themeDisplay.getUserId(),
                            proposalWrapper.getProposalId(), attribute.getName(), attribute.getAdditionalId(),
                            attribute.getStringValue(), attribute.getNumericValue(), attribute.getRealValue());
                }
            }
            return proposalWrapper;
        }catch (Proposal2PhaseNotFoundException ignored){
            return null;
        }
    }

    public static void sendAuthorNotification(ThemeDisplay themeDisplay,
                                              ProposalWrapper proposalWrapper, ContestPhase contestPhase) throws PortalException,
            SystemException {
        ServiceContext serviceContext = new ServiceContext();
        serviceContext.setPortalURL(themeDisplay.getPortalURL());
        try {
            Contest contest = ContestClientUtil
                    .getContest(ContestClientUtil.getContestPhase(contestPhase.getContestPhasePK()).getContestPK());

            Proposal updatedProposal = ProposalsClient.getProposal(proposalWrapper.getProposalId());
            org.xcolab.client.contest.pojo.Contest contestMicro = ContestClientUtil.getContest(contest.getContestPK());
            new ProposalCreationNotification(updatedProposal, contestMicro, serviceContext).sendMessage();
        } catch (ContestNotFoundException | ProposalNotFoundException ignored) {

        }
    }
}
