package com.ext.portlet.proposals;

public enum ProposalJudgeType {
    JUDGE(1),
    FELLOW(2),
    PUBLIC(3);

    private final int id;

    ProposalJudgeType(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}