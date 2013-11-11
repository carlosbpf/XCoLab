package com.ext.portlet.service;

import com.liferay.portal.service.ServiceWrapper;

/**
 * <p>
 * This class is a wrapper for {@link ProposalContestPhaseAttributeTypeLocalService}.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       ProposalContestPhaseAttributeTypeLocalService
 * @generated
 */
public class ProposalContestPhaseAttributeTypeLocalServiceWrapper
    implements ProposalContestPhaseAttributeTypeLocalService,
        ServiceWrapper<ProposalContestPhaseAttributeTypeLocalService> {
    private ProposalContestPhaseAttributeTypeLocalService _proposalContestPhaseAttributeTypeLocalService;

    public ProposalContestPhaseAttributeTypeLocalServiceWrapper(
        ProposalContestPhaseAttributeTypeLocalService proposalContestPhaseAttributeTypeLocalService) {
        _proposalContestPhaseAttributeTypeLocalService = proposalContestPhaseAttributeTypeLocalService;
    }

    /**
    * Adds the proposal contest phase attribute type to the database. Also notifies the appropriate model listeners.
    *
    * @param proposalContestPhaseAttributeType the proposal contest phase attribute type
    * @return the proposal contest phase attribute type that was added
    * @throws SystemException if a system exception occurred
    */
    public com.ext.portlet.model.ProposalContestPhaseAttributeType addProposalContestPhaseAttributeType(
        com.ext.portlet.model.ProposalContestPhaseAttributeType proposalContestPhaseAttributeType)
        throws com.liferay.portal.kernel.exception.SystemException {
        return _proposalContestPhaseAttributeTypeLocalService.addProposalContestPhaseAttributeType(proposalContestPhaseAttributeType);
    }

    /**
    * Creates a new proposal contest phase attribute type with the primary key. Does not add the proposal contest phase attribute type to the database.
    *
    * @param name the primary key for the new proposal contest phase attribute type
    * @return the new proposal contest phase attribute type
    */
    public com.ext.portlet.model.ProposalContestPhaseAttributeType createProposalContestPhaseAttributeType(
        java.lang.String name) {
        return _proposalContestPhaseAttributeTypeLocalService.createProposalContestPhaseAttributeType(name);
    }

    /**
    * Deletes the proposal contest phase attribute type with the primary key from the database. Also notifies the appropriate model listeners.
    *
    * @param name the primary key of the proposal contest phase attribute type
    * @throws PortalException if a proposal contest phase attribute type with the primary key could not be found
    * @throws SystemException if a system exception occurred
    */
    public void deleteProposalContestPhaseAttributeType(java.lang.String name)
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException {
        _proposalContestPhaseAttributeTypeLocalService.deleteProposalContestPhaseAttributeType(name);
    }

    /**
    * Deletes the proposal contest phase attribute type from the database. Also notifies the appropriate model listeners.
    *
    * @param proposalContestPhaseAttributeType the proposal contest phase attribute type
    * @throws SystemException if a system exception occurred
    */
    public void deleteProposalContestPhaseAttributeType(
        com.ext.portlet.model.ProposalContestPhaseAttributeType proposalContestPhaseAttributeType)
        throws com.liferay.portal.kernel.exception.SystemException {
        _proposalContestPhaseAttributeTypeLocalService.deleteProposalContestPhaseAttributeType(proposalContestPhaseAttributeType);
    }

    /**
    * Performs a dynamic query on the database and returns the matching rows.
    *
    * @param dynamicQuery the dynamic query
    * @return the matching rows
    * @throws SystemException if a system exception occurred
    */
    @SuppressWarnings("rawtypes")
    public java.util.List dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.kernel.exception.SystemException {
        return _proposalContestPhaseAttributeTypeLocalService.dynamicQuery(dynamicQuery);
    }

    /**
    * Performs a dynamic query on the database and returns a range of the matching rows.
    *
    * <p>
    * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
    * </p>
    *
    * @param dynamicQuery the dynamic query
    * @param start the lower bound of the range of model instances
    * @param end the upper bound of the range of model instances (not inclusive)
    * @return the range of matching rows
    * @throws SystemException if a system exception occurred
    */
    @SuppressWarnings("rawtypes")
    public java.util.List dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end) throws com.liferay.portal.kernel.exception.SystemException {
        return _proposalContestPhaseAttributeTypeLocalService.dynamicQuery(dynamicQuery,
            start, end);
    }

    /**
    * Performs a dynamic query on the database and returns an ordered range of the matching rows.
    *
    * <p>
    * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
    * </p>
    *
    * @param dynamicQuery the dynamic query
    * @param start the lower bound of the range of model instances
    * @param end the upper bound of the range of model instances (not inclusive)
    * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
    * @return the ordered range of matching rows
    * @throws SystemException if a system exception occurred
    */
    @SuppressWarnings("rawtypes")
    public java.util.List dynamicQuery(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
        int end,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws com.liferay.portal.kernel.exception.SystemException {
        return _proposalContestPhaseAttributeTypeLocalService.dynamicQuery(dynamicQuery,
            start, end, orderByComparator);
    }

    /**
    * Returns the number of rows that match the dynamic query.
    *
    * @param dynamicQuery the dynamic query
    * @return the number of rows that match the dynamic query
    * @throws SystemException if a system exception occurred
    */
    public long dynamicQueryCount(
        com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
        throws com.liferay.portal.kernel.exception.SystemException {
        return _proposalContestPhaseAttributeTypeLocalService.dynamicQueryCount(dynamicQuery);
    }

    public com.ext.portlet.model.ProposalContestPhaseAttributeType fetchProposalContestPhaseAttributeType(
        java.lang.String name)
        throws com.liferay.portal.kernel.exception.SystemException {
        return _proposalContestPhaseAttributeTypeLocalService.fetchProposalContestPhaseAttributeType(name);
    }

    /**
    * Returns the proposal contest phase attribute type with the primary key.
    *
    * @param name the primary key of the proposal contest phase attribute type
    * @return the proposal contest phase attribute type
    * @throws PortalException if a proposal contest phase attribute type with the primary key could not be found
    * @throws SystemException if a system exception occurred
    */
    public com.ext.portlet.model.ProposalContestPhaseAttributeType getProposalContestPhaseAttributeType(
        java.lang.String name)
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException {
        return _proposalContestPhaseAttributeTypeLocalService.getProposalContestPhaseAttributeType(name);
    }

    public com.liferay.portal.model.PersistedModel getPersistedModel(
        java.io.Serializable primaryKeyObj)
        throws com.liferay.portal.kernel.exception.PortalException,
            com.liferay.portal.kernel.exception.SystemException {
        return _proposalContestPhaseAttributeTypeLocalService.getPersistedModel(primaryKeyObj);
    }

    /**
    * Returns a range of all the proposal contest phase attribute types.
    *
    * <p>
    * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
    * </p>
    *
    * @param start the lower bound of the range of proposal contest phase attribute types
    * @param end the upper bound of the range of proposal contest phase attribute types (not inclusive)
    * @return the range of proposal contest phase attribute types
    * @throws SystemException if a system exception occurred
    */
    public java.util.List<com.ext.portlet.model.ProposalContestPhaseAttributeType> getProposalContestPhaseAttributeTypes(
        int start, int end)
        throws com.liferay.portal.kernel.exception.SystemException {
        return _proposalContestPhaseAttributeTypeLocalService.getProposalContestPhaseAttributeTypes(start,
            end);
    }

    /**
    * Returns the number of proposal contest phase attribute types.
    *
    * @return the number of proposal contest phase attribute types
    * @throws SystemException if a system exception occurred
    */
    public int getProposalContestPhaseAttributeTypesCount()
        throws com.liferay.portal.kernel.exception.SystemException {
        return _proposalContestPhaseAttributeTypeLocalService.getProposalContestPhaseAttributeTypesCount();
    }

    /**
    * Updates the proposal contest phase attribute type in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
    *
    * @param proposalContestPhaseAttributeType the proposal contest phase attribute type
    * @return the proposal contest phase attribute type that was updated
    * @throws SystemException if a system exception occurred
    */
    public com.ext.portlet.model.ProposalContestPhaseAttributeType updateProposalContestPhaseAttributeType(
        com.ext.portlet.model.ProposalContestPhaseAttributeType proposalContestPhaseAttributeType)
        throws com.liferay.portal.kernel.exception.SystemException {
        return _proposalContestPhaseAttributeTypeLocalService.updateProposalContestPhaseAttributeType(proposalContestPhaseAttributeType);
    }

    /**
    * Updates the proposal contest phase attribute type in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
    *
    * @param proposalContestPhaseAttributeType the proposal contest phase attribute type
    * @param merge whether to merge the proposal contest phase attribute type with the current session. See {@link com.liferay.portal.service.persistence.BatchSession#update(com.liferay.portal.kernel.dao.orm.Session, com.liferay.portal.model.BaseModel, boolean)} for an explanation.
    * @return the proposal contest phase attribute type that was updated
    * @throws SystemException if a system exception occurred
    */
    public com.ext.portlet.model.ProposalContestPhaseAttributeType updateProposalContestPhaseAttributeType(
        com.ext.portlet.model.ProposalContestPhaseAttributeType proposalContestPhaseAttributeType,
        boolean merge)
        throws com.liferay.portal.kernel.exception.SystemException {
        return _proposalContestPhaseAttributeTypeLocalService.updateProposalContestPhaseAttributeType(proposalContestPhaseAttributeType,
            merge);
    }

    /**
    * Returns the Spring bean ID for this bean.
    *
    * @return the Spring bean ID for this bean
    */
    public java.lang.String getBeanIdentifier() {
        return _proposalContestPhaseAttributeTypeLocalService.getBeanIdentifier();
    }

    /**
    * Sets the Spring bean ID for this bean.
    *
    * @param beanIdentifier the Spring bean ID for this bean
    */
    public void setBeanIdentifier(java.lang.String beanIdentifier) {
        _proposalContestPhaseAttributeTypeLocalService.setBeanIdentifier(beanIdentifier);
    }

    /**
     * @deprecated Renamed to {@link #getWrappedService}
     */
    public ProposalContestPhaseAttributeTypeLocalService getWrappedProposalContestPhaseAttributeTypeLocalService() {
        return _proposalContestPhaseAttributeTypeLocalService;
    }

    /**
     * @deprecated Renamed to {@link #setWrappedService}
     */
    public void setWrappedProposalContestPhaseAttributeTypeLocalService(
        ProposalContestPhaseAttributeTypeLocalService proposalContestPhaseAttributeTypeLocalService) {
        _proposalContestPhaseAttributeTypeLocalService = proposalContestPhaseAttributeTypeLocalService;
    }

    public ProposalContestPhaseAttributeTypeLocalService getWrappedService() {
        return _proposalContestPhaseAttributeTypeLocalService;
    }

    public void setWrappedService(
        ProposalContestPhaseAttributeTypeLocalService proposalContestPhaseAttributeTypeLocalService) {
        _proposalContestPhaseAttributeTypeLocalService = proposalContestPhaseAttributeTypeLocalService;
    }
}