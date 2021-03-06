package org.xcolab.service.proposal.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.xcolab.client.members.pojo.Member;
import org.xcolab.client.proposals.exceptions.ProposalNotFoundException;
import org.xcolab.client.proposals.pojo.phases.Proposal2Phase;
import org.xcolab.model.tables.pojos.Proposal;
import org.xcolab.model.tables.pojos.ProposalContestPhaseAttribute;
import org.xcolab.model.tables.pojos.ProposalVersion;
import org.xcolab.model.tables.pojos.ProposalVote;
import org.xcolab.service.proposal.domain.proposal.ProposalDao;
import org.xcolab.service.proposal.domain.proposal2phase.Proposal2PhaseDao;
import org.xcolab.service.proposal.domain.proposalcontestphaseattribute.ProposalContestPhaseAttributeDao;
import org.xcolab.service.proposal.domain.proposalversion.ProposalVersionDao;
import org.xcolab.service.proposal.domain.proposalvote.ProposalVoteDao;
import org.xcolab.service.proposal.exceptions.NotFoundException;
import org.xcolab.service.proposal.service.proposal.ProposalService;
import org.xcolab.service.proposal.service.proposal.ProposalVersionService;
import org.xcolab.service.proposal.service.proposal2phase.Proposal2PhaseService;
import org.xcolab.service.utils.PaginationHelper;
import org.xcolab.util.enums.contest.ProposalContestPhaseAttributeKeys;

import java.util.List;

@RestController
public class ProposalsController {

    private final ProposalDao proposalDao;
    private final ProposalService proposalService;

    private final ProposalVoteDao proposalVoteDao;
    private final Proposal2PhaseService proposal2PhaseService;

    private final ProposalVersionDao proposalVersionDao;
    private final ProposalVersionService proposalVersionService;
    private final ProposalContestPhaseAttributeDao proposalContestPhaseAttributeDao;

    @Autowired
    public ProposalsController(ProposalContestPhaseAttributeDao proposalContestPhaseAttributeDao,
            ProposalVersionDao proposalVersionDao, ProposalDao proposalDao,
            ProposalVoteDao proposalVoteDao, Proposal2PhaseService proposal2PhaseService,
            ProposalVersionService proposalVersionService,
            ProposalService proposalService) {
        this.proposalContestPhaseAttributeDao = proposalContestPhaseAttributeDao;
        this.proposalVersionDao = proposalVersionDao;
        this.proposalDao = proposalDao;
        this.proposalVoteDao = proposalVoteDao;
        this.proposal2PhaseService = proposal2PhaseService;
        this.proposalService = proposalService;
        this.proposalVersionService = proposalVersionService;

    }

    @RequestMapping(value = "/proposals", method = RequestMethod.POST)
    public Proposal createProposal(@RequestBody Proposal proposal) {
        return this.proposalDao.create(proposal);
    }

    @RequestMapping(value = "/proposals", method = {RequestMethod.GET, RequestMethod.HEAD})
    public List<Proposal> listProposals(
            @RequestParam(required = false) Integer startRecord,
            @RequestParam(required = false) Integer limitRecord,
            @RequestParam(required = false) Long contestId,
            @RequestParam(required = false) Boolean visible,
            @RequestParam(required = false) Long contestPhaseId,
            @RequestParam(required = false) Integer ribbon,
            @RequestParam(required = false) String sort) {
        PaginationHelper paginationHelper = new PaginationHelper(startRecord, limitRecord, sort);

        return proposalDao
                .findByGiven(paginationHelper, null, contestId, visible, contestPhaseId, ribbon);
    }

    @RequestMapping(value = "/proposals/{proposalId}", method = RequestMethod.GET)
    public Proposal getProposal(@PathVariable long proposalId,
                                @RequestParam(required = false, defaultValue = "false") boolean includeDeleted)
            throws NotFoundException {
        final Proposal proposal = proposalDao.get(proposalId);
        if (proposal.getVisible() || includeDeleted) {
            return proposal;
        }
        throw new NotFoundException();
    }

    @GetMapping("/proposals/{proposalId}/phaseIds")
    public List<Long> getContestPhasesForProposal(@PathVariable long proposalId) {
        return proposal2PhaseService.getContestPhasesForProposal(proposalId);
    }

    @RequestMapping(value = "/proposals/{proposalId}/contestIntegrationRelevantSubproposal", method = RequestMethod.GET)
    public List<Proposal> listProposals(@PathVariable long proposalId) {
        return proposalService.getContestIntegrationRelevantSubproposals(proposalId);
    }

    @GetMapping("/proposals/{proposalId}/getLatestContestPhaseIdInProposal")
    public Long getLatestContestPhaseIdInProposal(@PathVariable long proposalId)
            throws NotFoundException {
        return proposalService.getLatestContestPhaseIdInProposal(proposalId);
    }


    @RequestMapping(value = "/proposals/{proposalId}/getSubproposals", method = RequestMethod.GET)
    public List<Proposal> listProposals(@PathVariable long proposalId, @RequestParam Boolean includeProposalsInSameContest) {
        return proposalService.getSubproposals(proposalId, includeProposalsInSameContest);
    }

    @RequestMapping(value = "/proposals/getMemberProposals", method = RequestMethod.GET)
    public List<Proposal> getMemberProposals(@RequestParam long userId) {
        return proposalService.getMemberProposals(userId);
    }


    @RequestMapping(value = "/proposals/{proposalId}/materializedPoints", method = RequestMethod.GET)
    public Integer getMaterializedPoints(@PathVariable long proposalId) {
        return proposalDao.getProposalMaterializedPoints(proposalId);
    }

    @RequestMapping(value = "/proposals/createProposal", method = RequestMethod.POST)
    public Proposal createProposal(@RequestParam long authorId, @RequestParam long contestPhaseId,  @RequestParam boolean publishActivity) {
        return proposalService.create(authorId, contestPhaseId, publishActivity);
    }

    @RequestMapping(value = "/proposals/{proposalId}", method = RequestMethod.PUT)
    public boolean updateProposal(@RequestBody Proposal proposal, @PathVariable long proposalId)
            throws NotFoundException {
        proposalDao.get(proposalId);
        return proposalDao.update(proposal);
    }

    @RequestMapping(value = "/proposals/{proposalId}", method = RequestMethod.DELETE)
    public boolean deleteProposal(@PathVariable long proposalId)
            throws NotFoundException {
        final Proposal proposal = proposalDao.get(proposalId);
        proposal.setVisible(false);
        return proposalDao.update(proposal);
    }

    @RequestMapping(value = "/proposals/numberOfProposalsForJudge", method = RequestMethod.GET)
    public Integer numberOfProposalsForJudge(@RequestParam long contestPhaseId, @RequestParam long userId)
            throws NotFoundException {
        PaginationHelper paginationHelper = new PaginationHelper(0, Integer.MAX_VALUE, null);

        List<Proposal> proposals = proposalDao.findByGiven(paginationHelper, null, null, null, contestPhaseId, null);
        int counter = 0;
        for (Proposal p : proposals) {
            ProposalContestPhaseAttribute pcpa = proposalContestPhaseAttributeDao.getByProposalIdContestPhaseIdName(p.getProposalId(), contestPhaseId, ProposalContestPhaseAttributeKeys.SELECTED_JUDGES);
            if(pcpa == null ){
                pcpa = new ProposalContestPhaseAttribute();
                pcpa.setProposalId(p.getProposalId());
                pcpa.setContestPhaseId(contestPhaseId);
                pcpa.setName(ProposalContestPhaseAttributeKeys.SELECTED_JUDGES);
                pcpa.setStringValue("");
                pcpa.setNumericValue(0L);
                pcpa.setRealValue(0.0);
                pcpa.setAdditionalId(0L);
                pcpa = proposalContestPhaseAttributeDao.create(pcpa);
            }
            String judges = pcpa.getStringValue();
            if (StringUtils.containsIgnoreCase(judges, userId + "")) {
                counter++;
            }
        }
        return counter;
    }

    @RequestMapping(value = "/proposals/{proposalId}/isUserInProposalTeam", method = RequestMethod.GET)
    public Boolean isUserOnTeam(@PathVariable Long proposalId, @RequestParam Long memberUserId)
            throws NotFoundException {
            return proposalService.isUserAMember(proposalId, memberUserId);
    }

    @RequestMapping(value = "/proposals/{proposalId}/removeUserFromProposalTeam", method = RequestMethod.DELETE)
    public Boolean removeUserFromProposalTeam(@PathVariable Long proposalId, @RequestParam Long memberUserId)
            throws NotFoundException {

        try {
            proposalService.removeProposalTeamMember(proposalId, memberUserId);
            return true;
        } catch (ProposalNotFoundException ignored) {
        }
        throw new NotFoundException();
    }

    @RequestMapping(value = "/proposals/{proposalId}/allMembers", method = RequestMethod.GET)
    public List<Member> getProposalMembers(@PathVariable Long proposalId)
            throws NotFoundException {

        try {
            return proposalService.getProposalMembers(proposalId);
        } catch (ProposalNotFoundException ignored) {

        }
        throw new NotFoundException();
    }

    @RequestMapping(value = "/proposalVersions", method = {RequestMethod.GET, RequestMethod.HEAD})
    public List<ProposalVersion> getProposalVersions(
            @RequestParam(required = false) Long proposalId
    ) {
        return proposalVersionDao.findByGiven(proposalId, null);
    }

    @RequestMapping(value = "/proposalVersions/getGroupedVersionsByContest", method = {RequestMethod.GET, RequestMethod.HEAD})
    public List<ProposalVersion> getGroupedVersionsByContest(
            @RequestParam(required = false) Long proposalId,
            @RequestParam(required = false) Long contestId,
            @RequestParam(required = false) Integer start,
            @RequestParam(required = false) Integer end

    ) {
        return proposalVersionService.getGroupedProposalVersionsForProposalAndContest(proposalId,contestId,start, end);
    }
    @RequestMapping(value = "/proposalVersions/countGroupedVersionsByContest", method = {RequestMethod.GET, RequestMethod.HEAD})
    public Integer countGroupedVersionsByContest(
            @RequestParam(required = false) Long proposalId,
            @RequestParam(required = false) Long contestId
    ) {
        return proposalVersionService.getGroupedProposalVersionsForProposalAndContest(proposalId,contestId,0, Integer.MAX_VALUE).size();
    }

    @RequestMapping(value = "/proposalVersions/count", method = RequestMethod.GET)
    public Integer getProposalVersionsCount(
            @RequestParam(required = false) Long proposalId
    ) {
        return proposalVersionDao.countByGiven(proposalId);
    }

    @RequestMapping(value = "/proposalVersions/getByProposalIdVersion", method = RequestMethod.GET)
    public ProposalVersion getProposalVersion(@RequestParam("proposalId") Long proposalId,
                                              @RequestParam("version") Integer version) throws NotFoundException {
        if (proposalId == null || proposalId == 0) {
            throw new NotFoundException("No proposalVersion Id given");
        } else {

            return proposalVersionDao.getByProposalIdVersion(proposalId, version);
        }
    }


    @RequestMapping(value = "/proposalVotes/count", method = {RequestMethod.GET, RequestMethod.HEAD})
    public Integer getProposalVotes(
            @RequestParam(required = false) Long contestPhaseId,
            @RequestParam(required = false) Long proposalId,
            @RequestParam(required = false) Long userId
    ) {
        return proposalVoteDao.countByGiven(proposalId,contestPhaseId, userId);
    }

    @RequestMapping(value = "/proposalVotes/hasUserVoted", method = {RequestMethod.GET, RequestMethod.HEAD})
    public Boolean hasUserVoted(
            @RequestParam(required = false) Long contestPhaseId,
            @RequestParam(required = false) Long proposalId,
            @RequestParam(required = false) Long memberId
    ) {
        return proposalVoteDao.countByGiven(proposalId,contestPhaseId, memberId) != 0;
    }

    @RequestMapping(value = "/proposalVotes", method = RequestMethod.POST)
    public ProposalVote createProposalVote(@RequestBody ProposalVote proposalVote) {
        return this.proposalVoteDao.create(proposalVote);
    }

    @RequestMapping(value = "/proposalVotes/deleteVote", method = RequestMethod.DELETE)
    public Boolean deleteProposalVote(@RequestParam Long contestPhaseId,@RequestParam Long memberId) {
        this.proposalVoteDao.delete(memberId,contestPhaseId);
        return true;
    }

    @RequestMapping(value = "/proposalVotes/updateVote", method = RequestMethod.POST)
    public boolean updateProposalVote(@RequestBody ProposalVote proposalVote) throws NotFoundException {

        return proposalVoteDao.update(proposalVote);

    }

    @RequestMapping(value = "/proposalVotes/getProposalVoteByProposalIdUserId", method = {RequestMethod.GET})
    public ProposalVote getProposalVoteByProposalIdUserId(
            @RequestParam(required = false) Long proposalId,
            @RequestParam(required = false) Long userId
    ) throws NotFoundException {
        List<ProposalVote> votesForUser = proposalVoteDao.findByGiven(proposalId, null, userId);
        if (votesForUser != null && !votesForUser.isEmpty()) {
            return votesForUser.get(0);
        } else {
            throw new NotFoundException();
        }

    }

    @RequestMapping(value = "/proposalIds", method = {RequestMethod.GET, RequestMethod.HEAD})
    public List<Long> listProposalIds(
            @RequestParam(required = false) Integer startRecord,
            @RequestParam(required = false) Integer limitRecord,
            @RequestParam(required = false) Long contestId,
            @RequestParam(required = false) Boolean visible,
            @RequestParam(required = false) Long contestPhaseId,
            @RequestParam(required = false) Integer ribbon,
            @RequestParam(required = false) String sort) {
        PaginationHelper paginationHelper = new PaginationHelper(startRecord, limitRecord, sort);

        return proposalDao
                .findIdsByGiven(paginationHelper, contestId, visible, contestPhaseId, ribbon);
    }

    @RequestMapping(value = "proposalThreadIds", method = {RequestMethod.GET, RequestMethod.HEAD})
    public List<Long> listThreadIds(
            @RequestParam(required = false) Integer startRecord,
            @RequestParam(required = false) Integer limitRecord,
            @RequestParam(required = false) Long contestId,
            @RequestParam(required = false) Boolean visible,
            @RequestParam(required = false) Long contestPhaseId,
            @RequestParam(required = false) Integer ribbon,
            @RequestParam(required = false) String sort) {
        PaginationHelper paginationHelper = new PaginationHelper(startRecord, limitRecord, sort);

        return proposalDao
                .findThreadIdsByGiven(paginationHelper, contestId, visible, contestPhaseId, ribbon);
    }

    @RequestMapping(value = "/proposals/getProposalsByCurrentContests", method = {RequestMethod.GET})
    public List<Proposal> getProposalsByCurrentContests(
            @RequestParam("contestTierIds") List<Long> contestTierIds,
            @RequestParam("contestTypeIds") List<Long> contestTypeIds,
            @RequestParam("filterText") String filterText) {
        return proposalService.getProposalsByCurrentContests(contestTypeIds,contestTierIds, filterText);
    }

}
