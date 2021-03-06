package org.xcolab.view.pages.proposals.utils;

import org.apache.commons.lang3.StringUtils;

import org.xcolab.client.contest.pojo.phases.ContestPhase;
import org.xcolab.client.members.MessagingClient;
import org.xcolab.client.members.pojo.Member;
import org.xcolab.client.proposals.ProposalAttributeClientUtil;
import org.xcolab.client.proposals.ProposalClientUtil;
import org.xcolab.client.proposals.enums.ProposalAttributeKeys;
import org.xcolab.client.proposals.pojo.Proposal;
import org.xcolab.entity.utils.judging.ProposalJudgingCommentHelper;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class ContestPhasePromotionEmail {

    private static final long ADMINISTRATOR_USER_ID = 10144L;

    public static void contestPhasePromotionEmailNotifyProposalContributors(Proposal proposal, ContestPhase contestPhase, HttpServletRequest request) {

        String subject = "Judging Results on your Proposal " + ProposalAttributeClientUtil
                .getProposalAttribute(proposal.getProposalId(), ProposalAttributeKeys.NAME, 0L).getStringValue();

        ProposalJudgingCommentHelper reviewContentHelper = new ProposalJudgingCommentHelper(proposal, contestPhase);
        String messageBody = reviewContentHelper.getPromotionComment(true);

        if (StringUtils.isNotEmpty(messageBody)) {
            MessagingClient
                    .sendMessage(subject, messageBody, ADMINISTRATOR_USER_ID, ADMINISTRATOR_USER_ID, getMemberUserIds(proposal));
        }
    }

    private static  List<Long> getMemberUserIds(Proposal proposal) {
        List<Long> recipientIds = new ArrayList<>();

        for (Member contributor : ProposalClientUtil.getProposalMembers(proposal.getProposalId())) {
            recipientIds.add(contributor.getUserId());
        }

        return recipientIds;
    }
}
