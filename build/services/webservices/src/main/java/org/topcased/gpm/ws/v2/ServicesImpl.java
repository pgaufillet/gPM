/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Gehin (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ws.v2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.attributes.AttributeData;
import org.topcased.gpm.business.authorization.impl.filter.FilterAccessControl;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.authorization.service.ActionAccessControlData;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.authorization.service.RoleProperties;
import org.topcased.gpm.business.dictionary.EnvironmentData;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.exportation.ExportProperties;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ContextBase;
import org.topcased.gpm.business.extensions.service.ExtendedActionData;
import org.topcased.gpm.business.extensions.service.ExtendedActionResult;
import org.topcased.gpm.business.fields.SummaryData;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fields.impl.CacheableInputData;
import org.topcased.gpm.business.fields.impl.CacheableInputDataType;
import org.topcased.gpm.business.importation.ImportExecutionReport;
import org.topcased.gpm.business.importation.ImportProperties;
import org.topcased.gpm.business.lifecycle.service.ProcessInformation;
import org.topcased.gpm.business.link.impl.CacheableLink;
import org.topcased.gpm.business.link.impl.CacheableLinkType;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.service.ProductSummaryData;
import org.topcased.gpm.business.report.ReportModelData;
import org.topcased.gpm.business.revision.RevisionSummaryData;
import org.topcased.gpm.business.scalar.BooleanValueData;
import org.topcased.gpm.business.scalar.DateValueData;
import org.topcased.gpm.business.scalar.IntegerValueData;
import org.topcased.gpm.business.scalar.RealValueData;
import org.topcased.gpm.business.scalar.ScalarValueData;
import org.topcased.gpm.business.scalar.StringValueData;
import org.topcased.gpm.business.search.criterias.CriteriaData;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.FilterData;
import org.topcased.gpm.business.search.criterias.FilterTypeData;
import org.topcased.gpm.business.search.criterias.OperationData;
import org.topcased.gpm.business.search.criterias.impl.VirtualFieldData;
import org.topcased.gpm.business.search.impl.fields.UsableFieldsManager;
import org.topcased.gpm.business.search.impl.fields.UsableTypeData;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.FilterResultIdIterator;
import org.topcased.gpm.business.search.impl.query.FilterResultIterator;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.FilterScope;
import org.topcased.gpm.business.search.service.FilterVisibilityConstraintData;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.business.serialization.data.AttachedDisplayHint;
import org.topcased.gpm.business.serialization.data.AttachedField;
import org.topcased.gpm.business.serialization.data.AttachedFieldValueData;
import org.topcased.gpm.business.serialization.data.CategoryValue;
import org.topcased.gpm.business.serialization.data.ChoiceDisplayHint;
import org.topcased.gpm.business.serialization.data.ChoiceField;
import org.topcased.gpm.business.serialization.data.ChoiceStringDisplayHint;
import org.topcased.gpm.business.serialization.data.ChoiceTreeDisplayHint;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.InputData;
import org.topcased.gpm.business.serialization.data.InputDataType;
import org.topcased.gpm.business.serialization.data.Link;
import org.topcased.gpm.business.serialization.data.LinkType;
import org.topcased.gpm.business.serialization.data.Lock;
import org.topcased.gpm.business.serialization.data.Lock.LockTypeEnumeration;
import org.topcased.gpm.business.serialization.data.PointerFieldValueData;
import org.topcased.gpm.business.serialization.data.Product;
import org.topcased.gpm.business.serialization.data.ProductType;
import org.topcased.gpm.business.serialization.data.RevisionData;
import org.topcased.gpm.business.serialization.data.SheetData;
import org.topcased.gpm.business.serialization.data.SheetType;
import org.topcased.gpm.business.serialization.data.SimpleField;
import org.topcased.gpm.business.serialization.data.TextDisplayHint;
import org.topcased.gpm.business.serialization.data.User;
import org.topcased.gpm.business.sheet.export.service.SheetExportFormat;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetHistoryData;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.business.util.AttachmentUtils;
import org.topcased.gpm.business.values.field.virtual.VirtualFieldType;
import org.topcased.gpm.common.accesscontrol.Roles;
import org.topcased.gpm.common.extensions.GUIContext;
import org.topcased.gpm.domain.export.ExportType;
import org.topcased.gpm.domain.export.ExportTypeEnum;
import org.topcased.gpm.domain.search.FilterType;
import org.topcased.gpm.domain.search.FilterTypeEnum;
import org.topcased.gpm.util.bean.CacheProperties;
import org.topcased.gpm.util.bean.LockProperties;
import org.topcased.gpm.ws.context.BooleanParam;
import org.topcased.gpm.ws.context.ContextParam;
import org.topcased.gpm.ws.context.IntegerParam;
import org.topcased.gpm.ws.context.StringParam;
import org.topcased.gpm.ws.context.StringsListParam;
import org.topcased.gpm.ws.v2.business.ProductNode;
import org.topcased.gpm.ws.v2.extensions.ExtendedActionSummary;
import org.topcased.gpm.ws.v2.extensions.GuiContext;
import org.topcased.gpm.ws.v2.search.FilterExecutionReport;
import org.topcased.gpm.ws.v2.search.FilterResult;
import org.topcased.gpm.ws.v2.util.FilterUsageEnum;

/**
 * Implementation of the SEI.
 * 
 * @author ogehin
 */
@WebService(endpointInterface = "org.topcased.gpm.ws.v2.Services")
public class ServicesImpl implements Services {

    /** The dummy string. */
    private final static String DUMMY_STRING = "";

    /** The generation format exception message. */
    private final static String GENERATION_FORMAT_EXCEPTION_MSG =
            "The generation format is not found.";

    /** The generation multi report exception message. */
    private final static String GENERATION_MULTI_REPORT_MSG =
            "Many reports are found. Choose one among the following reports : ";

    /** The generation report name not found message. */
    private final static String GENERATION_REPORT_NAME_MSG =
            "This report is not found : ";

    /**
     * The serviceLocator is a factory that is able to retrieve instance of
     * different services of the business API.
     */
    private ServiceLocator serviceLocator;

    /**
     * get serviceLocator
     * 
     * @return the serviceLocator
     */
    public ServiceLocator getServiceLocator() {
        if (serviceLocator == null) {
            serviceLocator = ServiceLocator.instance();
        }
        return serviceLocator;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#login(String, String)
     */
    public String login(String pUserName, String pPassword) throws GDMException {
        return getServiceLocator().getAuthorizationService().login(pUserName,
                pPassword);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#selectRole(String, String, String,
     *      String)
     */
    public String selectRole(String pUserToken, String pRoleName,
            String pProductName, String pBusinessProcessName)
        throws GDMException {
        return getServiceLocator().getAuthorizationService().selectRole(
                pUserToken, pRoleName, pProductName, pBusinessProcessName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#logout()
     */
    public void logout(String pUserToken) throws GDMException {
        getServiceLocator().getAuthorizationService().logout(pUserToken);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#lockSheet(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.business.serialization.data.Lock.LockTypeEnumeration)
     */
    public void lockSheet(final String pRoleToken, final String pSheetId,
            final LockTypeEnumeration pLockType) throws GDMException {
        getServiceLocator().getSheetService().lockSheet(pRoleToken, pSheetId,
                new LockProperties(pLockType));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#lockSheetForUser(java.lang.String,
     *      java.lang.String, org.topcased.gpm.business.serialization.data.Lock)
     */
    public void lockSheetForUser(final String pRoleToken,
            final String pSheetId, final Lock pLock) throws GDMException {
        getServiceLocator().getSheetService().lockSheet(pRoleToken, pSheetId,
                pLock);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getLock(java.lang.String,
     *      java.lang.String)
     */
    public Lock getLock(final String pRoleToken, final String pSheetId)
        throws GDMException {
        return getServiceLocator().getSheetService().getLock(pRoleToken,
                pSheetId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getSheetTypes(java.lang.String,
     *      java.lang.String)
     */
    public SheetType[] getSheetTypes(String pRoleToken, String pProcessName)
        throws GDMException {
        List<SheetType> lSheetTypes =
                getServiceLocator().getSheetService().getSerializableSheetTypes(
                        pRoleToken, pProcessName);
        return lSheetTypes.toArray(new SheetType[lSheetTypes.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getSheetTypesByKeys(java.lang.String,
     *      java.lang.String[])
     */
    public SheetType[] getSheetTypesByKeys(String pRoleToken, String[] pKeys)
        throws GDMException {
        if (pKeys == null) {
            return null;
        }
        ArrayList<SheetType> lSheetTypes = new ArrayList<SheetType>();
        for (String lKey : pKeys) {
            lSheetTypes.add(getServiceLocator().getSheetService().getSerializableSheetType(
                    lKey));
        }
        return lSheetTypes.toArray(new SheetType[lSheetTypes.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getSheetTypeWithAccessControls(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    public SheetType getSheetTypeWithAccessControls(String pRoleToken,
            String pProcessName, String pProductName, String pStateName,
            String pSheetTypeName) throws GDMException {
        SheetType lSheetType = new SheetType();
        CacheProperties lCacheProperties =
                new CacheProperties(
                        true,
                        new AccessControlContextData(
                                pProductName,
                                getServiceLocator().getAuthorizationService().getRoleNameFromToken(
                                        pRoleToken), pStateName, null, null,
                                null));

        CacheableSheetType lCacheableSheetType =
                getServiceLocator().getSheetService().getCacheableSheetTypeByName(
                        pRoleToken, pProcessName, pSheetTypeName,
                        lCacheProperties);

        // if the CacheableSheetType was not found 
        if (null == lCacheableSheetType) {
            throw new InvalidNameException("Type ''{0}'' does not exist",
                    pSheetTypeName);
        }

        lCacheableSheetType.marshal(lSheetType);

        return lSheetType;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getSheetsByKeys(String, String[])
     */
    public SheetData[] getSheetsByKeys(String pRoleToken, String[] pKeys)
        throws GDMException {
        if (pKeys == null) {
            return null;
        }
        ArrayList<SheetData> lSheetDatas = new ArrayList<SheetData>();
        for (String lKey : pKeys) {
            if (getServiceLocator().getFieldsContainerService().isValuesContainerExists(
                    lKey)) {
                lSheetDatas.add(getServiceLocator().getSheetService().getSerializableSheet(
                        pRoleToken, lKey));
            }
        }
        return lSheetDatas.toArray(new SheetData[lSheetDatas.size()]);
    }

    /**
     * Get an 'empty' sheet corresponding to a given sheet type.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pTypeName
     *            Sheet type name
     * @param pProductName
     *            Product name
     * @param pCtxParams
     *            Array of context parameters (may be null)
     * @return The sheet data pre-filled with the default values.
     * @throws GDMException
     *             internal exception.
     */
    public SheetData getSheetModel(String pRoleToken, String pTypeName,
            String pProductName, ContextParam[] pCtxParams) throws GDMException {
        CacheableSheet lCachedSheet;
        CacheableSheetType lType;

        lType = getServiceLocator().getSheetService().getCacheableSheetTypeByName(
                        pRoleToken, pTypeName, CacheProperties.IMMUTABLE);

        // if the CacheableSheetType was not found 
        if (null == lType) {
            throw new InvalidNameException("Type ''{0}'' does not exist",
                    pTypeName);
        }

        lCachedSheet = getServiceLocator().getSheetService().getCacheableSheetModel(
                        pRoleToken, lType, pProductName, convertCtx(pCtxParams));

        return (SheetData) (lCachedSheet.marshal());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getLinkTypesByKeys(String, String[])
     */
    public LinkType[] getLinkTypesByKeys(String pRoleToken, String[] pKeys)
        throws GDMException {
        if (pKeys == null) {
            return null;
        }
        ArrayList<LinkType> lLinkTypes = new ArrayList<LinkType>();
        for (String lKey : pKeys) {
            lLinkTypes.add(getServiceLocator().getLinkService().getSerializableLinkType(
                    pRoleToken, lKey));
        }
        return lLinkTypes.toArray(new LinkType[lLinkTypes.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getLinks(java.lang.String,
     *      java.lang.String)
     */
    public Link[] getLinks(String pRoleToken, String pContainerId)
        throws GDMException {
        List<Link> lLinks =
                getServiceLocator().getLinkService().getSerializableLinks(
                        pRoleToken, pContainerId);
        return lLinks.toArray(new Link[lLinks.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getLinksByKeys(String, String[])
     */
    public Link[] getLinksByKeys(String pRoleToken, String[] pKeys)
        throws GDMException {
        if (pKeys == null) {
            return null;
        }
        ArrayList<Link> lLinks = new ArrayList<Link>();
        for (String lKey : pKeys) {
            lLinks.add(getServiceLocator().getLinkService().getSerializableLinkByKey(
                    pRoleToken, lKey));
        }
        return lLinks.toArray(new Link[lLinks.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getLinkableSheetTypes(java.lang.String)
     */
    public Collection<String> getLinkableSheetTypes(String pRoleToken,
            String pSheetTypeId) {
        return getServiceLocator().getSheetService().getLinkableSheetTypes(
                pSheetTypeId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getProductTypesByKeys(String,
     *      String[])
     */
    public ProductType[] getProductTypesByKeys(String pRoleToken, String[] pKeys)
        throws GDMException {
        if (pKeys == null) {
            return null;
        }
        ArrayList<ProductType> lProductTypes = new ArrayList<ProductType>();
        for (String lKey : pKeys) {
            lProductTypes.add(getServiceLocator().getProductService().getSerializableProductType(
                    pRoleToken, lKey));
        }
        return lProductTypes.toArray(new ProductType[lProductTypes.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getProductsByKeys(String, String[])
     */
    public Product[] getProductsByKeys(String pRoleToken, String[] pKeys)
        throws GDMException {
        if (pKeys == null) {
            return null;
        }
        ArrayList<Product> lProducts = new ArrayList<Product>();
        for (String lKey : pKeys) {
            lProducts.add(getServiceLocator().getProductService().getSerializableProduct(
                    pRoleToken, lKey));
        }
        return lProducts.toArray(new Product[lProducts.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#createSheet(String, String, String,
     *      SheetData)
     */
    public String createSheet(String pRoleToken, String pProcessName,
            String pProductName, SheetData pSheetData) throws GDMException {
    	SheetService lService = getServiceLocator().getSheetService();
        pSheetData.setProductName(pProductName);
        CacheableSheetType lSheetType = lService.getCacheableSheetTypeByName(pRoleToken, pProcessName,
                        pSheetData.getType(), CacheProperties.IMMUTABLE);
        CacheableSheet lSheet = new CacheableSheet(pSheetData, lSheetType);
        AttachmentUtils.correctAttachmentNames(lSheet);
        return lService.createSheet(pRoleToken, lSheet, null);
    }



	/**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#createSheetEx(String, String,
     *      String, SheetData, Context)
     */
    public String createSheetEx(String pRoleToken, String pProcessName,
            String pProductName, SheetData pSheetData, ContextParam[] pCtxParams)
        throws GDMException {
        pSheetData.setProductName(pProductName);
        return getServiceLocator().getSheetService().createSheet(pRoleToken,
                pProcessName, pSheetData, convertCtx(pCtxParams));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#updateSheets(String, String,
     *      SheetData[])
     */
    public void updateSheets(String pRoleToken, String pProcessName,
    		SheetData[] pSheetDatas) throws GDMException {
    	if (pSheetDatas != null) {
    		SheetService lService = getServiceLocator().getSheetService();
    		for (SheetData lSheetData : pSheetDatas) {
    			CacheableSheetType lSheetType = lService.getCacheableSheetTypeByName(pRoleToken,
    					pProcessName, lSheetData.getType(), CacheProperties.IMMUTABLE);
    			CacheableSheet lSheet = new CacheableSheet(lSheetData, lSheetType);
    			AttachmentUtils.correctAttachmentNames(lSheet);
    			lService.updateSheet(pRoleToken, lSheet, ContextBase.getEmptyContext());
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#updateSheets(String, String,
     *      SheetData[], ContextParam[])
     */
    public void updateSheetsEx(String pRoleToken, String pProcessName,
            SheetData[] pSheetDatas, ContextParam[] pCtxParams)
        throws GDMException {
        Context lContext = convertCtx(pCtxParams);
        if (pSheetDatas != null) {
            for (SheetData lSheetData : pSheetDatas) {
                getServiceLocator().getSheetService().updateSheet(pRoleToken,
                        pProcessName, lSheetData, lContext);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v1.Services#getProductNames(String, String)
     */
    public Collection<String> getProductNames(String pUserToken,
            String pBusinessProcess) throws GDMException {
        return getServiceLocator().getAuthorizationService().getProductNames(
                pUserToken, pBusinessProcess);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getProducts(java.lang.String,
     *      java.lang.String)
     */
    public Product[] getProducts(String pUserToken, String pBusinessProcess)
        throws GDMException {
        Collection<Product> lProducts =
                getServiceLocator().getAuthorizationService().getSerializableProducts(
                        pUserToken, pBusinessProcess);
        return lProducts.toArray(new Product[lProducts.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getProduct(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public Product getProduct(String pRoleToken, String pBusinessProcess,
            String pProductName) throws GDMException {
        String lProductId =
                getServiceLocator().getProductService().getProductId(
                        pRoleToken, pProductName);
        return getServiceLocator().getProductService().getSerializableProduct(
                pRoleToken, lProductId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getProductByKey(java.lang.String,
     *      java.lang.String)
     */
    public Product getProductByKey(String pRoleToken, String pProductId)
        throws GDMException {
        return getServiceLocator().getProductService().getSerializableProduct(
                pRoleToken, pProductId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getRolesNames(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public String[] getRolesNames(String pUserToken,
            String pBusinessProcessName, String pProductName)
        throws GDMException {
        Collection<String> lRoles =
                getServiceLocator().getAuthorizationService().getRolesNames(
                        pUserToken, pBusinessProcessName, pProductName);
        return lRoles.toArray(new String[lRoles.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getRoleName(java.lang.String)
     */
    public String getRoleName(String pRoleToken) throws GDMException {
        return getServiceLocator().getAuthorizationService().getRoleNameFromToken(
                pRoleToken);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#deleteSheet(java.lang.String,
     *      java.lang.String[])
     */
    public void deleteSheets(String pRoleToken, String[] pKeys)
        throws GDMException {
        if (pKeys != null) {
            for (String lKey : pKeys) {
                getServiceLocator().getSheetService().deleteSheet(pRoleToken,
                        lKey, null);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#deleteSheet(java.lang.String,
     *      java.lang.String[], ContextParam[])
     */
    public void deleteSheetsEx(String pRoleToken, String[] pKeys,
            ContextParam[] pCtxParams) throws GDMException {
        Context lCtx = convertCtx(pCtxParams);
        if (pKeys != null) {
            for (String lKey : pKeys) {
                getServiceLocator().getSheetService().deleteSheet(pRoleToken,
                        lKey, lCtx);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#createSheetLink(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String)
     * @deprecated since 3.0.0: this method shall be removed, as Links should
     *             not be directly accessible
     */
    public Link createSheetLink(String pRoleToken, String pLinkTypeName,
            String pSourceId, String pDestId) throws GDMException {

        CacheableLink lLink =
                getServiceLocator().getLinkService().createCacheableSheetLink(
                        pRoleToken, pLinkTypeName, pSourceId, pDestId, null);
        Link lSerializableLink = new Link();
        lLink.marshal(lSerializableLink);
        return lSerializableLink;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#createSheetLink(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      ContextParam[])
     * @deprecated since 3.0.0: this method shall be removed, as Links should
     *             not be directly accessible
     */
    public Link createSheetLinkEx(String pRoleToken, String pLinkTypeName,
            String pSourceId, String pDestId, ContextParam[] pCtxParams)
        throws GDMException {

        Context lCtx = convertCtx(pCtxParams);
        CacheableLink lLink =
                getServiceLocator().getLinkService().createCacheableSheetLink(
                        pRoleToken, pLinkTypeName, pSourceId, pDestId, lCtx);
        Link lSerializableLink = new Link();
        lLink.marshal(lSerializableLink);
        return lSerializableLink;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#deleteLinks(java.lang.String,
     *      java.lang.String[])
     */
    public void deleteLinks(String pRoleToken, String[] pKeys)
        throws GDMException {
        if (pKeys != null) {
            for (String lKey : pKeys) {
                getServiceLocator().getLinkService().deleteLink(pRoleToken,
                        lKey, null);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#deleteLinks(java.lang.String,
     *      java.lang.String[], ContextParam[])
     */
    public void deleteLinksEx(String pRoleToken, String[] pKeys,
            ContextParam[] pCtxParams) throws GDMException {
        Context lCtx = convertCtx(pCtxParams);
        if (pKeys != null) {
            for (String lKey : pKeys) {
                getServiceLocator().getLinkService().deleteLink(pRoleToken,
                        lKey, lCtx);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#updateLinks(String, Link[])
     */
    public void updateLinks(String pRoleToken, Link[] pLinks)
        throws GDMException {
        if (pLinks != null) {
            for (Link lLink : pLinks) {
                getServiceLocator().getLinkService().updateLink(pRoleToken,
                        lLink, null);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#updateLinksEx(String, Link[],
     *      ContextParam[])
     */
    public void updateLinksEx(String pRoleToken, Link[] pLinks,
            ContextParam[] pCtxParams) throws GDMException {
        Context lCtx = convertCtx(pCtxParams);
        if (pLinks != null) {
            for (Link lLink : pLinks) {
                getServiceLocator().getLinkService().updateLink(pRoleToken,
                        lLink, lCtx);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getSheetsByRefs(java.lang.String,
     *      java.lang.String[])
     */
    public SheetData[] getSheetsByRefs(String pRoleToken, String pProcessName,
            String pProductName, String[] pRefs) throws GDMException {
        if (pRefs == null) {
            return null;
        }
        ArrayList<SheetData> lSheetDatas = new ArrayList<SheetData>();
        for (String lRef : pRefs) {
            SheetData lSheet =
                    getServiceLocator().getSheetService().getSerializableSheetByRef(
                            pRoleToken, pProcessName, pProductName, lRef);
            if (null == lSheet) {
                throw new InvalidIdentifierException("Invalid reference '"
                        + lRef + "'");
            }
            lSheetDatas.add(lSheet);
        }
        return lSheetDatas.toArray(new SheetData[lSheetDatas.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#changeState(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public void changeState(String pRoleToken, String pSheetId,
            String pTransition) throws GDMException {
        getServiceLocator().getSheetService().changeState(pRoleToken, pSheetId,
                pTransition, null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#changeState(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public void changeStateEx(String pRoleToken, String pSheetId,
            String pTransition, ContextParam[] pCtxParams) throws GDMException {
        getServiceLocator().getSheetService().changeState(pRoleToken, pSheetId,
                pTransition, convertCtx(pCtxParams));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#executeProductFilter(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String)
     * @see #executeProductFilterWithScope(String, int, int, String, String,
     *      FilterScope) for a more precise control
     */
    public ProductSummaryData[] executeProductFilter(String pRoleToken,
            String pBusinessProcessName, String pProductName,
            String pUserLogin, String pFilterName) throws GDMException {

        Collection<SummaryData> lSummaries =
                executeFilterForSummaries(pRoleToken, pBusinessProcessName,
                        pProductName, pUserLogin, pFilterName);
        return lSummaries.toArray(new ProductSummaryData[lSummaries.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v1.Services#
     *      executeProductFilterWithScope(String, int, int, String, String,
     *      FilterScope)
     */
    @SuppressWarnings("unchecked")
	public ProductSummaryData[] executeProductFilterWithScope(
            String pRoleToken, int pMaxResult, int pFirstResult,
            String pProductName, String pFilterName, FilterScope pScope)
        throws GDMException {
        FilterVisibilityConstraintData lConstraints =
                new FilterVisibilityConstraintData(StringUtils.EMPTY,
                        StringUtils.EMPTY, pProductName);
        ExecutableFilterData lFilter =
                getServiceLocator().getSearchService().retrieveFilterDataFromName(
                        pRoleToken, pFilterName, lConstraints, pScope);
        FilterResultIterator<SheetSummaryData> lResult =
                getServiceLocator().getSearchService().executeFilter(
                        pRoleToken, lFilter, lConstraints,
                        new FilterQueryConfigurator(0, -1, 0));
        Collection<ProductSummaryData> lProducts =
                IteratorUtils.toList(lResult);
        return lProducts.toArray(new ProductSummaryData[lProducts.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#executeSheetFilter(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String)
     * @see #executeSheetFilterWithScope(String, int, int, String, String,
     *      FilterScope) for a more precise control
     */
    public SheetSummaryData[] executeSheetFilter(String pRoleToken,
            String pBusinessProcessName, String pProductName,
            String pUserLogin, String pFilterName) throws GDMException {
        Collection<SummaryData> lSummaries =
                executeFilterForSummaries(pRoleToken, pBusinessProcessName,
                        pProductName, pUserLogin, pFilterName);
        return lSummaries.toArray(new SheetSummaryData[lSummaries.size()]);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private Collection<SummaryData> executeFilterForSummaries(
            String pRoleToken, String pBusinessProcessName,
            String pProductName, String pUserLogin, String pFilterName)
        throws GDMException {
        FilterVisibilityConstraintData lConstraints =
                new FilterVisibilityConstraintData(StringUtils.EMPTY,
                        StringUtils.EMPTY, pProductName);
        ExecutableFilterData lFilter =
                getServiceLocator().getSearchService().retrieveFilterDataFromName(
                        pRoleToken, pFilterName, lConstraints, null);

        // Throw exception if the executable filter data doesn't exist.
        if (lFilter == null || lFilter.getResultSummaryData() == null) {
            throw new InvalidNameException(pFilterName);
        }

        FilterResultIterator lFilterResultIterator =
                getServiceLocator().getSearchService().executeFilter(
                        pRoleToken,
                        lFilter,
                        new FilterVisibilityConstraintData(pUserLogin,
                                pBusinessProcessName, pProductName),
                        new FilterQueryConfigurator());

        return IteratorUtils.toList(lFilterResultIterator);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#executeSheetFilter(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String)
     * @see #executeSheetFilterWithScope(String, int, int, String, String,
     *      FilterScope) for a more precise control
     */
    @SuppressWarnings("unchecked")
	@Override
    public Collection<String> executeFilter(String pRoleToken,
            String pBusinessProcessName, String pProductName,
            String pUserLogin, String pFilterName) throws GDMException {
        FilterVisibilityConstraintData lConstraints =
                new FilterVisibilityConstraintData(StringUtils.EMPTY,
                        StringUtils.EMPTY, pProductName);
        ExecutableFilterData lFilter =
                getServiceLocator().getSearchService().retrieveFilterDataFromName(
                        pRoleToken, pFilterName, lConstraints, null);

        // Throw exception if the executable filter data doesn't exist.
        if (lFilter == null || lFilter.getResultSummaryData() == null) {
            throw new InvalidNameException(pFilterName);
        }

        FilterResultIdIterator lFilterResultIdIterator =
                getServiceLocator().getSearchService().executeFilterIdentifier(
                        pRoleToken,
                        lFilter,
                        new FilterVisibilityConstraintData(pUserLogin,
                                pBusinessProcessName, pProductName),
                        new FilterQueryConfigurator());

        return IteratorUtils.toList(lFilterResultIdIterator);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v1.Services# executeSheetFilterWithScope(String,
     *      int, int, String, String, FilterScope)
     */
    @SuppressWarnings("unchecked")
	public SheetSummaryData[] executeSheetFilterWithScope(String pRoleToken,
            int pMaxResult, int pFirstResult, String pProductName,
            String pFilterName, FilterScope pScope) throws GDMException {
        FilterVisibilityConstraintData lConstraints =
                new FilterVisibilityConstraintData(StringUtils.EMPTY,
                        StringUtils.EMPTY, pProductName);
        ExecutableFilterData lFilter =
                getServiceLocator().getSearchService().retrieveFilterDataFromName(
                        pRoleToken, pFilterName, lConstraints, pScope);
        FilterResultIterator<SheetSummaryData> lResult =
                getServiceLocator().getSearchService().executeFilter(
                        pRoleToken, lFilter, lConstraints,
                        new FilterQueryConfigurator(0, -1, 0));
        Collection<SheetSummaryData> lSheets = IteratorUtils.toList(lResult);
        return lSheets.toArray(new SheetSummaryData[lSheets.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getAttachedFileContent(java.lang.String,
     *      java.lang.String)
     */
    public byte[] getAttachedFileContent(String pRoleToken,
            String pAttachedFieldValueId) throws GDMException {
        return getServiceLocator().getSheetService().getAttachedFileContent(
                pRoleToken, pAttachedFieldValueId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getBusinessProcessNames(java.lang.String)
     */
    public String[] getBusinessProcessNames(String pUserToken)
        throws GDMException {
        Collection<String> lProcessNames =
                getServiceLocator().getAuthorizationService().getBusinessProcessNames(
                        pUserToken);
        return lProcessNames.toArray(new String[lProcessNames.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getCreatableSheetTypes(java.lang.String,
     *      java.lang.String)
     */
    public SheetType[] getCreatableSheetTypes(String pRoleToken,
            String pProcessName) throws GDMException {
        List<SheetType> lSheetTypes =
                getServiceLocator().getSheetService().getCreatableSerializableSheetTypes(
                        pRoleToken, pProcessName);
        return lSheetTypes.toArray(new SheetType[lSheetTypes.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getEnvironmentCategory(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    public EnvironmentData getEnvironmentCategory(String pRoleToken,
            String pBusinessProcessName, String pEnvironmentName,
            String pCategoryName) throws GDMException {
        return getServiceLocator().getEnvironmentService().getEnvironmentCategory(
                pRoleToken, pBusinessProcessName, pEnvironmentName,
                pCategoryName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getCategoryValues(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public String[] getCategoryValues(String pRoleToken, String pCategoryName,
            String pProcessName, String pEnvironmentName) throws GDMException {
        ArrayList<String> lCategoryNames = new ArrayList<String>();
        lCategoryNames.add(pCategoryName);
        Map<String, List<CategoryValue>> lCategoryValuesMap =
                getServiceLocator().getEnvironmentService().getCategoryValues(
                        pRoleToken, pProcessName, pEnvironmentName,
                        lCategoryNames);
        List<CategoryValue> lCategoryValuesList =
                lCategoryValuesMap.get(pCategoryName);
        ArrayList<String> lCategoryValues = new ArrayList<String>();
        if (lCategoryValuesList != null) {
            for (CategoryValue lValue : lCategoryValuesList) {
                lCategoryValues.add(lValue.getValue());
            }
        }
        return lCategoryValues.toArray(new String[lCategoryValues.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getPossibleLinkTypes(java.lang.String,
     *      java.lang.String)
     */
    public String[] getPossibleLinkTypes(String pRoleToken, String pSheetTypeId)
        throws GDMException {
        List<String> lLinkTypes =
                getServiceLocator().getSheetService().getPossibleLinkTypes(
                        pRoleToken, pSheetTypeId);
        return lLinkTypes.toArray(new String[lLinkTypes.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getSheetProcessInformations(java.lang.String,
     *      java.lang.String[])
     */
    public ProcessInformation[] getSheetProcessInformations(String pRoleToken,
            String[] pSheetIds) throws GDMException {
        if (pSheetIds == null) {
            return null;
        }
        ArrayList<ProcessInformation> lProcessInfos =
                new ArrayList<ProcessInformation>();
        for (String lSheetId : pSheetIds) {
            lProcessInfos.add(getServiceLocator().getSheetService().getSheetProcessInformation(
                    pRoleToken, lSheetId));
        }
        return lProcessInfos.toArray(new ProcessInformation[lProcessInfos.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v1.Services#getSheetHistory(java.lang.String,
     *      java.lang.String)
     */
    public SheetHistoryData[] getSheetHistory(String pRoleToken, String pSheetId)
        throws GDMException {
        return getServiceLocator().getSheetService().getSheetHistory(
                pRoleToken, pSheetId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getUserFromLogin(java.lang.String)
     */
    @WebResult(name = "user")
    public User getUserFromLogin(@WebParam(name = "login") String pLogin)
        throws GDMException {
        return getServiceLocator().getAuthorizationService().getUser(pLogin);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getSheetState(java.lang.String)
     */
    @WebResult(name = "stateName")
    public String getSheetState(@WebParam(name = "sheetId") String pSheetId)
        throws GDMException {
        return getServiceLocator().getLifeCycleService().getProcessStateName(
                pSheetId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getVisibleExecutableFilterNamesByFilterType(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      org.topcased.gpm.business.search.criterias.FilterTypeData,
     *      org.topcased.gpm.ws.v1.filter.FilterUsageEnum)
     */
    public String[] getVisibleExecutableFilterNamesByFilterType(
            String pRoleToken, String pBusinessProcessName,
            String pProductName, String pUserLogin, FilterTypeData pFilterType,
            FilterUsageEnum pUsage) throws GDMException {
        final FilterVisibilityConstraintData lFvcd =
                new FilterVisibilityConstraintData(pUserLogin,
                        pBusinessProcessName, pProductName);
        Collection<ExecutableFilterData> lFilters =
                getServiceLocator().getSearchService().getVisibleExecutableFilter(
                        pRoleToken, lFvcd, pFilterType, pUsage.getValue());
        List<String> lFiltersNames = new ArrayList<String>();
        if (lFilters == null) {
            return null;
        }
        for (ExecutableFilterData lFilter : lFilters) {
            if (lFilter.isExecutable()
                    && areSimilar(lFvcd,
                            lFilter.getFilterVisibilityConstraintData())) {
                lFiltersNames.add(lFilter.getLabelKey());
            }
        }
        return lFiltersNames.toArray(new String[lFiltersNames.size()]);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<String> getExecutableFilterNamesForContext(String pRoleToken,
            String pBusinessProcessName, String pProductName,
            String pUserLogin, FilterTypeData pFilterType,
            FilterUsageEnum pUsage, FilterScope pFilterScope)
        throws GDMException {

        final FilterVisibilityConstraintData lFvcd =
                new FilterVisibilityConstraintData(pUserLogin,
                        pBusinessProcessName, pProductName);
        final Collection<ExecutableFilterData> lFilters =
                getServiceLocator().getSearchService().getVisibleExecutableFilter(
                        pRoleToken, lFvcd, pFilterType, pUsage.getValue());
        final List<String> lFilterNames = new LinkedList<String>();
        if (lFilters == null) {
            return null;
        }

        FilterScope lFilterScope = null;
        FilterVisibilityConstraintData visibility = null;
        for (ExecutableFilterData lFilter : lFilters) {
            if (lFilter.isExecutable()) {
                visibility = lFilter.getFilterVisibilityConstraintData();
                if (visibility.getProductName() == null
                        && visibility.getUserLogin() == null) {
                    lFilterScope = FilterScope.INSTANCE_FILTER;
                }
                else if (visibility.getProductName() != null
                        && visibility.getUserLogin() == null) {
                    lFilterScope = FilterScope.PRODUCT_FILTER;
                }
                else {
                    lFilterScope = FilterScope.USER_FILTER;
                }
                if (pFilterScope != null && pFilterScope.equals(lFilterScope)) {
                    lFilterNames.add(lFilter.getLabelKey());
                }
            }
        }
        return lFilterNames;
    }

    private boolean areSimilar(FilterVisibilityConstraintData pFvcd1,
            FilterVisibilityConstraintData pFvcd2) {
        return ((pFvcd1.getBusinessProcessName() == null && pFvcd2.getBusinessProcessName() == null) || (pFvcd1.getBusinessProcessName().equals(pFvcd2.getBusinessProcessName())))
                && ((pFvcd1.getProductName() == null && pFvcd2.getProductName() == null) || (pFvcd1.getProductName().equals(pFvcd2.getProductName())))
                && ((pFvcd1.getUserLogin() == null && pFvcd2.getUserLogin() == null) || (pFvcd1.getUserLogin().equals(pFvcd2.getUserLogin())));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getVisibleFilterNamesBySheetType(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    public String[] getVisibleFilterNamesBySheetType(String pRoleToken,
            String pSheetTypeName, FilterScope pScope) {

        List<FilterData> lFilters =
                getServiceLocator().getSearchService().getVisibleFilterDatasBySheetType(
                        pRoleToken, pSheetTypeName, pScope);

        List<String> lFiltersNames = new ArrayList<String>();

        if (lFilters == null) {
            return null;
        }

        for (FilterData lFilter : lFilters) {
            lFiltersNames.add(lFilter.getLabelKey());
        }

        return lFiltersNames.toArray(new String[lFiltersNames.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#get(java.lang.String,
     *      java.lang.String[])
     */
    public AttributeData[] get(String pElemId, String[] pAttrNames)
        throws GDMException {
        return getServiceLocator().getAttributesService().get(pElemId,
                pAttrNames);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getAll(java.lang.String)
     */
    public AttributeData[] getAll(String pElemId) throws GDMException {
        return getServiceLocator().getAttributesService().getAll(pElemId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getAttrNames(java.lang.String)
     */
    public String[] getAttrNames(String pElemId) throws GDMException {
        return getServiceLocator().getAttributesService().getAttrNames(pElemId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getGlobalAttrNames()
     */
    public String[] getGlobalAttrNames() throws GDMException {
        return getServiceLocator().getAttributesService().getGlobalAttrNames();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getGlobalAttributes(java.lang.String[])
     */
    public AttributeData[] getGlobalAttributes(String[] pAttrNames)
        throws GDMException {
        return getServiceLocator().getAttributesService().getGlobalAttributes(
                pAttrNames);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#removeAll(java.lang.String)
     */
    public void removeAll(String pElemId) throws GDMException {
        getServiceLocator().getAttributesService().removeAll(pElemId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#set(java.lang.String,
     *      org.topcased.gpm.business.attributes.AttributeData[])
     */
    public void set(String pElemId, AttributeData[] pAttrs) throws GDMException {
        getServiceLocator().getAttributesService().set(pElemId, pAttrs);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#setGlobalAttributes(java.lang.String,
     *      org.topcased.gpm.business.attributes.AttributeData[])
     */
    public void setGlobalAttributes(String pRoleToken,
            AttributeData[] pAttributesData) throws GDMException {
        getServiceLocator().getAttributesService().setGlobalAttributes(
                pRoleToken, pAttributesData);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getUsersWithRole(java.lang.String,
     *      java.lang.String, java.lang.String[], boolean, boolean)
     */
    public String[] getUsersWithRole(String pBusinessProcessName,
            String pRoleName, String[] pProductNames,
            boolean pAllowInstanceRole, boolean pRoleOnAllProducts)
        throws GDMException {

        int lRoleProps = Roles.PRODUCT_ROLE;
        if (pAllowInstanceRole) {
            lRoleProps = Roles.INSTANCE_ROLE;
        }

        if (pRoleOnAllProducts) {
            lRoleProps = lRoleProps | Roles.ROLE_ON_ALL_PRODUCTS;
        }
        else {
            lRoleProps = lRoleProps | Roles.ROLE_ON_ONE_PRODUCT;
        }
        List<String> lUserLogins =
                getServiceLocator().getAuthorizationService().getUsersWithRole(
                        pBusinessProcessName, pRoleName, pProductNames,
                        lRoleProps);
        return lUserLogins.toArray(new String[0]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#addRoleForUsers(java.lang.String,
     *      java.lang.String[], java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    public void addRoleForUsers(String pRoleToken, String[] pLoginNames,
            String pRoleName, String pProductName, String pBusinessProcessName)
        throws GDMException {
        getServiceLocator().getAuthorizationService().addRoleForUsers(
                pRoleToken, pLoginNames, pRoleName, pProductName,
                pBusinessProcessName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#removeRoleForUsers(java.lang.String,
     *      java.lang.String[], java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    public void removeRoleForUsers(String pRoleToken, String[] pLoginNames,
            String pRoleName, String pProductName, String pBusinessProcessName)
        throws GDMException {
        getServiceLocator().getAuthorizationService().removeRoleForUsers(
                pRoleToken, pLoginNames, pRoleName, pProductName,
                pBusinessProcessName);
    }

    /**
     * Get information on all users. <br>
     * <b>Warning !</b> this method gets all information for all users that is
     * very long with a large amount of users <br>
     * 
     * @see org.topcased.gpm.ws.v2.Services#getAllUsers()
     * @deprecated in 3.0
     */
    public User[] getAllUsers() throws GDMException {
        return getServiceLocator().getAuthorizationService().getAllUsers().toArray(
                new User[0]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getUserFromRole(java.lang.String)
     */
    public User getUserFromRole(String pRoleToken) throws GDMException {
        return getServiceLocator().getAuthorizationService().getUserFromRole(
                pRoleToken);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getChoiceStringList(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      org.topcased.gpm.ws.context.ContextParam[])
     */
    public List<String> getChoiceStringList(String pRoleToken, String pTypeId,
            String pFieldId, ContextParam[] pCtxParams) throws GDMException {
        return getServiceLocator().getFieldsService().getChoiceStringList(
                pRoleToken, pTypeId, pFieldId, convertCtx(pCtxParams));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getChoiceStringData(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      org.topcased.gpm.ws.context.ContextParam[])
     */
    public HashMap<String, Object> getChoiceStringData(String pRoleToken,
            String pTypeId, String pFieldId, ContextParam[] pCtxParams)
        throws GDMException {
        return getServiceLocator().getFieldsService().getChoiceStringData(
                pRoleToken, pTypeId, pFieldId, convertCtx(pCtxParams));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getLinkedElementsIds(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public List<String> getLinkedElementsIds(String pRoleToken,
            String pValuesContainerId, String pLinkTypeName)
        throws GDMException {
        return getServiceLocator().getLinkService().getLinkedElementsIds(
                pValuesContainerId, pLinkTypeName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getRolesNames(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      org.topcased.gpm.business.authorization.service.RoleProperties)
     */
    public Collection<String> getRolesNamesWithProperties(String pUserToken,
            String pBusinessProcessName, String pProductName,
            RoleProperties pRoleProperties) throws GDMException {
        return getServiceLocator().getAuthorizationService().getRolesNames(
                pUserToken, pBusinessProcessName, pProductName, pRoleProperties);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getUserLanguage(java.lang.String)
     */
    public String getUserLanguage(String pToken) throws GDMException {
        return getServiceLocator().getI18nService().getPreferredLanguage(pToken);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getValuesForUser(java.lang.String,
     *      java.lang.String[])
     */
    public String[] getValuesForUser(String pToken, String[] pKeys)
        throws GDMException {
        if (pKeys == null) {
            return null;
        }
        ArrayList<String> lRes = new ArrayList<String>(pKeys.length);
        for (String lKey : pKeys) {
            lRes.add(getServiceLocator().getI18nService().getValueForUser(
                    pToken, lKey));
        }
        return lRes.toArray(new String[lRes.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#executeExtendedAction(java.lang.String,
     *      java.lang.String, java.lang.String, java.util.Collection,
     *      org.topcased.gpm.business.serialization.data.InputData)
     */
    public ExtendedActionResult executeExtendedAction(String pRoleToken,
            String pExtendedActionName, String pSheetId,
            Collection<String> pSheetIds, InputData pInputData)
        throws GDMException {
        return getServiceLocator().getExtensionsService().executeExtendedAction(
                pRoleToken, pExtendedActionName, null, pSheetId, pSheetIds,
                pInputData);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#createRevision(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public String createRevision(String pRoleToken, String pContainerId,
            String pLabel) throws GDMException {
        return getServiceLocator().getRevisionService().createRevision(
                pRoleToken, pContainerId, pLabel);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getRevisionData(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public RevisionData getRevision(String pRoleToken, String pContainerId,
            String pRevisionId) throws GDMException {
        return getServiceLocator().getRevisionService().getRevision(pRoleToken,
                pContainerId, pRevisionId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getRevisionDataByLabel(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public RevisionData getRevisionByLabel(String pRoleToken,
            String pContainerId, String pLabel) throws GDMException {
        String lRevisionId =
                getServiceLocator().getRevisionService().getRevisionIdFromRevisionLabel(
                        pRoleToken, pContainerId, pLabel);
        return getServiceLocator().getRevisionService().getRevision(pRoleToken,
                pContainerId, lRevisionId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getRevisionAsCacheableSheet(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public SheetData getSheetDataInRevision(String pRoleToken,
            String pContainerId, String pRevisionId) throws GDMException {
        return getServiceLocator().getRevisionService().getSerializableSheetInRevision(
                pRoleToken, pContainerId, pRevisionId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getSheetDataByRevisionLabel(String,
     *      String, String)
     */
    public SheetData getSheetDataByRevisionLabel(String pRoleToken,
            String pContainerId, String pRevisionLabel) throws GDMException {
        String lRevisionId =
                getServiceLocator().getRevisionService().getRevisionIdFromRevisionLabel(
                        pRoleToken, pContainerId, pRevisionLabel);
        return getServiceLocator().getRevisionService().getSerializableSheetInRevision(
                pRoleToken, pContainerId, lRevisionId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#deleteRevision(java.lang.String,
     *      java.lang.String)
     */
    public void deleteRevision(String pRoleToken, String pContainerId)
        throws GDMException {
        getServiceLocator().getRevisionService().deleteRevision(pRoleToken,
                pContainerId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getRevisionsCount(java.lang.String,
     *      java.lang.String)
     */
    public int getRevisionsCount(String pRoleToken, String pContainerId)
        throws GDMException {
        return getServiceLocator().getRevisionService().getRevisionsCount(
                pRoleToken, pContainerId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getRevisionsSummary(java.lang.String,
     *      java.lang.String)
     */
    public Collection<RevisionSummaryData> getRevisionsSummary(
            String pRoleToken, String pContainerId) throws GDMException {
        return getServiceLocator().getRevisionService().getRevisionsSummary(
                pRoleToken, pContainerId);
    }

    /**
     * Convert an array of context parameters into an extensions Context.
     * <p>
     * The extension context returned can be used in gPM API calls.
     * 
     * @param pCtxParams
     *            Array of WS context parameters
     * @return Extension context (or null if <i>pCtxParams</i> contains no
     *         context parameters)
     */
    public static org.topcased.gpm.business.extensions.service.Context convertCtx(
            org.topcased.gpm.ws.context.ContextParam[] pCtxParams) {
        if (pCtxParams != null && pCtxParams.length != 0) {
            Context lCtx = Context.getEmptyContext();
            for (ContextParam lParam : pCtxParams) {
                lCtx.put(lParam.getName(), lParam.getValue());
            }
            return lCtx;
        }
        // else
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#overrideTypes(org.topcased.gpm.business.serialization.data.ChoiceField,
     *      org.topcased.gpm.business.serialization.data.SimpleField,
     *      org.topcased.gpm.business.serialization.data.AttachedField,
     *      org.topcased.gpm.business.serialization.data.AttachedFieldValueData,
     *      org.topcased.gpm.business.serialization.data.TextDisplayHint,
     *      org.topcased.gpm.business.serialization.data.ChoiceDisplayHint,
     *      org.topcased.gpm.business.serialization.data.ChoiceTreeDisplayHint,
     *      org.topcased.gpm.business.serialization.data.ChoiceStringDisplayHint,
     *      org.topcased.gpm.business.serialization.data.AttachedDisplayHint,
     *      org.topcased.gpm.ws.context.StringParam,
     *      org.topcased.gpm.ws.context.BooleanParam,
     *      org.topcased.gpm.ws.context.IntegerParam,
     *      org.topcased.gpm.ws.context.StringsListParam,
     *      org.topcased.gpm.business.search.criterias.OperationData,
     *      org.topcased.gpm.business.scalar.BooleanValueData,
     *      org.topcased.gpm.business.scalar.DateValueData,
     *      org.topcased.gpm.business.scalar.IntegerValueData,
     *      org.topcased.gpm.business.scalar.RealValueData,
     *      org.topcased.gpm.business.scalar.StringValueData,
     *      org.topcased.gpm.business.scalar.ScalarValueData,
     *      org.topcased.gpm.business.search.criterias.CriteriaData,
     *      org.topcased.gpm.business.search.impl.fields.UsableTypeData,
     *      org.topcased.gpm.business.search.service.UsableFieldData,
     *      org.topcased.gpm.business.search.criterias.impl.VirtualFieldData,
     *      org.topcased.gpm.business.search.criterias.CriteriaFieldData,
     *      org.topcased.gpm.business.fields.PointerFieldValueData)
     */
    public void overrideTypes(ChoiceField pChoiceField,
            SimpleField pSimpleField, AttachedField pAttachedField,
            AttachedFieldValueData pAttachedFieldValueData,
            TextDisplayHint pTextDisplayHint,
            ChoiceDisplayHint pChoiceDisplayHint,
            ChoiceTreeDisplayHint pChoiceTreeDisplayHint,
            ChoiceStringDisplayHint pChoiceStringDisplayHint,
            AttachedDisplayHint pAttachedDisplayHint, StringParam pStringParam,
            BooleanParam pBoolParam, IntegerParam pIntParam,
            StringsListParam pListStrParam, OperationData pOperationData,
            BooleanValueData pBooleanValueData, DateValueData pDateValueData,
            IntegerValueData pIntegerValueData, RealValueData pRealValueData,
            StringValueData pStringValueData,
            ScalarValueData pScalarValueData1, CriteriaData pCriteriaData,
            UsableTypeData pUsableTypeData, UsableFieldData pUsableFieldData,
            VirtualFieldData pVirtualFieldData,
            CriteriaFieldData pCriteriaFieldData,
            PointerFieldValueData pPointerFieldValueData) {

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#isValuesContainerExists(java.lang.String)
     */
    public boolean isValuesContainerExists(String pValuesContainerId) {
        return getServiceLocator().getFieldsContainerService().isValuesContainerExists(
                pValuesContainerId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#importData(java.lang.String, byte[],
     *      org.topcased.gpm.business.importation.ImportProperties)
     */
    public ImportExecutionReport importData(final String pRoleToken,
            final byte[] pInputStream, final ImportProperties pProperties)
        throws GDMException, ImportException {
        return getServiceLocator().getImportService().importData(pRoleToken,
                new ByteArrayInputStream(pInputStream), pProperties,
                Context.getEmptyContext());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#exportData(java.lang.String,
     *      org.topcased.gpm.business.exportation.ExportProperties)
     */
    public byte[] exportData(final String pRoleToken,
            final ExportProperties pProperties) throws GDMException {

        // Initialize output stream byte array.
        final ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();

        // Retrieve data.
        getServiceLocator().getExportService().exportData(pRoleToken,
                lOutputStream, pProperties);

        // Return file data in byte array.
        return lOutputStream.toByteArray();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#generateSheetReport(java.lang.String,
     *      java.util.List,
     *      org.topcased.gpm.business.sheet.export.service.SheetExportFormat,
     *      java.lang.String)
     */
    public byte[] generateSheetReport(String pRoleToken,
            List<String> pSheetIds, SheetExportFormat pExportFormat,
            String pReportName) throws GDMException {

        // Retrieve the process name with role token.
        String lProcessName =
                getServiceLocator().getAuthorizationService().getProcessName(
                        pRoleToken);

        // Initialize output stream byte array.
        ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();

        // Initialise language to english, change to french if necessary.
        Locale lLocal = Locale.ENGLISH;
        String lUserLanguage = getUserLanguage(pRoleToken);
        if (lUserLanguage != null
                && lUserLanguage.equals(Locale.FRENCH.getLanguage())) {
            lLocal = Locale.FRENCH;
        }

        // Retrieve export type when exist.
        ExportType lExportType = null;
        switch (pExportFormat) {
            case PDF:
                lExportType =
                        getServiceLocator().getReportingService().getExportType(
                                ExportTypeEnum.PDF);
                break;
            case EXCEL:
                lExportType =
                        getServiceLocator().getReportingService().getExportType(
                                ExportTypeEnum.XLS);
                break;
            case XML:
                break;
            default:
                throw new GDMException(GENERATION_FORMAT_EXCEPTION_MSG);
        }

        // Generate file with specific report if exist.
        if (lExportType != null) {
            if (pSheetIds == null) {
                throw new GDMException(
                        "Cannot generate report because the result is empty.");
            }
            List<ReportModelData> lReportModelDataList =
                    getServiceLocator().getReportingService().getCompatibleModels(
                            pRoleToken, pSheetIds.toArray(new String[0]),
                            lExportType);

            // Generate specific report when find one report.
            if (lReportModelDataList.size() == 1) {
                getServiceLocator().getReportingService().generateReport(
                        pRoleToken, lProcessName, lOutputStream, pSheetIds,
                        pExportFormat, lLocal, lReportModelDataList.get(0),
                        null);
                return lOutputStream.toByteArray();
            }

            // Generate specific report with the report name specify when find more than one report.
            if (lReportModelDataList.size() > 1 && pReportName != null) {
                String lReportModelString = DUMMY_STRING;
                for (ReportModelData lReportModelData : lReportModelDataList) {
                    if (pReportName.equals(lReportModelData.getName())) {
                        getServiceLocator().getReportingService().generateReport(
                                pRoleToken, lProcessName, lOutputStream,
                                pSheetIds, pExportFormat, lLocal,
                                lReportModelData, null);
                        return lOutputStream.toByteArray();
                    }
                    lReportModelString += lReportModelData.getName() + " ; ";
                }

                // Throw an exception if the report name doesn't exist.
                throw new GDMException(GENERATION_MULTI_REPORT_MSG
                        + lReportModelString + GENERATION_REPORT_NAME_MSG
                        + pReportName);
            }

            // Continue normally if no report found.
        }

        // Generate generic report.
        getServiceLocator().getSheetService().exportSheets(pRoleToken,
                lOutputStream, pSheetIds, pExportFormat);

        // Return the file data in byte array format.
        return lOutputStream.toByteArray();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#generateFilterResultReport(java.lang.String,
     *      org.topcased.gpm.business.sheet.export.service.SheetExportFormat,
     *      java.lang.String,
     *      org.topcased.gpm.business.search.service.FilterScope)
     */
    // Iterator typing uncheck warning.
    @SuppressWarnings("unchecked")
    public byte[] generateFilterResultReport(String pRoleToken,
            SheetExportFormat pExportFormat, String pFilterName,
            FilterScope pScope) throws GDMException {

        // Initialize output stream byte array.
        ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();

        // Retrieve parameters with role token.
        String lUserLogin =
                getServiceLocator().getAuthorizationService().getUserFromRole(
                        pRoleToken).getLogin();
        String lProcessName =
                getServiceLocator().getAuthorizationService().getProcessName(
                        pRoleToken);
        String lProductName =
                getServiceLocator().getAuthorizationService().getProductName(
                        pRoleToken);

        // Retrieve the executable filter data switch filter scope.
        ExecutableFilterData lExecutableFilterData = null;
        switch (pScope) {
            case INSTANCE_FILTER:
                lExecutableFilterData =
                        getServiceLocator().getSearchService().getExecutableFilterByName(
                                pRoleToken, lProcessName, null, null,
                                pFilterName);
                break;
            case PRODUCT_FILTER:
                lExecutableFilterData =
                        getServiceLocator().getSearchService().getExecutableFilterByName(
                                pRoleToken, lProcessName, lProductName, null,
                                pFilterName);
                break;
            case USER_FILTER:
            default:
                lExecutableFilterData =
                        getServiceLocator().getSearchService().getExecutableFilterByName(
                                pRoleToken, lProcessName, null, lUserLogin,
                                pFilterName);
                break;
        }

        // Throw exception if the executable filter data doesn't exist.
        if (lExecutableFilterData == null
                || lExecutableFilterData.getResultSummaryData() == null) {
            throw new InvalidNameException(pFilterName);
        }

        // Initialize result label map with the result of the excecutable filter data.
        Map<String, String> lLabels = new HashMap<String, String>();
        UsableFieldData[] lFieldDataArray =
                lExecutableFilterData.getResultSummaryData().getUsableFieldDatas();
        for (UsableFieldData lFieldData : lFieldDataArray) {
            lLabels.put(lFieldData.getFieldName(), lFieldData.getFieldName());
        }

        // Execute the filter.
        List<SheetSummaryData> lResults =
                IteratorUtils.toList(getServiceLocator().getSearchService().executeFilter(
                        pRoleToken,
                        lExecutableFilterData,
                        new FilterVisibilityConstraintData(lUserLogin,
                                lProcessName, lProductName),
                        new FilterQueryConfigurator()));

        Context lContext = Context.getEmptyContext();
        if (lResults.size() >= getServiceLocator().getSearchService().getResultsLimit()) {
            lContext.put("limitReached", true);
        }

        // Generate data with the filter result.
        getServiceLocator().getSheetExportService().exportSheetSummaries(
                pRoleToken, lOutputStream, lLabels, lResults, pExportFormat,
                lContext);

        // Return the file data in byte array format.
        return lOutputStream.toByteArray();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#setAttachedFileContent(java.lang.String,
     *      java.lang.String, byte[])
     */
    @Override
    public void setAttachedFileContent(final String pRoleToken,
            final String pAttachedFieldValueId,
            final byte[] pAttachedFileContent) throws GDMException {
        setAttachedFileContentWithSheetId(pRoleToken, null,
                pAttachedFieldValueId, pAttachedFileContent);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#setAttachedFileContent(java.lang.String,
     *      java.lang.String, byte[])
     */
    public void setAttachedFileContentWithSheetId(final String pRoleToken,
            final String pSheetId, final String pAttachedFieldValueId,
            final byte[] pAttachedFileContent) throws GDMException {
        if (pSheetId == null) {
            getServiceLocator().getSheetService().setAttachedFileContent(
                    pRoleToken, pAttachedFieldValueId, pAttachedFileContent);
        }
        else {
            getServiceLocator().getSheetService().setAttachedFileContent(
                    pRoleToken, pSheetId, pAttachedFieldValueId,
                    pAttachedFileContent);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#createOrUpdateExecutableFilter(java.lang.String,
     *      org.topcased.gpm.business.search.service.ExecutableFilterData)
     */
    public void createOrUpdateExecutableFilter(String pRoleToken,
            ExecutableFilterData pExecutableFilterData) {
        if (pExecutableFilterData.getId() == null) {
            getServiceLocator().getSearchService().createExecutableFilter(
                    pRoleToken, pExecutableFilterData);
        }
        else {
            getServiceLocator().getSearchService().updateExecutableFilter(
                    pRoleToken, pExecutableFilterData);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#deleteExecutableFilter(java.lang.String,
     *      java.lang.String)
     */
    public void deleteExecutableFilter(String pRoleToken, String pFilterId) {
        getServiceLocator().getSearchService().removeExecutableFilter(
                pRoleToken, pFilterId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#executeFilterData(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      org.topcased.gpm.business.search.service.ExecutableFilterData)
     */
    @SuppressWarnings("unchecked")
    public FilterResult executeFilterData(String pRoleToken,
            String pBusinessProcessName, String pProductName,
            String pUserLogin, ExecutableFilterData pExecutableFilterData) {
        final FilterResultIterator<? extends SummaryData> lResultIterator =
                getServiceLocator().getSearchService().executeFilter(
                        pRoleToken,
                        pExecutableFilterData,
                        new FilterVisibilityConstraintData(pUserLogin,
                                pBusinessProcessName, pProductName),
                        new FilterQueryConfigurator());

        List<SummaryData> lExecutionResults = new ArrayList<SummaryData>();
        lExecutionResults = IteratorUtils.toList(lResultIterator);

        return new FilterResult(lExecutionResults, new FilterExecutionReport(
                lResultIterator.getExecutionReport()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getEditableFilterScope(java.lang.String)
     */
    public List<FilterScope> getEditableFilterScope(String pRoleToken) {
        return getServiceLocator().getAuthorizationService().getEditableFilterScope(
                pRoleToken);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getExecutableFilter(java.lang.String,
     *      java.lang.String)
     */
    public ExecutableFilterData getExecutableFilter(String pRoleToken,
            String pFilterId) {
        return getServiceLocator().getSearchService().getExecutableFilter(pRoleToken, pFilterId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getExecutableFilterByName(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    public ExecutableFilterData getExecutableFilterByName(String pRoleToken,
            String pProcessName, String pFilterProductName,
            String pFilterUserLogin, String pFilterName) {
        return getServiceLocator().getSearchService().getExecutableFilterByName(
                pRoleToken, pProcessName, pFilterProductName, pFilterUserLogin,
                pFilterName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getExecutableFilterScope(java.lang.String)
     */
    public List<FilterScope> getExecutableFilterScope(String pRoleToken) {
        return getServiceLocator().getAuthorizationService().getExecutableFilterScope(
                pRoleToken);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getFilterFieldsMaxDepth()
     */
    public int getFilterFieldsMaxDepth() {
        return getServiceLocator().getSearchService().getMaxFieldsDepth();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getSearcheableContainers(java.lang.String,
     *      FilterTypeEnum)
     */
    public List<String> getSearcheableContainers(String pRoleToken,
            FilterTypeData pFilterType) {
        List<CacheableFieldsContainer> lContainers =
                new ArrayList<CacheableFieldsContainer>();

        // Get all available containers for type in argument
        if (pFilterType.toFilterType() == FilterType.SHEET) {
            lContainers.addAll(getServiceLocator().getFieldsContainerService().getFieldsContainer(
                    pRoleToken,
                    org.topcased.gpm.business.serialization.data.SheetType.class,
                    0));
        }
        else if (pFilterType.toFilterType() == FilterType.PRODUCT) {
            lContainers.addAll(getServiceLocator().getFieldsContainerService().getFieldsContainer(
                    pRoleToken,
                    org.topcased.gpm.business.serialization.data.ProductType.class,
                    0));
        }
        else if (pFilterType.toFilterType() == FilterType.LINK) {
            lContainers.addAll(getServiceLocator().getFieldsContainerService().getFieldsContainer(
                    pRoleToken,
                    org.topcased.gpm.business.serialization.data.LinkType.class,
                    0));
        }

        // Remove unauthorized containers from list
        Iterator<CacheableFieldsContainer> lIte = lContainers.iterator();
        List<String> lAuthorizedContainersIds = new ArrayList<String>();
        while (lIte.hasNext()) {
            String lContainerId = lIte.next().getId();
            if (getServiceLocator().getAuthorizationService().getFilterAccessControl(
                    pRoleToken, lContainerId).getEditable()) {
                lAuthorizedContainersIds.add(lContainerId);
            }
        }

        return lAuthorizedContainersIds;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getSearcheableFieldsLabel(java.lang.String,
     *      List)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<String> getSearcheableFieldsLabel(String pRoleToken,
            List<String> pContainerIds) {
        HashSet<String> lSearcheableFields = null;
        Set<String> lCurrentContainerFields = new HashSet<String>();

        Iterator<String> lContainerIte = pContainerIds.iterator();
        while (lContainerIte.hasNext()) {
            String lContainerID = lContainerIte.next();
            CacheableFieldsContainer lContainer =
                    getServiceLocator().getFieldsService().getCacheableFieldsContainer(
                            pRoleToken, lContainerID, CacheProperties.IMMUTABLE);
            Collection<? extends Field> lFields = lContainer.getAllFields();

            // Fill the fields label set of the current container 
            for (Field lField : lFields) {
                FilterAccessControl lAccess =
                        getServiceLocator().getAuthorizationService().getFilterAccessControl(
                                pRoleToken, lContainerID, lField.getLabelKey());
                if (lAccess == null || lAccess.getEditable()) {
                    lCurrentContainerFields.add(lField.getLabelKey());
                }
            }
            // Add virtual fields of the current container depending on its type
            // LINK
            if (lContainer instanceof CacheableLinkType) {
                for (String lFieldLabel : UsableFieldsManager.LINK_VIRTUAL_FIELDS) {
                    FilterAccessControl lAccess =
                            getServiceLocator().getAuthorizationService().getFilterAccessControl(
                                    pRoleToken, lContainerID, lFieldLabel);
                    if (lAccess == null || lAccess.getEditable()) {
                        lCurrentContainerFields.add(lFieldLabel);
                    }
                }
            }
            // SHEET
            else if (lContainer instanceof CacheableSheetType) {
                for (String lFieldLabel : UsableFieldsManager.SHEET_VIRTUAL_FIELDS) {
                    FilterAccessControl lAccess =
                            getServiceLocator().getAuthorizationService().getFilterAccessControl(
                                    pRoleToken, lContainerID, lFieldLabel);
                    if (lAccess == null || lAccess.getEditable()) {
                        lCurrentContainerFields.add(lFieldLabel);
                    }
                }
            }
            // If first iteration, initialize the result set
            if (lSearcheableFields == null) {
                lSearcheableFields =
                        new HashSet<String>(lCurrentContainerFields);
            }
            // Else make the intersection of "current labels" set and "result labels" set
            else {
                lSearcheableFields.retainAll(lCurrentContainerFields);
            }
            lCurrentContainerFields.clear();
        }

        return new ArrayList(lSearcheableFields);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getAvailableExtendedActions(java.lang.String,
     *      org.topcased.gpm.ws.v2.extensions.GuiContext, java.lang.String)
     */
    public List<ExtendedActionSummary> getAvailableExtendedActions(
            String pRoleToken, List<GuiContext> pGuiContexts,
            String pContainerId) throws GDMException {

        // Get the container type ID
        String lContainerTypeId;
        if (pContainerId == null || StringUtils.isBlank(pContainerId)) {
            lContainerTypeId =
                    getServiceLocator().getInstanceService().getBusinessProcessId(
                            getServiceLocator().getAuthorizationService().getProcessName(
                                    pRoleToken));
        }
        else {
            lContainerTypeId =
                    getServiceLocator().getFieldsContainerService().getFieldsContainerIdByValuesContainer(
                            pRoleToken, pContainerId);
        }

        // Get the Gui context
        List<GUIContext> lGuiContexts =
                new ArrayList<GUIContext>(pGuiContexts.size());
        for (GuiContext lGuiContext : pGuiContexts) {
            lGuiContexts.add(GUIContext.fromString(lGuiContext.toString()));
        }

        // Get extended actions for the container
        List<ExtendedActionData> lExtendedActionDatas =
                getServiceLocator().getExtensionsService().getAvailableExtendedActions(
                        lContainerTypeId, lGuiContexts);

        ArrayList<ExtendedActionSummary> lExtendedActionSummaries =
                new ArrayList<ExtendedActionSummary>(
                        lExtendedActionDatas.size());

        for (ExtendedActionData lExtendedActionData : lExtendedActionDatas) {
            // Get action access of the extended action
            String lMenuName =
                    lExtendedActionData.getMenuEntryParentName() + "."
                            + lExtendedActionData.getMenuEntryName();
            ActionAccessControlData lActionAccessControlData =
                    getServiceLocator().getAuthorizationService().getApplicationActionAccessControl(
                            pRoleToken, pContainerId, lMenuName);

            // Add extended action to the final list if action not confidential and enabled
            if (lActionAccessControlData.getEnabled()
                    && !lActionAccessControlData.getConfidential()) {
                ExtendedActionSummary lExtendedActionSummary =
                        new ExtendedActionSummary();
                lExtendedActionSummary.setExtendedActionName(lExtendedActionData.getName());

                if (lExtendedActionData.getInputDataTypeName() != null) {
                    CacheableInputDataType lCacheableInputDataType =
                            getServiceLocator().getFieldsService().getCacheableInputDataTypeByName(
                                    pRoleToken,
                                    lExtendedActionData.getInputDataTypeName(),
                                    getServiceLocator().getAuthorizationService().getProcessName(
                                            pRoleToken),
                                    CacheProperties.IMMUTABLE);
                    lExtendedActionSummary.setInputDataTypeId(lCacheableInputDataType.getId());
                }

                lExtendedActionSummary.setGuiContexts(new ArrayList<GuiContext>(
                        lExtendedActionData.getContexts().size()));
                for (GUIContext lGuiContext : lExtendedActionData.getContexts()) {
                    lExtendedActionSummary.getGuiContexts().add(
                            GuiContext.valueOf(lGuiContext.getValue()));
                }

                lExtendedActionSummary.setMenuEntryName(lExtendedActionData.getMenuEntryName());
                lExtendedActionSummary.setMenuEntryParentName(lExtendedActionData.getMenuEntryParentName());

                lExtendedActionSummaries.add(lExtendedActionSummary);
            }
        }

        return lExtendedActionSummaries;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getInputDataType(java.lang.String,
     *      java.lang.String)
     */
    public InputDataType getInputDataType(String pRoleToken,
            String pInputDataTypeId) throws GDMException {
        return getServiceLocator().getFieldsService().getInputDataType(
                pRoleToken, pInputDataTypeId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getInputDataModel(java.lang.String,
     *      java.lang.String)
     */
    public InputData getInputDataModel(String pRoleToken,
            String pInputDataTypeId) throws GDMException {

        CacheableInputDataType lCacheableInputDataType =
                getServiceLocator().getFieldsService().getCacheableInputDataType(
                        pRoleToken, pInputDataTypeId, CacheProperties.IMMUTABLE);
        CacheableInputData lCacheableInputData =
                getServiceLocator().getFieldsService().getInputDataModel(
                        pRoleToken, lCacheableInputDataType,
                        Context.getEmptyContext());

        InputData lInputData = new InputData();
        lCacheableInputData.marshal(lInputData);
        return lInputData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#executeExtendedActionByContainer(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.util.List,
     *      org.topcased.gpm.business.serialization.data.InputData)
     */
    public ExtendedActionResult executeExtendedActionByContainer(
            String pRoleToken, String pExtendedActionName,
            String pExtensionsContainerTypeId, String pSheetId,
            List<String> pSheetIds, InputData pInputData) throws GDMException {
        return getServiceLocator().getExtensionsService().executeExtendedAction(
                pRoleToken, pExtendedActionName, pExtensionsContainerTypeId,
                pSheetId, pSheetIds, pInputData);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getAvailableReportModels(java.lang.String,
     *      java.util.Collection,
     *      org.topcased.gpm.business.sheet.export.service.SheetExportFormat)
     */
    public List<String> getAvailableReportModels(String pRoleToken,
            Collection<String> pSheetIds, SheetExportFormat pExportFormat)
        throws GDMException {
        ExportType lExportType =
                getServiceLocator().getReportingService().getExportType(
                        pExportFormat.getExtension());

        List<ReportModelData> lReportModelDatas =
                getServiceLocator().getReportingService().getCompatibleModels(
                        pRoleToken, pSheetIds.toArray(new String[0]),
                        lExportType);

        List<String> lReportModelNames = new ArrayList<String>();
        for (ReportModelData lReportModelData : lReportModelDatas) {
            lReportModelNames.add(lReportModelData.getName());
        }
        return lReportModelNames;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getLinkType(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public LinkType getLinkType(String pRoleToken, String pLinkTypeId,
            String pDisplayedContainerId) throws GDMException {
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData(
                        CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                        CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                        CacheProperties.DEFAULT_ACCESS_CONTROL_USED,
                        pLinkTypeId, pDisplayedContainerId,
                        CacheProperties.DEFAULT_ACCESS_CONTROL_USED);
        CacheableLinkType lCacheableLinkType =
                getServiceLocator().getLinkService().getLinkType(pRoleToken,
                        pLinkTypeId,
                        new CacheProperties(false, lAccessControlContextData));

        LinkType lLinkType = new LinkType();
        lCacheableLinkType.marshal(lLinkType);
        return lLinkType;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getActionAccessControls(java.lang.String,
     *      org.topcased.gpm.business.authorization.service.AccessControlContextData,
     *      java.util.List)
     */
    public List<ActionAccessControlData> getActionAccessControls(
            String pRoleToken,
            AccessControlContextData pAccessControlContextData,
            List<String> pActionNames) throws GDMException {

        AccessControlContextData lAccd =
                new AccessControlContextData(pAccessControlContextData);

        if (lAccd.getRoleName() == null) {
            lAccd.setRoleName(CacheProperties.DEFAULT_ACCESS_CONTROL_USED);
        }
        if (lAccd.getProductName() == null) {
            lAccd.setProductName(CacheProperties.DEFAULT_ACCESS_CONTROL_USED);
        }
        if (lAccd.getContainerTypeId() == null) {
            lAccd.setContainerTypeId(CacheProperties.DEFAULT_ACCESS_CONTROL_USED);
        }
        if (lAccd.getStateName() == null) {
            lAccd.setStateName(CacheProperties.DEFAULT_ACCESS_CONTROL_USED);
        }
        if (lAccd.getValuesContainerId() == null) {
            lAccd.setValuesContainerId(CacheProperties.DEFAULT_ACCESS_CONTROL_USED);
        }
        if (lAccd.getVisibleTypeId() == null) {
            lAccd.setVisibleTypeId(CacheProperties.DEFAULT_ACCESS_CONTROL_USED);
        }

        return getServiceLocator().getAuthorizationService().getApplicationActionAccessControl(
                pRoleToken, lAccd, pActionNames);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getCategoryValues(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public List<String> getCategoryValuesByProduct(String pRoleToken,
            String pProductName, String pCategoryName) throws GDMException {
        CacheableProduct lCacheableProduct =
                getServiceLocator().getProductService().getCacheableProduct(
                        pRoleToken,
                        getServiceLocator().getProductService().getProductId(
                                pRoleToken, pProductName),
                        CacheProperties.IMMUTABLE);

        List<CategoryValue> lCategoryValues =
                getServiceLocator().getEnvironmentService().getCategoryValues(
                        pRoleToken,
                        getServiceLocator().getAuthorizationService().getProcessName(
                                pRoleToken),
                        lCacheableProduct.getEnvironmentNames(),
                        Arrays.asList(pCategoryName)).get(pCategoryName);

        ArrayList<String> lValues =
                new ArrayList<String>(lCategoryValues.size());
        for (CategoryValue lCategoryValue : lCategoryValues) {
            lValues.add(lCategoryValue.getValue());
        }

        return lValues;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getAllVirtualFieldTypes()
     */
    public List<String> getAllVirtualFieldTypes() {
        ArrayList<String> lList = new ArrayList<String>();
        for (VirtualFieldType lType : VirtualFieldType.values()) {
            lList.add(lType.getValue());
        }
        lList.add(UsableFieldsManager.NOT_SPECIFIED);
        lList.add(UsableFieldsManager.CURRENT_PRODUCT);
        lList.add(UsableFieldsManager.CURRENT_USER_LOGIN);
        lList.add(UsableFieldsManager.CURRENT_USER_NAME);
        return lList;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getProductsAsTree(java.lang.String,
     *      java.lang.String)
     */
    public List<ProductNode> getProductsAsTree(String pUserToken,
            String pProcessName) {
        // Get Available Products
        List<String> lAvailableProductNames =
                new ArrayList<String>(
                        getServiceLocator().getAuthorizationService().getProductNames(
                                pUserToken, pProcessName));
        // Get All Products
        List<String> lAllProducts =
                getServiceLocator().getAuthorizationService().getAllProductNames(
                        pProcessName);

        // Create Product Hierarchy
        List<String> lTopLevelProducts = new LinkedList<String>();
        Map<String, ProductNode> lProductHierarchies =
                new HashMap<String, ProductNode>();

        for (String lProductName : lAllProducts) {
            if (!lProductHierarchies.containsKey(lProductName)) {
                lProductHierarchies.put(lProductName, getProductHierarchy(
                        lProductName, pProcessName, lAvailableProductNames,
                        lTopLevelProducts, lProductHierarchies));
            }
        }

        List<ProductNode> lResult = new ArrayList<ProductNode>();
        // Sort top level products
        Collections.sort(lTopLevelProducts);
        for (String lTopLevelProduct : lTopLevelProducts) {
            ProductNode lHierarchy = lProductHierarchies.get(lTopLevelProduct);
            // Remove not reachable products
            cleanDisabledProducts(lHierarchy);
            // Add this product only if itself or one of his children is enabled 
            if ((lHierarchy.getChildren() != null && !lHierarchy.getChildren().isEmpty())
                    || lHierarchy.isEnabled()) {
                lResult.add(lHierarchy);
            }
        }

        return lResult;
    }

    /**
     * Remove products from Hierarchy when
     * 
     * @param pHierarchy
     */
    private void cleanDisabledProducts(ProductNode pHierarchy) {
        List<ProductNode> lChildren = pHierarchy.getChildren();
        List<ProductNode> lChildrenToRemove = new ArrayList<ProductNode>();
        // For all children of the parent
        for (ProductNode lChild : lChildren) {
            // Clean this child and his children
            cleanDisabledProducts(lChild);
            // Check if this child has children
            if (lChild.getChildren() == null || lChild.getChildren().isEmpty()) {
                if (!lChild.isEnabled()) {
                    // No children and not enabled
                    lChildrenToRemove.add(lChild);
                }
            }
        }
        if (!lChildrenToRemove.isEmpty()) {
            lChildren.removeAll(lChildrenToRemove);
        }
    }

    /**
     * Get the product hierarchy for the product in parameter
     * 
     * @param pProductName
     *            the product
     * @param pProcessName
     *            the process to search
     * @param pAvailableProductNames
     *            list to fill
     * @param pTopLevelProducts
     *            top level products (root leaves of product tree)
     * @param pProductHierarchies
     *            the Product hierarchy map
     * @return The ProductNode object representing the product hierarchy for the
     *         product name in parameter
     */
    private ProductNode getProductHierarchy(String pProductName,
            String pProcessName, List<String> pAvailableProductNames,
            List<String> pTopLevelProducts,
            Map<String, ProductNode> pProductHierarchies) {

        boolean lEnabled = pAvailableProductNames.contains(pProductName);

        ProductNode lProductHierarchy =
                new ProductNode(pProductName, new ArrayList<ProductNode>(),
                        lEnabled);

        // Get parents
        List<String> lProductParents =
                getServiceLocator().getProductService().getProductParentsNames(
                        pProcessName, pProductName);

        if (lProductParents == null || lProductParents.isEmpty()) {
            pTopLevelProducts.add(lProductHierarchy.getProductName());
        }
        else {
            for (String lParentName : lProductParents) {
                if (!pProductHierarchies.containsKey(lParentName)) {
                    pProductHierarchies.put(lParentName, getProductHierarchy(
                            lParentName, pProcessName, pAvailableProductNames,
                            pTopLevelProducts, pProductHierarchies));
                }
                pProductHierarchies.get(lParentName).getChildren().add(
                        lProductHierarchy);
                Collections.sort(pProductHierarchies.get(lParentName).getChildren());
            }
        }

        return lProductHierarchy;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#getAvailableExtendedActionsForExtensionContainer(java.lang.String,
     *      java.util.List, java.lang.String)
     */
    public List<ExtendedActionSummary> getAvailableExtendedActionsForExtensionContainer(
            String pRoleToken, List<GuiContext> pGuiContexts,
            String pExtensionContainerId) {

        // Get the Gui context
        List<GUIContext> lGuiContexts =
                new ArrayList<GUIContext>(pGuiContexts.size());
        for (GuiContext lGuiContext : pGuiContexts) {
            lGuiContexts.add(GUIContext.fromString(lGuiContext.toString()));
        }

        // Get extended actions for the container
        List<ExtendedActionData> lExtendedActionDatas =
                getServiceLocator().getExtensionsService().getAvailableExtendedActions(
                        pExtensionContainerId, lGuiContexts);

        ArrayList<ExtendedActionSummary> lExtendedActionSummaries =
                new ArrayList<ExtendedActionSummary>(
                        lExtendedActionDatas.size());

        for (ExtendedActionData lExtendedActionData : lExtendedActionDatas) {
            // Get action access of the extended action
            String lMenuName =
                    lExtendedActionData.getMenuEntryParentName() + "."
                            + lExtendedActionData.getMenuEntryName();

            ActionAccessControlData lActionAccessControlData =
                    serviceLocator.getAuthorizationService().getApplicationActionAccessControlForType(
                            pRoleToken, pExtensionContainerId, lMenuName);

            // Add extended action to the final list if action not confidential and enabled
            if (lActionAccessControlData.getEnabled()
                    && !lActionAccessControlData.getConfidential()) {
                ExtendedActionSummary lExtendedActionSummary =
                        new ExtendedActionSummary();
                lExtendedActionSummary.setExtendedActionName(lExtendedActionData.getName());

                if (lExtendedActionData.getInputDataTypeName() != null) {
                    CacheableInputDataType lCacheableInputDataType =
                            getServiceLocator().getFieldsService().getCacheableInputDataTypeByName(
                                    pRoleToken,
                                    lExtendedActionData.getInputDataTypeName(),
                                    getServiceLocator().getAuthorizationService().getProcessName(
                                            pRoleToken),
                                    CacheProperties.IMMUTABLE);
                    lExtendedActionSummary.setInputDataTypeId(lCacheableInputDataType.getId());
                }

                lExtendedActionSummary.setGuiContexts(new ArrayList<GuiContext>(
                        lExtendedActionData.getContexts().size()));
                for (GUIContext lGuiContext : lExtendedActionData.getContexts()) {
                    lExtendedActionSummary.getGuiContexts().add(
                            GuiContext.valueOf(lGuiContext.getValue()));
                }

                lExtendedActionSummary.setMenuEntryName(lExtendedActionData.getMenuEntryName());
                lExtendedActionSummary.setMenuEntryParentName(lExtendedActionData.getMenuEntryParentName());

                lExtendedActionSummaries.add(lExtendedActionSummary);
            }
        }

        return lExtendedActionSummaries;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.v2.Services#initializeSheet(String, String,
     *      String)
     */
    public SheetData initializeSheet(final String pRoleToken,
            final String pResultSheetTypeId, final String pSourceSheetId)
        throws GDMException {

        // result
        SheetData lSheetData = null;
        final CacheableSheet lCacheableSheet =
                getServiceLocator().getSheetService().getCacheableSheetInitializationModel(
                        pRoleToken, pResultSheetTypeId, pSourceSheetId);
        // Conversion of the cacheable sheet into a serializable sheetData.
        final Object lMarshalizedCacheableSheet = lCacheableSheet.marshal();
        if (lMarshalizedCacheableSheet instanceof SheetData) {
            lSheetData = (SheetData) lMarshalizedCacheableSheet;
        }
        else {
            throw new GDMException(
                    "Unable to retrieve the right sheet in database");
        }
        return lSheetData;
    }
    
    /**
     * {@inheritDoc}
     */
    public SheetData duplicateSheet(final String pRoleToken,
            final String pSheetId)
        throws GDMException {

        // result
        SheetData lSheetData = null;

        final SheetService lSheetService = getServiceLocator().getSheetService();
        final CacheableSheet lCacheableSheet =
            lSheetService.getCacheableSheetDuplicationModel(pRoleToken,
                        pSheetId);
        // Conversion of the cacheable sheet into a serializable sheetData.
        Object lMarshalizedCacheableSheet = lCacheableSheet.marshal();
        if (lMarshalizedCacheableSheet instanceof SheetData) {
            lSheetData = (SheetData) lMarshalizedCacheableSheet;
        }
        else {
            throw new GDMException("Unable to duplicate the sheet.");
        }
        return lSheetData;
    }

    /**
     * {@inheritDoc}
     */
    public List<ActionAccessControlData> getActionAccessControlsByProductAndRole(
            final String pRoleToken,
            final AccessControlContextData pAccessControlContextData,
            final List<String> pActionNames) throws GDMException {

        final List<ActionAccessControlData> lResult =
                new ArrayList<ActionAccessControlData>();
        final AuthorizationService lAuthorizationService =
                getServiceLocator().getAuthorizationService();
        final String lProcessName =
                lAuthorizationService.getProcessName(pRoleToken);
        //Getting all sheet types.
        final List<CacheableSheetType> lSheetTypes =
                getServiceLocator().getSheetService().getSheetTypes(pRoleToken,
                        lProcessName, CacheProperties.IMMUTABLE);
        //We need all combinations of sheet types / sheet states.

        for (CacheableSheetType lSheetType : lSheetTypes) {
            final String lSheetTypeId = lSheetType.getId();
            //Getting all sheet States.
            final List<String> lSheetStates =
                    new ArrayList<String>(
                            getServiceLocator().getLifeCycleService().getAllStateNames(
                                    lSheetTypeId));
            for (String lSheetState : lSheetStates) {
                final AccessControlContextData lAccd =
                        new AccessControlContextData(pAccessControlContextData);
                lAccd.setContainerTypeId(lSheetTypeId);
                lAccd.setStateName(lSheetState);
                //Default values
                lAccd.setValuesContainerId(CacheProperties.DEFAULT_ACCESS_CONTROL_USED);
                lAccd.setVisibleTypeId(CacheProperties.DEFAULT_ACCESS_CONTROL_USED);

                //Getting the access controls.
                final List<ActionAccessControlData> lAccessControlsForThisProductRoleTypeState =
                        lAuthorizationService.getApplicationActionAccessControl(
                                pRoleToken, lAccd, pActionNames);
                //Adding the access controls we just retrieved to the result list.
                lResult.addAll(lAccessControlsForThisProductRoleTypeState);
            }
            //Adding the access controls for the cases when the sheet state is null. 
            AccessControlContextData lAccd =
                    new AccessControlContextData(pAccessControlContextData);
            lAccd.setContainerTypeId(lSheetTypeId);
            lAccd.setStateName(CacheProperties.DEFAULT_ACCESS_CONTROL_USED);
            lAccd.setValuesContainerId(CacheProperties.DEFAULT_ACCESS_CONTROL_USED);
            lAccd.setVisibleTypeId(CacheProperties.DEFAULT_ACCESS_CONTROL_USED);
            final List<ActionAccessControlData> lAccessControlsForThisProductRoleType =
                    lAuthorizationService.getApplicationActionAccessControl(
                            pRoleToken, lAccd, pActionNames);
            lResult.addAll(lAccessControlsForThisProductRoleType);
        }
        //Adding the access controls for the cases when the sheet type is null
        final AccessControlContextData lAccd =
                new AccessControlContextData(pAccessControlContextData);
        lAccd.setContainerTypeId(CacheProperties.DEFAULT_ACCESS_CONTROL_USED);
        lAccd.setStateName(CacheProperties.DEFAULT_ACCESS_CONTROL_USED);
        lAccd.setValuesContainerId(CacheProperties.DEFAULT_ACCESS_CONTROL_USED);
        lAccd.setVisibleTypeId(CacheProperties.DEFAULT_ACCESS_CONTROL_USED);
        final List<ActionAccessControlData> lAccessControlsForThisProductRole =
                lAuthorizationService.getApplicationActionAccessControl(
                        pRoleToken, lAccd, pActionNames);
        lResult.addAll(lAccessControlsForThisProductRole);

        return lResult;
    }
    
    
    /**
     * Obtain the pointed field value from the pointer field label, and
     * references to pointed field.
     * 
     * @param pRoleToken
     *            session token
     * @param pFieldLabel
     *            pointer field label
     * @param pReferencedContainerId
     *            ID of pointed container
     * @param pReferencedFieldLabel
     *            pointed field label
     * @return the pointed field value object (as a list of
     *         <code>&lt;FieldValueData&gt;</code>) :
     *         <ul>
     *         <li>If the pointed field value is null => returns
     *         <code>null</code></li>
     *         <li>If the pointed field value is a single field value data =>
     *         returns a list with 1 element</li>
     *         <li>If the pointed field value is a list of field value datas =>
     *         returns the list</li>
     *         <li>If the pointed field value is a map of
     *         <code>&lt;String, List&lt;FieldValueData&gt;&gt;</code> (for a
     *         multiple field)=> returns the union of all FieldValueData lists</li>
     *         </ul>
     */
    @SuppressWarnings("unchecked")
	public List<FieldValueData> getPointedFieldValue(final String pRoleToken,
            final String pFieldLabel, final String pReferencedContainerId,
            final String pReferencedFieldLabel) {

        final Object lPointedFieldValue =
                serviceLocator.getFieldsService().getPointedFieldValue(
                        pRoleToken, pFieldLabel, pReferencedContainerId,
                        pReferencedFieldLabel);

        if (lPointedFieldValue == null) {
            return null;
        }
        List<FieldValueData> lFieldValueDatas = null;
        if (lPointedFieldValue instanceof List<?>) {
            lFieldValueDatas = (List<FieldValueData>) lPointedFieldValue;
        }
        else if (lPointedFieldValue instanceof FieldValueData) {
            lFieldValueDatas = new ArrayList<FieldValueData>(1);
            lFieldValueDatas.add((FieldValueData) lPointedFieldValue);
        }
        else if (lPointedFieldValue instanceof Map<?, ?>) {
            lFieldValueDatas = new ArrayList<FieldValueData>(1);
            final Map<String, List<FieldValueData>> lMap =
                    (Map<String, List<FieldValueData>>) lPointedFieldValue;
            for (List<FieldValueData> lList : lMap.values()) {
                lFieldValueDatas.addAll(lList);
            }
        }
        return lFieldValueDatas;
    }

}
