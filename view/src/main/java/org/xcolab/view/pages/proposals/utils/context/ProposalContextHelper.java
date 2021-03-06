package org.xcolab.view.pages.proposals.utils.context;

import org.apache.commons.lang3.StringUtils;

import org.xcolab.client.contest.ContestClient;
import org.xcolab.client.contest.ContestClientUtil;
import org.xcolab.client.contest.exceptions.ContestNotFoundException;
import org.xcolab.client.contest.pojo.Contest;
import org.xcolab.client.contest.pojo.phases.ContestPhase;
import org.xcolab.client.members.PermissionsClient;
import org.xcolab.client.members.pojo.Member;
import org.xcolab.client.proposals.ProposalClient;
import org.xcolab.client.proposals.ProposalPhaseClient;
import org.xcolab.client.proposals.exceptions.Proposal2PhaseNotFoundException;
import org.xcolab.client.proposals.exceptions.ProposalNotFoundException;
import org.xcolab.client.proposals.pojo.Proposal;
import org.xcolab.client.proposals.pojo.phases.Proposal2Phase;
import org.xcolab.entity.utils.portlet.RequestParamUtil;

import org.xcolab.util.exceptions.ReferenceResolutionException;
import org.xcolab.view.auth.MemberAuthUtil;
import org.xcolab.view.pages.proposals.wrappers.ProposalJudgeWrapper;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class ProposalContextHelper {

    private static final String PROPOSAL_ID_PARAM = "proposalId";
    private static final String PLAN_ID_PARAM = "planId";
    private static final String CONTEST_ID_PARAM = "contestId";
    private static final String CONTEST_URL_NAME_PARAM = "contestUrlName";
    private static final String CONTEST_YEAR_PARAM = "contestYear";
    private static final String CONTEST_PHASE_ID_PARAM = "phaseId";
    private static final String VERSION_PARAM = "version";

    private final String givenContestUrlName;
    private final long givenContestYear;
    private final long givenContestId;
    private final long givenPhaseId;
    private final int givenVersion;
    private final long givenProposalId;
    private final HttpServletRequest request;
    private final HashMap<String,String> pathParameters;
    private final Contest contest;
    private final ClientHelper clientHelper;

    public ProposalContextHelper(HttpServletRequest request,HashMap<String,String>  pathParameters) {
        this.request = request;
        this.pathParameters = pathParameters;
        final long proposalIdParam = RequestParamUtil.getLong(request, PROPOSAL_ID_PARAM,pathParameters);
        givenProposalId = (proposalIdParam == 0)
                ? RequestParamUtil.getLong(request, PLAN_ID_PARAM,pathParameters) : proposalIdParam;
        givenContestUrlName = RequestParamUtil.getString(request, CONTEST_URL_NAME_PARAM,pathParameters);
        givenContestYear = RequestParamUtil.getLong(request, CONTEST_YEAR_PARAM,pathParameters);
        givenContestId = RequestParamUtil.getLong(request, CONTEST_ID_PARAM,pathParameters);
        givenPhaseId = RequestParamUtil.getLong(request, CONTEST_PHASE_ID_PARAM,pathParameters);
        givenVersion = RequestParamUtil.getInteger(request, VERSION_PARAM,pathParameters);

        Contest transientContest = fetchContest();

        clientHelper = new ClientHelper(transientContest);
        if (transientContest != null) {
            contest = setupContestFromTheRightClient(transientContest.getContestPK());
        }else{
            contest = null;
        }
    }

    private Contest setupContestFromTheRightClient(Long contestId) {
        Contest localContest = null;
        try {
            localContest = clientHelper.getContestClient().getContest(contestId);
        } catch (ContestNotFoundException ignored) {

        }
        return localContest;
    }

    public Member getMember() {
        return MemberAuthUtil.getMemberOrNull(request);
    }

    private Contest fetchContest() {
        Contest localContest = null;
        if (StringUtils.isNotBlank(givenContestUrlName) && givenContestYear > 0) {
            localContest = ContestClientUtil
                    .getContestByContestUrlNameContestYear(givenContestUrlName, givenContestYear);
        } else if (givenContestId > 0) {
            try {
                localContest = ContestClientUtil.getContest(givenContestId);
            } catch (ContestNotFoundException ignored) {
            }
        }
        return localContest;
    }

    public Contest getContest() throws InvalidAccessException {
        if (contest == null) {
            final boolean contestUserSupplied = StringUtils.isNotBlank(givenContestUrlName)
                    || givenContestId > 0;
            if (contestUserSupplied) {
                throw new InvalidAccessException();
            }
        }
        return contest;
    }

    public ClientHelper getClientHelper() {
        return clientHelper;
    }

    public ContestPhase getContestPhase(Contest contest, Proposal proposal) {
        final ContestClient contestClient = clientHelper.getContestClient();
        final ProposalClient proposalClient = clientHelper.getProposalClient();

        ContestPhase contestPhase;
        if (givenPhaseId > 0) {
            contestPhase = contestClient.getContestPhase(givenPhaseId);
        } else if (proposal != null) {
            contestPhase = proposalClient.getLatestContestPhaseInProposal(proposal.getProposalId());
        } else {
            contestPhase = contestClient.getActivePhase(contest.getContestPK());
        }

        if (contestPhase == null) {
            throw ReferenceResolutionException
                    .toObject(ContestPhase.class, "")
                    .fromObject(Contest.class, contest.getContestPK());
        }
        return contestPhase;
    }

    public Proposal2Phase getProposal2Phase(ContestPhase contestPhase) {
        final ProposalPhaseClient proposalPhaseClient = clientHelper.getProposalPhaseClient();
        try {
            return proposalPhaseClient
                    .getProposal2PhaseByProposalIdContestPhaseId(givenProposalId,
                            contestPhase.getContestPhasePK());
        } catch (Proposal2PhaseNotFoundException e) {
            return null;
        }
    }

    public Proposal getProposal() throws InvalidAccessException {
        final ProposalClient proposalClient = clientHelper.getProposalClient();
        Proposal proposal = null;
        if (givenProposalId > 0) {
            try {
                proposal = proposalClient.getProposal(givenProposalId);
            } catch (ProposalNotFoundException e) {
                throw new InvalidAccessException();
            }
        }
        return proposal;
    }

    public Proposal getProposalWrapper(Proposal proposal, Proposal2Phase proposal2Phase,
            ContestPhase contestPhase, Contest contest, Member member) {
        Proposal proposalWrapper;
        if (givenVersion > 0) {
            if (member != null && PermissionsClient
                    .canJudge(member.getUserId(), contest.getContestPK())) {
                proposalWrapper = new ProposalJudgeWrapper(proposal, givenVersion,
                        contest, contestPhase, proposal2Phase, member);
            } else {
                proposalWrapper = new Proposal(proposal, givenVersion,
                        contest,
                        contestPhase, proposal2Phase);
            }
        } else {
            final boolean hasVersionTo =
                    proposal2Phase != null && proposal2Phase.getVersionTo() > 0;
            final int localVersion =
                    hasVersionTo ? proposal2Phase.getVersionTo()
                            : proposal.getCurrentVersion();

            if (member != null && PermissionsClient
                    .canJudge(member.getUserId(), contest.getContestPK())) {
                proposalWrapper = new ProposalJudgeWrapper(proposal,
                        localVersion,
                        contest, contestPhase, proposal2Phase, member);
            } else {
                proposalWrapper = new Proposal(proposal, localVersion,
                        contest, contestPhase, proposal2Phase);
            }
        }
        return proposalWrapper;
    }

    public long getGivenPhaseId() {
        return givenPhaseId;
    }

    public String getGivenContestUrlName() {
        return givenContestUrlName;
    }

    public long getGivenContestYear() {
        return givenContestYear;
    }

    public long getGivenContestId() {
        return givenContestId;
    }

    public int getGivenVersion() {
        return givenVersion;
    }

    public long getGivenProposalId() {
        return givenProposalId;
    }

    public static class InvalidAccessException extends Exception {

    }
}
