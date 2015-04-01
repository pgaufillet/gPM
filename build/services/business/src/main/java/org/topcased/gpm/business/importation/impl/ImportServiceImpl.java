/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Atos Origin
 ******************************************************************/
package org.topcased.gpm.business.importation.impl;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.authorization.impl.RoleSelector;
import org.topcased.gpm.business.exception.ExtensionException;
import org.topcased.gpm.business.exception.FieldValidationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.exception.SchemaValidationException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.importation.ImportExecutionReport;
import org.topcased.gpm.business.importation.ImportProperties;
import org.topcased.gpm.business.importation.impl.dictionary.CategoryImportManager;
import org.topcased.gpm.business.importation.impl.dictionary.EnvironmentImportManager;
import org.topcased.gpm.business.importation.impl.product.ProductImportManager;
import org.topcased.gpm.business.importation.impl.product.ProductLinkImportManager;
import org.topcased.gpm.business.importation.impl.report.ElementType;
import org.topcased.gpm.business.importation.impl.sheet.SheetImportManager;
import org.topcased.gpm.business.importation.impl.sheet.SheetLinkImportManager;
import org.topcased.gpm.business.importation.service.ImportService;
import org.topcased.gpm.business.serialization.converter.XMLConverter;
import org.topcased.gpm.business.serialization.data.Category;
import org.topcased.gpm.business.serialization.data.Dictionary;
import org.topcased.gpm.business.serialization.data.Environment;
import org.topcased.gpm.business.serialization.data.ExcludeData;
import org.topcased.gpm.business.serialization.data.ExtensionPointsToExcludeData;
import org.topcased.gpm.business.serialization.data.Filter;
import org.topcased.gpm.business.serialization.data.Link;
import org.topcased.gpm.business.serialization.data.Product;
import org.topcased.gpm.business.serialization.data.ProductLink;
import org.topcased.gpm.business.serialization.data.RuleData;
import org.topcased.gpm.business.serialization.data.SheetData;
import org.topcased.gpm.business.serialization.data.SheetFilter;
import org.topcased.gpm.business.serialization.data.SheetLink;
import org.topcased.gpm.business.serialization.data.User;
import org.topcased.gpm.business.serialization.data.UserRole;
import org.topcased.gpm.business.serialization.data.ValuesContainerData;
import org.topcased.gpm.domain.accesscontrol.EndUser;
import org.topcased.gpm.util.xml.SchemaValidator;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;

/**
 * The Class ImportServiceImpl.
 * 
 * @author llatil
 */
public class ImportServiceImpl extends ServiceImplBase implements ImportService {

    /** Logger for the class. */
    private static final Logger LOGGER =
            Logger.getLogger(ImportServiceImpl.class);

    private static Set<String> staticSupportedTags = new HashSet<String>();

    static {
        staticSupportedTags.add(SUPPORTED_TAG_PRODUCTS);
        staticSupportedTags.add(SUPPORTED_TAG_PRODUCT_LINKS);
        staticSupportedTags.add(SUPPORTED_TAG_SHEETS);
        staticSupportedTags.add(SUPPORTED_TAG_SHEET_LINKS);
        staticSupportedTags.add(SUPPORTED_TAG_USERS);
        staticSupportedTags.add(SUPPORTED_TAG_USER_ROLES);
        staticSupportedTags.add(SUPPORTED_TAG_FILTERS);
        staticSupportedTags.add(SUPPORTED_TAG_CATEGORIES);
        staticSupportedTags.add(SUPPORTED_TAG_ENVIRONMENTS);
        staticSupportedTags.add(SUPPORTED_TAG_RULE);
    }

    private static Set<String> staticSupportedContainerTags =
            new HashSet<String>();

    static {
        staticSupportedContainerTags.add("dictionary");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.service.ImportService#importData(java.lang.String,
     *      java.io.InputStream,
     *      org.topcased.gpm.business.importation.ImportProperties,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    public ImportExecutionReport importData(final String pRoleToken,
            final InputStream pInputStream, final ImportProperties pProperties,
            final Context pContext) throws SchemaValidationException,
        ImportException {

        // Validate XML
        BufferedInputStream lInputStream =
                new BufferedInputStream(pInputStream);
        SchemaValidator lSchemaValidator = new SchemaValidator();
        lSchemaValidator.validate(lInputStream, Boolean.FALSE);

        XMLConverter lConverter = new XMLConverter(lInputStream);

        HierarchicalStreamReader lHierarchicalReader =
                lConverter.createHierarchicalStreamReader();

        ImportExecutionReport lReport = new ImportExecutionReport();
        try {
            importInstance(pRoleToken, pProperties, lReport, pContext,
                    lConverter, lHierarchicalReader);
        }
        catch (FieldValidationException ex) {
            throw new ImportException(ex.getUserMessage(), null);
        }

        return lReport;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.service.ImportService#importSheet(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.SheetData,
     *      org.topcased.gpm.business.importation.ImportProperties,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    public ImportExecutionReport importSheet(final String pRoleToken,
            final SheetData pSheet, final ImportProperties pProperties,
            final Context pContext) throws ImportException {
        return sheetImportManager.importData(pRoleToken, pSheet, pProperties,
                pContext);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.service.ImportService#importLink(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.Link,
     *      org.topcased.gpm.business.importation.ImportProperties,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    public ImportExecutionReport importLink(final String pRoleToken,
            final Link pLink, final ImportProperties pProperties,
            final Context pContext) throws ImportException {
        final ImportExecutionReport lReport;

        if (pLink instanceof SheetLink) {
            lReport =
                    sheetLinkImportManager.importData(pRoleToken,
                            (SheetLink) pLink, pProperties, pContext);
        }
        else if (pLink instanceof ProductLink) {
            lReport =
                    productLinkImportManager.importData(pRoleToken,
                            (ProductLink) pLink, pProperties, pContext);
        }
        else {
            lReport = new ImportExecutionReport();
            lReport.add(ElementType.LINK,
                    "Only sheet' link and product's link can be imported.");
        }

        return lReport;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.service.ImportService#importLinks(java.lang.String,
     *      java.util.Collection,
     *      org.topcased.gpm.business.extensions.service.Context)
     * @deprecated
     */
    public void importLinks(String pRoleToken, Collection<Link> pLinks,
            Context pCtx) {
        Context lLocalCtx = Context.createContext(pCtx);

        lLocalCtx.put(Context.GPM_SKIP_EXT_PTS, Boolean.TRUE);

        for (Link lSerializableLink : pLinks) {
            try {
                importLink(pRoleToken, lSerializableLink,
                        new ImportProperties(), pCtx);
            }
            catch (ImportException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Exception not handle by old method.", e);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.service.ImportService#importProducts(java.lang.String,
     *      java.util.Collection,
     *      org.topcased.gpm.business.extensions.service.Context)
     * @deprecated
     */
    public void importProducts(String pRoleToken,
            Collection<Product> pProducts, Context pCtx) {
        getAuthService().assertGlobalAdminRole(pRoleToken);

        Context lLocalCtx = Context.createContext(pCtx);
        lLocalCtx.put(Context.GPM_SKIP_EXT_PTS, Boolean.TRUE);

        for (Product lSerializableProduct : pProducts) {
            try {
                importProduct(pRoleToken, lSerializableProduct,
                        new ImportProperties(), pCtx);
            }
            catch (ImportException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Exception not handle by old method.", e);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.service.ImportService#importSheets(java.lang.String,
     *      java.util.Collection,
     *      org.topcased.gpm.business.extensions.service.Context)
     * @deprecated
     */
    public void importSheets(String pRoleToken, Collection<SheetData> pSheets,
            Context pCtx) {
        Context lLocalCtx = Context.createContext(pCtx);

        lLocalCtx.put(Context.GPM_SKIP_EXT_PTS, Boolean.TRUE);

        for (SheetData lSerializableSheet : pSheets) {
            try {
                importSheet(pRoleToken, lSerializableSheet,
                        new ImportProperties(), pCtx);
            }
            catch (ImportException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Exception not handle by old method.", e);
                }
            }
        }
    }

    private void importObjects(String pRoleToken,
            ObjectInputStream pObjectStream, ImportProperties pProperties,
            ImportExecutionReport pReport, Context pContext)
        throws ImportException {
        final RoleSelector lRoleSelector = new RoleSelector(pRoleToken);

        // all rules for one sheet type ie: FT, DM, ...
        Map<String, List<RuleData>> lRules =
                new HashMap<String, List<RuleData>>();
        ExtensionPointsToExcludeData lExtensionPointsToExclude = null;
        if (pProperties.getExtensionPointsToExclude() != null) {
            lExtensionPointsToExclude = new ExtensionPointsToExcludeData();
            Set<ExcludeData> lExcludeDataList = new HashSet<ExcludeData>();
            for (String lExtensionPoint : pProperties.getExtensionPointsToExclude()) {
                ExcludeData lExcludeData = new ExcludeData();
                lExcludeData.setName(lExtensionPoint);
                lExcludeDataList.add(lExcludeData);
            }
            lExtensionPointsToExclude.setCommandsToExclude(lExcludeDataList);
        }
        try {
            Object lCurrentObject = null;
            while ((lCurrentObject = pObjectStream.readObject()) != null) {
                // This code is not necessary (an exception will be thrown anyways)
                // But it avoid findbugs's complain about an infinite loop
                
                // Get the list of extension point to exclude if it is a valuesContainerData
                if(lCurrentObject instanceof ValuesContainerData){
                    ValuesContainerData lValuesContainerData = (ValuesContainerData)lCurrentObject;
                    if (lExtensionPointsToExclude != null) {
                        if (lValuesContainerData.getExtensionPointsToExclude() == null) {
                            lValuesContainerData.setExtensionPointsToExclude(lExtensionPointsToExclude);
                        }
                        else {
                            lValuesContainerData.getExtensionPointsToExclude().getCommandsToExclude().addAll(
                                    lExtensionPointsToExclude.getCommandsToExclude());
                        }
                    }
                }
                
                if (lCurrentObject instanceof SheetData) {
                    SheetData lSheetData = (SheetData) lCurrentObject;

                    List<RuleData> lGlobalRule = lRules.get(lSheetData.getType());
                    if (lGlobalRule != null && !lGlobalRule.isEmpty()) {
                        for (RuleData lRuleData : lGlobalRule) {
                            RuleData lRule = new RuleData();
                            lRule.getTransition().addAll(lRuleData.getTransition());
                            lSheetData.getRules().add(lRuleData);
                        }
                    }

                    sheetImportManager.importData(lRoleSelector,
                            (SheetData) lCurrentObject, pProperties, pReport,
                            pContext);
                }
                else if (lCurrentObject instanceof Product) {
                    productImportManager.importData(lRoleSelector,
                            (Product) lCurrentObject, pProperties, pReport,
                            pContext);
                }
                else if (lCurrentObject instanceof SheetLink) {
                    sheetLinkImportManager.importData(lRoleSelector,
                            (SheetLink) lCurrentObject, pProperties, pReport,
                            pContext);
                }
                else if (lCurrentObject instanceof ProductLink) {
                    productLinkImportManager.importData(lRoleSelector,
                            (ProductLink) lCurrentObject, pProperties, pReport,
                            pContext);
                }
                else if (lCurrentObject instanceof User) {
                    userImportManager.importData(lRoleSelector,
                            (User) lCurrentObject, pProperties, pReport,
                            pContext);
                }
                else if (lCurrentObject instanceof UserRole) {
                    userRoleImportManager.importData(pRoleToken,
                            (UserRole) lCurrentObject, pProperties, pContext);
                }
                else if (lCurrentObject instanceof Filter) {
                    if (lCurrentObject instanceof SheetFilter) {
                        SheetFilter lSheetFilter = (SheetFilter) lCurrentObject;
                        if (lSheetFilter.getUserLogin() != null && 
                                !lSheetFilter.getUserLogin().trim().isEmpty()) {
                            String lUserLogin = lSheetFilter.getUserLogin();
                            EndUser lUser = getEndUserDao().getUser(lUserLogin,
                                    authorizationService.isLoginCaseSensitive());
                            if (StringUtils.isNotBlank(lUserLogin) && null == lUser) {
                                throw new GDMException(
                                        "User name " + lUserLogin + " " + " invalid");
                            }
                        }
                    }
                    filterImportManager.importData(lRoleSelector,
                            (Filter) lCurrentObject, pProperties, pReport,
                            pContext);
                }
                else if (lCurrentObject instanceof Category) {
                    categoryImportManager.importData(lRoleSelector,
                            (Category) lCurrentObject, pProperties, pReport,
                            pContext);
                }
                else if (lCurrentObject instanceof Environment) {
                    environmentImportManager.importData(lRoleSelector,
                            (Environment) lCurrentObject, pProperties, pReport,
                            pContext);
                }
                else if (lCurrentObject instanceof RuleData) {
                    RuleData lRuleData = (RuleData) lCurrentObject;
                    if (lRules.get(lRuleData.getType()) != null) {
                        lRules.get(lRuleData.getType()).add(lRuleData);
                    }
                    else {
                        List<RuleData> lRuleDataList = new ArrayList<RuleData>();
                        lRuleDataList.add(lRuleData);
                        lRules.put(lRuleData.getType(), lRuleDataList);
                    }
                }
                else if (lCurrentObject instanceof ExtensionPointsToExcludeData) {
                    ExtensionPointsToExcludeData lExtensionPointsToExcludeData =
                            (ExtensionPointsToExcludeData) lCurrentObject;
                    if (lExtensionPointsToExclude != null) {
                        lExtensionPointsToExclude.getCommandsToExclude().addAll(
                                lExtensionPointsToExcludeData.getCommandsToExclude());
                    }
                    else {
                        lExtensionPointsToExclude =
                                lExtensionPointsToExcludeData;
                    }
                }
            }
        }
        catch (EOFException e) {
            // End of the object stream
        }
        catch (ClassNotFoundException e) {
            throw new GDMException("Error importing element.", e);
        }
        catch (IOException e) {
            throw new GDMException("Error importing element.", e);
        }
        catch (ExtensionException ex) {
            throw new GDMException(ex);
        }
        finally {
            lRoleSelector.close();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.service.ImportService#importInstance(java.lang.String,
     *      java.io.InputStream,
     *      org.topcased.gpm.business.extensions.service.Context)
     * @deprecated
     */
    public void importInstance(String pToken, InputStream pInputStream,
            Context pCtx) throws SchemaValidationException {
        try {
            importData(pToken, pInputStream, new ImportProperties(), pCtx);
        }
        catch (ImportException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception not handle by old method.", e);
            }
        }
    }

    /**
     * Import serializable objects
     * 
     * @param pToken
     *            The role token
     * @param pProperties
     *            The import properties
     * @param pContext
     *            The context
     * @param pConverter
     *            The XML converter
     * @param pHierarchicalReader
     *            The hierarchical reader
     */
    private void importInstance(String pToken, ImportProperties pProperties,
            final ImportExecutionReport pReport, Context pContext,
            XMLConverter pConverter,
            HierarchicalStreamReader pHierarchicalReader)
        throws ImportException {
        while (pHierarchicalReader.hasMoreChildren()) {
            pHierarchicalReader.moveDown();
            String lCurrentNodeName = pHierarchicalReader.getNodeName();

            if (staticSupportedTags.contains(lCurrentNodeName)) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Importing '" + lCurrentNodeName + "'");
                }
                // Note: This object stream must not be closed as closing it seems to close
                // the underlying input stream.
                ObjectInputStream lOis = pConverter.createObjectInputStream();
                importObjects(pToken, lOis, pProperties, pReport, pContext);
            }
            else if (staticSupportedContainerTags.contains(lCurrentNodeName)) {
                importInstance(pToken, pProperties, pReport, pContext,
                        pConverter, pHierarchicalReader);
            }
            pHierarchicalReader.moveUp();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.service.ImportService#importUsers(java.lang.String,
     *      java.util.Collection,
     *      org.topcased.gpm.business.extensions.service.Context)
     * @deprecated
     */
    public void importUsers(String pRoleToken, Collection<User> pUsers,
            Context pCtx) {
        getAuthService().assertGlobalAdminRole(pRoleToken);

        Context lLocalCtx = Context.createContext(pCtx);
        lLocalCtx.put(Context.GPM_SKIP_EXT_PTS, Boolean.TRUE);

        for (User lSerializableUser : pUsers) {
            try {
                importUser(pRoleToken, lSerializableUser,
                        new ImportProperties(), pCtx);
            }
            catch (ImportException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Exception not handle by old method.", e);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.service.ImportService#importUser(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.User,
     *      org.topcased.gpm.business.importation.ImportProperties,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    public ImportExecutionReport importUser(String pRoleToken, User pUser,
            ImportProperties pProperties, Context pContext)
        throws ImportException {
        return userImportManager.importData(pRoleToken, pUser, pProperties,
                pContext);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.service.ImportService#importUserRoles(java.lang.String,
     *      java.util.Collection,
     *      org.topcased.gpm.business.extensions.service.Context)
     * @deprecated
     */
    public void importUserRoles(String pRoleToken,
            Collection<UserRole> pUserRoles, Context pCtx) {
        getAuthService().assertGlobalAdminRole(pRoleToken);

        Context lLocalCtx = Context.createContext(pCtx);
        lLocalCtx.put(Context.GPM_SKIP_EXT_PTS, Boolean.TRUE);

        for (UserRole lSerializableUserRole : pUserRoles) {
            try {
                importUserRole(pRoleToken, lSerializableUserRole,
                        new ImportProperties(), pCtx);
            }
            catch (ImportException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Exception not handle by old method.", e);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.service.ImportService#importUserRole(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.UserRole,
     *      org.topcased.gpm.business.importation.ImportProperties,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    public ImportExecutionReport importUserRole(String pRoleToken,
            UserRole pUserRole, ImportProperties pProperties, Context pContext)
        throws ImportException {
        return userRoleImportManager.importData(pRoleToken, pUserRole,
                pProperties, pContext);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.service.ImportService#importFilters(java.lang.String,
     *      java.util.Collection,
     *      org.topcased.gpm.business.extensions.service.Context)
     * @deprecated
     */
    public void importFilters(String pRoleToken, Collection<Filter> pFilters,
            Context pCtx) {
        getAuthService().assertGlobalAdminRole(pRoleToken);

        Context lLocalCtx = Context.createContext(pCtx);
        lLocalCtx.put(Context.GPM_SKIP_EXT_PTS, Boolean.TRUE);

        for (Filter lSerializableFilter : pFilters) {
            try {
                importFilter(pRoleToken, lSerializableFilter,
                        new ImportProperties(), pCtx);
            }
            catch (ImportException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Exception not handle by old method.", e);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.service.ImportService#importFilter(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.Filter,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    public ImportExecutionReport importFilter(String pRoleToken,
            Filter pFilter, ImportProperties pProperties, Context pContext)
        throws ImportException {
        EndUser lUser =
                getEndUserDao().getUser(pFilter.getUserLogin(),
                        authorizationService.isLoginCaseSensitive());
        if (StringUtils.isNotBlank(pFilter.getUserLogin()) && null == lUser) {
            throw new GDMException("User name " + pFilter.getUserLogin() + " "
                    + " invalid");
        }
        return filterImportManager.importData(pRoleToken, pFilter, pProperties,
                pContext);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.service.ImportService#importDictionary(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.Dictionary,
     *      org.topcased.gpm.business.extensions.service.Context)
     * @deprecated
     */
    public void importDictionary(String pRoleToken, Dictionary pDictionary,
            Context pCtx) {

        importCategories(pRoleToken, pDictionary.getCategories(), pCtx);

        importEnvironments(pRoleToken, pDictionary.getEnvironments(), pCtx);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.service.ImportService#importCategories(java.lang.String,
     *      java.util.Collection,
     *      org.topcased.gpm.business.extensions.service.Context)
     * @deprecated
     */
    public void importCategories(String pRoleToken,
            Collection<Category> pCategories, Context pCtx) {
        getAuthService().assertGlobalAdminRole(pRoleToken);

        Context lLocalCtx = Context.createContext(pCtx);
        lLocalCtx.put(Context.GPM_SKIP_EXT_PTS, Boolean.TRUE);

        for (Category lSerializableCategory : pCategories) {
            try {
                importCategory(pRoleToken, lSerializableCategory,
                        new ImportProperties(), pCtx);
            }
            catch (ImportException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Exception not handle by old method.", e);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.service.ImportService#importCategory(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.Category,
     *      org.topcased.gpm.business.importation.ImportProperties,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    public ImportExecutionReport importCategory(String pRoleToken,
            Category pCategory, ImportProperties pProperties, Context pContext)
        throws ImportException {
        return categoryImportManager.importData(pRoleToken, pCategory,
                pProperties, pContext);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.service.ImportService#importEnvironments(java.lang.String,
     *      java.util.Collection,
     *      org.topcased.gpm.business.extensions.service.Context)
     * @deprecated
     */
    public void importEnvironments(String pRoleToken,
            Collection<Environment> pEnvironments, Context pCtx) {
        getAuthService().assertGlobalAdminRole(pRoleToken);

        Context lLocalCtx = Context.createContext(pCtx);
        lLocalCtx.put(Context.GPM_SKIP_EXT_PTS, Boolean.TRUE);

        for (Environment lSerializableEnvironment : pEnvironments) {
            try {
                importEnvironment(pRoleToken, lSerializableEnvironment,
                        new ImportProperties(), pCtx);
            }
            catch (ImportException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Exception not handle by old method.", e);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.service.ImportService#importEnvironment(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.Environment,
     *      org.topcased.gpm.business.importation.ImportProperties,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    public ImportExecutionReport importEnvironment(String pRoleToken,
            Environment pEnvironment, ImportProperties pProperties,
            Context pContext) throws ImportException {
        return environmentImportManager.importData(pRoleToken, pEnvironment,
                pProperties, pContext);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.service.ImportService#isContentOnly(java.lang.String,
     *      java.io.InputStream, java.lang.String)
     */
    public boolean isContentOnly(final String pRoleToken,
            InputStream pInputStream, final String pTag) {
        authorizationService.assertGlobalAdminRole(pRoleToken);

        XMLConverter lConverter = new XMLConverter(pInputStream);

        HierarchicalStreamReader lHierarchicalReader =
                lConverter.createHierarchicalStreamReader();
        boolean lOnly = true;
        Collection<String> lUnsupportedTags =
                new HashSet<String>(staticSupportedTags);
        lUnsupportedTags.remove(pTag);
        lUnsupportedTags.addAll(staticSupportedContainerTags);
        while (lHierarchicalReader.hasMoreChildren() && lOnly) {
            lHierarchicalReader.moveDown();
            String lCurrentNodeName = lHierarchicalReader.getNodeName();

            lOnly = !lUnsupportedTags.contains(lCurrentNodeName);
            lHierarchicalReader.moveUp();
        }

        return lOnly;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.service.ImportService#importProduct(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.Product,
     *      org.topcased.gpm.business.importation.ImportProperties,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    public ImportExecutionReport importProduct(String pRoleToken,
            Product pProduct, ImportProperties pProperties, Context pContext)
        throws ImportException {
        return productImportManager.importData(pRoleToken, pProduct,
                pProperties, pContext);
    }

    private SheetImportManager sheetImportManager;

    private SheetLinkImportManager sheetLinkImportManager;

    private ProductImportManager productImportManager;

    private ProductLinkImportManager productLinkImportManager;

    private FilterImportManager filterImportManager;

    private UserImportManager userImportManager;

    private UserRoleImportManager userRoleImportManager;

    private CategoryImportManager categoryImportManager;

    private EnvironmentImportManager environmentImportManager;

    public void setSheetImportManager(SheetImportManager pSheetImportManager) {
        sheetImportManager = pSheetImportManager;
    }

    public void setSheetLinkImportManager(
            SheetLinkImportManager pSheetLinkImportManager) {
        sheetLinkImportManager = pSheetLinkImportManager;
    }

    public void setProductImportManager(
            ProductImportManager pProductImportManager) {
        productImportManager = pProductImportManager;
    }

    public void setProductLinkImportManager(
            ProductLinkImportManager pProductLinkImportManager) {
        productLinkImportManager = pProductLinkImportManager;
    }

    public void setFilterImportManager(FilterImportManager pFilterImportManager) {
        filterImportManager = pFilterImportManager;
    }

    public void setUserImportManager(UserImportManager pUserImportManager) {
        userImportManager = pUserImportManager;
    }

    public void setUserRoleImportManager(
            UserRoleImportManager pUserRoleImportManager) {
        userRoleImportManager = pUserRoleImportManager;
    }

    public void setCategoryImportManager(
            CategoryImportManager pCategoryImportManager) {
        categoryImportManager = pCategoryImportManager;
    }

    public void setEnvironmentImportManager(
            EnvironmentImportManager pEnvironmentImportManager) {
        environmentImportManager = pEnvironmentImportManager;
    }
}
