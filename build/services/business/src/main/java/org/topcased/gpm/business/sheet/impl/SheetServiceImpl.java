/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet.impl;

import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.authorization.impl.AbstractContext;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.authorization.service.TypeAccessControlData;
import org.topcased.gpm.business.cache.CacheableFactory;
import org.topcased.gpm.business.dynamic.DynamicValuesContainerAccessFactory;
import org.topcased.gpm.business.events.LogoutEvent;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.exception.InvalidTokenException;
import org.topcased.gpm.business.exception.LockException;
import org.topcased.gpm.business.exception.MandatoryValuesException;
import org.topcased.gpm.business.exception.StaleSheetDataException;
import org.topcased.gpm.business.exception.UndeletableElementException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ContextBase;
import org.topcased.gpm.business.extensions.service.ContextValueFactory;
import org.topcased.gpm.business.extensions.service.ExtensionPointNames;
import org.topcased.gpm.business.extensions.service.ExtensionPointParameters;
import org.topcased.gpm.business.extensions.service.ExtensionsService;
import org.topcased.gpm.business.fields.LineFieldData;
import org.topcased.gpm.business.fields.MultipleLineFieldData;
import org.topcased.gpm.business.fields.service.FieldsService;
import org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService;
import org.topcased.gpm.business.lifecycle.service.LifeCycleService;
import org.topcased.gpm.business.lifecycle.service.ProcessInformation;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.serialization.data.AttachedFieldValueData;
import org.topcased.gpm.business.serialization.data.Lock;
import org.topcased.gpm.business.serialization.data.Lock.LockScopeEnumeration;
import org.topcased.gpm.business.serialization.data.TransitionHistoryData;
import org.topcased.gpm.business.sheet.export.service.SheetExportFormat;
import org.topcased.gpm.business.sheet.service.SheetData;
import org.topcased.gpm.business.sheet.service.SheetHistoryData;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.business.util.AttachmentInError;
import org.topcased.gpm.business.util.AttachmentStatus;
import org.topcased.gpm.business.util.AttachmentUtils;
import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.business.util.log.GPMActionLogConstants;
import org.topcased.gpm.business.util.log.GPMLogger;
import org.topcased.gpm.business.values.ValuesAccessProperties;
import org.topcased.gpm.business.values.sheet.impl.cacheable.CacheableSheetAccess;
import org.topcased.gpm.common.valuesContainer.LockType;
import org.topcased.gpm.domain.businessProcess.BusinessProcess;
import org.topcased.gpm.domain.dictionary.CategoryValueDao;
import org.topcased.gpm.domain.dictionary.Environment;
import org.topcased.gpm.domain.extensions.CommandDao;
import org.topcased.gpm.domain.extensions.ExtensionPoint;
import org.topcased.gpm.domain.extensions.ExtensionPointDao;
import org.topcased.gpm.domain.extensions.ExtensionsContainerDao;
import org.topcased.gpm.domain.facilities.DisplayGroupDao;
import org.topcased.gpm.domain.facilities.FieldGroupDao;
import org.topcased.gpm.domain.facilities.LinkSheetSummaryGroupDao;
import org.topcased.gpm.domain.fields.AttachedFieldContentValue;
import org.topcased.gpm.domain.fields.AttachedFieldDao;
import org.topcased.gpm.domain.fields.AttachedFieldValue;
import org.topcased.gpm.domain.fields.AttachedFieldValueDao;
import org.topcased.gpm.domain.fields.ChoiceFieldDao;
import org.topcased.gpm.domain.fields.Field;
import org.topcased.gpm.domain.fields.FieldDao;
import org.topcased.gpm.domain.fields.FieldsContainer;
import org.topcased.gpm.domain.fields.MultipleField;
import org.topcased.gpm.domain.fields.MultipleFieldDao;
import org.topcased.gpm.domain.fields.SimpleFieldDao;
import org.topcased.gpm.domain.fields.ValuesContainer;
import org.topcased.gpm.domain.link.LinkDao;
import org.topcased.gpm.domain.link.LinkNavigation;
import org.topcased.gpm.domain.link.LinkType;
import org.topcased.gpm.domain.process.Node;
import org.topcased.gpm.domain.process.ProcessDefinition;
import org.topcased.gpm.domain.process.ProcessDefinitionDao;
import org.topcased.gpm.domain.product.Product;
import org.topcased.gpm.domain.sheet.Sheet;
import org.topcased.gpm.domain.sheet.SheetHistory;
import org.topcased.gpm.domain.sheet.SheetType;
import org.topcased.gpm.util.bean.CacheProperties;
import org.topcased.gpm.util.bean.LockProperties;

/**
 * Sheet service implementation.
 * 
 * @author llatil
 */
public class SheetServiceImpl extends ServiceImplBase implements SheetService,
        ApplicationListener {
		
	// This map holds the list of attached files in error.
    // Sheet Id -> Error List
    private Map<String, List<AttachmentInError>> attachedFilesInError =
            new HashMap<String, List<AttachmentInError>>();

    private final CacheableSheetTypeFactory sheetTypeFactory =
            new CacheableSheetTypeFactory();

    private final CacheableSheetFactory sheetFactory =
            new CacheableSheetFactory();

    /** The separator between multiple field and simple field label key. */
    private final static String MULTIPLE_SEPARATOR = "::";

    private double maxAttachedFileSize;

    private final static int BYTETOMEGARATIO = 1048576;
    
    /** GPM Logger */
    private GPMLogger gpmLogger = GPMLogger.getLogger(SheetServiceImpl.class);
    
    /**
     * Default constructor.
     * <p>
     * This constructor should not be invoked directly. Use the service locator
     * or Spring injectors to get a reference to the sheet service.
     */
    public SheetServiceImpl() {
        registerFactories(sheetTypeFactory, sheetFactory);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getSheetTypes(java.lang.String,
     *      java.lang.String, org.topcased.gpm.util.bean.CacheProperties)
     */
    @Override
    public List<CacheableSheetType> getSheetTypes(String pRoleToken,
            String pProcessName, CacheProperties pCacheProperties) {
        List<String> lSheetTypeIds =
                getSheetTypeDao().getSheetTypesId(pProcessName);

        List<CacheableSheetType> lCacheableSheetTypes =
                new ArrayList<CacheableSheetType>();

        for (String lSheetTypeId : lSheetTypeIds) {
            lCacheableSheetTypes.add(getCacheableSheetType(pRoleToken,
                    lSheetTypeId, pCacheProperties));
        }

        return lCacheableSheetTypes;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getSerializableSheetTypes(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public List<org.topcased.gpm.business.serialization.data.SheetType> getSerializableSheetTypes(
            final String pRoleToken, final String pProcessName) {

        List<SheetType> lTypes =
                getInternalSheetTypes(pRoleToken, pProcessName);
        List<org.topcased.gpm.business.serialization.data.SheetType> lRes =
                new ArrayList<org.topcased.gpm.business.serialization.data.SheetType>(
                        lTypes.size());
        for (SheetType lType : lTypes) {
            lRes.add(getSerializableSheetType(lType.getId()));
        }
        return lRes;
    }

    private List<SheetType> getInternalSheetTypes(final String pRoleToken,
            final String pProcessName) {

        // Note: the given roleToken is checked by the getSheetTypeAccessControl
        // method of the authorization service. No need to check it here.

        BusinessProcess lBusProcess = getBusinessProcess(pProcessName);
        List<SheetType> lTypes = getSheetTypeDao().getSheetTypes(lBusProcess);
        AuthorizationService lAuthService = getAuthService();
        List<SheetType> lRes = new ArrayList<SheetType>(lTypes);
        TypeAccessControlData lSheetAccessCtl;
        for (SheetType lType : lTypes) {
            // Check the role access on this sheet type.
            AccessControlContextData lAccessControlContextData =
                    new AccessControlContextData();
            String lProductName = lAuthService.getProductName(pRoleToken);
            lAccessControlContextData.setRoleName(lAuthService.getRoleNameFromToken(pRoleToken));
            lAccessControlContextData.setProductName(lProductName);
            lAccessControlContextData.setStateName(null);
            lAccessControlContextData.setContainerTypeId(lType.getId());
            lSheetAccessCtl =
                    lAuthService.getTypeAccessControl(pRoleToken,
                            lAccessControlContextData);
            if (lSheetAccessCtl.getConfidential()) {
                // If this sheet type is confidential, remove it.
                lRes.remove(lType);
            }
        }
        return lRes;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getCreatableSerializableSheetTypes(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public List<org.topcased.gpm.business.serialization.data.SheetType> getCreatableSerializableSheetTypes(
            final String pRoleToken, final String pProcessName) {
        List<CacheableSheetType> lList =
                fieldsContainerServiceImpl.<CacheableSheetType> getFieldsContainer(
                        pRoleToken,
                        org.topcased.gpm.business.serialization.data.SheetType.class,
                        FieldsContainerService.NOT_CONFIDENTIAL
                                | FieldsContainerService.CREATE);
        List<org.topcased.gpm.business.serialization.data.SheetType> lRes =
                new ArrayList<org.topcased.gpm.business.serialization.data.SheetType>(
                        lList.size());
        for (CacheableSheetType lType : lList) {
            lRes.add(getSerializableSheetType(lType.getId()));
        }
        return lRes;
    }

    private String getSheetTypeKeyBySheetKey(final String pSheetId) {
        return getValuesContainerDao().getTypeId(pSheetId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getSheetsByType(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public List<SheetSummaryData> getSheetsByType(final String pProcessName,
            final String pSheetTypeName) {
        SheetType lSheetType = getSheetType(pProcessName, pSheetTypeName);
        List<Sheet> lSheets = getSheetDao().getSheets(lSheetType);

        return sheetUtils.createSheetSummaryList(lSheets);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getSheetSummaryByKey(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public SheetSummaryData getSheetSummaryByKey(final String pRoleToken,
            final String pSheetId) throws AuthorizationException {
        Sheet lSheet = (Sheet) getSheetDao().load(pSheetId);

        // gets the access control for lSheet
        TypeAccessControlData lTypeAccessControlData;
        lTypeAccessControlData =
                getAuthService().getSheetAccessControl(pRoleToken,
                        lSheet.getId());
        if (lTypeAccessControlData != null) {
            // if the user hasn't the right to get this sheetType
            if (lTypeAccessControlData.getConfidential()) {
                throw new AuthorizationException("Illegal access to the sheet "
                        + lSheet.getId() + " : the access is confidential ");
            }
        }

        // Verify if the sheet is readable by pRoleToken
        // an exception is launched if the sheet is not readable.
        fieldsContainerServiceImpl.assertValuesContainerIsReadable(pRoleToken,
                pSheetId);

        return sheetUtils.createSheetSummary(lSheet);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getCacheableSheetModel(java.lang.String,
     *      org.topcased.gpm.business.sheet.impl.CacheableSheetType,
     *      java.lang.String)
     */
    @Override
    public CacheableSheet getCacheableSheetModel(String pRoleToken,
            CacheableSheetType pCacheableSheetType, String pProductName,
            Context pContext) {
        // Gets the access control for the sheet type
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        String lProductName = getAuthService().getProductName(pRoleToken);
        lAccessControlContextData.setRoleName(getAuthService().getRoleNameFromToken(
                pRoleToken));
        lAccessControlContextData.setProductName(lProductName);
        lAccessControlContextData.setStateName(pCacheableSheetType.getInitialStateName());
        lAccessControlContextData.setContainerTypeId(pCacheableSheetType.getId());
        TypeAccessControlData lTypeAccessControlData =
                getAuthService().getTypeAccessControl(pRoleToken,
                        lAccessControlContextData);

        if (lTypeAccessControlData != null) {
            // if the user hasn't the right to get this sheetType
            if (lTypeAccessControlData.getConfidential()) {
                throw new AuthorizationException(
                        "Illegal access to the sheet type '"
                                + pCacheableSheetType.getName()
                                + "' : the access is confidential ");
            }
        }

        // Get the business process.
        String lBusinessProcessName =
                pCacheableSheetType.getBusinessProcessName();

        // Get product.
        CacheableProduct lCacheableProduct =
                getProductService().getCacheableProductByName(pProductName,
                        lBusinessProcessName, CacheProperties.IMMUTABLE);
        if (null == lCacheableProduct) {
            throw new InvalidNameException(pProductName,
                    "Product name ''{0}'' is invalid.");
        }

        CacheableSheet lCacheableSheet =
                (CacheableSheet) fieldsContainerServiceImpl.getValuesContainerModel(
                        pRoleToken, pCacheableSheetType,
                        lCacheableProduct.getProductName(),
                        lCacheableProduct.getEnvironmentNames());
        // Extension point postGetSheetModel or postGetModel
        final ExtensionPoint lPostGetModel =
                getExecutableExtensionPoint(pCacheableSheetType.getId(),
                        ExtensionPointNames.POST_GET_MODEL, pContext);

        if (lPostGetModel != null) {
            final SheetDataFactory lSheetDataFactory =
                    new SheetDataFactory(pRoleToken, lCacheableSheet);
            final ContextBase lContext = new ContextBase(pContext);

            lContext.put(ExtensionPointParameters.SHEET, lCacheableSheet);

            lContext.put(ExtensionPointParameters.SHEET_TYPE,
                    pCacheableSheetType);

            lContext.addFactory(
                    ExtensionPointParameters.SHEET_DATA.getParameterName(),
                    lSheetDataFactory);

            getExtensionsService().execute(pRoleToken, lPostGetModel, lContext);

            // Check if the actual value of the SheetData has been created
            // If this is the case, that means the ext point command retrieved
            // its value and we assume it updated it.
            final SheetData lSheetData =
                    (SheetData) lContext.getValue(ExtensionPointParameters.SHEET_DATA.getParameterName());

            if (lSheetData != null) {
                // Recreate the CachedSheet from the SheetData (required as the
                // ext point commands may have changed the SheetData).
                lCacheableSheet =
                        dataTransformationServiceImpl.getCacheableSheetFromSheetData(
                                pRoleToken, lSheetData);
            }
        }

        return lCacheableSheet;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getCacheableSheet(java.lang.String,
     *      java.lang.String, org.topcased.gpm.util.bean.CacheProperties)
     */
    @Override
    public CacheableSheet getCacheableSheet(String pRoleToken, String pSheetId,
            CacheProperties pProperties) {
        return (CacheableSheet) fieldsContainerServiceImpl.getValuesContainer(
                pRoleToken, pSheetId,
                FieldsContainerService.FIELD_NOT_CONFIDENTIAL, pProperties);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getSerializableSheet(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public org.topcased.gpm.business.serialization.data.SheetData getSerializableSheet(
            String pRoleToken, String pSheetId) {
        org.topcased.gpm.business.serialization.data.SheetData lSheetData =
                new org.topcased.gpm.business.serialization.data.SheetData();
        getCacheableSheet(pRoleToken, pSheetId, CacheProperties.MUTABLE).marshal(
                lSheetData);
        return lSheetData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getSerializableSheetTypeByName(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public org.topcased.gpm.business.serialization.data.SheetType getSerializableSheetTypeByName(
            String pRoleToken, String pProcessName, String pSheetTypeName) {
        org.topcased.gpm.business.serialization.data.SheetType lSheetType =
                new org.topcased.gpm.business.serialization.data.SheetType();
        getCacheableSheetTypeByName(pRoleToken, pProcessName, pSheetTypeName,
                CacheProperties.IMMUTABLE).marshal(lSheetType);
        return lSheetType;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getSheetByKey(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public org.topcased.gpm.business.sheet.service.SheetData getSheetByKey(
            final String pRoleToken, final String pSheetId)
        throws AuthorizationException {
        // Get the sheet from cache.
        final CacheableSheet lSheet =
                getCacheableSheet(pSheetId, CacheProperties.IMMUTABLE);
        // Get the sheetType access control for this sheet
        final String lStateName = lSheet.getCurrentStateName();
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        String lProductName = getAuthService().getProductName(pRoleToken);
        lAccessControlContextData.setRoleName(getAuthService().getRoleNameFromToken(
                pRoleToken));
        lAccessControlContextData.setProductName(lProductName);
        lAccessControlContextData.setStateName(lStateName);
        lAccessControlContextData.setContainerTypeId(lSheet.getTypeId());
        final TypeAccessControlData lTypeAccessControlData =
                getAuthService().getTypeAccessControl(pRoleToken,
                        lAccessControlContextData);

        // Check if user has the right to get this sheetType
        if (lTypeAccessControlData.getConfidential()) {
            throw new AuthorizationException("Illegal access to the sheet "
                    + lSheet.getId() + " : the access is confidential ");
        }

        // Verify if the sheet is readable by pRoleToken
        // an exception is launched if the sheet is not readable.
        fieldsContainerServiceImpl.assertValuesContainerIsReadable(pRoleToken,
                pSheetId);

        return dataTransformationServiceImpl.getSheetDataFromCacheableSheet(
                pRoleToken, lSheet);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getSerializableSheetByRef(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public org.topcased.gpm.business.serialization.data.SheetData getSerializableSheetByRef(
            final String pRoleToken, final String pProcessName,
            final String pProductName, final String pSheetRef) {
        authorizationService.assertValidRoleToken(pRoleToken);

        Product lProduct = getProduct(pProcessName, pProductName);
        Sheet lSheet = getSheetDao().getSheetByReference(lProduct, pSheetRef);

        if (null != lSheet) {
            // TODO the access is checked by the getSerializableSheet method ?
            return getSerializableSheet(pRoleToken, lSheet.getId());
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getSheetRefByKey(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public LineFieldData getSheetRefByKey(final String pRoleToken,
            final String pSheetId) throws AuthorizationException {

        // Check access controls for all users != admin
        if (!authorizationService.hasGlobalAdminRole(pRoleToken)) {
            // gets the sheetType access control for lSheet
            TypeAccessControlData lTypeAccessControlData;
            lTypeAccessControlData =
                    getAuthService().getSheetAccessControl(pRoleToken, pSheetId);

            // Check if user has the right to get this sheetType
            if (lTypeAccessControlData != null) {
                if (lTypeAccessControlData.getConfidential()) {
                    throw new AuthorizationException(
                            "Illegal access to the sheet " + pSheetId
                                    + " : the access is confidential ");
                }
            }

            // Verify if the sheet is readable by pRoleToken
            // an exception is launched if the sheet is not readable.
            fieldsContainerServiceImpl.assertValuesContainerIsReadable(
                    pRoleToken, pSheetId);
        }

        CacheableSheet lSheet =
                getCacheableSheet(pSheetId, CacheProperties.IMMUTABLE);
        return getSheetRef(pRoleToken, lSheet);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getSheetRefStringByKey(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public String getSheetRefStringByKey(final String pRoleToken,
            final String pSheetId) {

        // Check access controls for all users != admin
        if (!authorizationService.hasGlobalAdminRole(pRoleToken)) {
            TypeAccessControlData lAccessCtl;
            lAccessCtl =
                    getAuthService().getSheetAccessControl(pRoleToken, pSheetId);
            if (lAccessCtl.getConfidential()) {
                throw new AuthorizationException("Sheet type is confidential");
            }
        }

        CacheableSheet lCachedSheet =
                getCachedElement(CacheableSheet.class, pSheetId,
                        CACHE_IMMUTABLE_OBJECT);
        if (null != lCachedSheet) {
            return lCachedSheet.getFunctionalReference();
        }

        Sheet lSheet = getSheet(pSheetId);
        return lSheet.getReference();
    }

    /**
     * Gets the sheet reference.
     * 
     * @param pRoleToken
     *            the role token
     * @param pSheet
     *            the sheet
     * @return the sheet ref
     */
    @SuppressWarnings("rawtypes")
	private LineFieldData getSheetRef(final String pRoleToken,
            final CacheableSheet pSheet) {
        Map lReferenceValues =
                (Map) pSheet.getValue(FieldsService.REFERENCE_FIELD_NAME);

        CacheableSheetType lSheetType =
                getCacheableSheetType(pSheet.getTypeId(),
                        CacheProperties.IMMUTABLE);
        return dataTransformationServiceImpl.getDataTransformationManager().createLineFieldData(
                pRoleToken, lSheetType, pSheet, null, lReferenceValues,
                lSheetType.getReferenceField(), null, null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#updateSheetRefByKey(java.lang.String,
     *      java.lang.String, org.topcased.gpm.business.fields.LineFieldData)
     */
    @Override
    public void updateSheetRefByKey(final String pRoleToken,
            final String pSheetId, final LineFieldData pSheetRef)
        throws AuthorizationException {
        if (StringUtils.isBlank(pRoleToken)) {
            throw new InvalidTokenException("The role token is blank.");
        }

        final Sheet lSheet = getSheet(pSheetId);
        // gets the sheetType access control for lSheet
        final TypeAccessControlData lTypeAccessControlData =
                getAuthService().getSheetAccessControl(pRoleToken,
                        lSheet.getId());

        // Stop the update if the user hasn't the right to get this
        // sheetType or to update it
        if (lTypeAccessControlData != null
                && (lTypeAccessControlData.getConfidential() || !lTypeAccessControlData.getUpdatable())) {
            throw new AuthorizationException("Illegal access to the sheet "
                    + lSheet.getId()
                    + " : the access is confidential or not updatable");
        }

        // Verify if the sheet is writable by pRoleToken
        // an exception is launched if the sheet is not writable.
        fieldsContainerServiceImpl.assertValuesContainerIsWritable(pRoleToken,
                pSheetId);

        final String lSheetTypeId = lSheet.getSheetTypeId();
        final CacheableSheetType lSheetType =
                getCacheableSheetType(lSheetTypeId);

        // Create a cacheable sheet with only its reference field
        final CacheableSheet lSheetWithNewRef = new CacheableSheet();

        // Compute a MultipleLineFieldData describing the field functional reference
        final MultipleLineFieldData lReferenceFieldData =
                new MultipleLineFieldData();
        lReferenceFieldData.setLabelKey(FieldsService.REFERENCE_FIELD_NAME);
        lReferenceFieldData.setLineFieldDatas(new LineFieldData[] { pSheetRef });
        // The functional reference field is multiple...
        lReferenceFieldData.setMultiField(true);
        // .. and mono valued
        lReferenceFieldData.setMultiLined(false);

        // Add the functional reference field on the CacheableSheet
        lSheetWithNewRef.addMultipleLineFieldDatas(new MultipleLineFieldData[] { lReferenceFieldData });

        // Update the domain object with the this cacheable object
        // will update only the reference field
        DynamicValuesContainerAccessFactory.getInstance().getAccessor(
                lSheetTypeId).updateDomainFromBusiness(lSheet,
                lSheetWithNewRef, null);

        // Update the cache of the reference
        lSheet.setReference(getReferenceAsString(lSheetType.getId(), lSheet));

        // Increment version number
        lSheet.setVersion(lSheet.getVersion() + 1);

        // Cache element has been updated
        removeElementFromCache(pSheetId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getAttachedFileContent(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public byte[] getAttachedFileContent(final String pRoleToken,
            final String pAttachedFieldValueId) {
        AttachedFieldValue lAttachedFieldValue =
                getAttachedFileValue(pRoleToken, pAttachedFieldValueId);

        if (lAttachedFieldValue.getAttachedFieldContent() != null) {
            byte[] lResult =
                    lAttachedFieldValue.getAttachedFieldContent().getContent();
            if (null != lResult) {
                return lResult;
            }
        }
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getAttachedFileValue(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public AttachedFieldValue getAttachedFileValue(String pRoleToken,
            String pAttachedFieldValueId) {
        AttachedFieldValue lAttachedFieldValue =
                (AttachedFieldValue) getAttachedFieldValueDao().load(
                        pAttachedFieldValueId);

        if (null == lAttachedFieldValue) {
            throw new GDMException("Invalid attached field value identifier");
        }

        return lAttachedFieldValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#setAttachedFileContent(java.lang.String,
     *      java.lang.String, byte[])
     */
    @Override
    public void setAttachedFileContent(final String pRoleToken,
            final String pAttachedFieldValueId, final byte[] pByteArray) {

        // Load the attached field value.
        AttachedFieldValue lAttachedFieldValue =
                (AttachedFieldValue) getAttachedFieldValueDao().load(
                        pAttachedFieldValueId);

        // Launch an exception if the attached field value is invalid.
        if (null == lAttachedFieldValue) {
            throw new GDMException("Invalid attached field value identifier");
        }

        // Set the attached files in attached field value.
        lAttachedFieldValue.getAttachedFieldContent().setContent(pByteArray);
        setAttachedFileContent(pRoleToken, null, pAttachedFieldValueId,
                pByteArray);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#setAttachedFileContent(java.lang.String,
     *      java.lang.String, byte[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setAttachedFileContent(final String pRoleToken,
            String pSheetId, final String pAttachedFieldValueId,
            final byte[] pByteArray) {

        if (pSheetId != null) {

        	CacheableSheet lCacheableSheet = getCacheableSheet(pRoleToken, pSheetId, CacheProperties.MUTABLE);

            AttachedFieldValueData lAttachedField = null;
            for (AttachedFieldValueData lField : (List<AttachedFieldValueData>) lCacheableSheet.getAllAttachedFileValues()) {
                if (lField.getId().equals(pAttachedFieldValueId)) {
                    lAttachedField = lField;
                }
            }

            if (lAttachedField != null && (pByteArray == null || pByteArray.length == 0)) {
                throw new GDMException(
                        "Import of attached file failed because the file '"
                                + lAttachedField.getFilename()
                                + "' is empty. If the file is not empty, "
                                + "please contact the support.");
            }

            List<AttachmentInError> lStatus = checkAttachedFileProperties(
                            lCacheableSheet,
                            pRoleToken,
                            (List<AttachedFieldValueData>) lCacheableSheet.getAllAttachedFileValues());

            if (!lStatus.isEmpty()) {
                throw lStatus.get(0).toException();
            }

            // Load the attached field value.
            AttachedFieldValue lAttachedFieldValue =
                    (AttachedFieldValue) getAttachedFieldValueDao().load(
                            pAttachedFieldValueId);

            // Launch an exception if the attached field value is invalid.
            if (null == lAttachedFieldValue) {
                throw new GDMException("Invalid attached field value identifier");
            }

            // Set the attached files in attached field value.
            AttachedFieldContentValue lAttachedFieldContentValue =
                    lAttachedFieldValue.getAttachedFieldContent();
            if (null == lAttachedFieldContentValue) {
                lAttachedFieldContentValue = AttachedFieldContentValue.newInstance();
            }
            // Set the attached files in attached field value.
            lAttachedFieldContentValue.setContent(pByteArray);
            lAttachedFieldValue.setAttachedFieldContent(lAttachedFieldContentValue);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#createSheet(java.lang.String,
     *      org.topcased.gpm.business.sheet.service.SheetData,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public String createSheet(final String pRoleToken, final SheetData pData,
            final Context pCtx) throws AuthorizationException {
        authorizationService.assertValidRoleToken(pRoleToken);

        // Ensure that data.sheetId == null
        if (pData.getId() != null) {
            throw new GDMException("The sheet id " + pData.getId()
                    + " already exists in DB and cannot be created");
        }

        CacheableSheet lCacheableSheet =
                dataTransformationServiceImpl.getCacheableSheetFromSheetData(
                        pRoleToken, pData);

        return createSheet(pRoleToken, lCacheableSheet, pCtx);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getCacheableSheetInitializationModel(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public CacheableSheet getCacheableSheetInitializationModel(
            final String pRoleToken, final String pSheetTypeId,
            final String pSourceSheetId) throws AuthorizationException {
        // Get the source sheet & the product
        final CacheableSheet lSourceSheet =
                getCacheableSheet(pSourceSheetId, CacheProperties.IMMUTABLE);
        // Gets the sheetType access control for lSourceSheet
        final TypeAccessControlData lAccessOnSourceSheet =
                getAuthService().getSheetAccessControl(pRoleToken,
                        lSourceSheet.getId());
        // Gets the sheetType access control for pSheetTypeId
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        String lProductName = getAuthService().getProductName(pRoleToken);
        lAccessControlContextData.setRoleName(getAuthService().getRoleNameFromToken(
                pRoleToken));
        lAccessControlContextData.setProductName(lProductName);
        lAccessControlContextData.setStateName(null);
        lAccessControlContextData.setContainerTypeId(pSheetTypeId);
        final TypeAccessControlData lAccessOnSheetType =
                getAuthService().getTypeAccessControl(pRoleToken,
                        lAccessControlContextData);

        // Stop the copy if the user hasn't the right to get lSourceSheet
        if (lAccessOnSourceSheet != null
                && lAccessOnSourceSheet.getConfidential()) {
            throw new AuthorizationException(
                    "Illegal access to the source sheet "
                            + lSourceSheet.getId()
                            + " : the access is confidential");
        }

        // Verify if the sheet is readable by pRoleToken
        // an exception is launched if the sheet is not readable.
        fieldsContainerServiceImpl.assertValuesContainerIsReadable(pRoleToken,
                pSourceSheetId);

        // Stop the copy if the user hasn't the right to get this sheetType
        if (lAccessOnSheetType != null && lAccessOnSheetType.getConfidential()) {
            throw new AuthorizationException(
                    "Illegal access to the sheet type " + pSheetTypeId
                            + " : the access is confidential");
        }

        // Get the sheet type for the sheet to create.
        final CacheableSheetType lSheetType =
                getCacheableSheetType(pSheetTypeId, CacheProperties.IMMUTABLE);

        // Compute context for skip extension points
        final Context lContext = Context.getEmptyContext();
        lContext.put(Context.GPM_SKIP_EXT_PTS, Boolean.TRUE);

        // Create a sheet model from the sheet type.
        final CacheableSheet lCacheableSheetModel =
                getCacheableSheetModel(pRoleToken, lSheetType,
                        lSourceSheet.getProductName(), lContext);

        // Extension point copyFromSheet or postGetInitializationModel
        final ExtensionPoint lPostGetInitializationModel =
                getExecutableExtensionPoint(lSheetType.getId(),
                        ExtensionPointNames.POST_GET_INITIALIZATION_MODEL, null);
        SheetData lNewSheetData = null;

        if (lPostGetInitializationModel != null) {
            final ContextBase lCtx = new ContextBase();
            final SheetDataFactory lNewSheetDataFactory =
                    new SheetDataFactory(pRoleToken, lCacheableSheetModel);

            lCtx.put(ExtensionPointParameters.SHEET, lCacheableSheetModel);
            lCtx.put(ExtensionPointParameters.SOURCE_SHEET, lSourceSheet);

            lCtx.addFactory(
                    ExtensionPointParameters.SHEET_DATA.getParameterName(),
                    lNewSheetDataFactory);

            getExtensionsService().execute(pRoleToken,
                    lPostGetInitializationModel, lCtx);

            lNewSheetData =
                    (SheetData) lCtx.getValue(ExtensionPointParameters.SHEET_DATA.getParameterName());
        }

        if (lNewSheetData == null) {
            return lCacheableSheetModel;
        }
        else {
            return dataTransformationServiceImpl.getCacheableSheetFromSheetData(
                    pRoleToken, lNewSheetData);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getCacheableSheetDuplicationModel(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public CacheableSheet getCacheableSheetDuplicationModel(
            final String pRoleToken, final String pSourceSheetId)
        throws AuthorizationException {
        // Get the source sheet & the product
        // gets the sheetType access control for pSourceSheetId
        final TypeAccessControlData lAccessOnSourceSheet =
                getAuthService().getSheetAccessControl(pRoleToken,
                        pSourceSheetId);

        // Stop the copy if the user hasn't the right to get this sheetType
        if (lAccessOnSourceSheet != null
                && lAccessOnSourceSheet.getConfidential()) {
            throw new AuthorizationException("Illegal access to the sheet "
                    + pSourceSheetId + " : the access is confidential");
        }

        // Verify if the sheet is readable by pRoleToken
        // an exception is launched if the sheet is not readable.
        fieldsContainerServiceImpl.assertValuesContainerIsReadable(pRoleToken,
                pSourceSheetId);

        final CacheableSheet lSourceSheet =
                getCacheableSheet(pSourceSheetId, CacheProperties.MUTABLE);
        final CacheableSheet lNewSheet =
                getCacheableSheet(pSourceSheetId, CacheProperties.MUTABLE);
        final CacheableSheetType lSheetType =
                getCacheableSheetType(lSourceSheet.getTypeId(),
                        CacheProperties.IMMUTABLE);

        // Re initialize the sheet state
        lNewSheet.setCurrentStateName(lSheetType.getInitialStateName());
        // Re initialize the id
        lNewSheet.setId(null);
        // Re initialize the functional reference
        lNewSheet.setFunctionalReference(null);
        lNewSheet.removeFieldValue(FieldsService.REFERENCE_FIELD_NAME);
        // Duplicate attached fields
        fieldsManager.duplicateAttachedFieldValues(pRoleToken, lNewSheet);

        // Extension point cloneFromSheet or postGetDuplicationModel
        final ExtensionPoint lPostGetDuplicationModel =
                getExecutableExtensionPoint(lSheetType.getId(),
                        ExtensionPointNames.POST_GET_DUPLICATION_MODEL, null);
        SheetData lNewSheetData = null;

        if (lPostGetDuplicationModel != null) {
            final ContextBase lCtx = new ContextBase();
            final SheetDataFactory lNewSheetDataFactory =
                    new SheetDataFactory(pRoleToken, lNewSheet);

            lCtx.put(ExtensionPointParameters.SHEET, lNewSheet);
            lCtx.put(ExtensionPointParameters.SOURCE_SHEET, lSourceSheet);

            lCtx.addFactory(
                    ExtensionPointParameters.SHEET_DATA.getParameterName(),
                    lNewSheetDataFactory);

            getExtensionsService().execute(pRoleToken,
                    lPostGetDuplicationModel, lCtx);

            lNewSheetData =
                    (SheetData) lCtx.getValue(ExtensionPointParameters.SHEET_DATA.getParameterName());
        }

        if (lNewSheetData == null) {
            return lNewSheet;
        }
        else {
            return dataTransformationServiceImpl.getCacheableSheetFromSheetData(
                    pRoleToken, lNewSheetData);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#updateSheet(java.lang.String,
     *      org.topcased.gpm.business.sheet.service.SheetData,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public void updateSheet(final String pRoleToken, final SheetData pData,
            final Context pCtx) throws AuthorizationException {
        if (StringUtils.isBlank(pRoleToken)) {
            throw new InvalidTokenException("The role token is blank.");
        }
        // Call same method with cacheable sheet instead.
        updateSheet(pRoleToken,
                dataTransformationServiceImpl.getCacheableSheetFromSheetData(
                        pRoleToken, pData), pCtx);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#updateSheet(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.serialization.data.SheetData,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public void updateSheet(final String pRoleToken, String pProcessName,
            org.topcased.gpm.business.serialization.data.SheetData pData,
            Context pCtx) throws AuthorizationException {
        CacheableSheetType lSheetType =
                getCacheableSheetTypeByName(pRoleToken, pProcessName,
                        pData.getType(), CacheProperties.IMMUTABLE);
        CacheableSheet lSheet = new CacheableSheet(pData, lSheetType);
        updateSheet(pRoleToken, lSheet, pCtx);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#createSheet(java.lang.String,
     *      org.topcased.gpm.business.sheet.impl.CacheableSheet,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @SuppressWarnings("unchecked")
    @Override
    public String createSheet(final String pRoleToken,
            CacheableSheet pCacheableSheetData, Context pCtx)
        throws AuthorizationException, MandatoryValuesException {

        checkAttachedFileProperties(
                pCacheableSheetData,
                pRoleToken,
                (List<AttachedFieldValueData>) pCacheableSheetData.getAllAttachedFileValues());
        // Gets the sheetType access control for this sheet
        CacheableSheetType lSheetType =
                getCacheableSheetType(pCacheableSheetData.getTypeId(),
                        new CacheProperties(false,
                                CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                                pCacheableSheetData.getCurrentStateName()));
        // If caller has 'admin' privilege, skip checking the actual type access
        // (as 'admin' is allowed to create any type).
        if (!authorizationService.hasGlobalAdminRole(pRoleToken)) {
            TypeAccessControlData lTypeAccessControlData;
            AccessControlContextData lAccessControlContextData =
                    new AccessControlContextData();
            String lProductName = getAuthService().getProductName(pRoleToken);
            lAccessControlContextData.setRoleName(getAuthService().getRoleNameFromToken(
                    pRoleToken));
            lAccessControlContextData.setProductName(lProductName);
            lAccessControlContextData.setStateName(null);
            lAccessControlContextData.setContainerTypeId(pCacheableSheetData.getTypeId());
            lTypeAccessControlData =
                    getAuthService().getTypeAccessControl(pRoleToken,
                            lAccessControlContextData);

            if (lTypeAccessControlData != null) {
                // if the user hasn't the right to get this sheet
                if (lTypeAccessControlData.getConfidential()
                        || !lTypeAccessControlData.getCreatable()) {
                    throw new AuthorizationException(
                            "Illegal access: cannot create a sheet type '"
                                    + lSheetType.getName() + "'");
                }
            }
        }

        CacheableSheet lLocalSheet = pCacheableSheetData;

        // Extension point preCreate
        final ExtensionPoint lPreCreate =
                getExecutableExtensionPoint(lSheetType.getId(),
                        ExtensionPointNames.PRE_CREATE, pCtx);

        if (lPreCreate != null) {
            final SheetDataFactory lSheetDataFactory =
                    new SheetDataFactory(pRoleToken, lLocalSheet);
            final ContextBase lCtx = new ContextBase(pCtx);

            lCtx.put(ExtensionPointParameters.SHEET, lLocalSheet);

            lCtx.put(ExtensionPointParameters.SHEET_TYPE, lSheetType);

            lCtx.addFactory(
                    ExtensionPointParameters.SHEET_DATA.getParameterName(),
                    lSheetDataFactory);
            // Execute the 'preCreate' extension.
            getExtensionsService().execute(pRoleToken, lPreCreate, lCtx);

            // Check if the actual value of the SheetData has been created
            // If this is the case, that means the ext point command retrieved
            // its value
            // and we assume it updated it.
            final SheetData lSheetData =
                    (SheetData) lCtx.getValue(ExtensionPointParameters.SHEET_DATA.getParameterName());

            if (lSheetData != null) {
                // Recreate the CachedSheet from the SheetData (required as the
                // ext
                // point commands may have changed the SheetData).
                lLocalSheet =
                        dataTransformationServiceImpl.getCacheableSheetFromSheetData(
                                pRoleToken, lSheetData);
                lLocalSheet.setAttributesMap(pCacheableSheetData.getAttributesMap());
                lLocalSheet.setCurrentStateName(pCacheableSheetData.getCurrentStateName());
            }
        }

        // Actual sheet creation in database
        String lSheetId =
                doCreateSheet(pRoleToken, lLocalSheet, lSheetType, pCtx);

        // Extension point postCreate
        final ExtensionPoint lPostCreate =
                getExecutableExtensionPoint(lSheetType.getId(),
                        ExtensionPointNames.POST_CREATE, pCtx);

        if (lPostCreate != null) {
            final SheetDataFactory lSheetDataFactory =
                    new SheetDataFactory(pRoleToken, lLocalSheet);
            final ContextBase lCtx = new ContextBase(pCtx);

            lCtx.put(ExtensionPointParameters.SHEET, lLocalSheet);

            lCtx.put(ExtensionPointParameters.SHEET_TYPE, lSheetType);

            lCtx.addFactory(
                    ExtensionPointParameters.SHEET_DATA.getParameterName(),
                    lSheetDataFactory);
            getExtensionsService().execute(pRoleToken, lPostCreate, lCtx);
        }

        
        String lUserToken = (String) pCtx.get(Context.ORIGINAL_USER_TOKEN);
        String lUserLogin = "";
        if (lUserToken != null) {
        	lUserLogin = getAuthService().getLogin(lUserToken);
        }
        
        // Log
        gpmLogger.mediumInfo(lUserLogin, GPMActionLogConstants.SHEET_CREATION, 
        		lLocalSheet.getTypeName(), lLocalSheet.getProductName(), 
        		lLocalSheet.getFunctionalReference());

        return lSheetId;
    }

    /**
     * Do the actual sheet creation in DB
     * 
     * @param pRoleToken
     *            Role session token
     * @param pCacheableSheetData
     *            Sheet data
     * @param pSheetTypeEntity
     *            Sheet type entity
     * @param pSheetType
     *            Sheet type (from cache)
     * @param pCtx
     *            Execution context
     * @return Sheet identifier
     */
    private String doCreateSheet(String pRoleToken,
            CacheableSheet pCacheableSheetData, CacheableSheetType pSheetType,
            Context pCtx) {

        // Name of LF state for the sheet (null to create the sheet in
        // the initial lifecycle state)
        String lState = null;
        String lInitialState = pSheetType.getInitialStateName();

        // Only 'admin' is allowed to create a sheet in a specific state
        // (different from initial state).
        if (StringUtils.isNotBlank(pCacheableSheetData.getCurrentStateName())) {
            String lCurrentStateName =
                    pCacheableSheetData.getCurrentStateName();
            if (!lCurrentStateName.equals(lInitialState)) {
                if (!authorizationService.hasGlobalAdminRole(pRoleToken)) {
                    TypeAccessControlData lTypeAccessControlData;
                    AccessControlContextData lAccessControlContextData =
                            new AccessControlContextData();
                    String lProductName =
                            authorizationService.getProductNameFromSessionToken(pRoleToken);

                    lAccessControlContextData.setRoleName(
                    authorizationService.getRoleNameFromToken(pRoleToken));
                    lAccessControlContextData.setProductName(lProductName);
                    lAccessControlContextData.setContainerTypeId(null);
                    lTypeAccessControlData =
                            authorizationService.getTypeAccessControl(
                                    pRoleToken, lAccessControlContextData);

                    if (lTypeAccessControlData != null) {
                        // if the user hasn't the right to get this sheet
                        if (lTypeAccessControlData.getConfidential()
                                || !lTypeAccessControlData.getCreatable()) {
                            throw new AuthorizationException(
                            "Illegal access: cannot change state");
                        }
                    }
                }
                //authorizationService.assertAdminRole(pRoleToken);
            }
            if (!pCacheableSheetData.getRules().isEmpty()) {
                lState = lInitialState;
            }
            else {
                lState = lCurrentStateName;
            }
        }
        // in the case de current state in null we create the sheet in intial state
        if (null == lState) {
            lState = lInitialState;
        }
        // Try to get the sheet entity from DB. If found in database, the sheet
        // content is
        // 'reseted' by this method.
        Sheet lSheetEntity =
                getAndReset(pCacheableSheetData.getId(), Sheet.class);
        if (null == lSheetEntity) {
            // Create the sheet entity (not found in database)
            lSheetEntity =
                    sheetDao.getNewSheet(pCacheableSheetData.getTypeId());
        }

        Product lProductEntity =
                getProduct(pSheetType.getBusinessProcessName(),
                        pCacheableSheetData.getProductName());

        lSheetEntity.setId(pCacheableSheetData.getId());
        lSheetEntity.setVersion(pCacheableSheetData.getVersion());

        for (Environment lEnv : lProductEntity.getEnvironments()) {
            lSheetEntity.addToEnvironmentList(lEnv);
        }

        SheetType lSheetTypeEntity = getSheetType(pSheetType.getId());

        lSheetEntity.setProduct(lProductEntity);
        lSheetEntity.setDefinition(lSheetTypeEntity);

        // Create the  proc instance
        String lProcessDefName =
                lSheetTypeEntity.getProcessDefinition().getName();
        String lProcessId =
                getLifeCycleService().createProcessInstance(pRoleToken,
                        pSheetType, lProcessDefName, lState);
        // Store the LF instance ID
        lSheetEntity.setCurrentNode(getProcessDefinitionDao().getNode(
                lProcessId, lState));
        
        /**
         * Store the Sheet creation Date this parameter was managed by JBPM,
         * since JBPM's tables were removed, the information should be stored
         * differently to avoid losses
         */
        lSheetEntity.setCreationDate(new Timestamp(System.currentTimeMillis()));

        getSheetDao().create(lSheetEntity);

        // Check mandatory fields
        fieldsManager.checkMandoryFields(pRoleToken, pSheetType,
                pCacheableSheetData);

        // Create the sheet field values
        DynamicValuesContainerAccessFactory.getInstance().getAccessor(
                pSheetType.getId()).updateDomainFromBusiness(lSheetEntity,
                pCacheableSheetData, pCtx);

        // Create the attributes of the sheet.
        getAttributesService().set(lSheetEntity.getId(),
                pCacheableSheetData.getAllAttributes());

        final String lStrRef =
                getReferenceAsString(pCacheableSheetData.getTypeId(),
                        lSheetEntity);

        lSheetEntity.setReference(lStrRef);
        pCacheableSheetData.setFunctionalReference(lStrRef);
        pCacheableSheetData.setId(lSheetEntity.getId());

        return lSheetEntity.getId();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#createSheet(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.serialization.data.SheetData,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public String createSheet(
            final String pRoleToken,
            final String pProcessName,
            final org.topcased.gpm.business.serialization.data.SheetData pSheetData,
            Context pCtx) {
        CacheableSheetType lSheetType =
                getCacheableSheetTypeByName(pRoleToken, pProcessName,
                        pSheetData.getType(), CacheProperties.IMMUTABLE);
        CacheableSheet lSheet = new CacheableSheet(pSheetData, lSheetType);

        return createSheet(pRoleToken, lSheet, pCtx);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getCacheableSheet(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.serialization.data.SheetData)
     */
    @Override
    public CacheableSheet getCacheableSheet(
            final String pRoleToken,
            final String pProcessName,
            final org.topcased.gpm.business.serialization.data.SheetData pSheetData) {
        CacheableSheetType lSheetType =
                getCacheableSheetTypeByName(pRoleToken, pProcessName,
                        pSheetData.getType(), CacheProperties.IMMUTABLE);
        return new CacheableSheet(pSheetData, lSheetType);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#updateSheet(java.lang.String,
     *      org.topcased.gpm.business.sheet.impl.CacheableSheet,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public void updateSheet(final String pRoleToken,
            CacheableSheet pCacheableSheetData, Context pCtx)
        throws AuthorizationException {
        updateSheet(pRoleToken, pCacheableSheetData, pCtx, false);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#updateSheet(java.lang.String,
     *      org.topcased.gpm.business.sheet.impl.CacheableSheet,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void updateSheet(final String pRoleToken,
            CacheableSheet pCacheableSheetData, Context pCtx,
            boolean pIgnoreVersion) throws AuthorizationException {
        checkAttachedFileProperties(
                pCacheableSheetData,
                pRoleToken,
                (List<AttachedFieldValueData>) pCacheableSheetData.getAllAttachedFileValues());
        // gets the sheetType access control for this sheet
        TypeAccessControlData lTypeAccessControlData =
                getAuthService().getSheetAccessControl(pRoleToken,
                        pCacheableSheetData.getId());
        // if the user hasn't the right to get this sheet
        if (lTypeAccessControlData.getConfidential()
                || !lTypeAccessControlData.getUpdatable()) {
            throw new AuthorizationException("Illegal access to the sheet "
                    + pCacheableSheetData.getId()
                    + " : the access is confidential or update is not allowed");
        }

        // Verify if the sheet is writable by pRoleToken
        // an exception is launched if the sheet is not writable.
        fieldsContainerServiceImpl.assertValuesContainerIsWritable(pRoleToken,
                pCacheableSheetData.getId());

        CacheableSheet lCachedSheet =
                getCacheableSheet(pCacheableSheetData.getId(),
                        CacheProperties.IMMUTABLE);

        // Check the version
        if (pCacheableSheetData.getVersion() != lCachedSheet.getVersion()) {
        	if (!pIgnoreVersion) {
        		if (getLock(pRoleToken, pCacheableSheetData.getId()) == null) {
        			throw new StaleSheetDataException(lCachedSheet.getVersion(),
        					pCacheableSheetData.getVersion());
        		}
            }
        }

        // Get the sheet entity
        Sheet lSheetEntity = getSheet(pCacheableSheetData.getId());

        CacheableSheetType lCacheableSheetType =
                getCacheableSheetType(lCachedSheet.getTypeId(),
                        new CacheProperties(false,
                                CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                                lCachedSheet.getCurrentStateName()));
        // Extension point preUpdate
        final ExtensionPoint lPreUpdate =
                getExecutableExtensionPoint(lCacheableSheetType.getId(),
                        ExtensionPointNames.PRE_UPDATE, pCtx);

        if (lPreUpdate != null) {
            final SheetDataFactory lSheetDataFactory =
                    new SheetDataFactory(pRoleToken, pCacheableSheetData);
            final ContextBase lCtx = new ContextBase(pCtx);

            lCtx.put(ExtensionPointParameters.SHEET, pCacheableSheetData);

            lCtx.put(ExtensionPointParameters.SHEET_TYPE, lCacheableSheetType);

            lCtx.addFactory(
                    ExtensionPointParameters.SHEET_DATA.getParameterName(),
                    lSheetDataFactory);

            // Execute the 'preUpdate' extension
            getExtensionsService().execute(pRoleToken, lPreUpdate, lCtx);

            // Check if the actual value of the SheetData has been created
            // If this is the case, that means the ext point command retrieved
            // its value
            // and we assume it updated it.
            final SheetData lSheetData =
                    (SheetData) lCtx.getValue(ExtensionPointParameters.SHEET_DATA.getParameterName());

            if (lSheetData != null) {
                // Recreate the CachedSheet from the SheetData (required as the
                // ext
                // point commands may have changed the SheetData).
                pCacheableSheetData =
                        dataTransformationServiceImpl.getCacheableSheetFromSheetData(
                                pRoleToken, lSheetData);
            }
        }
        // Check mandatory fields
        fieldsManager.checkMandoryFields(pRoleToken, lCacheableSheetType,
                pCacheableSheetData);

        // Update values
        DynamicValuesContainerAccessFactory.getInstance().getAccessor(
                lCacheableSheetType.getId()).updateDomainFromBusiness(
                lSheetEntity, pCacheableSheetData, pCtx);

        // Update the attributes of the sheet.
        getAttributesService().update(lSheetEntity.getId(),
                pCacheableSheetData.getAllAttributes());

        // Increment the version of the sheet.
        int lPrevVersion = lSheetEntity.getVersion();
        lSheetEntity.setVersion(lPrevVersion + 1);

        // Update the cache of the reference.
        lSheetEntity.setReference(getReferenceAsString(
                lCacheableSheetType.getId(), lSheetEntity));

        // Update the product if needed.
        if (!lCachedSheet.getProductName().equals(
                pCacheableSheetData.getProductName())) {
            lSheetEntity.setProduct(getProduct(
                    getBusinessProcessName(pRoleToken),
                    pCacheableSheetData.getProductName()));
        }

        removeElementFromCache(lSheetEntity.getId());

        // Extension point postUpdate
        final ExtensionPoint lPostUpdate =
                getExecutableExtensionPoint(lCacheableSheetType.getId(),
                        ExtensionPointNames.POST_UPDATE, pCtx);

        if (lPostUpdate != null) {
            final ContextBase lCtx = new ContextBase(pCtx);
            final SheetDataFactory lSheetDataFactory =
                    new SheetDataFactory(pRoleToken, pCacheableSheetData);

            lCtx.put(ExtensionPointParameters.SHEET, pCacheableSheetData);

            lCtx.put(ExtensionPointParameters.SHEET_TYPE, lCacheableSheetType);

            lCtx.addFactory(
                    ExtensionPointParameters.SHEET_DATA.getParameterName(),
                    lSheetDataFactory);

            // Execute the postUpdate extension
            getExtensionsService().execute(pRoleToken, lPostUpdate, lCtx);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#createOrUpdateSheet(java.lang.String,
     *      org.topcased.gpm.business.sheet.impl.CacheableSheet,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public String createOrUpdateSheet(final String pRoleToken,
            CacheableSheet pCacheableSheetData, Context pCtx) {
        // Test if the sheet already exist in the DB.
        if (StringUtils.isNotBlank(pCacheableSheetData.getId())
                && (sheetDao.load(pCacheableSheetData.getId()) != null)) {
            updateSheet(pRoleToken, pCacheableSheetData, pCtx);
            return pCacheableSheetData.getId();
        }
        // else
        return createSheet(pRoleToken, pCacheableSheetData, pCtx);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#updateSheet(java.lang.String,
     *      org.topcased.gpm.business.sheet.impl.CacheableSheet,
     *      java.lang.String, Context)
     */
    @Override
    public void updateSheet(final String pRoleToken,
            final CacheableSheet pSheetData, final String pTransitionName,
            Context pCtx) throws AuthorizationException {
        try {
            pCtx.put(ExtensionsService.TRANSITION_NAME, pTransitionName);
            // Get the current sheet data (before the update)
            CacheableSheet lCurrentCachedSheet =
                    getCacheableSheet(pSheetData.getId(),
                            CacheProperties.IMMUTABLE);

            updateSheet(pRoleToken, pSheetData, pCtx);

            if (StringUtils.isNotEmpty(pTransitionName)) {
                changeState(pRoleToken, pSheetData.getId(), pTransitionName,
                        lCurrentCachedSheet, pSheetData, pCtx);
            }
        }
        finally {
            removeElementFromCache(pSheetData.getId());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#deleteSheet(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public void deleteSheet(final String pRoleToken, final String pSheetId,
            final Context pContext) throws AuthorizationException {
        // gets the sheetType access control for this sheet
        TypeAccessControlData lTypeAccessControlData =
                getAuthService().getSheetAccessControl(pRoleToken, pSheetId);

        // if the user hasn't the right to get this sheet
        if (lTypeAccessControlData.getConfidential()
                || !lTypeAccessControlData.getDeletable()) {
            throw new AuthorizationException(
                    "Illegal access to the sheet "
                            + pSheetId
                            + " : the access is confidential or deletion is not allowed");
        }

        // Verify if the sheet is writable by pRoleToken
        // an exception is launched if the sheet is not writable.
        fieldsContainerServiceImpl.assertValuesContainerIsWritable(pRoleToken,
                pSheetId);

        String lSheetTypeId =
                getSheetSummaryByKey(pRoleToken, pSheetId).getSheetTypeId();
        SheetType lSheetType = getSheetType(lSheetTypeId);
        // Extension point preDelete
        final ExtensionPoint lPreDelete =
                getExecutableExtensionPoint(lSheetType.getId(),
                        ExtensionPointNames.PRE_DELETE, pContext);

        if (lPreDelete != null) {
            final Context lCtx = new ContextBase(pContext);

            lCtx.put(ExtensionPointParameters.SHEET_ID, pSheetId);

            getExtensionsService().execute(pRoleToken, lPreDelete, lCtx);
        }

        final CacheableSheet lCacheableSheet =
                getCacheableSheet(pSheetId, CacheProperties.MUTABLE);

        // Remove all links of the sheet.
        List<String> lLinksId = getLinkIds(pSheetId);
        for (String lLinkId : lLinksId) {
            getLinkService().deleteLink(pRoleToken, lLinkId, pContext);
        }

        // Remove all OverriddenRoles for the sheet
        authorizationService.deleteOverriddenRolesFromContainerId(pSheetId);

        // Remove the sheet.
        getSheetDao().remove(pSheetId);
        removeElementFromCache(pSheetId);

        // Extension point postDelete
        final ExtensionPoint lPostDelete =
                getExecutableExtensionPoint(lSheetType.getId(),
                        ExtensionPointNames.POST_DELETE, pContext);

        if (lPostDelete != null) {
            final Context lCtx = new ContextBase(pContext);

            lCtx.put(ExtensionPointParameters.SHEET_ID, pSheetId);

            lCtx.put(ExtensionPointParameters.SHEET, lCacheableSheet);

            getExtensionsService().execute(pRoleToken, lPostDelete, lCtx);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#duplicateSheet(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public String duplicateSheet(final String pRoleToken, final String pSheetId)
        throws AuthorizationException {

        // Get the type access control for this sheet
        TypeAccessControlData lTypeAccessControlData;
        lTypeAccessControlData =
                getAuthService().getSheetAccessControl(pRoleToken, pSheetId);
        if (lTypeAccessControlData != null) {
            // if the user hasn't the right to get this sheet
            if (lTypeAccessControlData.getConfidential()) {
                throw new AuthorizationException("Illegal access to the sheet "
                        + pSheetId + ": the access is confidential.");
            }
        }

        // Verify if the sheet is writable by pRoleToken
        // an exception is launched if the sheet is not writable.
        fieldsContainerServiceImpl.assertValuesContainerIsWritable(pRoleToken,
                pSheetId);

        org.topcased.gpm.business.sheet.service.SheetData lSheetData =
                getSheetByKey(pRoleToken, pSheetId);
        // We have to remove the id...
        lSheetData.setId(null);
        return createSheet(pRoleToken, lSheetData, null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getSheetProcessInformation(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public ProcessInformation getSheetProcessInformation(
            final String pRoleToken, final String pSheetId)
        throws AuthorizationException {

        // gets the sheetType access control for this sheet
        TypeAccessControlData lTypeAccessControlData;
        lTypeAccessControlData =
                getAuthService().getSheetAccessControl(pRoleToken, pSheetId);
        if (lTypeAccessControlData != null) {
            // if the user hasn't the right to get this sheet
            if (lTypeAccessControlData.getConfidential()) {
                throw new AuthorizationException("Illegal access to the sheet "
                        + pSheetId + ": confidential access.");
            }
        }

        // Verify if the sheet is readable by pRoleToken
        // an exception is launched if the sheet is not readable.
        fieldsContainerServiceImpl.assertValuesContainerIsReadable(pRoleToken,
                pSheetId);

        return getLifeCycleService().getProcessInstanceInformation(pRoleToken,
                pSheetId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#changeState(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public void changeState(final String pRoleToken, final String pSheetId,
            final String pTransition, Context pContext)
        throws AuthorizationException {
        try {
            getAuthService().validateRoleToken(pRoleToken);

            // Mutable is need, because the cacheable is put on the context of
            // extension points
            CacheableSheet lSheetData =
                    getCacheableSheet(pSheetId, CacheProperties.MUTABLE);

            // The current sheet content is used for both current and updated data
            // (as no actual update is performed by this method).
            changeState(pRoleToken, pSheetId, pTransition, lSheetData,
                    lSheetData, pContext);
        }
        finally {
            removeElementFromCache(pSheetId);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#changeState(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    public void changeState(final String pRoleToken, final String pSheetId,
            final String pTransition, Set<String> pCommandsToExclude,
            Context pContext) throws AuthorizationException {
        try {
            getAuthService().validateRoleToken(pRoleToken);

            // Mutable is need, because the cacheable is put on the context of
            // extension points
            CacheableSheet lSheetData =
                    getCacheableSheet(pSheetId, CacheProperties.MUTABLE);
            lSheetData.setExtentionPointsToExclude(pCommandsToExclude);
            // The current sheet content is used for both current and updated data
            // (as no actual update is performed by this method).
            changeState(pRoleToken, pSheetId, pTransition, lSheetData,
                    lSheetData, pContext);
        }
        finally {
            removeElementFromCache(pSheetId);
        }
    }

    /**
     * Signal a transition on a sheet. This method is the actual implementation
     * of sheet state transition. It expects both current sheet content, and
     * updated sheet content (in case of sheet update) as parameters (given by
     * the calling method).
     * 
     * @param pRoleToken
     *            User session token.
     * @param pSheetId
     *            Id of the sheet target.
     * @param pTransition
     *            name of the transition to perform.
     * @param pSheetData
     *            Current sheet content
     * @param pUpdatedSheetData
     *            Updated sheet content
     * @param pContext
     *            Context execution
     * @throws AuthorizationException
     *             Illegal access to the sheet
     */
    private void changeState(final String pRoleToken, final String pSheetId,
            final String pTransition, CacheableSheet pSheetData,
            CacheableSheet pUpdatedSheetData, Context pContext)
        throws AuthorizationException {
        // gets the sheetType access control for this sheet
        TypeAccessControlData lTypeAccessControlData =
                getAuthService().getSheetAccessControl(pRoleToken, pSheetId);

        // if the user hasn't the right to get this sheet
        if (lTypeAccessControlData.getConfidential()
                || !lTypeAccessControlData.getUpdatable()) {
            throw new AuthorizationException(
                    "Illegal access to the sheet "
                            + pSheetId
                            + " : the access is confidential or the sheet isn't updatable.");
        }

        // Verify if the sheet is writable by pRoleToken
        // an exception is launched if the sheet is not writable.
        fieldsContainerServiceImpl.assertValuesContainerIsWritable(pRoleToken,
                pSheetId);

        String lUserLogin = authorizationService.getLoginFromToken(pRoleToken);

        Sheet lSheet = getSheet(pSheetId);
        CacheableSheet lCachedSheet =
                getCacheableSheet(pSheetId, CacheProperties.MUTABLE);
        lCachedSheet.setExtentionPointsToExclude(pSheetData.getExtentionPointsToExclude());
        CacheableSheetType lSheetType =
                getCacheableSheetType(lCachedSheet.getTypeId(),
                        CacheProperties.IMMUTABLE);

        LifeCycleService lLifeCycleService = getLifeCycleService();
        String lOriginStateName =
                lLifeCycleService.getProcessStateName(pSheetId);

        Node lNode =
                getSheetTypeDao().getNode(lSheetType.getId(), lOriginStateName);

        // Extension point preChangeState
        final ExtensionPoint lPreChangeState =
                getExecutableExtensionPoint(lSheetType.getId(),
                        ExtensionPointNames.PRE_CHANGE_STATE, pContext);

        if (lPreChangeState != null) {
            final Context lCtx = Context.createContext(pContext);

            lCtx.put(ExtensionPointParameters.SHEET_ID, pSheetId);

            lCtx.put(ExtensionPointParameters.TRANSITION_NAME, pTransition);
            lCtx.put(ExtensionPointParameters.SHEET_DATA, pSheetData);
            lCtx.put(ExtensionPointParameters.STATE_NAME, lOriginStateName);

            getExtensionsService().execute(pRoleToken, lPreChangeState, lCtx);
        }

        // Extension point leaveState
        final ExtensionPoint lLeaveState =
                getExecutableExtensionPoint(lNode,
                        ExtensionPointNames.LEAVE_STATE, pContext);

        if (lLeaveState != null) {
            final Context lCtx = Context.createContext(pContext);

            lCtx.put(ExtensionPointParameters.SHEET_ID, pSheetId);

            lCtx.put(ExtensionPointParameters.TRANSITION_NAME, pTransition);
            lCtx.put(ExtensionPointParameters.SHEET_DATA, pSheetData);
            lCtx.put(ExtensionPointParameters.STATE_NAME, lOriginStateName);
            getExtensionsService().execute(pRoleToken, lLeaveState, lCtx);
        }

        // Increment sheet version number.
        int lVersion = lSheet.getVersion();
        lSheet.setVersion(lVersion + 1);

        lLifeCycleService.performTransition(pRoleToken, pSheetId, pTransition);

        String lDestinationStateName =
                lLifeCycleService.getProcessStateName(pSheetId);

        SheetHistory lSheetHistory = SheetHistory.newInstance();

        // Set the origin and destination state names
        lSheetHistory.setOriginState(lOriginStateName);
        lSheetHistory.setDestinationState(lDestinationStateName);

        // Set the user login who performed the transition.
        lSheetHistory.setLoginName(lUserLogin);

        // Set the current date in the history.
        lSheetHistory.setChangeDate(new Timestamp(System.currentTimeMillis()));

        // set the transition name
        lSheetHistory.setTransitionName(pTransition);

        // Add it in the history list.
        lSheet.addToSheetHistoryList(lSheetHistory);

        removeElementFromCache(lSheet.getId());

        // Check if a 'enterState' ext. point exists on the destination state.
        lNode =
                getSheetTypeDao().getNode(lSheetType.getId(),
                        lDestinationStateName);

        // Extension point enterState
        final ExtensionPoint lEnterState =
                getExecutableExtensionPoint(lNode,
                        ExtensionPointNames.ENTER_STATE, pContext);

        if (lEnterState != null) {
            final Context lCtx = Context.createContext(pContext);

            lCtx.put(ExtensionPointParameters.SHEET_ID, pSheetId);

            lCtx.put(ExtensionPointParameters.TRANSITION_NAME, pTransition);
            lCtx.put(ExtensionPointParameters.SHEET_DATA, pUpdatedSheetData);
            lCtx.put(ExtensionPointParameters.STATE_NAME, lDestinationStateName);

            getExtensionsService().execute(pRoleToken, lEnterState, lCtx);
        }

        // Extension point postChangeState
        final ExtensionPoint lPostChangeState =
                getExecutableExtensionPoint(lSheetType.getId(),
                        ExtensionPointNames.POST_CHANGE_STATE, pContext);

        if (lPostChangeState != null) {
            final Context lCtx = Context.createContext(pContext);

            lCtx.put(ExtensionPointParameters.SHEET_ID, pSheetId);

            lCtx.put(ExtensionPointParameters.TRANSITION_NAME, pTransition);
            lCtx.put(ExtensionPointParameters.SHEET_DATA, pUpdatedSheetData);
            lCtx.put(ExtensionPointParameters.STATE_NAME, lDestinationStateName);

            getExtensionsService().execute(pRoleToken, lPostChangeState, lCtx);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#
     *      addSheetHistory(java.lang.String, java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void addSheetHistory(final String pRoleToken, final String pSheetId,
            String pLoginName, String pOriginState, String pDestinationState,
            final String pTransitionNme) throws AuthorizationException {
        addSheetHistory(pRoleToken, pSheetId, pLoginName, pOriginState,
                pDestinationState, new Timestamp(System.currentTimeMillis()),
                pTransitionNme);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#addSheetHistory(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String, java.sql.Timestamp)
     */
    @Override
    public void addSheetHistory(final String pRoleToken, final String pSheetId,
            String pLoginName, String pOriginState, String pDestinationState,
            Timestamp pChangeDate, final String pTransitionName)
        throws AuthorizationException {
        // gets the sheetType access control for this sheet
        TypeAccessControlData lTypeAccessControlData;
        lTypeAccessControlData =
                getAuthService().getSheetAccessControl(pRoleToken, pSheetId);
        if (lTypeAccessControlData != null) {
            // if the user hasn't the right to get this sheet
            if (lTypeAccessControlData.getConfidential()) {
                throw new AuthorizationException("Illegal access to the sheet "
                        + pSheetId + " : the access is confidential.");
            }
        }
        Sheet lSheet = getSheet(pSheetId);

        SheetHistory lSheetHistory = SheetHistory.newInstance();
        lSheetHistory.setChangeDate(pChangeDate);
        lSheetHistory.setDestinationState(pDestinationState);
        lSheetHistory.setLoginName(pLoginName);
        lSheetHistory.setOriginState(pOriginState);
        lSheetHistory.setTransitionName(pTransitionName);
        lSheet.getSheetHistories().add(lSheetHistory);
        removeElementFromCache(lSheet.getId());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#
     *      getSheetHistory(java.lang.String, java.lang.String)
     */
    @Override
    public SheetHistoryData[] getSheetHistory(final String pRoleToken,
            final String pSheetId) throws AuthorizationException {
        // gets the sheetType access control for this sheet
        TypeAccessControlData lTypeAccessControlData;
        lTypeAccessControlData =
                getAuthService().getSheetAccessControl(pRoleToken, pSheetId);
        if (lTypeAccessControlData != null) {
            // if the user hasn't the right to get this sheet
            if (lTypeAccessControlData.getConfidential()) {
                throw new AuthorizationException("Illegal access to the sheet "
                        + pSheetId + " : the access is confidential.");
            }
        }

        SheetHistoryData[] lHistData;

        CacheableSheet lCachedSheet = getCached(pSheetId, CacheableSheet.class);
        if (null != lCachedSheet) {
            fieldsContainerServiceImpl.assertValuesContainerIsReadable(
                    pRoleToken, pSheetId);

            lHistData =
                    new SheetHistoryData[lCachedSheet.getTransitionsHistory().size()];

            int i = 0;
            for (TransitionHistoryData lSheetHistory : lCachedSheet.getTransitionsHistory()) {
                SheetHistoryData lSheetHistoryData = new SheetHistoryData();

                lSheetHistoryData.setChangeDate(lSheetHistory.getTransitionDate());
                lSheetHistoryData.setOriginState(lSheetHistory.getOriginState());
                lSheetHistoryData.setDestinationState(lSheetHistory.getDestinationState());

                lSheetHistoryData.setLoginName(lSheetHistory.getLogin());

                lHistData[i++] = lSheetHistoryData;
            }
        }
        else {
            // Verify if the sheet is readable by pRoleToken
            // an exception is launched if the sheet is not readable.
            fieldsContainerServiceImpl.assertValuesContainerIsReadable(
                    pRoleToken, pSheetId);

            Sheet lSheet = getSheet(pSheetId);
            lHistData = new SheetHistoryData[lSheet.getSheetHistories().size()];

            int i = 0;
            for (SheetHistory lSheetHistory : lSheet.getSheetHistories()) {
                SheetHistoryData lSheetHistoryData = new SheetHistoryData();

                lSheetHistoryData.setChangeDate(lSheetHistory.getChangeDate());
                lSheetHistoryData.setOriginState(lSheetHistory.getOriginState());
                lSheetHistoryData.setDestinationState(lSheetHistory.getDestinationState());

                lSheetHistoryData.setLoginName(lSheetHistory.getLoginName());

                lHistData[i++] = lSheetHistoryData;
            }
        }
        return lHistData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#setSheetHistory(java.lang.String,
     *      java.lang.String, java.util.List)
     */
    @Override
    public void setSheetHistory(final String pRoleToken, final String pSheetId,
            final List<? extends TransitionHistoryData> pTransitionData) {
        final SheetHistoryData[] lHistory;

        if (null == pTransitionData) {
            lHistory = new SheetHistoryData[0];
        }
        else {
            lHistory = SheetUtils.convertHistoryData(pTransitionData);
        }
        setSheetHistory(pRoleToken, pSheetId, lHistory);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#setSheetHistory(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.sheet.service.SheetHistoryData[])
     */
    @Override
    public void setSheetHistory(final String pRoleToken, final String pSheetId,
            final SheetHistoryData[] pHistory) throws AuthorizationException {
        String lSheetTypeId = getSheetTypeKeyBySheetKey(pSheetId);
        // Can set transition only if this user can perform the last transition
        // Skip verification for global admin
        if (!getAuthService().hasGlobalAdminRole(pRoleToken)
                && !getLifeCycleService().canPerformTransitionHistory(
                        pRoleToken, lSheetTypeId,
                        SheetUtils.convertHistoryData(pHistory))) {
            throw new AuthorizationException(
                    "No rights to set the transition history"
                            + "(You cannot perform the last transition.");
        }
        Sheet lSheet = getSheet(pSheetId);
        final Collection<SheetHistory> lTransitionsHistory =
                lSheet.getSheetHistories();
        // Remove all existing history entries
        lTransitionsHistory.clear();

        Collection<String> lStates =
                getLifeCycleService().getAllStateNames(lSheetTypeId);

        for (SheetHistoryData lTransitionData : pHistory) {
            SheetHistory lTransition = SheetHistory.newInstance();

            if (!lStates.contains(lTransitionData.getOriginState())) {
                throw new InvalidNameException(
                        lTransitionData.getOriginState(),
                        "State name ''{0}'' does not exist in the lifecycle");
            }
            if (!lStates.contains(lTransitionData.getDestinationState())) {
                throw new InvalidNameException(
                        lTransitionData.getDestinationState(),
                        "State name ''{0}'' does not exist in the lifecycle");
            }
            lTransition.setOriginState(lTransitionData.getOriginState());
            lTransition.setDestinationState(lTransitionData.getDestinationState());

            lTransition.setLoginName(lTransitionData.getLoginName());
            lTransition.setTransitionName(lTransitionData.getTransitionName());

            if (lTransitionData.getChangeDate() != null) {
                Timestamp lTransitionTime =
                        new Timestamp(lTransitionData.getChangeDate().getTime());
                lTransition.setChangeDate(lTransitionTime);
            }
            lTransitionsHistory.add(lTransition);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getPossibleLinkTypes(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public List<String> getPossibleLinkTypes(String pRoleToken,
            String pProcessName, String pProductName, String pSheetTypeId) {
        final Collection<String> lLinkTypeIds =
                getLinkTypeDao().getLinkTypesId(pSheetTypeId,
                        LinkNavigation.BIDIRECTIONAL_CREATION);
        final List<String> lResult = new ArrayList<String>(lLinkTypeIds.size());

        for (String lLinkTypeId : lLinkTypeIds) {
            if (!getAuthService().hasAdminAccess(pRoleToken)) {
                final TypeAccessControlData lTypeAccessControl =
                        getAuthService().getTypeAccessControl(pRoleToken,
                                pProcessName, pProductName, null, lLinkTypeId);

                if (lTypeAccessControl.getConfidential()
                        || !lTypeAccessControl.getCreatable()) {
                    continue;
                }
            }
            lResult.add(lLinkTypeId);
        }

        return lResult;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getPossibleLinkTypes(java.lang.String)
     */
    @Override
    public List<String> getPossibleLinkTypes(final String pRoleToken,
            final String pSheetTypeId) {
        return getLinkTypeDao().getLinkTypesId(pSheetTypeId,
                LinkNavigation.BIDIRECTIONAL_CREATION);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getLinkableSheetTypes(java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<String> getLinkableSheetTypes(final String pSheetTypeId) {
        SheetType lSheetType = getSheetType(pSheetTypeId);
        Collection<LinkType> lLinks =
                getLinkTypeDao().getLinkTypes(lSheetType.getId(),
                        lSheetType.getBusinessProcess(),
                        LinkNavigation.BIDIRECTIONAL_CREATION);

        List<String> lResult = new ArrayList<String>(lLinks.size());
        for (LinkType lLink : lLinks) {
            FieldsContainer lLinkedSheetType;
            if (lLink.getOriginType().equals(lSheetType)) {
                lLinkedSheetType = lLink.getDestType();
            }
            else {
                lLinkedSheetType = lLink.getOriginType();
            }

            if (!lResult.contains(lLinkedSheetType.getId())) {
                lResult.add(lLinkedSheetType.getId());
            }
        }
        return lResult;
    }

    /**
     * Get the list of identifiers of all links of the given sheet.
     * 
     * @param pSheetId
     *            Identifier of the sheet
     * @return Set of links identifiers
     */
    private List<String> getLinkIds(final String pSheetId) {
        return getLinkDao().getLinks(pSheetId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#exportSheets(java.lang.String,
     *      java.io.OutputStream, java.util.List,
     *      org.topcased.gpm.business.sheet.export.service.SheetExportFormat)
     */
    @Override
    public void exportSheets(String pRoleToken, OutputStream pOutputStream,
            List<String> pSheetIds, SheetExportFormat pExportFormat)
        throws AuthorizationException {

        if (pSheetIds.size() > getSheetExportService().getMaxExportableSheets()) {
            throw new AuthorizationException(
                    "search.request.execute.export.limit.error");
        }

        List<String> lSheetIds = new ArrayList<String>();
        // Get sheet data
        for (String lSheetId : pSheetIds) {
            // gets the sheetType access control for this sheet
            TypeAccessControlData lTypeAccessControlData;
            lTypeAccessControlData =
                    getAuthService().getSheetAccessControl(pRoleToken, lSheetId);
            // if the user has the right to get this sheet
            if ((lTypeAccessControlData != null && !lTypeAccessControlData.getConfidential())
                    || lTypeAccessControlData == null) {
                lSheetIds.add(lSheetId);
            }

            // Verify if the sheet is readable by pRoleToken
            // an exception is launched if the sheet is not readable.
            fieldsContainerServiceImpl.assertValuesContainerIsReadable(
                    pRoleToken, lSheetId);
        }
        // if the user has selected at least one sheet but there is no sheets
        // the user can export
        if (!pSheetIds.isEmpty() && lSheetIds.isEmpty()) {
            throw new AuthorizationException(
                    "Export denied for all the selected sheets.");
        }

        // Export it
        getSheetExportService().exportSheets(pRoleToken, pOutputStream,
                lSheetIds, pExportFormat);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#exportSheets(java.lang.String,
     *      java.io.OutputStream, java.lang.String[],
     *      org.topcased.gpm.business.sheet.export.service.SheetExportFormat,
     *      java.util.List)
     */
    @Override
    public void exportSheets(String pRoleToken, OutputStream pOutputStream,
            String[] pSheetIds, SheetExportFormat pExportFormat,
            List<String> pExportedFieldsLabel) throws AuthorizationException {

        if (pSheetIds.length > getSheetExportService().getMaxExportableSheets()) {
            throw new AuthorizationException(
                    "search.request.execute.export.limit.error");
        }

        List<String> lSheetIds = new ArrayList<String>();
        // Get sheet data
        for (String lId : pSheetIds) {
            // gets the sheetType access control for this sheet
            TypeAccessControlData lTypeAccessControlData;
            lTypeAccessControlData =
                    getAuthService().getSheetAccessControl(pRoleToken, lId);
            // if the user has the right to get this sheet
            if ((lTypeAccessControlData != null && !lTypeAccessControlData.getConfidential())
                    || lTypeAccessControlData == null) {
                lSheetIds.add(lId);
            }

            // Verify if the sheet is readable by pRoleToken
            // an exception is launched if the sheet is not readable.
            fieldsContainerServiceImpl.assertValuesContainerIsReadable(
                    pRoleToken, lId);
        }
        // if the user has selected at least one sheet but there is no sheets
        // the user can export
        if (pSheetIds.length > 0 && lSheetIds.isEmpty()) {
            throw new AuthorizationException(
                    "Export denied for all the selected sheets.");
        }

        // Export it
        getSheetExportService().exportSheets(pRoleToken, pOutputStream,
                lSheetIds, pExportFormat, pExportedFieldsLabel);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#exportSheetSummaries(java.lang.String,
     *      java.io.OutputStream, java.util.Map, java.util.Collection,
     *      org.topcased.gpm.business.sheet.export.service.SheetExportFormat,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public void exportSheetSummaries(String pRoleToken,
            OutputStream pOutputStream, Map<String, String> pLabels,
            Collection<SheetSummaryData> pSheetSummaries,
            SheetExportFormat pExportFormat, Context pContext)
        throws AuthorizationException {
        List<SheetSummaryData> lSheetSummaries =
                new ArrayList<SheetSummaryData>();
        for (SheetSummaryData lSheetSummary : pSheetSummaries) {
            // gets the sheetType access control for this sheet
            TypeAccessControlData lTypeAccessControlData;
            lTypeAccessControlData =
                    getAuthService().getSheetAccessControl(pRoleToken,
                            lSheetSummary.getId());
            // if the user has the right to get this sheet
            if ((lTypeAccessControlData != null && !lTypeAccessControlData.getConfidential())
                    || lTypeAccessControlData == null) {
                lSheetSummaries.add(lSheetSummary);
            }

            // Verify if the sheet is readable by pRoleToken
            // an exception is launched if the sheet is not readable.
            fieldsContainerServiceImpl.assertValuesContainerIsReadable(
                    pRoleToken, lSheetSummary.getId());
        }
        // if the user has selected at least one sheet but there is no sheets
        // the user can export
        if (lSheetSummaries.isEmpty()) {
            throw new AuthorizationException(
                    "Export denied for all the selected sheet summaries.");
        }
        // Export it
        getSheetExportService().exportSheetSummaries(pRoleToken, pOutputStream,
                pLabels, pSheetSummaries, pExportFormat, pContext);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#addExtension(java.lang.String,
     *      java.lang.String, java.util.List)
     */
    @Override
    public void addExtension(final String pToken,
            final String pFieldsContainerId, final String pExtensionPointName,
            final List<String> pCommandNames) {
        getExtensionsService().setExtension(pToken, pFieldsContainerId,
                pExtensionPointName, pCommandNames);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#
     *      removeExtension(java.lang.String, java.lang.String)
     */
    @Override
    public void removeExtension(final String pToken,
            final String pFieldsContainerId, final String pExtensionPointName) {
        getExtensionsService().removeExtension(pToken, pFieldsContainerId,
                pExtensionPointName);

        FieldsContainer lFieldsContainer =
                getFieldsContainer(pFieldsContainerId);
        ExtensionPoint lExtPoint =
                getExtensionsContainerDao().getExtensionPoint(
                        lFieldsContainer.getId(), pExtensionPointName);

        if (null != lExtPoint) {
            lFieldsContainer.removeFromExtensionPointList(lExtPoint);
            getExtensionPointDao().remove(lExtPoint);
        }
    }

    /**
     * Get a the reference of a container a string
     * 
     * @param pSheetTypeId
     *            The type of the container
     * @param pContainer
     *            The values container
     * @return The reference as a String
     */
    private String getReferenceAsString(final String pSheetTypeId,
            final ValuesContainer pContainer) {
        return DynamicValuesContainerAccessFactory.getInstance().getAccessor(
                pSheetTypeId).getFieldValueAsString(
                FieldsService.REFERENCE_FIELD_NAME, pContainer);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#createSheetType(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.sheet.impl.CacheableSheetType)
     */
    @Override
    public String createSheetType(final String pRoleToken,
            final String pBusinessProcName, final CacheableSheetType pSheetType) {
        final BusinessProcess lBusinessProc =
                getBusinessProcess(pBusinessProcName);

        if (pSheetType.getName() == null) {
            throw new InvalidNameException("Name of the sheet type is null");
        }

        SheetType lSheetType =
                getSheetTypeDao().getSheetType(lBusinessProc,
                        pSheetType.getName());

        if (lSheetType == null) {
            lSheetType = SheetType.newInstance();
            lSheetType.setName(pSheetType.getName());
            lSheetType.setBusinessProcess(lBusinessProc);
        }

        lSheetType.setDescription(pSheetType.getDescription());
        lSheetType.setSelectable(pSheetType.isSelectable());

        // Process creation
        final ApplicationContext lApplicationContext =
                ContextLocator.getContext();
        final ProcessDefinitionDao lProcDefDao =
                (ProcessDefinitionDao) lApplicationContext.getBean("processDefinitionDao");
        final ProcessDefinition lProcDef = ProcessDefinition.newInstance();

        lProcDef.setName(lSheetType.getName());
        lProcDefDao.create(lProcDef);
        lSheetType.setProcessDefinition(lProcDef);

        removeElementFromCache(pSheetType.getId());

        getSheetTypeDao().create(lSheetType);

        return lSheetType.getId();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService
     *      #setSheetTypeReferenceField(java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void setSheetTypeReferenceField(final String pRoleToken,
            final String pSheetTypeId, final String pRefFieldId) {
        SheetType lSheetType =
                fieldsManager.getFieldsContainer(SheetType.class, pSheetTypeId);

        Field lRefField = fieldsManager.getField(pRefFieldId);
        if (!(lRefField instanceof MultipleField)) {
            throw new InvalidIdentifierException(pRefFieldId);
        }
        lSheetType.setReferenceField((MultipleField) lRefField);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#deleteSheetType(java.lang.String,
     *      java.lang.String, java.lang.String, boolean)
     */
    @Override
    public void deleteSheetType(final String pRoleToken,
            final String pBusinessProcName, final String pSheetTypeName,
            boolean pDeleteSheets) {

        SheetType lSheetType = getSheetType(pBusinessProcName, pSheetTypeName);

        if (getValuesContainerDao().getCount(lSheetType.getId()) > 0
                && !pDeleteSheets) {
            throw new UndeletableElementException(pSheetTypeName);
        }

        // Delete the sheets of this type.
        getValuesContainerDao().deleteContainers(lSheetType.getId());

        // Delete the display groups referencing this type
        getFieldGroupDao().deleteGroups(lSheetType);

        getFieldsContainerDao().remove(lSheetType);
    }

    /**
     * Get CacheableSheetType without access control. It's not a method of the
     * interface, internal use only
     * 
     * @param pSheetTypeId
     *            The id of the sheet type
     * @param pProperties
     *            The cache properties
     * @return The CacheableSheetType
     */
    @Override
    public CacheableSheetType getCacheableSheetType(String pSheetTypeId,
            CacheProperties pProperties) {
        CacheableSheetType lCachedSheetType =
                getCachedElement(CacheableSheetType.class, pSheetTypeId,
                        pProperties.getCacheFlags());

        if (null == lCachedSheetType) {
            SheetType lSheetType = getSheetType(pSheetTypeId);
            
            lCachedSheetType =
                    (CacheableSheetType) sheetTypeFactory.createCacheableObject(lSheetType);
            addElementInCache(lCachedSheetType);
        }

        return lCachedSheetType;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getCacheableSheetType(java.lang.String)
     */
    @Override
    public CacheableSheetType getCacheableSheetType(String pSheetTypeId) {
        return getCacheableSheetType(pSheetTypeId, CacheProperties.IMMUTABLE);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getCacheableSheetType(java.lang.String,
     *      java.lang.String, org.topcased.gpm.util.bean.CacheProperties)
     */
    @Override
    public CacheableSheetType getCacheableSheetType(String pRoleToken,
            String pSheetTypeId, CacheProperties pProperties) {
        final CacheableSheetType lSheetType =
                getCacheableSheetType(pSheetTypeId, pProperties);

        if (pProperties.getSpecificAccessControl() == null) {
            return lSheetType;
        }
        // else
        pProperties.getSpecificAccessControl().setContainerTypeId(
                lSheetType.getId());
        return authorizationService.getCheckedFieldsContainer(pRoleToken,
                pProperties.getSpecificAccessControl(), lSheetType);
    }

    /**
     * Get type from its name.
     * 
     * @param pRoleToken
     *            Role token
     * @param pTypeName
     *            Name of the type to retrieve
     * @return Type.
     */
    // TODO: define in interface
    public CacheableSheetType getTypeByName(String pRoleToken, String pTypeName) {
        String lInstanceName =
                authorizationService.getProcessNameFromToken(pRoleToken);
        return getCacheableSheetTypeByName(pRoleToken, lInstanceName,
                pTypeName, CacheProperties.IMMUTABLE);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getCacheableSheetTypeByName(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      org.topcased.gpm.util.bean.CacheProperties)
     */
    @Override
    public CacheableSheetType getCacheableSheetTypeByName(String pRoleToken,
            String pProcessName, String pSheetTypeName,
            CacheProperties pProperties) {
        String lSheetTypeId = null;
        lSheetTypeId =
                getFieldsContainerDao().getFieldsContainerId(pSheetTypeName,
                        pProcessName);
        if (null == lSheetTypeId) {
            return null;
        }
        return getCacheableSheetType(pRoleToken, lSheetTypeId, pProperties);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getCacheableSheetTypeByName(java.lang.String,
     *      java.lang.String, org.topcased.gpm.util.bean.CacheProperties)
     */
    @Override
    public CacheableSheetType getCacheableSheetTypeByName(String pRoleToken,
            String pSheetTypeName, CacheProperties pCacheProperties) {
        String lInstanceName =
                authorizationService.getProcessNameFromToken(pRoleToken);
        return getCacheableSheetTypeByName(pRoleToken, lInstanceName,
                pSheetTypeName, pCacheProperties);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getSerializableSheetType(java.lang.String)
     */
    @Override
    public org.topcased.gpm.business.serialization.data.SheetType getSerializableSheetType(
            String pSheetTypeId) {
        org.topcased.gpm.business.serialization.data.SheetType lSheetType =
                new org.topcased.gpm.business.serialization.data.SheetType();
        getCacheableSheetType(pSheetTypeId, CacheProperties.IMMUTABLE).marshal(
                lSheetType);
        return lSheetType;
    }

    /**
     * Get CacheableSheet without access control. It's not a method of the
     * interface, internal use only
     * 
     * @param pSheetId
     *            The sheet id
     * @param pCacheProperties
     *            The cache properties
     * @return The cacheable sheet
     */
    @Override
    public CacheableSheet getCacheableSheet(String pSheetId,
            CacheProperties pCacheProperties) {
        CacheableSheet lCachedSheet =
                (CacheableSheet) getCachedValuesContainer(pSheetId,
                        pCacheProperties.getCacheFlags());

        if (null == lCachedSheet) {
            Sheet lSheet = getSheet(pSheetId);
            CacheableSheetType lSheetType =
                    getCacheableSheetType(lSheet.getDefinition().getId(),
                            CacheProperties.IMMUTABLE);
            String lStateName =
                    getLifeCycleService().getProcessStateName(pSheetId);

            lCachedSheet = new CacheableSheet(lSheet, lStateName, lSheetType);
            addElementInCache(lCachedSheet);
        }
        return lCachedSheet;
    }

    /** The field group dao. */
    private FieldGroupDao fieldGroupDao;

    /**
     * Gets the field group dao.
     * 
     * @return the field group dao
     */
    public FieldGroupDao getFieldGroupDao() {
        return fieldGroupDao;
    }

    /**
     * Sets the field group dao.
     * 
     * @param pFieldGroupDao
     *            the field group dao
     */
    public void setFieldGroupDao(final FieldGroupDao pFieldGroupDao) {
        fieldGroupDao = pFieldGroupDao;
    }

    /** The field dao. */
    private FieldDao fieldDao;

    /**
     * getFieldDao.
     * 
     * @return the FieldDao
     */
    public FieldDao getFieldDao() {
        return fieldDao;
    }

    /**
     * setFieldDao.
     * 
     * @param pFieldDao
     *            the FieldDao to set
     */
    public void setFieldDao(final FieldDao pFieldDao) {
        fieldDao = pFieldDao;
    }

    /** The simple field dao. */
    private SimpleFieldDao simpleFieldDao;

    /**
     * setSimpleFieldDao Sets the reference to <code>simpleField</code>'s DAO.
     * 
     * @param pSimpleFieldDao
     *            SimpleFieldDao
     */
    public void setSimpleFieldDao(final SimpleFieldDao pSimpleFieldDao) {
        simpleFieldDao = pSimpleFieldDao;
    }

    /**
     * getSimpleFieldDao Gets the reference to <code>simpleField</code>'s DAO.
     * 
     * @return SimpleFieldDao
     */
    protected SimpleFieldDao getSimpleFieldDao() {
        return simpleFieldDao;
    }

    /** The multiple field dao. */
    private MultipleFieldDao multipleFieldDao;

    /**
     * setMultipleFieldDao Sets the reference to <code>multipleField</code>'s
     * DAO.
     * 
     * @param pMultipleFieldDao
     *            MultipleFieldDao
     */
    public void setMultipleFieldDao(final MultipleFieldDao pMultipleFieldDao) {
        multipleFieldDao = pMultipleFieldDao;
    }

    /**
     * getMultipleFieldDao Gets the reference to <code>multipleField</code>'s
     * DAO.
     * 
     * @return MultipleFieldDao
     */
    protected MultipleFieldDao getMultipleFieldDao() {
        return multipleFieldDao;
    }

    /** The attached field dao. */
    private AttachedFieldDao attachedFieldDao;

    /**
     * Set the reference to <code>attachedField</code>'s DAO.
     * 
     * @param pAttachedFieldDao
     *            AttachedFieldDao
     */
    public void setAttachedFieldDao(final AttachedFieldDao pAttachedFieldDao) {
        attachedFieldDao = pAttachedFieldDao;
    }

    /**
     * Get the reference to <code>attachedField</code>'s DAO.
     * 
     * @return AttachedFieldDao
     */
    protected AttachedFieldDao getAttachedFieldDao() {
        return attachedFieldDao;
    }

    /** The choice field dao. */
    private ChoiceFieldDao choiceFieldDao;

    /**
     * Set the reference to <code>choiceField</code>'s DAO.
     * 
     * @param pChoiceFieldDao
     *            ChoiceFieldDao
     */
    public void setChoiceFieldDao(final ChoiceFieldDao pChoiceFieldDao) {
        choiceFieldDao = pChoiceFieldDao;
    }

    /**
     * Get the reference to <code>choiceField</code>'s DAO.
     * 
     * @return ChoiceFieldDao
     */
    protected ChoiceFieldDao getChoiceFieldDao() {
        return choiceFieldDao;
    }

    /** The link dao. */
    private LinkDao linkDao;

    /**
     * Gets the link dao.
     * 
     * @return the link dao
     */
    public LinkDao getLinkDao() {
        return linkDao;
    }

    /**
     * Sets the link dao.
     * 
     * @param pLinkDao
     *            the link dao
     */
    public void setLinkDao(final LinkDao pLinkDao) {
        linkDao = pLinkDao;
    }

    /** The attached field value dao. */
    private AttachedFieldValueDao attachedFieldValueDao;

    /**
     * Gets the attached field value dao.
     * 
     * @return the attached field value dao
     */
    public AttachedFieldValueDao getAttachedFieldValueDao() {
        return attachedFieldValueDao;
    }

    /**
     * Sets the attached field value dao.
     * 
     * @param pAttachedFieldValueDao
     *            the new attached field value dao
     */
    public void setAttachedFieldValueDao(
            final AttachedFieldValueDao pAttachedFieldValueDao) {
        attachedFieldValueDao = pAttachedFieldValueDao;
    }

    /** The command dao. */
    private CommandDao commandDao;

    /**
     * getCommandDao.
     * 
     * @return Returns the commandDao.
     */
    public CommandDao getCommandDao() {
        return commandDao;
    }

    /**
     * setCommandDao.
     * 
     * @param pCommandDao
     *            The commandDao to set.
     */
    public void setCommandDao(final CommandDao pCommandDao) {
        commandDao = pCommandDao;
    }

    /** The extension point dao. */
    private ExtensionPointDao extensionPointDao;

    /**
     * getExtensionPointDao.
     * 
     * @return Returns the extensionPointDao.
     */
    public ExtensionPointDao getExtensionPointDao() {
        return extensionPointDao;
    }

    /**
     * setExtensionPointDao.
     * 
     * @param pExtensionPointDao
     *            The extensionPointDao to set.
     */
    public void setExtensionPointDao(final ExtensionPointDao pExtensionPointDao) {
        extensionPointDao = pExtensionPointDao;
    }

    /** The display group dao. */
    private DisplayGroupDao displayGroupDao;

    /**
     * getDisplayGroupDao.
     * 
     * @return Returns the displayGroupDao.
     */
    public DisplayGroupDao getDisplayGroupDao() {
        return displayGroupDao;
    }

    /**
     * setDisplayGroupDao.
     * 
     * @param pDisplayGroupDao
     *            The displayGroupDao to set.
     */
    public void setDisplayGroupDao(final DisplayGroupDao pDisplayGroupDao) {
        displayGroupDao = pDisplayGroupDao;
    }

    /** The link sheet summary group dao. */
    private LinkSheetSummaryGroupDao linkSheetSummaryGroupDao;

    /**
     * getLinkSheetSummaryGroupDao.
     * 
     * @return Returns the linkSheetSummaryGroupDao.
     */
    public LinkSheetSummaryGroupDao getLinkSheetSummaryGroupDao() {
        return linkSheetSummaryGroupDao;
    }

    /**
     * setLinkSheetSummaryGroupDao.
     * 
     * @param pLinkSheetSummaryGroupDao
     *            The linkSheetSummaryGroupDao to set.
     */
    public void setLinkSheetSummaryGroupDao(
            final LinkSheetSummaryGroupDao pLinkSheetSummaryGroupDao) {
        linkSheetSummaryGroupDao = pLinkSheetSummaryGroupDao;
    }

    /** The extensions container dao. */
    private ExtensionsContainerDao extensionsContainerDao;

    /**
     * getExtensionsContainerDao.
     * 
     * @return Returns the extensionsContainerDao.
     */
    @Override
    public ExtensionsContainerDao getExtensionsContainerDao() {
        return extensionsContainerDao;
    }

    /**
     * setExtensionsContainerDao.
     * 
     * @param pExtensionsContainerDao
     *            The extensionsContainerDao to set.
     */
    @Override
    public void setExtensionsContainerDao(
            final ExtensionsContainerDao pExtensionsContainerDao) {
        extensionsContainerDao = pExtensionsContainerDao;
    }

    /** The category value dao. */
    private CategoryValueDao categoryValueDao;

    /**
     * getCategoryValueDao.
     * 
     * @return Returns the categoryValueDao.
     */
    public CategoryValueDao getCategoryValueDao() {
        return categoryValueDao;
    }

    /**
     * setCategoryValueDao.
     * 
     * @param pCategoryValueDao
     *            The categoryValueDao to set.
     */
    public void setCategoryValueDao(final CategoryValueDao pCategoryValueDao) {
        categoryValueDao = pCategoryValueDao;
    }

    /** The sheet utils. */
    private SheetUtils sheetUtils;

    /**
     * Gets the sheet utils.
     * 
     * @return the sheet utils
     */
    public SheetUtils getSheetUtils() {
        return sheetUtils;
    }

    /**
     * Sets the sheet utils.
     * 
     * @param pSheetUtils
     *            the new sheet utils
     */
    public void setSheetUtils(final SheetUtils pSheetUtils) {
        sheetUtils = pSheetUtils;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#lockSheet(java.lang.String,
     *      java.lang.String, int)
     */
    @Override
    public void lockSheet(String pRoleToken, String pSheetId, Lock pLock) {
        final String lCurrentUserLogin =
                getAuthService().getLoginFromToken(pRoleToken);
        final String lSheetTypeId = getValuesContainerDao().getTypeId(pSheetId);
        final boolean lIsAdmin = getAuthService().hasAdminAccess(pRoleToken);

        if (!lIsAdmin && !pLock.getOwner().equals(lCurrentUserLogin)) {
            throw new AuthorizationException(
                    "Only admin can handle locks for another user");
        }

        // Extension point preLock
        final ExtensionPoint lPreLock =
                getExecutableExtensionPoint(lSheetTypeId,
                        ExtensionPointNames.PRE_LOCK, null);

        if (lPreLock != null) {
            final Context lCtx = new ContextBase();

            lCtx.put(ExtensionPointParameters.SHEET_ID, pSheetId);
            lCtx.put(ExtensionPointParameters.LOCK, pLock);

            getExtensionsService().execute(pRoleToken, lPreLock, lCtx);
        }

        switch (pLock.getType()) {
            case NONE:
                atomicActionsManager.lock(pLock.getOwner(), null, null,
                        pSheetId, lIsAdmin);
                break;
            default:
                atomicActionsManager.lock(pLock.getOwner(),
                        LockType.fromString(pLock.getType().toString()),
                        getSessionTokenForLock(pRoleToken, pLock.getScope()),
                        pSheetId, lIsAdmin);
        }

        // Extension point postLock
        final ExtensionPoint lPostLock =
                getExecutableExtensionPoint(lSheetTypeId,
                        ExtensionPointNames.POST_LOCK, null);

        if (lPostLock != null) {
            final Context lCtx = new ContextBase();

            lCtx.put(ExtensionPointParameters.SHEET_ID, pSheetId);
            lCtx.put(ExtensionPointParameters.LOCK, pLock);

            getExtensionsService().execute(pRoleToken, lPostLock, lCtx);
        }

        removeElementFromCache(pSheetId);
    }

    private String getSessionTokenForLock(String pRoleToken,
            LockScopeEnumeration pLockScope) {
        switch (pLockScope) {
            case PERMANENT:
                return null;
            case USER_SESSION:
                return authorizationService.getUserToken(pRoleToken);
            case ROLE_SESSION:
                return pRoleToken;
            default:
                throw new RuntimeException("Value " + pLockScope + " invalid");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#lockSheet(java.lang.String,
     *      java.lang.String, org.topcased.gpm.util.bean.LockProperties)
     */
    @Override
    public void lockSheet(String pRoleToken, String pSheetId,
            LockProperties pLockProperties) throws AuthorizationException,
        LockException {
        if (StringUtils.isBlank(pSheetId)) {
            throw new IllegalArgumentException("Sheet id is blank");
        }

        if (null == pLockProperties.getLockType()) {
            throw new IllegalArgumentException("Lock type is null");
        }
        // Get current user login
        String lUserToken =
                getAuthService().getUserSessionFromRoleSession(pRoleToken);
        String lCurrentUserLogin =
                getAuthService().getLoggedUserData(lUserToken, true).getLogin();

        lockSheet(pRoleToken, pSheetId, new Lock(lCurrentUserLogin,
                pLockProperties.getLockType(), pLockProperties.getLockScope()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#removeLock(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void removeLock(String pRoleToken, String pSheetId) {
        lockSheet(pRoleToken, pSheetId, LockProperties.PERMANENT_NONE_LOCK);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getLock(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public org.topcased.gpm.business.serialization.data.Lock getLock(
            final String pRoleToken, final String pSheetId) {
        return atomicActionsManager.getLock(pSheetId);
    }

    private class CacheableSheetTypeFactory implements CacheableFactory {
        public Object createCacheableObject(Object pEntityObject) {
            SheetType lSheetType = (SheetType) pEntityObject;

            List<org.topcased.gpm.domain.facilities.DisplayGroup> lDisplayGroups;
            lDisplayGroups = getDisplayGroupDao().getDisplayGroup(lSheetType);

            return new CacheableSheetType(lSheetType, lDisplayGroups);
        }

        public Class<?>[] getSupportedClasses() {
            return new Class<?>[] { SheetType.class };
        }
    }

    private class CacheableSheetFactory implements CacheableFactory {
        public Object createCacheableObject(Object pEntityObject) {
            Sheet lSheet = (Sheet) pEntityObject;
            CacheableSheetType lSheetType =
                    getCacheableSheetType(lSheet.getDefinition().getId(),
                            CacheProperties.IMMUTABLE);
            String lStateName =
                    getLifeCycleService().getProcessStateName(lSheet.getId());

            return new CacheableSheet(lSheet, lStateName, lSheetType);
        }

        public Class<?>[] getSupportedClasses() {
            return new Class<?>[] { Sheet.class };
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#filterCacheableSheet(org.topcased.gpm.business.sheet.impl.CacheableSheet,
     *      java.util.List)
     */
    @Override
    public void filterCacheableSheet(CacheableSheet pCacheableSheetData,
            List<String> pLabelKeys) {
        if (pLabelKeys != null) {
            for (String lLabelKey : pLabelKeys) {
                if (lLabelKey == null || lLabelKey.equals("")) {
                    throw new IllegalArgumentException(
                            "The list of labelkey cannot contain null or blank values");
                }
                String[] lFieldsNameTab = lLabelKey.split(MULTIPLE_SEPARATOR);
                String lMultipleFieldName = lFieldsNameTab[0];
                String lSimpleFieldName = null;
                if (lFieldsNameTab.length > 1) {
                    // We remove a simple field from a multiple field.
                    lSimpleFieldName = lFieldsNameTab[1];
                    pCacheableSheetData.removeSubFieldValue(lMultipleFieldName,
                            lSimpleFieldName);
                }
                else {
                    // We remove a simple field or an entire multiple field.
                    pCacheableSheetData.removeFieldValue(lMultipleFieldName);
                }
            }
        }
    }

    /**
     * Sheet data factory : Construction of a sheet data from a cacheable sheet
     * 
     * @deprecated
     * @since 1.8
     * @see CacheableSheet
     */
    private class SheetDataFactory implements ContextValueFactory {
        private CacheableSheet sheet;

        private String roleToken;

        protected SheetDataFactory(String pRoleToken, CacheableSheet pSheet) {
            sheet = pSheet;
            roleToken = pRoleToken;
        }

        public Object create(String pName, Class<?> pObjClass) {
            return dataTransformationServiceImpl.getSheetDataFromCacheableSheet(
                    roleToken, sheet);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getCacheableSheetLinksByType(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public CacheableSheetLinksByType getCacheableSheetLinksByType(
            String pProcessName, String pSheetId, String pLinkTypeId) {
        // CacheableSheetLinksByType are always immutable
        CacheableSheetLinksByType lCachedElement =
                getCachedElement(CacheableSheetLinksByType.class,
                        CacheableSheetLinksByType.getCachedElementId(pSheetId,
                                pLinkTypeId), CACHE_IMMUTABLE_OBJECT);

        // The cacheable sheet must be load
        if (lCachedElement == null) {
            lCachedElement =
                    new CacheableSheetLinksByType(pSheetId, pLinkTypeId,
                            getLinkService().getSheetLinksByType(pProcessName,
                                    pSheetId, pLinkTypeId, true));
            addElementInCache(lCachedElement);
        }

        return lCachedElement;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#initializeSheetValues(java.lang.String,
     *      org.topcased.gpm.business.sheet.impl.CacheableSheetType,
     *      org.topcased.gpm.business.sheet.impl.CacheableSheet,
     *      org.topcased.gpm.business.sheet.impl.CacheableSheetType,
     *      org.topcased.gpm.business.sheet.impl.CacheableSheet)
     */
    @Override
    public void initializeSheetValues(final String pRoleToken,
            final CacheableSheetType pOriginSheetType,
            final CacheableSheet pOriginSheet,
            final CacheableSheetType pDestinationSheetType,
            final CacheableSheet pDestinationSheet, final Context pContext) {
        valuesServiceImpl.initializeValues(pRoleToken,
                new CacheableSheetAccess(pRoleToken, pOriginSheetType,
                        pOriginSheet,
                        ValuesAccessProperties.NOT_CHECKED_READ_ONLY),
                new CacheableSheetAccess(pRoleToken, pDestinationSheetType,
                        pDestinationSheet,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE),
                pContext);
    }

    /**
     * Clean all the sesion locks : must be called after a restart of the
     * application
     */
    // TODO: define in interface
    public void cleanAllSessionLocks() {
        final List<String> lContainerLockedForSession =
                getLockDao().getAllContainerLockedForSession();

        if (lContainerLockedForSession != null) {
            for (String lContainerLockedId : lContainerLockedForSession) {
                atomicActionsManager.lockSheet(null, null, null,
                        lContainerLockedId, true);
                removeElementFromCache(lContainerLockedId);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
     */
    @Override
    @SuppressWarnings("unchecked")
    public void onApplicationEvent(ApplicationEvent pEvent) {
        if (pEvent instanceof LogoutEvent) {
            final LogoutEvent lLogoutEvent = (LogoutEvent) pEvent;
            final AbstractContext lClosedSessionContext =
                    lLogoutEvent.getClosedSessionContext();

            if (lClosedSessionContext != null) {
                final List<org.topcased.gpm.domain.fields.Lock> lLocksToRemove =
                        getLockDao().getLocksBySessionToken(
                                lClosedSessionContext.getToken());

                if (lLocksToRemove != null && !lLocksToRemove.isEmpty()) {
                    for (org.topcased.gpm.domain.fields.Lock lLock : lLocksToRemove) {
                        atomicActionsManager.lockSheet(null, null, null,
                                lLock.getContainerId(), true);
                        removeElementFromCache(lLock.getContainerId());
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getSheetIdByReference(String,
     *      String)
     */
    @Override
    public String getSheetIdByReference(final String pRoleToken,
            final String pReference) {
        authorizationService.assertValidRoleToken(pRoleToken);
        String lProcessName =
                authorizationService.getProcessNameFromToken(pRoleToken);
        String lProductName =
                authorizationService.getProductNameFromSessionToken(pRoleToken);
        return getSheetIdByReference(lProcessName, lProductName, pReference);
    }

    /**
     * Retrieve the identifier of the sheet from its reference.
     * 
     * @param pProcessName
     *            Process name
     * @param pProductName
     *            Product name
     * @param pReference
     *            Sheet's reference
     * @return Identifier of the sheet.
     */
    @Override
    public String getSheetIdByReference(final String pProcessName,
            final String pProductName, final String pReference) {
        return sheetDao.getIdByReference(pProcessName, pProductName, pReference);
    }

    /**
     * Test sheet existence using its reference
     * 
     * @param pTypeName
     *            Type name of the sheet
     * @param pProductName
     *            Product name of the sheet
     * @param pReference
     *            Reference of the sheet
     * @return True if the sheet exists, false otherwise
     */
    // TODO: define in interface
    public boolean isReferenceExists(String pTypeName, String pProductName,
            String pReference) {
        if (StringUtils.isNotBlank(pTypeName)
                && StringUtils.isNotBlank(pProductName)
                && StringUtils.isNotBlank(pReference)) {
            return sheetDao.isReferenceExists(pTypeName, pProductName,
                    pReference);
        }
        else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#isSheetIdExist(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public boolean isSheetIdExist(String pUserToken, String pSheetId) {
        getAuthService().validateToken(pUserToken);
        return sheetDao.exist(pSheetId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getProcessName(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public String getProcessName(String pUserToken, String pSheetId) {
        getAuthService().validateToken(pUserToken);
        return getCacheableSheetType(
                getCacheableSheet(pSheetId, CacheProperties.IMMUTABLE).getTypeId(),
                CacheProperties.IMMUTABLE).getBusinessProcessName();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#getProductName(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public String getProductName(String pUserToken, String pSheetId) {
        getAuthService().validateToken(pUserToken);
        return getCacheableSheet(pSheetId, CacheProperties.IMMUTABLE).getProductName();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.SheetService#isSheetLocked(String,
     *      String, DisplayMode)
     */
    @Override
    public boolean isSheetLocked(String pRoleToken, String pSheetId,
            DisplayMode pDisplayMode) {
        boolean lIsSheetLocked = false;
        switch (pDisplayMode) {
            case VISUALIZATION:
                lIsSheetLocked =
                        !getFieldsContainerServiceImpl().isValuesContainerReadable(
                                pRoleToken, pSheetId);
                break;
            case EDITION:
                lIsSheetLocked =
                        !getFieldsContainerServiceImpl().isValuesContainerWritable(
                                pRoleToken, pSheetId);
            default:
                break;
        }
        return lIsSheetLocked;
    }

    private List<AttachmentInError> checkAttachedFileProperties(CacheableSheet pCacheableSheetData,
            String pRoleToken,
            List<AttachedFieldValueData> pAttachedFieldValues) {
        
        String lId = pCacheableSheetData.getId();

        // Do not try to add errors if some errors are already present
        List<AttachmentInError> lPrevious = attachedFilesInError.get(lId);
        if (lPrevious != null && !lPrevious.isEmpty()) {
        	return lPrevious;
        }
        
    	List<AttachmentInError> lAttachedFieldsInError = new ArrayList<AttachmentInError>();

        // Check max attached file size
        if (getMaxAttachedFileSize() != 0) {

            double lOldContentSize = 0;
            double lNewContentSize = 0;

            for (AttachedFieldValueData lAttachedFieldValue : pAttachedFieldValues) {
            	
            	// FIXME : ne pas stocker l'objet entier en m�moire !
                final byte[] lNewContent = lAttachedFieldValue.getNewContent();

                if (lNewContent != null) {
                    // new attached File to Sheet
                    lNewContentSize += (double) lNewContent.length / BYTETOMEGARATIO;
                }
                else if (lAttachedFieldValue.getId() != null) {
                    // Old attached file, we should get its size;
                    lOldContentSize += getAttachedFileSize(pRoleToken, lAttachedFieldValue.getId());
                }
            }
            
            // Check that the total size does not exceed the maximum size.
            // Note that if the previous size already exceeds the maximum size (this may happen
            // if the sheet has been imported), and no other file is attached, do not generate an error. 
            if (lNewContentSize > 0 && getMaxAttachedFileSize() - (lNewContentSize + lOldContentSize) < 0) {
            	lAttachedFieldsInError.add(
            			new AttachmentInError(AttachmentStatus.EXCEEDED_SIZE, (int) getMaxAttachedFileSize()));
             }
        }

        // Check empty files
        for (AttachedFieldValueData lFieldData : pAttachedFieldValues) {
            if (!StringUtils.isBlank(lFieldData.getValue())
                    && lFieldData.getNewContent() != null
                    && !StringUtils.isBlank(lFieldData.getFilename())) {
                if (lFieldData.getNewContent().length == 0) {
                    lAttachedFieldsInError.add(
                    		new AttachmentInError(AttachmentStatus.ZERO_SIZE, lFieldData.getFilename()));
                     pCacheableSheetData.removeFieldValue(
                            pCacheableSheetData.getValuesMap(),
                            lFieldData.getName(), lFieldData.getValue());
                }
            }
            
        }

        char[] lInvalidChars = AttachmentUtils.getInvalidChars();
        // Check invalid characters
        for (AttachedFieldValueData lFieldData : pAttachedFieldValues) {
            if (!StringUtils.isBlank(lFieldData.getValue())
                    && lFieldData.getNewContent() != null
                    && !StringUtils.isBlank(lFieldData.getFilename())) {
            	
            	String name = lFieldData.getFilename();
				String previousName = name;
				boolean replaced = false;
				for (char c : lInvalidChars) {
					if (name.indexOf(c) != -1) {
						name = name.replace(c, '_');
						replaced = true;
					}
				}
				if (replaced) {
					// item 1 : name in error
					// item 2 : corrected name (invalid chars -> underscore)
                    lAttachedFieldsInError.add(new AttachmentInError(AttachmentStatus.INVALID_NAME, previousName, name));
                    
                    pCacheableSheetData.removeFieldValue(
                            pCacheableSheetData.getValuesMap(),
                            lFieldData.getName(), lFieldData.getValue());
                    // lFieldData.setFilename(name);
                }
            }
        }
        
      	attachedFilesInError.put(lId, lAttachedFieldsInError);
        
        return lAttachedFieldsInError;
    }

    /**
     * set the MaxAttachedFileSize
     * 
     * @param pValue
     *            the maximum attached field size
     */
    public void setMaxAttachedFileSize(double pValue) {
        this.maxAttachedFileSize = pValue;
    }

    @Override
    public double getMaxAttachedFileSize() {
        return maxAttachedFileSize;
    }

    /**
     * Retrieves the attached file content, Field ID given
     * 
     * @param pRoleToken
     *            role token
     * @param pAttachedFieldValueId
     *            attached field id
     * @return file size
     */
    private double getAttachedFileSize(final String pRoleToken,
            final String pAttachedFieldValueId) {

        // Load the attached field value.
        AttachedFieldValue lAttachedFieldValue =
                (AttachedFieldValue) getAttachedFieldValueDao().load(
                        pAttachedFieldValueId);

        // Launch an exception if the attached field value is invalid.
        if (null == lAttachedFieldValue) {
            throw new GDMException("Invalid attached field value identifier");
        }

        // Get the attached files size From attached field value.
        return lAttachedFieldValue.getFileSize();
    }

    /**
     * Retrieves the attached file content, Field ID given
     * 
     * @param pSheetId
     *            the sheet id
     * @return list of attached field in error
     */
    @Override
    public List<AttachmentInError> getAndAcknowledgeAttachedFilesInError(String pSheetId) {
    	List<AttachmentInError> lResult = attachedFilesInError.get(pSheetId);
        attachedFilesInError.remove(pSheetId);
        return lResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSheetTypeIdBySheetId(String pSheetId) {
        return getValuesContainerDao().getTypeId(pSheetId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getSheetTypeIdBySheetIds(List<String> pSheetIds) {
        return getValuesContainerDao().getTypesId(pSheetIds);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getSheetsCount(String pSheetTypeId){
        return getSheetDao().getSheetsCount(
                getSheetType(pSheetTypeId));
    }
}
