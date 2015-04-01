/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/

package org.topcased.gpm.business.revision.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.attributes.impl.CacheableAttributesContainer;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.authorization.service.TypeAccessControlData;
import org.topcased.gpm.business.dynamic.DynamicValuesContainerAccessFactory;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.ConstraintException;
import org.topcased.gpm.business.exception.FieldValidationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ContextBase;
import org.topcased.gpm.business.extensions.service.ExtensionPointNames;
import org.topcased.gpm.business.extensions.service.ExtensionPointParameters;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.fields.service.FieldsService;
import org.topcased.gpm.business.revision.RevisionData;
import org.topcased.gpm.business.revision.RevisionDifferencesData;
import org.topcased.gpm.business.revision.RevisionSummaryData;
import org.topcased.gpm.business.revision.service.RevisionService;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.domain.attributes.Attribute;
import org.topcased.gpm.domain.attributes.AttributeValue;
import org.topcased.gpm.domain.attributes.AttributesContainer;
import org.topcased.gpm.domain.dictionary.Environment;
import org.topcased.gpm.domain.extensions.ExtensionPoint;
import org.topcased.gpm.domain.fields.FieldsContainer;
import org.topcased.gpm.domain.fields.ValuesContainer;
import org.topcased.gpm.domain.revision.Revision;
import org.topcased.gpm.domain.sheet.Sheet;
import org.topcased.gpm.domain.sheet.SheetType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Implementation of the revision management service
 * 
 * @author mfranche
 */
public class RevisionServiceImpl extends ServiceImplBase implements
        RevisionService {
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.revision.service.RevisionService#createRevision(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public String createRevision(final String pRoleToken,
            final String pContainerId, final String pLabel)
        throws AuthorizationException {
        final CacheableRevision lRevision = new CacheableRevision();

        lRevision.setAuthor(getAuthService().getLoggedUserData(
                getAuthService().getUserSessionFromRoleSession(pRoleToken), true).getLogin());
        lRevision.setCreationDate(new Date());
        lRevision.setLabel(pLabel);

        return createRevision(pRoleToken, pContainerId, lRevision, null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.revision.service.RevisionService#createRevision(java.lang.String,
     *      java.lang.String, org.topcased.gpm.business.revision.RevisionData)
     */
    @Override
    public String createRevision(final String pRoleToken,
            final String pContainerId, final RevisionData pRevisionData)
        throws AuthorizationException {
        final CacheableRevision lRevision =
                dataTransformationServiceImpl.getCacheableRevisionFromRevisionData(
                        pRoleToken, pRevisionData, pContainerId);

        return createRevision(pRoleToken, pContainerId, lRevision, null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.revision.service.RevisionService#createRevision(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.revision.impl.CacheableRevision,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public String createRevision(final String pRoleToken,
            final String pRevisedContainerId,
            final CacheableRevision pCacheableRevision, final Context pCtx) {
        CacheableRevision lCacheableRevision = pCacheableRevision;

        // Check the validity of the token
        if (StringUtils.isBlank(pRoleToken)) {
            throw new InvalidTokenException("The role token is blank.");
        }

        // Verify if the sheet is writable by pRoleToken
        // an exception is launched if the sheet is not writable.
        getFieldsContainerServiceImpl().assertValuesContainerIsReadable(
                pRoleToken, pRevisedContainerId);

        // Ensure that data.sheetId == null
        if (lCacheableRevision.getId() != null) {
            throw new GDMException("The revision id "
                    + lCacheableRevision.getId()
                    + " already exists in DB and cannot be created");
        }

        // Verify if revision data label has been filled
        if (StringUtils.isBlank(lCacheableRevision.getLabel())) {
            throw new GDMException("The revision data has no label");
        }

        // Get domain objects
        final ValuesContainer lRevisedContainer =
                getValuesContainer(pRevisedContainerId);

        // Check that is allowed to create revision for a container
        if (!isRevisionEnabled(lRevisedContainer.getDefinition().getId())) {
            throw new AuthorizationException(
                    "Revision control is not enabled on the sheet type "
                            + lRevisedContainer.getDefinition().getId());
        }

        // Verify if this label has not already been set to another revision
        for (Revision lRevision : lRevisedContainer.getRevisions()) {
            if (lRevision.getLabel().compareTo(lCacheableRevision.getLabel()) == 0) {
                throw new GDMException("A revision with label "
                        + lCacheableRevision.getLabel()
                        + " already exists on the container "
                        + pRevisedContainerId);
            }
        }

        final CacheableValuesContainer lCacheableRevisedContainer =
                getCachedValuesContainer(pRevisedContainerId,
                        CacheProperties.IMMUTABLE.getCacheFlags());
        final CacheableSheetType lCacheableRevisedContainerType =
                getSheetService().getCacheableSheetType(
                        lCacheableRevisedContainer.getTypeId(),
                        CacheProperties.IMMUTABLE);

        // Get sheet type (only if revised container is a sheet)
        final String lSheetState;

        if (lCacheableRevisedContainer instanceof CacheableSheet) {
            final org.topcased.gpm.business.serialization.data.Attribute lStateAttribute =
                    new org.topcased.gpm.business.serialization.data.Attribute();
            CacheableAttributesContainer lRevisionAttributes =
                    lCacheableRevision.getRevisionAttributes();

            if (lRevisionAttributes == null) {
                lRevisionAttributes = new CacheableAttributesContainer();
                lCacheableRevision.setRevisionAttributes(lRevisionAttributes);
            }

            lSheetState =
                    ((CacheableSheet) lCacheableRevisedContainer).getCurrentStateName();
            // Add sheet state on attributes
            lStateAttribute.setName(SHEET_STATE_ATTRIBUTE_NAME);
            lStateAttribute.setValue(lSheetState);
            lRevisionAttributes.addAttributes(Collections.singletonList(lStateAttribute));
        }
        else {
            lSheetState = null;
        }

        // Get the type access control
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        String lProductName = getAuthService().getProductName(pRoleToken);
        lAccessControlContextData.setRoleName(getAuthService().getRoleNameFromToken(
                pRoleToken));
        lAccessControlContextData.setProductName(lProductName);
        lAccessControlContextData.setStateName(lSheetState);
        lAccessControlContextData.setContainerTypeId(lCacheableRevisedContainerType.getId());
        final TypeAccessControlData lTypeAccessControlData =
                getAuthService().getTypeAccessControl(pRoleToken,
                        lAccessControlContextData);

        // Result is null if the user hasn't the right to get this sheetType
        if (lTypeAccessControlData != null
                && lTypeAccessControlData.getConfidential()) {
            throw new AuthorizationException("Illegal access to the sheet "
                    + lCacheableRevisedContainer.getId()
                    + " : the access is confidential ");
        }

        // Extension point preCreateRevision
        final ExtensionPoint lPreCreateRevision =
                getExecutableExtensionPoint(
                        lCacheableRevisedContainer.getTypeId(),
                        ExtensionPointNames.PRE_CREATE_REVISION, pCtx);

        if (lPreCreateRevision != null) {
            final RevisionData lRevisionData =
                    dataTransformationServiceImpl.getRevisionDataFromCacheableRevision(
                            pRoleToken, lCacheableRevision, pRevisedContainerId);
            final Context lCtx = new ContextBase(pCtx);

            lCtx.put(ExtensionPointParameters.REVISION_DATA, lRevisionData);

            lCtx.put(ExtensionPointParameters.VALUES_CONTAINER_ID,
                    pRevisedContainerId);

            getExtensionsService().execute(pRoleToken, lPreCreateRevision, lCtx);
            lCacheableRevision =
                    dataTransformationServiceImpl.getCacheableRevisionFromRevisionData(
                            pRoleToken, lRevisionData, pRevisedContainerId);
        }

        // If no field values, use the values of the revised container
        if (lCacheableRevision.getValuesMap().isEmpty()) {
            lCacheableRevision.setValuesMap(lCacheableRevisedContainer.getValuesMap());
        }

        // Create a new revision object for domain
        final String lRevisionId =
                doCreateRevision(lCacheableRevision,
                        lCacheableRevisedContainerType, lRevisedContainer, pCtx);

        // Extension point lPostCreateRevision
        final ExtensionPoint lPostCreateRevision =
                getExecutableExtensionPoint(
                        lCacheableRevisedContainer.getTypeId(),
                        ExtensionPointNames.POST_CREATE_REVISION, pCtx);

        if (lPostCreateRevision != null) {
            final Context lCtx = new ContextBase(pCtx);

            lCtx.put(ExtensionPointParameters.REVISION_ID, lRevisionId);

            lCtx.put(ExtensionPointParameters.VALUES_CONTAINER_ID,
                    pRevisedContainerId);

            getExtensionsService().execute(pRoleToken, lPostCreateRevision,
                    lCtx);
        }

        return lRevisionId;
    }

    private String doCreateRevision(CacheableRevision pCacheableRevision,
            CacheableFieldsContainer pType, ValuesContainer pRevisedContainer,
            Context pCtx) {
        final Revision lRevisionEntity =
                revisionDao.getNewRevision(pType.getId());
        final FieldsContainer lTypeEntity = getFieldsContainer(pType.getId());

        lRevisionEntity.setId(pCacheableRevision.getId());
        lRevisionEntity.setVersion(pCacheableRevision.getVersion());

        // Set revision information
        lRevisionEntity.setLabel(pCacheableRevision.getLabel());
        lRevisionEntity.setAuthor(pCacheableRevision.getAuthor());
        lRevisionEntity.setCreationDate(pCacheableRevision.getCreationDate());

        // Same definition that the persisted type
        lRevisionEntity.setDefinition(lTypeEntity);

        // Create the attributes container associated to the revision
        final AttributesContainer lRevisionAttributesContainer =
                AttributesContainer.newInstance();
        final CacheableAttributesContainer lRevisionAttributes =
                pCacheableRevision.getRevisionAttributes();

        // Fill attributes container with attributes of the cacheable revision
        if (lRevisionAttributes != null
                && lRevisionAttributes.getAllAttributes() != null) {
            for (org.topcased.gpm.business.serialization.data.Attribute lAttributeData : lRevisionAttributes.getAllAttributes()) {
                final Attribute lAttribute = Attribute.newInstance();

                // The name of the attribute
                lAttribute.setName(lAttributeData.getName());

                // The values of the attribute 
                final ArrayList<AttributeValue> lArrayList =
                        new ArrayList<AttributeValue>();

                for (String lValue : lAttributeData.getValues()) {
                    final AttributeValue lAttributeValue =
                            AttributeValue.newInstance();

                    lAttributeValue.setStringValue(lValue);
                    lArrayList.add(lAttributeValue);
                }

                // Attribute values persistence in DB is associated to the attribute one
                lAttribute.setAttributeValues(lArrayList);
                // Attributes persistence in DB is associated to the attributes container one
                lRevisionAttributesContainer.addToAttributeList(lAttribute);
            }
        }

        // Attributes container persistence in DB is associated to the revision one
        lRevisionEntity.setRevisionAttrs(lRevisionAttributesContainer);

        // Persist the revision in DB
        getRevisionDao().create(lRevisionEntity);

        // Create the revision field values
        DynamicValuesContainerAccessFactory.getInstance().getAccessorOnRevision(
                pType.getId()).updateDomainFromBusiness(lRevisionEntity,
                pCacheableRevision, pCtx);

        // Create the attributes of the revision
        getAttributesService().set(lRevisionEntity.getId(),
                pCacheableRevision.getAllAttributes());

        // Same environment that the sheet
        for (Environment lEnv : pRevisedContainer.getEnvironments()) {
            lRevisionEntity.addToEnvironmentList(lEnv);
        }

        // For sheets, update the cache of the reference
        if (lTypeEntity instanceof SheetType) {
            final String lRevisionRef =
                    DynamicValuesContainerAccessFactory.getInstance().getAccessorOnRevision(
                            pType.getId()).getFieldValueAsString(
                            FieldsService.REFERENCE_FIELD_NAME, lRevisionEntity);

            lRevisionEntity.setReferenceCache(lRevisionRef);
        }

        // Add the revision to the sheet and update sheet in DB
        pRevisedContainer.addToRevisionList(lRevisionEntity);

        return lRevisionEntity.getId();
    }

    /**
     * Import a list of revisions on a given object.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pElementId
     *            The identifier of element containing the revisions
     * @param pTypeId
     *            Identifier of element type
     * @param pRevisions
     *            List of revisions to import
     * @param pCtx
     *            Execution context
     * @throws FieldValidationException
     *             A field is not valid.
     */
    // TODO declare in interface
    public void importRevisions(
            String pRoleToken,
            String pElementId,
            String pTypeId,
            List<org.topcased.gpm.business.serialization.data.RevisionData> pRevisions,
            Context pCtx) throws FieldValidationException {
        getAuthService().assertGlobalAdminRole(pRoleToken);

        CacheableFieldsContainer lType =
                getCachedFieldsContainer(pTypeId, CACHE_IMMUTABLE_OBJECT);
        ValuesContainer lContainer = getValuesContainer(pElementId);

        if (!lContainer.getRevisions().isEmpty()) {
            // Remove already existing revisions.
            Collection<Revision> lRevisionEntities;
            lRevisionEntities =
                    new ArrayList<Revision>(lContainer.getRevisions());
            for (Revision lRevisionEentityToRemove : lRevisionEntities) {
                doDeleteRevision(lRevisionEentityToRemove.getId());
            }
            // Clear the previously existing revisions list
            lContainer.getRevisions().clear();
        }

        // Create the imported revisions.
        if (null != pRevisions && !pRevisions.isEmpty()) {
            for (org.topcased.gpm.business.serialization.data.RevisionData lRevision : pRevisions) {
                CacheableRevision lCachedRevision =
                        new CacheableRevision(lRevision, lType);
                doCreateRevision(lCachedRevision, lType, lContainer, pCtx);
            }
        }
    }

    /**
     * Verify if revisions are enabled on this type (by getting the value of the
     * extended attribute named "revisionEnabled")
     * 
     * @param pContainerId
     *            The type identifier
     * @return Boolean indicating if the revision is enabled
     */
    protected boolean isRevisionEnabled(String pContainerId) {
        final CacheableFieldsContainer lType =
                getCachedFieldsContainer(pContainerId,
                        CacheProperties.IMMUTABLE.getCacheFlags());
        // Check if the revision management is enabled on the type
        final String[] lAttrValues =
                lType.getAttributeValues(REVISION_ENABLED_ATTRIBUTE_NAME);

        if (null == lAttrValues) {
            return false;
        }
        else {
            // If the attribute has a value, convert it as a boolean.
            return Boolean.valueOf(lAttrValues[0]);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.revision.service.RevisionService#deleteRevision(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void deleteRevision(final String pRoleToken,
            final String pContainerId) throws AuthorizationException {
        // Verify if the sheet is writable by pRoleToken
        // an exception is launched if the sheet is not writable.
        getFieldsContainerServiceImpl().assertValuesContainerIsWritable(
                pRoleToken, pContainerId);

        final CacheableSheet lCacheableSheet =
                getSheetService().getCacheableSheet(pContainerId,
                        CacheProperties.IMMUTABLE);
        final CacheableSheetType lCacheableSheetType =
                getSheetService().getCacheableSheetType(
                        lCacheableSheet.getTypeId(), CacheProperties.IMMUTABLE);

        // gets the sheetType access control for this sheet
        final String lStateName = lCacheableSheet.getCurrentStateName();
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        String lProductName = getAuthService().getProductName(pRoleToken);
        lAccessControlContextData.setRoleName(getAuthService().getRoleNameFromToken(
                pRoleToken));
        lAccessControlContextData.setProductName(lProductName);
        lAccessControlContextData.setStateName(lStateName);
        lAccessControlContextData.setContainerTypeId(lCacheableSheetType.getId());
        final TypeAccessControlData lTypeAccessControlData =
                getAuthService().getTypeAccessControl(pRoleToken,
                        lAccessControlContextData);

        // Result is null if the user hasn't the right to get this sheetType
        if (lTypeAccessControlData != null
                && lTypeAccessControlData.getConfidential()) {
            throw new AuthorizationException("Illegal access to the sheet "
                    + lCacheableSheet.getId()
                    + " : the access is confidential ");
        }

        if (!isRevisionEnabled(lCacheableSheetType.getId())) {
            throw new AuthorizationException(
                    "Revision control is not enabled on the sheet type "
                            + lCacheableSheetType.getId());
        }

        final ValuesContainer lSheet = getValuesContainer(pContainerId);

        // Get latest revision on pContainer
        final Revision lLatestRevision = getLatestRevisionOnContainer(lSheet);

        if (null == lLatestRevision) {
            // The revision already exists
            throw new ConstraintException("No revisions on the container");
        }

        // Extension point preDeleteRevision
        final ExtensionPoint lPreDeleteRevision =
                getExecutableExtensionPoint(lCacheableSheetType.getId(),
                        ExtensionPointNames.PRE_DELETE_REVISION, null);

        if (lPreDeleteRevision != null) {
            final Context lCtx = new ContextBase();

            lCtx.put(ExtensionPointParameters.VALUES_CONTAINER_ID, pContainerId);

            lCtx.put(ExtensionPointParameters.REVISION_ID,
                    lLatestRevision.getId());

            getExtensionsService().execute(pRoleToken, lPreDeleteRevision, lCtx);
        }

        doDeleteRevision(lLatestRevision.getId());

        // Extension point postDeleteRevision
        final ExtensionPoint lPostDeleteRevision =
                getExecutableExtensionPoint(lCacheableSheetType.getId(),
                        ExtensionPointNames.POST_DELETE_REVISION, null);

        if (lPostDeleteRevision != null) {
            final Context lCtx = new ContextBase();

            lCtx.put(ExtensionPointParameters.VALUES_CONTAINER_ID, pContainerId);

            lCtx.put(ExtensionPointParameters.REVISION_ID,
                    lLatestRevision.getId());

            getExtensionsService().execute(pRoleToken, lPostDeleteRevision,
                    lCtx);
        }
    }

    private void doDeleteRevision(String pRevisionId)
        throws AuthorizationException {
        final Revision lRevisonEntity =
                (Revision) getRevisionDao().load(pRevisionId);
        final ValuesContainer lRevContainer =
                getRevisionDao().getRevisionContainer(lRevisonEntity);

        // Remove the revision from the list.
        lRevContainer.getRevisions().remove(lRevisonEntity);

        // Remove the revision
        getRevisionDao().remove(lRevisonEntity.getId());
    }

    /**
     * Get the latest revision on a container
     * 
     * @param pRevContainer
     *            The entity object containing the revision
     * @return The latest revision of the specified container (or null)
     */
    private Revision getLatestRevisionOnContainer(ValuesContainer pRevContainer) {
        Collection<Revision> lRevisionCollection = pRevContainer.getRevisions();

        Revision lLastRevision = null;
        for (Revision lRevision : lRevisionCollection) {
            if (lLastRevision == null) {
                lLastRevision = lRevision;
            }
            else {
                if (lRevision.getCreationDate().after(
                        lLastRevision.getCreationDate())) {
                    lLastRevision = lRevision;
                }
            }
        }
        return lLastRevision;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.revision.service.RevisionService#getDifferencesBetweenRevisions(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public RevisionDifferencesData getDifferencesBetweenRevisions(
            String pRoleToken, String pContainerId, String pRevisionFirstId,
            String pRevisionSndId) throws AuthorizationException {
        // Verify if the sheet is readable by pRoleToken
        // an exception is launched if the sheet is not readable.
        getFieldsContainerServiceImpl().assertValuesContainerIsReadable(
                pRoleToken, pContainerId);

        RevisionData lRevisionFirstData =
                getRevisionData(pRoleToken, pContainerId, pRevisionFirstId);
        RevisionData lRevisionSndData =
                getRevisionData(pRoleToken, pContainerId, pRevisionSndId);

        return RevisionUtils.createRevisionDifferencesData(lRevisionFirstData,
                lRevisionSndData);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.revision.service.RevisionService#getCacheableSheetInRevision(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public CacheableSheet getCacheableSheetInRevision(String pRoleToken,
            String pContainerId, String pRevisionId) {
        final CacheableRevision lCacheableRevision =
                getCacheableRevision(pRoleToken, pContainerId, pRevisionId);
        final CacheableSheet lCacheableSheet =
                getSheetService().getCacheableSheet(pContainerId,
                        CacheProperties.MUTABLE);

        // Update with the cacheable revision values
        lCacheableSheet.setFunctionalReference(lCacheableRevision.getFunctionalReference());
        lCacheableSheet.setValuesMap(lCacheableRevision.getValuesMap());

        return lCacheableSheet;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.revision.service.RevisionService#getSerializableSheetInRevision(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public org.topcased.gpm.business.serialization.data.SheetData getSerializableSheetInRevision(
            String pRoleToken, String pContainerId, String pRevisionId)
        throws AuthorizationException {
        CacheableSheet lSheet =
                getCacheableSheetInRevision(pRoleToken, pContainerId,
                        pRevisionId);
        org.topcased.gpm.business.serialization.data.SheetData lSheetData =
                new org.topcased.gpm.business.serialization.data.SheetData();
        lSheet.marshal(lSheetData);
        return lSheetData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.revision.service.RevisionService#getRevisionData(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public org.topcased.gpm.business.serialization.data.RevisionData getRevision(
            String pRoleToken, String pContainerId, String pRevisionId)
        throws AuthorizationException {
        final CacheableRevision lCacheableRevision =
                getCacheableRevision(pRoleToken, pContainerId, pRevisionId);
        final org.topcased.gpm.business.serialization.data.RevisionData lRevData =
                new org.topcased.gpm.business.serialization.data.RevisionData();

        lCacheableRevision.marshal(lRevData);

        return lRevData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.revision.service.RevisionService#getRevisionData(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public RevisionData getRevisionData(String pRoleToken, String pContainerId,
            String pRevisionId) throws AuthorizationException {
        final CacheableRevision lCacheableRevision =
                getCacheableRevision(pRoleToken, pContainerId, pRevisionId);

        return dataTransformationServiceImpl.getRevisionDataFromCacheableRevision(
                pRoleToken, lCacheableRevision, pContainerId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.revision.service.RevisionService#getRevisionsSummary(java.lang.String)
     */
    @Override
    public List<RevisionSummaryData> getRevisionsSummary(String pRoleToken,
            String pContainerId) throws AuthorizationException {

        // Verify if the sheet is readable by pRoleToken
        // an exception is launched if the sheet is not readable.
        getFieldsContainerServiceImpl().assertValuesContainerIsReadable(
                pRoleToken, pContainerId);

        CacheableSheet lCacheableSheet =
                getSheetService().getCacheableSheet(pContainerId,
                        CacheProperties.IMMUTABLE);
        CacheableSheetType lCacheableSheetType =
                getSheetService().getCacheableSheetType(
                        lCacheableSheet.getTypeId(), CacheProperties.IMMUTABLE);

        // gets the sheetType access control for this sheet
        String lStateName = lCacheableSheet.getCurrentStateName();
        TypeAccessControlData lTypeAccessControlData;
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        String lProductName = getAuthService().getProductName(pRoleToken);
        lAccessControlContextData.setRoleName(getAuthService().getRoleNameFromToken(
                pRoleToken));
        lAccessControlContextData.setProductName(lProductName);
        lAccessControlContextData.setStateName(lStateName);
        lAccessControlContextData.setContainerTypeId(lCacheableSheetType.getId());
        lTypeAccessControlData =
                getAuthService().getTypeAccessControl(pRoleToken,
                        lAccessControlContextData);
        if (lTypeAccessControlData != null) {
            // result is null if the user hasn't the right to get this sheetType
            if (lTypeAccessControlData.getConfidential()) {
                throw new AuthorizationException("Illegal access to the sheet "
                        + lCacheableSheet.getId()
                        + " : the access is confidential ");
            }
        }

        if (!isRevisionEnabled(lCacheableSheetType.getId())) {
            throw new AuthorizationException(
                    "Revision control is not enabled on the sheet type "
                            + lCacheableSheetType.getId());
        }
        return getRevisionsSummary(pContainerId);
    }

    /**
     * Get revision summary
     * 
     * @param pContainerId
     *            The container id
     * @return The list of revision summary data
     */
    // TODO declare in superclass
    @SuppressWarnings("unchecked")
    public List<RevisionSummaryData> getRevisionsSummary(String pContainerId) {
        List<Revision> lRevisions = getRevisionDao().getRevisions(pContainerId);
        List<RevisionSummaryData> lRevisionSummaryCollection =
                new ArrayList<RevisionSummaryData>(lRevisions.size());

        for (Revision lRevision : lRevisions) {
            lRevisionSummaryCollection.add(RevisionUtils.createRevisionSummaryData(lRevision));
        }
        return lRevisionSummaryCollection;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.revision.service.RevisionService#getRevisionLabels(java.lang.String,
     *      java.lang.String, int)
     */
    @Override
    public List<String> getRevisionLabels(String pRoleToken,
            String pContainerId, int pLastItems) {
        // Verify if the sheet is readable by pRoleToken
        // an exception is launched if the sheet is not readable.
        getFieldsContainerServiceImpl().assertValuesContainerIsReadable(
                pRoleToken, pContainerId);

        CacheableSheet lCacheableSheet =
                getSheetService().getCacheableSheet(pContainerId,
                        CacheProperties.IMMUTABLE);
        CacheableSheetType lCacheableSheetType =
                getSheetService().getCacheableSheetType(
                        lCacheableSheet.getTypeId(), CacheProperties.IMMUTABLE);

        // gets the sheetType access control for this sheet
        String lStateName = lCacheableSheet.getCurrentStateName();
        TypeAccessControlData lTypeAccessControlData;
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        String lProductName = getAuthService().getProductName(pRoleToken);
        lAccessControlContextData.setRoleName(getAuthService().getRoleNameFromToken(
                pRoleToken));
        lAccessControlContextData.setProductName(lProductName);
        lAccessControlContextData.setStateName(lStateName);
        lAccessControlContextData.setContainerTypeId(lCacheableSheetType.getId());
        lTypeAccessControlData =
                getAuthService().getTypeAccessControl(pRoleToken,
                        lAccessControlContextData);
        if (lTypeAccessControlData != null) {
            // result is null if the user hasn't the right to get this sheetType
            if (lTypeAccessControlData.getConfidential()) {
                throw new AuthorizationException("Illegal access to the sheet "
                        + lCacheableSheet.getId()
                        + " : the access is confidential ");
            }
        }

        if (!isRevisionEnabled(lCacheableSheetType.getId())) {
            throw new AuthorizationException(
                    "Revision control is not enabled on the sheet type "
                            + lCacheableSheetType.getId());
        }

        List<String> lRevisionLabels =
                getRevisionDao().getRevisionLabels(pContainerId, pLastItems);

        return lRevisionLabels;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.revision.service.RevisionService#updateContainerFromRevision(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public void updateContainerFromRevision(String pRoleToken,
            String pContainerId, String pRevisionId)
        throws AuthorizationException {

        // Verify if the sheet is writable by pRoleToken
        // an exception is launched if the sheet is not writable.
        getFieldsContainerServiceImpl().assertValuesContainerIsWritable(
                pRoleToken, pContainerId);

        // Get the revision as sheet data
        CacheableSheet lSheetFromRev =
                getCacheableSheetInRevision(pRoleToken, pContainerId,
                        pRevisionId);
        // Update the sheet with the sheet data
        getSheetService().updateSheet(pRoleToken, lSheetFromRev, null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.revision.service.RevisionService#getCacheableRevision(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public CacheableRevision getCacheableRevision(String pRoleToken,
            String pSheetId, String pRevisionId) {
        // Verify if the sheet is readable by pRoleToken
        // an exception is launched if the sheet is not readable.
        getFieldsContainerServiceImpl().assertValuesContainerIsReadable(
                pRoleToken, pSheetId);

        final CacheableSheet lCacheableSheet =
                getSheetService().getCacheableSheet(pSheetId,
                        CacheProperties.IMMUTABLE);

        // Gets the sheetType access control for this sheet
        final String lStateName = lCacheableSheet.getCurrentStateName();
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        String lProductName = getAuthService().getProductName(pRoleToken);
        lAccessControlContextData.setRoleName(getAuthService().getRoleNameFromToken(
                pRoleToken));
        lAccessControlContextData.setProductName(lProductName);
        lAccessControlContextData.setStateName(lStateName);
        lAccessControlContextData.setContainerTypeId(lCacheableSheet.getTypeId());
        final TypeAccessControlData lTypeAccessControlData =
                getAuthService().getTypeAccessControl(pRoleToken,
                        lAccessControlContextData);

        if (lTypeAccessControlData != null) {
            // Result is null if the user hasn't the right to get this sheetType
            if (lTypeAccessControlData.getConfidential()) {
                throw new AuthorizationException("Illegal access to the sheet "
                        + lCacheableSheet.getId()
                        + " : the access is confidential ");
            }
        }

        if (!isRevisionEnabled(lCacheableSheet.getTypeId())) {
            throw new AuthorizationException(
                    "Revision control is not enabled on the sheet type "
                            + lCacheableSheet.getTypeId());
        }

        return getCacheableRevision(pSheetId, pRevisionId);
    }

    /**
     * Get a cacheable revision
     * 
     * @param pSheetId
     *            The sheet id
     * @param pRevisionId
     *            The revision id
     * @return The cacheable revision
     */
    // TODO declare in interface
    public CacheableRevision getCacheableRevision(String pSheetId,
            String pRevisionId) {
        // Revision is always immutable
        CacheableRevision lCachedRevision =
                (CacheableRevision) getCachedValuesContainer(
                        CacheableRevision.class, pRevisionId,
                        CacheProperties.IMMUTABLE.getCacheFlags());

        if (null == lCachedRevision) {
            final Sheet lSheet = getSheet(pSheetId);
            final Revision lRevision = getRevision(pSheetId, pRevisionId);
            final CacheableSheetType lSheetType =
                    getSheetService().getCacheableSheetType(
                            lSheet.getDefinition().getId(),
                            CacheProperties.IMMUTABLE);
            lCachedRevision = new CacheableRevision(lRevision, lSheetType);
            addElementInCache(lCachedRevision);
        }

        return lCachedRevision;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.revision.service.RevisionService#getRevisionCount(java.lang.String)
     */
    @Override
    public int getRevisionsCount(String pRoleToken, String pContainerId) {
        // Verify if the sheet is readable by pRoleToken
        // an exception is launched if the sheet is not readable.
        getFieldsContainerServiceImpl().assertValuesContainerIsReadable(
                pRoleToken, pContainerId);
        return getRevisionDao().getRevisionsCount(pContainerId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.revision.service.RevisionService#getRevisionDataByLabel(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public RevisionData getRevisionDataByLabel(String pRoleToken,
            String pContainerId, String pLabel) throws AuthorizationException {
        final String lRevisionId =
                getRevisionIdFromRevisionLabel(pRoleToken, pContainerId, pLabel);

        if (StringUtils.isBlank(lRevisionId)) {
            throw new InvalidIdentifierException(lRevisionId);
        }
        return getRevisionData(pRoleToken, pContainerId, lRevisionId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.revision.service.RevisionService#getRevisionIdFromRevisionLabel(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public String getRevisionIdFromRevisionLabel(String pRoleToken,
            String pContainerId, String pLabel) throws AuthorizationException {
        getFieldsContainerServiceImpl().assertValuesContainerIsReadable(
                pRoleToken, pContainerId);
        return getRevisionDao().getRevisionIdByLabel(pContainerId, pLabel);
    }

    /**
     * Get the sheet state value (stored in the revision attributes) of a
     * revision
     * 
     * @param pRevision
     *            The revision
     * @return The sheet state value
     */
    protected String getSheetStateValue(Revision pRevision) {
        final AttributesContainer lRevAttributesContainer =
                pRevision.getRevisionAttrs();

        for (Attribute lAttribute : lRevAttributesContainer.getAttributes()) {
            if (lAttribute.getName().compareTo(SHEET_STATE_ATTRIBUTE_NAME) == 0) {
                final AttributeValue lAttributeValue =
                        lAttribute.getAttributeValues().iterator().next();

                return lAttributeValue.getStringValue();
            }
        }

        return null;
    }
}
