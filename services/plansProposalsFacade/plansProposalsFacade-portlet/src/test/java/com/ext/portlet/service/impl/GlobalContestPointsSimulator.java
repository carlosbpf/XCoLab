package com.ext.portlet.service.impl;

import com.ext.portlet.ProposalAttributeKeys;
import com.ext.portlet.model.*;
import com.liferay.portal.NoSuchUserException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Manuel Thurner on 22/09/14.
 */
public class GlobalContestPointsSimulator extends GlobalContestSimulator {
    private double probabilityOfPointsDistributionAdditionalNonTeamMembers;
    protected Map<Integer, DistributionConfiguration> globalProposalsDistributions;
    protected Map<Integer, Map<Integer, DistributionConfiguration>> sideProposalsDistributions;
    protected List<Points> points;

    class DistributionConfiguration {
        //the teamMemberShares indizes are the indizes in the proposalTeamMembers field
        public Map<Integer, Double> teamMemberShares = new HashMap<Integer, Double>();
        //in additionalShares, the indizes are the indizes of the users field
        public Map<Integer, Double> additionalShares = new HashMap<Integer, Double>();
    }

    public void setPointsDistributions(
            double probabilityOfEmptyGlobalProposalConfiguration,
            double probabilityOfEmptySideProposalConfiguration,
            double probabilityOfPointsDistributionAdditionalNonTeamMembers
    ) throws NoSuchUserException, SystemException {
        this.probabilityOfPointsDistributionAdditionalNonTeamMembers = probabilityOfPointsDistributionAdditionalNonTeamMembers;

        //Global Proposals
        for (int i = 0; i < amountOfGlobalProposals; i++) {
            if (!doWithProbability(probabilityOfEmptyGlobalProposalConfiguration)) {
                DistributionConfiguration config = this.setPointsDistributionForProposal(globalProposals.get(i), globalProposalsTeamMembers.get(i), true);
                globalProposalsDistributions.put(i, config);
            }
        }

        //Side Proposals
        for (int i = 0; i < amountOfSideContests; i++) {
            for (int j = 0; j < amountOfProposalsPerSideContest; j++) {
                sideProposalsDistributions.put(i, new HashMap<Integer, DistributionConfiguration>());
                if (!doWithProbability(probabilityOfEmptySideProposalConfiguration)) {
                    DistributionConfiguration config = this.setPointsDistributionForProposal(sideProposals.get(i).get(j), sideProposalsTeamMembers.get(i).get(j), true);
                    sideProposalsDistributions.get(i).put(j, config);
                }
            }
        }
    }

    public void runPointDistributionAlgorithm() throws SystemException, PortalException {
        testInstance.pointsLocalService.distributePoints(globalContest.getContestPK());
    }



    /**
     * Sets a random point distribution configuration for the given proposal and its team members.
     * Dependent on the fields probabilityOfPointsDistributionAdditionalNonTeamMembers and random dice throws,
     * a configuration is saved in the database and a DistributionConfiguration object returned, which can be
     * used for later verification of the distributed points.
     *
     * @param proposal
     * @param teamMembers
     * @param isGlobal
     * @return
     * @throws com.liferay.portal.NoSuchUserException
     * @throws com.liferay.portal.kernel.exception.SystemException
     */
    private DistributionConfiguration setPointsDistributionForProposal(Proposal proposal, List<User> teamMembers, boolean isGlobal) throws NoSuchUserException, SystemException {
        DistributionConfiguration config = new DistributionConfiguration();

        //ANY TEAM MEMBER
        User author = teamMembers.get(0);
        double sumOfShares = 0.0;
        for (int i = 0; i < teamMembers.size(); i++) {
            User teamMember = teamMembers.get(i);
            //distribute randomly until the last member. he will get the rest
            double share;
            if (i < teamMembers.size()-1) {
                do {
                    share = generateProbabilityForTeamMembers(teamMembers.size());
                } while (share+sumOfShares > 1.0);
            } else {
                share = 1-sumOfShares;
            }

            config.teamMemberShares.put(i, share);

            testInstance.pointsDistributionConfigurationService.addDistributionConfiguration(
                    proposal.getProposalId(),
                    isGlobal ? 2 : 7, //team member point type
                    teamMember.getUserId(),
                    0L,
                    share,
                    author.getUserId()
            );
            sumOfShares += share;
        }
        assertTrue(sumOfShares == 1);

        sumOfShares = 0.0;
        //ANY NON-TEAM-MEMBER
        for (int i = 1; doWithProbability(probabilityOfPointsDistributionAdditionalNonTeamMembers/i); i++) {
            int randomUserIndex = randomInt(0, amountOfUsers);
            User randomUser = users.get(randomUserIndex);

            double share;
            do {
                share = generateProbabilityForTeamMembers(i);
            } while (share+sumOfShares > 1.0);
            if (share > 0) {
                testInstance.pointsDistributionConfigurationService.addDistributionConfiguration(
                        proposal.getProposalId(),
                        isGlobal ? 5 : 8, //any non-team-member
                        randomUser.getUserId(),
                        0L,
                        share,
                        author.getUserId()
                );
                config.additionalShares.put(randomUserIndex, share);
                sumOfShares += share;
            }
        }
        assertTrue(sumOfShares <= 1);

        if (sumOfShares > 0 && sumOfShares < 1) {
            int randomUserIndex = randomInt(0, amountOfUsers);
            User randomUser = users.get(randomUserIndex);
            double share = 1-sumOfShares;
            //add another one to make the sum total 1
            testInstance.pointsDistributionConfigurationService.addDistributionConfiguration(
                    proposal.getProposalId(),
                    isGlobal ? 5 : 8, //any non-team-member
                    randomUser.getUserId(),
                    0L,
                    share,
                    author.getUserId()
            );
            config.additionalShares.put(randomUserIndex, share);
            sumOfShares += share;
        }

        assertTrue(sumOfShares == 1);

        return config;
    }

    private void assertDistributionForGlobalProposal(int proposalIndex, long sourceId, double pointsToBeDistributed) {
        DistributionConfiguration config = globalProposalsDistributions.get(proposalIndex);
        Proposal p = globalProposals.get(proposalIndex);
        double teamPoints = pointsToBeDistributed*0.2*0.9;
        double nonTeamPoints = pointsToBeDistributed*0.2*0.1;

        int amountOfSubProposals = globalProposalLinksToGlobalProposals.get(proposalIndex).size();
        for (int i = 0; i < globalProposalLinksToSideProposals.get(proposalIndex).size(); i++) {
            amountOfSubProposals += globalProposalLinksToSideProposals.get(proposalIndex).get(i).size();
        }
        double pointsPerSubProposal = (pointsToBeDistributed*0.8)/amountOfSubProposals;

        //TEAM
        for (int j = 0; j < globalProposalsTeamMembers.get(proposalIndex).size(); j++) {
            User teamMember = globalProposalsTeamMembers.get(proposalIndex).get(j);
            double share;
            if (config != null) {
                share = config.teamMemberShares.get(j);
            } else {
                //no config is present. shares are distributed equally
                share = 1.00/globalProposalsTeamMembers.get(proposalIndex).size();
            }
            assertNotNull(popPointEntryInList(points, p.getProposalId(), teamMember.getUserId(), sourceId, 0, Math.ceil(teamPoints*share)));
        }

        //NON-TEAM
        if (config != null) {
            for (int userIndex: config.additionalShares.keySet()) {
                double share = config.additionalShares.get(userIndex);
                assertNotNull(popPointEntryInList(points, p.getProposalId(), users.get(userIndex).getUserId(), sourceId, 0, Math.ceil(nonTeamPoints*share)));
            }
        }

        //SUB-PROPOSALS
        //first gather all linked proposal ids and where they are located in our fields
        List<Long> childrenProposalIds = new ArrayList<Long>();
        List<Boolean> childrenProposalIsGlobal = new ArrayList<Boolean>();
        List<Integer> childrenGlobalProposalIndizes = new ArrayList<Integer>();
        List<Integer[]> childrenSideProposalIndizes = new ArrayList<Integer[]>();
        for (Integer subProposalIndex: globalProposalLinksToGlobalProposals.get(proposalIndex)) {
            childrenProposalIds.add(globalProposals.get(subProposalIndex).getProposalId());
            childrenGlobalProposalIndizes.add(subProposalIndex);
            childrenProposalIsGlobal.add(true);
        }
        for (int i = 0; i < globalProposalLinksToSideProposals.get(proposalIndex).size(); i++) {
            for (Integer subProposalIndex: globalProposalLinksToSideProposals.get(proposalIndex).get(i)) {
                childrenProposalIds.add(sideProposals.get(i).get(subProposalIndex).getProposalId());
                childrenProposalIsGlobal.add(false);
                childrenSideProposalIndizes.add(new Integer[]{i, subProposalIndex});
            }
        }

        //now go through all linked proposals and find their origin
        for (int i = 0; i < amountOfSubProposals; i++) {
            //find the related subProposal Point entry
            Points subProposalSourcePoints = popPointEntryInList(points, p.getProposalId(), 0, sourceId, 0, pointsPerSubProposal);
            assertNotNull(subProposalSourcePoints);

            //find the proposalId which has the sub-proposal Point entry as a parent
            Long proposalId = getProposalIdByPointsSourceIdInList(points, subProposalSourcePoints.getId());
            assertNotNull(proposalId);

            //remove
            int indexOfProposalId = childrenProposalIds.indexOf(proposalId);
            Boolean isProposalGlobal = childrenProposalIsGlobal.get(indexOfProposalId);
            if (indexOfProposalId > -1) {
                childrenProposalIds.remove(indexOfProposalId);
                childrenProposalIsGlobal.remove(indexOfProposalId);
            } else {
                throw new RuntimeException("Wrong sub-proposal "+proposalId);
            }
            //distinguish between proposal types
            if (isProposalGlobal) {
                int globalProposalIndex = childrenGlobalProposalIndizes.get(indexOfProposalId);
                childrenGlobalProposalIndizes.remove(indexOfProposalId);

                this.assertDistributionForGlobalProposal(globalProposalIndex, subProposalSourcePoints.getId(), pointsPerSubProposal);
            } else {
                Integer[] sideProposalIndex = childrenSideProposalIndizes.get(indexOfProposalId);
                childrenSideProposalIndizes.remove(indexOfProposalId);

                this.assertDistributionForSideProposal(sideProposalIndex, subProposalSourcePoints.getId(), pointsPerSubProposal);
            }
        }



    }

    public void assertDistributions() throws SystemException {
        //Assure that the individual Point data entries are correct
        this.points = new ArrayList<Points>(testInstance.pointsLocalService.getPointses(0, Integer.MAX_VALUE));

        for (int i: globalProposalsInLastPhase) {
            this.assertDistributionForGlobalProposal(i, 0L, pointsToBeDistributed);
        }

        //all points should be popped and verified after this.
        assertTrue(points.isEmpty());
    }

    private Long getProposalIdByPointsSourceIdInList(List<Points> points, long pointsSourceId) {
        for (Points p : points) {
            if (p.getPointsSourceId() == pointsSourceId) {
                return p.getProposalId();
            }
        }
        return null;
    }

    private Points popPointEntryInList(List<Points> points, long proposalId, long userId, long pointsSourceId, double materializedPoints, double hypotheticalPoints) {
        Points find = null;
        for (Points p : points) {
            if (p.getProposalId() == proposalId &&
                    p.getUserId() == userId &&
                    p.getMaterializedPoints() == materializedPoints &&
                    p.getHypotheticalPoints() == hypotheticalPoints &&
                    p.getPointsSourceId() == pointsSourceId) {
                find = p;
                break;
            }
        }
        if (find != null) points.remove(find);
        return find;
    }
}
