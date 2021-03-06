package org.xcolab.service.activities.activityEntry.discussion;

import org.apache.commons.lang3.StringEscapeUtils;

import org.xcolab.client.comment.exceptions.CategoryNotFoundException;
import org.xcolab.client.comment.exceptions.CommentNotFoundException;
import org.xcolab.client.comment.exceptions.ThreadNotFoundException;
import org.xcolab.client.comment.pojo.Category;
import org.xcolab.client.comment.pojo.CategoryGroup;
import org.xcolab.client.comment.pojo.Comment;
import org.xcolab.client.comment.pojo.CommentThread;
import org.xcolab.client.comment.util.CategoryClientUtil;
import org.xcolab.client.comment.util.CommentClientUtil;
import org.xcolab.client.comment.util.ThreadClientUtil;
import org.xcolab.client.contest.exceptions.ContestNotFoundException;
import org.xcolab.client.contest.pojo.Contest;
import org.xcolab.client.members.MembersClient;
import org.xcolab.client.members.exceptions.MemberNotFoundException;
import org.xcolab.client.members.pojo.Member;
import org.xcolab.client.proposals.ProposalAttributeClientUtil;
import org.xcolab.client.proposals.ProposalClientUtil;
import org.xcolab.client.proposals.enums.ProposalAttributeKeys;
import org.xcolab.client.proposals.exceptions.ProposalNotFoundException;
import org.xcolab.client.proposals.pojo.Proposal;
import org.xcolab.model.tables.pojos.ActivityEntry;
import org.xcolab.service.activities.activityEntry.provider.ActivityEntryContentProvider;
import org.xcolab.util.enums.activity.ActivityEntryType;

public abstract class DiscussionBaseActivityEntry implements ActivityEntryContentProvider {

    protected ActivityEntry activityEntry;

    protected Comment comment;

    protected CommentThread thread;

    protected Category category;

    protected CategoryGroup categoryGroup;

    protected Proposal proposal;

    protected Contest contest;

    protected String proposalName;

    public static final String HYPERLINK_FORMAT = "<a href=\"%s\">%s</a>";

    @Override
    public void setActivityEntry(ActivityEntry activityEntry) {
        this.activityEntry = activityEntry;
        if (!this.getSecondaryType().equals(DiscussionActivitySubType.DISCUSSION_PROPOSAL_COMMENT.getSecondaryTypeId())) {

            try {
                category = CategoryClientUtil.getCategory(activityEntry.getClassPrimaryKey());
                comment = CommentClientUtil
                        .getComment(Long.parseLong(activityEntry.getExtraData()));
                thread = ThreadClientUtil.getThread(comment.getThreadId());
                category = CategoryClientUtil.getCategory(thread.getCategoryId());
            } catch (CategoryNotFoundException | ThreadNotFoundException | CommentNotFoundException e) {
                //_log.warn("Could not initialize discussion from " + activityEntry);
            }
        } else {
            try {
                thread = ThreadClientUtil.getThread(activityEntry.getClassPrimaryKey());
                final Long proposalId = ThreadClientUtil.getProposalIdForThread(thread.getThreadId());

                if (proposalId != null) {

                    proposal = ProposalClientUtil.getProposal(proposalId);
                    contest = ProposalClientUtil.getCurrentContestForProposal(proposal.getProposalId());


                    proposalName = ProposalAttributeClientUtil
                            .getProposalAttribute(proposal.getProposalId(), ProposalAttributeKeys.NAME,null).getStringValue();
                }
            } catch (ProposalNotFoundException | ContestNotFoundException | ThreadNotFoundException ignored) {

            }

        }
    }

    protected String getProposalLink() {
        String url = "";
        if (proposal!= null) {
            url = "<a href='" + proposal.getProposalLinkUrl(contest)+ "/tab/COMMENTS" + "'>" + proposalName + "</a>";
        }
        return url;
    }

    protected String getThreadLink() {
        return String.format(
                HYPERLINK_FORMAT,
                StringEscapeUtils.escapeHtml4(thread.getLinkUrl()), thread.getTitle());
    }

    protected String getCategoryLink() {
        return String.format(
                HYPERLINK_FORMAT,
                StringEscapeUtils.escapeHtml4(category.getLinkUrl()), category.getName());
    }

    protected String getUserLink() {

        try {
            Member member = MembersClient.getMember(this.activityEntry.getMemberId());
            return member.generateUserURL();

        }catch (MemberNotFoundException ignored){
            return "<user removed>";
        }
    }

    @Override
    public Long getPrimaryType() {
        return ActivityEntryType.DISCUSSION.getPrimaryTypeId();
    }


    public enum DiscussionActivitySubType {
        DISCUSSION_PROPOSAL_COMMENT(1L),
        DISCUSSION_CATEGORY_ADDED(2L),
        DISCUSSION_ADDED(3L),
        DISCUSSION_FORUM_COMMENT(4L),
        DISCUSSION_ADDED_COMMENT(5L);

        private final Long secondaryTypeId;
        DiscussionActivitySubType(Long type) {
            this.secondaryTypeId = type;
        }

        public Long getSecondaryTypeId(){
            return this.secondaryTypeId;
        }
    }
}
