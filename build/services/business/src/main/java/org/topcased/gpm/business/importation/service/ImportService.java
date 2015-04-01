/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin),
 * Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.importation.service;

import java.io.InputStream;
import java.util.Collection;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.topcased.gpm.business.exception.FieldValidationException;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.exception.SchemaValidationException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.importation.ImportExecutionReport;
import org.topcased.gpm.business.importation.ImportProperties;
import org.topcased.gpm.business.serialization.data.Category;
import org.topcased.gpm.business.serialization.data.Dictionary;
import org.topcased.gpm.business.serialization.data.Environment;
import org.topcased.gpm.business.serialization.data.Filter;
import org.topcased.gpm.business.serialization.data.Link;
import org.topcased.gpm.business.serialization.data.Product;
import org.topcased.gpm.business.serialization.data.SheetData;
import org.topcased.gpm.business.serialization.data.User;
import org.topcased.gpm.business.serialization.data.UserRole;

/**
 * gPM instance import service. There are two ways for getting the importation
 * errors.
 * <ol>
 * <li>The errors have been raised as exception (ImportException) and the import
 * is stopped. The caller must handle them.</li>
 * <li>The errors are in a report and the importation continues.</li>
 * </ol>
 * The way is define in the ImportProperties object.
 * 
 * @author llatil
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface ImportService {
    /** SUPPORTED_TAG_ENVIRONMENTS */
    public static final String SUPPORTED_TAG_ENVIRONMENTS = "environments";

    /** SUPPORTED_TAG_CATEGORIES */
    public static final String SUPPORTED_TAG_CATEGORIES = "categories";

    /** SUPPORTED_TAG_FILTERS */
    public static final String SUPPORTED_TAG_FILTERS = "filters";

    /** SUPPORTED_TAG_USER_ROLES */
    public static final String SUPPORTED_TAG_USER_ROLES = "userRoles";

    /** SUPPORTED_TAG_USERS */
    public static final String SUPPORTED_TAG_USERS = "users";

    /** SUPPORTED_TAG_SHEET_LINKS */
    public static final String SUPPORTED_TAG_SHEET_LINKS = "sheetLinks";

    /** SUPPORTED_TAG_SHEETS */
    public static final String SUPPORTED_TAG_SHEETS = "sheets";

    /** SUPPORTED_TAG_PRODUCT_LINKS */
    public static final String SUPPORTED_TAG_PRODUCT_LINKS = "productLinks";

    /** SUPPORTED_TAG_PRODUCTS */
    public static final String SUPPORTED_TAG_PRODUCTS = "products";

    /** SUPPORTED_TAG_RULES */
    public static final String SUPPORTED_TAG_RULE = "rule";

    /**
     * Import all the data contains on an XML file.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pInputStream
     *            The input stream reading the XML file.
     * @param pProperties
     *            The import properties.
     * @param pContext
     *            The context.
     * @return The import execution report.
     * @throws SchemaValidationException
     *             Error if the schema is invalid.
     * @throws ImportException
     *             Error if the content to import has unauthorized type
     *             (ImportFlag.ERROR)
     */
    public ImportExecutionReport importData(String pRoleToken,
            InputStream pInputStream, ImportProperties pProperties,
            Context pContext) throws SchemaValidationException, ImportException;

    /**
     * Import a sheet.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pSheet
     *            The sheet.
     * @param pProperties
     *            The import properties.
     * @param pContext
     *            The context.
     * @return The import execution report.
     * @throws FieldValidationException
     *             A field is not valid.
     * @throws ImportException
     *             Error if the content to import has unauthorized type
     *             (ImportFlag.ERROR)
     */
    public ImportExecutionReport importSheet(String pRoleToken,
            SheetData pSheet, ImportProperties pProperties, Context pContext)
        throws FieldValidationException, ImportException;

    /**
     * Import a sheet link or a product link.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pLink
     *            The link.
     * @param pProperties
     *            The import properties.
     * @param pContext
     *            The context.
     * @return The import execution report.
     * @throws FieldValidationException
     *             A field is not valid.
     * @throws ImportException
     *             Error if the content to import has unauthorized type
     *             (ImportFlag.ERROR)
     */
    public ImportExecutionReport importLink(String pRoleToken, Link pLink,
            ImportProperties pProperties, Context pContext)
        throws FieldValidationException, ImportException;

    /**
     * Import a collection of sheets in the gPM database
     * 
     * @param pRoleToken
     *            Role session token (must have 'admin' privileges)
     * @param pSheets
     *            Sheets to import
     * @param pCtx
     *            Execution context
     * @deprecated
     * @see ImportService#importSheet(String, SheetData, ImportProperties)
     * @since 1.8.1
     */
    void importSheets(String pRoleToken, Collection<SheetData> pSheets,
            Context pCtx);

    /**
     * Import a collection of products in the gPM database
     * 
     * @param pRoleToken
     *            Role session token (must have 'admin' privileges)
     * @param pProducts
     *            Products to import
     * @param pCtx
     *            Execution context
     * @deprecated
     * @see ImportService#importProduct(String, Product, ImportProperties,
     *      Context)
     * @since 1.8.2
     */
    void importProducts(String pRoleToken, Collection<Product> pProducts,
            Context pCtx);

    /**
     * Import a product.
     * 
     * @param pRoleToken
     *            Role token
     * @param pProduct
     *            Product to import.
     * @param pProperties
     *            The import properties
     * @param pContext
     *            Context for importation.
     * @return The import execution report.
     * @throws ImportException
     *             On import exception.
     */
    public ImportExecutionReport importProduct(final String pRoleToken,
            final Product pProduct, ImportProperties pProperties,
            Context pContext) throws ImportException;

    /**
     * Import a collection of links in the gPM database
     * 
     * @param pRoleToken
     *            Role session token (must have 'admin' privileges)
     * @param pLinks
     *            Links to import
     * @param pCtx
     *            Execution context
     * @deprecated
     * @see ImportService#importLink(String, Link, ImportProperties)
     * @since 1.8.1
     */
    void importLinks(String pRoleToken, Collection<Link> pLinks, Context pCtx);

    /**
     * Import an instance (XML read from the input stream) in the gPM database
     * 
     * @param pRoleToken
     *            Role session token (must have 'admin' privileges)
     * @param pInputStream
     *            Data to import
     * @param pCtx
     *            Execution context
     * @throws SchemaValidationException
     *             The XML file not correspond to the XSD
     * @deprecated
     * @see ImportService#importData(String, InputStream, ImportProperties)
     * @since 1.8.1
     */
    public void importInstance(String pRoleToken, InputStream pInputStream,
            Context pCtx) throws SchemaValidationException;

    /**
     * Import a collection of users in the gPM database
     * 
     * @param pRoleToken
     *            Role session token (must have 'admin' privileges)
     * @param pUsers
     *            Users to import
     * @param pCtx
     *            Execution context
     * @deprecated
     * @see ImportService#importUser(String, User, ImportProperties, Context)
     * @since 1.8.2
     */
    void importUsers(String pRoleToken, Collection<User> pUsers, Context pCtx);

    /**
     * Import the user in the gPM database.
     * 
     * @param pRoleToken
     *            Role token
     * @param pUser
     *            User to import
     * @param pProperties
     *            The import properties.
     * @param pContext
     *            The context.
     * @return The import execution report.
     * @throws ImportException
     *             Error if the content to import has unauthorized type
     *             (ImportFlag.ERROR)
     */

    public ImportExecutionReport importUser(String pRoleToken, User pUser,
            ImportProperties pProperties, Context pContext)
        throws ImportException;

    /**
     * Import a collection of userRoles in the gPM database
     * 
     * @param pRoleToken
     *            Role session token (must have 'admin' privileges)
     * @param pUserRoles
     *            UserRoles to import
     * @param pCtx
     *            Execution context
     * @deprecated
     * @see ImportService#importUserRole(String, UserRole, ImportProperties,
     *      Context)
     * @since 1.8.2
     */
    void importUserRoles(String pRoleToken, Collection<UserRole> pUserRoles,
            Context pCtx);

    /**
     * Import a collection of userRoles in the gPM database.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pUserRole
     *            User role to import
     * @param pProperties
     *            Import properties
     * @param pContext
     *            Import context
     * @return Import execution report.
     * @throws ImportException
     *             Errors during importation.
     */
    public ImportExecutionReport importUserRole(final String pRoleToken,
            final UserRole pUserRole, final ImportProperties pProperties,
            final Context pContext) throws ImportException;

    /**
     * Import a collection of filters in the gPM database
     * 
     * @param pRoleToken
     *            Role session token (must have 'admin' privileges)
     * @param pFilters
     *            Filters to import
     * @param pCtx
     *            Execution context
     * @deprecated
     * @see ImportService#importFilters(String, Collection, Context)
     * @since 1.8.2
     */
    void importFilters(String pRoleToken, Collection<Filter> pFilters,
            Context pCtx);

    /**
     * Import a filter in the gPM database.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pFilter
     *            Filter to import
     * @param pProperties
     *            Import properties
     * @param pContext
     *            Import context.
     * @throws ImportException
     *             Error during importation
     * @return An importation report.
     */
    public ImportExecutionReport importFilter(final String pRoleToken,
            final Filter pFilter, final ImportProperties pProperties,
            final Context pContext) throws ImportException;

    /**
     * Import a complete dictionary in the gPM database
     * 
     * @param pRoleToken
     *            Role session token (must have 'admin' privileges)
     * @param pDictionary
     *            Dictionary to import
     * @param pCtx
     *            Execution context@deprecated
     * @deprecated
     * @see ImportService#importCategory(String, Category, ImportProperties,
     *      Context)
     * @see ImportService#importEnvironments(String, Collection, Context)
     * @since 1.8.2
     */
    void importDictionary(String pRoleToken, Dictionary pDictionary,
            Context pCtx);

    /**
     * Import a collection of categories in the gPM database
     * 
     * @param pRoleToken
     *            Role session token (must have 'admin' privileges)
     * @param pCategories
     *            Filters to import
     * @param pCtx
     *            Execution context
     * @deprecated
     * @see ImportService#importCategory(String, Category, ImportProperties,
     *      Context)
     * @since 1.8.2
     */
    void importCategories(String pRoleToken, Collection<Category> pCategories,
            Context pCtx);

    /**
     * Import a category in the gPM database
     * 
     * @param pRoleToken
     *            Role token
     * @param pCategory
     *            Category to import
     * @param pProperties
     *            Import properties
     * @param pContext
     *            Import's context
     * @return Report for this importation.
     * @throws ImportException
     *             Error while importing.
     */
    public ImportExecutionReport importCategory(String pRoleToken,
            Category pCategory, ImportProperties pProperties, Context pContext)
        throws ImportException;

    /**
     * Import a collection of environments in the gPM database
     * 
     * @param pRoleToken
     *            Role session token (must have 'admin' privileges)
     * @param pEnvironments
     *            Filters to import
     * @param pCtx
     *            Execution context
     * @deprecated
     * @see ImportService#importEnvironment(String, Environment,
     *      ImportProperties, Context)
     * @since 1.8.2
     */
    void importEnvironments(String pRoleToken,
            Collection<Environment> pEnvironments, Context pCtx);

    /**
     * Import an environment in the gPM database
     * 
     * @param pRoleToken
     *            Role token
     * @param pEnvironment
     *            Environment to import
     * @param pProperties
     *            Import's properties
     * @param pContext
     *            Import's context
     * @return Report of the importation.
     * @throws ImportException
     *             Error while importing.
     */
    public ImportExecutionReport importEnvironment(final String pRoleToken,
            final Environment pEnvironment, final ImportProperties pProperties,
            final Context pContext) throws ImportException;

    /**
     * Test the content of data.
     * <p>
     * Test the content of data is only of the specified tag.
     * 
     * @param pRoleToken
     *            Role token
     * @param pInputStream
     *            Data to test
     * @param pTag
     *            Supported tag (products, sheets, links....)
     * @return True if data contains only data of specified tag, false otherwise
     */
    public boolean isContentOnly(final String pRoleToken,
            InputStream pInputStream, final String pTag);
}