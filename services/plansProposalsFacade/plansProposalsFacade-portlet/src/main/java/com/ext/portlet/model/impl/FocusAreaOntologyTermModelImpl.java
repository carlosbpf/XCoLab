package com.ext.portlet.model.impl;

import com.ext.portlet.model.FocusAreaOntologyTerm;
import com.ext.portlet.model.FocusAreaOntologyTermModel;
import com.ext.portlet.model.FocusAreaOntologyTermSoap;
import com.ext.portlet.service.persistence.FocusAreaOntologyTermPK;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.impl.BaseModelImpl;

import java.io.Serializable;

import java.sql.Types;

import java.util.ArrayList;
import java.util.List;

/**
 * The base model implementation for the FocusAreaOntologyTerm service. Represents a row in the &quot;xcolab_FocusAreaOntologyTerm&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.ext.portlet.model.FocusAreaOntologyTermModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link FocusAreaOntologyTermImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see FocusAreaOntologyTermImpl
 * @see com.ext.portlet.model.FocusAreaOntologyTerm
 * @see com.ext.portlet.model.FocusAreaOntologyTermModel
 * @generated
 */
@JSON(strict = true)
public class FocusAreaOntologyTermModelImpl extends BaseModelImpl<FocusAreaOntologyTerm>
    implements FocusAreaOntologyTermModel {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never modify or reference this class directly. All methods that expect a focus area ontology term model instance should use the {@link com.ext.portlet.model.FocusAreaOntologyTerm} interface instead.
     */
    public static final String TABLE_NAME = "xcolab_FocusAreaOntologyTerm";
    public static final Object[][] TABLE_COLUMNS = {
            { "focusAreaId", Types.BIGINT },
            { "ontologyTermId", Types.BIGINT },
            { "order_", Types.INTEGER }
        };
    public static final String TABLE_SQL_CREATE = "create table xcolab_FocusAreaOntologyTerm (focusAreaId LONG not null,ontologyTermId LONG not null,order_ INTEGER,primary key (focusAreaId, ontologyTermId))";
    public static final String TABLE_SQL_DROP = "drop table xcolab_FocusAreaOntologyTerm";
    public static final String ORDER_BY_JPQL = " ORDER BY focusAreaOntologyTerm.order ASC";
    public static final String ORDER_BY_SQL = " ORDER BY xcolab_FocusAreaOntologyTerm.order_ ASC";
    public static final String DATA_SOURCE = "liferayDataSource";
    public static final String SESSION_FACTORY = "liferaySessionFactory";
    public static final String TX_MANAGER = "liferayTransactionManager";
    public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.util.service.ServiceProps.get(
                "value.object.entity.cache.enabled.com.ext.portlet.model.FocusAreaOntologyTerm"),
            true);
    public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.util.service.ServiceProps.get(
                "value.object.finder.cache.enabled.com.ext.portlet.model.FocusAreaOntologyTerm"),
            true);
    public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(com.liferay.util.service.ServiceProps.get(
                "value.object.column.bitmask.enabled.com.ext.portlet.model.FocusAreaOntologyTerm"),
            true);
    public static long FOCUSAREAID_COLUMN_BITMASK = 1L;
    public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.util.service.ServiceProps.get(
                "lock.expiration.time.com.ext.portlet.model.FocusAreaOntologyTerm"));
    private static ClassLoader _classLoader = FocusAreaOntologyTerm.class.getClassLoader();
    private static Class<?>[] _escapedModelProxyInterfaces = new Class[] {
            FocusAreaOntologyTerm.class
        };
    private long _focusAreaId;
    private long _originalFocusAreaId;
    private boolean _setOriginalFocusAreaId;
    private long _ontologyTermId;
    private int _order;
    private long _columnBitmask;
    private FocusAreaOntologyTerm _escapedModelProxy;

    public FocusAreaOntologyTermModelImpl() {
    }

    /**
     * Converts the soap model instance into a normal model instance.
     *
     * @param soapModel the soap model instance to convert
     * @return the normal model instance
     */
    public static FocusAreaOntologyTerm toModel(
        FocusAreaOntologyTermSoap soapModel) {
        FocusAreaOntologyTerm model = new FocusAreaOntologyTermImpl();

        model.setFocusAreaId(soapModel.getFocusAreaId());
        model.setOntologyTermId(soapModel.getOntologyTermId());
        model.setOrder(soapModel.getOrder());

        return model;
    }

    /**
     * Converts the soap model instances into normal model instances.
     *
     * @param soapModels the soap model instances to convert
     * @return the normal model instances
     */
    public static List<FocusAreaOntologyTerm> toModels(
        FocusAreaOntologyTermSoap[] soapModels) {
        List<FocusAreaOntologyTerm> models = new ArrayList<FocusAreaOntologyTerm>(soapModels.length);

        for (FocusAreaOntologyTermSoap soapModel : soapModels) {
            models.add(toModel(soapModel));
        }

        return models;
    }

    public FocusAreaOntologyTermPK getPrimaryKey() {
        return new FocusAreaOntologyTermPK(_focusAreaId, _ontologyTermId);
    }

    public void setPrimaryKey(FocusAreaOntologyTermPK primaryKey) {
        setFocusAreaId(primaryKey.focusAreaId);
        setOntologyTermId(primaryKey.ontologyTermId);
    }

    public Serializable getPrimaryKeyObj() {
        return new FocusAreaOntologyTermPK(_focusAreaId, _ontologyTermId);
    }

    public void setPrimaryKeyObj(Serializable primaryKeyObj) {
        setPrimaryKey((FocusAreaOntologyTermPK) primaryKeyObj);
    }

    public Class<?> getModelClass() {
        return FocusAreaOntologyTerm.class;
    }

    public String getModelClassName() {
        return FocusAreaOntologyTerm.class.getName();
    }

    @JSON
    public long getFocusAreaId() {
        return _focusAreaId;
    }

    public void setFocusAreaId(long focusAreaId) {
        _columnBitmask |= FOCUSAREAID_COLUMN_BITMASK;

        if (!_setOriginalFocusAreaId) {
            _setOriginalFocusAreaId = true;

            _originalFocusAreaId = _focusAreaId;
        }

        _focusAreaId = focusAreaId;
    }

    public long getOriginalFocusAreaId() {
        return _originalFocusAreaId;
    }

    @JSON
    public long getOntologyTermId() {
        return _ontologyTermId;
    }

    public void setOntologyTermId(long ontologyTermId) {
        _ontologyTermId = ontologyTermId;
    }

    @JSON
    public int getOrder() {
        return _order;
    }

    public void setOrder(int order) {
        _columnBitmask = -1L;

        _order = order;
    }

    public long getColumnBitmask() {
        return _columnBitmask;
    }

    @Override
    public FocusAreaOntologyTerm toEscapedModel() {
        if (_escapedModelProxy == null) {
            _escapedModelProxy = (FocusAreaOntologyTerm) ProxyUtil.newProxyInstance(_classLoader,
                    _escapedModelProxyInterfaces,
                    new AutoEscapeBeanHandler(this));
        }

        return _escapedModelProxy;
    }

    @Override
    public Object clone() {
        FocusAreaOntologyTermImpl focusAreaOntologyTermImpl = new FocusAreaOntologyTermImpl();

        focusAreaOntologyTermImpl.setFocusAreaId(getFocusAreaId());
        focusAreaOntologyTermImpl.setOntologyTermId(getOntologyTermId());
        focusAreaOntologyTermImpl.setOrder(getOrder());

        focusAreaOntologyTermImpl.resetOriginalValues();

        return focusAreaOntologyTermImpl;
    }

    public int compareTo(FocusAreaOntologyTerm focusAreaOntologyTerm) {
        int value = 0;

        if (getOrder() < focusAreaOntologyTerm.getOrder()) {
            value = -1;
        } else if (getOrder() > focusAreaOntologyTerm.getOrder()) {
            value = 1;
        } else {
            value = 0;
        }

        if (value != 0) {
            return value;
        }

        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        FocusAreaOntologyTerm focusAreaOntologyTerm = null;

        try {
            focusAreaOntologyTerm = (FocusAreaOntologyTerm) obj;
        } catch (ClassCastException cce) {
            return false;
        }

        FocusAreaOntologyTermPK primaryKey = focusAreaOntologyTerm.getPrimaryKey();

        if (getPrimaryKey().equals(primaryKey)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getPrimaryKey().hashCode();
    }

    @Override
    public void resetOriginalValues() {
        FocusAreaOntologyTermModelImpl focusAreaOntologyTermModelImpl = this;

        focusAreaOntologyTermModelImpl._originalFocusAreaId = focusAreaOntologyTermModelImpl._focusAreaId;

        focusAreaOntologyTermModelImpl._setOriginalFocusAreaId = false;

        focusAreaOntologyTermModelImpl._columnBitmask = 0;
    }

    @Override
    public CacheModel<FocusAreaOntologyTerm> toCacheModel() {
        FocusAreaOntologyTermCacheModel focusAreaOntologyTermCacheModel = new FocusAreaOntologyTermCacheModel();

        focusAreaOntologyTermCacheModel.focusAreaId = getFocusAreaId();

        focusAreaOntologyTermCacheModel.ontologyTermId = getOntologyTermId();

        focusAreaOntologyTermCacheModel.order = getOrder();

        return focusAreaOntologyTermCacheModel;
    }

    @Override
    public String toString() {
        StringBundler sb = new StringBundler(7);

        sb.append("{focusAreaId=");
        sb.append(getFocusAreaId());
        sb.append(", ontologyTermId=");
        sb.append(getOntologyTermId());
        sb.append(", order=");
        sb.append(getOrder());
        sb.append("}");

        return sb.toString();
    }

    public String toXmlString() {
        StringBundler sb = new StringBundler(13);

        sb.append("<model><model-name>");
        sb.append("com.ext.portlet.model.FocusAreaOntologyTerm");
        sb.append("</model-name>");

        sb.append(
            "<column><column-name>focusAreaId</column-name><column-value><![CDATA[");
        sb.append(getFocusAreaId());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>ontologyTermId</column-name><column-value><![CDATA[");
        sb.append(getOntologyTermId());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>order</column-name><column-value><![CDATA[");
        sb.append(getOrder());
        sb.append("]]></column-value></column>");

        sb.append("</model>");

        return sb.toString();
    }
}
