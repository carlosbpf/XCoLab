package org.xcolab.service.proposal.domain.proposalunversionedattribute;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xcolab.model.tables.pojos.ProposalUnversionedAttribute;
import org.xcolab.model.tables.records.ProposalUnversionedAttributeRecord;
import org.xcolab.service.proposal.exceptions.NotFoundException;


import java.util.List;

import static org.xcolab.model.Tables.PROPOSAL_UNVERSIONED_ATTRIBUTE;

@Repository
public class ProposalUnversionedAttributeDaoImpl implements ProposalUnversionedAttributeDao {

    @Autowired
    private DSLContext dslContext;

    public ProposalUnversionedAttribute create(ProposalUnversionedAttribute proposalUnversionedAttribute) {

        ProposalUnversionedAttributeRecord ret = this.dslContext.insertInto(PROPOSAL_UNVERSIONED_ATTRIBUTE)
                .set(PROPOSAL_UNVERSIONED_ATTRIBUTE.ID_, proposalUnversionedAttribute.getId_())
                .set(PROPOSAL_UNVERSIONED_ATTRIBUTE.PROPOSAL_ID, proposalUnversionedAttribute.getProposalId())
                .set(PROPOSAL_UNVERSIONED_ATTRIBUTE.CREATE_AUTHOR_ID, proposalUnversionedAttribute.getCreateAuthorId())
                .set(PROPOSAL_UNVERSIONED_ATTRIBUTE.LAST_AUTHOR_ID, proposalUnversionedAttribute.getLastAuthorId())
                .set(PROPOSAL_UNVERSIONED_ATTRIBUTE.CREATE_DATE, proposalUnversionedAttribute.getCreateDate())
                .set(PROPOSAL_UNVERSIONED_ATTRIBUTE.LAST_UPDATE_DATE, proposalUnversionedAttribute.getLastUpdateDate())
                .set(PROPOSAL_UNVERSIONED_ATTRIBUTE.NAME, proposalUnversionedAttribute.getName())
                .set(PROPOSAL_UNVERSIONED_ATTRIBUTE.ADDTIONAL_ID, proposalUnversionedAttribute.getAddtionalId())
                .set(PROPOSAL_UNVERSIONED_ATTRIBUTE.NUMERIC_VALUE, proposalUnversionedAttribute.getNumericValue())
                .set(PROPOSAL_UNVERSIONED_ATTRIBUTE.STRING_VALUE, proposalUnversionedAttribute.getStringValue())
                .set(PROPOSAL_UNVERSIONED_ATTRIBUTE.REAL_VALUE, proposalUnversionedAttribute.getRealValue())
                .returning(PROPOSAL_UNVERSIONED_ATTRIBUTE.ID_)
                .fetchOne();
        if (ret != null) {
            proposalUnversionedAttribute.setId_(ret.getValue(PROPOSAL_UNVERSIONED_ATTRIBUTE.ID_));
            return proposalUnversionedAttribute;
        } else {
            return null;
        }

    }

    public ProposalUnversionedAttribute get(Long id_) throws NotFoundException {

        final Record record = this.dslContext.selectFrom(PROPOSAL_UNVERSIONED_ATTRIBUTE)
                .where(PROPOSAL_UNVERSIONED_ATTRIBUTE.ID_.eq(id_))
                .fetchOne();

        if (record == null) {
            throw new NotFoundException("ProposalUnversionedAttribute with id " + id_ + " does not exist");
        }
        return record.into(ProposalUnversionedAttribute.class);

    }

    @Override
    public boolean update(ProposalUnversionedAttribute proposalUnversionedAttribute) {
        return dslContext.update(PROPOSAL_UNVERSIONED_ATTRIBUTE)
                .set(PROPOSAL_UNVERSIONED_ATTRIBUTE.ID_, proposalUnversionedAttribute.getId_())
                .set(PROPOSAL_UNVERSIONED_ATTRIBUTE.PROPOSAL_ID, proposalUnversionedAttribute.getProposalId())
                .set(PROPOSAL_UNVERSIONED_ATTRIBUTE.CREATE_AUTHOR_ID, proposalUnversionedAttribute.getCreateAuthorId())
                .set(PROPOSAL_UNVERSIONED_ATTRIBUTE.LAST_AUTHOR_ID, proposalUnversionedAttribute.getLastAuthorId())
                .set(PROPOSAL_UNVERSIONED_ATTRIBUTE.CREATE_DATE, proposalUnversionedAttribute.getCreateDate())
                .set(PROPOSAL_UNVERSIONED_ATTRIBUTE.LAST_UPDATE_DATE, proposalUnversionedAttribute.getLastUpdateDate())
                .set(PROPOSAL_UNVERSIONED_ATTRIBUTE.NAME, proposalUnversionedAttribute.getName())
                .set(PROPOSAL_UNVERSIONED_ATTRIBUTE.ADDTIONAL_ID, proposalUnversionedAttribute.getAddtionalId())
                .set(PROPOSAL_UNVERSIONED_ATTRIBUTE.NUMERIC_VALUE, proposalUnversionedAttribute.getNumericValue())
                .set(PROPOSAL_UNVERSIONED_ATTRIBUTE.STRING_VALUE, proposalUnversionedAttribute.getStringValue())
                .set(PROPOSAL_UNVERSIONED_ATTRIBUTE.REAL_VALUE, proposalUnversionedAttribute.getRealValue())
                .where(PROPOSAL_UNVERSIONED_ATTRIBUTE.ID_.eq(proposalUnversionedAttribute.getId_()))
                .execute() > 0;
    }

    @Override
    public ProposalUnversionedAttribute getByProposalIdName(Long proposalId, String name) {
        final SelectQuery<Record> query = dslContext.select()
                .from(PROPOSAL_UNVERSIONED_ATTRIBUTE).getQuery();

        if (proposalId != null) {
            query.addConditions(PROPOSAL_UNVERSIONED_ATTRIBUTE.PROPOSAL_ID.eq(proposalId));
        }
        if (name != null) {
            query.addConditions(PROPOSAL_UNVERSIONED_ATTRIBUTE.NAME.eq(name));
        }
        Record rec = query.fetchOne();
        if(rec == null){
            return null;
        }else {
            return rec.into(ProposalUnversionedAttribute.class);
        }
    }

    @Override
    public List<ProposalUnversionedAttribute> findByGiven(Long proposalId) {
        final SelectQuery<Record> query = dslContext.select()
                .from(PROPOSAL_UNVERSIONED_ATTRIBUTE).getQuery();

        if (proposalId != null) {
            query.addConditions(PROPOSAL_UNVERSIONED_ATTRIBUTE.PROPOSAL_ID.eq(proposalId));
        }
        return query.fetchInto(ProposalUnversionedAttribute.class);
    }

    @Override
    public int delete(Long id_) {
        return dslContext.deleteFrom(PROPOSAL_UNVERSIONED_ATTRIBUTE)
                .where(PROPOSAL_UNVERSIONED_ATTRIBUTE.ID_.eq(id_))
                .execute();
    }


}
