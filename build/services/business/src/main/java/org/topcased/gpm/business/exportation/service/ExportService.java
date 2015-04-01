/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exportation.service;

import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.topcased.gpm.business.exception.SystemException;
import org.topcased.gpm.business.exportation.ExportExecutionReport;
import org.topcased.gpm.business.exportation.ExportProperties;

/**
 * gPM instance export service.
 * 
 * @author llatil
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface ExportService {

    /**
     * Flag to export sheets content.
     * 
     * @deprecated
     * @since 1.8.1
     * @see ExportProperties#getSheetsFlag()
     */
    public static final long EXPORT_SHEETS = 1 << 0;

    /**
     * Flag to export products content.
     * 
     * @deprecated
     * @since 1.8.1
     * @see ExportProperties#getProductsFlag()
     */
    public static final long EXPORT_PRODUCTS = 1 << 1;

    /**
     * Flag to export links (sheet & product links) content.
     * 
     * @deprecated
     * @since 1.8.1
     * @see ExportProperties#getSheetLinksFlag()
     * @see ExportProperties#getProductLinksFlag()
     */
    public static final long EXPORT_LINKS = 1 << 2;

    /**
     * Flag to export attached file content.
     * 
     * @deprecated
     * @since 1.8.1
     * @see ExportProperties#getAttachedFilePath()
     */
    public static final long EXPORT_FILE_CONTENT = 1 << 3;

    /**
     * Flag to export revisions content.
     * 
     * @deprecated
     * @since 1.8.1
     * @see ExportProperties#isExportRevision()
     */
    public static final long EXPORT_REVISIONS = 1 << 4;

    /**
     * Flag to export users.
     * 
     * @deprecated
     * @since 1.8.1
     * @see ExportProperties#getUsersFlag()
     * @see ExportProperties#getUserRolesFlag()
     */
    public static final long EXPORT_USERS = 1 << 5;

    /**
     * Flag to export filter definitions.
     * 
     * @deprecated
     * @since 1.8.1
     * @see ExportProperties#getFiltersFlag()
     */
    public static final long EXPORT_FILTERS = 1 << 6;

    /**
     * Flag to export dictionary & environments content.
     * 
     * @deprecated
     * @since 1.8.1
     * @see ExportProperties#getCategoriesFlag()
     * @see ExportProperties#getEnvironmentsFlag()
     */
    public static final long EXPORT_DICTIONARY = 1 << 7;

    /**
     * The Constant EXPORT_ALL.
     * 
     * @deprecated
     * @since 1.8.1
     * @see ExportProperties#setAllFlags(org.topcased.gpm.business.exportation.ExportProperties.ExportFlag)
     */
    public static final long EXPORT_ALL =
            EXPORT_SHEETS | EXPORT_PRODUCTS | EXPORT_LINKS
                    | EXPORT_FILE_CONTENT | EXPORT_REVISIONS | EXPORT_USERS
                    | EXPORT_FILTERS | EXPORT_DICTIONARY;

    /**
     * Name of context parameter used to restrict the product names to export.
     * 
     * @deprecated
     * @since 1.8.1
     * @see ExportProperties#getLimitedProductsName()
     */
    public static final String CONTEXT_PRODUCT_NAMES = "export.productNames";

    /**
     * Name of context parameter used to restrict the sheet types to export.
     * 
     * @deprecated
     * @since 1.8.1
     * @see ExportProperties#getLimitedTypesName()
     */
    public static final String CONTEXT_SHEET_TYPE_NAMES =
            "export.sheetTypeNames";

    /**
     * Name of context parameter used to define path of exported attached files
     * content.
     * 
     * @deprecated
     * @since 1.8.1
     * @see ExportProperties#getAttachedFilePath()
     */
    public static final String CONTEXT_ATTACHED_FILES_PATH =
            "export.attachedFilesPath";

    /**
     * Export the data of an instance on an XML file.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pOutputStream
     *            The output stream containing the exported data.
     * @param pProperties
     *            The export properties.
     * @return The export execution report.
     * @throws SystemException
     *             The export failed.
     */
    public ExportExecutionReport exportData(String pRoleToken,
            OutputStream pOutputStream, ExportProperties pProperties)
        throws SystemException;

    /**
     * Get the id of all the dictionary categories.
     * 
     * @param pRoleToken
     *            The role token.
     * @return The id of all the dictionary categories.
     */
    public List<String> getDictionaryCategoriesId(String pRoleToken);

    /**
     * Get the id of all the environments.
     * 
     * @param pRoleToken
     *            The role token.
     * @return The id of all the environments.
     */
    public List<String> getEnvironmentsId(String pRoleToken);

    /**
     * Get the id of all the filters of specific products.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pProductsName
     *            The products name.
     * @return The id of all the filters of specific products.
     */
    public List<String> getFiltersId(String pRoleToken,
            Collection<String> pProductsName);

    /**
     * Get the id of all the product links of specific types and products.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pProductsName
     *            The products name.
     * @param pTypesName
     *            The types name.
     * @return The id of all the product links of specific types and products.
     */
    public List<String> getProductLinksId(String pRoleToken,
            Collection<String> pProductsName, Collection<String> pTypesName);

    /**
     * Get the id of all the products of specific types and products.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pProductsName
     *            The products name.
     * @param pTypesName
     *            The types name.
     * @return The id of all the products of specific types and products.
     */
    public List<String> getProductsId(String pRoleToken,
            Collection<String> pProductsName, Collection<String> pTypesName);

    /**
     * Get the name of the products accessible from a specific one through a
     * sheet link of a specific type from destination to origin.
     * 
     * @param pRoleToken
     *            The role token
     * @param pSheetLinkTypeName
     *            the sheet link type name
     * @param pOriginProductName
     *            the origin product name
     * @return the sheet link destination products name
     */
    public List<String> getSheetLinkDestinationProductsName(String pRoleToken,
            String pSheetLinkTypeName, String pOriginProductName);

    /**
     * Get the id of all the sheet links of specific types and products.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pOriginProductName
     *            The origin product name.
     * @param pDestinationProductName
     *            The destination product name.
     * @param pTypesName
     *            The types name.
     * @return The id of all the sheet links of specific types and products.
     */
    public List<String> getSheetLinksId(String pRoleToken,
            String pOriginProductName, String pDestinationProductName,
            Collection<String> pTypesName);

    /**
     * Get the id of all the sheets of specific types and products.
     * 
     * @param pRoleToken
     *            The role token.
     * @param pProductsName
     *            The products name.
     * @param pTypesName
     *            The types name.
     * @return The id of all the sheets of specific types and products.
     */
    public List<String> getSheetsId(String pRoleToken,
            Collection<String> pProductsName, Collection<String> pTypesName);

    /**
     * Get the id of all the users.
     * 
     * @param pRoleToken
     *            The role token.
     * @return The id of all the users.
     */
    public List<String> getUsersId(String pRoleToken);
}
