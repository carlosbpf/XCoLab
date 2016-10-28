package org.xcolab.portlets.proposals.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.ext.portlet.ProposalAttributeKeys;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import org.xcolab.client.contest.ContestClient;
import org.xcolab.client.contest.OntologyClient;
import org.xcolab.client.contest.exceptions.ContestNotFoundException;
import org.xcolab.client.contest.pojo.Contest;
import org.xcolab.client.contest.pojo.FocusArea;
import org.xcolab.client.contest.pojo.OntologyTerm;
import org.xcolab.client.proposals.Proposal2PhaseClient;
import org.xcolab.client.proposals.ProposalAttributeClient;
import org.xcolab.client.proposals.ProposalContestPhaseAttributeClient;
import org.xcolab.client.proposals.ProposalsClient;
import org.xcolab.client.proposals.pojo.Proposal;
import org.xcolab.client.proposals.pojo.Proposal2Phase;
import org.xcolab.enums.ContestTier;
import org.xcolab.portlets.proposals.wrappers.ContestWrapper;
import org.xcolab.util.exceptions.DatabaseAccessException;
import org.xcolab.util.exceptions.InternalException;
import org.xcolab.utils.IdListUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.xcolab.portlets.proposals.utils.ProposalPickerFilterUtil.SectionDefFocusAreaArgument;

public class ProposalPickerFilter {
    private static final Log _log = LogFactoryUtil.getLog(ProposalPickerFilter.class);
    private static final List<Long> ANY_TERM_IDS = Arrays.asList(1L, 2L, 3L, 1300601L);

    /**
     * Filters the passed list of proposals
     * @param proposals the list of proposals to be filtered. Will be modified and contains the result!
     * @return A list of all proposals that were removed
     */
    public Set<Long> filter(List<Pair<Proposal,Date>> proposals) {
        return filter(proposals, null);
    }
    /**
     * Filters the passed list of proposals
     * @param proposals the list of proposals to be filtered. Will be modified and contains the result!
     * @param additionalFilterCriterion The additional filter criterion - if any - used by this filter
     * @return A list of all proposals that were removed
     */
    public Set<Long> filter(List<Pair<Proposal,Date>> proposals, Object additionalFilterCriterion) {
        return new HashSet<>();
    }

    /**
     * Filters the passed list of contests
     * @param contests the list of contests to be filtered. Will be modified and contains the result!
     * @param additionalFilterCriterion The additional filter criterion - if any - used by this filter
     * @return A list of all contests that were removed
     */
    public Set<Long> filterContests(List<Pair<ContestWrapper,Date>> contests, Object additionalFilterCriterion){
        return new HashSet<>();
    }

    public final static ProposalPickerFilter ACCEPT_ALL = new ProposalPickerFilter();

    public final static ProposalPickerFilter SECTION_DEF_FOCUS_AREA_FILTER = new ProposalPickerFilter() {
        @Override
        public Set<Long> filter(List<Pair<Proposal, Date>> proposals, Object additionalFilterCriterion) {
            Set<Long> removedProposals = new HashSet<>();
            try {
                final SectionDefFocusAreaArgument additionalArguments = (SectionDefFocusAreaArgument) additionalFilterCriterion;
                final List<Long> filterExceptionContestIds = additionalArguments.getFilterExceptionContestIds();
                final Long sectionFocusAreaId = additionalArguments.getSectionFocusAreaId();
                final Long contestFocusAreaId = additionalArguments.getContestFocusAreaId();

                List<OntologyTerm> terms = getOntologyTermsFromSectionAndContest(sectionFocusAreaId, contestFocusAreaId);


                if (!terms.isEmpty()) {
                    List<Long> otIds = new ArrayList<>();
                    for(OntologyTerm ot : terms){
                        otIds.add(ot.getId_());
                    }
                    List<Contest> contests = ContestClient.getContestMatchingOntologyTerms(otIds);
                    for (long filterExceptionContestId : filterExceptionContestIds) {
                        try {
                            final Contest filterExceptionContest = ContestClient.getContest(filterExceptionContestId);
                            if (!contests.contains(filterExceptionContest)) {
                                contests.add(filterExceptionContest);
                            }
                        }catch (ContestNotFoundException ignored){

                        }
                    }
                    for (Iterator<Pair<Proposal,Date>> i = proposals.iterator(); i.hasNext();){
                        Proposal p = i.next().getLeft();
                        try {
                            if (!contests.contains(ProposalsClient.getCurrentContestForProposal(p.getProposalId()))) {
                                removedProposals.add(p.getProposalId());
                                i.remove();
                            }
                        } catch (ContestNotFoundException  e){
                            removedProposals.add(p.getProposalId());
                            i.remove();
                        }
                    }
                }
            } catch (SystemException e) {
                throw new DatabaseAccessException(e);
            } catch (PortalException e) {
                throw new InternalException(e);
            }
            return removedProposals;
        }

        @Override
        public Set<Long> filterContests(List<Pair<ContestWrapper, Date>> contests,
                                        Object additionalFilterCriterion) {
            Set<Long> removedContests = new HashSet<>();
            try {
                final SectionDefFocusAreaArgument additionalArguments = (SectionDefFocusAreaArgument) additionalFilterCriterion;
                final List<Long> filterExceptionContestIds = additionalArguments.getFilterExceptionContestIds();
                final Long sectionFocusAreaId = additionalArguments.getSectionFocusAreaId();
                final Long contestFocusAreaId = additionalArguments.getContestFocusAreaId();

                List<OntologyTerm> requiredTerms = getOntologyTermsFromSectionAndContest(sectionFocusAreaId, contestFocusAreaId);

                for (Iterator<Pair<ContestWrapper,Date>> i = contests.iterator(); i.hasNext();){
                    ContestWrapper contest = i.next().getLeft();
                    if (filterExceptionContestIds.contains(contest.getContestPK())) {
                        continue;
                    }
                        FocusArea focusArea = OntologyClient.getFocusArea(contest.getFocusAreaId());
                        List<OntologyTerm> contestTerms = OntologyClient.getOntologyTermsForFocusArea(focusArea);
                        for (OntologyTerm requiredTerm : requiredTerms) {
                            List<OntologyTerm> requiredDescendantTerms = OntologyClient.getAllOntologyTermDescendant(requiredTerm.getId_());
                            requiredDescendantTerms.add(requiredTerm);
                            if (!CollectionUtils.containsAny(requiredDescendantTerms, contestTerms)) {
                                removedContests.add(contest.getContestPK());
                                i.remove();
                                break;
                            }
                        }

                }
            } catch (SystemException e) {
                throw new DatabaseAccessException(e);
            } catch (PortalException e) {
                throw new InternalException(e);
            }

            return removedContests;
        }

        private List<OntologyTerm> getOntologyTermsFromSectionAndContest(Long focusAreaId, Long contestFocusAreaId) throws SystemException, PortalException {
            Long localContestFocusAreaId;
            if (focusAreaId < 0) {
                localContestFocusAreaId = contestFocusAreaId;
                focusAreaId = Math.abs(focusAreaId);
            } else {
                localContestFocusAreaId = 0L;
            }
            List<OntologyTerm> terms = new ArrayList<>();

            FocusArea focusArea = OntologyClient.getFocusArea(focusAreaId);
            addTermsInFocusArea(terms, focusArea);
            FocusArea contestFocusArea = OntologyClient.getFocusArea(localContestFocusAreaId);
            addTermsInFocusArea(terms, contestFocusArea);

            return terms;
        }

        private void addTermsInFocusArea(List<OntologyTerm> terms, FocusArea focusArea) throws PortalException, SystemException {
            if (focusArea != null) {
                final List<OntologyTerm> focusAreaTerms = OntologyClient.getOntologyTermsForFocusArea(focusArea);
                removeRootTerms(focusAreaTerms);
                _log.debug(String.format("Added %d non-root contest terms", focusAreaTerms.size()));
                terms.addAll(focusAreaTerms);
            }
        }

        private void removeRootTerms(List<OntologyTerm> terms) {
            for (Iterator<OntologyTerm> i = terms.iterator(); i.hasNext();){
                OntologyTerm o = i.next();

                if (o.getParentId() == 0 && ANY_TERM_IDS.contains(o.getId_())) {
                    i.remove();
                }
            }
        }
    };

    public final static ProposalPickerFilter TEXT_BASED = new ProposalPickerFilter() {
        @Override
        public Set<Long> filter(List<Pair<Proposal,Date>> proposals, Object additionalFilterCriterion) {
            Set<Long> removedProposals = new HashSet<>();
            if (!(additionalFilterCriterion instanceof String)) {
                return removedProposals;
            }
            String searchCriterion = (String) additionalFilterCriterion;
            for (Iterator<Pair<Proposal,Date>> i = proposals.iterator(); i.hasNext();){
                Proposal p = i.next().getLeft();
                try{
                    // PROPOSAL NAME
                    //TODO: optimize
                    String proposalName = ProposalAttributeClient.getProposalAttribute(p.getProposalId(), ProposalAttributeKeys.NAME, 0L).getStringValue();
                    if (StringUtils.containsIgnoreCase(proposalName, searchCriterion)){
                        continue;
                    }
                    // CONTEST NAME
                    String contestName = ProposalsClient.getCurrentContestForProposal(p.getProposalId()).getContestName();
                    if (StringUtils.containsIgnoreCase(contestName,searchCriterion)){
                        continue;
                    }
                    // Remove element if it does not match any criterion
                    removedProposals.add(p.getProposalId());
                    i.remove();
                } catch (ContestNotFoundException e) {
                    throw new DatabaseAccessException(e);
                }
            }
            return removedProposals;
        }

        @Override
        public Set<Long> filterContests(List<Pair<ContestWrapper, Date>> contests,
                                        Object additionalFilterCriterion) {
            Set<Long> removedContests = new HashSet<>();
            if (!(additionalFilterCriterion instanceof String)) {
                return removedContests;
            }
            String searchCriterion = (String) additionalFilterCriterion;
            for (Iterator<Pair<ContestWrapper,Date>> i = contests.iterator(); i.hasNext();){
                ContestWrapper c = i.next().getLeft();

                try{
                    // CONTEST NAME
                    if (StringUtils.containsIgnoreCase(c.getContestName(),searchCriterion)){
                        continue;
                    }
                    if (StringUtils.containsIgnoreCase(c.getContestShortName(), searchCriterion)) {
                        continue;
                    }
                    // focus area
                    if (StringUtils.containsIgnoreCase(c.getWhatName(), searchCriterion)) {
                        continue;
                    }
                    if (StringUtils.containsIgnoreCase(c.getWhoName(), searchCriterion)) {
                        continue;
                    }
                    if (StringUtils.containsIgnoreCase(c.getWhereName(), searchCriterion)) {
                        continue;
                    }

                    // Remove element if it does not match any criterion
                    removedContests.add(c.getContestPK());
                    i.remove();
                } catch (SystemException e) {
                    throw new DatabaseAccessException(e);
                } catch (PortalException e) {
                    throw new InternalException(e);
                }
            }
            return removedContests;
        }
    };

    public final static ProposalPickerFilter WINNERS_ONLY = new ProposalPickerFilter() {
        @Override
        public Set<Long> filter(List<Pair<Proposal, Date>> proposals, Object additionalFilterCriterion) {
            Set<Long> removedProposals = new HashSet<>();
            for (Iterator<Pair<Proposal,Date>> i = proposals.iterator(); i.hasNext();){
                Proposal p = i.next().getLeft();
                    List<Proposal2Phase> phases = Proposal2PhaseClient.getProposal2PhaseByProposalId(p.getProposalId());
                    boolean winner = false;
                    for (Proposal2Phase phase : phases){
                        winner = winner || ProposalContestPhaseAttributeClient.hasProposalContestPhaseAttribute(
                                p.getProposalId(), phase.getContestPhaseId(), "RIBBON");
                    }
                    if (!winner) {
                        removedProposals.add(p.getProposalId());
                        i.remove();
                    }
            }
            return removedProposals;
        }
    };

    public final static ProposalPickerFilter CONTEST_TIER = new ProposalPickerFilter() {
        @Override
        public Set<Long> filter(List<Pair<Proposal, Date>> proposals, Object additionalFilterCriterion) {
            Set<Long> removedProposals = new HashSet<>();

                final Set<Long> allowedContestTiers = getAllowedTiers((Long) additionalFilterCriterion);

                if (!allowedContestTiers.isEmpty()) {
                    Set<Contest> tierFilteredContests = new HashSet<>();
                    for (Long tier : allowedContestTiers) {
                        ContestTier contestTier = ContestTier.getContestTierByTierType(tier);
                        if (contestTier != null) {
                            tierFilteredContests.addAll(ContestClient.getContestsMatchingTier(contestTier.getTierType()));
                        } else {
                            _log.error(String.format("Could not find contest tier %d. Tier ignored in filtering.", tier));
                        }
                    }
                    int count = 0;
                    for (Iterator<Pair<Proposal,Date>> i = proposals.iterator(); i.hasNext();){
                        count++;
                        System.out.println("Filtered proposal No. " + count + " for contest tier");
                        Proposal p = i.next().getLeft();
                        try {
                            if (!tierFilteredContests.contains(ProposalsClient.getCurrentContestForProposal(p.getProposalId()))) {
                                i.remove();
                            }
                        } catch (ContestNotFoundException e){
                            i.remove();
                        }
                    }
                }

            return removedProposals;
        }

        @Override
        public Set<Long> filterContests(List<Pair<ContestWrapper, Date>> contests,
                                        Object additionalFilterCriterion) {
            Set<Long> removedContests = new HashSet<>();

            final Set<Long> allowedContestTiers = getAllowedTiers((Long) additionalFilterCriterion);

            if (!allowedContestTiers.isEmpty()) { //empty list = allow all
                for (Iterator<Pair<ContestWrapper,Date>> i = contests.iterator(); i.hasNext();){
                    ContestWrapper contest = i.next().getLeft();
                    if (!allowedContestTiers.contains(contest.getContestTier())) {
                        removedContests.add(contest.getContestPK());
                        i.remove();
                        //_log.error(String.format("Contest %d caused an error while filtering for contest tier." +
                        //        "Removing contests from list...", contest.getContestPK()));
                    }
                }
            }
            return removedContests;
        }

        private Set<Long> getAllowedTiers(Long filterTier) {
            // if filterTier < 0:
            //  allow tier <= (-filterTier)
            // else if filterTier > 0
            //  only allow tier == filterTier
            Set<Long> allowedTiers = new HashSet<>();
            final long positiveFilterTier = Math.abs(filterTier);
            allowedTiers.add(positiveFilterTier);

            if (filterTier < 0) {
                for (Long currentTier = positiveFilterTier - 1; currentTier >= 0; currentTier--) {
                    allowedTiers.add(currentTier);
                }
            }
            return allowedTiers;
        }
    };

    public final static ProposalPickerFilter CONTEST_TYPE_FILTER = new ProposalPickerFilter() {
        @Override
        public Set<Long> filter(List<Pair<Proposal, Date>> proposals, Object additionalFilterCriterion) {
            Set<Long> removedProposals = new HashSet<>();

            final String allowedContestTypeIdsString = (String) additionalFilterCriterion;

            if (!allowedContestTypeIdsString.isEmpty()) {//no selection = allow all
                final List<Long> allowedContestTypeIds = IdListUtil.getIdsFromString(allowedContestTypeIdsString);
                for (Iterator<Pair<Proposal, Date>> i = proposals.iterator(); i.hasNext(); ) {
                    Proposal proposal = i.next().getLeft();
                    try {

                        long contestTypeId = ProposalsClient.getCurrentContestForProposal(proposal.getProposalId()).getContestTypeId();
                        if (!allowedContestTypeIds.contains(contestTypeId)) {
                            i.remove();
                        }
                    } catch (ContestNotFoundException  e) {
                        _log.warn(String.format("Could not get ContestType for proposal %d while filtering for contest types",
                                proposal.getProposalId()), e);
                        i.remove();
                    }
                }
            }

            return removedProposals;
        }

        @Override
        public Set<Long> filterContests(List<Pair<ContestWrapper, Date>> contests, Object additionalFilterCriterion) {
            Set<Long> removedContests = new HashSet<>();

            final String allowedContestTypeIdsString = (String) additionalFilterCriterion;

            if (!allowedContestTypeIdsString.isEmpty()) { //no selection = allow all
                List<Long> allowedContestTypeIds = IdListUtil.getIdsFromString(allowedContestTypeIdsString);

                for (Iterator<Pair<ContestWrapper, Date>> i = contests.iterator(); i.hasNext(); ) {
                    ContestWrapper contest = i.next().getLeft();
                    if (!allowedContestTypeIds.contains(contest.getContestTypeId())) {
                        removedContests.add(contest.getContestPK());
                        i.remove();
                    }
                }
            }

            return removedContests;
        }
    };
}
