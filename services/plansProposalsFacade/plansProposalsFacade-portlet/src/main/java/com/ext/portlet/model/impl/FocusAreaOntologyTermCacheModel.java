package com.ext.portlet.model.impl;

import com.ext.portlet.model.FocusAreaOntologyTerm;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.model.CacheModel;

import java.io.Serializable;

/**
 * The cache model class for representing FocusAreaOntologyTerm in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @see FocusAreaOntologyTerm
 * @generated
 */
public class FocusAreaOntologyTermCacheModel implements CacheModel<FocusAreaOntologyTerm>,
    Serializable {
    public long focusAreaId;
    public long ontologyTermId;
    public int order;

    @Override
    public String toString() {
        StringBundler sb = new StringBundler(7);

        sb.append("{focusAreaId=");
        sb.append(focusAreaId);
        sb.append(", ontologyTermId=");
        sb.append(ontologyTermId);
        sb.append(", order=");
        sb.append(order);
        sb.append("}");

        return sb.toString();
    }

    public FocusAreaOntologyTerm toEntityModel() {
        FocusAreaOntologyTermImpl focusAreaOntologyTermImpl = new FocusAreaOntologyTermImpl();

        focusAreaOntologyTermImpl.setFocusAreaId(focusAreaId);
        focusAreaOntologyTermImpl.setOntologyTermId(ontologyTermId);
        focusAreaOntologyTermImpl.setOrder(order);

        focusAreaOntologyTermImpl.resetOriginalValues();

        return focusAreaOntologyTermImpl;
    }
}
