/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.fieldscontainer.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.authorization.service.TypeAccessControlData;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.exception.LockException;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fields.impl.CacheableInputData;
import org.topcased.gpm.business.fields.impl.CacheableInputDataType;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService;
import org.topcased.gpm.business.link.impl.CacheableLink;
import org.topcased.gpm.business.link.impl.CacheableLinkType;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.impl.CacheableProductType;
import org.topcased.gpm.business.revision.service.RevisionService;
import org.topcased.gpm.business.serialization.data.CategoryValue;
import org.topcased.gpm.business.serialization.data.ChoiceField;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.FieldsContainer;
import org.topcased.gpm.business.serialization.data.Lock;
import org.topcased.gpm.business.serialization.data.SimpleField;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.util.FieldsContainerUtils;
import org.topcased.gpm.domain.attributes.Attribute;
import org.topcased.gpm.domain.attributes.AttributeValue;
import org.topcased.gpm.domain.dynamic.container.link.DynamicLinkGeneratorFactory;
import org.topcased.gpm.domain.dynamic.container.product.DynamicProductGeneratorFactory;
import org.topcased.gpm.domain.dynamic.container.revision.DynamicRevisionGeneratorFactory;
import org.topcased.gpm.domain.dynamic.container.sheet.DynamicSheetGeneratorFactory;
import org.topcased.gpm.domain.fields.ValuesContainer;
import org.topcased.gpm.domain.link.LinkType;
import org.topcased.gpm.domain.product.ProductType;
import org.topcased.gpm.domain.sheet.SheetType;
import org.topcased.gpm.domain.util.IdentityVisitor;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * FieldsContainerServiceImpl
 * 
 * @author mkargbo
 */
public class FieldsContainerServiceImpl extends ServiceImplBase implements
        FieldsContainerService {

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService#getFieldsContainerInfo(java.lang.String,
     *      java.lang.Class, long)
     */
    @SuppressWarnings("unchecked")
    public <TYPE extends CacheableFieldsContainer> List<TYPE> getFieldsContainer(
            String pRoleToken, Class<? extends FieldsContainer> pClazz,
            long pTypeAccess) {

        Class<? extends org.topcased.gpm.domain.fields.FieldsContainer> lEntityClazz =
                null;
        Class<? extends org.topcased.gpm.domain.fields.FieldsContainer> lEntityImplClazz =
                null;
        Class<? extends CacheableFieldsContainer> lCachedClazz = null;
        if (pClazz == null) {
            lEntityClazz =
                    FieldsContainerUtils.getEntityClass(FieldsContainer.class);
            lEntityImplClazz =
                    FieldsContainerUtils.getEntityImplClass(FieldsContainer.class);
            lCachedClazz =
                    FieldsContainerUtils.getCachedClass(FieldsContainer.class);
        }
        else {
            lEntityClazz = FieldsContainerUtils.getEntityClass(pClazz);
            lEntityImplClazz = FieldsContainerUtils.getEntityImplClass(pClazz);
            lCachedClazz = FieldsContainerUtils.getCachedClass(pClazz);
        }
        List<String> lFieldsContainersIds = getFieldsContainerDao().getFieldsContainerId(lEntityClazz);
        List<TYPE> lResult = new ArrayList<TYPE>();

        String lProcessName = getAuthService().getProcessName(pRoleToken);

        boolean lNotConfidential = ((pTypeAccess & NOT_CONFIDENTIAL) != 0);
        boolean lCreate = ((pTypeAccess & CREATE) != 0);
        boolean lUptade = ((pTypeAccess & UPDATE) != 0);
        boolean lDelete = ((pTypeAccess & DELETE) != 0);
        for (String lFieldsContainerId : lFieldsContainersIds) {
            AccessControlContextData lAccessControlContext =
                    new AccessControlContextData(
                            CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                            CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                            CacheProperties.ACCESS_CONTROL_NOT_USED,
                            lFieldsContainerId,
                            CacheProperties.ACCESS_CONTROL_NOT_USED,
                            CacheProperties.DEFAULT_ACCESS_CONTROL_USED);
            TypeAccessControlData lTypeAccessControlData =
                    getAuthService().getTypeAccessControl(pRoleToken,
                            lAccessControlContext);
            boolean lTypeAccessFlag = true;

            //The flag is false if the given flag defines the type access AND this 
            //type access doesn't
            //correspond to the access control.
            if (lNotConfidential
                    && (lTypeAccessControlData.getConfidential().compareTo(
                            !lNotConfidential) != 0)) {
                lTypeAccessFlag = false;
            }

            if (lCreate
                    && (lTypeAccessControlData.getCreatable().compareTo(lCreate) != 0)) {
                lTypeAccessFlag = false;
            }

            if (lUptade
                    && (lTypeAccessControlData.getUpdatable().compareTo(lUptade) != 0)) {
                lTypeAccessFlag = false;
            }
            if (lDelete
                    && (lTypeAccessControlData.getDeletable().compareTo(lDelete) != 0)) {
                lTypeAccessFlag = false;
            }

            if (lTypeAccessFlag) {
                TYPE lType =
                        (TYPE) getCachedElement(lCachedClazz, lEntityImplClazz,
                                lFieldsContainerId, CACHE_IMMUTABLE_OBJECT);

                if (lProcessName.equals(lType.getBusinessProcessName())) {
                    lResult.add(lType);
                }
            }
        }
        return lResult;
    }

    /**
     * Create an empty CacheableValuesContainer, initialized with default values
     * 
     * @param pRoleToken
     *            The role token
     * @param pCacheableFieldsContainer
     *            The type
     * @param pProductName
     *            The name of the product
     * @param pEnvironmentNames
     *            The name of the environments
     * @return The new CacheableValuesContainer
     */
    public CacheableValuesContainer getValuesContainerModel(
            final String pRoleToken,
            final CacheableFieldsContainer pCacheableFieldsContainer,
            final String pProductName, final List<String> pEnvironmentNames) {
        final CacheableValuesContainer lValuesContainer;

        if (pCacheableFieldsContainer instanceof CacheableSheetType) {
            lValuesContainer = new CacheableSheet();
            // Set state Name
            ((CacheableSheet) lValuesContainer).setCurrentStateName(((CacheableSheetType) pCacheableFieldsContainer).getInitialStateName());
        }
        else if (pCacheableFieldsContainer instanceof CacheableProductType) {
            lValuesContainer = new CacheableProduct();
        }
        else if (pCacheableFieldsContainer instanceof CacheableLinkType) {
            lValuesContainer = new CacheableLink();
        }
        else if (pCacheableFieldsContainer instanceof CacheableInputDataType) {
            lValuesContainer = new CacheableInputData();
        }
        else {
            throw new GDMException("Invalid container type : "
                    + pCacheableFieldsContainer.getClass());
        }

        // Initialize type properties
        lValuesContainer.setTypeId(pCacheableFieldsContainer.getId());
        lValuesContainer.setTypeName(pCacheableFieldsContainer.getName());

        // Initialize values container properties
        lValuesContainer.setFunctionalReference(StringUtils.EMPTY);
        lValuesContainer.setVersion(0);

        // Initialize product properties
        lValuesContainer.setProductName(pProductName);
        lValuesContainer.setEnvironmentNames(pEnvironmentNames);

        // Initialize default field values
        for (Field lField : pCacheableFieldsContainer.getAllFields()) {
            // Get the default value
            String lFieldValue = null;

            if (lField instanceof SimpleField) {
                final SimpleField lSimpleField = (SimpleField) lField;
                lFieldValue = lSimpleField.getDefaultValue();
            }
            else if (lField instanceof ChoiceField) {
                final ChoiceField lChoiceField = (ChoiceField) lField;
                lFieldValue = lChoiceField.getDefaultValue();

                // Check that the default value is available for the selected environments
                if (StringUtils.isNotEmpty(lFieldValue)) {
                    // Get category values
                    final List<CategoryValue> lValues =
                            getEnvService().getCategoryValues(
                                    pRoleToken,
                                    pCacheableFieldsContainer.getBusinessProcessName(),
                                    pEnvironmentNames,
                                    Collections.singletonList(lChoiceField.getCategoryName())).get(
                                    lChoiceField.getCategoryName());

                    // If invalid value, doesn't use the default value
                    if (lValues == null) {
                        lFieldValue = null;
                    }
                    else {
                        boolean lValueExists = false;

                        // Check is the category value has been found
                        for (CategoryValue lValue : lValues) {
                            if (lValue.getValue().equals(lFieldValue)) {
                                lValueExists = true;
                                break;
                            }
                        }
                        if (!lValueExists) {
                            lFieldValue = null;
                        }
                    }
                }
            }

            if (StringUtils.isNotEmpty(lFieldValue)) {
                // Add field to the cacheableSheet
                final FieldValueData lFieldValueData =
                        new FieldValueData(lField.getLabelKey(), lFieldValue);

                if (StringUtils.isNotEmpty(lField.getMultipleField())) {
                    lValuesContainer.setValue(
                            pCacheableFieldsContainer.getFieldFromId(
                                    lField.getMultipleField()).getLabelKey(),
                            lFieldValueData);
                }
                else if (!lField.getMultiple()) {
                    lValuesContainer.setValue(lFieldValueData);
                }
            }
        }

        return lValuesContainer;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService#initDynamicMapping(java.lang.String)
     */
    public void initDynamicMapping(String pId) {
        final org.topcased.gpm.domain.fields.FieldsContainer lType =
                (org.topcased.gpm.domain.fields.FieldsContainer) getFieldsContainerDao().load(
                        pId);

        // Create the model associated for store values containers
        if (lType instanceof SheetType) {
            DynamicSheetGeneratorFactory.getInstance().initDynamicObjectGenerator(
                    lType.getId(), (SheetType) lType);
            if (isReviseable(lType)) {
                DynamicRevisionGeneratorFactory.getInstance().initDynamicObjectGenerator(
                        lType.getId(), lType);
            }
        }
        else if (lType instanceof LinkType) {
            DynamicLinkGeneratorFactory.getInstance().initDynamicObjectGenerator(
                    lType.getId(), (LinkType) lType);
        }
        else if (lType instanceof ProductType) {
            DynamicProductGeneratorFactory.getInstance().initDynamicObjectGenerator(
                    lType.getId(), (ProductType) lType);
        }
        else {
            throw new GDMException("Unknow fields container: "
                    + lType.getClass());
        }
    }

    private boolean isReviseable(
            org.topcased.gpm.domain.fields.FieldsContainer pFieldsContainer) {
        for (Attribute lAttribute : pFieldsContainer.getAttributes()) {
            if (lAttribute.getName().equals(
                    RevisionService.REVISION_ENABLED_ATTRIBUTE_NAME)) {
                final List<AttributeValue> lAttributeValues =
                        lAttribute.getAttributeValues();

                if (lAttributeValues == null || lAttributeValues.isEmpty()) {
                    return false;
                }
                else {
                    return Boolean.valueOf(lAttributeValues.get(0).getValue());
                }
            }
        }

        return false;
    }

    /**
     * Get informations of all fields container for the specified type.
     * <p>
     * Retrieve the fields containers without role token verification.
     * 
     * @param pClazz
     *            Type of fields container to retrieve, for all fields container
     * @param <TYPE>
     *            Fields container object's type
     * @return Fields container information.
     */
    @SuppressWarnings("unchecked")
    public <TYPE extends CacheableFieldsContainer> List<TYPE> getFieldsContainer(
            Class<? extends FieldsContainer> pClazz) {

        final Class<? extends org.topcased.gpm.domain.fields.FieldsContainer> lEntityClazz;
        final Class<? extends org.topcased.gpm.domain.fields.FieldsContainer> lEntityImplClazz;
        final Class<? extends CacheableFieldsContainer> lCachedClazz;
        if (pClazz == null) {
            lEntityClazz =
                    FieldsContainerUtils.getEntityClass(FieldsContainer.class);
            lEntityImplClazz =
                    FieldsContainerUtils.getEntityImplClass(FieldsContainer.class);
            lCachedClazz =
                    FieldsContainerUtils.getCachedClass(FieldsContainer.class);
        }
        else {
            lEntityClazz = FieldsContainerUtils.getEntityClass(pClazz);
            lEntityImplClazz = FieldsContainerUtils.getEntityImplClass(pClazz);
            lCachedClazz = FieldsContainerUtils.getCachedClass(pClazz);
        }
        List<String> lFieldsContainersIds =
                getFieldsContainerDao().getFieldsContainerId(lEntityClazz);
        List<TYPE> lResult = new ArrayList<TYPE>();

        for (String lFieldsContainerId : lFieldsContainersIds) {
            TYPE lType =
                    (TYPE) getCachedElement(lCachedClazz, lEntityImplClazz,
                            lFieldsContainerId, CACHE_IMMUTABLE_OBJECT);
            lResult.add(lType);
        }
        return lResult;
    }

    /**
     * Test the existence of a fields container from its name.
     * 
     * @param pProcessName
     *            Process name of the fields container
     * @param pTypeName
     *            Name of the fields container
     * @return True if the fields container exists (process name and name
     *         parameter not blank), false otherwise
     */
    public boolean isFieldsContainerExists(String pProcessName, String pTypeName) {
        if (StringUtils.isNotBlank(pProcessName)
                && StringUtils.isNotBlank(pTypeName)) {
            return getFieldsContainerDao().isFieldsContainerExists(
                    pProcessName, pTypeName);
        }
        else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService#getFieldsContainerId(java.lang.String,
     *      java.lang.String)
     */
    public String getFieldsContainerId(final String pRoleToken,
            final String pFieldsContainerName) throws InvalidTokenException {
        authorizationService.assertValidRoleToken(pRoleToken);

        String lProcessName =
                authorizationService.getProcessNameFromToken(pRoleToken);

        String lFieldsContainerId =
                getFieldsContainerDao().getFieldsContainerId(
                        pFieldsContainerName, lProcessName);
        return lFieldsContainerId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService#getValuesContainerId(java.lang.String,
     *      java.lang.String)
     */
    public List<String> getValuesContainerId(final String pRoleToken,
            final String pFieldsContainerId) throws InvalidTokenException {
        authorizationService.assertValidRoleToken(pRoleToken);

        List<String> lValuesContainerId =
                getValuesContainerDao().getAllId(pFieldsContainerId);
        return lValuesContainerId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService#getFieldsContainerIdByValuesContainer(java.lang.String,
     *      java.lang.String)
     */
    public String getFieldsContainerIdByValuesContainer(String pRoleToken,
            String pValuesContainerId) throws InvalidTokenException {
        return getValuesContainer(pRoleToken, pValuesContainerId,
                FIELD_NOT_CONFIDENTIAL, CacheProperties.IMMUTABLE).getTypeId();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService#getValuesContainer(java.lang.String,
     *      java.lang.String, org.topcased.gpm.util.bean.CacheProperties)
     */
    @Override
    public CacheableValuesContainer getValuesContainer(String pRoleToken,
            String pValuesContainerId, CacheProperties pCacheProperties) {
        return getValuesContainer(pRoleToken, pValuesContainerId,
                FIELD_NOT_CONFIDENTIAL, pCacheProperties);
    }

    /**
     * Get values container from its identifier.
     * <p>
     * The values container values respect the field access flag.
     * <p>
     * If a flag is not defined, the access property is not use.
     * <p>
     * Eg: Get fields that are not confidential and can be export.
     * 
     * <pre>
     * getValuesContainer(roleToken, valuesContainerIdentifier, NOT_CONFIDENTIAL
     *         | FIELD_EXPORT, cacheProperties)
     * </pre>
     * 
     * Note: If a field access is not defined, the access control property will
     * be ignore.<br />
     * e.g: If the field access 'EXPORT' is not set, it doesn't means that the
     * property is 'NOT_EXPORT'. It means that the property will be ignore.
     * 
     * @param pRoleToken
     *            Role token
     * @param pValuesContainerId
     *            Values container's identifier
     * @param pFieldAccess
     *            Field access to use
     * @param pCacheProperties
     *            Cache properties
     * @return Values container (fields have been checked)
     */
    public CacheableValuesContainer getValuesContainer(String pRoleToken,
            String pValuesContainerId, final long pFieldAccess,
            CacheProperties pCacheProperties) {

        assertValuesContainerIsReadable(pRoleToken, pValuesContainerId);

        final CacheableValuesContainer lValuesContainer =
                getCachedValuesContainer(pValuesContainerId,
                        pCacheProperties.getCacheFlags());

        if (lValuesContainer == null) {
            return null;
        }

        CacheableFieldsContainer lFieldsContainer =
                getCachedFieldsContainer(lValuesContainer.getTypeId(),
                        CACHE_IMMUTABLE_OBJECT);

        final AccessControlContextData lAccessControl;

        // If specified, use specific access control context
        if (pCacheProperties.getSpecificAccessControl() == null) {
            lAccessControl =
                    new AccessControlContextData(
                            CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                            CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                            CacheProperties.ACCESS_CONTROL_NOT_USED,
                            CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                            CacheProperties.ACCESS_CONTROL_NOT_USED,
                            CacheProperties.DEFAULT_ACCESS_CONTROL_USED);
        }
        else {
            lAccessControl = pCacheProperties.getSpecificAccessControl();
        }

        return authorizationService.getCheckedValuesContainer(pRoleToken,
                lAccessControl, lFieldsContainer, lValuesContainer,
                pFieldAccess);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService#isValuesContainerExists(java.lang.String)
     */
    public boolean isValuesContainerExists(final String pValuesContainerId) {
        if (StringUtils.isNotBlank(pValuesContainerId)) {
            return getValuesContainerDao().exist(pValuesContainerId);
        }
        else {
            return false;
        }
    }

    /**
     * Verify if the sheet is readable by the current user
     * 
     * @param pRoleToken
     *            The role token
     * @param pValuesContainerId
     *            The sheet id
     * @return Boolean indicating if the sheet is readable.
     * @throws AuthorizationException
     *             illegal access to the sheet
     */
    public boolean isValuesContainerReadable(String pRoleToken,
            String pValuesContainerId) {
        // The admin can read all sheets
        if (getAuthService().hasAdminAccess(pRoleToken)) {
            return true;
        }
        else {
            // Get current user login
            final String lCurrentUserLogin =
                    getAuthService().getLoginFromToken(pRoleToken);
            // Get container lock on this sheet
            final Lock lContainerLock =
                    atomicActionsManager.getLock(pValuesContainerId);

            // If there is no lock, or the lock belongs to the current user, sheet
            // is readable.
            if (lContainerLock == null || lContainerLock.getOwner() == null
                    || lContainerLock.getOwner().equals(lCurrentUserLogin)) {
                return true;
            }
            else {
                // If the lock is a READ_WRITE_LOCK the values container is *not* readable.
                switch (lContainerLock.getType()) {
                    case READ_WRITE:
                        return false;
                    default:
                        return true;
                }
            }
        }
    }

    /**
     * Throws a LockException if the values container is locked
     * 
     * @param pRoleToken
     *            Role session token
     * @param pValuesContainerId
     *            Sheet
     * @throws LockException
     *             When the sheet is locked and cannot be read.
     * @param pRoleToken
     * @param pValuesContainerId
     * @throws LockException
     */
    public void assertValuesContainerIsReadable(String pRoleToken,
            String pValuesContainerId) throws LockException {
        if (!isValuesContainerReadable(pRoleToken, pValuesContainerId)) {
            ValuesContainer lValuesContainer =
                    getValuesContainer(pValuesContainerId);

            String lMsg =
                    "Values container "
                            + lValuesContainer.getFunctionalReference()
                            + " is currently locked ";
            throw new LockException(lMsg, lValuesContainer.getId());
        }
    }

    /**
     * Verify if the values container is writable by the current user
     * 
     * @param pRoleToken
     *            The role token
     * @param pValuesContainerId
     *            The values container id
     * @return Boolean indicating if the values container is writable.
     */
    public boolean isValuesContainerWritable(String pRoleToken,
            String pValuesContainerId) {
        boolean lIsCurrentLoginAdmin =
                getAuthService().hasAdminAccess(pRoleToken);

        // The admin can write all sheets
        if (lIsCurrentLoginAdmin) {
            return true;
        }

        // Get current user login
        String lCurrentUserLogin =
                authorizationService.getLoginFromToken(pRoleToken);

        // Get container lock on this sheet
        org.topcased.gpm.domain.fields.Lock lContainerLock =
                getLockDao().getLock(pValuesContainerId);

        // If there is no lock : the sheet is writable.
        if (lContainerLock == null || lContainerLock.getOwner() == null
                || lContainerLock.getOwner().equals(lCurrentUserLogin)) {
            return true;
        }

        // In all other cases (a lock exists, and not owned by the current
        // user), the sheet is
        // not writable.
        return false;
    }

    /**
     * Throws a LockException if the values container cannot be written (because
     * it is locked).
     * 
     * @param pRoleToken
     *            Role session token
     * @param pValuesContainerId
     *            Values container identifier
     * @throws LockException
     *             the lock exception
     */
    public void assertValuesContainerIsWritable(String pRoleToken,
            String pValuesContainerId) throws LockException {
        if (!isValuesContainerWritable(pRoleToken, pValuesContainerId)) {
            ValuesContainer lValuesContainer =
                    getValuesContainer(pValuesContainerId);

            String lMsg =
                    "Values container "
                            + lValuesContainer.getFunctionalReference()
                            + " is currently locked ";
            throw new LockException(lMsg, lValuesContainer.getId());
        }
    }

    /**
     * Gets a values container from its identifier, with possible eager loading.
     * 
     * @param pClazz
     *            Actual class of the container
     * @param pId
     *            The identifier of the object.
     * @param <C>
     *            ValuesContainer
     * @return The values container (typed according to clazz parameter)
     * @throws IllegalArgumentException
     *             The given container identifier is blank
     * @throws InvalidIdentifierException
     *             The given identifier does not exist in the database.
     */
    @SuppressWarnings("unchecked")
    public final <C extends ValuesContainer> C getValuesContainer(
            Class<C> pClazz, String pId) throws IllegalArgumentException,
        InvalidIdentifierException {
        if (StringUtils.isBlank(pId)) {
            throw new IllegalArgumentException("Container id is blank");
        }

        ValuesContainer lContainer =
                (ValuesContainer) getValuesContainerDao().load(pId);

        lContainer = IdentityVisitor.getIdentity(lContainer);

        if (null == lContainer
                || !pClazz.isAssignableFrom(lContainer.getClass())) {
            throw new InvalidIdentifierException(pId,
                    "Invalid container id {0}");
        }
        return (C) lContainer;
    }

    /**
     * Gets a values container from its identifier, with possible eager loading.
     * 
     * @param pClazz
     *            Actual class of the container
     * @param pId
     *            The identifier of the object.
     * @param pEager
     *            Eager loading *
     * @param <C>
     *            ValuesContainer
     * @return The values container (typed according to clazz parameter)
     * @throws IllegalArgumentException
     *             The sheet hasn't been found.
     */
    @SuppressWarnings("unchecked")
    public final <C extends ValuesContainer> C getValuesContainer(
            Class<C> pClazz, String pId, boolean pEager)
        throws IllegalArgumentException {
        ValuesContainer lContainer = getValuesContainerDao().load(pId, pEager);

        if (null == lContainer) {
            throw new IllegalArgumentException("Values container id " + pId
                    + "invalid.");
        }
        return (C) lContainer;
    }
}
