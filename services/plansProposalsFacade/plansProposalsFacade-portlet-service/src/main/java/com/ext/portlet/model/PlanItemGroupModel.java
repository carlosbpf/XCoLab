package com.ext.portlet.model;

import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

/**
 * The base model interface for the PlanItemGroup service. Represents a row in the &quot;xcolab_PlanItemGroup&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.ext.portlet.model.impl.PlanItemGroupModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.ext.portlet.model.impl.PlanItemGroupImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see PlanItemGroup
 * @see com.ext.portlet.model.impl.PlanItemGroupImpl
 * @see com.ext.portlet.model.impl.PlanItemGroupModelImpl
 * @generated
 */
public interface PlanItemGroupModel extends BaseModel<PlanItemGroup> {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never modify or reference this interface directly. All methods that expect a plan item group model instance should use the {@link PlanItemGroup} interface instead.
     */

    /**
     * Returns the primary key of this plan item group.
     *
     * @return the primary key of this plan item group
     */
    public long getPrimaryKey();

    /**
     * Sets the primary key of this plan item group.
     *
     * @param primaryKey the primary key of this plan item group
     */
    public void setPrimaryKey(long primaryKey);

    /**
     * Returns the plan ID of this plan item group.
     *
     * @return the plan ID of this plan item group
     */
    public long getPlanId();

    /**
     * Sets the plan ID of this plan item group.
     *
     * @param planId the plan ID of this plan item group
     */
    public void setPlanId(long planId);

    /**
     * Returns the group ID of this plan item group.
     *
     * @return the group ID of this plan item group
     */
    public long getGroupId();

    /**
     * Sets the group ID of this plan item group.
     *
     * @param groupId the group ID of this plan item group
     */
    public void setGroupId(long groupId);

    public boolean isNew();

    public void setNew(boolean n);

    public boolean isCachedModel();

    public void setCachedModel(boolean cachedModel);

    public boolean isEscapedModel();

    public Serializable getPrimaryKeyObj();

    public void setPrimaryKeyObj(Serializable primaryKeyObj);

    public ExpandoBridge getExpandoBridge();

    public void setExpandoBridgeAttributes(ServiceContext serviceContext);

    public Object clone();

    public int compareTo(PlanItemGroup planItemGroup);

    public int hashCode();

    public CacheModel<PlanItemGroup> toCacheModel();

    public PlanItemGroup toEscapedModel();

    public String toString();

    public String toXmlString();
}