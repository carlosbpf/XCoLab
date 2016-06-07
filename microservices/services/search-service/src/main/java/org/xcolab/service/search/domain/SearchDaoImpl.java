package org.xcolab.service.search.domain;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.xcolab.model.tables.pojos.Comment;
import org.xcolab.model.tables.pojos.Contest;
import org.xcolab.model.tables.pojos.Member;
import org.xcolab.model.tables.pojos.ProposalAttribute;
import org.xcolab.service.utils.PaginationHelper;

import java.util.List;

import static org.xcolab.model.Tables.COMMENT;
import static org.xcolab.model.Tables.CONTEST;
import static org.xcolab.model.Tables.MEMBER;
import static org.xcolab.model.Tables.PROPOSAL_ATTRIBUTE;
import static org.xcolab.service.utils.db.jooq.CustomDsl.match;

@Repository
public class SearchDaoImpl implements SearchDao {

    @Autowired
    private DSLContext dslContext;

    @Override
    public List<ProposalAttribute> findProposalAttribute(PaginationHelper paginationHelper,
            String query) {
        final Field<Double> relevance = match(PROPOSAL_ATTRIBUTE.STRING_VALUE).against(query)
                .as("relevance");
        return dslContext.select(PROPOSAL_ATTRIBUTE.STRING_VALUE, relevance)
                .from(PROPOSAL_ATTRIBUTE)
                .where(match(PROPOSAL_ATTRIBUTE.STRING_VALUE).against(query))
                .orderBy(relevance.desc())
                .limit(paginationHelper.getStartRecord(), paginationHelper.getLimitRecord())
                .fetchInto(ProposalAttribute.class);
    }

    public List<Member> findMember(PaginationHelper paginationHelper,
                                                         String query) {
        final Field<Double> relevance = match(MEMBER.SHORT_BIO).against(query)
                .as("relevance");
        return dslContext.select(MEMBER.SHORT_BIO, relevance)
                .from(MEMBER)
                .where(match(MEMBER.SHORT_BIO).against(query))
                .orderBy(relevance.desc())
                .limit(paginationHelper.getStartRecord(), paginationHelper.getLimitRecord())
                .fetchInto(Member.class);
    }

    public List<Comment> findComment(PaginationHelper paginationHelper,
                                        String query) {
        final Field<Double> relevance = match(COMMENT.CONTENT).against(query)
                .as("relevance");
        return dslContext.select(COMMENT.CONTENT, relevance)
                .from(COMMENT)
                .where(match(COMMENT.CONTENT).against(query))
                .orderBy(relevance.desc())
                .limit(paginationHelper.getStartRecord(), paginationHelper.getLimitRecord())
                .fetchInto(Comment.class);
    }

    public List<Contest> findContest(PaginationHelper paginationHelper,
                                     String query) {
        final Field<Double> relevance = match(CONTEST.CONTEST_DESCRIPTION).against(query)
                .as("relevance");
        return dslContext.select(CONTEST.CONTEST_DESCRIPTION, relevance)
                .from(CONTEST)
                .where(match(CONTEST.CONTEST_DESCRIPTION).against(query))
                .orderBy(relevance.desc())
                .limit(paginationHelper.getStartRecord(), paginationHelper.getLimitRecord())
                .fetchInto(Contest.class);
    }
}
