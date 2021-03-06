package org.xcolab.service.proposal.domain.pointtype;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xcolab.model.tables.pojos.PointType;
import org.xcolab.model.tables.records.PointTypeRecord;
import org.xcolab.service.proposal.exceptions.NotFoundException;

import java.util.List;

import static org.xcolab.model.Tables.POINT_TYPE;

@Repository
public class PointTypeDaoImpl implements PointTypeDao {

    @Autowired
    private DSLContext dslContext;

    public PointType create(PointType pointType) {

        PointTypeRecord ret = this.dslContext.insertInto(POINT_TYPE)
                .set(POINT_TYPE.ID_, pointType.getId_())
                .set(POINT_TYPE.PARENT_POINT_TYPE_ID, pointType.getParentPointTypeId())
                .set(POINT_TYPE.PERCENTAGE_OF_PARENT, pointType.getPercentageOfParent())
                .set(POINT_TYPE.DISTRIBUTION_STRATEGY, pointType.getDistributionStrategy())
                .set(POINT_TYPE.RECEIVER_LIMITATION_STRATEGY, pointType.getReceiverLimitationStrategy())
                .set(POINT_TYPE.NAME, pointType.getName())
                .set(POINT_TYPE.SORT, pointType.getSort())
                .returning(POINT_TYPE.ID_)
                .fetchOne();
        if (ret != null) {
            pointType.setId_(ret.getValue(POINT_TYPE.ID_));
            return pointType;
        } else {
            return null;
        }

    }

    public PointType get(Long id_) throws NotFoundException {

        final Record record =  this.dslContext.selectFrom(POINT_TYPE)
                .where(POINT_TYPE.ID_.eq(id_))
                .fetchOne();

        if (record == null) {
            throw new NotFoundException("PointType with id " + id_ + " does not exist");
        }
        return record.into(PointType.class);

    }

    public boolean update(PointType pointType) {
        return dslContext.update(POINT_TYPE)
                .set(POINT_TYPE.ID_, pointType.getId_())
                .set(POINT_TYPE.PARENT_POINT_TYPE_ID, pointType.getParentPointTypeId())
                .set(POINT_TYPE.PERCENTAGE_OF_PARENT, pointType.getPercentageOfParent())
                .set(POINT_TYPE.DISTRIBUTION_STRATEGY, pointType.getDistributionStrategy())
                .set(POINT_TYPE.RECEIVER_LIMITATION_STRATEGY, pointType.getReceiverLimitationStrategy())
                .set(POINT_TYPE.NAME, pointType.getName())
                .set(POINT_TYPE.SORT, pointType.getSort())
                .where(POINT_TYPE.ID_.eq(pointType.getId_()))
                .execute() > 0;
    }

    @Override
    public List<PointType> findByGiven(Long parentPointTypeId) {
        final SelectQuery<Record> query = dslContext.select()
                .from(POINT_TYPE).getQuery();

        if (parentPointTypeId != null) {
            query.addConditions(POINT_TYPE.PARENT_POINT_TYPE_ID.eq(parentPointTypeId));
        }
        query.addOrderBy(POINT_TYPE.SORT.asc());

        return query.fetchInto(PointType.class);
    }
}
