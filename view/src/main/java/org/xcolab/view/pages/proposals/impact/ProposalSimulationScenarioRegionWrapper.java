package org.xcolab.view.pages.proposals.impact;

import org.xcolab.client.contest.pojo.Contest;
import org.xcolab.client.proposals.pojo.Proposal;

public class ProposalSimulationScenarioRegionWrapper {

    private Proposal proposalWrapper;
    private String simulation = "No model selected";
    private String scenario = "-";
    private String region = "";
    private String proposalName = "No proposal selected for this region";

    public ProposalSimulationScenarioRegionWrapper(Proposal proposalWrapper) {
        this.proposalWrapper = proposalWrapper;
        proposalName = proposalWrapper.getName();
        Contest contestForProposal = proposalWrapper.getContest();

        //ContestWrapper contestWrapper = new ContestWrapper(contestForProposal); //contestForProposal
        this.region = contestForProposal.getWhereName();

    }

    public ProposalSimulationScenarioRegionWrapper() {
    }

    public String getSimulation() {
        return simulation;
    }

    public String getScenario() {
        return scenario;
    }

    public String getRegion() {
        return region;
    }

    public void setSimulation(String simulation) {
        this.simulation = simulation;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setRegion(Contest contest) {
            this.region = contest.getWhereName();
    }

    public void setProposalWrapper(Proposal proposalWrapper) {
        this.proposalWrapper = proposalWrapper;
    }

    public String getProposalUrl() {
        if (proposalWrapper != null) {
            return proposalWrapper.getProposalUrl();
        } else {
            return "";
        }
    }

    public String getProposalName() {
        return this.proposalName;
    }
}
