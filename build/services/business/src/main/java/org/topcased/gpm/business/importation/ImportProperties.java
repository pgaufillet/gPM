/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.importation;

import java.util.List;

import org.topcased.gpm.business.extensions.ExtensionsExecutionProperties;

/**
 * Properties used by the import.
 * 
 * @author tpanuel
 */
public class ImportProperties extends ExtensionsExecutionProperties {
    private ImportFlag sheetsFlag;

    private ImportFlag sheetLinksFlag;

    private ImportFlag productsFlag;

    private ImportFlag productLinksFlag;

    private ImportFlag environmentsFlag;

    private ImportFlag categoriesFlag;

    private ImportFlag filtersFlag;

    private ImportFlag usersFlag;

    private ImportFlag userRolesFlag;

    private boolean exceptionRaising = true;

    private boolean importFileContent = true;

    private boolean ignoreVersion;

    private List<String> extensionPointsToExclude;

    /**
     * By default, all the elements are imported with create or update option.
     * An empty context is create.
     */
    public ImportProperties() {
        setAllFlags(ImportFlag.CREATE_OR_UPDATE);
    }

    /**
     * Get the import flag for sheets.
     * 
     * @return The import flag for sheets.
     */
    public ImportFlag getSheetsFlag() {
        return sheetsFlag;
    }

    /**
     * Set the import flag for sheets.
     * 
     * @param pSheetsFlag
     *            The new import flag for sheets.
     */
    public void setSheetsFlag(final ImportFlag pSheetsFlag) {
        sheetsFlag = pSheetsFlag;
    }

    /**
     * Get the import flag for sheet links.
     * 
     * @return The import flag for sheet links.
     */
    public ImportFlag getSheetLinksFlag() {
        return sheetLinksFlag;
    }

    /**
     * Set the import flag for sheet links.
     * 
     * @param pSheetLinksFlag
     *            The new import flag for sheet links.
     */
    public void setSheetLinksFlag(final ImportFlag pSheetLinksFlag) {
        sheetLinksFlag = pSheetLinksFlag;
    }

    /**
     * Get the import flag for products.
     * 
     * @return The import flag for products.
     */
    public ImportFlag getProductsFlag() {
        return productsFlag;
    }

    /**
     * Set the import flag for products.
     * 
     * @param pProductsFlag
     *            The new import flag for products.
     */
    public void setProductsFlag(final ImportFlag pProductsFlag) {
        productsFlag = pProductsFlag;
    }

    /**
     * Get the import flag for product links.
     * 
     * @return The import flag for product links.
     */
    public ImportFlag getProductLinksFlag() {
        return productLinksFlag;
    }

    /**
     * Set the import flag for product links.
     * 
     * @param pProductLinksFlag
     *            The new import flag for product links.
     */
    public void setProductLinksFlag(final ImportFlag pProductLinksFlag) {
        productLinksFlag = pProductLinksFlag;
    }

    /**
     * Get the import flag for environments.
     * 
     * @return The import flag for environments.
     */
    public ImportFlag getEnvironmentsFlag() {
        return environmentsFlag;
    }

    /**
     * Set the import flag for environments.
     * 
     * @param pEnvironmentsFlag
     *            The new import flag for environments.
     */
    public void setEnvironmentsFlag(final ImportFlag pEnvironmentsFlag) {
        environmentsFlag = pEnvironmentsFlag;
    }

    /**
     * Get the import flag for categories.
     * 
     * @return The import flag for categories.
     */
    public ImportFlag getCategoriesFlag() {
        return categoriesFlag;
    }

    /**
     * Set the import flag for categories.
     * 
     * @param pCategoriesFlag
     *            The new import flag for categories.
     */
    public void setCategoriesFlag(final ImportFlag pCategoriesFlag) {
        categoriesFlag = pCategoriesFlag;
    }

    /**
     * Get the import flag for filters.
     * 
     * @return The import flag for filters.
     */
    public ImportFlag getFiltersFlag() {
        return filtersFlag;
    }

    /**
     * Set the import flag for filters.
     * 
     * @param pFiltersFlag
     *            The new import flag for filters.
     */
    public void setFiltersFlag(final ImportFlag pFiltersFlag) {
        filtersFlag = pFiltersFlag;
    }

    /**
     * Get the import flag for users.
     * 
     * @return The import flag for users.
     */
    public ImportFlag getUsersFlag() {
        return usersFlag;
    }

    /**
     * Set the import flag for users.
     * 
     * @param pUsersFlag
     *            The new import flag for users.
     */
    public void setUsersFlag(final ImportFlag pUsersFlag) {
        usersFlag = pUsersFlag;
    }

    /**
     * Get the import flag for user roles.
     * 
     * @return The import flag for user roles.
     */
    public ImportFlag getUserRolesFlag() {
        return userRolesFlag;
    }

    /**
     * Set the import flag for user roles.
     * 
     * @param pUserRolesFlag
     *            The new import flag for user roles.
     */
    public void setUserRolesFlag(final ImportFlag pUserRolesFlag) {
        userRolesFlag = pUserRolesFlag;
    }

    /**
     * Import exception can be raise.
     * <p>
     * If false, the ImportExecutionReport is filled with the error.
     * 
     * @return True if import exceptions can be raise, false otherwise
     */
    public boolean isExceptionRaising() {
        return exceptionRaising;
    }

    /**
     * Set true if import exceptions can be raise, false to fill the import's
     * execution report.
     * 
     * @param pExceptionRaising
     *            True to raise import exception, false to fill the report.
     */
    public void setExceptionRaising(final boolean pExceptionRaising) {
        exceptionRaising = pExceptionRaising;
    }

    /**
     * Set the same import flag for all the types.
     * 
     * @param pFlag
     *            The import flag.
     */
    public void setAllFlags(final ImportFlag pFlag) {
        sheetsFlag = pFlag;
        sheetLinksFlag = pFlag;
        productsFlag = pFlag;
        productLinksFlag = pFlag;
        environmentsFlag = pFlag;
        categoriesFlag = pFlag;
        filtersFlag = pFlag;
        usersFlag = pFlag;
        userRolesFlag = pFlag;
    }

    /**
     * Set if the content of attached files is imported.
     * 
     * @param pImportFileContent
     *            If the content of attached files is imported.
     */
    public void setImportFileContent(final boolean pImportFileContent) {
        importFileContent = pImportFileContent;
    }

    /**
     * Get if the content of attached files is imported.
     * 
     * @return If the content of attached files is imported.
     */
    public boolean isImportFileContent() {
        return importFileContent;
    }

    /**
     * Determine if the version has to be ignored for
     * 
     * @return true if the version is ignored.
     */
    public boolean isIgnoreVersion() {
        return ignoreVersion;
    }

    /**
     * Set the attribute ignoreVersion. This attribute is in charge of
     * determining if the version of the element has to be taken into account
     * for an element import. It is compared with the version stored in the
     * database to check if the element has been modified in the database and if
     * the import element is up to date.
     * 
     * @param pIgnoreVersion true if the version has to be ignored
     */
    public void setIgnoreVersion(final boolean pIgnoreVersion) {
        this.ignoreVersion = pIgnoreVersion;
    }

    public List<String> getExtensionPointsToExclude() {
        return extensionPointsToExclude;
    }

    public void setExtensionPointsToExclude(
            List<String> pExtensionPointsToExclude) {
        this.extensionPointsToExclude = pExtensionPointsToExclude;
    }

    /**
     * Flag used to import a specific type of data.
     * 
     * @author tpanuel
     */
    public enum ImportFlag {
        // The element is not import without error
        SKIP,
        // An error is launched if an element of this type is found
        ERROR,
        // Error is an element with the same reference already exists
        CREATE_ONLY,
        // Error is an element with the same reference doesn't exists
        UPDATE_ONLY,
        // If the element already exists, it's delete an re-create
        ERASE,
        // If an element with the same reference already exists, 
        // it's updated else it us create
        CREATE_OR_UPDATE,
    }
}