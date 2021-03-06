package org.xcolab.service.proposal.domain.pointsdistributionconfiguration;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xcolab.model.tables.pojos.PointsDistributionConfiguration;
import org.xcolab.model.tables.records.PointsDistributionConfigurationRecord;
import org.xcolab.service.proposal.exceptions.NotFoundException;

import java.util.List;

import static org.xcolab.model.Tables.POINTS_DISTRIBUTION_CONFIGURATION;

@Repository
public class PointsDistributionConfigurationDaoImpl implements PointsDistributionConfigurationDao {

    @Autowired
    private DSLContext dslContext;

    @Override
    public PointsDistributionConfiguration create(PointsDistributionConfiguration pointsDistributionConfiguration) {

        PointsDistributionConfigurationRecord ret = this.dslContext.insertInto(POINTS_DISTRIBUTION_CONFIGURATION)
                .set(POINTS_DISTRIBUTION_CONFIGURATION.ID_, pointsDistributionConfiguration.getId_())
                .set(POINTS_DISTRIBUTION_CONFIGURATION.PROPOSAL_ID, pointsDistributionConfiguration.getProposalId())
                .set(POINTS_DISTRIBUTION_CONFIGURATION.POINT_TYPE_ID, pointsDistributionConfiguration.getPointTypeId())
                .set(POINTS_DISTRIBUTION_CONFIGURATION.TARGET_USER_ID, pointsDistributionConfiguration.getTargetUserId())
                .set(POINTS_DISTRIBUTION_CONFIGURATION.TARGET_SUB_PROPOSAL_ID, pointsDistributionConfiguration.getTargetSubProposalId())
                .set(POINTS_DISTRIBUTION_CONFIGURATION.TARGET_PLAN_SECTION_DEFINITION_ID, pointsDistributionConfiguration.getTargetPlanSectionDefinitionId())
                .set(POINTS_DISTRIBUTION_CONFIGURATION.PERCENTAGE, pointsDistributionConfiguration.getPercentage())
                .set(POINTS_DISTRIBUTION_CONFIGURATION.CREATOR, pointsDistributionConfiguration.getCreator())
                .set(POINTS_DISTRIBUTION_CONFIGURATION.CREATE_DATE, pointsDistributionConfiguration.getCreateDate())
                .returning(POINTS_DISTRIBUTION_CONFIGURATION.ID_)
                .fetchOne();
        if (ret != null) {
            pointsDistributionConfiguration.setId_(ret.getValue(POINTS_DISTRIBUTION_CONFIGURATION.ID_));
            return pointsDistributionConfiguration;
        } else {
            return null;
        }

    }

    @Override
    public boolean update(PointsDistributionConfiguration pointsDistributionConfiguration) {
        return dslContext.update(POINTS_DISTRIBUTION_CONFIGURATION)
                .set(POINTS_DISTRIBUTION_CONFIGURATION.ID_, pointsDistributionConfiguration.getId_())
                .set(POINTS_DISTRIBUTION_CONFIGURATION.PROPOSAL_ID, pointsDistributionConfiguration.getProposalId())
                .set(POINTS_DISTRIBUTION_CONFIGURATION.POINT_TYPE_ID, pointsDistributionConfiguration.getPointTypeId())
                .set(POINTS_DISTRIBUTION_CONFIGURATION.TARGET_USER_ID, pointsDistributionConfiguration.getTargetUserId())
                .set(POINTS_DISTRIBUTION_CONFIGURATION.TARGET_SUB_PROPOSAL_ID, pointsDistributionConfiguration.getTargetSubProposalId())
                .set(POINTS_DISTRIBUTION_CONFIGURATION.TARGET_PLAN_SECTION_DEFINITION_ID, pointsDistributionConfiguration.getTargetPlanSectionDefinitionId())
                .set(POINTS_DISTRIBUTION_CONFIGURATION.PERCENTAGE, pointsDistributionConfiguration.getPercentage())
                .set(POINTS_DISTRIBUTION_CONFIGURATION.CREATOR, pointsDistributionConfiguration.getCreator())
                .set(POINTS_DISTRIBUTION_CONFIGURATION.CREATE_DATE, pointsDistributionConfiguration.getCreateDate())
                .where(POINTS_DISTRIBUTION_CONFIGURATION.ID_.eq(pointsDistributionConfiguration.getId_()))
                .execute() > 0;
    }

    @Override
    public List<PointsDistributionConfiguration> findByGiven(Long proposalId, Long pointTypeId) {
        final SelectQuery<Record> query = dslContext.select()
                .from(POINTS_DISTRIBUTION_CONFIGURATION).getQuery();

        if (proposalId != null) {
            query.addConditions(POINTS_DISTRIBUTION_CONFIGURATION.PROPOSAL_ID.eq(proposalId));
        }
        if (pointTypeId != null) {
            query.addConditions(POINTS_DISTRIBUTION_CONFIGURATION.POINT_TYPE_ID.eq(pointTypeId));
        }
        return query.fetchInto(PointsDistributionConfiguration.class);
    }

    @Override
    public int deleteByProposalId(Long proposalId) {
        return dslContext.deleteFrom(POINTS_DISTRIBUTION_CONFIGURATION)
                .where(POINTS_DISTRIBUTION_CONFIGURATION.PROPOSAL_ID.eq(proposalId))
                .execute();
    }
    @Override
    public int delete(Long id_) {
        return dslContext.deleteFrom(POINTS_DISTRIBUTION_CONFIGURATION)
                .where(POINTS_DISTRIBUTION_CONFIGURATION.ID_.eq(id_))
                .execute();
    }

    @Override
    public PointsDistributionConfiguration get(Long id_) throws NotFoundException {

        final Record record = this.dslContext.selectFrom(POINTS_DISTRIBUTION_CONFIGURATION)
                .where(POINTS_DISTRIBUTION_CONFIGURATION.ID_.eq(id_))
                .fetchOne();

        if (record == null) {
            throw new NotFoundException("PointsDistributionConfiguration with id " + id_ + " does not exist");
        }
        return record.into(PointsDistributionConfiguration.class);

    }

    @Override
    public PointsDistributionConfiguration getByPlanSectionDefinitionId(Long targetPlanSectionDefinitionId) throws NotFoundException {

        final Record record = this.dslContext.selectFrom(POINTS_DISTRIBUTION_CONFIGURATION)
                .where(POINTS_DISTRIBUTION_CONFIGURATION.TARGET_PLAN_SECTION_DEFINITION_ID.eq(targetPlanSectionDefinitionId))
                .fetchOne();

        if (record == null) {
            throw new NotFoundException("PointsDistributionConfiguration with targetPlanSectionDefinitionId " + targetPlanSectionDefinitionId + " does not exist");
        }
        return record.into(PointsDistributionConfiguration.class);

    }
}
