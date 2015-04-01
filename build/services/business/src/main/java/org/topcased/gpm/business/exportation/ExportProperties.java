/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exportation;

import org.apache.commons.lang.StringUtils;

/**
 * Properties used by the export.
 * 
 * @author tpanuel
 */
public class ExportProperties {
    private ExportFlag sheetsFlag;

    private ExportFlag sheetLinksFlag;

    private ExportFlag productsFlag;

    private ExportFlag productLinksFlag;

    private ExportFlag environmentsFlag;

    private ExportFlag categoriesFlag;

    private ExportFlag filtersFlag;

    private ExportFlag usersFlag;

    private ExportFlag userRolesFlag;

    private String[] limitedElementsId;

    private String[] limitedTypesName;

    private String[] limitedProductsName;

    private String attachedFilePath;

    private boolean exportRevision;

    private boolean exportUID;

    /**
     * By default, the UID are exported.
     */
    public ExportProperties() {
        setAllFlags(ExportFlag.NO);
        limitedElementsId = new String[0];
        limitedTypesName = new String[0];
        limitedProductsName = new String[0];
        attachedFilePath = StringUtils.EMPTY;
        exportRevision = true;
        exportUID = true;
    }

    /**
     * Get the export flag for sheets.
     * 
     * @return The export flag for sheets.
     */
    public ExportFlag getSheetsFlag() {
        return sheetsFlag;
    }

    /**
     * Set the export flag for sheets.
     * 
     * @param pSheetsFlag
     *            The new export flag for sheets.
     */
    public void setSheetsFlag(final ExportFlag pSheetsFlag) {
        sheetsFlag = pSheetsFlag;
    }

    /**
     * Get the export flag for sheet links.
     * 
     * @return The export flag for sheet links.
     */
    public ExportFlag getSheetLinksFlag() {
        return sheetLinksFlag;
    }

    /**
     * Set the export flag for sheet links.
     * 
     * @param pSheetLinksFlag
     *            The new export flag for sheet links.
     */
    public void setSheetLinksFlag(final ExportFlag pSheetLinksFlag) {
        sheetLinksFlag = pSheetLinksFlag;
    }

    /**
     * Get the export flag for products.
     * 
     * @return The export flag for products.
     */
    public ExportFlag getProductsFlag() {
        return productsFlag;
    }

    /**
     * Set the export flag for products.
     * 
     * @param pProductsFlag
     *            The new export flag for products.
     */
    public void setProductsFlag(final ExportFlag pProductsFlag) {
        productsFlag = pProductsFlag;
    }

    /**
     * Get the export flag for product links.
     * 
     * @return The export flag for product links.
     */
    public ExportFlag getProductLinksFlag() {
        return productLinksFlag;
    }

    /**
     * Set the export flag for product links.
     * 
     * @param pProductLinksFlag
     *            The new export flag for product links.
     */
    public void setProductLinksFlag(final ExportFlag pProductLinksFlag) {
        productLinksFlag = pProductLinksFlag;
    }

    /**
     * Get the export flag for environments.
     * 
     * @return The export flag for environments.
     */
    public ExportFlag getEnvironmentsFlag() {
        return environmentsFlag;
    }

    /**
     * Set the export flag for environments.
     * 
     * @param pEnvironmentsFlag
     *            The new export flag for environments.
     */
    public void setEnvironmentsFlag(final ExportFlag pEnvironmentsFlag) {
        environmentsFlag = pEnvironmentsFlag;
    }

    /**
     * Get the export flag for categories.
     * 
     * @return The export flag for categories.
     */
    public ExportFlag getCategoriesFlag() {
        return categoriesFlag;
    }

    /**
     * Set the export flag for categories.
     * 
     * @param pCategoriesFlag
     *            The new export flag for categories.
     */
    public void setCategoriesFlag(final ExportFlag pCategoriesFlag) {
        categoriesFlag = pCategoriesFlag;
    }

    /**
     * Get the export flag for filters.
     * 
     * @return The export flag for filters.
     */
    public ExportFlag getFiltersFlag() {
        return filtersFlag;
    }

    /**
     * Set the export flag for filters.
     * 
     * @param pFiltersFlag
     *            The new export flag for filters.
     */
    public void setFiltersFlag(final ExportFlag pFiltersFlag) {
        filtersFlag = pFiltersFlag;
    }

    /**
     * Get the export flag for users.
     * 
     * @return The export flag for users.
     */
    public ExportFlag getUsersFlag() {
        return usersFlag;
    }

    /**
     * Set the export flag for users.
     * 
     * @param pUsersFlag
     *            The new export flag for users.
     */
    public void setUsersFlag(final ExportFlag pUsersFlag) {
        usersFlag = pUsersFlag;
    }

    /**
     * Get the export flag for user roles.
     * 
     * @return The export flag for user roles.
     */
    public ExportFlag getUserRolesFlag() {
        return userRolesFlag;
    }

    /**
     * Set the export flag for user roles.
     * 
     * @param pUserRolesFlag
     *            The new export flag for user roles.
     */
    public void setUserRolesFlag(final ExportFlag pUserRolesFlag) {
        userRolesFlag = pUserRolesFlag;
    }

    /**
     * Set the same export flag for all the types.
     * 
     * @param pFlag
     *            The new export flag.
     */
    public void setAllFlags(final ExportFlag pFlag) {
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
     * Get the limited elements id. If for a specific type of element, the flag
     * LIMITED is used, their id must be in this list.
     * 
     * @return The limited elements id.
     */
    public String[] getLimitedElementsId() {
        return limitedElementsId;
    }

    /**
     * Set the limited elements id.
     * 
     * @param pLimitedElementsId
     *            The new limited elements id.
     */
    public void setLimitedElementsId(final String[] pLimitedElementsId) {
        limitedElementsId = pLimitedElementsId;
    }

    /**
     * Get the limited types name. Export only the fields container (sheet type,
     * product type, link type) of specific types. If null or empty, no
     * limitation.
     * 
     * @return The limited types name.
     */
    public String[] getLimitedTypesName() {
        return limitedTypesName;
    }

    /**
     * Set the limited types name.
     * 
     * @param pLimitedTypesName
     *            The new limited types name.
     */
    public void setLimitedTypesName(final String[] pLimitedTypesName) {
        limitedTypesName = pLimitedTypesName;
    }

    /**
     * Get the limited products name. Only elements associated to these products
     * are exported. If null or empty, no limitation on products.
     * 
     * @return The limited products name.
     */
    public String[] getLimitedProductsName() {
        return limitedProductsName;
    }

    /**
     * Set the limited products name.
     * 
     * @param pLimitedProductsName
     *            The new limited products name.
     */
    public void setLimitedProductsName(final String[] pLimitedProductsName) {
        limitedProductsName = pLimitedProductsName;
    }

    /**
     * Get the path where the attached files will be exported. If null or empty,
     * the attached file are not exported.
     * 
     * @return The path where the attached files will be exported.
     */
    public String getAttachedFilePath() {
        return attachedFilePath;
    }

    /**
     * Set the path where the attached files will be exported.
     * 
     * @param pAttachedFilePath
     *            The new path where the attached files will be exported.
     */
    public void setAttachedFilePath(final String pAttachedFilePath) {
        attachedFilePath = pAttachedFilePath;
    }

    /**
     * Test if the sheet's revisions will exported too.
     * 
     * @return If the sheet's revisions will exported too.
     */
    public boolean isExportRevision() {
        return exportRevision;
    }

    /**
     * Set if the sheet's revisions will exported too.
     * 
     * @param pExportRevision
     *            If the sheet's revisions will exported too.
     */
    public void setExportRevision(final boolean pExportRevision) {
        exportRevision = pExportRevision;
    }

    /**
     * Test if UID are exported.
     * 
     * @return If UID are exported.
     */
    public boolean isExportUID() {
        return exportUID;
    }

    /**
     * Set if the UID are exported.
     * 
     * @param pExportUID
     *            The UID are exported.
     */
    public void setExportUID(final boolean pExportUID) {
        exportUID = pExportUID;
    }

    /**
     * Flag used to export a specific type of data.
     * 
     * @author tpanuel
     */
    public enum ExportFlag {
        // All elements of the type are exported
        ALL,
        // Only the elements where the id are present
        // on the exported elements id list are exported
        LIMITED,
        // No element of the type are exported
        NO
    }
}