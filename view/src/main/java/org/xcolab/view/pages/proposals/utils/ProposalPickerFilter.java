package org.xcolab.view.pages.proposals.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.xcolab.client.contest.ContestClientUtil;
import org.xcolab.client.contest.OntologyClientUtil;
import org.xcolab.client.contest.exceptions.ContestNotFoundException;
import org.xcolab.client.contest.pojo.Contest;
import org.xcolab.client.contest.pojo.ontology.FocusArea;
import org.xcolab.client.contest.pojo.ontology.OntologyTerm;
import org.xcolab.client.proposals.ProposalAttributeClientUtil;
import org.xcolab.client.proposals.ProposalClientUtil;
import org.xcolab.client.proposals.ProposalPhaseClientUtil;
import org.xcolab.client.proposals.enums.ProposalAttributeKeys;
import org.xcolab.client.proposals.pojo.Proposal;
import org.xcolab.client.proposals.pojo.phases.Proposal2Phase;
import org.xcolab.util.IdListUtil;
import org.xcolab.util.enums.contest.ContestTier;
import org.xcolab.util.exceptions.DatabaseAccessException;
import org.xcolab.view.pages.proposals.utils.ProposalPickerFilterUtil.SectionDefFocusAreaArgument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class ProposalPickerFilter {
    private static final Logger _log = LoggerFactory.getLogger(ProposalPickerFilter.class);
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
    public Set<Long> filterContests(List<Pair<Contest,Date>> contests, Object additionalFilterCriterion){
        return new HashSet<>();
    }

    public final static ProposalPickerFilter ACCEPT_ALL = new ProposalPickerFilter();

    public final static ProposalPickerFilter SECTION_DEF_FOCUS_AREA_FILTER = new ProposalPickerFilter() {
        @Override
        public Set<Long> filter(List<Pair<Proposal, Date>> proposals, Object additionalFilterCriterion) {
            Set<Long> removedProposals = new HashSet<>();
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
                List<Contest> contests = ContestClientUtil.getContestMatchingOntologyTerms(otIds);
                for (long filterExceptionContestId : filterExceptionContestIds) {
                    try {
                        final Contest filterExceptionContest = ContestClientUtil.getContest(filterExceptionContestId);
                        if (!contests.contains(filterExceptionContest)) {
                            contests.add(filterExceptionContest);
                        }
                    }catch (ContestNotFoundException ignored){

                    }
                }
                for (Iterator<Pair<Proposal,Date>> i = proposals.iterator(); i.hasNext();){
                    Proposal p = i.next().getLeft();
                    try {
                        if (!contests.contains(ProposalClientUtil.getCurrentContestForProposal(p.getProposalId()))) {
                            removedProposals.add(p.getProposalId());
                            i.remove();
                        }
                    } catch (ContestNotFoundException  e){
                        removedProposals.add(p.getProposalId());
                        i.remove();
                    }
                }
            }
            return removedProposals;
        }

        @Override
        public Set<Long> filterContests(List<Pair<Contest, Date>> contests,
                                        Object additionalFilterCriterion) {
            Set<Long> removedContests = new HashSet<>();
            final SectionDefFocusAreaArgument additionalArguments = (SectionDefFocusAreaArgument) additionalFilterCriterion;
            final List<Long> filterExceptionContestIds = additionalArguments.getFilterExceptionContestIds();
            final Long sectionFocusAreaId = additionalArguments.getSectionFocusAreaId();
            final Long contestFocusAreaId = additionalArguments.getContestFocusAreaId();

            List<OntologyTerm> requiredTerms = getOntologyTermsFromSectionAndContest(sectionFocusAreaId, contestFocusAreaId);

            for (Iterator<Pair<Contest,Date>> i = contests.iterator(); i.hasNext();){
                Contest contest = i.next().getLeft();
                if (filterExceptionContestIds.contains(contest.getContestPK())) {
                    continue;
                }
                    FocusArea focusArea = OntologyClientUtil.getFocusArea(contest.getFocusAreaId());
                    List<OntologyTerm> contestTerms = OntologyClientUtil.getOntologyTermsForFocusArea(focusArea);
                    for (OntologyTerm requiredTerm : requiredTerms) {
                        List<OntologyTerm> requiredDescendantTerms = OntologyClientUtil.getAllOntologyTermDescendant(requiredTerm.getId_());
                        requiredDescendantTerms.add(requiredTerm);
                        if (!CollectionUtils.containsAny(requiredDescendantTerms, contestTerms)) {
                            removedContests.add(contest.getContestPK());
                            i.remove();
                            break;
                        }
                    }

            }

            return removedContests;
        }

        private List<OntologyTerm> getOntologyTermsFromSectionAndContest(Long focusAreaId,
                Long contestFocusAreaId) {
            Long localContestFocusAreaId;
            if (focusAreaId < 0) {
                localContestFocusAreaId = contestFocusAreaId;
                focusAreaId = Math.abs(focusAreaId);
            } else {
                localContestFocusAreaId = 0L;
            }
            List<OntologyTerm> terms = new ArrayList<>();

            FocusArea focusArea = OntologyClientUtil.getFocusArea(focusAreaId);
            addTermsInFocusArea(terms, focusArea);
            FocusArea contestFocusArea = OntologyClientUtil.getFocusArea(localContestFocusAreaId);
            addTermsInFocusArea(terms, contestFocusArea);

            return terms;
        }

        private void addTermsInFocusArea(List<OntologyTerm> terms, FocusArea focusArea) {
            if (focusArea != null) {
                final List<OntologyTerm> focusAreaTerms = OntologyClientUtil.getOntologyTermsForFocusArea(focusArea);
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
                    String proposalName = ProposalAttributeClientUtil.getProposalAttribute(p.getProposalId(), ProposalAttributeKeys.NAME, 0L).getStringValue();
                    if (StringUtils.containsIgnoreCase(proposalName, searchCriterion)){
                        continue;
                    }
                    // CONTEST NAME
                    String contestName = ProposalClientUtil.getCurrentContestForProposal(p.getProposalId()).getContestName();
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
        public Set<Long> filterContests(List<Pair<Contest, Date>> contests,
                                        Object additionalFilterCriterion) {
            Set<Long> removedContests = new HashSet<>();
            if (!(additionalFilterCriterion instanceof String)) {
                return removedContests;
            }
            String searchCriterion = (String) additionalFilterCriterion;
            for (Iterator<Pair<Contest,Date>> i = contests.iterator(); i.hasNext();){
                Contest c = i.next().getLeft();

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
                    List<Proposal2Phase> phases = ProposalPhaseClientUtil.getProposal2PhaseByProposalId(p.getProposalId());
                    boolean winner = false;
                    for (Proposal2Phase phase : phases){
                        winner = winner || ProposalPhaseClientUtil.hasProposalContestPhaseAttribute(
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

            final Set<Long> allowedContestTiers = new HashSet<>(ProposalPickerFilterUtil.getAllowedTiers((Long) additionalFilterCriterion));

            if (!allowedContestTiers.isEmpty()) {
                Set<Contest> tierFilteredContests = new HashSet<>();
                for (Long tier : allowedContestTiers) {
                    ContestTier contestTier = ContestTier.getContestTierByTierType(tier);
                    if (contestTier != null) {
                        tierFilteredContests.addAll(ContestClientUtil.getContestsMatchingTier(contestTier.getTierType()));
                    } else {
                        _log.error(String.format("Could not find contest tier %d. Tier ignored in filtering.", tier));
                    }
                }

                for (Iterator<Pair<Proposal,Date>> i = proposals.iterator(); i.hasNext();){
                    Proposal p = i.next().getLeft();
                    try {
                        if (!tierFilteredContests.contains(ProposalClientUtil.getCurrentContestForProposal(p.getProposalId()))) {
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
        public Set<Long> filterContests(List<Pair<Contest, Date>> contests,
                                        Object additionalFilterCriterion) {
            Set<Long> removedContests = new HashSet<>();

            final Set<Long> allowedContestTiers = new HashSet<>(ProposalPickerFilterUtil.getAllowedTiers((Long) additionalFilterCriterion));

            if (!allowedContestTiers.isEmpty()) { //empty list = allow all
                for (Iterator<Pair<Contest,Date>> i = contests.iterator(); i.hasNext();){
                    Contest contest = i.next().getLeft();
                    if (!allowedContestTiers.contains(contest.getContestTier())) {
                        removedContests.add(contest.getContestPK());
                        i.remove();
                        _log.error(String.format("Contest %d caused an error while filtering for contest tier." +
                                "Removing contests from list...", contest.getContestPK()));
                    }
                }
            }
            return removedContests;
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

                        long contestTypeId = ProposalClientUtil.getCurrentContestForProposal(proposal.getProposalId()).getContestTypeId();
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
        public Set<Long> filterContests(List<Pair<Contest, Date>> contests, Object additionalFilterCriterion) {
            Set<Long> removedContests = new HashSet<>();

            final String allowedContestTypeIdsString = (String) additionalFilterCriterion;

            if (!allowedContestTypeIdsString.isEmpty()) { //no selection = allow all
                List<Long> allowedContestTypeIds = IdListUtil.getIdsFromString(allowedContestTypeIdsString);

                for (Iterator<Pair<Contest, Date>> i = contests.iterator(); i.hasNext(); ) {
                    Contest contest = i.next().getLeft();
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
