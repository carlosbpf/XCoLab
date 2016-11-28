package com.ext.portlet.model.impl;

import com.ext.portlet.model.ModelCategory;
import com.ext.portlet.model.ModelCategoryModel;
import com.ext.portlet.model.ModelCategorySoap;

import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;

import java.io.Serializable;

import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The base model implementation for the ModelCategory service. Represents a row in the &quot;xcolab_ModelCategory&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface {@link com.ext.portlet.model.ModelCategoryModel} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link ModelCategoryImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ModelCategoryImpl
 * @see com.ext.portlet.model.ModelCategory
 * @see com.ext.portlet.model.ModelCategoryModel
 * @generated
 */
@JSON(strict = true)
public class ModelCategoryModelImpl extends BaseModelImpl<ModelCategory>
    implements ModelCategoryModel {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never modify or reference this class directly. All methods that expect a model category model instance should use the {@link com.ext.portlet.model.ModelCategory} interface instead.
     */
    public static final String TABLE_NAME = "xcolab_ModelCategory";
    public static final Object[][] TABLE_COLUMNS = {
            { "modelCategoryPK", Types.BIGINT },
            { "modelCategoryName", Types.VARCHAR },
            { "modelCategoryDescription", Types.VARCHAR },
            { "modelCategoryDisplayWeight", Types.INTEGER }
        };
    public static final String TABLE_SQL_CREATE = "create table xcolab_ModelCategory (modelCategoryPK LONG not null primary key,modelCategoryName VARCHAR(75) null,modelCategoryDescription VARCHAR(75) null,modelCategoryDisplayWeight INTEGER)";
    public static final String TABLE_SQL_DROP = "drop table xcolab_ModelCategory";
    public static final String ORDER_BY_JPQL = " ORDER BY modelCategory.modelCategoryPK ASC";
    public static final String ORDER_BY_SQL = " ORDER BY xcolab_ModelCategory.modelCategoryPK ASC";
    public static final String DATA_SOURCE = "liferayDataSource";
    public static final String SESSION_FACTORY = "liferaySessionFactory";
    public static final String TX_MANAGER = "liferayTransactionManager";
    public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.util.service.ServiceProps.get(
                "value.object.entity.cache.enabled.com.ext.portlet.model.ModelCategory"),
            true);
    public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(com.liferay.util.service.ServiceProps.get(
                "value.object.finder.cache.enabled.com.ext.portlet.model.ModelCategory"),
            true);
    public static final boolean COLUMN_BITMASK_ENABLED = false;
    public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(com.liferay.util.service.ServiceProps.get(
                "lock.expiration.time.com.ext.portlet.model.ModelCategory"));
    private static ClassLoader _classLoader = ModelCategory.class.getClassLoader();
    private static Class<?>[] _escapedModelInterfaces = new Class[] {
            ModelCategory.class
        };
    private long _modelCategoryPK;
    private String _modelCategoryName;
    private String _modelCategoryDescription;
    private int _modelCategoryDisplayWeight;
    private ModelCategory _escapedModel;

    public ModelCategoryModelImpl() {
    }

    /**
     * Converts the soap model instance into a normal model instance.
     *
     * @param soapModel the soap model instance to convert
     * @return the normal model instance
     */
    public static ModelCategory toModel(ModelCategorySoap soapModel) {
        if (soapModel == null) {
            return null;
        }

        ModelCategory model = new ModelCategoryImpl();

        model.setModelCategoryPK(soapModel.getModelCategoryPK());
        model.setModelCategoryName(soapModel.getModelCategoryName());
        model.setModelCategoryDescription(soapModel.getModelCategoryDescription());
        model.setModelCategoryDisplayWeight(soapModel.getModelCategoryDisplayWeight());

        return model;
    }

    /**
     * Converts the soap model instances into normal model instances.
     *
     * @param soapModels the soap model instances to convert
     * @return the normal model instances
     */
    public static List<ModelCategory> toModels(ModelCategorySoap[] soapModels) {
        if (soapModels == null) {
            return null;
        }

        List<ModelCategory> models = new ArrayList<ModelCategory>(soapModels.length);

        for (ModelCategorySoap soapModel : soapModels) {
            models.add(toModel(soapModel));
        }

        return models;
    }

    @Override
    public long getPrimaryKey() {
        return _modelCategoryPK;
    }

    @Override
    public void setPrimaryKey(long primaryKey) {
        setModelCategoryPK(primaryKey);
    }

    @Override
    public Serializable getPrimaryKeyObj() {
        return _modelCategoryPK;
    }

    @Override
    public void setPrimaryKeyObj(Serializable primaryKeyObj) {
        setPrimaryKey(((Long) primaryKeyObj).longValue());
    }

    @Override
    public Class<?> getModelClass() {
        return ModelCategory.class;
    }

    @Override
    public String getModelClassName() {
        return ModelCategory.class.getName();
    }

    @Override
    public Map<String, Object> getModelAttributes() {
        Map<String, Object> attributes = new HashMap<String, Object>();

        attributes.put("modelCategoryPK", getModelCategoryPK());
        attributes.put("modelCategoryName", getModelCategoryName());
        attributes.put("modelCategoryDescription", getModelCategoryDescription());
        attributes.put("modelCategoryDisplayWeight",
            getModelCategoryDisplayWeight());

        return attributes;
    }

    @Override
    public void setModelAttributes(Map<String, Object> attributes) {
        Long modelCategoryPK = (Long) attributes.get("modelCategoryPK");

        if (modelCategoryPK != null) {
            setModelCategoryPK(modelCategoryPK);
        }

        String modelCategoryName = (String) attributes.get("modelCategoryName");

        if (modelCategoryName != null) {
            setModelCategoryName(modelCategoryName);
        }

        String modelCategoryDescription = (String) attributes.get(
                "modelCategoryDescription");

        if (modelCategoryDescription != null) {
            setModelCategoryDescription(modelCategoryDescription);
        }

        Integer modelCategoryDisplayWeight = (Integer) attributes.get(
                "modelCategoryDisplayWeight");

        if (modelCategoryDisplayWeight != null) {
            setModelCategoryDisplayWeight(modelCategoryDisplayWeight);
        }
    }

    @JSON
    @Override
    public long getModelCategoryPK() {
        return _modelCategoryPK;
    }

    @Override
    public void setModelCategoryPK(long modelCategoryPK) {
        _modelCategoryPK = modelCategoryPK;
    }

    @JSON
    @Override
    public String getModelCategoryName() {
        if (_modelCategoryName == null) {
            return StringPool.BLANK;
        } else {
            return _modelCategoryName;
        }
    }

    @Override
    public void setModelCategoryName(String modelCategoryName) {
        _modelCategoryName = modelCategoryName;
    }

    @JSON
    @Override
    public String getModelCategoryDescription() {
        if (_modelCategoryDescription == null) {
            return StringPool.BLANK;
        } else {
            return _modelCategoryDescription;
        }
    }

    @Override
    public void setModelCategoryDescription(String modelCategoryDescription) {
        _modelCategoryDescription = modelCategoryDescription;
    }

    @JSON
    @Override
    public int getModelCategoryDisplayWeight() {
        return _modelCategoryDisplayWeight;
    }

    @Override
    public void setModelCategoryDisplayWeight(int modelCategoryDisplayWeight) {
        _modelCategoryDisplayWeight = modelCategoryDisplayWeight;
    }

    @Override
    public ExpandoBridge getExpandoBridge() {
        return ExpandoBridgeFactoryUtil.getExpandoBridge(0,
            ModelCategory.class.getName(), getPrimaryKey());
    }

    @Override
    public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
        ExpandoBridge expandoBridge = getExpandoBridge();

        expandoBridge.setAttributes(serviceContext);
    }

    @Override
    public ModelCategory toEscapedModel() {
        if (_escapedModel == null) {
            _escapedModel = (ModelCategory) ProxyUtil.newProxyInstance(_classLoader,
                    _escapedModelInterfaces, new AutoEscapeBeanHandler(this));
        }

        return _escapedModel;
    }

    @Override
    public Object clone() {
        ModelCategoryImpl modelCategoryImpl = new ModelCategoryImpl();

        modelCategoryImpl.setModelCategoryPK(getModelCategoryPK());
        modelCategoryImpl.setModelCategoryName(getModelCategoryName());
        modelCategoryImpl.setModelCategoryDescription(getModelCategoryDescription());
        modelCategoryImpl.setModelCategoryDisplayWeight(getModelCategoryDisplayWeight());

        modelCategoryImpl.resetOriginalValues();

        return modelCategoryImpl;
    }

    @Override
    public int compareTo(ModelCategory modelCategory) {
        long primaryKey = modelCategory.getPrimaryKey();

        if (getPrimaryKey() < primaryKey) {
            return -1;
        } else if (getPrimaryKey() > primaryKey) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof ModelCategory)) {
            return false;
        }

        ModelCategory modelCategory = (ModelCategory) obj;

        long primaryKey = modelCategory.getPrimaryKey();

        if (getPrimaryKey() == primaryKey) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (int) getPrimaryKey();
    }

    @Override
    public void resetOriginalValues() {
    }

    @Override
    public CacheModel<ModelCategory> toCacheModel() {
        ModelCategoryCacheModel modelCategoryCacheModel = new ModelCategoryCacheModel();

        modelCategoryCacheModel.modelCategoryPK = getModelCategoryPK();

        modelCategoryCacheModel.modelCategoryName = getModelCategoryName();

        String modelCategoryName = modelCategoryCacheModel.modelCategoryName;

        if ((modelCategoryName != null) && (modelCategoryName.length() == 0)) {
            modelCategoryCacheModel.modelCategoryName = null;
        }

        modelCategoryCacheModel.modelCategoryDescription = getModelCategoryDescription();

        String modelCategoryDescription = modelCategoryCacheModel.modelCategoryDescription;

        if ((modelCategoryDescription != null) &&
                (modelCategoryDescription.length() == 0)) {
            modelCategoryCacheModel.modelCategoryDescription = null;
        }

        modelCategoryCacheModel.modelCategoryDisplayWeight = getModelCategoryDisplayWeight();

        return modelCategoryCacheModel;
    }

    @Override
    public String toString() {
        StringBundler sb = new StringBundler(9);

        sb.append("{modelCategoryPK=");
        sb.append(getModelCategoryPK());
        sb.append(", modelCategoryName=");
        sb.append(getModelCategoryName());
        sb.append(", modelCategoryDescription=");
        sb.append(getModelCategoryDescription());
        sb.append(", modelCategoryDisplayWeight=");
        sb.append(getModelCategoryDisplayWeight());
        sb.append("}");

        return sb.toString();
    }

    @Override
    public String toXmlString() {
        StringBundler sb = new StringBundler(16);

        sb.append("<model><model-name>");
        sb.append("com.ext.portlet.model.ModelCategory");
        sb.append("</model-name>");

        sb.append(
            "<column><column-name>modelCategoryPK</column-name><column-value><![CDATA[");
        sb.append(getModelCategoryPK());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>modelCategoryName</column-name><column-value><![CDATA[");
        sb.append(getModelCategoryName());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>modelCategoryDescription</column-name><column-value><![CDATA[");
        sb.append(getModelCategoryDescription());
        sb.append("]]></column-value></column>");
        sb.append(
            "<column><column-name>modelCategoryDisplayWeight</column-name><column-value><![CDATA[");
        sb.append(getModelCategoryDisplayWeight());
        sb.append("]]></column-value></column>");

        sb.append("</model>");

        return sb.toString();
    }
}
